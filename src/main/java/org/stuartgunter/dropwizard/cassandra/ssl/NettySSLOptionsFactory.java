package org.stuartgunter.dropwizard.cassandra.ssl;

import com.datastax.driver.core.NettySSLOptions;
import com.datastax.driver.core.SSLOptions;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;

import javax.net.ssl.SSLException;

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
 *         <td>No default. Uses standard behavior from Netty.</td>
 *         <td>The {@link SslContext} implementation to use, as specified through the {@link SslProvider} enum.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("netty")
public class NettySSLOptionsFactory implements SSLOptionsFactory {

    private SslProvider provider;

    @JsonProperty
    public SslProvider getProvider() {
        return provider;
    }

    @JsonProperty
    public void setProvider(SslProvider provider) {
        this.provider = provider;
    }

    @Override
    public SSLOptions build() {
        SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();

        if (provider != null) {
            sslContextBuilder.sslProvider(provider);
        }

        SslContext sslContext;
        try {
            sslContext = sslContextBuilder.build();
        } catch (SSLException e) {
            throw new RuntimeException("Unable to build SslContext for Netty", e);
        }

        return new NettySSLOptions(sslContext);
    }
}
