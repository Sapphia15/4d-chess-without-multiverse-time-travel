package unicorns.net;

import java.io.ObjectOutputStream;
import java.net.Socket;

import gameutil.math.geom.Point;
import gameutil.text.Console;
import net.Controller;
import net.Request;
import unicorns.Board;
import unicorns.Main;
import unicorns.Panel;

public class ClientController extends Controller {

	int id=-1;
	Panel observer;
	boolean white;
	
	public ClientController(Socket s, ObjectOutputStream out,Panel observer) {
		super(s, out);
		Console.s.println("Connected to server");
		this.observer=observer;
	}

	@Override
	public void get(Request r, String route) {
		switch (route) {
		
			case "ID":
				this.id=(int) r.get("ID");
				Console.s.println("Your ID: "+id);
			break;
			
			case "move":
			{
				if (white) {
					confirm(observer.getGame().getWhiteTime());
				} else {
					confirm(observer.getGame().getBlackTime());
				}
				observer.makeMove((Point[]) r.get("move"),(char)r.get("promote"));
				//send timeStamp information and the current time to game screen
				long currentTime=System.currentTimeMillis();
				//also send the timeStamp of this player's start of turn
				Request startTurn=new Request("post","confirm");
				startTurn.set("timeStamp", currentTime);
			}
			break;
			case "confirm":
				long timeStamp=(long)r.get("timeStamp");
				//send timeStamp to game screen
				//sets opponent time so this client's estimate doesn't get too far off (should only decrease or stay the same, never increase)
				observer.confirm(timeStamp);
			break;
			case "code":
				observer.getOnline().setCode((String) r.get("code"));
				observer.getOnline().setGenerated(true);
			break;
			case "joined":
				Console.s.println("opp joined");
				observer.setOnline(true);
				observer.getGame().setOppColor(!white);
				observer.setScreen("game");
			break;
			case "game":
				if ((boolean) r.get("success")) {
					observer.setOnline(true);
					observer.getGame().setOppColor(!(boolean)r.get("white"));
					this.white=(boolean)r.get("white");
					observer.setClocks(((long)r.get("clocks"))>0);
					observer.setScreen("game");
				} else {
					Main.err.println("Failed to join game.");
				}
			break;
			
		}
		
	}
	
	
	
	public void onDisconnect() {
		Console.s.println("Disconnected from server.");
	}
	
	public void sendMove(Point[] move,char promote,long timeStamp) {
		Request mReq=new Request("post","move");
		mReq.set("move", move);
		mReq.set("promote", promote);
		mReq.set("timeStamp",timeStamp);
		queueProcessRequest(mReq);
	}
	
	public void sendCode(String s) {
		Request cReq=new Request("post","code");
		cReq.set("code", s);
		queueProcessRequest(cReq);
	}
	
	public void confirm(long timeStamp) {
		Request cReq=new Request("post","confirm");
		cReq.set("timeStamp", timeStamp);
		queueProcessRequest(cReq);
	}
	
	public void createGame(boolean white,long clocks) {
		this.white=white;
		Request cReq=new Request("post","create");
		cReq.set("white",white);
		cReq.set("clocks",clocks);
		queueProcessRequest(cReq);
	}
	
}
