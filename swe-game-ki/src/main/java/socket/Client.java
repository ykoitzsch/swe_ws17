package socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import socket.communication.MessageFactory;
import socket.communication.MsgType;
import socket.communication.Test;
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
			XMLMessage msg = new XMLMessage();
			msg.setType(MsgType.JOIN);
			msg.setDesc("Description");
			String l[] = new String[] {"W","W","G","G","W","W","G","G","W","W","G","G","W","W","G","G","W","W","G","G"};
			
			Test t = new Test(l);
			msg.setSendable(t);
			sendToServer(msg);
			
			//sendToServer(MessageFactory.join(gamename));
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
		if(m.getType() == MsgType.START){
			System.out.println(socket.toString() + ": Spielstart");
			sendToServer(MessageFactory.clientisrdy());		
		}
		if(m.getType() == MsgType.JOIN){
			System.out.println(socket.toString() + ": Gegner hat die Verbindung beendet.");
			join("");
		}

	}
	
	public void sendToServer(XMLMessage xml){
		try {
			JAXBContext context = JAXBContext.newInstance(Test.class, XMLMessage.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//m.marshal(xml, writer);
			m.marshal(xml, new File("TEst.xml"));
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
	
}
