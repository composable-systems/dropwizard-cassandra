package systems.composable.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.ErrorAwarePolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A factory for configuring and building {@link ErrorAwarePolicy} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>subPolicy</td>
 *         <td>No default. You must provide a child policy.</td>
 *         <td>The load balancing policy to wrap with error awareness.</td>
 *     </tr>
 *     <tr>
 *         <td>maxErrorsPerMinute</td>
 *         <td>Inherits the default from {@link ErrorAwarePolicy.Builder}</td>
 *         <td>The maximum number of errors allowed per minute for each host.</td>
 *     </tr>
 *     <tr>
 *         <td>retryPeriod</td>
 *         <td>Inherits the default from {@link ErrorAwarePolicy.Builder}</td>
 *         <td>The time during which a host is excluded by the policy once it has exceeded {@link #maxErrorsPerMinute}</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("errorAware")
public class ErrorAwarePolicyFactory implements LoadBalancingPolicyFactory {

    @Valid
    @NotNull
    private LoadBalancingPolicyFactory subPolicy;

    private Integer maxErrorsPerMinute;

    private Duration retryPeriod;

    @JsonProperty
    public LoadBalancingPolicyFactory getSubPolicy() {
        return subPolicy;
    }

    @JsonProperty
    public void setSubPolicy(LoadBalancingPolicyFactory subPolicy) {
        this.subPolicy = subPolicy;
    }

    @JsonProperty
    public Integer getMaxErrorsPerMinute() {
        return maxErrorsPerMinute;
    }

    @JsonProperty
    public void setMaxErrorsPerMinute(Integer maxErrorsPerMinute) {
        this.maxErrorsPerMinute = maxErrorsPerMinute;
    }

    @JsonProperty
    public Duration getRetryPeriod() {
        return retryPeriod;
    }

    @JsonProperty
    public void setRetryPeriod(Duration retryPeriod) {
        this.retryPeriod = retryPeriod;
    }

    @Override
    public LoadBalancingPolicy build() {
        ErrorAwarePolicy.Builder builder = ErrorAwarePolicy.builder(subPolicy.build());

        if (maxErrorsPerMinute != null) {
            builder.withMaxErrorsPerMinute(maxErrorsPerMinute);
        }

        if (retryPeriod != null) {
            builder.withRetryPeriod(retryPeriod.getQuantity(), retryPeriod.getUnit());
        }

        return builder.build();
    }
}
