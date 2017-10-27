package repository.concrete;

import model.Person;
import repository.RepositoryImpl;

public class PersonRepository extends RepositoryImpl<Person>{

	public PersonRepository() {
		super(Person.class);
	}

}
