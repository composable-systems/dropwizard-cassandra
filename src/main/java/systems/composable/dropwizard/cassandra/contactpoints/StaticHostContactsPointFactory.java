/*
 * Copyright Homeaway, Inc 2016. All Rights Reserved.
 * No unauthorized use of this software.
 */

package systems.composable.dropwizard.cassandra.contactpoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 */
@JsonTypeName("static")
public class StaticHostContactsPointFactory implements ContactPointsFactory {

    @NotEmpty
    private String[] contactPoints;

    @JsonProperty
    public String[] getContactPoints() {
        return contactPoints;
    }

    @JsonProperty
    public void setContactPoints(String[] contactPoints) {
        this.contactPoints = contactPoints;
    }

    @Override
    public String[] build() {
        return this.contactPoints;
    }
}
