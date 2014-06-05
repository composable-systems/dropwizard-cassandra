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

package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class CassandraResource {

    private final Session session;

    public CassandraResource(Session session) {
        this.session = session;
    }

    @GET
    @Path("/query")
    public List<String> query() {
        final ResultSet resultSet = session.execute("SELECT * FROM SYSTEM.SCHEMA_COLUMNFAMILIES;");
        return FluentIterable.from(resultSet.all())
                .transform(new Function<Row, String>() {
                    @Override
                    public String apply(Row input) {
                        return input.getString(0);
                    }
                })
                .toList();
    }
}
