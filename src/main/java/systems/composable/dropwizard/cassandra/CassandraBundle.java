/*
 * Copyright 2017 Composable Systems Limited
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
package systems.composable.dropwizard.cassandra;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Generics;
import systems.composable.dropwizard.cassandra.cli.CommandCassandra;
import systems.composable.dropwizard.cassandra.cli.MigrationConfiguration;
import systems.composable.dropwizard.cassandra.cli.MigrationFactory;

/**
 * A reusable bundle of Cassandra functionality that initializes {@link javax.ws.rs.core.Context}-annotated
 * {@link com.datastax.driver.core.Cluster} and {@link com.datastax.driver.core.Session} object instances.
 * <p>Also, this bundle attaches CLI migration commands, based on functionality of
 * <a href='https://github.com/builtamont-oss/cassandra-migration'>builtamont-oss/cassandra-migration</a>.
 * <h1>The main usage is following:</h1>
 * <ol>
 * <li> In your main application class add the following code into your
 * {@link io.dropwizard.Application#initialize(Bootstrap)} method:
 * <pre>
 * &#64;Override
 * public void initialize(final Bootstrap&lt;MyAppConfiguration&gt; bootstrap) {
 *   //...
 *   bootstrap.addBundle(new CassandraBundle&lt;MyAppConfiguration&gt;() {
 *     &#64;Override
 *     public CassandraFactory getCassandraFactory(MyAppConfiguration configuration) {
 *       return configuration.getCassandraFactory();
 *     }
 *   });
 *   //...
 * }
 * </pre>
 * </li>
 * <li>Now you may inject into your resources either {@link com.datastax.driver.core.Cluster}:
 * <pre>
 * &#64;Path("/test")
 * public class TestService {
 *   &#64;Context Cluster cluster;
 *
 *   &#64;Produces(MediaType.APPLICATION_JSON)
 *   &#64;GET
 *   &#64;Path("/users")
 *   public List&lt;User&gt; getUsers() {
 *     try (final Session session = cluster.connect("auth")) {
 *       final ResultSet resultSet = session.execute("SELECT * FROM users");
 *       //...
 *     }
 *   }
 * }
 * </pre>
 * or {@link com.datastax.driver.core.Session} instance:
 * <pre>
 * &#64;Path("/test")
 * public class TestService {
 *   &#64;Produces(MediaType.APPLICATION_JSON)
 *   &#64;GET
 *   &#64;Path("/users")
 *   public List&lt;User&gt; getUsers(&#64;Context Session session) {
 *     final ResultSet resultSet = session.execute("SELECT * FROM users");
 *     //...
 *   }
 * }
 * </pre>
 * </li>
 * </ol>
 * If you use injected {@link com.datastax.driver.core.Session} instance, then session
 * is opened with keyspace that is defined in your application configuration for Cassandra
 * (see {@link CassandraFactory#getKeyspace()}).
 * If keyspace isn't specified in your configuration, then session will be opened with no
 * defined keyspace, so that you have to explicitly specify it in statements for tables/column&nbsp;families.
 *
 * <h1>Migration configuration</h1>
 * If you don't specify migration configuration in your application descriptor,
 * then default values will be used, as specified in {@link MigrationFactory}.
 * <p>To change defaults, you have to extend your {@link CassandraBundle} implementation with overridden
 * method {@link CassandraBundle#getMigrationFactory(Configuration)}, like that:
 * <pre>
 * &#64;Override
 * public void initialize(final Bootstrap&lt;MyAppConfiguration&gt; bootstrap) {
 *   //...
 *   bootstrap.addBundle(new CassandraBundle&lt;MyAppConfiguration&gt;() {
 *     &#64;Override
 *     public CassandraFactory getCassandraFactory(MyAppConfiguration configuration) {
 *       return configuration.getCassandraFactory();
 *     }
 *     &#64;Override
 *     public MigrationFactory getMigrationFactory(MyAppConfiguration configuration) {
 *       return configuration.MigrationFactory();
 *     }
 *   });
 *   //...
 * }
 * </pre>
 * and, of course, you have to add one more property into your application configuration class:
 * <pre>
 * public class MyAppConfiguration extends Configuration {
 *   &#64;Valid &#64;NotNull &#64;JsonProperty("migration") private MigrationFactory migrationFactory;
 *   public MigrationFactory getMigrationFactory() { return migrationFactory; }
 *   public void setMigrationFactory(MigrationFactory migrationFactory) { this.migrationFactory = migrationFactory; }
 * }
 * </pre>
 */
public abstract class CassandraBundle<T extends Configuration>
		implements ConfiguredBundle<T>, CassandraConfiguration<T>, MigrationConfiguration<T> {

	/**
	 * Initializes Cassandra in application bootstrap.
	 * Does nothing for now.
	 *
	 * @param bootstrap The application bootstrap
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(Bootstrap<?> bootstrap) {
		bootstrap.addCommand(new CommandCassandra<T>(this, this,
				Generics.getTypeParameter(getClass(), Configuration.class)));
	}

	/**
	 * Initializes the Cassandra environment: registers context binder for
	 * {@link com.datastax.driver.core.Cluster} and {@link com.datastax.driver.core.Session} instances.
	 *
	 * @param configuration The configuration object
	 * @param environment The application's Environment
	 */
	@Override
	public void run(T configuration, Environment environment) throws Exception {
		environment.jersey().register(CassandraProvider.binder(getCassandraFactory(configuration), environment));
	}

	@Override
	public MigrationFactory getMigrationFactory(T configuration) {
		// Default migration configuration
		return new MigrationFactory();
	}
}
