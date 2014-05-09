package org.stuartgunter.dropwizard.cassandra;

import org.junit.Test;

public class CassandraHealthCheckTest {
    @Test
    public void isHealthyIfNoExceptionIsThrownOnConnectionToCluster() throws Exception {
    }

    @Test
    public void isUnhealthyIfAnExceptionIsThrownOnFailedConnectionToCluster() throws Exception {
    }

    @Test
    public void isHealthyIfNoExceptionIsThrownOnConnectionToKeyspace() throws Exception {
    }

    @Test
    public void isUnhealthyIfAnExceptionIsThrownOnFailedConnectionToKeyspace() throws Exception {
    }
}
