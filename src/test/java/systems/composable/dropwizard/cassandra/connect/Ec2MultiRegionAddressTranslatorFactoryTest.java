package systems.composable.dropwizard.cassandra.connect;

import org.junit.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

public class Ec2MultiRegionAddressTranslatorFactoryTest {

    @Test
    public void buildsPolicyWithChildPolicy() throws Exception {
        EC2MultiRegionAddressTranslatorFactory factory = new EC2MultiRegionAddressTranslatorFactory();

        assertNotNull(factory.build());
    }
}
