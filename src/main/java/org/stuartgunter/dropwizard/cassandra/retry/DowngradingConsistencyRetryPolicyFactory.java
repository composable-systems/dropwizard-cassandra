package org.stuartgunter.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A factory for configuring and building {@link DowngradingConsistencyRetryPolicy} instances.
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
@JsonTypeName("downgradingConsistency")
public class DowngradingConsistencyRetryPolicyFactory implements RetryPolicyFactory {
    @Override
    public RetryPolicy build() {
        return DowngradingConsistencyRetryPolicy.INSTANCE;
    }
}
