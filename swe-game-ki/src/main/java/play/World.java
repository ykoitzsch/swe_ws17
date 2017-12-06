package play;

import java.util.ArrayList;
	
	public class World {
		enum TileType{WATER, GRASS, STONE}
		enum EntityType {PLAYER1, FORTP1, CHESTP1, PLAYER2, FORTP2, CHESTP2}

		Tile[][] tiles;
		
		public World(int width, int height) {
			tiles = new Tile[width][height];
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					tiles[i][j] = new Tile(i,j);
				}
			}
		}
		
		class Tile{
			int x;
			int y;
			TileType type;
			ArrayList<Entity> entities = new ArrayList<Entity>();
			Tile(int x, int y){
				this.x = x;
				this.y = y;
			}
		}
		
		class Entity{
			EntityType entity;
			Entity(EntityType eType){
				entity = eType;
			}
		}
	}
	
	
