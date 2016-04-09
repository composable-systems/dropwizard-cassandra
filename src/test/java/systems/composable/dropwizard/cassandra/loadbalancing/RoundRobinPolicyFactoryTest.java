package systems.composable.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoundRobinPolicyFactoryTest {

    @Test
    public void buildsPolicy() throws Exception {
        final RoundRobinPolicyFactory factory = new RoundRobinPolicyFactory();

        final LoadBalancingPolicy policy = factory.build();

        assertThat(policy).isExactlyInstanceOf(RoundRobinPolicy.class);
    }
}