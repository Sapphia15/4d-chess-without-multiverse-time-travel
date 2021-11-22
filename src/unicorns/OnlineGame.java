package unicorns;

import unicorns.net.ServerController;

public class OnlineGame {
	int whiteID;
	int blackID;
	ServerController white;
	ServerController black;
	String code;
	boolean firstTurn=true;
	long whiteTime=60000*20;
	long blackTime=60000*20;
	boolean started=false;
	boolean whiteTurn=true;
	long clocks=-1;
	
	public OnlineGame(ServerController s,boolean white,String code,long clocks) {
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
	
	public ServerController getWhite() {
		return white;
	}
	
	public ServerController getBlack() {
		return black;
	}
	
}
