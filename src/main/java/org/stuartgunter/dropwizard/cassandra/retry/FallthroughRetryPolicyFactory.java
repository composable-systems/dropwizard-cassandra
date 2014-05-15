package org.stuartgunter.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.FallthroughRetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A factory for configuring and building {@link FallthroughRetryPolicy} instances.
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
@JsonTypeName("fallthrough")
public class FallthroughRetryPolicyFactory implements RetryPolicyFactory {
    @Override
    public RetryPolicy build() {
        return FallthroughRetryPolicy.INSTANCE;
    }
}
