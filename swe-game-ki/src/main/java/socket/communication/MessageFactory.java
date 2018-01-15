package socket.communication;

import play.map.Coordinate;
import play.map.Map;

public class MessageFactory {
	
	//Server --> Client -----------------------------------------------------------------
	public static XMLMessage sc_gamestart(int gameid){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.RDY);
		m.setDesc(String.valueOf(gameid));
		return m;
	}

	public static XMLMessage sc_sendMap(Map map){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.MAP);
		m.setMap(map);
		return m;
	}

	public static XMLMessage sc_makeMove(){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.MOVE);
		return m;
	}
	
	public static XMLMessage sc_makeMove(Coordinate c){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.MOVE);
		m.setCoordinate(c);
		return m;
	}
	
	public static XMLMessage sc_pregameInformation(String playernumber){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.INFO);
		m.setDesc(playernumber);
		return m;
	}
	
	public static XMLMessage sc_over(String winner){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.OVER);
		m.setDesc(winner);
		return m;
	}
	
	public static XMLMessage sc_chestCoord(Coordinate c){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.CHEST);
		m.setCoordinate(c);
		return m;
	}

	
	// Client --> Server ----------------------------------------------------------------
	public static XMLMessage cs_join(String gamename){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.JOIN);
		m.setDesc(gamename);
		return m;
	}
	
	public static XMLMessage cs_sendMap(Map map){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.MAP);
		m.setMap(map);
		return m;
	}
	
	public static XMLMessage cs_move(Coordinate target){
		XMLMessage m = new XMLMessage();
		m.setType(MsgType.MOVE);
		m.setCoordinate(target);
		return m;
	}
}
