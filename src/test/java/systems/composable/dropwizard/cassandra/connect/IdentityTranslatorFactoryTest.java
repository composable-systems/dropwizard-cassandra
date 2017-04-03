package systems.composable.dropwizard.cassandra.connect;

import org.junit.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

public class IdentityTranslatorFactoryTest {
    @Test
    public void buildsPolicyWithChildPolicy() throws Exception {
        IdentityTranslatorFactory factory = new IdentityTranslatorFactory();

        assertNotNull(factory.build());
    }
}
