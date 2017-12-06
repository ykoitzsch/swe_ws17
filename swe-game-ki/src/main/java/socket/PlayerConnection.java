package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerConnection{

	public PrintWriter writer;
	public BufferedReader reader;
	private Socket socket;
	private String tempName;
	
	public PlayerConnection(Socket socket){
		try {
			this.socket = socket;
			this.writer = new PrintWriter(socket.getOutputStream(),true);
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	
	public void close() throws IOException{
		writer.close();
		reader.close();
		socket.close();
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}	
	
	
}
