package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Provides a simple means of creating new sessions to Cassandra.
 */
public class SessionFactory {

    private final Cluster cluster;
    private final String keyspace;

    public SessionFactory(Cluster cluster, String keyspace) {
        this.cluster = cluster;
        this.keyspace = keyspace;
    }

    /**
     * Creates a new initialised {@link Session session} to the Cassandra cluster. If provided, the keyspace will
     * set on the Session before returning it.
     * @return An initialised session
     */
    public Session create() {
        return keyspace == null ? cluster.connect() : cluster.connect(keyspace);
    }
}
