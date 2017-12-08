package repository.concrete;

import play.Game;
import play.GameLog;
import repository.RepositoryImpl;

public class GameRepository extends RepositoryImpl<Game>{

	public GameRepository() {
		super(Game.class);
	}

}
