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

package org.stuartgunter.dropwizard.cassandra.multiCluster;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.stuartgunter.dropwizard.cassandra.CassandraBundle;
import org.stuartgunter.dropwizard.cassandra.CassandraFactory;

public class MultiClusterTestApp extends Application<MultiClusterTestConfiguration> {

    private final CassandraBundle<MultiClusterTestConfiguration> cassandraBundle1 =
            new CassandraBundle<MultiClusterTestConfiguration>() {
                @Override
                protected CassandraFactory cassandraConfiguration(MultiClusterTestConfiguration configuration) {
                    return configuration.getCassandra1Config();
                }
            };

    private final CassandraBundle<MultiClusterTestConfiguration> cassandraBundle2 =
            new CassandraBundle<MultiClusterTestConfiguration>() {
                @Override
                protected CassandraFactory cassandraConfiguration(MultiClusterTestConfiguration configuration) {
                    return configuration.getCassandra2Config();
                }
            };

    @Override
    public void initialize(Bootstrap<MultiClusterTestConfiguration> bootstrap) {
        bootstrap.addBundle(cassandraBundle1);
        bootstrap.addBundle(cassandraBundle2);
    }

    @Override
    public void run(MultiClusterTestConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new HelloWorldResource());
    }
}
