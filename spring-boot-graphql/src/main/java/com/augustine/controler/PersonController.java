package com.augustine.controler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.augustine.dao.PersonRepository;
import com.augustine.entity.Person;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@RestController
public class PersonController {

	@Autowired
	private PersonRepository personRepository;

	@Value("classpath:person.graphqls")
	private Resource schemaResource;
	private GraphQL graphQL;

	@PostConstruct
	public void loadSchema() throws IOException {
		File schemaFile =schemaResource.getFile();
		TypeDefinitionRegistry registery=new SchemaParser().parse(schemaFile);
		RuntimeWiring writing= buildwriting();
		GraphQLSchema schema= new SchemaGenerator().makeExecutableSchema(registery, writing);
		graphQL=GraphQL.newGraphQL(schema).build();
	}

	private RuntimeWiring buildwriting() {

		DataFetcher<List<Person>>fetcher1=data->{
			return (List<Person>) personRepository.findAll();
		};

		DataFetcher<Person>fetcher2=data->{
			return personRepository.findByEmail(data.getArgument("email"));
		};

		return RuntimeWiring.newRuntimeWiring().type("Query",typewriting->
		typewriting.dataFetcher("getAllPerson", fetcher1).dataFetcher("findPerson", fetcher2)).build();
	}

	@PostMapping("/addperson")
	//@RequestMapping(value = "/addperson", method = RequestMethod.POST)
	public String addPerson(@RequestBody List<Person> persons) {		

		if(persons != null) {
			personRepository.saveAll(persons);	
			return "Added a person"+persons.size();
		} else {
			return "Request does not contain a body";
		}
	}
	@GetMapping("/findAllperson")
	public List<Person> getPersons() {

		return (List<Person>) personRepository.findAll();
	}

	@PostMapping("/getAll")
	public ResponseEntity<Object> getAll(@RequestBody String query){
		ExecutionResult result=graphQL.execute(query);
		return new ResponseEntity<Object>(result, HttpStatus.OK);		
	}

	@PostMapping("/getPersonByEmail")
	public ResponseEntity<Object> getPersonByEmail(@RequestBody String query){
		ExecutionResult result=graphQL.execute(query);
		return new ResponseEntity<Object>(result, HttpStatus.OK);		
	}
}
