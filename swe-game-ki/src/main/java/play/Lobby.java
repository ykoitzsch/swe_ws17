package play;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import socket.PlayerConnection;

public class Lobby {

	private PlayerConnection player1;
	private PlayerConnection player2;
	private HashMap<String, PlayerConnection> queue = new HashMap<String, PlayerConnection>();
	
	private int playerCount = 0;
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	private GameManager gameHandler;
	private int gameCount = 0;
	private static Lobby lobbyInstance = new Lobby();
		
	private Lobby(){}
	
	public static Lobby getLobbyInstance(){
		return lobbyInstance;
	}
	
	public synchronized void addPlayerConnection(Socket socket){
		if(playerCount++ == 0){
			player1 = new PlayerConnection(socket);
		}
		else{ 
			player2 = new PlayerConnection(socket);
			startNewGame(player1, player2, gameCount++);
			playerCount = 0;
		}
	}
	
	public synchronized void addPlayerConnection(Socket socket, String gamename) {
		if(queue.containsKey(gamename)) {
			startNewGame(queue.get(gamename),new PlayerConnection(socket),gameCount++);
		}
		else {
			queue.put(gamename, new PlayerConnection(socket));
		}
	}
	
	public synchronized void startNewGame(PlayerConnection p1, PlayerConnection p2, int gameID) {
		gameHandler = new GameManager(p1, p2, gameID);
		threadPool.execute(gameHandler);
	}
}
