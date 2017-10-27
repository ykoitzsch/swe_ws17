package play;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import socket.PlayerConnection;
import socket.communication.MessageFactory;
import socket.communication.XMLMessage;

public class GameRequestHandler implements Runnable{
	
	private PlayerConnection p1;
	private PlayerConnection p2;
	private Game game;
	private int id;
	
	public GameRequestHandler(PlayerConnection p1, PlayerConnection p2, int id){
		this.p1 = p1;
		this.p2 = p2;
		this.id = id;
	}

	@Override
	public void run() {
		
		sendToClient(MessageFactory.gamestart(),p1);
		sendToClient(MessageFactory.gamestart(),p2);
		
		if(!ready()) {
			return;
		} 
		
		//sendToClient(MessageFactory.generateMap(),p1);
		//sendToClient(MessageFactory.generateMap(),p2);

			
		
		
		
	}
	
	public void sendToClient(XMLMessage xml, PlayerConnection p){
		try {
			JAXBContext context = JAXBContext.newInstance(XMLMessage.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(xml, p.writer);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	private boolean ready() {
		try {
			if((p1.reader.readLine()) != null){
			}else return false;
			
			if((p2.reader.readLine()) != null){
			} else return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	
		return true;
	}
}
