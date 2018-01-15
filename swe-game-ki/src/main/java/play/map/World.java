package play.map;

	public class World {
		Map map;
		Map p1Map;
		Map p2Map;		
		Coordinate p1Pos;
		Coordinate p2Pos;

		public World() {}

		public Map getMap() {
			return map;
		}

		public void setMap(Map map) {
			this.map = map;
		}

		public Map getP1Map() {
			return p1Map;
		}

		public void setP1Map(Map p1Map) {
			this.p1Map = p1Map;
		}

		public Map getP2Map() {
			return p2Map;
		}

		public void setP2Map(Map p2Map) {
			this.p2Map = p2Map;
		}

		public Coordinate getP1Pos() {
			return p1Pos;
		}

		public void setP1Pos(Coordinate p1) {
			this.p1Pos = p1;
		}

		public Coordinate getP2Pos() {
			return p2Pos;
		}

		public void setP2Pos(Coordinate p2) {
			this.p2Pos = p2;
		}	
		
		
		
}

	
	
