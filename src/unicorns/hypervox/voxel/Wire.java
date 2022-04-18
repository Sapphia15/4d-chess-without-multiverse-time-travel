package unicorns.hypervox.voxel;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import gameutil.math.geom.Point;
import gameutil.math.geom.Vector;
import unicorns.Assets;
import unicorns.Main;
import unicorns.hypervox.Animation;
import unicorns.hypervox.world.Paravox;
import unicorns.hypervox.world.World;

public class Wire extends Vox {
	
	protected double power=0;//Main.rand.nextInt(2);
	protected double nextPow=0;
	protected Animation on=Assets.WIRE_ON;
	
	static Vector[] unitVecs=new Vector[] {
		new Vector(new double[] {1,0,0,0}),
		new Vector(new double[] {0,1,0,0}),
		new Vector(new double[] {0,0,1,0}),
		new Vector(new double[] {0,0,0,1}),
	};

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Wire() {
		super(Assets.WIRE_OFF);
	}
	
	public Wire(Color c) {
		super(Assets.WIRE_OFF,c);
	}
	
	
	public Wire(Point p) {
		super(p,Assets.WIRE_OFF);
	}
	
	public Wire(Point p,Color c) {
		super(p,Assets.HYPERCRATE,c);
	}
	
	public Wire(int x,int y,int z,int w) {
		super(x,y,z,w,Assets.WIRE_OFF);
	}
	
	public Wire(int x,int y,int z,int w,Color c) {
		super(x,y,z,w,Assets.WIRE_OFF,c);
	}
	
	public void update(World world) {
		super.update(world);
		power=nextPow;
		ArrayList<Wire> wires=getNeighboringWires(world);
		powerUpdate(wires);
	}
	
	public void powerUpdate(ArrayList<Wire> wires) {
		transmitterPowerUpdate(wires);
	}
	
	protected void transmitterPowerUpdate(ArrayList<Wire> wires) {
		nextPow=0;

		double mostPower=power;
		for (Wire w:wires) {
			if (w.power>mostPower) {
				mostPower=w.power;
				//nextPow+=w.power*.998/wires.size();
			}
		}
		nextPow+=mostPower-1;
		if (nextPow<0) {
			nextPow=0;
		}
	}
	
	protected ArrayList<Wire> getNeighboringWires(World world){
		ArrayList<Wire> wires=new ArrayList<>();
		for (int i=0;i<2;i++) {
			for (Vector v:unitVecs) {
				Point p=new Point(getPos().tuple.$A$(v.getSpds().$X$(1-2*i)));
				try {
					
					for (Vox vox:world.getVoxelsAt(p,getLayer())) {
						if (vox instanceof Wire) {
							wires.add((Wire)vox);
						} else if (vox instanceof Paravox) {
							Wire wireInParavox=getWireInParavox((Paravox) vox,v.$X$(1-2*i));
							if (wireInParavox!=null) {
								wires.add(wireInParavox);
							}
						} else if (world instanceof Paravox) {
							if (Paravox.isExit(p)) {
								Wire wireOutOfParavox=getWireOutOfParavox((Paravox) world,v.$X$(1-2*i));
								if (wireOutOfParavox!=null) {
									wires.add(wireOutOfParavox);
								}
							}
						}
					}
				} catch(NullPointerException e) {
					//e.printStackTrace();
				}
			}
		}
		return wires;
	}
	
	private Wire getWireOutOfParavox(Paravox pvox,Vector v) {
		try {
			World world=pvox.getParent();
			Point p=new Point(pvox.getPos().tuple.$A$(v.getSpds()));
			for (Vox vox:world.getVoxelsAt(p,getLayer())) {
				if (vox instanceof Wire) {
					return (Wire)vox;
				} else if (vox instanceof Paravox) {
					return getWireInParavox((Paravox) vox,v);
				} else if (world instanceof Paravox) {
					if (Paravox.isExit(p)) {
						if (!world.equals(pvox)) {
							return getWireOutOfParavox((Paravox)world,v);
						}
					}
				}
			}
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
			
	
	private Wire getWireInParavox(Paravox vox,Vector v) {
			for (Vox voxInParavox:((Paravox) vox).getVoxelsAt(new Point(v.$X$(2)), getLayer())) {
				if (voxInParavox instanceof Wire) {
					return(Wire)voxInParavox;
				} else if (voxInParavox instanceof Paravox) {
					return getWireInParavox((Paravox)voxInParavox,v);
				}
			}
			return null;
	}
	
	public void prepForSerialization() {
		super.prepForSerialization();
		on.prepForSerialization();
	}
	
	public void loadImage() {
		super.loadImage();
		on.loadImages();
	}
	
	public Image getImage() {
		if (power<.01) {
			return i;
		} else {
			return on.get();
			
		}
		
	}
	
	public double getPower() {
		return power;
	}

}
