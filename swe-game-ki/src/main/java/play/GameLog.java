package play;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import model.Model;

@Entity
@Table(name="game_log")
public class GameLog extends Model{
	@Id
	@GeneratedValue
	int id;
	int game_id;
	String action;
	Timestamp timestamp;
	
	GameLog(){}
	public GameLog(int game_id, String action) {
		this.game_id = game_id;
		this.action = action;
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}
	
	@Override
	public int getId() {
		return id;
	}
	public int getGame_id() {
		return game_id;
	}
	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	

	
	
	

}
