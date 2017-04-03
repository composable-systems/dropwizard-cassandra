package systems.composable.dropwizard.cassandra.connect;

import com.datastax.driver.core.policies.AddressTranslator;
import com.datastax.driver.core.policies.IdentityTranslator;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("IdentityTranslator")
public class IdentityTranslatorFactory implements AddressTranslatorFactory {
  @Override
  public AddressTranslator build() {
    return new IdentityTranslator();
  }
}
