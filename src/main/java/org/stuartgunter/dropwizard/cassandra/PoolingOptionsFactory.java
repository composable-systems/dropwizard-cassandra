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

package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;

public class PoolingOptionsFactory {

    @Valid
    private HostDistanceOptions remote;
    @Valid
    private HostDistanceOptions local;

    @JsonProperty
    public HostDistanceOptions getRemote() {
        return remote;
    }

    @JsonProperty
    public void setRemote(HostDistanceOptions remote) {
        this.remote = remote;
    }

    @JsonProperty
    public HostDistanceOptions getLocal() {
        return local;
    }

    @JsonProperty
    public void setLocal(HostDistanceOptions local) {
        this.local = local;
    }

    public PoolingOptions build() {
        PoolingOptions poolingOptions = new PoolingOptions();
        if (local != null) {
            setPoolingOptions(poolingOptions, HostDistance.LOCAL, local);
        }
        if (remote != null) {
            setPoolingOptions(poolingOptions, HostDistance.REMOTE, remote);
        }
        return poolingOptions;
    }

    private void setPoolingOptions(PoolingOptions poolingOptions, HostDistance hostDistance, HostDistanceOptions options) {
        if (options.getCoreConnections() != null) {
            poolingOptions.setCoreConnectionsPerHost(hostDistance, options.getCoreConnections());
        }
        if (options.getMaxConnections() != null) {
            poolingOptions.setMaxConnectionsPerHost(hostDistance, options.getMaxConnections());
        }
        if (options.getMaxSimultaneousRequests() != null) {
            poolingOptions.setMaxSimultaneousRequestsPerConnectionThreshold(hostDistance, options.getMaxSimultaneousRequests());
        }
        if (options.getMinSimultaneousRequests() != null) {
            poolingOptions.setMinSimultaneousRequestsPerConnectionThreshold(hostDistance, options.getMinSimultaneousRequests());
        }
    }

    public static class HostDistanceOptions {

        @Min(0)
        private Integer minSimultaneousRequests;
        @Min(0)
        private Integer maxSimultaneousRequests;
        @Min(0)
        private Integer coreConnections;
        @Min(0)
        private Integer maxConnections;

        @JsonProperty
        public Integer getMinSimultaneousRequests() {
            return minSimultaneousRequests;
        }

        @JsonProperty
        public void setMinSimultaneousRequests(Integer minSimultaneousRequests) {
            this.minSimultaneousRequests = minSimultaneousRequests;
        }

        @JsonProperty
        public Integer getMaxSimultaneousRequests() {
            return maxSimultaneousRequests;
        }

        @JsonProperty
        public void setMaxSimultaneousRequests(Integer maxSimultaneousRequests) {
            this.maxSimultaneousRequests = maxSimultaneousRequests;
        }

        @JsonProperty
        public Integer getCoreConnections() {
            return coreConnections;
        }

        @JsonProperty
        public void setCoreConnections(Integer coreConnections) {
            this.coreConnections = coreConnections;
        }

        @JsonProperty
        public Integer getMaxConnections() {
            return maxConnections;
        }

        @JsonProperty
        public void setMaxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
        }
    }
}
