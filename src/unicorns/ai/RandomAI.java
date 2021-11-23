package unicorns.ai;

import java.util.ArrayList;

import gameutil.math.geom.Point;
import unicorns.Board;
import unicorns.Main;

public class RandomAI extends AI{

	@Override
	public Point[] getMove(ArrayList<Point[]> legalMoves,Board b) {
		// TODO Auto-generated method stub
		return legalMoves.get(Main.rand.nextInt(legalMoves.size()));
	}
	

}
