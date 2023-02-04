package unicorns;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import gameutil.math.geom.Point;
import gameutil.text.Console;
import graphics.screen.SPanel;
import unicorns.net.ClientController;
import unicorns.screens.Game;
import unicorns.screens.HowToPlay;
import unicorns.screens.HyperVox;
import unicorns.screens.Mapper;
import unicorns.screens.Online;
import unicorns.screens.Title;
import unicorns.screens.TutorialIntro;
import unicorns.screens.TutorialScreen;
import unicorns.screens.TutorialSelect;

public class Panel extends SPanel{

	boolean ai;
	boolean clocks=true;
	boolean online;
	boolean music=true;
	String variant="standard";
	String song="For_Dee.wav";
	ClientController controller;
	long clockTime=60000*20;
	long bonus=3000;
	long delay=0;
	public static  final int sWidth=1360;
	public static  final int sHeight=768;
	public static  final int smidX=sWidth/2;
	public static final int smidY=sHeight/2;
	public BufferedImage scene;
	public int drawSceneWidth=sWidth;
	public int drawSceneX=0;
	public static final double ratio=((double)sWidth)/sHeight;
	
	public Panel(Frame observer) {
		super(observer);
		TutorialScreen.initRects();
		this.screens.put("game", new Game(this));
		this.screens.put("title", new Title(this));
		this.screens.put("online", new Online(this));
		this.screens.put("hyperVox", new HyperVox(this));
		this.screens.put("tutorial", new TutorialSelect(this));
		this.screens.put("Intro To 4D", new TutorialIntro(this));
		this.screens.put("How To Play 4D Chess Without Multiverse Time Travel", new HowToPlay(this));
		this.screens.put("map", new Mapper(this));
		this.currentScreen=screens.get("title");
		this.setScreen("title");
		
		Main.err.setVisible(true);
		//Main.err.println("Attempting to connect to main server.");
		try {
			Socket s=new Socket("209.126.7.208",25565);
			controller=new ClientController(s,new ObjectOutputStream(s.getOutputStream()),this);
			Main.err.setVisible(false);
		} catch (IOException e) {
			Main.err.println("Failed to connect to server! Press \\ while in game to open command window.");
			Main.err.println("Type help and press enter for help with commands.");
			e.printStackTrace();
		}
		
	}
	
	public void initScene(){
		scene=new BufferedImage(sWidth,sHeight, BufferedImage.TYPE_INT_ARGB);
		//g.setBackground(new Color(255,255,255,0));
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
	
	public boolean isMusicOn() {
		return music;
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
		controller.createGame(white, clocks,delay,bonus,variant);
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
	
	public void toggleMusic() {
		music=!music;
		if (music) {
			Main.sounds.resumeSound(song);
		} else {
			Main.sounds.pauseSound(song);
		}
	}
	
	public void setSong(String song) {
		if (!this.song.equals(song)) {
			if (music) {
				Main.sounds.pauseSound(this.song);
				Main.sounds.resumeSound(song);
				this.song=song;
			}
		}
	}
	
	public java.awt.Point convert(java.awt.Point p) {
		return new java.awt.Point((int)(((double)(p.x-drawSceneX)/drawSceneWidth)*sWidth),(int)((double)(p.y)/this.getHeight()*sHeight));
	}
	
	public HyperVox getHyperVox() {
		return (HyperVox) screens.get("hyperVox");
	}
	
	public long getClockTime() {
		return clockTime;
	}
	
	public void setClockTime(long l) {
		clockTime=l;
	}
	
	public long getDelay() {
		return delay;
	}
	
	public void setDelay(long l) {
		delay=l;
	}
	
	public long getBonus() {
		return bonus;
	}
	
	public void setBonus(long i) {
		bonus=i;
	}
	
	public String getVariant() {
		return variant;
	}
	
	public void setVariant(String variant) {
		this.variant=variant;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4753098130173598977L;

}
