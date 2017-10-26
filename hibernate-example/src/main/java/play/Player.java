package play;

import socket.PlayerConnection;

public class Player {

	private PlayerConnection connection;
	private String playerName;
	
	public Player(PlayerConnection pc, String playerName){
		this.connection = pc;
		this.playerName = playerName;
	}

	public PlayerConnection getPlayerConnection() {
		return connection;
	}

	public void setPlayerConnection(PlayerConnection playerConnection) {
		this.connection = playerConnection;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	
}
