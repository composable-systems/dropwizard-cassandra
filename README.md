# Dropwizard Bundle for Cassandra

[Dropwizard](http://dropwizard.io) provides [bundles](http://dropwizard.github.io/dropwizard/manual/core.html#bundles)
as a means to package common functionality in a modular form, easy to use in multiple applications. Some of the standard
bundles include support for things like view rendering and Hibernate.

This bundle aims to provide useful functionality for Dropwizard apps that communicate with [Cassandra](http://cassandra.apache.org).
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

