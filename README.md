# dropwizard-cassandra

[![Build Status](https://travis-ci.org/composable-systems/dropwizard-cassandra.png?branch=master)](https://travis-ci.org/composable-systems/dropwizard-cassandra)

The `dropwizard-cassandra` library provides useful functionality for Dropwizard apps that communicate with [Cassandra](http://cassandra.apache.org) clusters.
Under the hood, it uses the [DataStax Cassandra Driver](http://www.datastax.com/documentation/developer/java-driver/3.0/java-driver/whatsNew2.html).

## What's Included

By default, the bundle includes:

* Configuration
* Managed Cluster
* Health Check
* Metrics
* Support for multiple clusters
* Injected Cluster and Session instances

### Configuration

A configuration class is defined for you with sensible defaults (wherever possible, relying on those provided by the driver).
All you need to do is override the default configuration as required and then let the `CassandraFactory` do the work of
wiring everything up correctly.

### Managed Cluster

The `Cluster` instance is wrapped as a `Managed` object in Dropwizard, allowing it to be properly closed when the application
terminates. Graceful termination is attempted first (with a configurable wait period), after which the cluster will be
forcefully terminated. Remember that we're talking about the client driver being closed... not the actual Cassandra cluster.

### Health Check

A health check is registered automatically for you, ensuring that your application reports the correct status based on
its ability to connect to Cassandra. The cluster is considered healthy if it can successfully execute the `validationQuery`
defined in configuration.

### Metrics

DataStax already expose metrics directly from the Cluster instance, but `CassandraFactory` extracts and registers them
with the `MetricRegistry` of your app - ensuring that they get correctly reported.

### Support for Multiple Clusters

For apps that connect to multiple Cassandra clusters, all features described above are fully supported through separation
by named clusters. Health checks and metrics are named according to cluster, allowing multiple separate clusters to
operate safely within the same application.


## Usage

Using the bundle is as simple as registering it in your Dropwizard application. The dependency can be found in Maven Central
with the following coordinates:

```xml
<dependency>
  <groupId>systems.composable</groupId>
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

Or, you may setup automatic injection of `Cluster` or `Session` instances into your resources.
To do that, just add `CassandraBundle` class into your bootstrap:

```java
public class YourApp extends Application<YourAppConfig> {
	
    @Override
    public void initialize(final Bootstrap<YourAppConfig> bootstrap) {
     //...
     bootstrap.addBundle(new CassandraBundle<YourAppConfig>() {
        @Override
        public CassandraFactory getCassandraFactory(YourAppConfig configuration) {
            return configuration.getCassandraFactory();
        }
     });
     //...
    }
}
```

and then you're able to inject Cassandra dependencies into your resources. You may either provide a `Cluster` instance:

```java
@Path("/test")
public class TestService {

    @Context Cluster cluster;

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/users")
    public List<User> getUsers() {
        try (final Session session = cluster.connect("auth")) {
            final ResultSet resultSet = session.execute("SELECT * FROM users");
            //...
        }
    }
}
```

or `Session` instance:

```java
@Path("/test")
public class TestService {

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/users")
    public List<User> getUsers(@Context Session session) {
        final ResultSet resultSet = session.execute("SELECT * FROM users");
        //...
    }
}
```

If you use injected `Session` instance, then session is opened with keyspace that is defined in your application
configuration for Cassandra (see `CassandraFactory.getKeyspace()`). If keyspace isn't specified in your configuration,
then session will be opened with no defined keyspace, so that you have to explicitly specify it in statements for
tables/column families.

## Configuration Reference

The `dropwizard-cassandra` library defines a number of configuration options that are largely based on the requirements
of the DataStax Cassandra driver. Some additional configuration is included for the bundle to register everything correctly
with Dropwizard.

The full set of configuration options are shown below. Only configuration keys are shown; please see the JavaDocs on the various
configuration classes for more details about the configuration options available and their default values - particularly
for polymorphic configuration - e.g. `ReconnectionPolicyFactory`. There are also a number of smoke tests ensuring
that the major configuration options are parseable. To find examples of particular config variants, take a look at the
[test resources](src/test/resources) folder.

`contactPoints` now support entries that resolve to multiple `InetAddress`es. In this case, every address resolved will
be added to the cluster. This is particularly useful when your Cassandra nodes are handled by an external service discovery
platform. For more information see [here](http://docs.datastax.com/en/drivers/java/3.0/com/datastax/driver/core/Cluster.Builder.html#addContactPoints-java.lang.String-)

```yaml
clusterName:
keyspace:
validationQuery:
healthCheckTimeout:
contactPoints:
port:
protocolVersion:
compression:
maxSchemaAgreementWait:
ssl:
  type:
addressTranslator:
  type:
reconnectionPolicy:
  type:
authProvider:
  type:
retryPolicy:
  type:
loadBalancingPolicy:
  type:
speculativeExecutionPolicy:
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
  heartbeatInterval:
  poolTimeout:
  local:
    maxRequestsPerConnection:
    newConnectionThreshold:
    coreConnections:
    maxConnections:
  remote:
    maxRequestsPerConnection:
    newConnectionThreshold:
    coreConnections:
    maxConnections:
metricsEnabled:
jmxEnabled:
shutdownGracePeriod:
```
