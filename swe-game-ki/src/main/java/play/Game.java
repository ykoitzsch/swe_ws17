package play;

public class Game{
	
	private enum GameState {START, X, Y};
	private GameState gameState;
	private int id;
	
	public Game(int id) {
		gameState = GameState.START;
		this.id = id;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	

}
