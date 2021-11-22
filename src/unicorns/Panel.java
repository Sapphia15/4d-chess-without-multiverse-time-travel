package unicorns;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import gameutil.math.geom.Point;
import gameutil.text.Console;
import graphics.screen.SPanel;
import unicorns.net.ClientController;
import unicorns.screens.Game;
import unicorns.screens.Online;
import unicorns.screens.Title;

public class Panel extends SPanel{

	boolean ai;
	boolean clocks=true;
	boolean online;
	ClientController controller;
	
	public Panel(Frame observer) {
		super(observer);
		
		this.screens.put("game", new Game(this));
		this.screens.put("title", new Title(this));
		this.screens.put("online", new Online(this));
		this.currentScreen=screens.get("title");
		this.setScreen("title");
		Main.err.setVisible(true);
		Main.err.println("Attempting to connect to main server.");
		try {
			Socket s=new Socket("68.61.11.144",25565);
			controller=new ClientController(s,new ObjectOutputStream(s.getOutputStream()),this);
			Main.err.setVisible(false);
		} catch (IOException e) {
			Main.err.println("Failed to connect to server! Press \\ while in game to open command window.");
			Main.err.println("Type help and press enter for help with commands.");
			e.printStackTrace();
		}
		
	}
	
	public boolean ai() {
		return ai;
	}
	
	public void setAi(boolean ai) {
		this.ai=ai;
	}
	
	public void setOnline(boolean online) {
		this.online=online;
	}
	
	public boolean isOnline() {
		return online;
	}
	
	public boolean clocks() {
		return clocks;
	}
	
	public void setClocks(boolean clocks) {
		this.clocks=clocks;
	}
	
	public Board getBoard() {
		return getGame().getBoard();
	}
	
	public Game getGame() {
		return (Game)this.screens.get("game");
	}
	
	public Online getOnline() {
		return (Online)this.screens.get("online");
	}
	
	public void createGame(boolean white,long clocks) {
		controller.createGame(white, clocks);
	}
	
	public void join(String code) {
		controller.sendCode(code);
	}

	public void makeMove(Point[] move,char promote) {
		getGame().makeMove(move,promote);
	}
	
	public void makeMove(Point[] move) {
		getGame().makeMove(move,'x');
	}
	
	public void submitOnlineMove(Point[] move,char promote) {
		controller.sendMove(move,promote, System.currentTimeMillis());
	}
	
	public void submitOnlineMove(Point[] move) {
		submitOnlineMove(move,'x');
	}
	
	public void confirm(long oppTime) {
		getGame().confirm(oppTime);
	}
	
	public void exitOnlineGame() {
		controller.exitGame();
	}
	
	public void connectToAlternateServer(String address) {
		if (controller!=null) {
			controller.disconnect();
		}
		try {
			Console.s.println("Connecting to alternate server "+address+"...");
			Socket s=new Socket(address.substring(0, address.indexOf(':')),Integer.parseInt(address.substring(address.indexOf(':')+1)));
			controller=new ClientController(s,new ObjectOutputStream(s.getOutputStream()),this);
			Main.err.println("Connected to "+address+"!");
		} catch (IOException e) {
			Main.err.println("Failed to connect to server!");
			e.printStackTrace();
		} catch (Exception e) {
			Main.err.println("Failed to connect to server! Make sure you typed in the address correctly.");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4753098130173598977L;

}
