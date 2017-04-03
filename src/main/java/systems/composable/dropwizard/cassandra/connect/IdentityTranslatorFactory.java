package systems.composable.dropwizard.cassandra.connect;

import com.datastax.driver.core.policies.AddressTranslator;
import com.datastax.driver.core.policies.IdentityTranslator;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A factory for configuring and building {@link com.datastax.driver.core.policies.IdentityTranslator} instances.
 */
@JsonTypeName("identityTranslator")
public class IdentityTranslatorFactory implements AddressTranslatorFactory {
    @Override
    public AddressTranslator build() {
        return new IdentityTranslator();
    }
}
