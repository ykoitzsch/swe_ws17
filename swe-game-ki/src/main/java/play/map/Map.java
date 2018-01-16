package play.map;

import java.util.Random;

public class Map {
	
	private int height;
	private int width;
	private String[][] grid;
	
	Map() {}
	public Map(int w, int h) {
		this.height = h;
		this.width = w;
		grid = new String[w][h];
	}
	
	public void populate(int mountain, int water) {
		try {
			for(int x = 0; x < grid.length; x++) {
				for(int y = 0; y < grid[0].length; y++) {
					grid[x][y] = "G";
				}
			}	
			
			int randX = new Random().nextInt(8);
			int randY = new Random().nextInt(4);
			for(int i = 0; i < mountain; i++) {
				placeMountains(randX, randY);
			}
			
			randX = new Random().nextInt(8);
			randY = new Random().nextInt(4);
			for(int i = 0; i < water; i++) {
				placeWater(randX, randY);
			}
			
			placeFort();
			
		} 
		catch(StackOverflowError e){
			populate(mountain, water);
		}	
	}
	
	private void placeMountains(int x, int y) {
		if(grid[x][y] == "G") {
			grid[x][y] = "M";
			return;		
		}
		placeMountains(new Random().nextInt(8), new Random().nextInt(4));
	}
	
	private void placeWater(int x, int y) {
		if(grid[x][y] == "G") {
			grid[x][y] = "W";
			if(hasIslands(grid)) {
				grid[x][y] = "G";
				placeWater(new Random().nextInt(8), new Random().nextInt(4));
			}else if(invalidBorder(grid)) {
				grid[x][y] = "G";
				placeWater(new Random().nextInt(8), new Random().nextInt(4));
			}
			else return;
		}
		else placeWater(new Random().nextInt(8), new Random().nextInt(4));
	}
	
	private void merge(int x, int y, String[][] grid){
		if (x < 0 || x == grid.length || y < 0 || y == grid[x].length || grid[x][y] == "W" || grid[x][y] == "M")
            return;
		
        grid[x][y] = "W";
        merge(x+1, y, grid); 
        merge(x-1, y, grid); 
        merge(x, y+1, grid); 
        merge(x, y-1, grid);
	}
	
	public void placeFort() {
		int direction = 1;
		int counter = 0;
		int repeat = 1;
		int x = width/2;
		int y = height/2;
		//1 = go left
		//2 = go up
		//3 = go right
		//4 = go down
		for(int i = 0; i < 64; i++) {
			for(int j = 0; j < repeat; j++) {
				if(grid[x][y] == "G") {
					grid[x][y] = "F";
					return;
				}
				switch(direction) {
					case 1: x-=1; break;
					case 2: y-=1; break;
					case 3: x+=1; break;
					case 4: y+=1; break;
				}
				
			}
			counter++;
			direction++;
			if(counter % 2 == 0) repeat++;
			
		}
	}
	
	private boolean invalidBorder(String[][] grid) {
		int count = 0;
		for(int i = 0; i < 8; i++) {
			if(grid[i][0] == "W") {
				count++;
				if(count > 3) {
					return true;
				}
			}
		}
		
		count = 0;
		for(int i = 0; i < 8; i++) {
			if(grid[i][3] == "W") {
				count++;
				if(count > 3) {
					return true;
				}
			}
		}
		
		count = 0;
		for(int i = 0; i < 4; i++) {
			if(grid[0][i] == "W") {
				count++;
				if(count > 1) {
					return true;
				}
			}
		}
		
		count = 0;
		for(int i = 0; i < 4; i++) {
			if(grid[7][i] == "W") {
				count++;
				if(count > 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean hasIslands(String[][] grid) {
		
		String[][] grid_copy = new String[width][height];
		
		for(int x = 0; x < grid.length; x++) {
			for(int y = 0; y < grid[0].length; y++) {
				grid_copy[x][y] = grid[x][y];
			} 
		}
		int count = 0;
		
		for(int x = 0; x < grid.length; x++) {
			for(int y = 0; y < grid[0].length; y++) {
				if(grid_copy[x][y] == "G") {
					count++;
					merge(x,y,grid_copy);
				}
			
			}
		}
		if(count > 1) return true;
		else return false;
	}
	
	
	
	public void setXY(int x, int y, String s) {
		grid[x][y] = s;
	}
	
	public String getXY(int x, int y) {
		return grid[x][y];
	}
	
	public Coordinate getXY(String s) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(grid[x][y].equals(s)) {
					return new Coordinate(x,y);
				}
			}
		}
		return new Coordinate(-1,-1);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String[][] getGrid() {
		return grid;
	}

	public void setGrid(String[][] grid) {
		this.grid = grid;
	}	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				sb.append("  "  +grid[x][y]);
				if(x == width-1) sb.append("\r");
			}
		}
		return sb.toString();
	}
}
