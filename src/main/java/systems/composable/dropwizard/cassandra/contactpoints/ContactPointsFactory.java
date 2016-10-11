/*
 * Copyright Homeaway, Inc 2016. All Rights Reserved.
 * No unauthorized use of this software.
 */

package systems.composable.dropwizard.cassandra.contactpoints;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

/**
 * @see StaticHostContactsPointFactory
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface ContactPointsFactory extends Discoverable {

    String[] build();
}
