package unicorns.hypervox.voxel;

import java.awt.Color;
import java.util.ArrayList;

import gameutil.math.geom.Point;
import gameutil.math.geom.Vector;
import unicorns.Assets;

public class DiodeInverter extends Wire{
	
	Vector in=Wire.unitVecs[1].$X$(-1);
	
	public DiodeInverter() {
		super();
	}
	
	public DiodeInverter(Color c) {
		super(c);
	}
	
	
	public DiodeInverter(Point p) {
		super(p);
	}
	
	public DiodeInverter(Point p,Color c) {
		super(p,c);
	}
	
	public DiodeInverter(int x,int y,int z,int w) {
		super(x,y,z,w);
	}
	
	public DiodeInverter(int x,int y,int z,int w,Color c) {
		super(x,y,z,w,c);
	}

	public void powerUpdate(ArrayList<Wire> wires) {
		nextPow=37;
		for (Wire w:wires) {
			if (new Vector(w.getPos().tuple.$S$(getPos().tuple)).equals(in)) {
				if (w.power>0) {
					nextPow=0;
				}
				
			}
		}
	}
}
