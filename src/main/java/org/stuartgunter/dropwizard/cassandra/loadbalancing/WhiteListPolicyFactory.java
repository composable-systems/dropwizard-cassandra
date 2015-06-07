package org.stuartgunter.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.WhiteListPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * A factory for configuring and building {@link com.datastax.driver.core.policies.WhiteListPolicy} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>childPolicy</td>
 *         <td>No default. You must provide a child policy.</td>
 *         <td>The wrapped policy.</td>
 *     </tr>
 *     <tr>
 *         <td>whiteList</td>
 *         <td>No default.</td>
 *         <td>The white listed hosts.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("whiteList")
public class WhiteListPolicyFactory implements LoadBalancingPolicyFactory {

    @Valid
    @NotNull
    private LoadBalancingPolicyFactory childPolicy;

    @NotNull
    private Collection<InetSocketAddress> whiteList;

    @JsonProperty
    public LoadBalancingPolicyFactory getChildPolicy() {
        return childPolicy;
    }

    @JsonProperty
    public void setChildPolicy(LoadBalancingPolicyFactory childPolicy) {
        this.childPolicy = childPolicy;
    }

    @JsonProperty
    public Collection<InetSocketAddress> getWhiteList() {
        return whiteList;
    }

    @JsonProperty
    public void setWhiteList(Collection<InetSocketAddress> whiteList) {
        this.whiteList = whiteList;
    }

    @Override
    public LoadBalancingPolicy build() {
        return new WhiteListPolicy(childPolicy.build(), whiteList);
    }
}
