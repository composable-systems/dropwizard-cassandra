package org.stuartgunter.dropwizard.cassandra.reconnection;

import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
import io.dropwizard.util.Duration;
import org.junit.Test;
import org.stuartgunter.dropwizard.cassandra.reconnection.ExponentialReconnectionPolicyFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExponentialReconnectionPolicyFactoryTest {

    @Test
    public void buildsPolicyWithDelayAndMaxInMillis() throws Exception {
        final ExponentialReconnectionPolicyFactory factory = new ExponentialReconnectionPolicyFactory();
        factory.setBaseDelay(Duration.seconds(4));
        factory.setMaxDelay(Duration.seconds(7));

        final ExponentialReconnectionPolicy policy = (ExponentialReconnectionPolicy) factory.build();

        assertThat(policy.getBaseDelayMs(), is(4000L));
        assertThat(policy.getMaxDelayMs(), is(7000L));
    }
}
