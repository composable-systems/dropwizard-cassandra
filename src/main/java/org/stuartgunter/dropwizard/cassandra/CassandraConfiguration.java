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

import com.datastax.driver.core.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;
import org.stuartgunter.dropwizard.cassandra.auth.AuthProviderFactory;
import org.stuartgunter.dropwizard.cassandra.pooling.PoolingOptionsFactory;
import org.stuartgunter.dropwizard.cassandra.reconnection.ReconnectionPolicyFactory;
import org.stuartgunter.dropwizard.cassandra.retry.RetryPolicyFactory;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Root configuration class for CassandraBundle. Your application should contain an instance of this configuration
 * class in order to initialise the Bundle.
 */
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

    @Valid
    private ReconnectionPolicyFactory reconnectionPolicy;

    @Valid
    private AuthProviderFactory authProvider;

    @Valid
    private RetryPolicyFactory retryPolicy;

    private QueryOptions queryOptions;
    private SocketOptions socketOptions;

    @Valid
    private PoolingOptionsFactory poolingOptions;

    private boolean metricsEnabled = true;
    private boolean jmxEnabled = true;

    @NotNull
    private Duration shutdownGracePeriod = Duration.seconds(30);

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
    public ReconnectionPolicyFactory getReconnectionPolicy() {
        return reconnectionPolicy;
    }

    @JsonProperty
    public void setReconnectionPolicy(ReconnectionPolicyFactory reconnectionPolicy) {
        this.reconnectionPolicy = reconnectionPolicy;
    }

    @JsonProperty
    public AuthProviderFactory getAuthProvider() {
        return authProvider;
    }

    @JsonProperty
    public void setAuthProvider(AuthProviderFactory authProvider) {
        this.authProvider = authProvider;
    }

    @JsonProperty
    public RetryPolicyFactory getRetryPolicy() {
        return retryPolicy;
    }

    @JsonProperty
    public void setRetryPolicy(RetryPolicyFactory retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    @JsonProperty
    public QueryOptions getQueryOptions() {
        return queryOptions;
    }

    @JsonProperty
    public void setQueryOptions(QueryOptions queryOptions) {
        this.queryOptions = queryOptions;
    }

    @JsonProperty
    public SocketOptions getSocketOptions() {
        return socketOptions;
    }

    @JsonProperty
    public void setSocketOptions(SocketOptions socketOptions) {
        this.socketOptions = socketOptions;
    }

    @JsonProperty
    public PoolingOptionsFactory getPoolingOptions() {
        return poolingOptions;
    }

    @JsonProperty
    public void setPoolingOptions(PoolingOptionsFactory poolingOptions) {
        this.poolingOptions = poolingOptions;
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
    public Duration getShutdownGracePeriod() {
        return shutdownGracePeriod;
    }

    @JsonProperty
    public void setShutdownGracePeriod(Duration shutdownGracePeriod) {
        this.shutdownGracePeriod = shutdownGracePeriod;
    }

    public Cluster buildCluster() {
        final Cluster.Builder builder = Cluster.builder();
        builder.addContactPoints(contactPoints);
        builder.withPort(port);
        builder.withCompression(compression);
        builder.withProtocolVersion(protocolVersion);

        if (authProvider != null) {
            builder.withAuthProvider(authProvider.build());
        }

        if (reconnectionPolicy != null) {
            builder.withReconnectionPolicy(reconnectionPolicy.build());
        }

        if (retryPolicy != null) {
            builder.withRetryPolicy(retryPolicy.build());
        }

        if (queryOptions != null) {
            builder.withQueryOptions(queryOptions);
        }

        if (socketOptions != null) {
            builder.withSocketOptions(socketOptions);
        }

        if (poolingOptions != null) {
            builder.withPoolingOptions(poolingOptions.build());
        }

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
