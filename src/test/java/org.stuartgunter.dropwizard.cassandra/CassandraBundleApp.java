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
