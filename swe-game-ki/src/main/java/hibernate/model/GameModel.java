package hibernate.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="game")
public class GameModel extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int id;
	private String winner;
	private String loser;
	private boolean draw;
	private String map;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	private GameLogModel gamelog;
	
	public GameModel() {}

	@Override
	public int getId() {
		return id;
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

	public boolean isDraw() {
		return draw;
	}

	public void setDraw(boolean draw) {
		this.draw = draw;
	}
	public String getMap() {
		return map;
	}
	public void setMap(String map) {
		this.map = map;
	}
	public GameLogModel getGamelog() {
		return gamelog;
	}
	public void setGamelog(GameLogModel gamelog) {
		this.gamelog = gamelog;
	}
	
}
