package org.stuartgunter.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenAwarePolicyFactoryTest {

    private final LoadBalancingPolicyFactory childPolicyFactory = mock(LoadBalancingPolicyFactory.class);
    private final LoadBalancingPolicy childPolicy = mock(LoadBalancingPolicy.class);

    @Before
    public void setUp() throws Exception {
        when(childPolicyFactory.build()).thenReturn(childPolicy);
    }

    @Test
    public void buildsPolicyWithChildPolicy() throws Exception {
        final TokenAwarePolicyFactory factory = new TokenAwarePolicyFactory();
        factory.setChildPolicy(childPolicyFactory);

        final TokenAwarePolicy policy = (TokenAwarePolicy) factory.build();

        assertThat(policy.getChildPolicy()).isSameAs(childPolicy);
    }
}