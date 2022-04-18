package unicorns.hypervox.voxel;

import java.awt.Color;
import java.util.ArrayList;

import gameutil.math.geom.Point;
import unicorns.Assets;

public class Idea extends Wire{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean wasPowered=false;
	
	public Idea() {
		super();
		init();
	}
	
	public Idea(Color c) {
		super(c);
		init();
	}
	
	
	public Idea(Point p) {
		super(p);
		init();
	}
	
	public Idea(Point p,Color c) {
		super(p,c);
		init();
	}
	
	public Idea(int x,int y,int z,int w) {
		super(x,y,z,w);
		init();
	}
	
	public Idea(int x,int y,int z,int w,Color c) {
		super(x,y,z,w,c);
		init();
	}
	
	public void whilePowered(){
		
	}
	
	public void onPowerOn() {
		
	}
	
	public void onPowerOff() {
		
	}
	
	private void init() {
		/*i=Assets.H_GEN_OFF;
		on=Assets.H_GEN_ON;*/
	}
	
	public void powerUpdate(ArrayList<Wire> wires) {
		nextPow=0;
		boolean powered=false;
		for (Wire w:wires) {
			if (w.power>0){
				//do task if powered
				whilePowered();
				if (!wasPowered) {
					wasPowered=true;
					onPowerOn();
				}
				powered=true;
				break;
			}
		}
		if (!powered&&wasPowered) {
			wasPowered=false;
			onPowerOff();
		}
	}
	
	
}
