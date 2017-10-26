package repository;

import java.util.HashMap;
import java.util.Map;

import model.Person;
import repository.concrete.PersonRepository;

public class RepositoryFactory {

	private static final Map<Class, IRepository> CLASSMAP = new HashMap<>();
	
	static{
		CLASSMAP.put(Person.class, new PersonRepository());
	}
	
	public static IRepository getRepository(Class model){
		return CLASSMAP.get(model);
	}
	
}
