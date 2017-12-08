package play.map;

import play.map.World.EntityType;

public class Entity{
	enum Owner{P1,P2}
	private EntityType entity;
	private Owner owner;
	int x;
	int y;

	Entity(){}
	public Entity(EntityType eType){
		entity = eType;
	}
	public EntityType getEntity() {
		return entity;
	}
	public void setEntity(EntityType entity) {
		this.entity = entity;
	}
	public Owner getOwner() {
		return owner;
	}
	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	
}