package play.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import play.map.algo.Node;

public class ClientGameInformation {

	private Coordinate playerPosition;
	private Coordinate opponentPosition;
	private Coordinate opponentCastlePosition;
	private Coordinate playerCastlePosition;
	private Coordinate chestPosition = new Coordinate(-1,-1);
	private int playerNumber;
	private int gameid;
	private Map clientMap;
	private ArrayList<Coordinate> possibleChestPositions = new ArrayList<>();
	private List<Node> path = new LinkedList<Node>();
	private Coordinate finalTarget;
	private boolean waiting = false;
	private boolean hasChest = false;
	
	public Coordinate getPlayerPosition() {
		return playerPosition;
	}
	public void setPlayerPosition(Coordinate playerPosition) {
		this.playerPosition = playerPosition;
	}
	public Coordinate getOpponentPosition() {
		return opponentPosition;
	}
	public void setOpponentPosition(Coordinate opponentPosition) {
		this.opponentPosition = opponentPosition;
	}
	public Map getClientMap() {
		return clientMap;
	}
	public void setClientMap(Map clientMap) {
		this.clientMap = clientMap;
	}
	public Coordinate getOpponentCastlePosition() {
		return opponentCastlePosition;
	}
	public void setOpponentCastlePosition(Coordinate opponentCastlePosition) {
		this.opponentCastlePosition = opponentCastlePosition;
	}
	public int getPlayerNumber() {
		return playerNumber;
	}
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
	public int getGameid() {
		return gameid;
	}
	public void setGameid(int gameid) {
		this.gameid = gameid;
	}
	public Coordinate getPlayerCastlePosition() {
		return playerCastlePosition;
	}
	public void setPlayerCastlePosition(Coordinate playerCastlePosition) {
		this.playerCastlePosition = playerCastlePosition;
	}
	public ArrayList<Coordinate> getPossibleChestPositions() {
		return possibleChestPositions;
	}
	public void setPossibleChestPositions(ArrayList<Coordinate> possibleChestPositions) {
		this.possibleChestPositions = possibleChestPositions;
	}
	public List<Node> getPath() {
		return path;
	}
	public void setPath(List<Node> path) {
		this.path = path;
	}
	public Coordinate getFinalTarget() {
		return finalTarget;
	}
	public void setFinalTarget(Coordinate finalTarget) {
		this.finalTarget = finalTarget;
	}
	public boolean isWaiting() {
		return waiting;
	}
	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}
	public Coordinate getChestPosition() {
		return chestPosition;
	}
	public void setChestPosition(Coordinate chestPosition) {
		this.chestPosition = chestPosition;
	}
	public boolean hasChest() {
		return hasChest;
	}
	public void setHasChest(boolean hasChest) {
		this.hasChest = hasChest;
	}
	
}

