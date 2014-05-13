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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.stuartgunter.dropwizard.cassandra.CassandraConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SmokeTestConfiguration extends Configuration {

    @Valid
    @NotNull
    private CassandraConfiguration cassandra;

    @JsonProperty("cassandra")
    public CassandraConfiguration getCassandraConfig() {
        return cassandra;
    }

    @JsonProperty("cassandra")
    public void setCassandraConfig(CassandraConfiguration cassndraConfig) {
        this.cassandra = cassndraConfig;
    }
}
