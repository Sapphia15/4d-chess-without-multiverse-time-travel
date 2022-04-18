package unicorns.hypervox.voxel;

import java.awt.Color;
import java.awt.Image;

import gameutil.math.geom.Point;
import unicorns.Assets;

public class Wall extends Vox implements Solid{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8340684910961804337L;
	public Wall(Point p) {
		super(p,Assets.WALL);
	}
	
	public Wall(int x,int y,int z, int w) {
		super(x,y,z,w,Assets.WALL);
	}
	
	public Wall() {
		super(Assets.WALL);
	}
	
	public Wall(Point p,Color c) {
		super(p,Assets.WALL,c);
	}
	
	public Wall(int x,int y,int z, int w,Color c) {
		super(x,y,z,w,Assets.WALL,c);
	}
	
	public Wall(Color c) {
		super(Assets.WALL,c);
	}
	
	public Wall(Point p,Image i) {
		super(p,i);
	}
	
	public Wall(Point p,Image i,Color c) {
		super(p,i,c);
	}
	
	public Wall(int x,int y,int z, int w,Image i) {
		super(x,y,z,w,i);
	}
	
	public Wall(Image i) {
		super(i);
	}
}
