package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.policies.FallthroughRetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("fallthrough")
public class FallthroughRetryPolicyFactory implements RetryPolicyFactory {
    @Override
    public RetryPolicy build() {
        return FallthroughRetryPolicy.INSTANCE;
    }
}
