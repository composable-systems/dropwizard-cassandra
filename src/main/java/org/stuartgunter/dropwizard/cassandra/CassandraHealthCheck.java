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

public class CassandraHealthCheck extends HealthCheck {

    private final Cluster cluster;
    private final String keyspace;

    public CassandraHealthCheck(Cluster cluster) {
        this(cluster, null);
    }

    public CassandraHealthCheck(Cluster cluster, String keyspace) {
        this.cluster = cluster;
        this.keyspace = keyspace;
    }

    @Override
    protected Result check() throws Exception {
        return (keyspace == null) ? connectToCluster() : connectToKeyspace();
    }

    private Result connectToKeyspace() {
        try (Session session = cluster.connect(keyspace)) {
            return Result.healthy();
        }
    }

    private Result connectToCluster() {
        try (Session session = cluster.connect()) {
            return Result.healthy();
        }
    }
}
