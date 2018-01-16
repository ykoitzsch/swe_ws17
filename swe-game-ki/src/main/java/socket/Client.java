package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import play.MapGUI;
import play.map.ClientGameInformation;
import play.map.Coordinate;
import play.map.Map;
import play.map.algo.AlgoMap;
import play.map.algo.Node;
import socket.communication.MessageFactory;
import socket.communication.MsgType;
import socket.communication.XMLMessage;

public class Client {

	public int port;
	private Socket socket;
	private String host;
	private BufferedReader reader;
	private PrintWriter writer;
	private ClientGameInformation cgi;
	private AlgoMap algoMap;
	MapGUI gui;
	
	public Client(int port){
		this.port = port;
	}
	
	public void connect(){
			try {
				socket = new Socket(host,port);
				this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.writer = new PrintWriter(socket.getOutputStream(),true);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void join(String gamename){
		sendToServer(MessageFactory.cs_join(gamename));
	}
	
	public void receiveMessages(){
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(XMLMessage.class);
			Unmarshaller um = context.createUnmarshaller();
			XMLMessage msg;
			String xml = "";
			StringBuilder sb = new StringBuilder();
		
			while((xml = reader.readLine()) != null){
				sb.append(xml);
				if(xml.endsWith(XMLMessage.msgEnd)){
					msg = (XMLMessage) um.unmarshal(new StreamSource(new StringReader(sb.toString())));
					sb.setLength(0);
					check(msg);
				}		
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void check(XMLMessage m){
		if(m.getType() == MsgType.INFO){
			cgi = new ClientGameInformation();
			cgi.setPlayerNumber(Integer.parseInt(m.getDesc()));
		}
		
		if(m.getType() == MsgType.RDY){
			cgi.setGameid(Integer.parseInt(m.getDesc()));
			sendToServer(MessageFactory.cs_sendMap(generateMap()));
		}
		if(m.getType() == MsgType.MAP){
			cgi.setClientMap(m.getMap());
			cgi.setPlayerPosition(m.getMap().getXY("MYFORT"));
			cgi.setPlayerCastlePosition(m.getMap().getXY("MYFORT"));
			cgi.setOpponentPosition(m.getMap().getXY("OPPONENT"));
			cgi.setOpponentCastlePosition(m.getMap().getXY("OPPONENT"));
			setPossibleChestPositions(m.getMap());
			algoMap = new AlgoMap(m.getMap());
			gui = new MapGUI("clientGUI - player" +cgi.getPlayerNumber()+" - gameid: "+ cgi.getGameid(), m.getMap(), cgi.getPlayerNumber());
			
			sendToServer(MessageFactory.cs_move(calculateSearchChestMove()));
		}
		if(m.getType() == MsgType.MOVE) {
			cgi.setOpponentPosition(m.getCoordinate());
			gui.clientUpdate(cgi);
			if(cgi.getPlayerPosition().equals(cgi.getChestPosition())) {
				cgi.setHasChest(true);
			}
				
			if(cgi.isWaiting()) {
				cgi.setWaiting(false);
				sendToServer(MessageFactory.cs_move(cgi.getPlayerPosition()));
			}
			else if(cgi.hasChest()) {
				sendToServer(MessageFactory.cs_move(calculateMoveToCastle())); 
			} 
			else if(cgi.getChestPosition().getX() != -1) {
				sendToServer(MessageFactory.cs_move(calculateMoveToChest()));
			}
			else {
				sendToServer(MessageFactory.cs_move(calculateSearchChestMove()));
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gui.clientUpdate(cgi);
			
		}
		if(m.getType() == MsgType.CHEST) {
			cgi.setChestPosition(m.getCoordinate());
		}	
		if(m.getType() == MsgType.OVER) {
			gui.showWinner(Integer.valueOf(m.getDesc()));
			if(m.getDesc().equals("1")) {
			}
			if(m.getDesc().equals("2")) {
			}
			if(m.getDesc().equals("0")) {
			}
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				reader.close();
				writer.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Coordinate calculateSearchChestMove() {
		Coordinate trgt;
		int playerX = cgi.getPlayerPosition().getX();
		int playerY = cgi.getPlayerPosition().getY();		

		if(cgi.getPath().isEmpty()) {
			trgt = getNearestCoordinate();
			cgi.setFinalTarget(trgt);
			cgi.setPath(algoMap.findPath(playerX, playerY, trgt.getX(), trgt.getY()));	
			removePathFromPossibleChestPositions(cgi.getPath());
		}
		int targetX = cgi.getPath().get(0).getX();
		int targetY = cgi.getPath().get(0).getY();
		Coordinate next = new Coordinate(targetX, targetY);
		cgi.setPlayerPosition(next);
		cgi.getPath().remove(0);
		if(cgi.getClientMap().getXY(targetX, targetY).equals("M")) {
			cgi.setWaiting(true);
		}

		return next;
	}
	
	public Coordinate calculateMoveToChest() {
		Coordinate trgt = cgi.getChestPosition();
		int playerX = cgi.getPlayerPosition().getX();
		int playerY = cgi.getPlayerPosition().getY();	
		
		if(!(cgi.getFinalTarget().equals(cgi.getChestPosition()))) {
			cgi.getPath().clear();
		}
	
		if(cgi.getPath().isEmpty()) {
			cgi.setFinalTarget(trgt);
			cgi.setPath(algoMap.findPath(playerX, playerY, trgt.getX(), trgt.getY()));	
		}
		int targetX = cgi.getPath().get(0).getX();
		int targetY = cgi.getPath().get(0).getY();
		Coordinate next = new Coordinate(targetX, targetY);
		cgi.setPlayerPosition(next);
		cgi.getPath().remove(0);
		if(cgi.getClientMap().getXY(targetX, targetY).equals("M")) {
			cgi.setWaiting(true);
		}
		return next;
	}
	
	public Coordinate calculateMoveToCastle() {
		Coordinate trgt = cgi.getOpponentCastlePosition();
		int playerX = cgi.getPlayerPosition().getX();
		int playerY = cgi.getPlayerPosition().getY();	
		
		if(!(cgi.getFinalTarget().equals(cgi.getOpponentCastlePosition()))) {
			cgi.getPath().clear();
		}
	
		if(cgi.getPath().isEmpty()) {
			cgi.setFinalTarget(trgt);
			cgi.setPath(algoMap.findPath(playerX, playerY, trgt.getX(), trgt.getY()));	
		}
		int targetX = cgi.getPath().get(0).getX();
		int targetY = cgi.getPath().get(0).getY();
		Coordinate next = new Coordinate(targetX, targetY);
		cgi.setPlayerPosition(next);
		cgi.getPath().remove(0);
		if(cgi.getClientMap().getXY(targetX, targetY).equals("M")) {
			cgi.setWaiting(true);
		}
		return next;
	}
	
	public void removePathFromPossibleChestPositions(List<Node> path) {
		ArrayList<Coordinate> chestPosList = cgi.getPossibleChestPositions();
		for(int i = 0; i < path.size(); i++) {
			for(int j = 0; j < chestPosList.size(); j++) { 
				int pathX = path.get(i).getX();
				int pathY = path.get(i).getY();
				int posChestX = chestPosList.get(j).getX();
				int posChestY = chestPosList.get(j).getY();
				
				if(pathX == posChestX && pathY == posChestY) {
					chestPosList.remove(j);
				}
			}
		}	
	}

	public Coordinate getNearestCoordinate() {
		int minCost = 100;
		Coordinate nearest = new Coordinate(-1,-1);
		for(int i = 0; i < cgi.getPossibleChestPositions().size(); i++) {
			int playerX = cgi.getPlayerPosition().getX(); 
			int playerY = cgi.getPlayerPosition().getY();
			int listX = cgi.getPossibleChestPositions().get(i).getX();
			int listY = cgi.getPossibleChestPositions().get(i).getY();
			int cost = Math.abs(listY-playerY)+Math.abs(playerX-listX);
			if(cost < minCost) {
				minCost = cost;
				nearest = cgi.getPossibleChestPositions().get(i);
			}
		}
		return nearest;
	}
	public void setPossibleChestPositions(Map m){
		ArrayList<Coordinate> tmpList = new ArrayList<Coordinate>();
		int tmpY;
		if(cgi.getPlayerCastlePosition().getY() > 3) tmpY = 4;
		else tmpY = 0;
		for(int y = tmpY; y < tmpY+4; y++) {
			for(int x = 0; x < 8; x++) {
				if(m.getXY(x, y).equals("G")) {
					tmpList.add(new Coordinate(x,y));
				} 
			}
		}
		cgi.setPossibleChestPositions(tmpList);
	}
	
	
	public void sendToServer(XMLMessage xml){
		try {
			JAXBContext context = JAXBContext.newInstance(XMLMessage.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(xml, writer);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public Map generateMap() {
		Map m = new Map(8,4);
		int mountainCount = 5;
		int waterCount = 4;
		m.populate(mountainCount, waterCount);
		return m;
	}
}
