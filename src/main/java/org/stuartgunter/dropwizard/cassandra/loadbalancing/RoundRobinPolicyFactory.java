package org.stuartgunter.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A factory for configuring and building {@link com.datastax.driver.core.policies.RoundRobinPolicy} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr colspan="3">
 *         <td>No configuration options available.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("roundRobin")
public class RoundRobinPolicyFactory implements LoadBalancingPolicyFactory {
    @Override
    public LoadBalancingPolicy build() {
        return new RoundRobinPolicy();
    }
}
