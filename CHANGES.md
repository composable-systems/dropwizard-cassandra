# Changes

## 0.5

* [Issue 5](https://github.com/stuartgunter/dropwizard-cassandra/issues/5) Fixed memory issue with `CassandraHealthCheck`
* Removed `SessionFactory`, as this was not adding any real value and might encourage poor use of `Session` instances
* [Pull 3](https://github.com/stuartgunter/dropwizard-cassandra/pull/3) Use `InetAddress` for contactPoints
* [Pull 2](https://github.com/stuartgunter/dropwizard-cassandra/pull/2) Fix null clusterName in log messages

## 0.4

* [Pull 1](https://github.com/stuartgunter/dropwizard-cassandra/pull/1) Make building independent of the Bundle

## 0.3.1

* Added `SessionFactory` to allow for easy session initialisation based on configuration (a session is established with
the configured `keyspace`, or directly to the cluster if a keyspace is not configured)

## 0.3

* Changed scope of dependencies from `provided` to `compile`
* Fixed issues relating to generation of JavaDoc
* Added logging to `CassandraHealthCheck`
* Updated license information and notices
* Updated docs on configuration

## 0.2

* Improved JavaDoc

## 0.1

* Provided `CassandraBundle` to build the cluster and register the relevant components
* Provided `CassandraHealthCheck` to establish whether a session can be initialised as per configuration
* Provided `CassandraManager` to ensure the cluster is appropriately closed on application shutdown
* Provided `CassandraMetricSet` to expose metrics named according to the cluster
* Provided `CassandraFactory` and related configuration classes to allow for full configuration of the Cassandra cluster
