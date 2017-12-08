package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.jboss.logging.Logger;

import play.map.Entity;
import play.map.Tile;
import play.map.World;
import play.map.World.EntityType;
import play.map.World.TileType;
import socket.communication.MessageFactory;
import socket.communication.MsgType;
import socket.communication.XMLMessage;

public class Client {

	public int port;
	private Socket socket;
	private String host;
	private BufferedReader reader;
	private PrintWriter writer;
	private boolean connected;
	private boolean ingame;
	
	public Client(int port){
		this.port = port;
		this.connected = false;
	}
	
	public void connect(){
			try {
				socket = new Socket(host,port);
				connected = true;
				this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.writer = new PrintWriter(socket.getOutputStream(),true);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void join(String gamename){
		if(connected) {
			sendToServer(MessageFactory.join(gamename));
		} 
	}
	
	public void receiveMessages(){
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(XMLMessage.class);
			Unmarshaller um = context.createUnmarshaller();
			XMLMessage msg;
			String xml = "";
			StringBuilder sb = new StringBuilder();
		
			while((xml = reader.readLine()) != null){
				sb.append(xml);
				if(xml.endsWith(XMLMessage.msgEnd)){
					msg = (XMLMessage) um.unmarshal(new StreamSource(new StringReader(sb.toString())));
					sb.setLength(0);
					check(msg);
				}		
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void check(XMLMessage m){
		if(m.getType() == MsgType.RDY){
			sendToServer(MessageFactory.clientisrdy());	
		}
		if(m.getType() == MsgType.JOIN){
			System.out.println(socket.toString() + ": Gegner hat die Verbindung beendet.");
			join("");
		}
		if(m.getType() == MsgType.GENMAP){
			sendToServer(MessageFactory.sendMap(generateMap()));
		}

	}
	
	public void sendToServer(XMLMessage xml){
		try {
			JAXBContext context = JAXBContext.newInstance(XMLMessage.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(xml, writer);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean isIngame() {
		return ingame;
	}

	public void setIngame(boolean ingame) {
		this.ingame = ingame;
	}
	
	public Tile[][] generateMap() {
		TileType x;
		if(Math.random() > 0.5) {
			x = TileType.WATER;
		}
		else x = TileType.GRASS;
		Tile[][] tiles = new Tile[8][4];
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 4; j++) {
				tiles[i][j] = new Tile(i,j, x);
			}
		}
		return tiles;
	}
}
