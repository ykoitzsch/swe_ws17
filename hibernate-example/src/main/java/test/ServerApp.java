package test;

import socket.Server;

public class ServerApp {
	
	public static void start(){
		Server s = Server.getServerInstance();
		s.start();
		s.listen();
	}
}
