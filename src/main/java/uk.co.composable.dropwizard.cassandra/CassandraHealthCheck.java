package uk.co.composable.dropwizard.cassandra;

import com.codahale.metrics.health.HealthCheck;
import com.datastax.driver.core.Cluster;

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
        if (keyspace == null) {
            cluster.connect();
        } else {
            cluster.connect(keyspace);
        }
        return Result.healthy();
    }
}
