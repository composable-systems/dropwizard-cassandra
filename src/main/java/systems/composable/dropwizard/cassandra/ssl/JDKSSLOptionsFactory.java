package systems.composable.dropwizard.cassandra.ssl;

import com.datastax.driver.core.JdkSSLOptions;
import com.datastax.driver.core.SSLOptions;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A factory for configuring and building {@link JdkSSLOptions} instances. Uses default settings.
 */
@JsonTypeName("jdk")
public class JDKSSLOptionsFactory implements SSLOptionsFactory {

    @Override
    public SSLOptions build() {
        return JdkSSLOptions.builder().build();
    }
}
