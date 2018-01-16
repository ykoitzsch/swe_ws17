package play;


import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import play.map.ClientGameInformation;
import play.map.Coordinate;
import play.map.Map;
import play.map.World;

public class MapGUI{
	
	ImageIcon player1 = new ImageIcon("src/main/resources/img/player1.png");
	ImageIcon castle1 = new ImageIcon("src/main/resources/img/castle1.png");
	ImageIcon chest1 = new ImageIcon("src/main/resources/img/chest1.png");
	ImageIcon player2 = new ImageIcon("src/main/resources/img/player2.png");
	ImageIcon castle2 = new ImageIcon("src/main/resources/img/castle2.png");
	ImageIcon chest2 = new ImageIcon("src/main/resources/img/chest2.png");
	ImageIcon water = new ImageIcon("src/main/resources/img/water.png");
	ImageIcon grass = new ImageIcon("src/main/resources/img/grass.png");
	ImageIcon mountain = new ImageIcon("src/main/resources/img/mountain.png");
	ImageIcon p1wChest = new ImageIcon("src/main/resources/img/player1wchest.png");
	ImageIcon p2wChest = new ImageIcon("src/main/resources/img/player2wchest.png");


	JLabel[][] cells;

	public MapGUI(String title, Map map, int player, String placeholder) {
		JFrame frame = new JFrame(title);
		frame.setBounds(500, 0, 400, 400);
		JPanel panel = new JPanel();
		cells = new JLabel[map.getWidth()][map.getHeight()];
		panel.setLayout(new GridLayout(map.getWidth(), map.getHeight()));
		String tile;
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				tile = map.getXY(x, y);
				JLabel cell = new JLabel();
				cell.setHorizontalAlignment(JLabel.CENTER);
				cell.setVerticalAlignment(JLabel.CENTER);
				cell.setOpaque(true);
				cells[x][y] = cell;
				if(tile.equals("F1")){
					cell.setBackground(Color.YELLOW);
					cell.setIcon(player1);
				}
				if(tile.equals("F2")) {
					cell.setBackground(Color.YELLOW);
					cell.setIcon(player2);
				}
				if(tile.equals("W")) {
					cell.setBackground(Color.BLACK);
				}
				cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				panel.add(cell);
			}
		}
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public MapGUI(String title, Map map, int player) {
		JFrame frame = new JFrame(title);
		frame.setSize(400, 400);
		JPanel panel = new JPanel();
		cells = new JLabel[map.getWidth()][map.getHeight()];
		panel.setLayout(new GridLayout(map.getWidth(), map.getHeight()));
		String tile;

		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				tile = map.getXY(x, y);
				JLabel cell = new JLabel();
				cell.setHorizontalAlignment(JLabel.CENTER);
				cell.setVerticalAlignment(JLabel.CENTER);
				cell.setOpaque(true);
				cells[x][y] = cell;
				
				if(tile.equals("W")) {
					cell.setIcon(water);
				}
				else if(tile.equals("M")) {
					cell.setIcon(mountain);
				}
				else cell.setIcon(grass);

				
				if(title.contains("serverGUI")) {
					if(tile.equals("C1")) {
						cell.setIcon(chest1);
					}
					if(tile.equals("F1")){
						cell.setIcon(castle1);
					}
					if(tile.equals("C2")) {
						cell.setIcon(chest2);
					}
					if(tile.contains("F2")){
						cell.setIcon(castle2);
					}
				}
				
				else if(player == 1) {
					frame.setLocation(x, 500);
					if(tile.equals("MYFORT")) {
						cell.setIcon(castle1);
					}
					if(tile.equals("OPPONENT")) {
						cell.setIcon(player2);
					}
				}
				else {
					frame.setLocation(500, 500);
					if(tile.equals("MYFORT")) {
						cell.setIcon(castle2);
					}
					if(tile.equals("OPPONENT")) {
						cell.setIcon(player1);
					}
				}
				cell.setBorder(BorderFactory.createLineBorder(Color.GREEN.darker()));
				panel.add(cell);
			}
		}
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public void clientUpdate(ClientGameInformation cgi) {

		drawBasicTextures(cgi.getClientMap());
		cleanPlayerIcons();
		Coordinate oppFort = cgi.getOpponentCastlePosition();
		Coordinate myFort = cgi.getPlayerCastlePosition();
		
		if(cgi.getPlayerNumber() == 1) {
			cells[oppFort.getX()][oppFort.getY()].setIcon(castle2);
			cells[myFort.getX()][myFort.getY()].setIcon(castle1);
			
			cells[cgi.getPlayerPosition().getX()][cgi.getPlayerPosition().getY()].setIcon(player1);
			cells[cgi.getOpponentPosition().getX()][cgi.getOpponentPosition().getY()].setIcon(player2);
			if(cgi.getChestPosition().getX() != -1) {
				if(cgi.hasChest()) {
					cells[cgi.getPlayerPosition().getX()][cgi.getPlayerPosition().getY()].setIcon(p1wChest);	
				}
				else {
					cells[cgi.getChestPosition().getX()][cgi.getChestPosition().getY()].setIcon(chest1);	
				}
			}

			
		}
		if(cgi.getPlayerNumber() == 2) {
			cells[oppFort.getX()][oppFort.getY()].setIcon(castle1);
			cells[myFort.getX()][myFort.getY()].setIcon(castle2);
			
			cells[cgi.getPlayerPosition().getX()][cgi.getPlayerPosition().getY()].setIcon(player2);
			cells[cgi.getOpponentPosition().getX()][cgi.getOpponentPosition().getY()].setIcon(player1);
			if(cgi.getChestPosition().getX() != -1) {
				if(cgi.hasChest()) {
					cells[cgi.getPlayerPosition().getX()][cgi.getPlayerPosition().getY()].setIcon(p2wChest);	
				}
				else {
					cells[cgi.getChestPosition().getX()][cgi.getChestPosition().getY()].setIcon(chest2);	
				}
			}
		}
	}
	
	public void debugUpdate(World w) {
		cleanPlayerIcons();
		cells[w.getP1Pos().getX()][w.getP1Pos().getY()].setBackground(Color.red.darker());
		cells[w.getP2Pos().getX()][w.getP2Pos().getY()].setBackground(Color.blue.darker());
		cells[w.getMap().getXY("F1").getX()][w.getMap().getXY("F1").getY()].setBackground(Color.yellow);
		cells[w.getMap().getXY("F2").getX()][w.getMap().getXY("F2").getY()].setBackground(Color.yellow);
		cells[w.getMap().getXY("C1").getX()][w.getMap().getXY("C1").getY()].setIcon(chest1);
		cells[w.getMap().getXY("C2").getX()][w.getMap().getXY("C2").getY()].setIcon(chest2);
		cells[w.getP1Pos().getX()][w.getP1Pos().getY()].setIcon(player1);
		cells[w.getP2Pos().getX()][w.getP2Pos().getY()].setIcon(player2);
	}
	
	public void serverUpdate(World w, boolean p1HasChest, boolean p2HasChest) {
		drawBasicTextures(w.getMap());
		cleanPlayerIcons();
		Coordinate fort1 = w.getMap().getXY("F1");
		Coordinate fort2 = w.getMap().getXY("F2");
		Coordinate ch1 = w.getMap().getXY("C1");
		Coordinate ch2 = w.getMap().getXY("C2");

		cells[fort1.getX()][fort1.getY()].setIcon(castle1);
		cells[fort2.getX()][fort2.getY()].setIcon(castle2);
		if(!p1HasChest)	cells[ch1.getX()][ch1.getY()].setIcon(chest1);
		if(!p2HasChest)	cells[ch2.getX()][ch2.getY()].setIcon(chest2);
		cells[w.getP1Pos().getX()][w.getP1Pos().getY()].setIcon(player1);
		cells[w.getP2Pos().getX()][w.getP2Pos().getY()].setIcon(player2);
		if(p1HasChest) 	cells[w.getP1Pos().getX()][w.getP1Pos().getY()].setIcon(p1wChest);
		if(p2HasChest) 	cells[w.getP2Pos().getX()][w.getP2Pos().getY()].setIcon(p2wChest);

	}
	
	public void cleanPlayerIcons() {
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				if(cells[x][y].getIcon() == player1 || cells[x][y].getIcon() == player2) cells[x][y].setIcon(null);
			}
		}
	}
	
	public void drawBasicTextures(Map map) {
		String tile;
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				tile = map.getXY(x, y);
				if(tile.equals("W")) {
					cells[x][y].setIcon(water);
				}
				else if(tile.equals("M")) {
					cells[x][y].setIcon(mountain);
				}
				else cells[x][y].setIcon(grass);	
			}
		}
	}
	
	public void showWinner(int winner) {
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(winner == 1) {
					
					cells[x][y].setBackground(Color.RED);
					cells[x][y].setIcon(null);
				}
				else {
					cells[x][y].setBackground(Color.BLUE);
					cells[x][y].setIcon(null);	
				}

			}
		}
	}
}


