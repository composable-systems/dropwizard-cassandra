package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.policies.DefaultRetryPolicy;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class DefaultRetryPolicyFactoryTest {

    @Test
    public void returnsDefaultRetryPolicyInstance() throws Exception {
        final DefaultRetryPolicyFactory factory = new DefaultRetryPolicyFactory();

        final DefaultRetryPolicy policy = (DefaultRetryPolicy) factory.build();

        assertThat(policy, sameInstance(DefaultRetryPolicy.INSTANCE));
    }
}
