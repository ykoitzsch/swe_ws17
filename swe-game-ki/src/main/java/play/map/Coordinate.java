package play.map;

public class Coordinate {

	private int x;
	private int y;
	
	Coordinate(){}
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
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
	
	public String toString() {
		return "["+x+"/"+y+"]";
	}
	
	@Override
	public boolean equals(Object o){
		if (o == null)
			return false;
		if (!(o instanceof Coordinate))
			return false;
		if (o == this)
			return true;

		Coordinate c = (Coordinate) o;
		if (c.getX() == x && c.getY() == y)
			return true;
		return false;
	}
	
	
}
