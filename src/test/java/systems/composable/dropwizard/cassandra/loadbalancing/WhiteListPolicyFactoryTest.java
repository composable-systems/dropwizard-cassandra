package systems.composable.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.WhiteListPolicy;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WhiteListPolicyFactoryTest {

    private final LoadBalancingPolicyFactory subPolicyFactory = mock(LoadBalancingPolicyFactory.class);
    private final LoadBalancingPolicy subPolicy = mock(LoadBalancingPolicy.class);

    @Before
    public void setUp() throws Exception {
        when(subPolicyFactory.build()).thenReturn(subPolicy);
    }

    @Test
    public void buildsPolicy() throws Exception {
        final WhiteListPolicyFactory factory = new WhiteListPolicyFactory();
        factory.setSubPolicy(subPolicyFactory);
        factory.setWhiteList(Collections.singletonList(new InetSocketAddress("localhost", 9876)));

        final WhiteListPolicy policy = (WhiteListPolicy) factory.build();

        assertThat(policy.getChildPolicy()).isSameAs(subPolicy);
    }
}