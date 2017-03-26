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

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:max@dominichenko.com">Max Dominichenko</a>
 */
class CliCommandSchema extends ConfiguredCommand<CliTestConfiguration> {

	private static final Logger LOG = LoggerFactory.getLogger(CliCommandSchema.class);

	static final String NAME = "schema";
	private static final String DESC = "Manages keystore for migration tests.";
	private static final String ACTION = "action";
	private static final String SCHEMA = "migration";

	enum Action {
		create("CREATE SCHEMA IF NOT EXISTS " + SCHEMA +
				" WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 3}"),
		remove("DROP SCHEMA " + SCHEMA);

		private final String cql;

		Action(String cql) {
			this.cql = cql;
		}
	}

	CliCommandSchema() {
		super(NAME, DESC);
	}

	@Override
	public void configure(Subparser subparser) {
		subparser.addArgument(ACTION).nargs("?").type(Action.class).required(true);
		super.configure(subparser);
	}

	@Override
	protected void run(
			Bootstrap<CliTestConfiguration> bootstrap,
			Namespace namespace,
			CliTestConfiguration configuration) throws Exception {
		try (final Cluster cluster = configuration.getCassandraFactory().build()) {
			try (final Session session = cluster.connect()) {
				Action action = namespace.get(ACTION);
				LOG.info("Trying to {} the {} schema", action, SCHEMA);
				session.execute(action.cql);
			}
		}
	}
}
