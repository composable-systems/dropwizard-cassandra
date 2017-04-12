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

import com.builtamont.cassandra.migration.api.CassandraMigrationException;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.dropwizard.cli.Cli;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.util.JarLocation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author <a href="mailto:max@dominichenko.com">Max Dominichenko</a>
 */
public class CliIntegrationTest {

	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

	private final ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
	private final ByteArrayOutputStream stdErr = new ByteArrayOutputStream();

	private final String cfgPath = Resources.getResource("migration.yml").getPath();

	private Cli cli;

	@Before
	public void before() throws Exception {
		final JarLocation location = mock(JarLocation.class);
		when(location.getVersion()).thenReturn(Optional.of("1.0.0"));

		final CliTestApp app = new CliTestApp();
		final Bootstrap<CliTestConfiguration> bootstrap = new Bootstrap<>(new CliTestApp());
		app.initialize(bootstrap);

		System.setOut(new PrintStream(stdOut));
//		System.setErr(new PrintStream(stdErr));

		cli = new Cli(location, bootstrap, stdOut, stdErr);
		schema(CliCommandSchema.Action.create);
	}

	private void schema(CliCommandSchema.Action action) throws Exception {
		cli.run(CliCommandSchema.NAME, action.name(), cfgPath);
	}

	@After
	public void after() throws Exception {
		System.setOut(originalOut);
		System.setErr(originalErr);
		schema(CliCommandSchema.Action.remove);
	}

	@Test
	public void infoPending() throws Exception {
		cli.run("cassandra", "info", cfgPath);
		String expected = Resources.toString(Resources.getResource("migration/info_pending.txt"), Charsets.UTF_8);
		assertThat(stdOut.toString()).contains(expected);
	}

	@Test
	public void validateFail() throws Exception {
		try {
			cli.run("cassandra", "validate", cfgPath);
			fail("Validate should be failed");
		} catch (CassandraMigrationException e) {
			assertThat(e.getMessage()).isEqualTo("Validation failed. Detected resolved migration not applied to database: 1.0");
		}
	}

	@Test
	public void migrateAndValidate() throws Exception {
		assertThat(cli.run("cassandra", "migrate", cfgPath)).isTrue();
		assertThat(cli.run("cassandra", "validate", cfgPath)).isTrue();
		assertThat(stdOut.toString())
				.contains("CommandMigrate: Done 2 successful migrations")
				.contains("Validate: Validated 2 migrations");
	}

	@Test
	public void baselineAndInfo() throws Exception {
		assertThat(cli.run("cassandra", "baseline", cfgPath)).isTrue();
		cli.run("cassandra", "info", cfgPath);
		assertThat(stdOut.toString())
				.contains("|    BASELINE | Baselin |     0.1 | Empty schema |")
				.contains("|         CQL | Pending |     1.0 |   CQL script |")
				.contains("| JAVA_DRIVER | Pending |     1.1 |  Java script |");
	}
}
