package org.stuartgunter.dropwizard.cassandra;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class CassandraBundleConfiguration extends Configuration {

    @Valid
    @NotNull
    private CassandraConfiguration cassndraConfig;

    @JsonProperty("cassandra")
    public CassandraConfiguration getCassandraConfig() {
        return cassndraConfig;
    }

    @JsonProperty("cassandra")
    public void setCassandraConfig(CassandraConfiguration cassndraConfig) {
        this.cassndraConfig = cassndraConfig;
    }
}
