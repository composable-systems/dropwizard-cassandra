package systems.composable.dropwizard.cassandra.speculativeexecution;

import com.datastax.driver.core.policies.NoSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.SpeculativeExecutionPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A factory for configuring and building {@link NoSpeculativeExecutionPolicy} instances.
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
@JsonTypeName("none")
public class NoSpeculativeExecutionPolicyFactory implements SpeculativeExecutionPolicyFactory {
    @Override
    public SpeculativeExecutionPolicy build() {
        return NoSpeculativeExecutionPolicy.INSTANCE;
    }
}
