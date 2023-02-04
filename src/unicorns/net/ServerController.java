package unicorns.net;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;

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
		System.out.println("Connected to client: "+s.getInetAddress().getHostAddress());
		System.out.println("ID: "+id);
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
					System.out.println("Game removed:\n  Code: "+game.getCode());
				}
				game=Server.createGame(this, (boolean) r.get("white"),(long)r.get("clocks"),(long)r.get("delay"),(long)r.get("bonus"),(String)r.get("variant"));
				Request codeReq=new Request("post","code");
				codeReq.set("code", game.getCode());
				queueProcessRequest(codeReq);
				System.out.println((boolean)r.get("white"));
				System.out.println((long) r.get("clocks"));
				System.out.println("Game created by client:\n     Code: "+game.getCode()+"\n     ID: "+clientID+"\n     White: "+(boolean) r.get("white")+"\n     Clocks: "+((long) r.get("clocks")));
			break;
			case "code":
				try {
					game=Server.getGame((String)r.get("code"));
					boolean connected=game.connect(this);
					Request gameReq=new Request("post","game");
					if (connected) {
						System.out.println(clientID+" joined game "+game.getCode());
						gameReq.set("success", true);
						boolean white=game.getPlayerID(true)==clientID;
						gameReq.set("white", white);
						gameReq.set("clocks", game.getClockTime());
						gameReq.set("delay", game.getDelay());
						gameReq.set("bonus", game.getBonus());
						gameReq.set("variant", game.getVariant());
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
					System.out.println("Exception!");
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
				if (game!=null) {
					Server.games.remove(game);
					System.out.println("Game removed:\n  Code: "+game.getCode());
					game=null;
				}
			break;
		}
		
	}
	
	public void onDisconnect() {
		System.out.println("ID: "+clientID+" Address: "+s.getInetAddress().getHostAddress()+" disconnected.");
		if (partner!=null) {
			partner.queueProcessRequest(new Request("post","disconnect"));
			partner=null;
		}
		if (game!=null) {
			Server.games.remove(game);
			System.out.println("Game removed:\n  Code: "+game.getCode());
		}
	}
	
	public int getClientID() {
		return clientID;
	}
	
}
