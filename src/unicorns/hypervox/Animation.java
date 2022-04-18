package unicorns.hypervox;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;

import gameutil.math.geom.g2D.RectangleR2;

public class Animation implements Serializable {
	int frame=0;
	private transient CopyOnWriteArrayList<Image> imgs=new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<ImageIcon> simgs=new CopyOnWriteArrayList<>();
	long lastUpdate=System.currentTimeMillis();
	long updateTime=30;
	
	public Animation(Image img) {
		imgs.add(img);
	}		
	
	public Animation(Image[] images,long updateTime) {
		for (Image i : images) {
			imgs.add(i);
		}
		this.updateTime=updateTime;
	}
			
	public Animation(Image[] images) {
		for (Image i : images) {
			imgs.add(i);
		}
	}
	
	public Animation() {
	}
	
	public Image get() {
		if (imgs.size()>1) {
			long dt=System.currentTimeMillis()-lastUpdate;
			frame=(frame+(int)(dt/updateTime))%imgs.size();
			if (dt>updateTime) {
				lastUpdate=System.currentTimeMillis();
			}
		}
		return imgs.get(frame);
	}
	
	public Image get(long time) {
		if (imgs.size()>1) {
			long dt=time-lastUpdate;
			frame=(frame+(int)(dt/updateTime))%imgs.size();
			if (dt>updateTime) {
				lastUpdate=time;
			}
		}
		return imgs.get(frame);
	}
	
	public void setFrame(int frame) {
		this.frame=frame;
		lastUpdate=System.currentTimeMillis();
	}
	
	public void setUpdateTime(long updateTime) {
		this.updateTime=updateTime;
	}
	
	public int getFrame() {
		return frame;
	}
	
	public long getLength() {
		return updateTime*imgs.size();
	}
	
	public int getFrames() {
		return imgs.size();
	}
	
	public void addFrame(Image i) {
		imgs.add(i);
	}
	
	public void addFrame(Image i,int index) {
		imgs.add(index,i);
	}
	
	public void removeFrame(int index) {
		imgs.remove(index);
	}
	
	public void prepForSerialization() {
		simgs.clear();
		for (Image i:imgs) {
			simgs.add(new ImageIcon(i));
		}
	}
	
	public void loadImages() {
		imgs=new CopyOnWriteArrayList<>();
		for (ImageIcon i:simgs) {
			imgs.add(i.getImage());
		}
	}
}
