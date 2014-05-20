package org.stuartgunter.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.FallthroughRetryPolicy;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FallthroughRetryPolicyFactoryTest {

    @Test
    public void returnsFallthroughRetryPolicyInstance() throws Exception {
        final FallthroughRetryPolicyFactory factory = new FallthroughRetryPolicyFactory();

        final FallthroughRetryPolicy policy = (FallthroughRetryPolicy) factory.build();

        assertThat(policy).isSameAs(FallthroughRetryPolicy.INSTANCE);
    }
}
