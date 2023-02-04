package unicorns;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import unicorns.net.ServerController;

public class OnlineGame {
	int whiteID;
	int blackID;
	ServerController white;
	ServerController black;
	String code;
	String variant;
	boolean firstTurn=true;
	long whiteTime=60000*20;
	long blackTime=60000*20;
	boolean started=false;
	boolean whiteTurn=true;
	long clocks=-1;
	long delay=0;
	long bonus=3000;
	
	public OnlineGame(ServerController s,boolean white,String code,long clocks,long delay,long bonus,String variant) {
		if (white) {
			whiteID=s.getClientID();
			blackID=-1;
			this.white=s;
		} else {
			whiteID=-1;
			blackID=s.getClientID();
			black=s;
		}
		this.code=code;
		
		this.clocks=clocks;
		this.delay=delay;
		this.bonus=bonus;
		this.variant=variant;
		whiteTime=clocks;
		blackTime=clocks;
	}
	
	public boolean connect(ServerController s) {
		if (whiteID==-1) {
			whiteID=s.getClientID();
			white=s;
		} else if (blackID==-1) {
			blackID=s.getClientID();
			black=s;
		} else {
			return false;
		}
		return true;
	}
	
	public long getPlayerID(boolean white) {
		if (white) {
			return whiteID;
		} else {
			return blackID;
		}
	}
	
	public void start() {
		whiteTime=clocks;
		blackTime=clocks;
		started=true;
		firstTurn=true;
		whiteTurn=true;
	}
	
	public String getCode() {
		return code;
	}
	
	public long getWhiteTime() {
		return whiteTime;
	}
	
	public long getBlackTime() {
		return blackTime;
	}
	
	public long getClockTime() {
		return clocks;
	}
	
	public long getDelay() {
		return delay;
	}
	
	public long getBonus() {
		return bonus;
	}
	
	public ServerController getWhite() {
		return white;
	}
	
	public ServerController getBlack() {
		return black;
	}

	public String getVariant() {
		return variant;
	}
	
}
