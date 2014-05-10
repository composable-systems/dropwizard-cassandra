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

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CassandraBundleApp extends Application<CassandraBundleConfiguration> {

    private final CassandraBundle<CassandraBundleConfiguration> cassandraBundle =
            new CassandraBundle<CassandraBundleConfiguration>() {
                @Override
                protected CassandraConfiguration cassandraConfiguration(CassandraBundleConfiguration configuration) {
                    return configuration.getCassandraConfig();
                }
            };

    @Override
    public void initialize(Bootstrap<CassandraBundleConfiguration> bootstrap) {
        bootstrap.addBundle(cassandraBundle);
    }

    @Override
    public void run(CassandraBundleConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new CassandraResource(cassandraBundle.getCluster()));
    }
}
