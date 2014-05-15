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
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.stuartgunter.dropwizard.cassandra.auth.AuthProviderFactory;
import org.stuartgunter.dropwizard.cassandra.pooling.PoolingOptionsFactory;
import org.stuartgunter.dropwizard.cassandra.reconnection.ReconnectionPolicyFactory;
import org.stuartgunter.dropwizard.cassandra.retry.RetryPolicyFactory;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Cluster.class)
public class CassandraFactoryTest {

    private final Cluster.Builder builder = mock(Cluster.Builder.class);
    private final Cluster cluster = mock(Cluster.class);
    private final AuthProviderFactory authProviderFactory = mock(AuthProviderFactory.class);
    private final AuthProvider authProvider = mock(AuthProvider.class);
    private final ReconnectionPolicyFactory reconnectionPolicyFactory = mock(ReconnectionPolicyFactory.class);
    private final ReconnectionPolicy reconnectionPolicy = mock(ReconnectionPolicy.class);
    private final RetryPolicyFactory retryPolicyFactory = mock(RetryPolicyFactory.class);
    private final RetryPolicy retryPolicy = mock(RetryPolicy.class);
    private final QueryOptions queryOptions = mock(QueryOptions.class);
    private final SocketOptions socketOptions = mock(SocketOptions.class);
    private final PoolingOptionsFactory poolingOptionsFactory = mock(PoolingOptionsFactory.class);
    private final PoolingOptions poolingOptions = mock(PoolingOptions.class);

    @Before
    public void setUp() throws Exception {
        mockStatic(Cluster.class);
        when(Cluster.builder()).thenReturn(builder);
        when(builder.build()).thenReturn(cluster);
        when(authProviderFactory.build()).thenReturn(authProvider);
        when(reconnectionPolicyFactory.build()).thenReturn(reconnectionPolicy);
        when(retryPolicyFactory.build()).thenReturn(retryPolicy);
        when(poolingOptionsFactory.build()).thenReturn(poolingOptions);
    }

    @Test
    public void buildsACluster() throws Exception {
        final CassandraFactory configuration = new CassandraFactory();
        configuration.setAuthProvider(authProviderFactory);
        configuration.setClusterName("test-cluster");
        configuration.setCompression(ProtocolOptions.Compression.LZ4);
        configuration.setContactPoints(new String[] {"host1", "host2"});
        configuration.setJmxEnabled(false);
        configuration.setMetricsEnabled(false);
        configuration.setPort(1234);
        configuration.setProtocolVersion(2);
        configuration.setReconnectionPolicy(reconnectionPolicyFactory);
        configuration.setRetryPolicy(retryPolicyFactory);
        configuration.setQueryOptions(queryOptions);
        configuration.setSocketOptions(socketOptions);
        configuration.setPoolingOptions(poolingOptionsFactory);

        final Cluster result = configuration.buildCluster();

        assertThat(result, sameInstance(cluster));
        verify(builder).addContactPoints("host1", "host2");
        verify(builder).withPort(1234);
        verify(builder).withCompression(ProtocolOptions.Compression.LZ4);
        verify(builder).withClusterName("test-cluster");
        verify(builder).withProtocolVersion(2);
        verify(builder).withoutJMXReporting();
        verify(builder).withoutMetrics();
        verify(builder).withAuthProvider(authProvider);
        verify(builder).withReconnectionPolicy(reconnectionPolicy);
        verify(builder).withRetryPolicy(retryPolicy);
        verify(builder).withQueryOptions(queryOptions);
        verify(builder).withSocketOptions(socketOptions);
        verify(builder).withPoolingOptions(poolingOptions);
        verify(builder).build();
        verifyNoMoreInteractions(builder);
    }
}
