package play;

public class Game{
	
	enum GameState {PREPARE, MAPX, PLAY, OVER};
	private GameState gameState;
	private int id;
	
	Game(int id) {
		gameState = GameState.PREPARE;
		this.id = id;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	

}
