package org.stuartgunter.dropwizard.cassandra.reconnection;

import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
import io.dropwizard.util.Duration;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExponentialReconnectionPolicyFactoryTest {

    @Test
    public void buildsPolicyWithDelayAndMaxInMillis() throws Exception {
        final ExponentialReconnectionPolicyFactory factory = new ExponentialReconnectionPolicyFactory();
        factory.setBaseDelay(Duration.seconds(4));
        factory.setMaxDelay(Duration.seconds(7));

        final ExponentialReconnectionPolicy policy = (ExponentialReconnectionPolicy) factory.build();

        assertThat(policy.getBaseDelayMs()).isEqualTo(4000L);
        assertThat(policy.getMaxDelayMs()).isEqualTo(7000L);
    }
}
