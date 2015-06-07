package org.stuartgunter.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

/**
 * A service provider interface for creating DataStax {@link LoadBalancingPolicy load balancing policies}.
 * <p/>
 * To create your own, just:
 * <ol>
 *     <li>Create a class which implements {@link LoadBalancingPolicyFactory}.</li>
 *     <li>Annotate it with {@code @JsonTypeName} and give it a unique type name.</li>
 *     <li>Add a {@code META-INF/services/org.stuartgunter.dropwizard.cassandra.loadbalancing.LoadBalancingPolicyFactory}
 *     file with your implementation's full class name to the class path.</li>
 * </ol>
 *
 * @see DCAwareRoundRobinPolicyFactory
 * @see LatencyAwarePolicyFactory
 * @see RoundRobinPolicyFactory
 * @see TokenAwarePolicyFactory
 * @see WhiteListPolicyFactory
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface LoadBalancingPolicyFactory extends Discoverable {

    LoadBalancingPolicy build();
}
