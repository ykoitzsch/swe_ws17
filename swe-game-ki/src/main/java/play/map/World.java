package play.map;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

	
	public class World {
		public enum EntityType {PLAYER, FORT, CHEST}
		public enum TileType {WATER, GRASS, STONE, EMPTY}

		Tile[][] tiles;
		ArrayList<Tile> tilesList = new ArrayList<Tile>();
		int width;
		int height;
		
		public World() {}
		public World(int width, int height) {
			this.height = height;
			this.width = width;
			tiles = new Tile[width][height];
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					tiles[i][j] = new Tile(i,j, TileType.EMPTY);
				}
			}
		}

		public ArrayList<Tile> getTilesList() {
			return tilesList;
		}

		public void setTilesList(ArrayList<Tile> tilesList) {
			this.tilesList = tilesList;
		}
		
		public void refreshTileList() {
			tilesList.clear();
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					tilesList.add(tiles[i][j]);
				}
			}	
		}
		
		public Tile[][] getTiles() {
			return tiles;
		}
		public void setTiles(Tile[][] tiles) {
			this.tiles = tiles;
		}
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
		public Tile getTile(int x, int y) {
			return tiles[x][y];
		}
		public void addPlayerMap(Tile[][] playerMap) {
			if(tiles[0][0].getType() == TileType.EMPTY) {
				System.out.println("Add Player1 Map to Worldmap...");
				for(int i = 0; i < playerMap.length;i++) {
					for(int j = 0; j < playerMap[0].length; j++) {
						tiles[i][j] = playerMap[i][j];
					} 
				}	
			}
			else {
				System.out.println("Add Player2 Map to Worldmap...");

				for(int i = 0; i < playerMap.length;i++) {
					for(int j = 0; j < playerMap[0].length; j++) {
						tiles[i][j+4] = playerMap[i][j];
					} 
				}	
			refreshTileList();
			placeChests();
			}
			
			System.out.println("WorldMap: ");
			for(int i = 0; i < 8;i++) {
				for(int j = 0; j < 8; j++) {
					if(tiles[j][i].hasChest() || tiles[j][i].hasFort()) {
						System.out.println("["+j+"/"+i+"] " + tiles[j][i].getType() + " CHEST");
					}
					if(tiles[j][i].hasFort()) {
						System.out.println("["+j+"/"+i+"] " + tiles[j][i].getType() + " FORT");
					}
					else {
						System.out.println("["+j+"/"+i+"] " + tiles[j][i].getType());
					}
				} 
			
			}
		
	}
		
	public void placeChests() {
		System.out.println("Placing Chest for P1...");
		tryToPlaceChest(0,7,0,3);
		
		System.out.println("Placing Chest for P2...");
		tryToPlaceChest(0,7,4,7);
	}
	
	
	
	public void tryToPlaceChest(int x1, int x2, int y1, int y2) {
		int randX = ThreadLocalRandom.current().nextInt(x1,x2+1);
		int randY = ThreadLocalRandom.current().nextInt(y1,y2+1);
		Tile t = tiles[randX][randY];
		if(t.getType() == TileType.GRASS && !t.hasFort()) {
			System.out.println("Successfully placed chest");
			tiles[t.getX()][t.getY()].getEntities().add(new Entity(EntityType.CHEST));
			return;
		}
		else {
			tryToPlaceChest(x1,x2,y1,y2);
		}
	}
}

	
	
