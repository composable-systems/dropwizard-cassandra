package org.stuartgunter.dropwizard.cassandra.auth;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.constraints.NotNull;

/**
 * A factory for configuring and building {@link PlainTextAuthProvider} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>username</td>
 *         <td>No default. You must define a username.</td>
 *         <td>The username to authenticate with.</td>
 *     </tr>
 *     <tr>
 *         <td>password</td>
 *         <td>No default. You must define a password.</td>
 *         <td>The password to authenticate with.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("plainText")
public class PlainTextAuthProviderFactory implements AuthProviderFactory {

    @NotNull
    private String username;
    @NotNull
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
