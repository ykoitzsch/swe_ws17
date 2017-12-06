package play;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import play.Game.GameState;
import play.map.Tile;
import play.map.World.TileType;
import socket.PlayerConnection;
import socket.communication.MessageFactory;
import socket.communication.MsgType;
import socket.communication.XMLMessage;

public class GameRequestHandler implements Runnable{
	
	private PlayerConnection p1;
	private PlayerConnection p2;
	private PlayerConnection activePlayer;
	private Game game;
	private int id;
	private long startTime;
	private Map<GameState, MsgType> expectedCommand = new HashMap<GameState, MsgType>();

	public GameRequestHandler(PlayerConnection p1, PlayerConnection p2, int id){
		this.p1 = p1;
		this.p2 = p2;
		this.id = id;
		activePlayer = p1;
		p1.setTempName(id + "Player1");
		p2.setTempName(id + "Player2");
		expectedCommand.put(GameState.PREPARE, MsgType.RDY);
		expectedCommand.put(GameState.MAPX, MsgType.MAP);
		game = new Game(id);

	}

	@Override
	public void run() {
		
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
				System.out.println(xml);
				
				if(xml.endsWith(XMLMessage.msgEnd)){
					msg = (XMLMessage) um.unmarshal(new StreamSource(new StringReader(sb.toString())));
					sb.setLength(0);
					// ---- //
					checkTime();
					inspectMessage(msg);
					toggleActivePlayer();
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
			
		}
		else {
			
		}
		
	}
	
	private void toggleActivePlayer() {
		if(activePlayer == p1) {
			activePlayer = p2;
		}
		else {
			activePlayer = p1;
			updateGameState();
		}
		System.out.println("It's " + activePlayer.getTempName() + " turn");
	}
	
	public void updateGameState() {
		if(game.getGameState() == GameState.PREPARE) {
			game.setGameState(GameState.MAPX);
		}
		else if(game.getGameState() == GameState.MAPX) {
			game.setGameState(GameState.PLAY);	
		}
	}

	public void notifyPlayer() {
		if(game.getGameState() == GameState.PREPARE) {
			sendToClient(MessageFactory.gamestart(),activePlayer);
		}
		else if(game.getGameState() == GameState.MAPX) {
			sendToClient(MessageFactory.generateMap(),activePlayer);
		}
	}
	
	public void inspectMessage(XMLMessage msg){
		System.out.println(game.getGameState());
		if(msg.getType() == expectedCommand.get(game.getGameState())) {
			
			if(msg.getType() == MsgType.RDY) {
				System.out.println(activePlayer.getTempName() + " is ready");
			}
			
			if(msg.getType() == MsgType.MAP) {
				System.out.println(activePlayer.getTempName() + " sent map");
				validateMap(msg.getTiles());
			}	
		}
		else {
			System.out.println("ERROR: Wrong Message Type. Received: "+msg.getType() +" Expected: "+game.getGameState());
		}
	}
	
	public void validateMap(Tile[][] tiles) {
		//check Size
		if(!(tiles.length == 8)) {
			System.out.println("Expected width: 8 but was: " + tiles[0].length);
		}
		if(!(tiles[0].length == 4)) {
			System.out.println("Expected height: 4 but was: " + tiles[1].length);
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
		}
		count = tileList.stream().filter(f -> f.getType() == TileType.STONE).count();
		if(count < 3) {
			System.out.println("Not enough Stone Tiles! Counted: " + count);
		}
		count = tileList.stream().filter(f -> f.getType() == TileType.WATER).count();
		if(count < 4) {
			System.out.println("Not enough Water Tiles! Counted: " + count);
		}
		count = tileList.stream().filter(f -> f.getType() == TileType.EMPTY).count();
		if(count > 0) {
			System.out.println("Empty Fields counted: " + count);
		}
		
		//water on the border check
		int waterCount = 0;
		for(int i = 0; i < 7; i++) {
			if(tiles[i][0].getType() == TileType.WATER) {
				 waterCount++;
				 if(waterCount > 3) {
					 System.out.println("Too many Water Tiles on the merge border! Counted: "+waterCount);
				 }
			}	
		}
		waterCount = 0;
		for(int i = 0; i < 7; i++) {
			if(tiles[i][3].getType() == TileType.WATER) {
				 waterCount++;
				 if(waterCount > 3) {
					 System.out.println("Too many Water Tiles on the merge border! Counted: "+waterCount);
				 }
			}
		}
		waterCount = 0;
		for(int i = 0; i < 3; i++) {
			if(tiles[0][i].getType() == TileType.WATER) {
				 waterCount++;
				 if(waterCount > 1) {
					 System.out.println("Too many Water Tiles on the side border! Counted: "+waterCount);
				 }
			}
		}
		waterCount = 0;
		for(int i = 0; i < 3; i++) {
			if(tiles[7][i].getType() == TileType.WATER) {
				 waterCount++;
				 if(waterCount > 1) {
					 System.out.println("Too many Water Tiles on the side border! Counted: "+waterCount);
				 }
			}
		}
		

		
	}

}
