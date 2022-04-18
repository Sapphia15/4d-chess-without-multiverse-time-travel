package unicorns.hypervox.voxel;

import java.awt.Color;
import java.io.Serializable;

import gameutil.math.geom.Point;
import unicorns.Assets;

public class Hypercrate extends Vox implements Pushable,Serializable {
	public static final int ID=1;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Hypercrate() {
		super(Assets.HYPERCRATE);
	}
	
	public Hypercrate(Color c) {
		super(Assets.HYPERCRATE,c);
	}
	
	
	public Hypercrate(Point p) {
		super(p,Assets.HYPERCRATE);
	}
	
	public Hypercrate(Point p,Color c) {
		super(p,Assets.HYPERCRATE,c);
	}
	
	public Hypercrate(int x,int y,int z,int w) {
		super(x,y,z,w,Assets.HYPERCRATE);
	}
	
	public Hypercrate(int x,int y,int z,int w,Color c) {
		super(x,y,z,w,Assets.HYPERCRATE,c);
	}

}
