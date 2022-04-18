package unicorns.hypervox.voxel;

import java.awt.Color;
import java.util.ArrayList;

import gameutil.math.geom.Point;
import unicorns.Assets;

public class HGen extends Wire{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public HGen() {
		super();
		init();
	}
	
	public HGen(Color c) {
		super(c);
		init();
	}
	
	
	public HGen(Point p) {
		super(p);
		init();
	}
	
	public HGen(Point p,Color c) {
		super(p,c);
		init();
	}
	
	public HGen(int x,int y,int z,int w) {
		super(x,y,z,w);
		init();
	}
	
	public HGen(int x,int y,int z,int w,Color c) {
		super(x,y,z,w,c);
		init();
	}

	private void init() {
		i=Assets.H_GEN_OFF;
		on=Assets.H_GEN_ON;
		power=37;
	}
	
	public void powerUpdate(ArrayList<Wire> wires) {
		nextPow=37;
		for (Wire w:wires) {
			if (w.power>0){
				nextPow=0;
				break;
			}
		}
	}
	
}
