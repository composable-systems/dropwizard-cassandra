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

import com.codahale.metrics.health.HealthCheck;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HealthCheck for a Cassandra Cluster.
 * <p/>
 * If a keyspace is specified, the health check attempts to initialise a Session with that keyspace; otherwise it
 * attempts to initialise a Session with the Cluster.
 */
public class CassandraHealthCheck extends HealthCheck {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraHealthCheck.class);
    private final SessionFactory sessionFactory;

    public CassandraHealthCheck(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    protected Result check() throws Exception {
        String keyspace = null;
        String clusterName = null;
        try (Session session = sessionFactory.create()) {
            clusterName = session.getCluster().getClusterName();
            keyspace = session.getLoggedKeyspace();
            return Result.healthy();
        } catch (Exception ex) {
            LOG.error("Unable to connect to Cassandra cluster [{}] with keyspace [{}]", clusterName, keyspace, ex);
            throw ex;
        }
    }
}
