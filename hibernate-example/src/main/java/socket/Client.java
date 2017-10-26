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

import socket.communication.MessageFactory;
import socket.communication.MsgType;
import socket.communication.XMLMessage;

public class Client {

	public int port;
	private Socket socket;
	private String host;
	private BufferedReader reader;
	private PrintWriter writer;
	private final String XMLEnd = "</xmlMessage>";
	
	public Client(int port){
		this.port = port;
	}
	
	public void connect(){
			try {
				socket = new Socket(host,port);
				this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.writer = new PrintWriter(socket.getOutputStream(),true);
				receiveMessages();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
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
				if(xml.endsWith(XMLEnd)){
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
		if(m.getType() == MsgType.SIMPLE){
			if(m.getDesc().equals("START")){ //kann man dann mit hashmap lösen
				sendToServer(MessageFactory.clientIsRdy());
			}	
		}
		else if(m.getType() == MsgType.COMPLEX){
			
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
}
