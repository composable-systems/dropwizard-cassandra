package systems.composable.dropwizard.cassandra.loadbalancing;

import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A factory for configuring and building {@link com.datastax.driver.core.policies.DCAwareRoundRobinPolicy} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>localDC</td>
 *         <td>No default.</td>
 *         <td>The name of the local datacenter (as known by Cassandra).</td>
 *     </tr>
 *     <tr>
 *         <td>usedHostsPerRemoteDC</td>
 *         <td>No default. May only be specified when localDC is specified.</td>
 *         <td>The number of host per remote datacenter that policies created by the returned factory should consider.</td>
 *     </tr>
 *     <tr>
 *         <td>allowRemoteDCsForLocalConsistencyLevel</td>
 *         <td>No default. May only be specified when localDC and usedHostsPerRemoteDC are specified.</td>
 *         <td>Whether or not the policy may return remote host when building query plan for query having consitency LOCAL_ONE and LOCAL_QUORUM.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("dcAwareRoundRobin")
public class DCAwareRoundRobinPolicyFactory implements LoadBalancingPolicyFactory {

    private String localDC;
    private Integer usedHostsPerRemoteDC;
    private Boolean allowRemoteDCsForLocalConsistencyLevel;

    @JsonProperty
    public String getLocalDC() {
        return localDC;
    }

    @JsonProperty
    public void setLocalDC(String localDC) {
        this.localDC = localDC;
    }

    @JsonProperty
    public Integer getUsedHostsPerRemoteDC() {
        return usedHostsPerRemoteDC;
    }

    @JsonProperty
    public void setUsedHostsPerRemoteDC(Integer usedHostsPerRemoteDC) {
        this.usedHostsPerRemoteDC = usedHostsPerRemoteDC;
    }

    @JsonProperty
    public Boolean getAllowRemoteDCsForLocalConsistencyLevel() {
        return allowRemoteDCsForLocalConsistencyLevel;
    }

    @JsonProperty
    public void setAllowRemoteDCsForLocalConsistencyLevel(Boolean allowRemoteDCsForLocalConsistencyLevel) {
        this.allowRemoteDCsForLocalConsistencyLevel = allowRemoteDCsForLocalConsistencyLevel;
    }

    @Override
    public LoadBalancingPolicy build() {
        DCAwareRoundRobinPolicy.Builder builder = DCAwareRoundRobinPolicy.builder();

        if (allowRemoteDCsForLocalConsistencyLevel == Boolean.TRUE) {
            builder.allowRemoteDCsForLocalConsistencyLevel();
        }

        if (localDC != null) {
            builder.withLocalDc(localDC);
        }

        if (usedHostsPerRemoteDC != null) {
            builder.withUsedHostsPerRemoteDc(usedHostsPerRemoteDC);
        }

        return builder.build();
    }
}
