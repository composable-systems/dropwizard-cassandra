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
import net.sourceforge.argparse4j.inf.Subparser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import systems.composable.dropwizard.cassandra.CassandraConfiguration;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A root CLI cassandra migration command. Organizes subcommands
 * {@link CommandInfo}, {@link CommandValidate}, {@link CommandBaseline} and {@link CommandMigrate}.
 */
public class CommandCassandra<T extends Configuration> extends AbstractCommand<T> {

	private static final Logger LOG = LoggerFactory.getLogger(CommandCassandra.class);

	private static final String NAME = "cassandra";
	private static final String DESC = "Runs cassandra migration tasks";
	private static final String COMMAND_NAME_ATTR = "subCommand";
	private final SortedMap<String, AbstractCommand<T>> subCommands = new TreeMap<>();

	public CommandCassandra(
			CassandraConfiguration<T> cassandraConfiguration,
			MigrationConfiguration<T> migrationConfiguration,
			Class<T> configurationClass) {
		super(NAME, DESC, cassandraConfiguration, migrationConfiguration, configurationClass);
		addSubCommand(new CommandInfo<>(cassandraConfiguration, migrationConfiguration, configurationClass));
		addSubCommand(new CommandValidate<>(cassandraConfiguration, migrationConfiguration, configurationClass));
		addSubCommand(new CommandBaseline<>(cassandraConfiguration, migrationConfiguration, configurationClass));
		addSubCommand(new CommandMigrate<>(cassandraConfiguration, migrationConfiguration, configurationClass));
	}

	private void addSubCommand(AbstractCommand<T> subCommand) {
		subCommands.put(subCommand.getName(), subCommand);
	}

	@Override
	public void configure(final Subparser subparser) {
		for (AbstractCommand<T> subCommand : subCommands.values()) {
			final Subparser cmdParser = subparser.addSubparsers()
					.addParser(subCommand.getName())
					.setDefault(COMMAND_NAME_ATTR, subCommand.getName())
					.description(subCommand.getDescription());
			subCommand.configure(cmdParser);
		}
	}

	@Override
	void run(Namespace namespace, CassandraMigration cassandraMigration, Session session) throws Exception {
		final AbstractCommand<T> cmd = subCommands.get(namespace.getString(COMMAND_NAME_ATTR));
		LOG.info("Run '{}' command: {}", cmd.getName(), cmd.getDescription());
		cmd.run(namespace, cassandraMigration, session);
	}
}
