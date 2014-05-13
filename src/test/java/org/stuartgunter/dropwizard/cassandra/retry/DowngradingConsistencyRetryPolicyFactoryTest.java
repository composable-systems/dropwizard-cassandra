package org.stuartgunter.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import org.junit.Test;
import org.stuartgunter.dropwizard.cassandra.retry.DowngradingConsistencyRetryPolicyFactory;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class DowngradingConsistencyRetryPolicyFactoryTest {

    @Test
    public void returnsDowngradingConsistencyRetryPolicyInstance() throws Exception {
        final DowngradingConsistencyRetryPolicyFactory factory = new DowngradingConsistencyRetryPolicyFactory();

        final DowngradingConsistencyRetryPolicy policy = (DowngradingConsistencyRetryPolicy) factory.build();

        assertThat(policy, sameInstance(DowngradingConsistencyRetryPolicy.INSTANCE));
    }
}
