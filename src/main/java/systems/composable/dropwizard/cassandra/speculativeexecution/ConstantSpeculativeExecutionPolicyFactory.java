package systems.composable.dropwizard.cassandra.speculativeexecution;

import com.datastax.driver.core.policies.ConstantSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.SpeculativeExecutionPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A factory for configuring and building {@link ConstantSpeculativeExecutionPolicy} instances.
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
 *         <td>No default. You must provide a value.</td>
 *         <td>The delay between each speculative execution. Must be strictly positive.</td>
 *     </tr>
 *     <tr>
 *         <td>maxSpeculativeExecutions</td>
 *         <td>No default. You must provide a value.</td>
 *         <td>The number of speculative executions. Must be strictly positive.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("constant")
public class ConstantSpeculativeExecutionPolicyFactory implements SpeculativeExecutionPolicyFactory {

    @NotNull
    private Duration delay;

    @NotNull
    @Min(1)
    private Integer maxSpeculativeExecutions;

    @JsonProperty
    public Duration getDelay() {
        return delay;
    }

    @JsonProperty
    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    @JsonProperty
    public Integer getMaxSpeculativeExecutions() {
        return maxSpeculativeExecutions;
    }

    @JsonProperty
    public void setMaxSpeculativeExecutions(Integer maxSpeculativeExecutions) {
        this.maxSpeculativeExecutions = maxSpeculativeExecutions;
    }

    @Override
    public SpeculativeExecutionPolicy build() {
        return new ConstantSpeculativeExecutionPolicy(delay.toMilliseconds(), maxSpeculativeExecutions);
    }
}
