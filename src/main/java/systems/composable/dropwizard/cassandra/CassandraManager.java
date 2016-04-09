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
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Manages the lifecycle of the Cassandra Cluster instance, ensuring that it is appropriately
 * closed when the application terminates.
 */
public class CassandraManager implements Managed {

    private final Logger LOG = LoggerFactory.getLogger(CassandraManager.class);

    private final Cluster cluster;
    private final Duration shutdownGracePeriod;

    public CassandraManager(Cluster cluster, Duration shutdownGracePeriod) {
        this.cluster = cluster;
        this.shutdownGracePeriod = shutdownGracePeriod;
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        LOG.debug("Attempting graceful shutdown of Cassandra cluster: {}", cluster.getClusterName());
        CloseFuture future = cluster.closeAsync();
        try {
            future.get(shutdownGracePeriod.toMilliseconds(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            LOG.warn("Cassandra cluster did not close in {}. Forcing it now.", shutdownGracePeriod);
            future.force();
        }
    }
}
