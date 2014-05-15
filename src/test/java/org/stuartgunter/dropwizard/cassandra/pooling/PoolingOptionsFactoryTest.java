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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PoolingOptionsFactoryTest {

    @Test
    public void buildsPoolingOptionsWithConfiguredValues() throws Exception {
        final PoolingOptionsFactory factory = new PoolingOptionsFactory();
        factory.setLocal(createHostDistanceOptions(1000, 2000, 3000, 4000));
        factory.setRemote(createHostDistanceOptions(5000, 6000, 7000, 8000));

        final PoolingOptions poolingOptions = factory.build();

        assertThat(poolingOptions.getCoreConnectionsPerHost(HostDistance.LOCAL), is(1000));
        assertThat(poolingOptions.getMaxConnectionsPerHost(HostDistance.LOCAL), is(2000));
        assertThat(poolingOptions.getMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL), is(3000));
        assertThat(poolingOptions.getMinSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL), is(4000));
        assertThat(poolingOptions.getCoreConnectionsPerHost(HostDistance.REMOTE), is(5000));
        assertThat(poolingOptions.getMaxConnectionsPerHost(HostDistance.REMOTE), is(6000));
        assertThat(poolingOptions.getMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.REMOTE), is(7000));
        assertThat(poolingOptions.getMinSimultaneousRequestsPerConnectionThreshold(HostDistance.REMOTE), is(8000));
    }

    private HostDistanceOptions createHostDistanceOptions(int coreConnections, int maxConnections, int maxSimultaneousRequests, int minSimultaneousRequests) {
        HostDistanceOptions options = new HostDistanceOptions();
        options.setCoreConnections(coreConnections);
        options.setMaxConnections(maxConnections);
        options.setMaxSimultaneousRequests(maxSimultaneousRequests);
        options.setMinSimultaneousRequests(minSimultaneousRequests);
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
        assertThat(poolingOptions.getCoreConnectionsPerHost(hostDistance),
                is(defaultPoolingOptions.getCoreConnectionsPerHost(hostDistance)));
        assertThat(poolingOptions.getMaxConnectionsPerHost(hostDistance),
                is(defaultPoolingOptions.getMaxConnectionsPerHost(hostDistance)));
        assertThat(poolingOptions.getMaxSimultaneousRequestsPerConnectionThreshold(hostDistance),
                is(defaultPoolingOptions.getMaxSimultaneousRequestsPerConnectionThreshold(hostDistance)));
        assertThat(poolingOptions.getMinSimultaneousRequestsPerConnectionThreshold(hostDistance),
                is(defaultPoolingOptions.getMinSimultaneousRequestsPerConnectionThreshold(hostDistance)));
    }
}
