package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.policies.LoggingRetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonTypeName("log")
public class LoggingRetryPolicyFactory implements RetryPolicyFactory {

    @NotNull
    @Valid
    private RetryPolicyFactory subPolicy;

    @JsonProperty
    public RetryPolicyFactory getSubPolicy() {
        return subPolicy;
    }

    @JsonProperty
    public void setSubPolicy(RetryPolicyFactory subPolicy) {
        this.subPolicy = subPolicy;
    }

    @Override
    public RetryPolicy build() {
        return new LoggingRetryPolicy(subPolicy.build());
    }
}
