package test;


import socket.Client;
import socket.Server;

public class ClientApp {

	public static void start(String gamename){

		Client c = new Client(Server.getServerInstance().getPort());
		c.connect();
		if(c.isConnected()) {
			c.join(gamename);
			c.receiveMessages();
		}
	}
}
