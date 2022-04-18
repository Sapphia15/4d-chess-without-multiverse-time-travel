package unicorns.hypervox.voxel;

import java.awt.Color;
import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

import gameutil.Cloner;
import gameutil.math.geom.HyperVoxel;
import gameutil.math.geom.Point;
import unicorns.Assets;
import unicorns.hypervox.world.World;

public class Vox extends HyperVoxel implements Serializable{
	public static final int ID=1;

	protected Color c=Color.green;
	protected transient Image i=Assets.VOX;
	protected int layer=0;
	protected ImageIcon im;
	protected long lastUpdate=System.currentTimeMillis();
	
	public Vox(Point p) {
		super(p);
	}
	
	public Vox(int x,int y,int z, int w) {
		this(new Point(new double[] {x,y,z,w}));
	}
	
	public Vox() {
		this(0,0,0,0);
	}
	
	public Vox(Point p,Color c) {
		this(p);
		this.c=c;
	}
	
	public Vox(int x,int y,int z, int w,Color c) {
		this(x,y,z,w);
		this.c=c;
	}
	
	public Vox(Color c) {
		this();
		this.c=c;
	}
	
	public Vox(Image i,Color c) {
		this(i);
		this.c=c;
	}
	
	public Vox(Point p,Image i) {
		this(p);
		this.i=i;
	}
	
	public Vox(Point p,Image i,Color c) {
		this(p,c);
		this.i=i;
	}
	
	public Vox(int x,int y,int z, int w,Image i) {
		this(x,y,z,w);
		this.i=i;
	}
	
	public Vox(int x,int y,int z, int w,Image i,Color c) {
		this(x,y,z,w,c);
		this.i=i;
	}
	public Vox(Image i) {
		this();
		this.i=i;
	}
	
	public Color getColor() {
		return c;
	}
	
	public void setColor(Color c) {
		this.c=c;
	}
	
	public Image getImage() {
		return i;
	}
	
	public void prepForSerialization() {
		im=new ImageIcon(i);
	}
	
	public void loadImage() {
		i=im.getImage();
	}

	public int getLayer() {
		return layer;
	}
	
	public void setLayer(int layer) {
		this.layer=layer;
	}

	public Vox clone() {
		prepForSerialization();
		Vox clone=(Vox)Cloner.Clone(this);
		clone.loadImage();
		return clone;
	}
	
	public void update(World w) {
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
