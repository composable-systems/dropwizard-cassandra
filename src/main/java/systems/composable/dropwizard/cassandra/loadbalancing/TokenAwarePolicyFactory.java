package systems.composable.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A factory for configuring and building {@link com.datastax.driver.core.policies.TokenAwarePolicy} instances.
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
 *         <td>No default. You must provide a child policy.</td>
 *         <td>The load balancing policy to wrap with token awareness.</td>
 *     </tr>
 *     <tr>
 *         <td>shuffleReplicas</td>
 *         <td>No default.</td>
 *         <td>Whether to shuffle the replicas returned by getRoutingKey.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("tokenAware")
public class TokenAwarePolicyFactory implements LoadBalancingPolicyFactory {

    @Valid
    @NotNull
    private LoadBalancingPolicyFactory subPolicy;

    private Boolean shuffleReplicas;

    @JsonProperty
    public LoadBalancingPolicyFactory getSubPolicy() {
        return subPolicy;
    }

    @JsonProperty
    public void setSubPolicy(LoadBalancingPolicyFactory subPolicy) {
        this.subPolicy = subPolicy;
    }

    @JsonProperty
    public Boolean getShuffleReplicas() {
        return shuffleReplicas;
    }

    @JsonProperty
    public void setShuffleReplicas(Boolean shuffleReplicas) {
        this.shuffleReplicas = shuffleReplicas;
    }

    @Override
    public LoadBalancingPolicy build() {
        return (shuffleReplicas == null)
                ? new TokenAwarePolicy(subPolicy.build())
                : new TokenAwarePolicy(subPolicy.build(), shuffleReplicas);
    }
}
