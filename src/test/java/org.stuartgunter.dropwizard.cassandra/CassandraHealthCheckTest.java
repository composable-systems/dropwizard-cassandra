package org.stuartgunter.dropwizard.cassandra;

import com.codahale.metrics.health.HealthCheck;
import com.datastax.driver.core.Cluster;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class CassandraHealthCheckTest {

    private final Cluster cluster = mock(Cluster.class);

    @Test
    public void isHealthyIfNoExceptionIsThrownOnConnectionToCluster() throws Exception {
        final CassandraHealthCheck healthCheck = new CassandraHealthCheck(cluster);

        final HealthCheck.Result result = healthCheck.execute();

        assertThat(result.isHealthy(), is(true));
        verify(cluster).connect();
        verifyNoMoreInteractions(cluster);
    }

    @Test
    public void isUnhealthyIfAnExceptionIsThrownOnFailedConnectionToCluster() throws Exception {
        when(cluster.connect()).thenThrow(new RuntimeException());
        final CassandraHealthCheck healthCheck = new CassandraHealthCheck(cluster);

        final HealthCheck.Result result = healthCheck.execute();

        assertThat(result.isHealthy(), is(false));
        verify(cluster).connect();
        verifyNoMoreInteractions(cluster);
    }

    @Test
    public void isHealthyIfNoExceptionIsThrownOnConnectionToKeyspace() throws Exception {
        final CassandraHealthCheck healthCheck = new CassandraHealthCheck(cluster, "keyspace");

        final HealthCheck.Result result = healthCheck.execute();

        assertThat(result.isHealthy(), is(true));
        verify(cluster).connect("keyspace");
        verifyNoMoreInteractions(cluster);
    }

    @Test
    public void isUnhealthyIfAnExceptionIsThrownOnFailedConnectionToKeyspace() throws Exception {
        when(cluster.connect(anyString())).thenThrow(new RuntimeException());
        final CassandraHealthCheck healthCheck = new CassandraHealthCheck(cluster, "keyspace");

        final HealthCheck.Result result = healthCheck.execute();

        assertThat(result.isHealthy(), is(false));
        verify(cluster).connect("keyspace");
        verifyNoMoreInteractions(cluster);
    }
}
