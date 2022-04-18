package unicorns.hypervox.world;

import gameutil.math.geom.Point;

public class PointAndWorld {
	private World w;
	private Point p;
	
	public PointAndWorld(Point p,World w) {
		this.p=p;
		this.w=w;
	}
	
	public World getWorld() {
		return w;
	}
	public Point getPoint() {
		return p;
	}
}
