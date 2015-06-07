package org.stuartgunter.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LatencyAwarePolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A factory for configuring and building {@link com.datastax.driver.core.policies.LatencyAwarePolicy} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>childPolicy</td>
 *         <td>No default. You must provide a child policy.</td>
 *         <td>The child policy that the resulting policy wraps.</td>
 *     </tr>
 *     <tr>
 *         <td>exclusionThreshold</td>
 *         <td>No default.</td>
 *         <td>The exclusion threshold to use for the resulting latency aware policy.</td>
 *     </tr>
 *     <tr>
 *         <td>minimumMeasurements</td>
 *         <td>No default.</td>
 *         <td>The minimum number of measurements per-host to consider for the resulting latency aware policy.</td>
 *     </tr>
 *     <tr>
 *         <td>retryPeriod</td>
 *         <td>No default.</td>
 *         <td>The retry period for the resulting latency aware policy.</td>
 *     </tr>
 *     <tr>
 *         <td>scale</td>
 *         <td>No default.</td>
 *         <td>The scale to use for the resulting latency aware policy.</td>
 *     </tr>
 *     <tr>
 *         <td>updateRate</td>
 *         <td>No default.</td>
 *         <td>The update rate for the resulting latency aware policy.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("latencyAware")
public class LatencyAwarePolicyFactory implements LoadBalancingPolicyFactory {

    @Valid
    @NotNull
    private LoadBalancingPolicyFactory childPolicy;

    private Double exclusionThreshold;
    private Integer minimumMeasurements;
    private Duration retryPeriod;
    private Duration scale;
    private Duration updateRate;

    @JsonProperty
    public LoadBalancingPolicyFactory getChildPolicy() {
        return childPolicy;
    }

    @JsonProperty
    public void setChildPolicy(LoadBalancingPolicyFactory childPolicy) {
        this.childPolicy = childPolicy;
    }

    @JsonProperty
    public Double getExclusionThreshold() {
        return exclusionThreshold;
    }

    @JsonProperty
    public void setExclusionThreshold(Double exclusionThreshold) {
        this.exclusionThreshold = exclusionThreshold;
    }

    @JsonProperty
    public Integer getMinimumMeasurements() {
        return minimumMeasurements;
    }

    @JsonProperty
    public void setMinimumMeasurements(Integer minimumMeasurements) {
        this.minimumMeasurements = minimumMeasurements;
    }

    @JsonProperty
    public Duration getRetryPeriod() {
        return retryPeriod;
    }

    @JsonProperty
    public void setRetryPeriod(Duration retryPeriod) {
        this.retryPeriod = retryPeriod;
    }

    @JsonProperty
    public Duration getScale() {
        return scale;
    }

    @JsonProperty
    public void setScale(Duration scale) {
        this.scale = scale;
    }

    @JsonProperty
    public Duration getUpdateRate() {
        return updateRate;
    }

    @JsonProperty
    public void setUpdateRate(Duration updateRate) {
        this.updateRate = updateRate;
    }

    @Override
    public LoadBalancingPolicy build() {
        LatencyAwarePolicy.Builder builder = LatencyAwarePolicy.builder(childPolicy.build());

        if (exclusionThreshold != null) {
            builder.withExclusionThreshold(exclusionThreshold);
        }

        if (minimumMeasurements != null) {
            builder.withMininumMeasurements(minimumMeasurements);
        }

        if (retryPeriod != null) {
            builder.withRetryPeriod(retryPeriod.getQuantity(), retryPeriod.getUnit());
        }

        if (scale != null) {
            builder.withScale(scale.getQuantity(), scale.getUnit());
        }

        if (updateRate != null) {
            builder.withUpdateRate(updateRate.getQuantity(), updateRate.getUnit());
        }

        return builder.build();
    }
}
