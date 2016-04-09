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
import systems.composable.dropwizard.cassandra.CassandraResource;

public class SmokeTestApp extends Application<SmokeTestConfiguration> {

    @Override
    public void initialize(Bootstrap<SmokeTestConfiguration> bootstrap) {
    }

    @Override
    public void run(SmokeTestConfiguration configuration, Environment environment) throws Exception {
        final Cluster cluster = configuration.getCassandraConfig().build(environment);
        final String keyspace = configuration.getCassandraConfig().getKeyspace();
        final Session session = keyspace != null ? cluster.connect(keyspace) : cluster.connect();

        environment.jersey().register(new CassandraResource(session));
    }
}
