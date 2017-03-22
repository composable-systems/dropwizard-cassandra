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

import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import systems.composable.dropwizard.cassandra.smoke.SmokeInjectedApp;
import systems.composable.dropwizard.cassandra.smoke.SmokeTestConfiguration;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class DropwizardInjectionIntegrationTest {

	@ClassRule
	public static final DropwizardAppRule<SmokeTestConfiguration> APP =
			new DropwizardAppRule<>(SmokeInjectedApp.class, Resources.getResource("minimal.yml").getPath());

	private void canQuery(String path) {
		assertThat(Lists.newArrayList(
				ClientBuilder.newClient()
						.target(UriBuilder.fromUri("http://localhost")
								.port(APP.getLocalPort())
								.path(path)
								.build())
						.request()
						.get(String[].class)))
				.contains("system");
	}

	@Test
	public void canQuerySessionField() throws Exception {
		canQuery("querySessionField");
	}

	@Test
	public void canQuerySessionParameter() throws Exception {
		canQuery("querySessionParameter");
	}

	@Test
	public void canQueryClusterField() throws Exception {
		canQuery("queryClusterField");
	}
}
