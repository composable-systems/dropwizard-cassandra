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

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metrics;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CassandraMetricSetTest {

    private final Cluster cluster = mock(Cluster.class);
    private final Metrics metrics = mock(Metrics.class);
    private final MetricRegistry registry = mock(MetricRegistry.class);
    private final Metric metricA = mock(Metric.class);
    private final Metric metricB = mock(Metric.class);
    private final Map<String, Metric> driverMetrics = ImmutableMap.of(
            "metricA", metricA,
            "metricB", metricB);

    @Before
    public void setUp() throws Exception {
        when(cluster.getClusterName()).thenReturn("test-cluster");
        when(cluster.getMetrics()).thenReturn(metrics);
        when(metrics.getRegistry()).thenReturn(registry);
        when(registry.getMetrics()).thenReturn(driverMetrics);
    }

    @Test
    public void createsMetricSetFromDriver() throws Exception {
        final CassandraMetricSet metricSet = new CassandraMetricSet(cluster);

        final Map<String, Metric> result = metricSet.getMetrics();

        assertThat(result, hasEntry("com.datastax.driver.core.Cluster.test-cluster.metricA", metricA));
        assertThat(result, hasEntry("com.datastax.driver.core.Cluster.test-cluster.metricB", metricB));
    }
}
