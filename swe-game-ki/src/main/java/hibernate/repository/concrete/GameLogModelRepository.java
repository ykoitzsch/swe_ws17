package hibernate.repository.concrete;

import hibernate.model.GameLogModel;
import hibernate.repository.RepositoryImpl;

public class GameLogModelRepository extends RepositoryImpl<GameLogModel>{

	public GameLogModelRepository() {
		super(GameLogModel.class);
	}

}
