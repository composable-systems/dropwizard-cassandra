package org.stuartgunter.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.stuartgunter.dropwizard.cassandra.retry.RetryPolicyFactory;

@JsonTypeName("downgradingConsistency")
public class DowngradingConsistencyRetryPolicyFactory implements RetryPolicyFactory {
    @Override
    public RetryPolicy build() {
        return DowngradingConsistencyRetryPolicy.INSTANCE;
    }
}
