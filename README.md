# Dropwizard Bundle for Cassandra

[Dropwizard](http://dropwizard.io) provides [bundles](http://dropwizard.github.io/dropwizard/manual/core.html#bundles)
as a means to package common functionality in a modular form, easy to use in multiple applications. Some of the standard
bundles include support for things like view rendering and Hibernate.

This bundle provides useful functionality for Dropwizard apps that communicate with [Cassandra](http://cassandra.apache.org).
Under the hood, it uses the [DataStax Cassandra Driver](http://www.datastax.com/documentation/developer/java-driver/2.0/java-driver/whatsNew2.html).

By default, you get:

* Configuration
* Managed Cluster
* Health Check
* Metrics
* Support for multiple clusters

## Configuration

A configuration class is defined for you, with sensible defaults (wherever possible, relying on those provided by the driver).
All you need to do is override the default configuration as required and then let the bundle do the work of wiring everything
up correctly.

## Managed Cluster

The Cluster instance is wrapped as a `Managed` object in Dropwizard, allowing it to be properly closed when the application
terminates. Graceful termination is attempted first (with a configurable wait period), after which the cluster will be
forcefully terminated. Remember that we're talking about the client driver being closed... not the actual Cassandra cluster
(unfortunately DataStax used a confusing term for their client API in this case).

## Health Check

A health check is registered automatically for you, ensuring that your application reports the correct status based on
its ability to connect to Cassandra. This will either connect just to the cluster, or to a particular keyspace (if configured).

## Metrics

DataStax already expose metrics directly from the Cluster instance, but this bundle extracts and registers them with the
`MetricRegistry` of your app - ensuring that they get correctly reported.

## Support for Multiple Clusters

For apps that connect to multiple Cassandra clusters, all features described above are fully supported through separation
by named clusters. Health checks and metrics are named according to cluster, allowing multiple separate clusters to
operate safely within the same application.


# Usage

Using the bundle is as simple as registering it in your Dropwizard application. The `CassandraBundle` is abstract and
requires you to implement a single method in order to provide the correct configuration (similar to the `dropwizard-hibernate` module).

    public class YourApp extends Application<YourAppConfig> {
        private final CassandraBundle<YourAppConfig> cassandraBundle =
                new CassandraBundle<YourAppConfig>() {
                    @Override
                    protected CassandraConfiguration cassandraConfiguration(YourAppConfig appConfig) {
                        return appConfig.getCassandraConfig();
                    }
                };

        @Override
        public void initialize(Bootstrap<CassandraBundleConfiguration> bootstrap) {
            bootstrap.addBundle(cassandraBundle);
        }

        @Override
        public void run(CassandraBundleConfiguration configuration, Environment environment) throws Exception {
            // you can now use `cassandraBundle.getCluster()` to use the cluster instance in your app
        }
    }
