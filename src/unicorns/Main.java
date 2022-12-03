 package unicorns;

import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFileChooser;

import gameutil.math.geom.Point;
import gameutil.math.geom.Tuple;
import gameutil.math.geom.Vector;
import gameutil.text.Argument;
import gameutil.text.Console;
import gameutil.text.Iru.IruConsole;
import unicorns.hypervox.CharMap;

public class Main {
	
	public static final Console err=new Console();
	public static final Console cmd=new Console();
	public static final Random rand=new Random();
	public static final Sounds sounds=new Sounds();
	public static final Frame f=new Frame();
	public static Panel panel;
	public static final JFileChooser fc = new JFileChooser();
	
	public static void main(String[] unicorns) {
		Console.s.setTitle("4d Chess Notation");
		Console.s.println("4d Chess");
		err.setTitle("Errors and Debug");
		err.setTheme(Console.theme.shell2);
		cmd.setTitle("Command Line 4D");
		err.setTheme(Console.theme.shell2);
		err.setVisible(false);
		cmd.setVisible(false);
		sounds.setSourcePath("assets/");
		new Assets();
		//Console.s.println(new Point(new Tuple(new double[] {2,3,1,3})).equals(new Point(new Tuple(new double[] {2,3,1,3}))));
		f.setLocationRelativeTo(null);
		f.setPreferredSize(new Dimension(804,654));
		panel=new Panel(f);
		f.add(panel);
		
		f.pack();
		Main.sounds.playSoundOnLoop("Xeraphina's_Dancing_Song.wav", 0);
		Main.sounds.pauseSound("Xeraphina's_Dancing_Song.wav");
		Main.sounds.playSoundOnLoop("tutorial.wav", 0);
		Main.sounds.pauseSound("tutorial.wav");
		Main.sounds.playSoundOnLoop("For_Dee.wav", 0);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setExtendedState(Frame.MAXIMIZED_BOTH);
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
					} else if (args.get(1).equals("")){
						cmd.setVisible(false);
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
					} else if (args.get(1).equals("hypervox")) {
						cmd.println("press W to go forward");
						cmd.println("press A to go left");
						cmd.println("press S to go back");
						cmd.println("press D to go right");
						cmd.println("press SPACE to go up");
						cmd.println("press Q to go kata");
						cmd.println("press SHIFT to go down");
						cmd.println("press E to go ana");
						cmd.println("press P to place a parabox in the place where you are when you move out of that spot");
					} else {
						cmd.println("help - show this");
						cmd.println("help ingame - show ingame help");
						cmd.println("connect <address> - connect to the specified server");
						cmd.println("hide <cmd/err/out> - hide the specified console window");
						cmd.println("show <err/out> - show the specified console window (to show this window press the \\ key while in game window)");
						cmd.println("exit - forcefully exit the program");
					}
				break;
				case "save":
					if (args.get(1).equals("")) {
						try {
							panel.getHyperVox().getWorld().save();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						try {
							panel.getHyperVox().getWorld().saveAs(args.get(1));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				break;
				case "load":
					panel.getHyperVox().loadWorld(args.get(1));
				break;
				case "ls":
					for (File file:new File("hyperVox/worlds/").listFiles()) {
						if (file.isFile()&&file.getName().endsWith(".wrld")) {
							cmd.println(file.getName().substring(0, file.getName().lastIndexOf(".wrld")));
						}
					}
				break;
			}
		}
	}
	
	public static File openFile() {
		if (fc.showOpenDialog(panel)==JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		} else {
			return null;
		}
	}
	
	public static File saveFile() {
		if (fc.showSaveDialog(panel)==JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		} else {
			return null;
		}
	}
}
