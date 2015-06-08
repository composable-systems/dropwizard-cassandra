package org.stuartgunter.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LatencyAwarePolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import io.dropwizard.util.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LatencyAwarePolicy.class)
public class LatencyAwarePolicyFactoryTest {

    private final LoadBalancingPolicyFactory subPolicyFactory = mock(LoadBalancingPolicyFactory.class);
    private final LoadBalancingPolicy subPolicy = mock(LoadBalancingPolicy.class);
    private final LatencyAwarePolicy.Builder policyBuilder = mock(LatencyAwarePolicy.Builder.class);
    private final LatencyAwarePolicy resultingPolicy = mock(LatencyAwarePolicy.class);

    @Before
    public void setUp() throws Exception {
        when(subPolicyFactory.build()).thenReturn(subPolicy);
        mockStatic(LatencyAwarePolicy.class);
        when(LatencyAwarePolicy.builder(subPolicy)).thenReturn(policyBuilder);
        when(policyBuilder.build()).thenReturn(resultingPolicy);
    }

    @Test
    public void buildsPolicyWithNoParams() throws Exception {
        final LatencyAwarePolicyFactory factory = new LatencyAwarePolicyFactory();
        factory.setSubPolicy(subPolicyFactory);

        final LoadBalancingPolicy policy = factory.build();

        assertThat(policy).isSameAs(resultingPolicy);
        verify(subPolicyFactory).build();

        verify(policyBuilder, never()).withExclusionThreshold(anyDouble());
        verify(policyBuilder, never()).withMininumMeasurements(anyInt());
        verify(policyBuilder, never()).withRetryPeriod(anyLong(), any(TimeUnit.class));
        verify(policyBuilder, never()).withScale(anyLong(), any(TimeUnit.class));
        verify(policyBuilder, never()).withUpdateRate(anyLong(), any(TimeUnit.class));
        verify(policyBuilder).build();
    }

    @Test
    public void buildsPolicyWithAllParams() throws Exception {
        final LatencyAwarePolicyFactory factory = new LatencyAwarePolicyFactory();
        factory.setSubPolicy(subPolicyFactory);
        factory.setExclusionThreshold(1.0d);
        factory.setMinimumMeasurements(2);
        factory.setRetryPeriod(Duration.minutes(3));
        factory.setScale(Duration.milliseconds(100));
        factory.setUpdateRate(Duration.seconds(5));

        final LoadBalancingPolicy policy = factory.build();

        assertThat(policy).isSameAs(resultingPolicy);
        verify(subPolicyFactory).build();

        InOrder inOrder = inOrder(policyBuilder);
        inOrder.verify(policyBuilder).withExclusionThreshold(1.0d);
        inOrder.verify(policyBuilder).withMininumMeasurements(2);
        inOrder.verify(policyBuilder).withRetryPeriod(3L, TimeUnit.MINUTES);
        inOrder.verify(policyBuilder).withScale(100L, TimeUnit.MILLISECONDS);
        inOrder.verify(policyBuilder).withUpdateRate(5L, TimeUnit.SECONDS);
        inOrder.verify(policyBuilder).build();
    }
}