package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import play.Lobby;
import socket.communication.MsgType;
import socket.communication.XMLMessage;

public class JoinGameHandler implements Runnable{

	private BufferedReader reader;
	private Socket socket;
	boolean active = true;
	
	public JoinGameHandler(Socket clientSocket) {
		try {
			this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.socket = clientSocket;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {	
			JAXBContext context;
			context = JAXBContext.newInstance(XMLMessage.class);
			Unmarshaller um = context.createUnmarshaller();
			XMLMessage msg;
			String xml = "";
			StringBuilder sb = new StringBuilder();
			while(active) {
				if((xml = reader.readLine()) != null){
					sb.append(xml);
						if(xml.endsWith(XMLMessage.msgEnd)){
							active = false;
							msg = (XMLMessage) um.unmarshal(new StreamSource(new StringReader(sb.toString())));
							sb.setLength(0);
							check(msg);
						}		
				}		
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void check(XMLMessage m){
		if(m.getType() == MsgType.JOIN){
			Lobby lobby = Lobby.getLobbyInstance();
			if(m.getDesc().isEmpty()) {
				lobby.addPlayerConnection(socket);
				this.reader = null;
				this.socket = null;
			}
			else {
				lobby.addPlayerConnection(socket, m.getDesc());
				this.reader = null;
				this.socket = null;
			}		
		}
	}
}
