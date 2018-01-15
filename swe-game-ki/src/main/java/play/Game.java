package play;

import play.map.World;
import socket.PlayerConnection;

public class Game{

	private int id;
	private World world;
	private PlayerConnection winner, loser;
	private boolean draw;
	private boolean p1HasChest;
	private boolean p2HasChest;

	Game(int id){
		this.world = new World();
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public World getWorld() {
		return world;
	}
	public void setWorld(World world) {
		this.world = world;
	}
	public PlayerConnection getWinner() {
		return winner;
	}
	public void setWinner(PlayerConnection winner) {
		this.winner = winner;
	}
	public PlayerConnection getLoser() {
		return loser;
	}
	public void setLoser(PlayerConnection loser) {
		this.loser = loser;
	}
	public boolean isDraw() {
		return draw;
	}
	public void setDraw(boolean draw) {
		this.draw = draw;
	}
	public boolean isP1HasChest() {
		return p1HasChest;
	}
	public void setP1HasChest(boolean p1HasChest) {
		this.p1HasChest = p1HasChest;
	}
	public boolean isP2HasChest() {
		return p2HasChest;
	}
	public void setP2HasChest(boolean p2HasChest) {
		this.p2HasChest = p2HasChest;
	}
	
	
}
