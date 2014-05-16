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

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * A bundle for integrating with Cassandra.
 */
public abstract class CassandraBundle<C extends Configuration> implements ConfiguredBundle<C> {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraBundle.class);

    private Cluster cluster;
    private String keyspace;

    protected abstract CassandraFactory cassandraConfiguration(C configuration);

    @Override
    public void run(C configuration, Environment environment) throws Exception {
        CassandraFactory cassandraConfig = cassandraConfiguration(configuration);
        keyspace = cassandraConfig.getKeyspace();

        LOG.debug("Building {} Cassandra cluster", cassandraConfig.getClusterName());
        cluster = cassandraConfig.buildCluster();

        LOG.debug("Registering {} Cassandra cluster for lifecycle management", cassandraConfig.getClusterName());
        environment.lifecycle().manage(new CassandraManager(cluster, cassandraConfig.getShutdownGracePeriod()));

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

    public Session newSession() {
        return keyspace == null ? cluster.connect() : cluster.connect(keyspace);
    }
}
