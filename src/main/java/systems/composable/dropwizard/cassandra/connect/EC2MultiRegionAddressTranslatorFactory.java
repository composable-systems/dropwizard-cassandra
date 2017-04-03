package systems.composable.dropwizard.cassandra.connect;

import com.datastax.driver.core.policies.AddressTranslator;
import com.datastax.driver.core.policies.EC2MultiRegionAddressTranslator;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("EC2MultiRegionAddressTranslator")
public class EC2MultiRegionAddressTranslatorFactory implements AddressTranslatorFactory {
  @Override
  public AddressTranslator build() {
    return new EC2MultiRegionAddressTranslator();
  }
}
