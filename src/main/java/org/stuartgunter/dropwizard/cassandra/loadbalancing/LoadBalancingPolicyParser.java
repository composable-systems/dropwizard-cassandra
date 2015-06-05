package org.stuartgunter.dropwizard.cassandra.loadbalancing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.datastax.driver.core.policies.LatencyAwarePolicy;
import com.datastax.driver.core.policies.LatencyAwarePolicy.Builder;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.google.common.collect.Lists;

public class LoadBalancingPolicyParser {
	
    public static LoadBalancingPolicy parseLbPolicy(String loadBalancingPolicyString) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
    	String lb_regex = "([a-zA-Z]*Policy)(\\()(.*)(\\))";
    	Pattern lb_pattern = Pattern.compile(lb_regex);
    	Matcher lb_matcher = lb_pattern.matcher(loadBalancingPolicyString);
    	
    	if(lb_matcher.matches()){
	    	if(lb_matcher.groupCount()>0){
	    		// Primary LB policy has been specified
	    		String primaryLoadBalancingPolicy = lb_matcher.group(1);
	    		String loadBalancingPolicyParams = lb_matcher.group(3);
	    		return getLbPolicy(primaryLoadBalancingPolicy, loadBalancingPolicyParams);
	    	}
    	}
    	
		return null;
    }
    
    public static LoadBalancingPolicy getLbPolicy(String lbString, String parameters) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    	LoadBalancingPolicy policy = null;
    	//LoadBalancingPolicy childPolicy = null;
    	if(!lbString.contains(".")){
			lbString="com.datastax.driver.core.policies."+ lbString;
		}
    	
    	if(parameters.length()>0){
    		// Child policy or parameters have been specified    		
    		String paramsRegex = "([^,]+\\(.+?\\))|([^,]+)";    
    		//String lb_regex = "([a-zA-Z]*Policy)(\\()(.*)(\\))";
    		Pattern param_pattern = Pattern.compile(paramsRegex);
        	Matcher lb_matcher = param_pattern.matcher(parameters);
        	        	
    		ArrayList<Object> paramList = Lists.newArrayList();
    		ArrayList<Class> primaryParametersClasses = Lists.newArrayList();
        	int nb=0;
        	while(lb_matcher.find()){
	        	if(lb_matcher.groupCount()>0){	        		
	        			try{
		        			if(lb_matcher.group().contains("(") && !lb_matcher.group().trim().startsWith("(")){
		        				// We are dealing with child policies here
		        				primaryParametersClasses.add(LoadBalancingPolicy.class);
		        				// Parse and add child policy to the parameter list
		        				paramList.add(parseLbPolicy(lb_matcher.group()));
		        				nb++;
		        			}else{
		        				// We are dealing with parameters that are not policies here
		        				String param = lb_matcher.group();
		        				if(param.contains("'") || param.contains("\"")){
		    	    				primaryParametersClasses.add(String.class);
		    	    				paramList.add(new String(param.trim().replace("'", "").replace("\"","")));
		    	    			}else if(param.contains(".") || param.toLowerCase().contains("(double)") || param.toLowerCase().contains("(float)")){
		    	    				// gotta allow using float or double
		    	    				if(param.toLowerCase().contains("(double)")){
		    	    					primaryParametersClasses.add(double.class);
		    	    					paramList.add(Double.parseDouble(param.replace("(double)","").trim()));
		    	    				}else{		    	    					
			    	    				primaryParametersClasses.add(float.class);
			    	    				paramList.add(Float.parseFloat(param.replace("(float)","").trim()));			    	    			
		    	    				}
		    	    			}else{
		    	    				if(param.toLowerCase().contains("(long)")){
		    	    					primaryParametersClasses.add(long.class);
		    	    					paramList.add(Long.parseLong(param.toLowerCase().replace("(long)","").trim()));
		    	    				}else{
		    	    					primaryParametersClasses.add(int.class);
		    	    					paramList.add(Integer.parseInt(param.toLowerCase().replace("(int)","").trim()));
		    	    				}
		    	    			}
		        				nb++;
		        			}
	        			}catch(Exception e){
	        				e.printStackTrace();
	        			}
	        		}	        		
	        }
        	        	
        	
        	if(nb>0){
        		// Instantiate load balancing policy with parameters
        		if(lbString.toLowerCase().contains("latencyawarepolicy")){
        			//special sauce for the latency aware policy which uses a builder subclass to instantiate
        			Builder builder = LatencyAwarePolicy.builder((LoadBalancingPolicy) paramList.get(0));
        			
                    builder.withExclusionThreshold((Double)paramList.get(1));
        			builder.withScale((Long)paramList.get(2), TimeUnit.MILLISECONDS);
        			builder.withRetryPeriod((Long)paramList.get(3), TimeUnit.MILLISECONDS);
        			builder.withUpdateRate((Long)paramList.get(4), TimeUnit.MILLISECONDS);
        			builder.withMininumMeasurements((Integer)paramList.get(5));
        			
        			return builder.build();
        			
        		}else{
	        		Class<?> clazz = Class.forName(lbString);
	        		Constructor<?> constructor = clazz.getConstructor(primaryParametersClasses.toArray(new Class[primaryParametersClasses.size()]));
	        		
	        		return (LoadBalancingPolicy) constructor.newInstance(paramList.toArray(new Object[paramList.size()]));
        		}
        	}else{
        		// Only one policy has been specified, with no parameter or child policy         		
        		Class<?> clazz = Class.forName(lbString);			
        		policy =  (LoadBalancingPolicy) clazz.newInstance();
    		
        		return policy;
        	
        	}        	
    			    	
    	}else{
    		// Only one policy has been specified, with no parameter or child policy 
    		
    		Class<?> clazz = Class.forName(lbString);			
    		policy =  (LoadBalancingPolicy) clazz.newInstance();
		
    		return policy;
    	
    	}
    	//return null;
    }

}
