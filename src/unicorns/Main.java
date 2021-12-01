package unicorns;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.Random;

import gameutil.math.geom.Point;
import gameutil.math.geom.Tuple;
import gameutil.math.geom.Vector;
import gameutil.text.Argument;
import gameutil.text.Console;
import gameutil.text.Iru.IruConsole;

public class Main {
	
	public static final Console err=new Console();
	public static final Console cmd=new Console();
	public static final Random rand=new Random();
	public static final Sounds sounds=new Sounds();
	
	public static void main(String[] unicorns) {
		Console.s.setTitle("4d Chess Without Multiverse Time Travel Notation");
		Console.s.println("4d Chess Without Multiverse Time Travel");
		err.setTitle("Errors and Debug");
		err.setTheme(Console.theme.shell2);
		cmd.setTitle("Command Line 4D");
		err.setTheme(Console.theme.shell2);
		err.setVisible(false);
		cmd.setVisible(false);
		sounds.setSourcePath("assets/");
		new Assets();
		//Console.s.println(new Point(new Tuple(new double[] {2,3,1,3})).equals(new Point(new Tuple(new double[] {2,3,1,3}))));
		Frame f=new Frame();
		f.setLocationRelativeTo(null);
		f.setPreferredSize(new Dimension(804,654));
		Panel panel=new Panel(f);
		f.add(panel);
		
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setTitle("4d Chess Without Multiverse Time Travel");
		panel.setDoubleBuffered(true);
		panel.closeOnExit();
		panel.setTargetFPS(30, 1);
		panel.start();
		String c="";
		while (!c.equals("exit")) {
			//cmd.setVisible(true);
			cmd.print(">>>");
			c=cmd.readLine();
			Argument args=Argument.getArgs(c);
			switch(args.get(0)) {
				case "exit":
					System.exit(1);
				break;
				case "connect":
					panel.connectToAlternateServer(args.get(1));
				break;
				case "hide":
					if (args.get(1).equals("cmd")) {
						cmd.setVisible(false);
					} else if (args.get(1).equals("err")) {
						err.setVisible(false);
					} else if (args.get(1).equals("out")) {
						Console.s.setVisible(false);
					}
				break;
				case "show":
					if (args.get(1).equals("err")) {
						err.setVisible(true);
					} else if (args.get(1).equals("out")) {
						Console.s.setVisible(true);
					}
				break;
				case "help":
					if (args.get(1).equals("ingame")) {
						cmd.println("press H to toggle highlighting of spaces that the selected piece can move to");
						cmd.println("press I to invert the view of X and Z dimensions");
						cmd.println("press K to invert the view of Y and W dimensions");
						cmd.println("press P to toggle white/black perspectives");
						cmd.println("press Z to undo a move that hasn't been submitted yet");
						cmd.println("press F to submit a move");
						cmd.println("press S to toggle sound effects at the end of each player's turn");
						cmd.println("press Esc to view the in game menu while in a game");
						cmd.println("press \\ to show this console window");
						cmd.println("piece movement and other help explanations coming soon(ish) (maybe)");
					} else {
						cmd.println("help - show this");
						cmd.println("help ingame - show ingame help");
						cmd.println("connect <address> - connect to the specified server");
						cmd.println("hide <cmd/err/out> - hide the specified console window");
						cmd.println("show <err/out> - show the specified console window (to show this window press the \\ key while in game window)");
						cmd.println("exit - forcefully exit the program");
					}
				break;
			}
		}
	}
}
