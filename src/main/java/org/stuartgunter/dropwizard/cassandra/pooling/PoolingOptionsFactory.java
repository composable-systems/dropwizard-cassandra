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
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.util.Duration;

import javax.validation.Valid;

/**
 * A factory for configuring and building {@link PoolingOptions} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>heartbeatInterval</td>
 *         <td>Defaults to 30 seconds.</td>
 *         <td>Specifies the heart beat interval, after which a message is sent on an idle connection to make sure it's still alive.</td>
 *     </tr>
 *     <tr>
 *         <td>poolTimeout</td>
 *         <td>Defaults to 5 seconds.</td>
 *         <td>Specifies the timeout when trying to acquire a connection from a host's pool.</td>
 *     </tr>
 *     <tr>
 *         <td>local</td>
 *         <td>No default. You must specify local pooling options.</td>
 *         <td>Specifies connection {@link HostDistanceOptions pooling options} for local hosts.</td>
 *     </tr>
 *     <tr>
 *         <td>remote</td>
 *         <td>No default. You must specify remote pooling options.</td>
 *         <td>Specifies connection {@link HostDistanceOptions pooling options} for remote hosts.</td>
 *     </tr>
 * </table>
 */
public class PoolingOptionsFactory {

    @Valid
    private Duration heartbeatInterval;
    @Valid
    private Duration poolTimeout;
    @Valid
    private HostDistanceOptions remote;
    @Valid
    private HostDistanceOptions local;

    @JsonProperty
    public Duration getHeartbeatInterval() {
        return heartbeatInterval;
    }

    @JsonProperty
    public void setHeartbeatInterval(Duration heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    @JsonProperty
    public Duration getPoolTimeout() {
        return poolTimeout;
    }

    @JsonProperty
    public void setPoolTimeout(Duration poolTimeout) {
        this.poolTimeout = poolTimeout;
    }

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
        if (heartbeatInterval != null) {
            poolingOptions.setHeartbeatIntervalSeconds((int) heartbeatInterval.toSeconds());
        }
        if (poolTimeout != null) {
            poolingOptions.setPoolTimeoutMillis((int) poolTimeout.toMilliseconds());
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

}
