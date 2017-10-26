package play;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

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
		this.game = new Game(id);
	}

	@Override
	public void run() {
		
		sendToClient(MessageFactory.getMessage("gamestart"),p1);
		sendToClient(MessageFactory.getMessage("gamestart"),p2);
			
			
		String s;
		try {
			s = p1.reader.readLine();
			System.out.println("From Client: " +  p1 + " " +  s);
			s = p2.reader.readLine();
			System.out.println("From Client: " +  p2 + " " +  s);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
}
