package systems.composable.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.ErrorAwarePolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import io.dropwizard.util.Duration;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ErrorAwarePolicyFactoryTest {

    private final LoadBalancingPolicyFactory subPolicyFactory = mock(LoadBalancingPolicyFactory.class);
    private final LoadBalancingPolicy subPolicy = mock(LoadBalancingPolicy.class);

    @Before
    public void setUp() throws Exception {
        when(subPolicyFactory.build()).thenReturn(subPolicy);
    }

    @Test
    public void buildsPolicyWithChildPolicy() throws Exception {
        final ErrorAwarePolicyFactory factory = new ErrorAwarePolicyFactory();
        factory.setSubPolicy(subPolicyFactory);
        factory.setMaxErrorsPerMinute(5);
        factory.setRetryPeriod(Duration.days(7));

        final ErrorAwarePolicy policy = (ErrorAwarePolicy) factory.build();

        assertThat(policy.getChildPolicy()).isSameAs(subPolicy);
    }
}