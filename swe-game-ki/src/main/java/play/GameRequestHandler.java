package play;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import hibernate.model.GameLogModel;
import hibernate.model.GameModel;
import hibernate.repository.IRepository;
import hibernate.repository.RepositoryFactory;
import play.map.Coordinate;
import play.map.Map;
import play.map.World;
import socket.PlayerConnection;
import socket.communication.MessageFactory;
import socket.communication.MsgType;
import socket.communication.XMLMessage;

public class GameRequestHandler implements Runnable{
	
	private IRepository<GameModel> gmRepo = new RepositoryFactory().getRepository(GameModel.class);
	private IRepository<GameLogModel> glRepo = new RepositoryFactory().getRepository(GameLogModel.class);


	private PlayerConnection p1;
	private PlayerConnection p2;
	private PlayerConnection activePlayer;
	private PlayerConnection inactivePlayer;
	private Game game;
	private int gamestate;
	private long startTime;
	private Coordinate lastMove;
	private MapGUI gui, debugGUI;
	private GameModel gamemodel = new GameModel();
	private GameLogModel gamelog = new GameLogModel();

	
	public GameRequestHandler(PlayerConnection p1, PlayerConnection p2, int id){
		this.p1 = p1;
		this.p2 = p2;
		activePlayer = p1;
		inactivePlayer = p2;
		game = new Game(id);
		gamestate = 1;
	}

	@Override
	public void run() {
		startTheGame();	
	}
	
	public void startTheGame() {
		sendToClient(MessageFactory.sc_pregameInformation("1"),p1);
		sendToClient(MessageFactory.sc_pregameInformation("2"),p2);
		notifyPlayer();
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
				if(xml.endsWith(XMLMessage.msgEnd)){
					msg = (XMLMessage) um.unmarshal(new StreamSource(new StringReader(sb.toString())));
					sb.setLength(0);
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
			finish(inactivePlayer, activePlayer, false);
		}	
	}
	
	private void toggleActivePlayer() {
		if(activePlayer == p1) {
			activePlayer = p2;
			inactivePlayer = p1;
		}
		else {
			activePlayer = p1;
			inactivePlayer = p2;
			gamestate++;
		}
	}
	
	public void notifyPlayer() {
		if(gamestate == 1) {
			sendToClient(MessageFactory.sc_gamestart(game.getId()),activePlayer);
		}
		else if(gamestate == 2) {
			sendToClient(MessageFactory.sc_sendMap(generateMapForClient()), activePlayer);
		}
		else if(gamestate > 2 && gamestate < 200) {
			sendToClient(MessageFactory.sc_makeMove(lastMove), activePlayer);
		}	
		else if(gamestate > 200) {
			finish(null,null, true);
		}
	}
		
