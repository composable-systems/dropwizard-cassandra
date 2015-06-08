package org.stuartgunter.dropwizard.cassandra.speculativeexecution;

import com.datastax.driver.core.policies.ConstantSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.SpeculativeExecutionPolicy;
import io.dropwizard.util.Duration;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstantSpeculativeExecutionPolicyFactoryTest {

    @Test
    public void buildsPolicyWithConfiguredValues() throws Exception {
        final ConstantSpeculativeExecutionPolicyFactory factory = new ConstantSpeculativeExecutionPolicyFactory();
        factory.setDelay(Duration.seconds(5));
        factory.setMaxSpeculativeExecutions(2);

        final SpeculativeExecutionPolicy policy = factory.build();

        assertThat(policy).isExactlyInstanceOf(ConstantSpeculativeExecutionPolicy.class);
    }
}