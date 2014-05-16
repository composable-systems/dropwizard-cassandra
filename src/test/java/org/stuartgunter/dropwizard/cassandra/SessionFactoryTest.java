package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
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

        assertThat(newSession, sameInstance(session));
        verify(cluster).connect();
    }

    @Test
    public void createsNewSessionWithKeyspace() throws Exception {
        Session session = mock(Session.class);
        when(cluster.connect(anyString())).thenReturn(session);

        SessionFactory sessionFactory = new SessionFactory(cluster, "keyspace");
        Session newSession = sessionFactory.create();

        assertThat(newSession, sameInstance(session));
        verify(cluster).connect("keyspace");
    }
}
