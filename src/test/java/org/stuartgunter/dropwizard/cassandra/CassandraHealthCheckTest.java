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

import com.codahale.metrics.health.HealthCheck;
import com.datastax.driver.core.Cluster;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CassandraHealthCheckTest {

    private final Cluster cluster = mock(Cluster.class);

    @Test
    public void isHealthyIfANodeIsUp() throws Exception {
        final CassandraHealthCheck healthCheck = StubCassandraHealthCheck.withAtLeastOneNodeUp(cluster);

        final HealthCheck.Result result = healthCheck.execute();

        assertThat(result.isHealthy()).isTrue();
    }

    @Test
    public void isUnhealthyIfAllNodesAreDown() throws Exception {
        final CassandraHealthCheck healthCheck = StubCassandraHealthCheck.withAllNodesDown(cluster);

        final HealthCheck.Result result = healthCheck.execute();

        assertThat(result.isHealthy()).isFalse();
    }

    @Test
    public void isUnhealthyIfAnExceptionIsThrown() throws Exception {
        when(cluster.getMetadata()).thenThrow(new RuntimeException());

        final CassandraHealthCheck healthCheck = new CassandraHealthCheck(cluster);
        final HealthCheck.Result result = healthCheck.execute();

        assertThat(result.isHealthy()).isFalse();
    }

    /**
     * Unfortunately we can't mock out the DataStax objects required for this test, so this is a temporary measure to
     * get _some_ degree of test coverage for the health check.
     */
    private static class StubCassandraHealthCheck extends CassandraHealthCheck {

        private final boolean atLeastOneNodeUp;

        private StubCassandraHealthCheck(Cluster cluster, boolean atLeastOneNodeUp) {
            super(cluster);
            this.atLeastOneNodeUp = atLeastOneNodeUp;
        }

        static CassandraHealthCheck withAtLeastOneNodeUp(Cluster cluster) {
            return new StubCassandraHealthCheck(cluster, true);
        }

        static CassandraHealthCheck withAllNodesDown(Cluster cluster) {
            return new StubCassandraHealthCheck(cluster, false);
        }

        @Override
        protected boolean atLeastOneNodeIsUp(Cluster cluster) {
            return atLeastOneNodeUp;
        }
    }
}
