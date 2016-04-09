package systems.composable.dropwizard.cassandra.reconnection;

import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;

import javax.validation.constraints.NotNull;

/**
 * A factory for configuring and building {@link ConstantReconnectionPolicy} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>delay</td>
 *         <td>No default. You must specify a delay.</td>
 *         <td>The constant delay between reconnection attempts.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("constant")
public class ConstantReconnectionPolicyFactory implements ReconnectionPolicyFactory {

    @NotNull
    private Duration delay;

    @JsonProperty
    public Duration getDelay() {
        return delay;
    }

    @JsonProperty
    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    @Override
    public ReconnectionPolicy build() {
        return new ConstantReconnectionPolicy(delay.toMilliseconds());
    }
}
