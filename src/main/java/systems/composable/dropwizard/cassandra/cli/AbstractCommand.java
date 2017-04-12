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
package systems.composable.dropwizard.cassandra.cli;

import com.builtamont.cassandra.migration.CassandraMigration;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import io.dropwizard.Configuration;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import systems.composable.dropwizard.cassandra.CassandraConfiguration;
import systems.composable.dropwizard.cassandra.CassandraFactory;

/**
 * Base abstract class for all CLI migration commands.
 */
abstract class AbstractCommand<T extends Configuration> extends ConfiguredCommand<T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCommand.class);

	private final CassandraConfiguration<T> cassandraConfiguration;
	private final MigrationConfiguration<T> migrationConfiguration;
	private final Class<T> configurationClass;

	AbstractCommand(
			String name,
			String description,
			CassandraConfiguration<T> cassandraConfiguration,
			MigrationConfiguration<T> migrationConfiguration,
			Class<T> configurationClass) {
		super(name, description);
		this.cassandraConfiguration = cassandraConfiguration;
		this.migrationConfiguration = migrationConfiguration;
		this.configurationClass = configurationClass;
	}

	@Override
	protected Class<T> getConfigurationClass() {
		return configurationClass;
	}

	@Override
	protected void run(Bootstrap<T> bootstrap, Namespace namespace, T configuration) throws Exception {
		final CassandraFactory cassandraFactory = cassandraConfiguration.getCassandraFactory(configuration);
		final MigrationFactory migrationFactory = migrationConfiguration.getMigrationFactory(configuration);
		LOG.debug("Effective migration config: {}", migrationFactory);
		try (final Cluster cluster = cassandraFactory.build()) {
			final String keyspace = cassandraFactory.getKeyspace();
			try (final Session session = cluster.connect(keyspace == null || keyspace.isEmpty() ? null : keyspace)) {
				run(namespace, migrationFactory.build(), session);
			}
		} catch (Exception e) {
			LOG.error("Error occurred on cassandra migration command", e);
			throw e;
		}
	}

	abstract void run(Namespace namespace, CassandraMigration cassandraMigration, Session session) throws Exception;
}
