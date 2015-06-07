package org.stuartgunter.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DCAwareRoundRobinPolicyFactoryTest {

    @Test
    public void buildsPolicyWithNoParams() throws Exception {
        final DCAwareRoundRobinPolicyFactory factory = new DCAwareRoundRobinPolicyFactory();

        final LoadBalancingPolicy policy = factory.build();

        assertThat(policy).isExactlyInstanceOf(DCAwareRoundRobinPolicy.class);
    }

    @Test
    public void buildsPolicyWithAllParams() throws Exception {
        final DCAwareRoundRobinPolicyFactory factory = new DCAwareRoundRobinPolicyFactory();
        factory.setLocalDC("dc1");
        factory.setUsedHostsPerRemoteDC(1);
        factory.setAllowRemoteDCsForLocalConsistencyLevel(true);

        final LoadBalancingPolicy policy = factory.build();

        assertThat(policy).isExactlyInstanceOf(DCAwareRoundRobinPolicy.class);
    }
}