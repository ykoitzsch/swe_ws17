package socket.communication;

public class MessageFactory {

	
	public static XMLMessage getMessage(String s) {
		
	}
	
	
	public static XMLMessage clientisrdy(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.SIMPLE);
		m.setDesc("READY");
		return m;
	}
	
	public static XMLMessage gamestart(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.SIMPLE);
		m.setDesc("START");
		return m;
	}
}
