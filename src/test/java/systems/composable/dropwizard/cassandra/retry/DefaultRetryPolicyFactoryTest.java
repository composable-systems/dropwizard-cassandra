package systems.composable.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.DefaultRetryPolicy;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultRetryPolicyFactoryTest {

    @Test
    public void returnsDefaultRetryPolicyInstance() throws Exception {
        final DefaultRetryPolicyFactory factory = new DefaultRetryPolicyFactory();

        final DefaultRetryPolicy policy = (DefaultRetryPolicy) factory.build();

        assertThat(policy).isSameAs(DefaultRetryPolicy.INSTANCE);
    }
}
