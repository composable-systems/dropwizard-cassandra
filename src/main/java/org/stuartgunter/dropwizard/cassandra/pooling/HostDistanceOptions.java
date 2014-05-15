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

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;

/**
 * A factory for configuring and building {@link com.datastax.driver.core.PoolingOptions} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>minSimultaneousRequests</td>
 *         <td>No default. You must specify minimum simultaneous requests.</td>
 *         <td>The number of simultaneous requests on a connection below which connections in excess are reclaimed.</td>
 *     </tr>
 *     <tr>
 *         <td>maxSimultaneousRequests</td>
 *         <td>No default. You must specify maximum simultaneous requests.</td>
 *         <td>The number of simultaneous requests on all connections to an host after which more connections are created.</td>
 *     </tr>
 *     <tr>
 *         <td>coreConnections</td>
 *         <td>No default. You must specify core connections.</td>
 *         <td>The core number of connections per host.</td>
 *     </tr>
 *     <tr>
 *         <td>maxConnections</td>
 *         <td>No default. You must specify maximum connections.</td>
 *         <td>The maximum number of connections per host.</td>
 *     </tr>
 * </table>
 */
public class HostDistanceOptions {

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
