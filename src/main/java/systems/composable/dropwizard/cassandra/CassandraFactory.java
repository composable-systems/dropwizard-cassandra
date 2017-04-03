/*
 * Copyright 2016 Composable Systems Limited
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

package systems.composable.dropwizard.cassandra;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.datastax.driver.core.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import systems.composable.dropwizard.cassandra.auth.AuthProviderFactory;
import systems.composable.dropwizard.cassandra.connect.AddressTranslatorFactory;
import systems.composable.dropwizard.cassandra.loadbalancing.LoadBalancingPolicyFactory;
import systems.composable.dropwizard.cassandra.pooling.PoolingOptionsFactory;
import systems.composable.dropwizard.cassandra.reconnection.ReconnectionPolicyFactory;
import systems.composable.dropwizard.cassandra.retry.RetryPolicyFactory;
import systems.composable.dropwizard.cassandra.speculativeexecution.SpeculativeExecutionPolicyFactory;
import systems.composable.dropwizard.cassandra.ssl.SSLOptionsFactory;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

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
 *         <td>SELECT key FROM system.local</td>
 *         <td>The query to execute against the cluster to determine whether it is healthy.</td>
 *     </tr>
 *     <tr>
 *         <td>healthCheckTimeout</td>
 *         <td>2 seconds.</td>
 *         <td>Sets the maximum time to wait for the validation query to respond.</td>
 *     </tr>
 *     <tr>
 *         <td>contactPoints</td>
 *         <td>No default. You must provide a list of contact points for the Cassandra driver.</td>
 *         <td>Each contact point can be a DNS record resolving to multiple hosts. In this case all of them will be added to the {@link Cluster}.</td>
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
 *         <td>maxSchemaAgreementWait</td>
 *         <td>No default.</td>
 *         <td>Sets the maximum time to wait for schema agreement before returning from a DDL query.</td>
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
 *         <td>loadBalancingPolicy</td>
 *         <td>No default.</td>
 *         <td>The {@link LoadBalancingPolicyFactory load balancing policy} to use.</td>
 *     </tr>
 *     <tr>
 *         <td>speculativeExecutionPolicy</td>
 *         <td>No default.</td>
 *         <td>The {@link SpeculativeExecutionPolicyFactory speculative execution policy} to use.</td>
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
 *     <tr>
 *         <td>addressTranslator</td>
 *         <td>No default.</td>
 *         <td>The {@link com.datastax.driver.core.policies.AddressTranslator} to use.</td>
 *     </tr>
 * </table>
 */
