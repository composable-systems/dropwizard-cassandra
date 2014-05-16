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
import com.datastax.driver.core.Session;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class CassandraHealthCheckTest {

    private final SessionFactory sessionFactory = mock(SessionFactory.class);
    private final Session session = mock(Session.class);
    private final Cluster cluster = mock(Cluster.class);

    @Before
    public void setUp() throws Exception {
        when(sessionFactory.create()).thenReturn(session);
        when(session.getCluster()).thenReturn(cluster);
    }

    @Test
    public void isHealthyIfNoExceptionIsThrown() throws Exception {
        final CassandraHealthCheck healthCheck = new CassandraHealthCheck(sessionFactory);

        final HealthCheck.Result result = healthCheck.execute();

        assertThat(result.isHealthy(), is(true));
        verify(sessionFactory).create();
    }

    @Test
    public void isUnhealthyIfAnExceptionIsThrown() throws Exception {
        when(sessionFactory.create()).thenThrow(new RuntimeException());
        final CassandraHealthCheck healthCheck = new CassandraHealthCheck(sessionFactory);

        final HealthCheck.Result result = healthCheck.execute();

        assertThat(result.isHealthy(), is(false));
        verify(sessionFactory).create();
    }
}
