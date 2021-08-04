package com.augustine.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class ApplicationNotFoundException extends RuntimeException implements GraphQLError{

	private Map<String, Object> extension= new HashMap<>();
	public ApplicationNotFoundException(String message,Long invalidApplicaitonId) {
	super(message);
	extension.put(message, invalidApplicaitonId);
	}
	@Override
	public List<SourceLocation> getLocations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErrorClassification getErrorType() {
		// TODO Auto-generated method stub
		return null;
	}

}
