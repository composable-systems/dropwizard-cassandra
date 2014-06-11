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
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stuartgunter.dropwizard.cassandra.auth.AuthProviderFactory;
import org.stuartgunter.dropwizard.cassandra.pooling.PoolingOptionsFactory;
import org.stuartgunter.dropwizard.cassandra.reconnection.ReconnectionPolicyFactory;
import org.stuartgunter.dropwizard.cassandra.retry.RetryPolicyFactory;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.net.InetAddress;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * A factory for configuring the Cassandra bundle.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>clusterName</td>
 *         <td>Defaults to the cluster name provided by the Cassandra driver.</td>
 *         <td>The name of the cluster, as defined by the Cassandra driver; also used in metrics and health checks.</td>
 *     </tr>
 *     <tr>
 *         <td>keyspace</td>
 *         <td>No default. If provided, this will be included in the health check.</td>
 *         <td>The name of the keyspace to connect to.</td>
 *     </tr>
 *     <tr>
 *         <td>validationQuery</td>
 *         <td>SELECT * FROM system.schema_keyspaces</td>
 *         <td>The query to execute against the cluster to determine whether it is healthy.</td>
 *     </tr>
 *     <tr>
 *         <td>contactPoints</td>
 *         <td>No default. You must provide a list of contact points for the Cassandra driver.</td>
 *         <td></td>
 *     </tr>
 *     <tr>
 *         <td>port</td>
 *         <td>9042</td>
 *         <td>The port to use to connect to the Cassandra host.</td>
 *     </tr>
 *     <tr>
 *         <td>protocolVersion</td>
 *         <td>-1</td>
 *         <td>The native protocol version to use.</td>
 *     </tr>
 *     <tr>
 *         <td>compression</td>
 *         <td>NONE</td>
 *         <td>Sets the compression to use for the transport. Must a value in the {@link ProtocolOptions.Compression compression enum}.</td>
 *     </tr>
 *     <tr>
 *         <td>reconnectionPolicy</td>
 *         <td>No default.</td>
 *         <td>The {@link ReconnectionPolicyFactory reconnection policy} to use.</td>
 *     </tr>
 *     <tr>
 *         <td>authProvider</td>
 *         <td>No default.</td>
 *         <td>The {@link AuthProviderFactory auth provider} to use.</td>
 *     </tr>
 *     <tr>
 *         <td>retryPolicy</td>
 *         <td>No default.</td>
 *         <td>The {@link RetryPolicyFactory retry policy} to use.</td>
 *     </tr>
 *     <tr>
 *         <td>queryOptions</td>
 *         <td>No default.</td>
 *         <td>The {@link QueryOptions} to use.</td>
 *     </tr>
 *     <tr>
 *         <td>socketOptions</td>
 *         <td>No default.</td>
 *         <td>The {@link SocketOptions} to use</td>
 *     </tr>
 *     <tr>
 *         <td>poolingOptions</td>
 *         <td>No default.</td>
 *         <td>The {@link PoolingOptionsFactory pooling options} to use.</td>
 *     </tr>
 *     <tr>
 *         <td>metricsEnabled</td>
 *         <td>true</td>
 *         <td>Whether or not to enable metrics reporting.</td>
 *     </tr>
 *     <tr>
 *         <td>jmxEnabled</td>
 *         <td>false</td>
 *         <td>Whether or not to enable JMX metrics reporting. This should ideally remain disabled in a Dropwizard app,
 *         as metrics reporters should be configured via the {@code metrics} configuration option.</td>
 *     </tr>
 *     <tr>
 *         <td>shutdownGracePeriod</td>
 *         <td>30 seconds</td>
 *         <td>The time to wait while the cluster closes gracefully; after which, the cluster will be forcefully terminated.</td>
 *     </tr>
 * </table>
 */
public class CassandraFactory {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraFactory.class);

    private String clusterName;
    private String keyspace;

    @NotEmpty
    private String validationQuery = "SELECT * FROM system.schema_keyspaces";

    @NotEmpty
    private InetAddress[] contactPoints;

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
    private boolean jmxEnabled = false;

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
    public String getValidationQuery() {
        return validationQuery;
    }

    @JsonProperty
    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    @JsonProperty
    public InetAddress[] getContactPoints() {
        return contactPoints;
    }

    @JsonProperty
    public void setContactPoints(InetAddress[] contactPoints) {
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

    public Cluster build(Environment environment) {
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

        Cluster cluster = builder.build();

        LOG.debug("Registering {} Cassandra cluster for lifecycle management", cluster.getClusterName());
        environment.lifecycle().manage(new CassandraManager(cluster, getShutdownGracePeriod()));

        LOG.debug("Registering {} Cassandra health check", cluster.getClusterName());
        environment.healthChecks().register(name("cassandra", cluster.getClusterName()), new CassandraHealthCheck(cluster, validationQuery));

        if (isMetricsEnabled()) {
            LOG.debug("Registering {} Cassandra metrics", cluster.getClusterName());
            environment.metrics().registerAll(new CassandraMetricSet(cluster));
        }

        return cluster;
    }
}
