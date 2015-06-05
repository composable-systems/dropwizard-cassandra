package org.stuartgunter.dropwizard.cassandra.loadbalancing;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.LatencyAwarePolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;

public class LoadBalancingPolicyParserTest {

	@Test
	public void roundRobinPolicyTest() throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
    	String lbPolicyStr = "RoundRobinPolicy()";    	
    	assertTrue(LoadBalancingPolicyParser.parseLbPolicy(lbPolicyStr) instanceof RoundRobinPolicy);
	}
	
	@Test
	public void TokenAwarePolicyTest() throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String lbPolicyStr = "TokenAwarePolicy(RoundRobinPolicy())";
    	assertTrue(LoadBalancingPolicyParser.parseLbPolicy(lbPolicyStr) instanceof TokenAwarePolicy);
	}
	
	@Test
	public void DCAwareRoundRobinPolicyTest() throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String lbPolicyStr = "DCAwareRoundRobinPolicy(\"dc1\")";
    	System.out.println(lbPolicyStr);
    	assertTrue(LoadBalancingPolicyParser.parseLbPolicy(lbPolicyStr) instanceof DCAwareRoundRobinPolicy);
	}
	
	@Test
	public void TokenAwareDCAwarePolicyTest() throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String  lbPolicyStr = "TokenAwarePolicy(DCAwareRoundRobinPolicy(\"dc1\"))";
    	System.out.println(lbPolicyStr);
    	assertTrue(LoadBalancingPolicyParser.parseLbPolicy(lbPolicyStr) instanceof TokenAwarePolicy);
	}
	
	@Test
	public void failedPolicyParsingTest() throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {	
    	String lbPolicyStr = "TokenAwarePolicy";
    	System.out.println(lbPolicyStr);
    	assertTrue(LoadBalancingPolicyParser.parseLbPolicy(lbPolicyStr)==null);
	}
	
	@Test
	public void latencyAwarePolicyParsingTest() throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {	   
		String lbPolicyStr = "LatencyAwarePolicy(TokenAwarePolicy(RoundRobinPolicy()),(double) 10.5,(long) 1,(long) 10,(long)1,10)";    	
    	System.out.println(lbPolicyStr);
    	assertTrue(LoadBalancingPolicyParser.parseLbPolicy(lbPolicyStr) instanceof LatencyAwarePolicy);
	}

}
