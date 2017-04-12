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

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import systems.composable.dropwizard.cassandra.CassandraBundle;
import systems.composable.dropwizard.cassandra.CassandraFactory;

/**
 * @author <a href="mailto:max@dominichenko.com">Max Dominichenko</a>
 */
public class CliTestApp extends Application<CliTestConfiguration> {

	private static final Logger LOG = LoggerFactory.getLogger(CliTestApp.class);

	@Override
	public void initialize(Bootstrap<CliTestConfiguration> bootstrap) {
		// Prevents NoHostAvailable test errors
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		bootstrap.addBundle(new CassandraBundle<CliTestConfiguration>() {
			@Override
			public CassandraFactory getCassandraFactory(CliTestConfiguration configuration) {
				return configuration.getCassandraFactory();
			}

			@Override
			public MigrationFactory getMigrationFactory(CliTestConfiguration configuration) {
				return configuration.getMigrationFactory();
			}
		});
		bootstrap.addCommand(new CliCommandSchema());
	}

	@Override
	public void run(CliTestConfiguration configuration, Environment environment) throws Exception {}
}
