package unicorns;

import java.awt.Dimension;
import java.awt.Frame;

import gameutil.math.geom.Vector;
import gameutil.text.Console;

public class Main {
	
	public static final Board b=new Board();
	
	public static void main(String[] unicorns) {
		Console.s.setTitle("4d Chess Without Multiverse Time Travel");
		Console.s.println("4d Chess Without Multiverse Time Travel");
		for (int i=0;i<4;i++) {
			Console.s.println(Vector.getUnitVector(4).getSpds().i(i));
		}
		b.setUp();
		Frame f=new Frame();
		f.setLocationRelativeTo(null);
		f.setPreferredSize(new Dimension(504,504));
		Panel panel=new Panel(f);
		f.add(panel);
		f.pack();
		
		f.setVisible(true);
		panel.setDoubleBuffered(true);
		panel.closeOnExit();
		
		panel.setTargetFPS(30, 1);
		panel.start();
		
	}
}
