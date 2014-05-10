package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolOptions;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private ProtocolOptions.Compression compression = ProtocolOptions.Compression.NONE;

    private boolean metricsEnabled = true;
    private boolean jmxEnabled = true;

    private int shutdownWaitSeconds = 20;

    @JsonProperty
    public String getClusterName() {
        return clusterName;
    }

    @JsonProperty
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    @JsonProperty
    public String getKeyspace() {
        return keyspace;
    }

    @JsonProperty
    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    @JsonProperty
    public String[] getContactPoints() {
        return contactPoints;
    }

    @JsonProperty
    public void setContactPoints(String[] contactPoints) {
        this.contactPoints = contactPoints;
    }

    @JsonProperty
    public int getPort() {
        return port;
    }

    @JsonProperty
    public void setPort(int port) {
        this.port = port;
    }

    @JsonProperty
    public int getProtocolVersion() {
        return protocolVersion;
    }

    @JsonProperty
    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @JsonProperty
    public ProtocolOptions.Compression getCompression() {
        return compression;
    }

    @JsonProperty
    public void setCompression(ProtocolOptions.Compression compression) {
        this.compression = compression;
    }

    @JsonProperty
    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    @JsonProperty
    public void setMetricsEnabled(boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
    }

    @JsonProperty
    public boolean isJmxEnabled() {
        return jmxEnabled;
    }

    @JsonProperty
    public void setJmxEnabled(boolean jmxEnabled) {
        this.jmxEnabled = jmxEnabled;
    }

    @JsonProperty
    public int getShutdownWaitSeconds() {
        return shutdownWaitSeconds;
    }

    @JsonProperty
    public void setShutdownWaitSeconds(int shutdownWaitSeconds) {
        this.shutdownWaitSeconds = shutdownWaitSeconds;
    }

    public Cluster buildCluster() {
        final Cluster.Builder builder = Cluster.builder();
        builder.addContactPoints(contactPoints);
        builder.withPort(port);
        builder.withCompression(compression);

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
