package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("downgradingConsistency")
public class DowngradingConsistencyRetryPolicyFactory implements RetryPolicyFactory {
    @Override
    public RetryPolicy build() {
        return DowngradingConsistencyRetryPolicy.INSTANCE;
    }
}
