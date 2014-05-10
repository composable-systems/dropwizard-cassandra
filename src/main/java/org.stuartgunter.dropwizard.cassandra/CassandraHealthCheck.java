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
