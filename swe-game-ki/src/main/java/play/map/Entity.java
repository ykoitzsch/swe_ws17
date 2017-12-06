package play.map;

import play.map.World.EntityType;

public class Entity{
	private EntityType entity;
	Entity(){}
	Entity(EntityType eType){
		entity = eType;
	}
	public EntityType getEntity() {
		return entity;
	}
	public void setEntity(EntityType entity) {
		this.entity = entity;
	}
	
	
}