public class CassandraFactory {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraFactory.class);

    private String clusterName;
    private String keyspace;

    @NotEmpty
    private String validationQuery = "SELECT key FROM system.local";

    @NotEmpty
    private String[] contactPoints;

    @Min(1)
    private int port = ProtocolOptions.DEFAULT_PORT;

    private Optional<ProtocolVersion> protocolVersion = Optional.empty();

    @Valid
    private Optional<SSLOptionsFactory> ssl = Optional.empty();

    @NotNull
    private ProtocolOptions.Compression compression = ProtocolOptions.Compression.NONE;

    private Optional<Duration> maxSchemaAgreementWait = Optional.empty();

    @Valid
    private Optional<ReconnectionPolicyFactory> reconnectionPolicy = Optional.empty();

    @Valid
    private Optional<AuthProviderFactory> authProvider = Optional.empty();

    @Valid
    private Optional<RetryPolicyFactory> retryPolicy = Optional.empty();

    @Valid
    private Optional<LoadBalancingPolicyFactory> loadBalancingPolicy = Optional.empty();

    @Valid
    private Optional<SpeculativeExecutionPolicyFactory> speculativeExecutionPolicy = Optional.empty();

    private Optional<QueryOptions> queryOptions = Optional.empty();
    private Optional<SocketOptions> socketOptions = Optional.empty();

    @Valid
    private Optional<PoolingOptionsFactory> poolingOptions = Optional.empty();

    @Valid
    private Optional<AddressTranslatorFactory> addressTranslator = Optional.empty();

    private boolean metricsEnabled = true;
    private boolean jmxEnabled = false;

    @NotNull
    private Duration shutdownGracePeriod = Duration.seconds(30);

    @NotNull
    private Duration healthCheckTimeout = Duration.seconds(2);

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
    public Optional<ProtocolVersion> getProtocolVersion() {
        return protocolVersion;
    }

    @JsonProperty
    public void setProtocolVersion(Optional<ProtocolVersion> protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @JsonProperty
    public Optional<SSLOptionsFactory> getSsl() {
        return ssl;
    }

    @JsonProperty
    public void setSsl(Optional<SSLOptionsFactory> ssl) {
        this.ssl = ssl;
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
    public void setMaxSchemaAgreementWait(Optional<Duration> maxSchemaAgreementWait) {
        this.maxSchemaAgreementWait = maxSchemaAgreementWait;
    }

    @JsonProperty
    public Optional<ReconnectionPolicyFactory> getReconnectionPolicy() {
        return reconnectionPolicy;
    }

    @JsonProperty
    public void setReconnectionPolicy(Optional<ReconnectionPolicyFactory> reconnectionPolicy) {
        this.reconnectionPolicy = reconnectionPolicy;
    }

    @JsonProperty
    public Optional<AuthProviderFactory> getAuthProvider() {
        return authProvider;
    }

    @JsonProperty
    public void setAuthProvider(Optional<AuthProviderFactory> authProvider) {
        this.authProvider = authProvider;
    }

    @JsonProperty
    public Optional<RetryPolicyFactory> getRetryPolicy() {
        return retryPolicy;
    }

    @JsonProperty
    public void setRetryPolicy(Optional<RetryPolicyFactory> retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    @JsonProperty
    public Optional<LoadBalancingPolicyFactory> getLoadBalancingPolicy() {
        return loadBalancingPolicy;
    }

    @JsonProperty
    public void setLoadBalancingPolicy(Optional<LoadBalancingPolicyFactory> loadBalancingPolicy) {
        this.loadBalancingPolicy = loadBalancingPolicy;
    }

    @JsonProperty
    public Optional<SpeculativeExecutionPolicyFactory> getSpeculativeExecutionPolicy() {
        return speculativeExecutionPolicy;
    }

    @JsonProperty
    public void setSpeculativeExecutionPolicy(Optional<SpeculativeExecutionPolicyFactory> speculativeExecutionPolicy) {
        this.speculativeExecutionPolicy = speculativeExecutionPolicy;
    }

    @JsonProperty
    public Optional<QueryOptions> getQueryOptions() {
        return queryOptions;
    }

    @JsonProperty
    public void setQueryOptions(Optional<QueryOptions> queryOptions) {
        this.queryOptions = queryOptions;
    }

    @JsonProperty
    public Optional<SocketOptions> getSocketOptions() {
        return socketOptions;
    }

    @JsonProperty
    public void setSocketOptions(Optional<SocketOptions> socketOptions) {
        this.socketOptions = socketOptions;
    }

    @JsonProperty
    public Optional<PoolingOptionsFactory> getPoolingOptions() {
        return poolingOptions;
    }

    @JsonProperty
    public void setPoolingOptions(Optional<PoolingOptionsFactory> poolingOptions) {
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

    @JsonProperty
    public Duration getHealthCheckTimeout() {
        return healthCheckTimeout;
    }

    @JsonProperty
    public void setHealthCheckTimeout(Duration healthCheckTimeout) {
        this.healthCheckTimeout = healthCheckTimeout;
    }

    @JsonProperty
    public Optional<AddressTranslatorFactory> getAddressTranslator() {
        return addressTranslator;
    }

    @JsonProperty
    public void setAddressTranslator(Optional<AddressTranslatorFactory> addressTranslator) {
        this.addressTranslator = addressTranslator;
    }


    /**
     * Builds a {@link Cluster} instance for the given {@link Environment}.
     * <p/>
     * The {@code environment} will be used for lifecycle management, as well as metrics and
     * health-checks.
     *
     * @param environment the environment to manage the lifecycle, metrics and health-checks.
     * @return a fully configured and managed {@link Cluster}.
     */
    public Cluster build(Environment environment) {
        final Cluster cluster = build(environment.metrics(), environment.healthChecks());

        LOG.debug("Registering {} Cassandra cluster for lifecycle management", cluster.getClusterName());
        environment.lifecycle().manage(new CassandraManager(cluster, getShutdownGracePeriod()));

        return cluster;
    }

    /**
     * Builds a {@link Cluster} instance.
     * <p/>
     * The {@link MetricRegistry} will be used to register client metrics, and the {@link
     * HealthCheckRegistry} to register client health-checks.
     *
     * @param metrics the registry to register client metrics.
     * @param healthChecks the registry to register client health-checks.
     * @return a fully configured {@link Cluster}.
     */
    public Cluster build(MetricRegistry metrics, HealthCheckRegistry healthChecks) {

        final Cluster.Builder builder = Cluster.builder();

        for (String contactPoint : contactPoints) {
            builder.addContactPoints(contactPoint);
        }

        builder.withPort(port);
        builder.withCompression(compression);

        protocolVersion.ifPresent(builder::withProtocolVersion);
        ssl.map(SSLOptionsFactory::build).ifPresent(builder::withSSL);
        maxSchemaAgreementWait.map(Duration::toSeconds).map(Long::intValue).ifPresent(builder::withMaxSchemaAgreementWaitSeconds);
        authProvider.map(AuthProviderFactory::build).ifPresent(builder::withAuthProvider);
        reconnectionPolicy.map(ReconnectionPolicyFactory::build).ifPresent(builder::withReconnectionPolicy);
        retryPolicy.map(RetryPolicyFactory::build).ifPresent(builder::withRetryPolicy);
        loadBalancingPolicy.map(LoadBalancingPolicyFactory::build).ifPresent(builder::withLoadBalancingPolicy);
        speculativeExecutionPolicy.map(SpeculativeExecutionPolicyFactory::build).ifPresent(builder::withSpeculativeExecutionPolicy);
        queryOptions.ifPresent(builder::withQueryOptions);
        socketOptions.ifPresent(builder::withSocketOptions);
        poolingOptions.map(PoolingOptionsFactory::build).ifPresent(builder::withPoolingOptions);
        addressTranslator.map(AddressTranslatorFactory::build).ifPresent(builder::withAddressTranslator);

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

        LOG.debug("Registering {} Cassandra health check", cluster.getClusterName());
        CassandraHealthCheck healthCheck = new CassandraHealthCheck(cluster, validationQuery, healthCheckTimeout);
        healthChecks.register(name("cassandra", cluster.getClusterName()), healthCheck);

        if (isMetricsEnabled()) {
            LOG.debug("Registering {} Cassandra metrics", cluster.getClusterName());
            metrics.registerAll(new CassandraMetricSet(cluster));
        }

        return cluster;
    }
}
