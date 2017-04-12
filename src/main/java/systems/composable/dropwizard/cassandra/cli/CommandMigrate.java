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
import com.datastax.driver.core.Session;
import io.dropwizard.Configuration;
import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import systems.composable.dropwizard.cassandra.CassandraConfiguration;

/**
 * A CLI command that starts the database migration. All pending migrations will be applied in order.
 * It's a subcommand of {@link CommandCassandra}.
 */
class CommandMigrate<T extends Configuration> extends AbstractCommand<T> {

	private static final Logger LOG = LoggerFactory.getLogger(CommandMigrate.class);

	private static final String NAME = "migrate";
	private static final String DESC = "Starts the database migration. All pending migrations will be applied in order.";

	CommandMigrate(
			CassandraConfiguration<T> cassandraConfiguration,
			MigrationConfiguration<T> migrationConfiguration,
			Class<T> configurationClass) {
		super(NAME, DESC, cassandraConfiguration, migrationConfiguration, configurationClass);
	}

	@Override
	void run(Namespace namespace, CassandraMigration cassandraMigration, Session session) throws Exception {
		int i = cassandraMigration.migrate(session);
		LOG.info("Done {} successful migration{}", i, i == 1 ? "" : "s");
	}
}
