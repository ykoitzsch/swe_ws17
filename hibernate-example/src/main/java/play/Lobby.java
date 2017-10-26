package play;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import socket.PlayerConnection;

public class Lobby {

	private PlayerConnection player1;
	private PlayerConnection player2;
	
	private int playerCount = 0;
	private ExecutorService threadPool;
	private GameRequestHandler gameHandler;
	private int gameCount = 0;
		
	public Lobby(){
		this.threadPool = Executors.newCachedThreadPool();
	}
	
	public void addPlayerConnection(Socket socket){
		if(playerCount++ == 0){
			player1 = new PlayerConnection(socket);
			System.out.println(socket + " has joined the lobby");
			System.out.println(socket + " is waiting for another player");
		}
		else{ 
			player2 = new PlayerConnection(socket);
			gameHandler = new GameRequestHandler(player1, player2, gameCount++);
			playerCount = 0;
			
			System.out.println(socket + " is the second player. Match starts now...");
			threadPool.execute(gameHandler);
		}
	}
}
