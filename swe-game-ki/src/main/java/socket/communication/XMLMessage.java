package socket.communication;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import play.map.Coordinate;
import play.map.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
public class XMLMessage {
	
	public static String msgEnd = "</xmlMessage>";
	private MsgType type;
	private String desc;
	private Map map;
	private Coordinate coordinate;
	
	public MsgType getType() {
		return type;
	}
	public void setType(MsgType type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	public Coordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
}
