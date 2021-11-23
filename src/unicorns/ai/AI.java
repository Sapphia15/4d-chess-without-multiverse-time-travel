package unicorns.ai;

import java.util.ArrayList;

import gameutil.math.geom.Point;
import unicorns.Board;

public abstract class AI {
	
	boolean white=false;
	
	public AI() {
		
	}
	
	public abstract Point[] getMove(ArrayList<Point[]> legalMoves,Board b);
	
	public void setColor(boolean white) {
		this.white=white;
	}
}
