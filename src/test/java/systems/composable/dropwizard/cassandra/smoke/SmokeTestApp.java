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

package systems.composable.dropwizard.cassandra.smoke;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import systems.composable.dropwizard.cassandra.CassandraResource;

public class SmokeTestApp extends Application<SmokeTestConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(SmokeTestApp.class);

    @Override
    public void initialize(Bootstrap<SmokeTestConfiguration> bootstrap) {
        // Prevents NoHostAvailable test errors
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(SmokeTestConfiguration configuration, Environment environment) throws Exception {
        LOG.debug("Running smoke test for {}", configuration.getCassandraConfig().getClusterName());

        final Cluster cluster = configuration.getCassandraConfig().build(environment);
        final String keyspace = configuration.getCassandraConfig().getKeyspace();
        final Session session = keyspace != null ? cluster.connect(keyspace) : cluster.connect();

        environment.jersey().register(new CassandraResource(session));
    }
}
