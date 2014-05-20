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
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A bundle for integrating with Cassandra.
 */
public abstract class CassandraBundle<C extends Configuration> implements ConfiguredBundle<C> {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraBundle.class);

    private Cluster cluster;
    private SessionFactory sessionFactory;

    protected abstract CassandraFactory cassandraConfiguration(C configuration);

    @Override
    public void run(C configuration, Environment environment) throws Exception {
        final CassandraFactory cassandraConfig = cassandraConfiguration(configuration);

        LOG.debug("Building {} Cassandra cluster", cassandraConfig.getClusterName());
        cluster = cassandraConfig.build(environment);

        sessionFactory = new SessionFactory(cluster, cassandraConfig.getKeyspace());
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    public Cluster getCluster() {
        return cluster;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
