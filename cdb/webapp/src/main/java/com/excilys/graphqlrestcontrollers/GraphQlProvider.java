package com.excilys.graphqlrestcontrollers;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;

@Component
public class GraphQlProvider {
	@Component
	public class GraphQLProvider {

		private GraphQL graphQL;

		@Bean
		public GraphQL graphQL() {
			return this.graphQL;
		}

		@PostConstruct
		public void init() throws IOException {
			URL url = Resources.getResource("schema.graphqls");
			String sdl = Resources.toString(url, Charsets.UTF_8);
			GraphQLSchema graphQLSchema = buildSchema(sdl);
			this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
		}

		@Autowired
		GraphQLDataFetchers graphQLDataFetchers;

		private GraphQLSchema buildSchema(final String sdl) {
			TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
			RuntimeWiring runtimeWiring = buildWiring();
			SchemaGenerator schemaGenerator = new SchemaGenerator();
			return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
		}

		private RuntimeWiring buildWiring() {
			return RuntimeWiring.newRuntimeWiring().type(TypeRuntimeWiring.newTypeWiring("Query")
					.dataFetcher("computerById", this.graphQLDataFetchers.getComputerById())).build();
		}

	}
}
