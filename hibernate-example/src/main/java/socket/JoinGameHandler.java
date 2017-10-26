package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class JoinGameHandler implements Runnable{

	private BufferedReader reader;
	
	public JoinGameHandler(Socket clientSocket) {
		try {
			this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String msg;
		while(Server.getServerInstance().getIsRunning())
			try {
				msg = reader.readLine();		
				check(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private void check(String msg){
		
	}

}
