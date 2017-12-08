package repository;

import java.util.HashMap;
import java.util.Map;

import play.Game;
import play.GameLog;
import repository.concrete.GameLogRepository;
import repository.concrete.GameRepository;

public class RepositoryFactory {

	private static final Map<Class, IRepository> CLASSMAP = new HashMap<>();
	
	static{
		CLASSMAP.put(Game.class, new GameRepository());
		CLASSMAP.put(GameLog.class, new GameLogRepository());
	}
	
	public static IRepository getRepository(Class model){
		return CLASSMAP.get(model);
	}
	
}
