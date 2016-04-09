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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import systems.composable.dropwizard.cassandra.CassandraFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MultiClusterTestConfiguration extends Configuration {

    @Valid
    @NotNull
    private CassandraFactory cassandra1;

    @Valid
    @NotNull
    private CassandraFactory cassandra2;

    @JsonProperty("cassandra1")
    public CassandraFactory getCassandra1Config() {
        return cassandra1;
    }

    @JsonProperty("cassandra1")
    public void setCassandra1Config(CassandraFactory cassndraConfig) {
        this.cassandra1 = cassndraConfig;
    }

    @JsonProperty("cassandra2")
    public CassandraFactory getCassandra2Config() {
        return cassandra2;
    }

    @JsonProperty("cassandra2")
    public void setCassandra2Config(CassandraFactory cassndraConfig) {
        this.cassandra2 = cassndraConfig;
    }
}