	public void inspectMessage(XMLMessage msg){	
		
		String logs = gamelog.getLogs();
		if(activePlayer == p1) logs += "["+ new Date().toString() +"] [Player 1]" + msg.getType() + "\n";
		else logs += "["+ new Date().toString() +"] [Player 2]" + msg.getType() + "\n";
		 
		gamelog.setLogs(logs);
		if(msg.getType() == MsgType.MAP) {
			if(activePlayer == p1) {
				int randX = new Random().nextInt(8);
				int randY = new Random().nextInt(4);
				placeChest(msg.getMap(), randX, randY, "C1");
				
				for(int x = 0; x < 8; x++) {
					for(int y = 0; y < 4; y++) {
						if(msg.getMap().getXY(x, y).equals("F")) {
							msg.getMap().setXY(x, y, "F1");
						}
					}
				}
				World w = game.getWorld();
				w.setP1Map(msg.getMap());
				game.setWorld(w);
			}	
			else {
				int randX = new Random().nextInt(8);
				int randY = new Random().nextInt(4);
				placeChest(msg.getMap(), randX, randY, "C2");
				for(int x = 0; x < 8; x++) {
					for(int y = 0; y < 4; y++) {
						if(msg.getMap().getXY(x, y).equals("F")) {
							msg.getMap().setXY(x, y, "F2");
						}
					}
				}
				World w = game.getWorld();
				w.setP2Map(msg.getMap());
				game.setWorld(w);
				combineMaps(game.getWorld().getP1Map(), msg.getMap());	
			}
		}
		
		if(msg.getType() == MsgType.MOVE) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Coordinate c = msg.getCoordinate();
			validateMove(c); 
			gui.serverUpdate(game.getWorld(), game.isP1HasChest(), game.isP2HasChest());
			debugGUI.debugUpdate(game.getWorld());
			checkForChests(c);
		}

	}
	
	public void validateMove(Coordinate c) {
		lastMove = c;
		checkForFinish(c);
		if(activePlayer == p1) {
			game.getWorld().setP1Pos(c);
		}
		else game.getWorld().setP2Pos(c);
	}
	
	public void checkForFinish(Coordinate c) {
		if(activePlayer == p1 && game.isP1HasChest() && c.equals(game.getWorld().getMap().getXY("F2"))) {
			finish(p1,p2,false);
		}
		else if(activePlayer == p2 && game.isP2HasChest() && c.equals(game.getWorld().getMap().getXY("F1"))) {
			finish(p2,p1,false);
		}
		
	}
	public void checkForChests(Coordinate c) {
		if(activePlayer == p1) {
			Coordinate chest = game.getWorld().getMap().getXY("C1");
			if(c.equals(chest)) {
				sendToClient(MessageFactory.sc_chestCoord(chest), activePlayer);
				game.setP1HasChest(true);
			}
			else if(game.getWorld().getMap().getXY(c.getX(), c.getY()).equals("M")){ //player is on mountain
				Coordinate tp, tl, t, b, l, r, bl, br;
				tp = new Coordinate(c.getX()+1, c.getY()-1);
				tl = new Coordinate(c.getX()-1, c.getY()-1);
				t = new Coordinate(c.getX(), c.getY()-1);
				b = new Coordinate(c.getX(), c.getY()+1);
				l = new Coordinate(c.getX()-1, c.getY());
				r = new Coordinate(c.getX()+1, c.getY());
				bl = new Coordinate(c.getX()-1, c.getY()+1);
				br = new Coordinate(c.getX()+1, c.getY()+1);
				if(		
					chest.equals(tp) || 
					chest.equals(tl) || 
					chest.equals(t) || 
					chest.equals(b) || 
					chest.equals(l) || 
					chest.equals(r) || 
					chest.equals(bl) || 
					chest.equals(br)) {
					sendToClient(MessageFactory.sc_chestCoord(chest), activePlayer);
				}
			}	
		}
		else {
			Coordinate chest = game.getWorld().getMap().getXY("C2");
			if(c.equals(chest)) {
				sendToClient(MessageFactory.sc_chestCoord(c), activePlayer);
				game.setP2HasChest(true);
			}
			else {
				Coordinate tp, tl, t, b, l, r, bl, br;
				tp = new Coordinate(chest.getX()+1, chest.getY()-1);
				tl = new Coordinate(chest.getX()-1, chest.getY()-1);
				t = new Coordinate(chest.getX(), chest.getY()-1);
				b = new Coordinate(chest.getX(), chest.getY()+1);
				l = new Coordinate(chest.getX()-1, chest.getY());
				r = new Coordinate(chest.getX()+1, chest.getY());
				bl = new Coordinate(chest.getX()-1, chest.getY()+1);
				br = new Coordinate(chest.getX()+1, chest.getY()+1);
				if(		
					chest.equals(tp) || 
					chest.equals(tl) || 
					chest.equals(t) || 
					chest.equals(b) || 
					chest.equals(l) || 
					chest.equals(r) || 
					chest.equals(bl) || 
					chest.equals(br)) {
					sendToClient(MessageFactory.sc_chestCoord(chest), activePlayer);
					System.out.println("client should see chest now");
				}
			}	
		}
	}
	
	public Map generateMapForClient() {
		Map map_copy = new Map(8,8);
		Map m = game.getWorld().getMap();
		
		if(activePlayer == p1) {
			for(int x = 0; x < 8; x++) {
				for(int y = 0; y < 8; y++) {
					if(m.getXY(x, y).contains("C")){
						map_copy.setXY(x, y, "G");
					}
					else if(m.getXY(x,y).equals("F1")) {
						map_copy.setXY(x, y, "MYFORT");
					}
					else if(m.getXY(x,y).equals("F2")) {
						map_copy.setXY(x, y, "OPPONENT");
					}
					else map_copy.setXY(x, y, m.getXY(x, y));

				}
			}	
		}
		else {
			for(int x = 0; x < 8; x++) {
				for(int y = 0; y < 8; y++) {
					if(m.getXY(x, y).contains("C")) {
						map_copy.setXY(x, y, "G");
					}
					else if(m.getXY(x,y).equals("F2")) {
						map_copy.setXY(x, y, "MYFORT");
					}
					else if(m.getXY(x,y).equals("F1")) {
						map_copy.setXY(x, y, "OPPONENT");
					}
					else map_copy.setXY(x, y, m.getXY(x, y));
				}
			}	
		}
		return map_copy;
	}
	
	public void placeChest(Map m, int x, int y, String chestName) {
		if(m.getXY(x, y).equals("G")) {
			m.setXY(x, y, chestName);
			return;
		}
		int randX = new Random().nextInt(8);
		int randY = new Random().nextInt(4);
		placeChest(m, randX, randY, chestName);
	}
	
	public void combineMaps(Map m1, Map m2) {	
		Map completeMap = new Map(8,8);
		if(new Random().nextInt(2) == 0) {
			for(int x = 0; x < 8; x++) {
				for(int y = 0; y < 4; y++) {
					completeMap.setXY(x, y, m1.getXY(x, y));
				}
			}
			for(int x = 0; x < 8; x++) {
				for(int y = 4; y < 8; y++) {
					completeMap.setXY(x, y, m2.getXY(x, y-4));
				}
			}
		}
		else {
			for(int x = 0; x < 8; x++) {
				for(int y = 0; y < 4; y++) {
					completeMap.setXY(x, y, m2.getXY(x, y));
				}
			}
			for(int x = 0; x < 8; x++) {
				for(int y = 4; y < 8; y++) {
					completeMap.setXY(x, y, m1.getXY(x, y-4));
				}
			}
		}
		game.getWorld().setMap(completeMap);
		game.getWorld().setP1Pos(completeMap.getXY("F1"));
		game.getWorld().setP2Pos(completeMap.getXY("F2"));
		gui = new MapGUI("id "+ game.getId() + " - serverGUI - gameid: " + game.getId(), completeMap, -1);
		debugGUI = new MapGUI("id "+ game.getId() + " - debugGUI - gameid: " + game.getId(), completeMap, -1, "debug");

	}
	
	public void finish(PlayerConnection winner, PlayerConnection loser, boolean draw) {
		if(draw) {
			gamemodel.setWinner("");
			gamemodel.setLoser("");
			gamemodel.setDraw(draw);
			gamemodel.setMap(game.getWorld().getMap().toString());
		}
		else {
			gamemodel.setWinner(winner.getSocket().toString());
			gamemodel.setLoser(loser.getSocket().toString());
			gamemodel.setDraw(draw);
			gamemodel.setMap(game.getWorld().getMap().toString());
		}
		gamemodel.setGamelog(gamelog);
		gmRepo.save(gamemodel);
		game.setWinner(winner);
		game.setLoser(loser);
		game.setDraw(draw);
		if(!draw) {
			if(winner == p1) {
				sendToClient(MessageFactory.sc_over("1"),p1);
				sendToClient(MessageFactory.sc_over("1"),p2);
			}
			else {
				sendToClient(MessageFactory.sc_over("2"),p1);
				sendToClient(MessageFactory.sc_over("2"),p2);
			}
		}
		else {
			sendToClient(MessageFactory.sc_over("0"),p1);
			sendToClient(MessageFactory.sc_over("0"),p2);
		}
		
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			p1.close();
			p2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
}
