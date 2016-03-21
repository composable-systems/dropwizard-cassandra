package org.stuartgunter.dropwizard.cassandra.ssl;

import com.datastax.driver.core.SSLOptions;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A service provider interface for creating DataStax {@link SSLOptions SSL options}.
 * <p/>
 * To create your own, just:
 * <ol>
 *     <li>Create a class which implements {@link SSLOptionsFactory}.</li>
 *     <li>Annotate it with {@code @JsonTypeName} and give it a unique type name.</li>
 *     <li>Add a {@code META-INF/services/org.stuartgunter.dropwizard.cassandra.ssl.SSLOptionsFactory}
 *     file with your implementation's full class name to the class path.</li>
 * </ol>
 *
 * @see JDKSSLOptionsFactory
 * @see NettySSLOptionsFactory
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface SSLOptionsFactory {

    SSLOptions build();
}
