package org.stuartgunter.dropwizard.cassandra;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metrics;
import io.dropwizard.Configuration;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

public class CassandraBundleTest {

    private final CassandraConfiguration cassandraConfiguration = mock(CassandraConfiguration.class);
    private final Configuration configuration = mock(Configuration.class);
    private final Cluster cluster = mock(Cluster.class);
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
        when(cassandraConfiguration.buildCluster()).thenReturn(cluster);
        when(cluster.getClusterName()).thenReturn("test-cluster");
    }

    @Test
    public void buildsCluster() throws Exception {
        bundle.run(configuration, environment);

        verify(cassandraConfiguration).buildCluster();
    }

    @Test
    public void registersHealthCheck() throws Exception {
        bundle.run(configuration, environment);

        verify(healthChecks).register(eq("cassandra.test-cluster"), isA(CassandraHealthCheck.class));
    }

    @Test
    public void registersMetricsWhenEnabled() throws Exception {
        Metrics clusterMetrics = mock(Metrics.class);
        MetricRegistry registry = mock(MetricRegistry.class);
        Map<String, Metric> driverMetrics = Collections.emptyMap();
        when(cluster.getMetrics()).thenReturn(clusterMetrics);
        when(clusterMetrics.getRegistry()).thenReturn(registry);
        when(registry.getMetrics()).thenReturn(driverMetrics);
        when(cassandraConfiguration.isMetricsEnabled()).thenReturn(true);

        bundle.run(configuration, environment);

        verify(metrics).registerAll(isA(CassandraMetricSet.class));
    }

    @Test
    public void doesNotRegistersMetricsWhenDisabled() throws Exception {
        bundle.run(configuration, environment);

        verifyZeroInteractions(metrics);
    }

    @Test
    public void managesClusterLifecycle() throws Exception {
        bundle.run(configuration, environment);

        verify(lifecycle).manage(isA(CassandraManager.class));
    }

    @Test
    public void providesACluster() throws Exception {
        bundle.run(configuration, environment);

        assertThat(bundle.getCluster(), sameInstance(cluster));
    }
}
