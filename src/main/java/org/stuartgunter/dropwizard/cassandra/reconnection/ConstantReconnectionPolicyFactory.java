package org.stuartgunter.dropwizard.cassandra.reconnection;

import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;
import org.stuartgunter.dropwizard.cassandra.reconnection.ReconnectionPolicyFactory;

import javax.validation.constraints.NotNull;

@JsonTypeName("constant")
public class ConstantReconnectionPolicyFactory implements ReconnectionPolicyFactory {

    @NotNull
    private Duration delay = Duration.milliseconds(0);

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
