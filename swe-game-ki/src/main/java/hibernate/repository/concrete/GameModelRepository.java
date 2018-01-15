package hibernate.repository.concrete;

import hibernate.model.GameModel;
import hibernate.repository.RepositoryImpl;

public class GameModelRepository extends RepositoryImpl<GameModel>{

	public GameModelRepository() {
		super(GameModel.class);
	}

}
