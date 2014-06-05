/*
 * Copyright 2014 Stuart Gunter
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

package org.stuartgunter.dropwizard.cassandra;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.datastax.driver.core.Cluster;
import io.dropwizard.Configuration;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class CassandraBundleTest {

    private final CassandraFactory cassandraFactory = mock(CassandraFactory.class);
    private final Configuration configuration = mock(Configuration.class);
    private final Cluster cluster = mock(Cluster.class);
    private final Environment environment = mock(Environment.class);
    private final LifecycleEnvironment lifecycle = mock(LifecycleEnvironment.class);
    private final HealthCheckRegistry healthChecks = mock(HealthCheckRegistry.class);
    private final MetricRegistry metrics = mock(MetricRegistry.class);

    private final CassandraBundle<Configuration> bundle = new CassandraBundle<Configuration>() {
        @Override
        protected CassandraFactory cassandraConfiguration(Configuration configuration) {
            return cassandraFactory;
        }
    };

    @Before
    public void setUp() throws Exception {
        when(environment.lifecycle()).thenReturn(lifecycle);
        when(environment.metrics()).thenReturn(metrics);
        when(environment.healthChecks()).thenReturn(healthChecks);
        when(cassandraFactory.build(any(Environment.class))).thenReturn(cluster);
        when(cluster.getClusterName()).thenReturn("test-cluster");
    }

    @Test
    public void buildsCluster() throws Exception {
        bundle.run(configuration, environment);

        verify(cassandraFactory).build(eq(environment));
    }

    @Test
    public void providesACluster() throws Exception {
        bundle.run(configuration, environment);

        assertThat(bundle.getCluster()).isSameAs(cluster);
    }
}
