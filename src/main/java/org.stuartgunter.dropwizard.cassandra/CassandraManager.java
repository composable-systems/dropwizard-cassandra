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

import com.datastax.driver.core.CloseFuture;
import com.datastax.driver.core.Cluster;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CassandraManager implements Managed {

    private final Logger LOG = LoggerFactory.getLogger(CassandraManager.class);

    private final Cluster cluster;
    private final int shutdownWaitSeconds;

    public CassandraManager(Cluster cluster, int shutdownWaitSeconds) {
        this.cluster = cluster;
        this.shutdownWaitSeconds = shutdownWaitSeconds;
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        LOG.debug("Attempting graceful shutdown of Cassandra cluster: {}", cluster.getClusterName());
        CloseFuture future = cluster.closeAsync();
        try {
            future.get(shutdownWaitSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            LOG.warn("Cassandra cluster did not close in {} seconds. Forcing it now.", shutdownWaitSeconds);
            future.force();
        }
    }
}
