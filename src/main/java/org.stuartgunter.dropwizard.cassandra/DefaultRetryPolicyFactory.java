package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("default")
public class DefaultRetryPolicyFactory implements RetryPolicyFactory {
    @Override
    public RetryPolicy build() {
        return DefaultRetryPolicy.INSTANCE;
    }
}
