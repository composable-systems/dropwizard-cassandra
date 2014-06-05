/*
 * Copyright 2014 Stuart Gunter
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

package org.stuartgunter.dropwizard.cassandra;

import com.google.common.io.Resources;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.stuartgunter.dropwizard.cassandra.smoke.SmokeTestApp;
import org.stuartgunter.dropwizard.cassandra.smoke.SmokeTestConfiguration;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CassandraHealthCheckIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<SmokeTestConfiguration> APP =
            new DropwizardAppRule<>(SmokeTestApp.class, Resources.getResource("minimal.yml").getPath());

    @Test
    public void reportsSuccess() throws Exception {
        final URI uri = UriBuilder.fromUri("http://localhost")
                .port(APP.getLocalPort() + 1)
                .path("healthcheck")
                .build();

        final WebResource resource = Client.create().resource(uri);
        final Map<String, Map<String, Object>> result = resource.get(Map.class);

        final String healthCheckName = "cassandra.minimal-cluster";
        assertThat(result).containsKey(healthCheckName);

        Map<String, Object> cassandraStatus = result.get(healthCheckName);
        assertThat(cassandraStatus).containsEntry("healthy", true);
    }
}
