package uk.co.composable.dropwizard.cassandra;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.datastax.driver.core.Cluster;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static com.codahale.metrics.MetricRegistry.name;

public class CassandraMetricSet implements MetricSet {

    private Map<String, Metric> metrics;

    public CassandraMetricSet(Cluster cluster) {
        final String clusterName = cluster.getClusterName();
        Map<String, Metric> driverMetrics = cluster.getMetrics().getRegistry().getMetrics();
        ImmutableMap.Builder<String, Metric> builder = ImmutableMap.builder();
        for (Map.Entry<String, Metric> metricEntry : driverMetrics.entrySet()) {
            builder.put(name(Cluster.class, clusterName, metricEntry.getKey()), metricEntry.getValue());
        }
        metrics = builder.build();
    }

    @Override
    public Map<String, Metric> getMetrics() {
        return metrics;
    }
}
