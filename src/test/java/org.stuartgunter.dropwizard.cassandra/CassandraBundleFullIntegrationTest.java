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

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class CassandraBundleFullIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<CassandraBundleConfiguration> APP =
            new DropwizardAppRule<>(CassandraBundleApp.class, Resources.getResource("cassandra-full.yml").getPath());

    @Test
    public void supportsFullConfiguration() throws Exception {
        final URI uri = UriBuilder.fromUri("http://localhost")
                .port(APP.getLocalPort() + 1)
                .path("healthcheck")
                .build();

        final WebResource resource = Client.create().resource(uri);
        final String result = resource.get(String.class);

        assertThat(result, containsString("cassandra.full-cluster"));
    }
}
