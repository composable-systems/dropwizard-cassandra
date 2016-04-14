package systems.composable.dropwizard.cassandra.ssl;

import com.datastax.driver.core.NettySSLOptions;
import com.datastax.driver.core.SSLOptions;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.configuration.ConfigurationValidationException;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.ValidationMethod;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;

import javax.net.ssl.SSLException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;

/**
 * A factory for configuring and building {@link NettySSLOptions} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>provider</td>
 *         <td>No default.</td>
 *         <td>The {@link SslContext} implementation to use, as specified through the {@link SslProvider} enum.</td>
 *     </tr>
 *     <tr>
 *         <td>ciphers</td>
 *         <td>No default.</td>
 *         <td>The cipher suites to enable, in the order of preference.</td>
 *     </tr>
 *     <tr>
 *         <td>clientAuth</td>
 *         <td>No default.</td>
 *         <td>The client authentication mode.</td>
 *     </tr>
 *     <tr>
 *         <td>sessionCacheSize</td>
 *         <td>No default.</td>
 *         <td>The size of the cache used for storing SSL session objects.</td>
 *     </tr>
 *     <tr>
 *         <td>sessionTimeout</td>
 *         <td>No default.</td>
 *         <td>The timeout for the cached SSL session objects.</td>
 *     </tr>
 *     <tr>
 *         <td>trustCertChainFile</td>
 *         <td>No default.</td>
 *         <td>Trusted certificates for verifying the remote endpoint's certificate. The file should contain
 *         an X.509 certificate chain in PEM format.</td>
 *     </tr>
 *     <tr>
 *         <td>keyManager.keyCertChainFile</td>
 *         <td>No default.</td>
 *         <td>An X.509 certificate chain file in PEM format identifying this host.</td>
 *     </tr>
 *     <tr>
 *         <td>keyManager.keyFile</td>
 *         <td>No default.</td>
 *         <td>A PKCS#8 private key file in PEM format identifying this host.</td>
 *     </tr>
 *     <tr>
 *         <td>keyManager.keyPassword</td>
 *         <td>No default.</td>
 *         <td>The password of the keyFile.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("netty")
public class NettySSLOptionsFactory implements SSLOptionsFactory {

    private SslProvider provider;
    private List<String> ciphers;
    private ClientAuth clientAuth;
    private Long sessionCacheSize;
    private Duration sessionTimeout;
    private File trustCertChainFile;

    @Valid
    private KeyManagerConfig keyManager;

    @JsonProperty
    public SslProvider getProvider() {
        return provider;
    }

    @JsonProperty
    public void setProvider(SslProvider provider) {
        this.provider = provider;
    }

    @JsonProperty
    public List<String> getCiphers() {
        return ciphers;
    }

    @JsonProperty
    public void setCiphers(List<String> ciphers) {
        this.ciphers = ciphers;
    }

    @JsonProperty
    public ClientAuth getClientAuth() {
        return clientAuth;
    }

    @JsonProperty
    public void setClientAuth(ClientAuth clientAuth) {
        this.clientAuth = clientAuth;
    }

    @JsonProperty
    public Long getSessionCacheSize() {
        return sessionCacheSize;
    }

    @JsonProperty
    public void setSessionCacheSize(Long sessionCacheSize) {
        this.sessionCacheSize = sessionCacheSize;
    }

    @JsonProperty
    public Duration getSessionTimeout() {
        return sessionTimeout;
    }

    @JsonProperty
    public void setSessionTimeout(Duration sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    @JsonProperty
    public File getTrustCertChainFile() {
        return trustCertChainFile;
    }

    @JsonProperty
    public void setTrustCertChainFile(File trustCertChainFile) {
        this.trustCertChainFile = trustCertChainFile;
    }

    @JsonProperty
    public KeyManagerConfig getKeyManager() {
        return keyManager;
    }

    @JsonProperty
    public void setKeyManager(KeyManagerConfig keyManager) {
        this.keyManager = keyManager;
    }

    @ValidationMethod(message = "must define keyManager when clientAuth is REQUIRE")
    public boolean isClientAuthConfigValid() {
        return clientAuth != ClientAuth.REQUIRE || keyManager != null;
    }

    @Override
    public SSLOptions build() {
        SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();

        if (provider != null) {
            sslContextBuilder.sslProvider(provider);
        }

        if (ciphers != null) {
            sslContextBuilder.ciphers(ciphers);
        }

        if (clientAuth != null) {
            sslContextBuilder.clientAuth(clientAuth);
        }

        if (sessionCacheSize != null) {
            sslContextBuilder.sessionCacheSize(sessionCacheSize);
        }

        if (sessionTimeout != null) {
            sslContextBuilder.sessionTimeout(sessionTimeout.toSeconds());
        }

        if (trustCertChainFile != null) {
            sslContextBuilder.trustManager(trustCertChainFile);
        }

        if (keyManager != null) {
            sslContextBuilder.keyManager(
                    keyManager.getKeyCertChainFile(),
                    keyManager.getKeyFile(),
                    keyManager.getKeyPassword());
        }

        SslContext sslContext;
        try {
            sslContext = sslContextBuilder.build();
        } catch (SSLException e) {
            throw new RuntimeException("Unable to build Netty SslContext", e);
        }

        return new NettySSLOptions(sslContext);
    }

    static class KeyManagerConfig {

        @NotNull
        private File keyCertChainFile;

        @NotNull
        private File keyFile;

        private String keyPassword;

        @JsonProperty
        public File getKeyCertChainFile() {
            return keyCertChainFile;
        }

        @JsonProperty
        public void setKeyCertChainFile(File keyCertChainFile) {
            this.keyCertChainFile = keyCertChainFile;
        }

        @JsonProperty
        public File getKeyFile() {
            return keyFile;
        }

        @JsonProperty
        public void setKeyFile(File keyFile) {
            this.keyFile = keyFile;
        }

        @JsonProperty
        public String getKeyPassword() {
            return keyPassword;
        }

        @JsonProperty
        public void setKeyPassword(String keyPassword) {
            this.keyPassword = keyPassword;
        }
    }
}
