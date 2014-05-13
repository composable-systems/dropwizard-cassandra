package org.stuartgunter.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.FallthroughRetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.stuartgunter.dropwizard.cassandra.retry.RetryPolicyFactory;

@JsonTypeName("fallthrough")
public class FallthroughRetryPolicyFactory implements RetryPolicyFactory {
    @Override
    public RetryPolicy build() {
        return FallthroughRetryPolicy.INSTANCE;
    }
}
