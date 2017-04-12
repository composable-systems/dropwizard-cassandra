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
import com.builtamont.cassandra.migration.api.MigrationVersion;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * A factory for configuring the cassandra migration.
 * Based on <a href='https://github.com/builtamont-oss/cassandra-migration'>builtamont-oss/cassandra-migration</a>.
 * <p>Supported YAML structure:
 * <pre>
 * migration:
 *   scripts:
 *     encoding: UTF-8
 *     locations:
 *     - db/migration
 *     timeout: 60
 *     allowOutOfOrder: false
 *   baseline:
 *     version: "1"
 *     description: "&lt;&lt;&nbsp;Cassandra&nbsp;Baseline&nbsp;&gt;&gt;"
 *   targetVersion:
 *   tablePrefix: ""
 * </pre>
 *
 * <h1>Configuration Parameters:</h1>
 * <table>
 * <tr>
 *   <th>Name</th>
 *   <th>Default</th>
 *   <th>Description</th>
 * </tr>
 * <tr>
 *   <td>scripts.encoding</td>
 *   <td>{@code "UTF-8"}</td>
 *   <td>The encoding of CQL scripts.</td>
 * </tr>
 * <tr>
 *   <td>scripts.locations</td>
 *   <td>{@code "db/migration"}</td>
 *   <td>Array of locations of the migration scripts.<br/>
 *       Scripts are scanned in the specified folder(s) recursively.</td>
 * </tr>
 * <tr>
 *   <td>scripts.timeout</td>
 *   <td>{@code 60}</td>
 *   <td>CQL scripts timeout in seconds.</td>
 * </tr>
 * <tr>
 *   <td>scripts.allowOutOfOrder</td>
 *   <td>{@code false}</td>
 *   <td>Allow out of order migration.</td>
 * </tr>
 * <tr>
 *   <td>baseline.version</td>
 *   <td>{@code "1"}</td>
 *   <td>Version to apply for an existing schema when baseline is run.</td>
 * </tr>
 * <tr>
 *   <td>baseline.description</td>
 *   <td><code>"&lt;&lt;&nbsp;Cassandra&nbsp;Baseline&nbsp;&gt;&gt;"</code></td>
 *   <td>Description to apply to an existing schema when baseline is run.</td>
 * </tr>
 * <tr>
 *   <td>targetVersion</td>
 *   <td>Latest available</td>
 *   <td>The target version. Migrations with a higher version number will be ignored.</td>
 * </tr>
 * <tr>
 *   <td>tablePrefix</td>
 *   <td>{@code ""}</td>
 *   <td>Prefix to be prepended to {@code cassandra_migration_version*} table names.</td>
 * </tr>
 * </table>
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class MigrationFactory {

	public static class Scripts {

		@JsonProperty private String[] locations;
		@JsonProperty private String encoding;
		@JsonProperty private Integer timeout;
		@JsonProperty private Boolean allowOutOfOrder;

		public String[] getLocations() {
			return locations;
		}

		public void setLocations(String[] locations) {
			this.locations = locations;
		}

		public String getEncoding() {
			return encoding;
		}

		public void setEncoding(String encoding) {
			this.encoding = encoding;
		}

		public Integer getTimeout() {
			return timeout;
		}

		public void setTimeout(Integer timeout) {
			this.timeout = timeout;
		}

		public Boolean isAllowOutOfOrder() {
			return allowOutOfOrder;
		}

		public void setAllowOutOfOrder(Boolean allowOutOfOrder) {
			this.allowOutOfOrder = allowOutOfOrder;
		}

		@Override
		public String toString() {
			return "Scripts{" +
					"locations=" + Arrays.toString(locations) +
					", encoding='" + encoding + '\'' +
					", timeout=" + timeout +
					", allowOutOfOrder=" + allowOutOfOrder +
					'}';
		}
	}

	public static class Baseline {
		@JsonProperty private String version;
		@JsonProperty private String description;

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		@Override
		public String toString() {
			return "Baseline{" +
					"version='" + version + '\'' +
					", description='" + description + '\'' +
					'}';
		}
	}

	@JsonProperty @NotNull private Scripts scripts = new Scripts();
	@JsonProperty @NotNull private Baseline baseline = new Baseline();
	@JsonProperty private String targetVersion;
	@JsonProperty private String tablePrefix;

	public Scripts getScripts() {
		return scripts;
	}

	public void setScripts(Scripts scripts) {
		this.scripts = scripts;
	}

	public Baseline getBaseline() {
		return baseline;
	}

	public void setBaseline(Baseline baseline) {
		this.baseline = baseline;
	}

	public String getTargetVersion() {
		return targetVersion;
	}

	public void setTargetVersion(String targetVersion) {
		this.targetVersion = targetVersion;
	}

	public String getTablePrefix() {
		return tablePrefix;
	}

	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	public CassandraMigration build() {
		final CassandraMigration cassandraMigration = new CassandraMigration();

		if (isNotBlank(getScripts().getEncoding()))
			cassandraMigration.setEncoding(getScripts().getEncoding());
		if (isNoneBlank(getScripts().getLocations()))
			cassandraMigration.setLocations(getScripts().getLocations());
		if (getScripts().getTimeout() != null)
			cassandraMigration.setTimeout(getScripts().getTimeout());
		if (getScripts().isAllowOutOfOrder() != null)
			cassandraMigration.setAllowOutOfOrder(getScripts().isAllowOutOfOrder());
		if (isNotBlank(getBaseline().getVersion()))
			cassandraMigration.setBaselineVersion(MigrationVersion.Companion.fromVersion(getBaseline().getVersion()));
		if (isNotBlank(getBaseline().getDescription()))
			cassandraMigration.setBaselineDescription(getBaseline().getDescription());
		if (isNotBlank(getTablePrefix()))
			cassandraMigration.setTablePrefix(getTablePrefix());
		if (isNotBlank(getTargetVersion()))
			cassandraMigration.setTarget(MigrationVersion.Companion.fromVersion(getTargetVersion()));

		return cassandraMigration;
	}

	@Override
	public String toString() {
		return "MigrationFactory{" +
				"scripts=" + scripts +
				", baseline=" + baseline +
				", targetVersion='" + targetVersion + '\'' +
				", tablePrefix='" + tablePrefix + '\'' +
				'}';
	}
}
