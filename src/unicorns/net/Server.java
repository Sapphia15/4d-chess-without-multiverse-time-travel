package unicorns.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import gameutil.text.Console;
import unicorns.OnlineGame;

public class Server extends net.Server{
	static CopyOnWriteArrayList<ServerController> controllers=new CopyOnWriteArrayList<>();
	static CopyOnWriteArrayList<String> codes=new CopyOnWriteArrayList<>();
	static CopyOnWriteArrayList<OnlineGame> games=new CopyOnWriteArrayList<>();
	public static Server server;
	public static final Random rand=new Random();
	public Server(ServerSocket s, int clientCap) {
		super(s, clientCap);
		
	}

	public static void main(String[] unicorns) {
		Console.s.setTheme(Console.theme.shell2);
		Console.s.setTitle("4d Chess Server");
		Console.s.println("Server starting...");
		try {
			server=new Server(new ServerSocket(25565),-1);
			Console.s.println("Server started");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Console.s.println("ERROR: Server failed to start.");
		};
		
	}

	@Override
	public void prepareNewClient(Socket s) {
		try {
			new ServerController(s,new ObjectOutputStream(s.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Console.s.println("IO Exception");
			e.printStackTrace();
		}
	}
	
	public static OnlineGame createGame(int id,boolean white,long clocks) {
		OnlineGame game=new OnlineGame(id,white,generateCode(),clocks);
		games.add(game);
		return game;
	}
	
	public static String generateCode() {
		char[] chars=new char[] {'g','p','d','u','n','b','r','q','k','G','P','D','U','N','B','R','Q','K'};
		String code="";
		for (int i=0;i<5; i++) {
			code=code+chars[rand.nextInt(chars.length)];
		}
		//no duplicate codes
		try {
			if (codes.contains(code)) {
				return generateCode();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		codes.add(code);
		return code;
	}
	
	public static OnlineGame getGame(String code) {
		for (OnlineGame game:games) {
			if (game.getCode().equals(code)) {
				return game;
			}
		}
		return null;
	}
}
