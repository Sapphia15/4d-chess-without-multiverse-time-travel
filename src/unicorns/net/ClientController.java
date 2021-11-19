package unicorns.net;

import java.io.ObjectOutputStream;
import java.net.Socket;

import gameutil.math.geom.Point;
import gameutil.text.Console;
import net.Controller;
import net.Request;
import unicorns.Board;
import unicorns.Panel;

public class ClientController extends Controller {

	int id=-1;
	Panel observer;
	
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
				observer.getBoard().makeMove((Point[]) r.get("move"));
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
			break;
			case "code":
				observer.getOnline().setCode((String) r.get("code"));
				observer.getOnline().setGenerated(true);
			break;
			
		}
		
	}
	
	
	
	public void onDisconnect() {
		Console.s.println("Disconnected from server.");
	}
	
	public void sendMove(Point[] move,long timeStamp) {
		Request mReq=new Request("post","move");
		mReq.set("move", move);
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
		Request cReq=new Request("post","create");
		cReq.set("white", white);
		cReq.set("clocks", clocks);
		queueProcessRequest(cReq);
	}
	
}
