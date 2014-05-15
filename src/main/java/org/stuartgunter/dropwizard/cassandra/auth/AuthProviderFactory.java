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

package org.stuartgunter.dropwizard.cassandra.auth;

import com.datastax.driver.core.AuthProvider;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

/**
 * A service provider interface for creating DataStax {@link AuthProvider auth providers}.
 * <p/>
 * To create your own, just:
 * <ol>
 *     <li>Create a class which implements {@link AuthProviderFactory}.</li>
 *     <li>Annotate it with {@code @JsonTypeName} and give it a unique type name.</li>
 *     <li>Add a {@code META-INF/services/org.stuartgunter.dropwizard.cassandra.auth.AuthProviderFactory}
 *     file with your implementation's full class name to the class path.</li>
 * </ol>
 *
 * @see PlainTextAuthProviderFactory
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface AuthProviderFactory extends Discoverable {

    AuthProvider build();
}
