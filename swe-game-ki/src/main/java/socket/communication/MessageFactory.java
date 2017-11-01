package socket.communication;


public class MessageFactory {
	
	//Server --> Client -----------------------------------------------------------------
	public static XMLMessage gamestart(String info){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.START);
		m.setDesc(info);
		return m;
	}
	
	public static XMLMessage gamestart(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.START);
		return m;
	}
	
	public static XMLMessage rejoin(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.JOIN);
		return m;
	}
	
	public static XMLMessage generateMap(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.GENMAP);
		return m;
	}
	
	// Client --> Server ----------------------------------------------------------------
	public static XMLMessage join(String gamename){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.JOIN);
		m.setDesc(gamename);
		return m;
	}
	
	public static XMLMessage clientisrdy(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.TEXT);
		return m;
	}
	
	public static XMLMessage test() {
		XMLMessage m = new XMLMessage();
		String s = "hallo";
		m.setSendable(s);
		return m;
	}
}
