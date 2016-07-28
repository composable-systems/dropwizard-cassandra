/*
 * Copyright 2016 Composable Systems Limited
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package systems.composable.dropwizard.cassandra;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.datastax.driver.core.policies.SpeculativeExecutionPolicy;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import systems.composable.dropwizard.cassandra.auth.AuthProviderFactory;
import systems.composable.dropwizard.cassandra.loadbalancing.LoadBalancingPolicyFactory;
import systems.composable.dropwizard.cassandra.pooling.PoolingOptionsFactory;
import systems.composable.dropwizard.cassandra.reconnection.ReconnectionPolicyFactory;
import systems.composable.dropwizard.cassandra.retry.RetryPolicyFactory;
import systems.composable.dropwizard.cassandra.speculativeexecution.SpeculativeExecutionPolicyFactory;
import systems.composable.dropwizard.cassandra.ssl.SSLOptionsFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Cluster.class)
public class CassandraFactoryTest {

    private final Environment environment = mock(Environment.class);
    private final LifecycleEnvironment lifecycle = mock(LifecycleEnvironment.class);
    private final HealthCheckRegistry healthChecks = mock(HealthCheckRegistry.class);
    private final MetricRegistry metrics = mock(MetricRegistry.class);
    private final Cluster.Builder builder = mock(Cluster.Builder.class);
    private final Cluster cluster = mock(Cluster.class);
    private final SSLOptionsFactory sslOptionsFactory = mock(SSLOptionsFactory.class);
    private final SSLOptions sslOptions = mock(SSLOptions.class);
    private final AuthProviderFactory authProviderFactory = mock(AuthProviderFactory.class);
    private final AuthProvider authProvider = mock(AuthProvider.class);
    private final ReconnectionPolicyFactory reconnectionPolicyFactory = mock(ReconnectionPolicyFactory.class);
    private final ReconnectionPolicy reconnectionPolicy = mock(ReconnectionPolicy.class);
    private final RetryPolicyFactory retryPolicyFactory = mock(RetryPolicyFactory.class);
    private final RetryPolicy retryPolicy = mock(RetryPolicy.class);
    private final LoadBalancingPolicyFactory loadBalancingPolicyFactory = mock(LoadBalancingPolicyFactory.class);
    private final LoadBalancingPolicy loadBalancingPolicy = mock(LoadBalancingPolicy.class);
    private final SpeculativeExecutionPolicyFactory speculativeExecutionPolicyFactory = mock(SpeculativeExecutionPolicyFactory.class);
    private final SpeculativeExecutionPolicy speculativeExecutionPolicy = mock(SpeculativeExecutionPolicy.class);
    private final QueryOptions queryOptions = mock(QueryOptions.class);
    private final SocketOptions socketOptions = mock(SocketOptions.class);
    private final PoolingOptionsFactory poolingOptionsFactory = mock(PoolingOptionsFactory.class);
    private final PoolingOptions poolingOptions = mock(PoolingOptions.class);
    private final Map<String, Metric> driverMetrics = Collections.emptyMap();
    private final Metrics clusterMetrics = mock(Metrics.class);
    private final MetricRegistry driverRegistry = mock(MetricRegistry.class);

    @Before
    public void setUp() throws Exception {
        mockStatic(Cluster.class);
        when(environment.lifecycle()).thenReturn(lifecycle);
        when(environment.metrics()).thenReturn(metrics);
        when(environment.healthChecks()).thenReturn(healthChecks);
        when(Cluster.builder()).thenReturn(builder);
        when(builder.build()).thenReturn(cluster);
        when(sslOptionsFactory.build()).thenReturn(sslOptions);
        when(authProviderFactory.build()).thenReturn(authProvider);
        when(reconnectionPolicyFactory.build()).thenReturn(reconnectionPolicy);
        when(retryPolicyFactory.build()).thenReturn(retryPolicy);
        when(loadBalancingPolicyFactory.build()).thenReturn(loadBalancingPolicy);
        when(speculativeExecutionPolicyFactory.build()).thenReturn(speculativeExecutionPolicy);
        when(poolingOptionsFactory.build()).thenReturn(poolingOptions);
        when(cluster.getMetrics()).thenReturn(clusterMetrics);
        when(clusterMetrics.getRegistry()).thenReturn(driverRegistry);
        when(driverRegistry.getMetrics()).thenReturn(driverMetrics);
    }

    @Test
    public void buildsACluster() throws Exception {
        final CassandraFactory configuration = new CassandraFactory();
        configuration.setAuthProvider(Optional.of(authProviderFactory));
        configuration.setClusterName("test-cluster");
        configuration.setCompression(ProtocolOptions.Compression.LZ4);
        configuration.setContactPoints(new String[]{"localhost", "127.0.0.1"});
        configuration.setJmxEnabled(false);
        configuration.setMetricsEnabled(false);
        configuration.setPort(1234);
        configuration.setSsl(Optional.of(sslOptionsFactory));
        configuration.setMaxSchemaAgreementWait(Optional.of(Duration.seconds(90)));
        configuration.setProtocolVersion(Optional.of(ProtocolVersion.V2));
        configuration.setReconnectionPolicy(Optional.of(reconnectionPolicyFactory));
        configuration.setRetryPolicy(Optional.of(retryPolicyFactory));
        configuration.setLoadBalancingPolicy(Optional.of(loadBalancingPolicyFactory));
        configuration.setSpeculativeExecutionPolicy(Optional.of(speculativeExecutionPolicyFactory));
        configuration.setQueryOptions(Optional.of(queryOptions));
        configuration.setSocketOptions(Optional.of(socketOptions));
        configuration.setPoolingOptions(Optional.of(poolingOptionsFactory));

        final Cluster result = configuration.build(environment);

        assertThat(result).isSameAs(cluster);
        verify(builder).addContactPoints("localhost");
        verify(builder).addContactPoints("127.0.0.1");
        verify(builder).withPort(1234);
        verify(builder).withSSL(sslOptions);
        verify(builder).withMaxSchemaAgreementWaitSeconds(90);
        verify(builder).withCompression(ProtocolOptions.Compression.LZ4);
        verify(builder).withClusterName("test-cluster");
        verify(builder).withProtocolVersion(ProtocolVersion.V2);
        verify(builder).withoutJMXReporting();
        verify(builder).withoutMetrics();
        verify(builder).withAuthProvider(authProvider);
        verify(builder).withReconnectionPolicy(reconnectionPolicy);
        verify(builder).withRetryPolicy(retryPolicy);
        verify(builder).withLoadBalancingPolicy(loadBalancingPolicy);
        verify(builder).withSpeculativeExecutionPolicy(speculativeExecutionPolicy);
        verify(builder).withQueryOptions(queryOptions);
        verify(builder).withSocketOptions(socketOptions);
        verify(builder).withPoolingOptions(poolingOptions);
        verify(builder).build();
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void registersHealthCheck() throws Exception {
        final CassandraFactory configuration = configurationWithDefaultContactPoints();
        when(cluster.getClusterName()).thenReturn("test-cluster");

        final Cluster result = configuration.build(environment);
        assertThat(result).isNotNull();
        verify(healthChecks).register(eq("cassandra.test-cluster"), isA(CassandraHealthCheck.class));
    }

    @Test
    public void registersMetricsWhenEnabled() throws Exception {
        final CassandraFactory configuration = configurationWithDefaultContactPoints();
        configuration.setMetricsEnabled(true);

        final Cluster result = configuration.build(environment);
        assertThat(result).isNotNull();
        verify(metrics).registerAll(isA(CassandraMetricSet.class));
    }

    @Test
    public void doesNotRegistersMetricsWhenDisabled() throws Exception {
        final CassandraFactory configuration = configurationWithDefaultContactPoints();
        configuration.setMetricsEnabled(false);

        final Cluster result = configuration.build(environment);
        assertThat(result).isNotNull();
        verifyZeroInteractions(metrics);
    }

    @Test
    public void managesClusterLifecycle() throws Exception {
        final CassandraFactory configuration = configurationWithDefaultContactPoints();

        final Cluster result = configuration.build(environment);
        assertThat(result).isNotNull();
        verify(lifecycle).manage(isA(CassandraManager.class));
    }

    private CassandraFactory configurationWithDefaultContactPoints() {
        final CassandraFactory configuration = new CassandraFactory();
        configuration.setContactPoints(new String[] { "localhost" });
        return configuration;
    }
}
