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

import com.datastax.driver.core.CloseFuture;
import com.datastax.driver.core.Cluster;
import io.dropwizard.util.Duration;
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
    private final CassandraManager manager = new CassandraManager(cluster, Duration.seconds(5));

    @Before
    public void setUp() throws Exception {
        when(cluster.closeAsync()).thenReturn(closeFuture);
    }

    @Test
    public void gracefullyClosesClusterOnStop() throws Exception {
        manager.stop();

        verify(cluster).closeAsync();
        verify(closeFuture).get(5000L, TimeUnit.MILLISECONDS);
    }

    @Test
    public void forcesClusterToCloseAfterTimeoutOnStop() throws Exception {
        when(closeFuture.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException());

        manager.stop();

        verify(cluster).closeAsync();
        verify(closeFuture).get(5000L, TimeUnit.MILLISECONDS);
        verify(closeFuture).force();
    }
}
