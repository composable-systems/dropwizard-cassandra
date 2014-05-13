package org.stuartgunter.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.stuartgunter.dropwizard.cassandra.retry.RetryPolicyFactory;

@JsonTypeName("default")
public class DefaultRetryPolicyFactory implements RetryPolicyFactory {
    @Override
    public RetryPolicy build() {
        return DefaultRetryPolicy.INSTANCE;
    }
}
