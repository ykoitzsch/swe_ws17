package hibernate.repository;

import java.util.HashMap;
import java.util.Map;

import hibernate.model.GameLogModel;
import hibernate.model.GameModel;
import hibernate.repository.concrete.GameLogModelRepository;
import hibernate.repository.concrete.GameModelRepository;

public class RepositoryFactory {

	private static final Map<Class, IRepository> CLASSMAP = new HashMap<>();
	
	static{
		CLASSMAP.put(GameModel.class, new GameModelRepository());
		CLASSMAP.put(GameLogModel.class, new GameLogModelRepository());
	}
	
	public static IRepository getRepository(Class model){
		return CLASSMAP.get(model);
	}
	
}
