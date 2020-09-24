package com.excilys.graphqlrestcontrollers;

import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.adapters.ComputerAdapter;

import graphql.schema.DataFetcher;

public class GraphQLDataFetchers {

	@Autowired
	ComputerAdapter service;

	public DataFetcher getComputerById() {
		return dataFetchingEnvironment -> {
			String strId = dataFetchingEnvironment.getArgument("id");
			Long id = Long.parseLong(strId);
			return this.service.findById(id).orElse(null);
		};
	}
}
