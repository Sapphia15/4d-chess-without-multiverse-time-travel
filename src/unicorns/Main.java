package unicorns;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.Random;

import gameutil.math.geom.Point;
import gameutil.math.geom.Tuple;
import gameutil.math.geom.Vector;
import gameutil.text.Console;
import gameutil.text.Iru.IruConsole;

public class Main {
	
	public static final Console err=new Console();
	public static final Random rand=new Random();
	public static final Sounds sounds=new Sounds();
	
	public static void main(String[] unicorns) {
		Console.s.setTitle("4d Chess Without Multiverse Time Travel");
		Console.s.println("4d Chess Without Multiverse Time Travel");
		err.setTitle("Errors and Debug");
		err.setTheme(Console.theme.shell2);
		//err.setVisible(false);
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
		panel.setDoubleBuffered(true);
		panel.closeOnExit();
		panel.setTargetFPS(30, 1);
		panel.start();
		
	}
}
