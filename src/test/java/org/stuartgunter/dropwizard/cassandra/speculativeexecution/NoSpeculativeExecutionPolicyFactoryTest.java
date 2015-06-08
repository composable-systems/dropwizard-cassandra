package org.stuartgunter.dropwizard.cassandra.speculativeexecution;

import com.datastax.driver.core.policies.NoSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.SpeculativeExecutionPolicy;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NoSpeculativeExecutionPolicyFactoryTest {

    @Test
    public void returnsSingletonInstance() throws Exception {
        final NoSpeculativeExecutionPolicyFactory factory = new NoSpeculativeExecutionPolicyFactory();

        final SpeculativeExecutionPolicy policy = factory.build();

        assertThat(policy).isSameAs(NoSpeculativeExecutionPolicy.INSTANCE);
    }
}