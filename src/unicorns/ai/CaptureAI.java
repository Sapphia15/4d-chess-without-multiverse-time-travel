package unicorns.ai;

import java.util.ArrayList;

import gameutil.math.geom.Point;
import unicorns.Board;
import unicorns.Main;

public class CaptureAI extends AI{
	
	@Override
	public Point[] getMove(ArrayList<Point[]> legalMoves,Board b) {
		int highestScore=0;
		Point[] bestMove=legalMoves.get(Main.rand.nextInt(legalMoves.size()));
		for (Point[] p:legalMoves) {
			b.makeMove(p);
			int score=0;
			switch (String.valueOf(b.lastPieceTaken()).toUpperCase().charAt(0)) {
				case 'Q':
					score+=15;
				break;
				case 'R':
					score+=5;
				break;
				case 'N':
					score+=4;
				break;
				case 'B':
					score+=4;
				break;
				case 'U':
					score+=3;
				break;
				case 'D':
					score+=2;
				break;
				case 'P':
					score+=1;
				break;
			}
			if (b.playerInCheckMate(white)) {
				score+=1000000;
			}
			if (score>highestScore) {
				highestScore=score;
				bestMove=p;
			}
			b.undo();
		}
		return bestMove;
	}

}
