package play;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import play.Game.GameState;
import play.map.Entity;
import play.map.Tile;
import play.map.World.EntityType;
import play.map.World.TileType;
import repository.IRepository;
import repository.RepositoryFactory;
import socket.PlayerConnection;
import socket.communication.MessageFactory;
import socket.communication.MsgType;
import socket.communication.XMLMessage;

public class GameRequestHandler implements Runnable{
	
	private PlayerConnection p1;
	private PlayerConnection p2;
	private PlayerConnection activePlayer;
	private Game game;
	private long startTime;
	private Map<GameState, MsgType> expectedCommand = new HashMap<GameState, MsgType>();
	IRepository<GameLog> gamelogRep = RepositoryFactory.getRepository(GameLog.class);
	IRepository<Game> gameRep = RepositoryFactory.getRepository(Game.class);
	int steps = 0;
	private static Logger LOGGER = Logger.getLogger(GameRequestHandler.class.getName());
	static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;
	
	public GameRequestHandler(PlayerConnection p1, PlayerConnection p2, int id){
		this.p1 = p1;
		this.p2 = p2;
		activePlayer = p1;
		p1.setTempName(id + "Player1");
		p2.setTempName(id + "Player2");
		expectedCommand.put(GameState.PREPARE, MsgType.RDY);
		expectedCommand.put(GameState.MAPX1, MsgType.MAP);
		expectedCommand.put(GameState.MAPX2, MsgType.MOVE);
		expectedCommand.put(GameState.PLAY, MsgType.MOVE);
		game = new Game(id);
		try {
			fileTxt = new FileHandler("Logs.txt");
			formatterTxt = new SimpleFormatter();
	        fileTxt.setFormatter(formatterTxt);
	        LOGGER.addHandler(fileTxt);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {	
			System.out.println("---------------------GameState.PREPARE---------------------");
			LOGGER.info("game started");
			sendToClient(MessageFactory.gamestart(),activePlayer);
			listen();
			
	}
	
	public void sendToClient(XMLMessage xml, PlayerConnection p){
		try {
			JAXBContext context = JAXBContext.newInstance(XMLMessage.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(xml, p.writer);
			startTime = System.nanoTime();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	private void listen() {
		try {
			JAXBContext context = JAXBContext.newInstance(XMLMessage.class);
			Unmarshaller um = context.createUnmarshaller();
			XMLMessage msg;
			String xml = "";
			StringBuilder sb = new StringBuilder();
		
			while((xml = activePlayer.reader.readLine()) != null){
				sb.append(xml);
				//System.out.println(xml);
				if(xml.endsWith(XMLMessage.msgEnd)){
					msg = (XMLMessage) um.unmarshal(new StreamSource(new StringReader(sb.toString())));
					sb.setLength(0);
					// ---- //
					checkTime();
					LOGGER.info("received message from " + activePlayer);
					LOGGER.info("inspecting message with type " + msg.getType());
					inspectMessage(msg);
					toggleActivePlayer();
					LOGGER.info("notify player");
					notifyPlayer();
				}		
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void checkTime() {
		double elapsed = (System.nanoTime() - startTime)/1000000000.0;
		if(elapsed > 3) {
			sendToClient(MessageFactory.defeat(),activePlayer);
			toggleActivePlayer();
			sendToClient(MessageFactory.defeat(),activePlayer);	
		}	
	}
	
	private void toggleActivePlayer() {
		if(activePlayer == p1) {
			LOGGER.info("Active player was p1, now its p2 turn");
			activePlayer = p2;
		}
		else {
			activePlayer = p1;
			LOGGER.info("Active player was p2, now its p1 turn");
			updateGameState();
		}
		System.out.println("It's " + activePlayer.getTempName() + " turn");
	}
	
	public void updateGameState() {
		System.out.println("Update GameState...");
		if(game.getGameState() == GameState.PREPARE) {
			System.out.println("---------------------GameState.MAPX1---------------------");
			game.setGameState(GameState.MAPX1);
		}
		else if(game.getGameState() == GameState.MAPX1) {
			System.out.println("---------------------GameState.MAPX2---------------------");
			game.setGameState(GameState.MAPX2);	
		}
		else if(game.getGameState() == GameState.MAPX2) {
			System.out.println("---------------------GameState.PLAY---------------------");
			game.setGameState(GameState.PLAY);	
		}
	}

	public void notifyPlayer() {
		System.out.println("Notify Client...");
		if(game.getGameState() == GameState.PREPARE) {
			sendToClient(MessageFactory.gamestart(),activePlayer);
		}
		else if(game.getGameState() == GameState.MAPX1) {
			sendToClient(MessageFactory.generateMap(),activePlayer);
		}
		else if(game.getGameState() == GameState.MAPX2) {
			sendToClient(MessageFactory.sendCompletelMap(game.getWorld().getTiles()),activePlayer);
		}
		else if(game.getGameState() == GameState.PLAY) {
			steps++;
			if(steps > 200) {
				sendToClient(MessageFactory.defeat(),activePlayer);
				toggleActivePlayer();
				sendToClient(MessageFactory.victory(),activePlayer);	
			}
			else
				sendToClient(MessageFactory.move(),activePlayer);
		}
		
	}
	
	public void inspectMessage(XMLMessage msg){
		if(msg.getType() == expectedCommand.get(game.getGameState())) {
			
			if(msg.getType() == MsgType.RDY) {
				System.out.println(activePlayer.getTempName() + " is ready");
			}
			
			if(msg.getType() == MsgType.MAP) {
				System.out.println(activePlayer.getTempName() + " sent map");
				validateMap(msg.getTiles());
			}	
			
			if(msg.getType() == MsgType.MOVE) {
				System.out.println(activePlayer.getTempName() + " wants to move");
				validateMove(msg);
			}		
		}
		else {
			LOGGER.warning("received message from " + activePlayer + " with wrong message type: " + msg.getType() + " expected: " + expectedCommand.get(game.getGameState()));
			System.out.println("ERROR: Wrong Message Type. Received: "+msg.getType() +" Expected: "+game.getGameState());
			sendToClient(MessageFactory.defeat(),activePlayer);
			toggleActivePlayer();
			sendToClient(MessageFactory.victory(),activePlayer);
		}
	}
	
	public void validateMap(Tile[][] tiles) {
			
		//check Size
		if(!(tiles.length == 8)) {
			System.out.println("Expected width: 8 but was: " + tiles[0].length);
			sendToClient(MessageFactory.defeat(),activePlayer);
			toggleActivePlayer();
			sendToClient(MessageFactory.victory(),activePlayer);
		}
		if(!(tiles[0].length == 4)) {
			System.out.println("Expected height: 4 but was: " + tiles[1].length);
			sendToClient(MessageFactory.defeat(),activePlayer);
			toggleActivePlayer();
			sendToClient(MessageFactory.victory(),activePlayer);
		}
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				tileList.add(tiles[i][j]);
			}
		}
		
		//check at least 3 mountain, 5 grass, 4 water
		long count;
		count = tileList.stream().filter(f -> f.getType() == TileType.GRASS).count();
		if(count < 5) {
			System.out.println("Not enough Grass Tiles! Counted: " + count);
			sendToClient(MessageFactory.defeat(),activePlayer);
			toggleActivePlayer();
			sendToClient(MessageFactory.victory(),activePlayer);
		}
		count = tileList.stream().filter(f -> f.getType() == TileType.STONE).count();
		if(count < 3) {
			System.out.println("Not enough Stone Tiles! Counted: " + count);
			sendToClient(MessageFactory.defeat(),activePlayer);
			toggleActivePlayer();
			sendToClient(MessageFactory.victory(),activePlayer);
		}
		count = tileList.stream().filter(f -> f.getType() == TileType.WATER).count();
		if(count < 4) {
			System.out.println("Not enough Water Tiles! Counted: " + count);
			sendToClient(MessageFactory.defeat(),activePlayer);
			toggleActivePlayer();
			sendToClient(MessageFactory.victory(),activePlayer);
		}
		count = tileList.stream().filter(f -> f.getType() == TileType.EMPTY).count();
		if(count > 0) {
			System.out.println("Empty Fields counted: " + count);
			sendToClient(MessageFactory.defeat(),activePlayer);
			toggleActivePlayer();
			sendToClient(MessageFactory.victory(),activePlayer);
		}
		
		//water on the border check
		int waterCount = 0;
		for(int i = 0; i < 7; i++) {
			if(tiles[i][0].getType() == TileType.WATER) {
				 waterCount++;
				 if(waterCount > 3) {
						sendToClient(MessageFactory.defeat(),activePlayer);
						toggleActivePlayer();
						sendToClient(MessageFactory.victory(),activePlayer);
				 }
			}	
		}
		waterCount = 0;
		for(int i = 0; i < 7; i++) {
			if(tiles[i][3].getType() == TileType.WATER) {
				 waterCount++;
				 if(waterCount > 3) {
						sendToClient(MessageFactory.defeat(),activePlayer);
						toggleActivePlayer();
						sendToClient(MessageFactory.victory(),activePlayer);
				 }
			}
		}
		waterCount = 0;
		for(int i = 0; i < 3; i++) {
			if(tiles[0][i].getType() == TileType.WATER) {
				 waterCount++;
				 if(waterCount > 1) {
						sendToClient(MessageFactory.defeat(),activePlayer);
						toggleActivePlayer();
						sendToClient(MessageFactory.victory(),activePlayer);
				 }
			}
		}
		waterCount = 0;
		for(int i = 0; i < 3; i++) {
			if(tiles[7][i].getType() == TileType.WATER) {
				 waterCount++;
				 if(waterCount > 1) {
						sendToClient(MessageFactory.defeat(),activePlayer);
						toggleActivePlayer();
						sendToClient(MessageFactory.victory(),activePlayer);
				 }
			}
		}
		
		//check for islands
		if(checkForIslands(tiles) > 1) {
			sendToClient(MessageFactory.defeat(),activePlayer);
			toggleActivePlayer();
			sendToClient(MessageFactory.victory(),activePlayer);
		}

		
		//check if fort is on grass
		boolean noFort = true;
		ArrayList<Entity> tmpEntity = new ArrayList<Entity>();
		for(Tile tile: tileList) {

			tmpEntity = tile.getEntities();
			for(Entity e : tmpEntity) {
				if(e.getEntity() == EntityType.FORT) {
					noFort = false;
					if(tile.getType() != TileType.GRASS) {
						System.out.println("Fort is not placed on Grass!");
						sendToClient(MessageFactory.defeat(),activePlayer);
						toggleActivePlayer();
						sendToClient(MessageFactory.victory(),activePlayer);
					}
				}
			}
		}			
		if(noFort) {
			sendToClient(MessageFactory.defeat(),activePlayer);
			toggleActivePlayer();
			sendToClient(MessageFactory.victory(),activePlayer);
			System.out.println("No Fort placed");
		}
		
		//If all conditions passed, put it into the world map
		System.out.println("PlayerMap: ");
		for(int i = 0; i < tiles.length;i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				System.out.println("["+i+"/"+j+"] " + tiles[i][j].getType());
			} 
		}
		game.getWorld().addPlayerMap(tiles);
		
		
	}
	
	public int checkForIslands(Tile[][] tiles) {
		int islands = 0;
		
		Tile[][] t = new Tile[tiles.length][tiles[0].length];
		for(int i = 0; i < t.length;i++) {
			for(int j = 0; j < t[0].length; j++) {
				t[i][j] = new Tile(i,j,tiles[i][j].getType());
			} 
		}

		for(int i = 0; i < t.length; i++) {
			for(int j = 0; j < t[0].length; j++) {
				islands += checkNeighbours(i,j,t);
			}
		}	
		return islands;
	}
	
	int checkNeighbours(int i, int j, Tile[][] t){
		if (i < 0 || i == t.length || j < 0 || j == t[i].length || t[i][j].getType() == TileType.WATER)
            return 0;
        t[i][j].setType(TileType.WATER);
        checkNeighbours(i+1, j,t); checkNeighbours(i-1, j,t); checkNeighbours(i, j+1,t); checkNeighbours(i, j-1,t);
        return 1;	
	}
	
	public void validateMove(XMLMessage xml){
		String coordinates = xml.getDesc();
		int x = Integer.parseInt(coordinates.substring(0,coordinates.indexOf(",")));
		int y = Integer.parseInt(coordinates.substring(coordinates.indexOf(",")+1));
		
		if(game.getWorld().getTile(x, y).getType() == TileType.WATER) {
			sendToClient(MessageFactory.defeat(),activePlayer);
			toggleActivePlayer();
			sendToClient(MessageFactory.victory(),activePlayer);
		}
		
		if(checkForPlayerChest(x,y)) {
			sendToClient(MessageFactory.foundChest(),activePlayer);
		}
		
	}
	
	public boolean checkForPlayerChest(int x, int y) {
		if(activePlayer == p1) {
			if(y < 4) {
				if(game.getWorld().getTile(x, y).hasChest()) {
					return true;	
				}
				return false;
			}
			return false;
		}
		if(activePlayer == p2) {
			if(y > 3) {
				if(game.getWorld().getTile(x, y).hasChest()) {
					return true;	
				}	
				return false;
			}
			return false;
		}
		return false;
	}
	
	
}
