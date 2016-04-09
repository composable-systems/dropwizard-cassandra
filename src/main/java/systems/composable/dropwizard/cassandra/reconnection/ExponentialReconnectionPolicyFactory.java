package systems.composable.dropwizard.cassandra.reconnection;

import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;

import javax.validation.constraints.NotNull;

/**
 * A factory for configuring and building {@link ExponentialReconnectionPolicy} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>baseDelay</td>
 *         <td>No default. You must specify a base delay.</td>
 *         <td>The base delay to use for the schedules created by this policy.</td>
 *     </tr>
 *     <tr>
 *         <td>maxDelay</td>
 *         <td>No default. You must specify a max delay.</td>
 *         <td>The maximum delay to wait between two attempts.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("exponential")
public class ExponentialReconnectionPolicyFactory implements ReconnectionPolicyFactory {

    @NotNull
    private Duration baseDelay;

    @NotNull
    private Duration maxDelay;

    @JsonProperty
    public Duration getBaseDelay() {
        return baseDelay;
    }

    @JsonProperty
    public void setBaseDelay(Duration baseDelay) {
        this.baseDelay = baseDelay;
    }

    @JsonProperty
    public Duration getMaxDelay() {
        return maxDelay;
    }

    @JsonProperty
    public void setMaxDelay(Duration maxDelay) {
        this.maxDelay = maxDelay;
    }

    @Override
    public ReconnectionPolicy build() {
        return new ExponentialReconnectionPolicy(baseDelay.toMilliseconds(), maxDelay.toMilliseconds());
    }
}
