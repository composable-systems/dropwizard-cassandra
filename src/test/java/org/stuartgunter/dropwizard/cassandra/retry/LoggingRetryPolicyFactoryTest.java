package org.stuartgunter.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.RetryPolicy;
import org.junit.Before;
import org.junit.Test;
import org.stuartgunter.dropwizard.cassandra.retry.LoggingRetryPolicyFactory;
import org.stuartgunter.dropwizard.cassandra.retry.RetryPolicyFactory;

import static org.mockito.Mockito.*;

public class LoggingRetryPolicyFactoryTest {

    private RetryPolicyFactory subPolicyFactory = mock(RetryPolicyFactory.class);
    private RetryPolicy subPolicy = mock(RetryPolicy.class);

    @Before
    public void setUp() throws Exception {
        when(subPolicyFactory.build()).thenReturn(subPolicy);
    }

    @Test
    public void buildsChildPolicyWhenBuildingLoggingRetryPolicy() throws Exception {
        final LoggingRetryPolicyFactory factory = new LoggingRetryPolicyFactory();
        factory.setSubPolicy(subPolicyFactory);

        factory.build();

        verify(subPolicyFactory).build();
    }
}
