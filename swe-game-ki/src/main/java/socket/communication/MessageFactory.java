package socket.communication;

import play.map.Tile;

public class MessageFactory {
	
	//Server --> Client -----------------------------------------------------------------
	public static XMLMessage gamestart(String info){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.RDY);
		m.setDesc(info);
		return m;
	}
	
	public static XMLMessage gamestart(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.RDY);
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
		m.setType(MsgType.RDY);
		return m;
	}
	
	public static XMLMessage sendMap(Tile[][] tiles){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.MAP);
		m.setTiles(tiles);
		return m;
	}

}
