package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


import play.Game;
import play.GameRequestHandler;
import play.Lobby;
import test.ClientApp;

public class Server {

	private final int PORT = 4444;
	private static Server server = new Server();
	public ServerSocket serverSocket;
	protected Socket clientSocket;
	protected boolean isRunning;
	private static Logger LOGGER = Logger.getLogger(Server.class.getName());
	static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

	
	private Server(){}

	public static Server getServerInstance(){
		return server;
	}
	
	public void start(){
		try {
			fileTxt = new FileHandler("Logs.txt");
			formatterTxt = new SimpleFormatter();
	        fileTxt.setFormatter(formatterTxt);
	        LOGGER.addHandler(fileTxt);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.isRunning = true;
		try{
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen(){
		while(isRunning){
			try {
				clientSocket = serverSocket.accept();
				LOGGER.info(clientSocket + " connected to server");
				System.out.println(clientSocket + " connected to the server");
				new Thread(new JoinGameHandler(clientSocket)).start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean getIsRunning(){
		return this.isRunning;
	}
	
	public void setIsRunning(boolean value){
		this.isRunning = value;
	}

	public int getPort() {
		return PORT;
	}
	
}
