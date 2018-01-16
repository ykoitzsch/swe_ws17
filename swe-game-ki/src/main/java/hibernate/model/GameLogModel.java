package hibernate.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="gamelog")
public class GameLogModel extends Model{

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int id;
	private String logs = "";
	
	public GameLogModel(){}

	@Override
	public int getId() {
		return id;
	}

	public String getLogs() {
		return logs;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}

}
