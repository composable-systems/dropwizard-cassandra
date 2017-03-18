/*
 * Copyright 2016 Composable Systems Limited
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

package systems.composable.dropwizard.cassandra.multiCluster;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MultiClusterTestApp extends Application<MultiClusterTestConfiguration> {

    @Override
    public void initialize(Bootstrap<MultiClusterTestConfiguration> bootstrap) {
        // Prevents NoHostAvailable test errors
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(MultiClusterTestConfiguration configuration, Environment environment) throws Exception {
        configuration.getCassandra1Config().build(environment);
        configuration.getCassandra2Config().build(environment);

        environment.jersey().register(new HelloWorldResource());
    }
}
