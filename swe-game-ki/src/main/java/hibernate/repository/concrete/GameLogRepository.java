package repository.concrete;

import play.GameLog;
import repository.RepositoryImpl;

public class GameLogRepository extends RepositoryImpl<GameLog>{

	public GameLogRepository() {
		super(GameLog.class);
	}

}
