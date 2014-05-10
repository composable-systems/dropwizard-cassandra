package org.stuartgunter.dropwizard.cassandra;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

public class CassandraBundleIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<CassandraBundleConfiguration> APP =
            new DropwizardAppRule<>(CassandraBundleApp.class, Resources.getResource("cassandra-bundle.yml").getPath());

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

        assertThat(result, containsString("com.datastax.driver.core.Cluster.my-cluster"));
    }
}
