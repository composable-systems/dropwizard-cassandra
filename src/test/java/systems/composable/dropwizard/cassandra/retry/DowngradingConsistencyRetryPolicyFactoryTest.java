package systems.composable.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DowngradingConsistencyRetryPolicyFactoryTest {

    @Test
    public void returnsDowngradingConsistencyRetryPolicyInstance() throws Exception {
        final DowngradingConsistencyRetryPolicyFactory factory = new DowngradingConsistencyRetryPolicyFactory();

        final DowngradingConsistencyRetryPolicy policy = (DowngradingConsistencyRetryPolicy) factory.build();

        assertThat(policy).isSameAs(DowngradingConsistencyRetryPolicy.INSTANCE);
    }
}
