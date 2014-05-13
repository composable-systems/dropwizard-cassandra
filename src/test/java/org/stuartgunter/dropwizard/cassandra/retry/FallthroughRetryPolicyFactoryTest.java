package org.stuartgunter.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.FallthroughRetryPolicy;
import org.junit.Test;
import org.stuartgunter.dropwizard.cassandra.retry.FallthroughRetryPolicyFactory;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class FallthroughRetryPolicyFactoryTest {

    @Test
    public void returnsFallthroughRetryPolicyInstance() throws Exception {
        final FallthroughRetryPolicyFactory factory = new FallthroughRetryPolicyFactory();

        final FallthroughRetryPolicy policy = (FallthroughRetryPolicy) factory.build();

        assertThat(policy, sameInstance(FallthroughRetryPolicy.INSTANCE));
    }
}
