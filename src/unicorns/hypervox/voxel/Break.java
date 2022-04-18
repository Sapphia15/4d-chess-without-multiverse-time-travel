package unicorns.hypervox.voxel;

import java.awt.Color;
import java.util.ArrayList;

import gameutil.math.geom.Point;
import unicorns.Assets;

public class Break extends Idea{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Break() {
		super();
		init();
	}
	
	public Break(Color c) {
		super(c);
		init();
	}
	
	
	public Break(Point p) {
		super(p);
		init();
	}
	
	public Break(Point p,Color c) {
		super(p,c);
		init();
	}
	
	public Break(int x,int y,int z,int w) {
		super(x,y,z,w);
		init();
	}
	
	public Break(int x,int y,int z,int w,Color c) {
		super(x,y,z,w,c);
		init();
	}

	private void init() {
		i=Assets.BREAK;
		on=Assets.H_GEN_ON;
	}
	
	public void onPowerOn() {
		
	}
}
