package play;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import model.Model;
import play.map.World;

@Entity
@Table(name="game")
public class Game extends Model{
	enum GameState {PREPARE, MAPX1, MAPX2, PLAY, OVER};

	@GeneratedValue
	@Id
	private int game_id;
	@Transient
	private GameState gameState;
	
	@Transient
	private World world;
	
	private String gamename;
	private String winner;
	private String loser;
	private ArrayList<GameLog> logs;



	Game(){}
	public Game(int gID) {
		gameState = GameState.PREPARE;
		this.world = new World(8,8);
		gamename = "Game "+gID;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	@Override
	public int getId() {
		return game_id;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public String getLoser() {
		return loser;
	}
	public void setLoser(String loser) {
		this.loser = loser;
	}
	public ArrayList<GameLog> getLogs() {
		return logs;
	}
	public void setLogs(ArrayList<GameLog> logs) {
		this.logs = logs;
	}

	public GameLog getLog(int id) {
		for(GameLog l : logs) {
			if(l.getId() == id) {
				return l;
			}
		}
		return null;
	}
	
	

}
