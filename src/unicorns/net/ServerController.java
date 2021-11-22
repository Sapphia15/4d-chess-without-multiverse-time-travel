package unicorns.net;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;

import gameutil.text.Console;
import net.Controller;
import net.Request;
import unicorns.OnlineGame;

public class ServerController extends Controller{

	static int id=1;
	int clientID;
	OnlineGame game;
	ServerController partner;
	
	public ServerController(Socket s, ObjectOutputStream out) {
		super(s, out);
		Console.s.println("Connected to client: "+s.getInetAddress().getHostAddress());
		Console.s.println("ID: "+id);
		Request idReq=new Request("post","ID");
		idReq.set("ID", id);
		clientID=id;
		id++;
		this.queueProcessRequest(idReq);
	}

	@Override
	public void get(Request r, String route) {
		switch (route) {
			case "create":
				if (game!=null) {
					Server.games.remove(game);
					Console.s.println("Game removed:\n  Code: "+game.getCode());
				}
				game=Server.createGame(this, (boolean) r.get("white"),(long)r.get("clocks"));
				Request codeReq=new Request("post","code");
				codeReq.set("code", game.getCode());
				queueProcessRequest(codeReq);
				Console.s.println((boolean)r.get("white"));
				Console.s.println((long) r.get("clocks"));
				Console.s.println("Game created by client:\n     Code: "+game.getCode()+"\n     ID: "+clientID+"\n     White: "+(boolean) r.get("white")+"\n     Clocks: "+((long) r.get("clocks")>0));
			break;
			case "code":
				try {
					game=Server.getGame((String)r.get("code"));
					boolean connected=game.connect(this);
					Request gameReq=new Request("post","game");
					if (connected) {
						Console.s.println(clientID+" joined game "+game.getCode());
						gameReq.set("success", true);
						boolean white=game.getPlayerID(true)==clientID;
						gameReq.set("white", white);
						gameReq.set("clocks", game.getWhiteTime());
						if (white) {
							partner=game.getBlack();
						} else {
							partner=game.getWhite();
						}
						partner.partner=this;
						//partner not noticing that opponent joining when they make a game as black for some reason...
						partner.queueProcessRequest(new Request("post","joined"));
						
					} else {
						gameReq.set("success", false);
					}
					queueProcessRequest(gameReq);
				} catch (Exception e) {
					Console.s.println("Exception!");
					e.printStackTrace();
					//send error to client
					
				}
			break;
			case "confirm":
				r.set("type", "post");
				partner.queueProcessRequest(r);
			break;
			case "move":
				//forward request to partner
				r.set("type", "post");
				partner.queueProcessRequest(r);
			break;
			case "exit":
				if (partner!=null) {
					partner.queueProcessRequest(new Request("post","disconnect"));
					partner=null;
				}
				Server.games.remove(game);
				Console.s.println("Game removed:\n  Code: "+game.getCode());
				game=null;
			break;
		}
		
	}
	
	public void onDisconnect() {
		Console.s.println("ID: "+clientID+" Address: "+s.getInetAddress().getHostAddress()+" disconnected.");
		if (game!=null) {
			Server.games.remove(game);
			Console.s.println("Game removed:\n  Code: "+game.getCode());
		}
	}
	
	public int getClientID() {
		return clientID;
	}
	
}
