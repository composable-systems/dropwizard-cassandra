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

package org.stuartgunter.dropwizard.cassandra.pooling;

import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PoolingOptionsFactoryTest {

    @Test
    public void buildsPoolingOptionsWithConfiguredValues() throws Exception {
        final PoolingOptionsFactory factory = new PoolingOptionsFactory();
        factory.setLocal(createHostDistanceOptions(1, 3, 5, 25));
        factory.setRemote(createHostDistanceOptions(2, 4, 6, 30));

        final PoolingOptions poolingOptions = factory.build();

        assertThat(poolingOptions.getCoreConnectionsPerHost(HostDistance.LOCAL)).isEqualTo(1);
        assertThat(poolingOptions.getMaxConnectionsPerHost(HostDistance.LOCAL)).isEqualTo(3);
        assertThat(poolingOptions.getMinSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL)).isEqualTo(5);
        assertThat(poolingOptions.getMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL)).isEqualTo(25);

        assertThat(poolingOptions.getCoreConnectionsPerHost(HostDistance.REMOTE)).isEqualTo(2);
        assertThat(poolingOptions.getMaxConnectionsPerHost(HostDistance.REMOTE)).isEqualTo(4);
        assertThat(poolingOptions.getMinSimultaneousRequestsPerConnectionThreshold(HostDistance.REMOTE)).isEqualTo(6);
        assertThat(poolingOptions.getMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.REMOTE)).isEqualTo(30);
    }

    private HostDistanceOptions createHostDistanceOptions(int coreConnections, int maxConnections, int minSimultaneousRequests, int maxSimultaneousRequests) {
        HostDistanceOptions options = new HostDistanceOptions();
        options.setCoreConnections(coreConnections);
        options.setMaxConnections(maxConnections);
        options.setMinSimultaneousRequests(minSimultaneousRequests);
        options.setMaxSimultaneousRequests(maxSimultaneousRequests);
        return options;
    }

    @Test
    public void buildsPoolingOptionsWithDefaultValues() throws Exception {
        final PoolingOptionsFactory factory = new PoolingOptionsFactory();
        final PoolingOptions defaultPoolingOptions = new PoolingOptions();

        final PoolingOptions poolingOptions = factory.build();

        verifySamePoolingOptions(poolingOptions, defaultPoolingOptions, HostDistance.LOCAL);
        verifySamePoolingOptions(poolingOptions, defaultPoolingOptions, HostDistance.REMOTE);
    }

    private void verifySamePoolingOptions(PoolingOptions poolingOptions, PoolingOptions defaultPoolingOptions, HostDistance hostDistance) {
        assertThat(poolingOptions.getCoreConnectionsPerHost(hostDistance))
                .isEqualTo(defaultPoolingOptions.getCoreConnectionsPerHost(hostDistance));
        assertThat(poolingOptions.getMaxConnectionsPerHost(hostDistance))
                .isEqualTo(defaultPoolingOptions.getMaxConnectionsPerHost(hostDistance));
        assertThat(poolingOptions.getMaxSimultaneousRequestsPerConnectionThreshold(hostDistance))
                .isEqualTo(defaultPoolingOptions.getMaxSimultaneousRequestsPerConnectionThreshold(hostDistance));
        assertThat(poolingOptions.getMinSimultaneousRequestsPerConnectionThreshold(hostDistance))
                .isEqualTo(defaultPoolingOptions.getMinSimultaneousRequestsPerConnectionThreshold(hostDistance));
    }
}
