package org.stuartgunter.dropwizard.cassandra;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.google.common.io.Resources;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;
import org.stuartgunter.dropwizard.cassandra.smoke.SmokeTestApp;
import org.stuartgunter.dropwizard.cassandra.smoke.SmokeTestConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class LoggingIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<SmokeTestConfiguration> APP =
        new DropwizardAppRule<>(SmokeTestApp.class, Resources.getResource("minimal.yml").getPath());

    @Test
    public void doesNotProduceLogEntriesWhenInstantiatingObjectMapper() {
        LogCapturing.start();
        Jackson.newObjectMapper();
        LogCapturing.verifyNoLogMessagesProduced();
    }

    private static class LogCapturing {

        private static ArgumentCaptor<ILoggingEvent> argumentCaptor;

        public static void start() {
            Appender<ILoggingEvent> logAppender = mock(Appender.class);
            Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            root.addAppender(logAppender);
            argumentCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
            doNothing().when(logAppender).doAppend(argumentCaptor.capture());
        }

        public static void verifyNoLogMessagesProduced() {
            assertThat(argumentCaptor.getAllValues()).isEmpty();
        }
    }
}
