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
	
	public static XMLMessage sendCompletelMap(Tile[][] tiles){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.XMAP);
		m.setTiles(tiles);
		return m;
	}
	
	public static XMLMessage move(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.MOVE);
		return m;
	}
	
	public static XMLMessage defeat(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.DEFEAT);
		return m;
	}
	
	public static XMLMessage victory(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.VICTORY);
		return m;
	}
	
	public static XMLMessage foundChest(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.CHEST);
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
