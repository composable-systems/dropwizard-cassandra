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

package org.stuartgunter.dropwizard.cassandra.smoke;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.stuartgunter.dropwizard.cassandra.CassandraBundle;
import org.stuartgunter.dropwizard.cassandra.CassandraFactory;
import org.stuartgunter.dropwizard.cassandra.CassandraResource;

public class SmokeTestApp extends Application<SmokeTestConfiguration> {

    private final CassandraBundle<SmokeTestConfiguration> cassandraBundle =
            new CassandraBundle<SmokeTestConfiguration>() {
                @Override
                protected CassandraFactory cassandraConfiguration(SmokeTestConfiguration configuration) {
                    return configuration.getCassandraConfig();
                }
            };

    @Override
    public void initialize(Bootstrap<SmokeTestConfiguration> bootstrap) {
        bootstrap.addBundle(cassandraBundle);
    }

    @Override
    public void run(SmokeTestConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new CassandraResource(cassandraBundle.getSessionFactory()));
    }
}
