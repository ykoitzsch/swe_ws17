package socket.communication;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class Test {

	private String[] tiles;
	
	Test(){}
	public Test(String[] f) {
		this.tiles = f;
	}
	public String[] getTiles() {
		return tiles;
	}
	public void setTiles(String[] tiles) {
		this.tiles = tiles;
	}
	
	
	
	
}