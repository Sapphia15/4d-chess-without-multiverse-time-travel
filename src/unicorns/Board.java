package unicorns;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import gameutil.math.geom.*;
import gameutil.text.Console;


public class Board {
	public static final Hypercube tess=new Hypercube(new Point(new Tuple(new  double[] {0,0,0,0})), 3);
	CopyOnWriteArrayList<Piece> pieces=new CopyOnWriteArrayList<>();
	Point[] legalMoves=new Point[0];
	Point lastMoveEnd=null;
	Point lastMoveStart=null;
	Piece selected=null;
	Board lastState=null;
	Point ghost=null;
	Piece ghostPawn=null;
	char lastPieceMoved='x';
	
	public Board() {
		
	}
	
	public void setUp() {
		//setUpPieces
		for (int i=0; i<4; i++) {
			for (int k=0;k<4;k++) {
				pieces.add(new Piece(new Point(new Tuple(new double[] {i,0,k,1})),'P'));
				pieces.add(new Piece(new Point(new Tuple(new double[] {i,1,k,0})),'P'));
				
				pieces.add(new Piece(new Point(new Tuple(new double[] {i,3,k,2})),'p'));
				pieces.add(new Piece(new Point(new Tuple(new double[] {i,2,k,3})),'p'));
			}
		}
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,1,0,1})),'P'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,1,1,1})),'P'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {2,1,1,1})),'P'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,1,2,1})),'P'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {2,1,2,1})),'P'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,1,3,1})),'P'));
		
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,2,0,2})),'p'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,2,1,2})),'p'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {2,2,1,2})),'p'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,2,2,2})),'p'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {2,2,2,2})),'p'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,2,3,2})),'p'));
		
		pieces.add(new Piece(new Point(new Tuple(new double[] {0,0,0,0})),'R'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,0,0,0})),'B'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {2,0,0,0})),'N'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {3,0,0,0})),'R'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {0,0,1,0})),'D'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,0,1,0})),'Q'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {2,0,1,0})),'K'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {3,0,1,0})),'D'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {0,0,2,0})),'U'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,0,2,0})),'D'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {2,0,2,0})),'D'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {3,0,2,0})),'U'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {0,0,3,0})),'R'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,0,3,0})),'B'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {2,0,3,0})),'N'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {3,0,3,0})),'R'));
		
		pieces.add(new Piece(new Point(new Tuple(new double[] {0,3,0,3})),'r'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,3,0,3})),'b'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {2,3,0,3})),'n'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {3,3,0,3})),'r'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {0,3,1,3})),'d'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,3,1,3})),'q'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {2,3,1,3})),'k'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {3,3,1,3})),'d'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {0,3,2,3})),'u'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,3,2,3})),'d'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {2,3,2,3})),'d'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {3,3,2,3})),'u'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {0,3,3,3})),'r'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {1,3,3,3})),'b'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {2,3,3,3})),'n'));
		pieces.add(new Piece(new Point(new Tuple(new double[] {3,3,3,3})),'r'));
		
		lastState=this.clone();
	}
	
	public Piece pieceAt(Point p) {
		for (Piece piece : pieces) {
			if (piece.pos.equals(p)) {
				return piece;
			}
		}
		return null;
	}
	
	public CopyOnWriteArrayList<Piece> getPieces(){
		return this.pieces;
	}
	
	public static boolean contains(Point f) {
		return tess.contains(f);
	}
	
	public static String pointToNotation(Point p) {
		String str="";
		switch ((int) p.tuple.i(0)){
			case 0:
				str="a";
			break;
			case 1:
				str="b";
			break;
			case 2:
				str="c";
			break;
			case 3:
				str="d";
			break;
		}
		str=str+String.valueOf((int)p.tuple.i(1)+1);
		switch ((int) p.tuple.i(2)){
			case 0:
				str=str+"α";
			break;
			case 1:
				str=str+"β";
			break;
			case 2:
				str=str+"γ";
			break;
			case 3:
				str=str+"δ";
			break;
		}
		str=str+String.valueOf((int)p.tuple.i(3)+1);
		return str;
	}
	
	public void selectPiece(Piece p) {
		selected=p;
		this.legalMoves=p.getPotentialMoves(p.type,this);
	}
	
	public void deselectPiece() {
		this.legalMoves=new Point[0];
		selected=null;
	}
	
	public Point[] moveableSpaces() {
		return this.legalMoves;
	}
	
	public boolean spaceMoveable(Point p) {
		for (Point legalMove:legalMoves) {
			if (legalMove.equals(p)) {
				return true;
			}
		}
		return false;
	}
	
	public Piece getSelectedPiece() {
		return selected;
	}
	
	public void move(Point p) {
		
		Board oldState=lastState.clone();
		lastState=this.clone();
		lastState.lastState=oldState.clone();//this will allow for any number of undos during analysis
		lastMoveStart=selected.getPos();
		ghost=null;
		ghostPawn=null;
		lastMoveEnd=p;
		selected.pos=p;
		lastPieceMoved=selected.getType();
		selected.firstMove=false;
	}
	
	public void secondPawnMove(Point p) {
		lastMoveEnd=p;
		ghost=selected.pos;
		ghostPawn=selected;
		selected.pos=p;
	}
	
	public void undo() {
		setState(lastState);
	}
	
	public void setState(Board b) {
		this.legalMoves=b.legalMoves.clone();
		if (b.lastMoveEnd!=null) {
			this.lastMoveEnd=b.lastMoveEnd.clone();
		} else {
			this.lastMoveEnd=null;
		}
		if (b.lastMoveStart!=null) {
			this.lastMoveStart=b.lastMoveStart.clone();
		} else {
			this.lastMoveStart=null;
		}
		this.ghost=b.ghost;
		
		this.lastPieceMoved=b.lastPieceMoved;
		this.pieces.clear();
		for (Piece p : b.pieces) {
			Piece clone=p.clone();
			if (b.ghostPawn!=null&&p.equals(b.ghostPawn)) {
				this.ghostPawn=clone;
			}
			this.pieces.add(clone);
		}
		if (b.lastState!=null) {
			this.lastState=b.lastState.clone();
		}
	}
	
	public Board clone() {
		Board cloneBoard=new Board();
		if (this.lastMoveEnd!=null) {
			cloneBoard.lastMoveEnd=this.lastMoveEnd.clone();
		}
		if (this.lastMoveStart!=null) {
			cloneBoard.lastMoveStart=this.lastMoveStart.clone();
		}
		for (Piece p : pieces) {
			Piece clone=p.clone();
			if (this.ghostPawn!=null&&p.equals(this.ghostPawn)) {
				cloneBoard.ghostPawn=clone;
			}
			cloneBoard.pieces.add(clone);
		}
		
		if (this.ghost!=null) {
			cloneBoard.ghost=this.ghost.clone();
		}
		
		cloneBoard.lastPieceMoved=this.lastPieceMoved;
		if(lastState!=null) {
			cloneBoard.lastState=lastState.clone();
		}
		
		return cloneBoard;
	}
	
	public Piece getWhiteKing() {
		for (Piece p : pieces) {
			if (p.type=='K') {
				return p;
			}
		}
		return null;
	}
	
	public Piece getBlackKing() {
		for (Piece p : pieces) {
			if (p.type=='k') {
				return p;
			}
		}
		return null;
	}
	
	public Point getGhost() {
		return ghost;
	}
	
	public void captureGhost() {
		if (ghostPawn!=null) {
			pieces.remove(ghostPawn);
		}
	}
	
	public boolean playerInCheck(boolean white) {
		Point king=null;
		if (white) {
			king=getWhiteKing().getPos();
		} else {
			king=getBlackKing().getPos();
		}
		king.printVals("King");
		for (Piece p:pieces) {
			if (p.white!=white) {
				for (Point move:p.getPotentialMoves(p.getType(),this)) {
					move.printVals("Move");
					if (move.equals(king)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean playerHasLegalMove(boolean white) {
		
		Board testBoard=this.clone();
		//testBoard.lastState=this.clone();
		for (Piece p:this.getPieces()) {
			if (p.white==white) {
				Point start=p.pos.clone();
				boolean fm=p.firstMove;
				boolean pawn=String.valueOf(p.type).toUpperCase().equals("P");
				for (Point move:p.getPotentialMoves(p.getType(),this)) {
					
					//Main.err.println("Testing move: "+Board.pointToNotation(start)+" "+Board.pointToNotation(move));
					Piece target=testBoard.pieceAt(move);
					boolean moveMade=testBoard.makeMove(start,move);
					Piece testPiece=testBoard.pieceAt(move).clone();
					//Main.err.println("In check on test board: "+testBoard.playerInCheck(white));
					if (!testBoard.playerInCheck(white)) {
						//Main.err.println("Legal Move found: "+Board.pointToNotation(start)+" "+Board.pointToNotation(move));
						return true;
					}
					if (moveMade) {
						testBoard.undo();
					} else  {
						Main.err.println("Tried to make illegal move on test board: "+Board.pointToNotation(start)+" "+Board.pointToNotation(move));
					}
					if (pawn&&move.distance(start)==1&&target==null&&fm) {
						for (Point secondMove : testPiece.getPotentialMoves(testPiece.getType(),testBoard)) {
							
							if (pieceAt(secondMove)==null) {
								
								moveMade=testBoard.makeMove(start,secondMove,move);
								
								if (!testBoard.playerInCheck(white)) {
									//Main.err.println("Legal Move found: "+Board.pointToNotation(start)+" ("+Board.pointToNotation(move)+") "+Board.pointToNotation(secondMove));
									return true;
									
								}
								if (moveMade) {
									testBoard.undo();
								} else  {
									Main.err.println("Tried to make illegal move on test board: "+Board.pointToNotation(start)+" ("+Board.pointToNotation(move)+") "+Board.pointToNotation(secondMove));
								}
							}
							
						}
					}
					
				}
			}
		}
		return false;
	}
	
	public ArrayList<Point[]> getAllLegalMoves(boolean white){
		ArrayList<Point[]> legalMoves=new ArrayList<>();
		Board testBoard=this.clone();
		//testBoard.lastState=this.clone();
		for (Piece p:this.getPieces()) {
			if (p.white==white) {
				Point start=p.pos.clone();
				boolean fm=p.firstMove;
				boolean pawn=String.valueOf(p.type).toUpperCase().equals("P");
				for (Point move:p.getPotentialMoves(p.getType(),this)) {
					
					//Main.err.println("Testing move: "+Board.pointToNotation(start)+" "+Board.pointToNotation(move));
					Piece target=testBoard.pieceAt(move);
					boolean moveMade=testBoard.makeMove(start,move);
					Piece testPiece=testBoard.pieceAt(move).clone();
					//Main.err.println("In check on test board: "+testBoard.playerInCheck(white));
					if (!testBoard.playerInCheck(white)) {
						//Main.err.println("Legal Move found: "+Board.pointToNotation(start)+" "+Board.pointToNotation(move));
						legalMoves.add(new Point[] {start,move});
					}
					if (moveMade) {
						testBoard.undo();
					} else  {
						Main.err.println("Tried to make illegal move on test board: "+Board.pointToNotation(start)+" "+Board.pointToNotation(move));
					}
					if (pawn&&move.distance(start)==1&&target==null&&fm) {
						for (Point secondMove : testPiece.getPotentialMoves(testPiece.getType(),testBoard)) {
							
							if (pieceAt(secondMove)==null) {
								
								moveMade=testBoard.makeMove(start,secondMove,move);
								
								if (!testBoard.playerInCheck(white)) {
									//Main.err.println("Legal Move found: "+Board.pointToNotation(start)+" ("+Board.pointToNotation(move)+") "+Board.pointToNotation(secondMove));
									legalMoves.add(new Point[] {start,secondMove,move});
									
								}
								if (moveMade) {
									testBoard.undo();
								} else  {
									Main.err.println("Tried to make illegal move on test board: "+Board.pointToNotation(start)+" ("+Board.pointToNotation(move)+") "+Board.pointToNotation(secondMove));
								}
							}
							
						}
					}
					
				}
			}
		}
		return legalMoves;
	}
	
	public boolean playerInCheckMate(boolean white) {
		return !playerHasLegalMove(white)&&playerInCheck(white);
	}
	
	public Point lastMoveStart() {
		return lastMoveStart;
	}
	
	public Point lastMoveEnd() {
		return lastMoveEnd;
	}
	
	public char lastPieceTypeMoved() {
		return lastPieceMoved;
	}
	
	public Piece getGhostPiece() {
		return ghostPawn;
	}
	
	public boolean moveIsLegal(Point start, Point end) {
		Board testBoard=this.clone();
		testBoard.makeMove(start, end);
		return testBoard.playerInCheck(testBoard.pieceAt(end).white);
	}
	
	public boolean makeMove(Point start,Point end,Point intermed,char promotion) {
		Piece p=this.pieceAt(start);
		Piece x=this.pieceAt(end);
		selectPiece(p);
		boolean white=p.white;
		//make the move... need to fix/finish
		if (x!=null) {
			if (white==x.isWhite()) {
				deselectPiece();
				return false;
			} else if (this.spaceMoveable(end)){
				//capture
				if (intermed!=null&&String.valueOf(p.getType()).toUpperCase().equals("P")) {
					deselectPiece();
					return false;
				}
				selectPiece(p);
				move(end);
				pieces.remove(x);
				deselectPiece();
				if (String.valueOf(p.getType()).toUpperCase().equals("P")&&((end.tuple.i(1)==3&&end.tuple.i(3)==3&&white)||(end.tuple.i(1)==0&&end.tuple.i(3)==0&&!white))) {
					//just promote
					pieces.remove(p);
					Piece newPiece=new Piece(p.pos,promotion);
					pieces.add(newPiece);
				} 
				return true;
			} else {
				deselectPiece();
				return false;
			}
		} else if(intermed!=null&&String.valueOf(p.getType()).toUpperCase().equals("P")&&this.spaceMoveable(intermed)&&this.pieceAt(end)==null) {
			selectPiece(p);
			move(end);
			deselectPiece();
			ghost=intermed;
			ghostPawn=p;
			return true;
		} else if (spaceMoveable(end)) {
			
			if (String.valueOf(p.getType()).toUpperCase().equals("P")){
				if(ghost!=null&&end.equals(getGhost())&&end.distance(selected.getPos())>1) {
					//capture="x";
					selectPiece(p);
					Piece ghostPawn=this.ghostPawn;
					move(end);
					getPieces().remove(ghostPawn);
					deselectPiece();
					return true;
				} else {
					selectPiece(p);
					move(end);
					deselectPiece();
					if ((end.tuple.i(1)==3&&end.tuple.i(3)==3&&white)||(end.tuple.i(1)==0&&end.tuple.i(3)==0&&!white)) {
						//just promote
						pieces.remove(p);
						Piece newPiece=new Piece(p.pos,promotion);
						pieces.add(newPiece);
					}
					return true;
				}
			} else {
				selectPiece(p);
				move(end);
				deselectPiece();
				return true;
			}
		} else {
			deselectPiece();
			return false;
		}
	}
	
	public boolean makeMove(Point start,Point end, Point intermed) {
		char q = 'q';
		if (pieceAt(start).white) {
			q='Q';
		}
		return makeMove(start,end,intermed,q);
	}
	
	public boolean makeMove(Point start,Point end) {
		return makeMove(start,end,null);
	}
	
	public boolean makeMove(Point[] move) {
		if (move.length==2) {
			return makeMove(move[0],move[1]);
		} else {
			//the only other case is a move length of three (a pawn move that moves two spaces)
			return makeMove(move[0],move[1],move[2]);
		}
	}
}
