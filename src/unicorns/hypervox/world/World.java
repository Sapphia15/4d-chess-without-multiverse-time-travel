package unicorns.hypervox.world;

import java.awt.Color;
import java.awt.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;

import Sprite.Sprite;
import gameutil.math.geom.Point;
import unicorns.hypervox.Player;
import unicorns.hypervox.Viewport;
import unicorns.hypervox.voxel.Solid;
import unicorns.hypervox.voxel.Vox;

public class World extends Vox implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ConcurrentHashMap<Integer,CopyOnWriteArrayList<Vox>> voxels=new ConcurrentHashMap<>();
	Player player=new Player();
	String name=new Date().toString();
	int timeDilation=0;
	int timeDilationStep=0;
	
	public ConcurrentHashMap<Integer,CopyOnWriteArrayList<Vox>> getScreenVox(Viewport v){
		ArrayList<Vox> screenVoxels=new ArrayList<>();
		World screen=new World();
		for (int i:voxels.keySet()) {
			for (Vox vox:(CopyOnWriteArrayList<Vox>) voxels.get(i).clone()) {
				if (v.contains(vox)) {
					screen.addVox(vox);
				}
			}
		}
		return screen.voxels;
	}
	
	public void addVox(Vox v) {
		//removeVox(v.getPos());
		CopyOnWriteArrayList<Vox> layer=voxels.get(v.getLayer());
		if (layer==null) {
			layer=new CopyOnWriteArrayList<>();
			voxels.put(v.getLayer(), layer);
		}
		layer.add(v);
	}
	
	public void removeVox(Vox v) {
		/*CopyOnWriteArrayList<Vox> layer=voxels.get(v.getLayer());
		if (layer!=null) {
			layer.remove(v);
			if (layer.size()==0) {
				voxels.remove(v.getLayer());
			}
		}*/
		//CopyOnWriteArrayList<Vox> layer=voxels.get(v.getLayer());
		if (voxels.keySet().contains(v.getLayer())) {
			voxels.get(v.getLayer()).remove(v);
			if (voxels.get(v.getLayer()).size()==0) {
				voxels.remove(v.getLayer());
			}
		}
		
	}
	
	public ConcurrentHashMap<Integer, CopyOnWriteArrayList<Vox>> getVoxelsAt(Point p){
		World w=new World();
		for (int i:voxels.keySet()) {
			for (Vox v:(CopyOnWriteArrayList<Vox>) voxels.get(i).clone()) {
				if (v.getPos().equals(p)) {
					w.addVox(v);
				}
			}
		}
		return w.voxels;
	}
	
	public CopyOnWriteArrayList<Vox> getVoxelsAt(Point p, int layer){
		return getVoxelsAt(p).get(layer);
	}
	
	public void removeVox(Point p) {
		for (int i:voxels.keySet()) {
			for (Vox v:(CopyOnWriteArrayList<Vox>) voxels.get(i).clone()) {
				if (v.getPos().equals(p)) {
					this.voxels.get(i).remove(v);
				}
			}
		}
		
	}
	
	public void removeVox(Point p,int layer) {
		if (voxels.keySet().contains(layer)) {
			for (Vox v:(CopyOnWriteArrayList<Vox>) voxels.get(layer).clone()) {
				if (v.getPos().equals(p)) {
					this.voxels.get(layer).remove(v);
				}
			}
		}
		
	}
	
	public void removeVox(Point p,int lowestLayer,int highestLayer) {
		for (int i:voxels.keySet()) {
			if (i>=lowestLayer&&i<=highestLayer) {
				for (Vox v:(CopyOnWriteArrayList<Vox>) voxels.get(i).clone()) {
					if (v.getPos().equals(p)) {
						this.voxels.get(i).remove(v);
					}
				}
			}
		}
		
	}
	
	public static boolean hasSolid(ConcurrentHashMap<Integer, CopyOnWriteArrayList<Vox>> voxels) {
		for (int i:voxels.keySet()){
			for (Vox v:voxels.get(i)) {
				try {
					if (v instanceof Solid) {
						return true;
					}
				} catch (Exception e) {
					
				}
			}
		}
		return false;
	}
	
	public static boolean hasParavox(ConcurrentHashMap<Integer, CopyOnWriteArrayList<Vox>> voxels) {
		for (int i:voxels.keySet()){
			for (Vox v:voxels.get(i)) {
				try {
					if (v instanceof Paravox) {
						return true;
					}
				} catch (Exception e) {
					
				}
			}
		}
		return false;
	}
	
	public static World load(String name) {
		if (new File("hypervox/worlds/"+name+".wrld").exists()) {
			try {
				ObjectInputStream in=new ObjectInputStream(new FileInputStream("hypervox/worlds/"+name+".wrld"));
				World w=(World)in.readObject();
				if (w!=null) {
					w.loadImage();
				}
				return w;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void save(String path) throws IOException {
    	//save to path
        ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(path));
       
        prepForSerialization();
        writer.writeObject(this);
        writer.close();
    }
	
	public boolean solidAt(Point p) {
		ConcurrentHashMap<Integer,CopyOnWriteArrayList<Vox>> voxAt=getVoxelsAt(p);
		for (int i:voxAt.keySet()) {
			for (Vox v:voxAt.get(i)) {
				if (v instanceof Solid) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean solidAt(Point p,int layer) {
		for (Vox v:getVoxelsAt(p,layer)) {
			if (v instanceof Solid) {
				return true;
			}
		}
		return false;
	}
	
	/**returns weather or not there is a solid in the specified layer range
	 * 
	 * @param p
	 * @param lowestLayer
	 * @param highestLayer
	 * @return
	 */
	public boolean solidAt(Point p,int lowestLayer,int highestLayer) {
		ConcurrentHashMap<Integer,CopyOnWriteArrayList<Vox>> voxAt=getVoxelsAt(p);
		for (int i:voxAt.keySet()) {
			if (i>=lowestLayer&&i<=highestLayer) {
				for (Vox v:voxAt.get(i)) {
					if (v instanceof Solid) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void save() throws IOException {
    	save("hypervox/worlds/"+name+".wrld");
    }
	
	public void saveAs(String name) throws IOException{
		this.name=name;
		save();
	}
	
	public void loadImage() {
		super.loadImage();
		player.loadImage();
		for (int i:voxels.keySet()) {
			for (Vox v:(CopyOnWriteArrayList<Vox>) voxels.get(i).clone()) {
				if (!v.equals(this)) {
					v.loadImage();
				}
				
			}
		}
	}
	
	public void prepForSerialization() {
		super.prepForSerialization();
		 for (int i:voxels.keySet()) {
			for (Vox v:(CopyOnWriteArrayList<Vox>) voxels.get(i).clone()) {
				if (!v.equals(this)) { //check if the voxel is this to prevent an infinite loop of prepForSerialization calls
					v.prepForSerialization();
				}
			}
		}
		 player.prepForSerialization();
	}
	
	public void update() {
		this.update(null);
	}
	
	public void setTimeDilation(int dilation) {
		timeDilation=dilation;
	}
	public int getTimeDilation() {
		return timeDilation;
	}
	
	public void update(World w) {
		timeDilationStep++;
		if (timeDilationStep>timeDilation) {
			timeDilationStep=0;
			super.update(w);
			for (int i:voxels.keySet()) {
				for (Vox v:voxels.get(i)) {
					if (!v.equals(this)) {
						v.update(this);
					}
				}
			}
		}
		
	}
	
}
