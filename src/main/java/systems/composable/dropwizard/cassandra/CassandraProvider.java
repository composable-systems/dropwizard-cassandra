/*
 * Copyright 2017 Composable Systems Limited
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
package systems.composable.dropwizard.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.inject.Singleton;

/**
 * Provides logic for registering factories that provide and dispose injected instances for
 * {@link javax.ws.rs.core.Context}-annotated
 * {@link Cluster} and {@link Session} classes.
 *
 * @author <a href="mailto:max@dominichenko.com">Max Dominichenko</a>
 * @see CassandraBundle
 */
class CassandraProvider {

	/**
	 * Encapsulates logic that binds {@link Cluster} and {@link Session} classes to
	 * {@link Singleton} or {@link RequestScoped} interfaces respectively.
	 */
	public static class Binder extends AbstractBinder {

		private final CassandraProvider cassandraProvider;

		Binder(CassandraFactory cassandraFactory, Environment environment) {
			cassandraProvider = new CassandraProvider(cassandraFactory, environment);
		}

		@Override
		protected void configure() {
			bindFactory(cassandraProvider.clusterFactory).to(Cluster.class).in(Singleton.class);
			bindFactory(cassandraProvider.sessionFactory).to(Session.class).in(RequestScoped.class);
		}
	}

	/**
	 * A factory for injected {@link Cluster} instance.
	 */
	public static class ClusterFactory implements Factory<Cluster> {

		private final Cluster cluster;

		ClusterFactory(Cluster cluster) {
			this.cluster = cluster;
		}

		@Override
		public Cluster provide() {
			return cluster;
		}

		@Override
		public void dispose(Cluster cluster) {
			cluster.close();
		}
	}

	/**
	 * A factory for injected {@link Session} instance.
	 */
	public static class SessionFactory implements Factory<Session> {

		private final ClusterFactory clusterFactory;
		private final String keyspace;

		SessionFactory(ClusterFactory clusterFactory, String keyspace) {
			this.clusterFactory = clusterFactory;
			this.keyspace = keyspace;
		}

		@Override
		public Session provide() {
			final Cluster cluster = clusterFactory.provide();
			return keyspace == null || keyspace.isEmpty() ? cluster.connect() : cluster.connect(keyspace);
		}

		@Override
		public void dispose(Session session) {
			session.close();
		}
	}

	static Binder binder(CassandraFactory cassandraFactory, Environment environment) {
		return new Binder(cassandraFactory, environment);
	}

	private final ClusterFactory clusterFactory;
	private final SessionFactory sessionFactory;

	CassandraProvider(CassandraFactory cassandraFactory, Environment environment) {
		clusterFactory = new ClusterFactory(cassandraFactory.build(environment));
		sessionFactory = new SessionFactory(clusterFactory, cassandraFactory.getKeyspace());
	}
}
