package org.stuartgunter.dropwizard.cassandra.auth;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.stuartgunter.dropwizard.cassandra.auth.AuthProviderFactory;

@JsonTypeName("plainText")
public class PlainTextAuthProviderFactory implements AuthProviderFactory {

    private String username;
    private String password;

    @JsonProperty
    public String getUsername() {
        return username;
    }

    @JsonProperty
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public AuthProvider build() {
        return new PlainTextAuthProvider(username, password);
    }
}
