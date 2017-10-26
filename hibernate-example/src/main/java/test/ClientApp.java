package test;


import socket.Client;
import socket.Server;

public class ClientApp {

	public static void start(){

		Client c = new Client(Server.getServerInstance().getPort());
		c.connect();
	}
}
