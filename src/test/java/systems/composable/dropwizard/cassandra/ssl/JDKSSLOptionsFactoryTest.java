package systems.composable.dropwizard.cassandra.ssl;

import com.datastax.driver.core.JdkSSLOptions;
import com.datastax.driver.core.SSLOptions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JDKSSLOptionsFactoryTest {

    @Test
    public void returnsInstanceOfJdkSSLOptions() throws Exception {
        final JDKSSLOptionsFactory factory = new JDKSSLOptionsFactory();

        final SSLOptions options = factory.build();

        assertThat(options).isInstanceOf(JdkSSLOptions.class);
    }
}