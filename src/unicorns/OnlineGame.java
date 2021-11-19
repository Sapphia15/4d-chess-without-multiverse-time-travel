package unicorns;

public class OnlineGame {
	int whiteID;
	int blackID;
	String code;
	boolean firstTurn=true;
	long whiteTime=60000*20;
	long blackTime=60000*20;
	boolean started=false;
	boolean whiteTurn=true;
	long clocks=-1;
	
	public OnlineGame(int id,boolean white,String code,long clocks) {
		if (white) {
			whiteID=id;
			blackID=-1;
		} else {
			whiteID=-1;
			blackID=id;
		}
		this.code=code;
		this.clocks=clocks;
	}
	
	public boolean connect(int id) {
		if (whiteID==-1) {
			blackID=id;
		} else if (blackID==-1) {
			whiteID=id;
		} else {
			return false;
		}
		return true;
	}
	
	public int getPlayerID(boolean white) {
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
	
}
