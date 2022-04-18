package unicorns.hypervox.world;

import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import gameutil.Cloner;
import gameutil.math.geom.Point;
import gameutil.math.geom.Tuple;
import gameutil.math.geom.Vector;
import unicorns.Assets;
import unicorns.hypervox.voxel.Pushable;
import unicorns.hypervox.voxel.Solid;
import unicorns.hypervox.voxel.Vox;
import unicorns.hypervox.voxel.Wall;

public class Paravox extends World implements Pushable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static ConcurrentHashMap<Integer, CopyOnWriteArrayList<Vox>> template;
	World parent;
	/*
	 //to get the entrence point you can just take the unit vector of direction of entry and multiply it by 2 or (tesseract sideLength - 1)/2
	 //I only realized this after already typing them here
	private static Point xTop=new Point(new Tuple(new double[] {2,0,0,0}));
	private static Point yTop=new Point(new Tuple(new double[] {0,2,0,0}));
	private static Point zTop=new Point(new Tuple(new double[] {0,0,2,0}));
	private static Point wTop=new Point(new Tuple(new double[] {0,0,0,2}));
	private static Point xBot=new Point(new Tuple(new double[] {-2,0,0,0}));
	private static Point yBot=new Point(new Tuple(new double[] {0,-2,0,0}));
	private static Point zBot=new Point(new Tuple(new double[] {0,0,-2,0}));
	private static Point wBot=new Point(new Tuple(new double[] {0,0,0,-2}));*/
	
	public Paravox() {
		this((World)(null));
		parent=this;
		addVox(this);
	}
	
	public Paravox(Point point, Color c) {
		this(c);
		setPos(point);
		parent=this;
		addVox(this);
	}
	
	public Paravox(World w) {
		i=Assets.PARAVOX;
		
		initTemplate(c);
		voxels=template;
		parent=w;
	}
	
	public Paravox(Point point, Color c,World w) {
		super.setColor(c);
		i=Assets.PARAVOX;
		
		initTemplate(c);
		voxels=template;
		parent=w;
		setPos(point);
		
	}
	
	public Paravox(Color c) {
		super.setColor(c);
		i=Assets.PARAVOX;
		
		initTemplate(c);
		voxels=template;
		parent=this;
		addVox(this);
	}
	
	public Paravox(Color c,World w) {
		super.setColor(c);
		i=Assets.PARAVOX;
		
		initTemplate(c);
		voxels=template;
		parent=w;
	}

	

	public Point getEntryPoint(Vector entryDirection) {
		return new Point(entryDirection.normalize().$X$(2));
	}
	
	
	public static boolean isExit(Point p) {
		int zeros=0;
		double nonZero=0;
		for (int i=0; i<4;i++) {
			if (p.tuple.i(i)==0) {
				zeros++;
			} else {
				nonZero=p.tuple.i(i);
			}
		}
		return zeros==3&&Math.abs(nonZero)==2;
	}
	
	public World getParent() {
		return parent;
	}
	
	public void setParent(World w) {
		this.parent=w;
	}
	
	private static void initTemplate(Color c) {
		template=new ConcurrentHashMap<>();
		template.put(0, new CopyOnWriteArrayList<Vox>());
		template.put(1, new CopyOnWriteArrayList<Vox>());
		//generate walls that share a face with each of the 8 cells (makes 8 orthotopes that each share a face with a different cell of the tesseract)
		//this is done so that each paravox has a 5x5x5x5 tesseract of open space that is enclosed within generated walls.
		for (int i=-3;i<4;i++) {
			for (int j=-3;j<3;j++) {
				for (int k=-3;k<4;k++) {
					Vox[] voxels=new Vox[]{
						
						new Wall(i,j,k,3,c),
						new	Wall(i,j,3,k,c),
						new Wall(i,3,j,k,c),
						new Wall(3,i,j,k,c),
					
						new Wall(i,j,k,-3,c),
						new Wall(i,j,-3,k,c),
						new Wall(i,-3,j,k,c),
						new Wall(-3,i,j,k,c),
					};
					for (Vox v:voxels) {
						if (isExit(new Point(new Vector(v.getPos()).normalize().$X$(2)))){
							v=new Wall(v.getPos(),Assets.EXIT,c);
						}
						if (!template.get(1).contains(v)) {
							template.get(1).add(v);
						}
					}
				}
			}
		}
		//Generate floor (just a filled 5x5x5x5 tesseract centered on (0,0,0,0))
		for (int x=-2;x<3;x++) {
			for (int y=-2;y<3;y++) {
				for (int z=-2;z<3;z++) {
					for (int w=-2;w<3;w++) {
						template.get(0).add(new Vox(x,y,z,w,c));
					}
				}
			}
		}
	}
	
	@Override
	public void setColor(Color c) {
		super.setColor(c);
		for (Vox v:voxels.get(0)) {
			if (!v.equals(this)) {
				v.setColor(c);
			}
		}
		for (Vox v:voxels.get(1)) {
			if (!v.equals(this)) {
				v.setColor(c);
			}
		}
	}
	
}
