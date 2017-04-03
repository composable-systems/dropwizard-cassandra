package systems.composable.dropwizard.cassandra.network;

import com.datastax.driver.core.policies.AddressTranslator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

/**
 * A service provider interface for creating DataStax {@link AddressTranslator}.
 * <p>
 * <p>To create your own, just:
 * <p>
 * <ol> <li>Create a class which implements {@link systems.composable.dropwizard.cassandra.network.AddressTranslatorFactory}. <li>Annotate it with
 * {@code @JsonTypeName} and give it a unique type name. <li>Add a {@code META-INF/services/systems.composable.dropwizard.cassandra.connect.AddressTranslatorFactory}
 * file with your implementation's full class name to the class path. </ol>
 *
 * @see EC2MultiRegionAddressTranslatorFactory
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface AddressTranslatorFactory extends Discoverable {
    AddressTranslator build();
}
