package socket.communication;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import play.map.Tile;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
public class XMLMessage {
	
	public static String msgEnd = "</xmlMessage>";
	private MsgType type;
	private String desc;
	//private List<?> sendable = new ArrayList<Object>();
	//private World world;
	private Tile[][] tiles;
	
	public MsgType getType() {
		return type;
	}
	public void setType(MsgType type) {
		this.type = type;
	}
//	public List<?> getSendable() {
//		return sendable;
//	}
//	public void setSendable(List<?> sendable) {
//		this.sendable = sendable;
//	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Tile[][] getTiles() {
		return tiles;
	}
	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}
	
	
//	public World getWorld() {
//		return world;
//	}
//	public void setWorld(World world) {
//		this.world = world;
//	}
	
	
	

}
