package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server {

	private final int PORT = 3333;
	private static Server server = new Server();
	public ServerSocket serverSocket;
	protected Socket clientSocket;
	protected boolean isRunning;
	private static final Logger logger = Logger.getLogger(Server.class.getName());
	
	private Server(){}

	public static Server getServerInstance(){
		return server;
	}
	
	public void start(){
		this.isRunning = true;
		try{
			serverSocket = new ServerSocket(PORT);
			logger.log(Level.FINE, "test");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen(){
		while(isRunning){
			try {
				clientSocket = serverSocket.accept();
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
