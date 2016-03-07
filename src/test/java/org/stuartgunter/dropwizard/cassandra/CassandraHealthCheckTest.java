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
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import io.dropwizard.util.Duration;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

public class CassandraHealthCheckTest {

    private final String validationQuery = "some validation query";
    private final Cluster cluster = mock(Cluster.class);
    private final Session session = mock(Session.class);
    private CassandraHealthCheck healthCheck;

    @Before
    public void setUp() throws Exception {
        when(cluster.connect()).thenReturn(session);
        healthCheck = new CassandraHealthCheck(cluster, validationQuery, Duration.seconds(1));
    }

    @Test
    public void isHealthyIfValidationQuerySucceeds() throws Exception {
        ResultSetFuture mockResultSetFuture = mock(ResultSetFuture.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSetFuture.get(Duration.seconds(1).toMilliseconds(), TimeUnit.MILLISECONDS)).thenReturn(mockResultSet);
        when(session.executeAsync(validationQuery)).thenReturn(mockResultSetFuture);
        final HealthCheck.Result result = healthCheck.execute();

        assertThat(result.isHealthy()).isTrue();
    }

    @Test
    public void isUnhealthyIfValidationQueryThrowsAnException() throws Exception {
        when(session.execute(validationQuery)).thenThrow(new RuntimeException());

        final HealthCheck.Result result = healthCheck.execute();

        assertThat(result.isHealthy()).isFalse();
    }
}
