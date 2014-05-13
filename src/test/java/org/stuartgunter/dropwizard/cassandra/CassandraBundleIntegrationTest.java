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

import com.google.common.collect.Lists;
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
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

public class CassandraBundleIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<SmokeTestConfiguration> APP =
            new DropwizardAppRule<>(SmokeTestApp.class, Resources.getResource("minimal.yml").getPath());

    @Test
    public void canQueryCassandra() throws Exception {
        final URI uri = UriBuilder.fromUri("http://localhost")
                .port(APP.getLocalPort())
                .path("query")
                .build();

        final WebResource resource = Client.create().resource(uri);
        final List<String> result = Lists.newArrayList(resource.get(String[].class));

        assertThat(result, hasItem("system"));
    }

    @Test
    public void cassandraMetricsArePublished() throws Exception {
        final URI uri = UriBuilder.fromUri("http://localhost")
                .port(APP.getLocalPort() + 1)
                .path("metrics")
                .build();

        final WebResource resource = Client.create().resource(uri);
        final String result = resource.get(String.class);

        assertThat(result, containsString("com.datastax.driver.core.Cluster.minimal-cluster"));
    }

    @Test
    public void cassandraHealthCheckIsPublished() throws Exception {
        final URI uri = UriBuilder.fromUri("http://localhost")
                .port(APP.getLocalPort() + 1)
                .path("healthcheck")
                .build();

        final WebResource resource = Client.create().resource(uri);
        final String result = resource.get(String.class);

        assertThat(result, containsString("cassandra.minimal-cluster"));
    }
}
