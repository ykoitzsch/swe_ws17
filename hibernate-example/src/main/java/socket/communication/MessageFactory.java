package socket.communication;

public class MessageFactory {

	public static XMLMessage clientIsRdy(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.SIMPLE);
		m.setDesc("READY");
		return m;
	}
	
	public static XMLMessage gameStart(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.SIMPLE);
		m.setDesc("START");
		return m;
	}
}
