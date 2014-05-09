package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.CloseFuture;
import com.datastax.driver.core.Cluster;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CassandraManagerTest {

    private final Cluster cluster = mock(Cluster.class);
    private final CloseFuture closeFuture = mock(CloseFuture.class);
    private final CassandraManager manager = new CassandraManager(cluster, 5);

    @Before
    public void setUp() throws Exception {
        when(cluster.closeAsync()).thenReturn(closeFuture);
    }

    @Test
    public void gracefullyClosesClusterOnStop() throws Exception {
        manager.stop();

        verify(cluster).closeAsync();
        verify(closeFuture).get(5, TimeUnit.SECONDS);
    }

    @Test
    public void forcesClusterToCloseAfterTimeoutOnStop() throws Exception {
        when(closeFuture.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException());

        manager.stop();

        verify(cluster).closeAsync();
        verify(closeFuture).get(5, TimeUnit.SECONDS);
        verify(closeFuture).force();
    }
}
