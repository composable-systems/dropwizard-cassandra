package systems.composable.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A factory for configuring and building {@link DefaultRetryPolicy} instances.
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
@JsonTypeName("default")
public class DefaultRetryPolicyFactory implements RetryPolicyFactory {
    @Override
    public RetryPolicy build() {
        return DefaultRetryPolicy.INSTANCE;
    }
}
