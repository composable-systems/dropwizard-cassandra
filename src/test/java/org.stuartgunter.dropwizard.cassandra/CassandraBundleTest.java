package org.stuartgunter.dropwizard.cassandra;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import io.dropwizard.Configuration;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CassandraBundleTest {

    private final CassandraConfiguration cassandraConfiguration = new CassandraConfiguration();
    private final Configuration configuration = mock(Configuration.class);
    private final Environment environment = mock(Environment.class);
    private final LifecycleEnvironment lifecycle = mock(LifecycleEnvironment.class);
    private final HealthCheckRegistry healthChecks = mock(HealthCheckRegistry.class);
    private final MetricRegistry metrics = mock(MetricRegistry.class);

    private final CassandraBundle<Configuration> bundle = new CassandraBundle<Configuration>() {
        @Override
        protected CassandraConfiguration cassandraConfiguration(Configuration configuration) {
            return cassandraConfiguration;
        }
    };

    @Before
    public void setUp() throws Exception {
        when(environment.lifecycle()).thenReturn(lifecycle);
        when(environment.metrics()).thenReturn(metrics);
        when(environment.healthChecks()).thenReturn(healthChecks);
    }

    @Test
    public void buildsCluster() throws Exception {
    }

    @Test
    public void registersHealthCheck() throws Exception {
    }

    @Test
    public void registersMetrics() throws Exception {
    }

    @Test
    public void managesClusterLifecycle() throws Exception {
    }

    @Test
    public void hasACluster() throws Exception {
    }
}
