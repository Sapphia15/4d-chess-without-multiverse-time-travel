package unicorns.hypervox;

import gameutil.math.geom.Orthotope;
import gameutil.math.geom.Point;

public class Viewport extends Orthotope{
	
	/**Generates hypercubical viewport
	 * 
	 * @param pos
	 * @param size
	 */
	public Viewport(Point pos,int size) {
		super(pos,size);
	}
	
	/**Generates hypercubical viewport
	 * 
	 * @param pos
	 * @param size
	 */
	public Viewport(double x,double y, double z, double w,int size) {
		this(new Point(new double[] {x,y,z,w}),size);
	}
	
	/**Generates hypercubical viewport at 0,0,0,0
	 * 
	 * @param pos
	 * @param size
	 */
	public Viewport(int size) {
		this(0,0,0,0,size);
	}
	
	/**sets the center space (only works well for odd sizes that have a center space)
	 * 
	 * @param p
	 */
	public void setCenterSpace(Point p) {
		setPos(new Point(p.tuple.$S$(2)));
	}
	
	public Point toBoardPoint(Point p) {
		return new Point(p.tuple.$S$(this.getPos().tuple));
	}
	
	public Point toWorldPoint(Point p) {
		return new Point(p.tuple.$A$(this.getPos().tuple));
	}

}
