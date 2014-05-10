package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.Cluster;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codahale.metrics.MetricRegistry.name;

public abstract class CassandraBundle<C extends Configuration> implements ConfiguredBundle<C> {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraBundle.class);

    private Cluster cluster;

    protected abstract CassandraConfiguration cassandraConfiguration(C configuration);

    @Override
    public void run(C configuration, Environment environment) throws Exception {
        CassandraConfiguration cassandraConfig = cassandraConfiguration(configuration);

        LOG.debug("Building {} Cassandra cluster", cassandraConfig.getClusterName());
        cluster = cassandraConfig.buildCluster();

        LOG.debug("Registering {} Cassandra cluster for lifecycle management", cassandraConfig.getClusterName());
        environment.lifecycle().manage(new CassandraManager(cluster, cassandraConfig.getShutdownWaitSeconds()));

        LOG.debug("Registering {} Cassandra health check", cassandraConfig.getClusterName());
        environment.healthChecks().register(name("cassandra", cluster.getClusterName()), new CassandraHealthCheck(cluster, cassandraConfig.getKeyspace()));

        if (cassandraConfig.isMetricsEnabled()) {
            LOG.debug("Registering {} Cassandra metrics", cassandraConfig.getClusterName());
            environment.metrics().registerAll(new CassandraMetricSet(cluster));
        }
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    public Cluster getCluster() {
        return cluster;
    }
}
