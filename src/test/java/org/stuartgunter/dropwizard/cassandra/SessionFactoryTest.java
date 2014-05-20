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

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionFactoryTest {

    private final Cluster cluster = mock(Cluster.class);

    @Test
    public void createsNewInitialisedSessionWithoutKeyspace() throws Exception {
        Session session = mock(Session.class);
        when(cluster.connect()).thenReturn(session);

        SessionFactory sessionFactory = new SessionFactory(cluster, null);
        Session newSession = sessionFactory.create();

        assertThat(newSession).isSameAs(session);
        verify(cluster).connect();
    }

    @Test
    public void createsNewSessionWithKeyspace() throws Exception {
        Session session = mock(Session.class);
        when(cluster.connect(anyString())).thenReturn(session);

        SessionFactory sessionFactory = new SessionFactory(cluster, "keyspace");
        Session newSession = sessionFactory.create();

        assertThat(newSession).isSameAs(session);
        verify(cluster).connect("keyspace");
    }
}
