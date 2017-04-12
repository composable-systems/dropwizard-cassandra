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
import com.builtamont.cassandra.migration.api.MigrationInfo;
import com.datastax.driver.core.Session;
import com.google.common.collect.Lists;
import io.dropwizard.Configuration;
import net.sourceforge.argparse4j.inf.Namespace;
import systems.composable.dropwizard.cassandra.CassandraConfiguration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * A CLI command that retrieves and prints the complete information about all the migrations.
 * It's a subcommand of {@link CommandCassandra}.
 */
class CommandInfo<T extends Configuration> extends AbstractCommand<T> {

	private static final String NAME = "info";
	private static final String DESC = "Retrieves and prints the complete information about all the migrations.";

	private static final String INFO_TYPE = "Type";
	private static final String INFO_STATE = "State";
	private static final String INFO_VERSION = "Version";
	private static final String INFO_DESC = "Description";
	private static final String INFO_SCRIPT = "Script";
	private static final String INFO_CHKSUM = "Checksum";
	private static final String INFO_INST_ON = "Installed On";
	private static final String INFO_EXEC_MS = "Exec Time";

	private static final List<String> INFOS = Lists.newArrayList(
			INFO_TYPE, INFO_STATE, INFO_VERSION, INFO_DESC, INFO_SCRIPT, INFO_CHKSUM, INFO_INST_ON, INFO_EXEC_MS);

	CommandInfo(
			CassandraConfiguration<T> cassandraConfiguration,
			MigrationConfiguration<T> migrationConfiguration,
			Class<T> configurationClass) {
		super(NAME, DESC, cassandraConfiguration, migrationConfiguration, configurationClass);
	}

	@Override
	void run(Namespace namespace, CassandraMigration cassandraMigration, Session session) throws Exception {
		final MigrationInfo[] migrationInfos = cassandraMigration.info(session).all();
		if (migrationInfos.length == 0) throw new IllegalStateException("No migration scripts found");

		final Map<String, Integer> widths = new HashMap<>();
		final List<Map<String, String>> rows = new LinkedList<>();

		INFOS.forEach(col -> widths.compute(col, (k, v) -> k.length()));
		Arrays.stream(migrationInfos).forEach(migrationInfo -> {
			final Map<String, String> row = new HashMap<>();
			INFOS.forEach(col -> {
				final String cell;
				switch (col) {
					case INFO_TYPE: cell = migrationInfo.getType().toString(); break;
					case INFO_STATE: cell = migrationInfo.getState().getDisplayName(); break;
					case INFO_VERSION: cell = migrationInfo.getVersion().toString(); break;
					case INFO_DESC: cell = migrationInfo.getDescription(); break;
					case INFO_SCRIPT: cell = migrationInfo.getScript(); break;
					case INFO_CHKSUM: cell = String.valueOf(migrationInfo.getChecksum()); break;
					case INFO_INST_ON:
						final Date d =  migrationInfo.getInstalledOn();
						cell = d != null ? d.toInstant().toString() : "";
						break;
					case INFO_EXEC_MS:
						final Integer ms = migrationInfo.getExecutionTime();
						cell = ms != null ? Duration.of(ms, ChronoUnit.MILLIS).toString() : "";
						break;
					default: cell = "";
				}
				row.put(col, cell);
				widths.compute(col, (k, v) -> Math.max(cell.length(), v));
			});
			rows.add(row);
		});

		final String separator = "+" + INFOS.stream()
				.map(col -> repeat('-', widths.get(col) + 2))
				.collect(Collectors.joining("+")) + "+" + System.lineSeparator();

		final StringBuilder sb = new StringBuilder()
				.append(separator)
				.append('|')
				.append(INFOS.stream()
						.map(col -> " " + center(col, widths.get(col)) + " ")
						.collect(Collectors.joining("|")))
				.append('|').append(System.lineSeparator())
				.append(separator);
		rows.forEach(row -> sb
				.append('|')
				.append(INFOS.stream()
						.map(col -> " " + leftPad(row.get(col), widths.get(col)) + " ")
						.collect(Collectors.joining("|")))
				.append('|').append(System.lineSeparator()));
		sb.append(separator);
		System.out.print(sb.toString());
	}
}
