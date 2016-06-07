/*
 * Copyright 2016 Composable Systems Limited
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
import systems.composable.dropwizard.cassandra.smoke.SmokeTestApp;
import systems.composable.dropwizard.cassandra.smoke.SmokeTestConfiguration;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DropwizardCassandraIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<SmokeTestConfiguration> APP =
            new DropwizardAppRule<>(SmokeTestApp.class, Resources.getResource("minimal.yml").getPath());

    @Test
    public void canQueryCassandra() throws Exception {
        final URI uri = UriBuilder.fromUri("http://localhost")
                .port(APP.getLocalPort())
                .path("query")
                .build();

        final WebTarget target = ClientBuilder.newClient().target(uri);
        final List<String> result = Lists.newArrayList(target.request().get(String[].class));

        assertThat(result).contains("system");
    }

    @Test
    public void cassandraMetricsArePublished() throws Exception {
        final URI uri = UriBuilder.fromUri("http://localhost")
                .port(APP.getAdminPort())
                .path("metrics")
                .build();

        final WebTarget target = ClientBuilder.newClient().target(uri);
        final String result = target.request().get(String.class);

        assertThat(result).contains("com.datastax.driver.core.Cluster.minimal-cluster");
    }

    @Test
    public void cassandraHealthCheckIsPublished() throws Exception {
        final URI uri = UriBuilder.fromUri("http://localhost")
                .port(APP.getAdminPort())
                .path("healthcheck")
                .build();

        final WebTarget target = ClientBuilder.newClient().target(uri);
        final String result = target.request().get(String.class);

        assertThat(result).contains("cassandra.minimal-cluster");
    }
}
