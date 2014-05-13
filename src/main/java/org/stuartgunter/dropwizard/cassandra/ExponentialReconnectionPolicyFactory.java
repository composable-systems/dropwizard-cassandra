package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;

import javax.validation.constraints.NotNull;

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
