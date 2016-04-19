# Changes

## 3.0.0

* Simplified the versioning scheme to use [Semantic Versioning](http://semver.org/), largely based on [Issue 19](https://github.com/composable-systems/dropwizard-cassandra/issues/19)
* Upgraded to use version `3.0.0` of the Cassandra Driver at the request of [devshorts](https://github.com/devshorts) in [Issue 17](https://github.com/composable-systems/dropwizard-cassandra/issues/17)
* Migrated `dropwizard-cassandra` from GitHub user [stuartgunter](https://github.com/composable-systems) to GitHub organisation [composable-systems](https://github.com/composable-systems)
* Changed Maven groupId from `org.stuartgunter` to `systems.composable`
* Changed root package from `org.stuartgunter` to `systems.composable`
* Changed default healthcheck query to support newer versions of Cassandra
* First class support for SSL configuration using either `jdk` or `netty` (and `netty` supports either `jdk` or `openssl` providers)

## 2.2-dw0.9-cs2.1

* Added `healthCheckTimeout` with default value of `2 seconds` (thanks to [mmusnjak](https://github.com/mmusnjak) for [PR 18](https://github.com/composable-systems/dropwizard-cassandra/pull/18)).

## 2.1-dw0.9-cs2.1

* Added support for `maxSchemaAgreementWait` via YAML configuration (thanks to [mmusnjak](https://github.com/mmusnjak) for [PR 16](https://github.com/composable-systems/dropwizard-cassandra/pull/16)).

## 2.0-dw0.9-cs2.1

* Updated to support Dropwizard 0.9.x (thanks to [mmusnjak](https://github.com/mmusnjak) for [PR 15](https://github.com/composable-systems/dropwizard-cassandra/pull/15)).

## 2.0-dw0.8-cs2.1

* Modified `contactPoints` to use all A records associated with DNS names instead of just one A record per DNS name (thanks to [manub](https://github.com/manub) for [PR 14](https://github.com/composable-systems/dropwizard-cassandra/pull/14)).
  This should not be a breaking change, but the version bump to `2.0` is precautionary as it informs users to be aware of the change in behaviour.

## 1.1-dw0.8-cs2.1

* Added support for `LoadBalancingPolicy` via YAML configuration (credit to [adejanovski](https://github.com/adejanovski) for [PR 12](https://github.com/composable-systems/dropwizard-cassandra/pull/12) that sparked this)
* Added support for `SpeculativeExecutionPolicy` via YAML configuration

## 1.0-dw0.8-cs2.1

* Upgraded to Dropwizard 0.8.1

## 1.0-dw0.7-cs2.1 (Dropwizard 0.7.x & Cassandra Driver 2.1.x)

* Upgraded to DataStax Cassandra Driver 2.1.2 (note: this includes breaking changes in the definition of `ProtocolVersion` - only relevant if you explicitly configure it though)

## 1.0-dw0.7-cs2.0 (Dropwizard 0.7.x & Cassandra Driver 2.0.x)

* [Issue 8](https://github.com/composable-systems/dropwizard-cassandra/issues/8) Changed versioning scheme to incorporate Dropwizard version.
* Removed the deprecated `CassandraBundle` (superceded by `CassandraFactory` in v0.4)
* Upgraded to DataStax Cassandra Driver 2.0.6
* Upgraded to Dropwizard 0.7.1

## 0.6.1

* [Pull 7](https://github.com/composable-systems/dropwizard-cassandra/pull/7) Upgraded to 2.0.3 Datastax Cassandra Driver

## 0.6

* [Pull 6](https://github.com/composable-systems/dropwizard-cassandra/pull/6) Add `build()` overload that doesn't use `Environment`
* `CassandraHealthCheck` now uses a validation query instead of relying on the driver, which was giving false positives

## 0.5

* [Issue 5](https://github.com/composable-systems/dropwizard-cassandra/issues/5) Changed implementation of `CassandraHealthCheck` to work around driver memory leak
* Removed `SessionFactory`, as this was not adding any real value and might encourage poor use of `Session` instances
* [Pull 3](https://github.com/composable-systems/dropwizard-cassandra/pull/3) Use `InetAddress` for contactPoints
* [Pull 2](https://github.com/composable-systems/dropwizard-cassandra/pull/2) Fix null clusterName in log messages

## 0.4

* [Pull 1](https://github.com/composable-systems/dropwizard-cassandra/pull/1) Make building independent of the Bundle

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
