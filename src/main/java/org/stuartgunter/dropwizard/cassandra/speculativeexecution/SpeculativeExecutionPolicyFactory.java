package org.stuartgunter.dropwizard.cassandra.speculativeexecution;

import com.datastax.driver.core.policies.SpeculativeExecutionPolicy;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A service provider interface for creating DataStax {@link SpeculativeExecutionPolicy speculative execution policies}.
 * <p/>
 * To create your own, just:
 * <ol>
 *     <li>Create a class which implements {@link SpeculativeExecutionPolicyFactory}.</li>
 *     <li>Annotate it with {@code @JsonTypeName} and give it a unique type name.</li>
 *     <li>Add a {@code META-INF/services/org.stuartgunter.dropwizard.cassandra.speculativeexecution.SpeculativeExecutionPolicyFactory}
 *     file with your implementation's full class name to the class path.</li>
 * </ol>
 *
 * @see ConstantSpeculativeExecutionPolicyFactory
 * @see NoSpeculativeExecutionPolicyFactory
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface SpeculativeExecutionPolicyFactory {

    SpeculativeExecutionPolicy build();
}
