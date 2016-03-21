package org.stuartgunter.dropwizard.cassandra.ssl;

import com.datastax.driver.core.NettySSLOptions;
import com.datastax.driver.core.SSLOptions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NettySSLOptionsFactoryTest {

    @Test
    public void returnsInstanceOfNettySSLOptions() throws Exception {
        final NettySSLOptionsFactory factory = new NettySSLOptionsFactory();

        final SSLOptions options = factory.build();

        assertThat(options).isInstanceOf(NettySSLOptions.class);
    }
}