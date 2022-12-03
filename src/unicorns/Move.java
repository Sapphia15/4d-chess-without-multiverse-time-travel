package unicorns;

import gameutil.math.geom.Point;

public class Move {
	Point[] points;
	char promotion='q';
	
	public Move(Point[] p, boolean white) {
		char q = 'q';
		if (white) {
			q='Q';
		}
		promotion=q;
		this.points=p.clone();
	}
	
	public Move(Point[] p,char c) {
		this.points=p.clone();
		this.promotion=c;
	}
	
	public Point[] getPoints() {
		return points.clone();
	}
	
	public char getPromotion() {
		return promotion;
	}
	
	public boolean getPlayer() {
		return String.valueOf(promotion).equals(String.valueOf(promotion).toUpperCase());
	}
}
