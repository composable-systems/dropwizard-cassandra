/*
 * Copyright 2014 Stuart Gunter
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.stuartgunter.dropwizard.cassandra.smoke;

import com.google.common.io.Resources;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * A series of smoke tests that ensure the application can load and initialise the Cassandra cluster under
 * different configuration variants.
 *
 * This doesn't test that it works correctly - just that the configuration can be successfully loaded.
 */
@RunWith(Parameterized.class)
public class SmokeTest {

    @Rule
    public final DropwizardAppRule<SmokeTestConfiguration> app;

    public SmokeTest(String configPath) {
        this.app = new DropwizardAppRule<>(SmokeTestApp.class, Resources.getResource(configPath).getPath());
    }

    @Parameterized.Parameters(name = "Config: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "smoke/auth/authProvider-plainText.yml" },
                { "smoke/reconnection/reconnectionPolicy-constant.yml" },
                { "smoke/reconnection/reconnectionPolicy-exponential.yml" },
                { "smoke/retry/retryPolicy-default.yml" },
                { "smoke/retry/retryPolicy-downgradingConsistency.yml" },
                { "smoke/retry/retryPolicy-fallthrough.yml" },
                { "smoke/retry/retryPolicy-log.yml" }
        });
    }

    @Test
    public void supportsConfiguration() throws Exception {
        assertThat(app.getEnvironment().healthChecks().getNames(),
                hasItem("cassandra.smoke-cluster"));
    }
}
