package unicorns;

import java.util.concurrent.ConcurrentHashMap;

import gameutil.math.geom.Orthotope;
import gameutil.math.geom.Point;
import gameutil.math.geom.g2D.PointR2;

public class Board7D {
	private ConcurrentHashMap<PointR2,Board> boards;
	private ConcurrentHashMap<Integer,Orthotope> orthotopes;
	//polytope of blended orthotopes to check if a point is in a board area
	
	public Board7D() {
		boards=new ConcurrentHashMap<>();
		orthotopes=new ConcurrentHashMap<>();
		
		//turn 0
		Board start=new Board();
		start.setUp();
		boards.put(new PointR2(0,.5), start.clone());
		boards.put(new PointR2(0,1), start.clone());
		orthotopes.put(0,makeTimelineOrthotope(0,.5,1));
	}
	
	public Orthotope makeTimelineOrthotope(int timeline,double firstMove,double lastMove) {
		return new Orthotope(new Point(new double[] {4,4,4,4,timeline-.4999999,lastMove}),new Point(new double[] {0,0,0,0,timeline+.4999999,firstMove}));
	}
	
	public void makeMove(Point[] move) {
		boolean isTimeTravelMove=false;
		double timeline=move[0].tuple.i(4);
		double turn=move[0].tuple.i(5);
		
		for (Point p : move) {
			if (p.tuple.i(4)!=timeline) {
				isTimeTravelMove=true;
			}
		}
		if (!isTimeTravelMove) {
			Point[] boardMove=new Point[move.length];
			int i=0;
			for (Point p:move) {
				boardMove[i]=new Point(new double[] {p.tuple.a()[0],p.tuple.a()[1],p.tuple.a()[2],p.tuple.a()[3]});
			}
			Board b= boards.get(new PointR2(timeline,turn)).clone();
			b.makeMove(boardMove);
			boards.put(new PointR2(timeline,turn+.5),b);
		} else {
			//TODO do time travel things...
		}
	}
}