package uk.co.composable.dropwizard.cassandra;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolOptions;
import com.google.common.base.Strings;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CassandraConfiguration {

    private String clusterName;
    private String keyspace;

    @NotEmpty
    private String[] contactPoints;

    @Min(1)
    private int port = ProtocolOptions.DEFAULT_PORT;

    @Max(2)
    private int protocolVersion = -1;

    @NotNull
    private AuthProvider authProvider = AuthProvider.NONE;

    @NotNull
    private ProtocolOptions.Compression compression = ProtocolOptions.Compression.NONE;
    private boolean metricsEnabled = true;
    private boolean jmxEnabled = true;

    private int shutdownWaitSeconds = 20;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public int getShutdownWaitSeconds() {
        return shutdownWaitSeconds;
    }

    public void setShutdownWaitSeconds(int shutdownWaitSeconds) {
        this.shutdownWaitSeconds = shutdownWaitSeconds;
    }

    public Cluster buildCluster() {
        Cluster.Builder builder = Cluster.builder()
                .addContactPoints(contactPoints)
                .withPort(port)
                .withAuthProvider(authProvider)
                .withCompression(compression);

        if (!metricsEnabled) {
            builder.withoutMetrics();
        }

        if (!jmxEnabled) {
            builder.withoutJMXReporting();
        }

        if (!Strings.isNullOrEmpty(clusterName)) {
            builder.withClusterName(clusterName);
        }

        return builder.build();
    }
}
