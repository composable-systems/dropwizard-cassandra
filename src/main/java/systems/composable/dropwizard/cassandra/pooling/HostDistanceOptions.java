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

package systems.composable.dropwizard.cassandra.pooling;

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
 *         <td>maxRequestsPerConnection</td>
 *         <td>Default is dependent on protocol version. See driver docs for details.</td>
 *         <td>The maximum number of connections per host.</td>
 *     </tr>
 *     <tr>
 *         <td>newConnectionThreshold</td>
 *         <td>Default is dependent on protocol version. See driver docs for details.</td>
 *         <td>The threshold that triggers the creation of a new connection to a host.</td>
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
    private Integer maxRequestsPerConnection;
    @Min(0)
    private Integer newConnectionThreshold;
    @Min(0)
    private Integer coreConnections;
    @Min(0)
    private Integer maxConnections;

    @JsonProperty
    public Integer getMaxRequestsPerConnection() {
        return maxRequestsPerConnection;
    }

    @JsonProperty
    public void setMaxRequestsPerConnection(Integer maxRequestsPerConnection) {
        this.maxRequestsPerConnection = maxRequestsPerConnection;
    }

    @JsonProperty
    public Integer getNewConnectionThreshold() {
        return newConnectionThreshold;
    }

    @JsonProperty
    public void setNewConnectionThreshold(Integer newConnectionThreshold) {
        this.newConnectionThreshold = newConnectionThreshold;
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
