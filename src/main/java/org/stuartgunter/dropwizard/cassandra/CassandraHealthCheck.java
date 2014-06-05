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
import com.datastax.driver.core.Host;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HealthCheck for a Cassandra Cluster.
 * <p/>
 * The health check returns healthy if at least one node is considered up by the driver (see {@link com.datastax.driver.core.Host#isUp()})
 */
public class CassandraHealthCheck extends HealthCheck {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraHealthCheck.class);
    private final Cluster cluster;

    public CassandraHealthCheck(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    protected Result check() throws Exception {
        try {
            if (atLeastOneNodeIsUp(cluster)) {
                return Result.healthy();
            }
            return Result.unhealthy("Cassandra driver considers all nodes down");
        } catch (Exception ex) {
            LOG.error("Unable to connect to Cassandra cluster [{}]",
                    cluster.getClusterName(), ex);
            throw ex;
        }
    }

    protected boolean atLeastOneNodeIsUp(Cluster cluster) {
        return Iterables.any(cluster.getMetadata().getAllHosts(),
                new Predicate<Host>() {
                    @Override
                    public boolean apply(Host host) {
                        return host.isUp();
                    }
                });
    }
}
