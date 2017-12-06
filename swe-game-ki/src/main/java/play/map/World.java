package play.map;

import java.util.ArrayList;
	
	public class World {
		public enum EntityType {PLAYER1, FORTP1, CHESTP1, PLAYER2, FORTP2, CHESTP2}
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
		
		
	}

	
	
