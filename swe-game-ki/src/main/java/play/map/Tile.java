package play.map;

import java.util.ArrayList;

import play.map.World.EntityType;
import play.map.World.TileType;

public class Tile {
	private int x;
	private int y;
	private TileType type;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	Tile(){}
	public Tile(int x, int y, TileType type){
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public TileType getType() {
		return type;
	}
	public void setType(TileType type) {
		this.type = type;
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}
	
	public boolean hasFort() {
		for(Entity e : entities) {
			if(e.getEntity() == EntityType.FORT) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasChest() {
		for(Entity e : entities) {
			if(e.getEntity() == EntityType.CHEST) {
				return true;
			}
		}
		return false;
	}
	
	
}
