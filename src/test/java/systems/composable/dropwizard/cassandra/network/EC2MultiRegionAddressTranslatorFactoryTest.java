package systems.composable.dropwizard.cassandra.network;

import org.junit.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

public class EC2MultiRegionAddressTranslatorFactoryTest {

    @Test
    public void buildInstanceNotNullTest() throws Exception {
        EC2MultiRegionAddressTranslatorFactory factory = new EC2MultiRegionAddressTranslatorFactory();

        assertNotNull(factory.build());
    }
}
