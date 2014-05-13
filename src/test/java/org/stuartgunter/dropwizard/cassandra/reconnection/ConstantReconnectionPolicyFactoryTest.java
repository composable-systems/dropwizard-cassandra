package org.stuartgunter.dropwizard.cassandra.reconnection;

import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import io.dropwizard.util.Duration;
import org.junit.Test;
import org.stuartgunter.dropwizard.cassandra.reconnection.ConstantReconnectionPolicyFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConstantReconnectionPolicyFactoryTest {

    @Test
    public void buildsPolicyWithDelayInMillis() throws Exception {
        final ConstantReconnectionPolicyFactory factory = new ConstantReconnectionPolicyFactory();
        factory.setDelay(Duration.seconds(5));

        final ConstantReconnectionPolicy policy = (ConstantReconnectionPolicy) factory.build();

        assertThat(policy.getConstantDelayMs(), is(5000L));
    }
}
