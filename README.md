# Dropwizard Bundle for Cassandra

[Dropwizard](http://dropwizard.io) provides [bundles](http://dropwizard.github.io/dropwizard/manual/core.html#bundles)
as a means to package common functionality in a modular form, easy to use in multiple applications. Some of the standard
bundles include support for things like view rendering and Hibernate.

This bundle provides useful functionality for Dropwizard apps that communicate with [Cassandra](http://cassandra.apache.org).
Under the hood, it uses the [DataStax Cassandra Driver](http://www.datastax.com/documentation/developer/java-driver/2.0/java-driver/whatsNew2.html).

## What's Included

By default, the bundle includes:

* Configuration
* Managed Cluster
* Health Check
* Metrics
* Support for multiple clusters

### Configuration

A configuration class is defined for you, with sensible defaults (wherever possible, relying on those provided by the driver).
All you need to do is override the default configuration as required and then let the bundle do the work of wiring everything
up correctly.

### Managed Cluster

The Cluster instance is wrapped as a `Managed` object in Dropwizard, allowing it to be properly closed when the application
terminates. Graceful termination is attempted first (with a configurable wait period), after which the cluster will be
forcefully terminated. Remember that we're talking about the client driver being closed... not the actual Cassandra cluster
(unfortunately DataStax used a confusing term for their client API in this case).

### Health Check

A health check is registered automatically for you, ensuring that your application reports the correct status based on
its ability to connect to Cassandra. This will either connect just to the cluster, or to a particular keyspace (if configured).

### Metrics

DataStax already expose metrics directly from the Cluster instance, but this bundle extracts and registers them with the
`MetricRegistry` of your app - ensuring that they get correctly reported.

### Support for Multiple Clusters

For apps that connect to multiple Cassandra clusters, all features described above are fully supported through separation
by named clusters. Health checks and metrics are named according to cluster, allowing multiple separate clusters to
operate safely within the same application.


## Usage

Using the bundle is as simple as registering it in your Dropwizard application. The dependency can be found in Maven Central
with the following coordinates:

```xml
<dependency>
  <groupId>org.stuartgunter</groupId>
  <artifactId>dropwizard-cassandra</artifactId>
  <version>${dropwizard-cassandra.version}</version>
</dependency>
```

Once you have the dependency registered, it's just a matter of adding `CassandraFactory` instances to your 
`Configuration` class:

```java
public class YourAppConfig extends Configuration {

    @Valid
    @NotNull
    private CassandraFactory cassandra = new CassandraFactory();

    @JsonProperty("cassandra")
    public CassandraFactory getCassandraFactory() {
        return cassandra;
    }

    @JsonProperty("cassandra")
    public void setCassandraFactory(CassandraFactory cassandra) {
        this.cassandra = cassandra;
    }
}
```

Then, in your `Application`, build `Cluster` instances when you need them:

```java
public class YourApp extends Application<YourAppConfig> {
    
    @Override
    public void run(YourAppConfig configuration, Environment environment) throws Exception {
        Cluster cassandra = configuration.getCassandraFactory().build(environment);
    }
}
```

Alternatively, you may use the `CassandraBundle` to ensure a `Cluster` has been initialized during startup.
`CassandraBundle` is abstract and requires you to implement a single method in order to provide the correct
configuration (similar to the `dropwizard-hibernate` module).

```java
public class YourApp extends Application<YourAppConfig> {
    private final CassandraBundle<YourAppConfig> cassandra =
            new CassandraBundle<YourAppConfig>() {
                @Override
                protected CassandraFactory cassandraConfiguration(YourAppConfig appConfig) {
                    return appConfig.getCassandraFactory();
                }
            };

    @Override
    public void initialize(Bootstrap<CassandraBundleConfiguration> bootstrap) {
        bootstrap.addBundle(cassandra);
    }

    @Override
    public void run(YourAppConfig configuration, Environment environment) throws Exception {
        /*
         * you can now use `cassandra.getCluster()` to use the cluster instance in your app,
         * or pass around a SessionFactory with `cassandra.getSessionFactory()` if you just need
         * to be able to create new sessions without knowing whether a keyspace was configured
         */
    }
}
```


## Configuration Reference

The `dropwizard-cassandra` bundle defines a number of configuration options that are largely based on the requirements
of the DataStax Cassandra driver. Some additional configuration is included for the bundle to register everything correctly
with Dropwizard.

The full set of configuration options are shown below. Only configuration keys are shown; please see the JavaDocs on the various
configuration classes for more details about the configuration options available and their default values - particularly
for polymorphic configuration - e.g. `ReconnectionPolicyFactory`. There are also a number of smoke tests ensuring
that the major configuration options are parseable. To find examples of particular config variants, take a look at the
[test resources](src/test/resources) folder.

```yaml
clusterName:
keyspace:
contactPoints:
port:
protocolVersion:
compression:
reconnectionPolicy:
  type:
authProvider:
  type:
retryPolicy:
  type:
queryOptions:
  consistencyLevel:
  serialConsistencyLevel:
  fetchSize:
socketOptions:
  connectTimeoutMillis:
  readTimeoutMillis:
  keepAlive:
  reuseAddress:
  soLinger:
  tcpNoDelay:
  receiveBufferSize:
  sendBufferSize:
poolingOptions:
  local:
    minSimultaneousRequests:
    maxSimultaneousRequests:
    coreConnections:
    maxConnections:
  remote:
    minSimultaneousRequests:
    maxSimultaneousRequests:
    coreConnections:
    maxConnections:
metricsEnabled:
jmxEnabled:
shutdownGracePeriod:
```
