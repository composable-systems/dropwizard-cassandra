package systems.composable.dropwizard.cassandra.retry;

import com.datastax.driver.core.policies.LoggingRetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A factory for configuring and building {@link LoggingRetryPolicy} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>subPolicy</td>
 *         <td>No default. You must specify a sub policy.</td>
 *         <td>The policy to wrap. The policy created by this factory will return the same decision as subPolicy but will log them.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("log")
public class LoggingRetryPolicyFactory implements RetryPolicyFactory {

    @NotNull
    @Valid
    private RetryPolicyFactory subPolicy;

    @JsonProperty
    public RetryPolicyFactory getSubPolicy() {
        return subPolicy;
    }

    @JsonProperty
    public void setSubPolicy(RetryPolicyFactory subPolicy) {
        this.subPolicy = subPolicy;
    }

    @Override
    public RetryPolicy build() {
        return new LoggingRetryPolicy(subPolicy.build());
    }
}
