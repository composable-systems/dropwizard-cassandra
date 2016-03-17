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
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import io.dropwizard.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * HealthCheck for a Cassandra Cluster.
 * <p/>
 * The health check returns healthy if the {@link CassandraFactory#validationQuery validationQuery} succeeds.
 */
public class CassandraHealthCheck extends HealthCheck {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraHealthCheck.class);

    private final Session session;
    private final String validationQuery;
    private final Duration timeout;

    public CassandraHealthCheck(Cluster cluster, String validationQuery, Duration timeout) {
        this.session = cluster.connect();
        this.validationQuery = validationQuery;
        this.timeout = timeout;
    }

    @Override
    protected Result check() throws Exception {
        try {
            session.executeAsync(validationQuery).get(timeout.toMilliseconds(), TimeUnit.MILLISECONDS);
            return Result.healthy();
        } catch (Exception ex) {
            LOG.error("Unable to connect to Cassandra cluster [{}]", session.getCluster().getClusterName(), ex);
            throw ex;
        }
    }
}
