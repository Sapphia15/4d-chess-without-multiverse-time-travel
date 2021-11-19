package unicorns;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;

import gameutil.math.geom.*;
import gameutil.text.Console;

public class Piece {
	Point pos;
	char type;
	boolean white;
	boolean firstMove=true;
	
	public Piece(Point pos,char type) {
		this.pos=pos;
		this.type=type;
		//uppercase char makes a white piece
		this.white=String.valueOf(type).toUpperCase().equals(String.valueOf(type));
	}
	
	public Point[] getPotentialMoves(char type,Board bo) {
		ArrayList<Point> moves=new ArrayList<Point>();
		Vector vp=new Vector(pos);
		Vector fx=new Vector(new double[] {1,0,0,0});//maybe change these to the unit vector function later
		Vector fy=new Vector(new double[] {0,1,0,0});
		Vector fz=new Vector(new double[] {0,0,1,0});
		Vector fw=new Vector(new double[] {0,0,0,1});
		
		switch (String.valueOf(type).toUpperCase()){
			case "P":
				//Console.s.println("pawn");
				int mult=-1;
				if (white) {
					mult=1;
				}
				Point py=new Point(vp.$A$(fy.$X$(mult)));
				//py.tuple.printVals();
				Point pw=new Point(vp.$A$(fw.$X$(mult)));
				if (Board.contains(py)&&bo.pieceAt(py)==null) {
					moves.add(py);
				}
				if (Board.contains(pw)&&bo.pieceAt(pw)==null) {
					moves.add(pw);
				}
				
				//diagonal captures
				for (int s=0;s<2;s++) {
					Vector[] vecs=new Vector[4];
					vecs[0]=fy.$X$(mult).$A$(fx.$X$(Math.pow(-1, s)));
					vecs[1]=fy.$X$(mult).$A$(fz.$X$(Math.pow(-1, s)));
					vecs[2]=fw.$X$(mult).$A$(fx.$X$(Math.pow(-1, s)));
					vecs[3]=fw.$X$(mult).$A$(fz.$X$(Math.pow(-1, s)));
					for (Vector v : vecs) {
						Point move=new Point(vp.$A$(v));
						Piece p=bo.pieceAt(move);
						if (p!=null &&p.white!=this.white) {
							moves.add(move);
						} else if (bo.ghost!=null) {
							if (bo.ghost.equals(move)) {
								moves.add(move);
							}
						}
					}
				}
				//need to add diagonal captures
			break;
			case "D":
				//this moveset seems to work!
				for (int j=0; j<8;j++) {
					Vector out=vp.clone();
					Vector outN=vp.clone();
					Vector dir=new Vector(new double[] {-1,Math.pow(-1,j),Math.pow(-1,(j-(j%2))/2),Math.pow(-1,(j-(j%4))/4)});
					boolean contP=true;
					boolean contN=true;
					for (int i=0;i<3;i++) {
						out=out.$A$(dir);
						outN=outN.$S$(dir);
						Point P=new Point(out);
						Point N=new Point(outN);
						Piece pieceP=bo.pieceAt(P);
						Piece pieceN=bo.pieceAt(N);
						if (pieceP!=null || !Board.contains(P)){
							if (pieceP!=null) {
								if (pieceP.white!=this.white&&contP) {
									moves.add(P);
								}
							}
							contP=false;
						}
						if (pieceN!=null || !Board.contains(N)) {
							if (pieceN!=null) {
								if (pieceN.white!=this.white&&contN) {
									moves.add(N);
								}
							}
							contN=false;
						}
						if (!(contP || contN)) {
							break;
						} else {
							if (contP) {
								moves.add(P);
							}
							if (contN) {
								moves.add(N);
							}
						}
					}
				}
			break;
			case "B":
				for (int n=0;n<6;n++) {
					for (int s=1;s<3;s++) {
						Vector dir=Vector.getUnitVector(n%4+1).$A$(Vector.getUnitVector((n+1)%4+1+(n-n%4)/4).$X$(Math.pow(-1, s)));
						
						Vector out=vp.clone();
						Vector outN=vp.clone();
						boolean contP=true;
						boolean contN=true;
						for (int i=0; i<3;i++) {
							out=out.$A$(dir);
							outN=outN.$S$(dir);
							Point P=new Point(out);
							Point N=new Point(outN);
							Piece pieceP=bo.pieceAt(P);
							Piece pieceN=bo.pieceAt(N);
							if (pieceP!=null || !Board.contains(P)){
								if (pieceP!=null) {
									if (pieceP.white!=this.white&&contP) {
										moves.add(P);
									}
								}
								contP=false;
							}
							if (pieceN!=null || !Board.contains(N)) {
								if (pieceN!=null) {
									if (pieceN.white!=this.white&&contN) {
										moves.add(N);
									}
								}
								contN=false;
							}
							if (!(contP || contN)) {
								break;
							} else {
								if (contP) {
									moves.add(P);
								}
								if (contN) {
									moves.add(N);
								}
							}
						}
					}
				}
			break;
			case "U":
				//probably not optimal but should work
				for (int n=0; n<4;n++) {
					for (int s=0; s<2;s++) {
						for (int s2=0;s2<2;s2++) {
							Vector out=vp.clone();
							Vector outN=vp.clone();
							boolean contP=true;
							boolean contN=true;
							Vector dir=Vector.getUnitVector(n%4+1).$A$(Vector.getUnitVector((n+1)%4+1).$X$(Math.pow(-1, s)).$A$(Vector.getUnitVector((n+2)%4+1).$X$(Math.pow(-1, s2))));
							for (int i=0; i<3;i++) {
								out=out.$A$(dir);
								outN=outN.$S$(dir);
								Point P=new Point(out);
								Point N=new Point(outN);
								Piece pieceP=bo.pieceAt(P);
								Piece pieceN=bo.pieceAt(N);
								if (pieceP!=null || !Board.contains(P)){
									if (pieceP!=null) {
										if (pieceP.white!=this.white&&contP) {
											moves.add(P);
										}
									}
									contP=false;
								}
								if (pieceN!=null || !Board.contains(N)) {
									if (pieceN!=null) {
										if (pieceN.white!=this.white&&contN) {
											moves.add(N);
										}
									}
									contN=false;
								}
								if (!(contP || contN)) {
									break;
								} else {
									if (contP) {
										moves.add(P);
									}
									if (contN) {
										moves.add(N);
									}
								}
							}
						}
					}
				}
			break;
			case "Q":
				for (Point move : getPotentialMoves('D',bo)) {
					moves.add(move);
				}
				for (Point move : getPotentialMoves('B',bo)) {
					moves.add(move);
				}
				for (Point move : getPotentialMoves('U',bo)) {
					moves.add(move);
				}
				for (Point move : getPotentialMoves('R',bo)) {
					moves.add(move);
				}
			break;
			case "R":
				for (int k=1;k<5;k++) {
					Vector dir=Vector.getUnitVector(k);
					Vector out=vp.clone();
					Vector outN=vp.clone();
					boolean contP=true;
					boolean contN=true;
					for (int i=0; i<3;i++) {
						out=out.$A$(dir);
						outN=outN.$S$(dir);
						Point P=new Point(out);
						Point N=new Point(outN);
						Piece pieceP=bo.pieceAt(P);
						Piece pieceN=bo.pieceAt(N);
						if (pieceP!=null || !Board.contains(P)){
							if (pieceP!=null) {
								if (pieceP.white!=this.white&&contP) {
									moves.add(P);
								}
							}
							contP=false;
						}
						if (pieceN!=null || !Board.contains(N)) {
							if (pieceN!=null) {
								if (pieceN.white!=this.white&&contN) {
									moves.add(N);
								}
							}
							contN=false;
						}
						if (!(contP || contN)) {
							break;
						} else {
							if (contP) {
								moves.add(P);
							}
							if (contN) {
								moves.add(N);
							}
						}
					}
				}
			break;
			case "N":
				//TODO knight moves
				for (int n=0;n<6;n++) {
					for (int s=0;s<2;s++) {
						for (int s2=0;s2<2;s2++) {
							Vector a=Vector.getUnitVector(n%4+1).$X$(Math.pow(-1, s2));
							Vector b=Vector.getUnitVector((n+1)%4+1+(n-n%4)/4).$X$(Math.pow(-1, s));
							Vector out=vp.$A$(a.$X$(2)).$A$(b);
							Vector outN=vp.$A$(b.$X$(2)).$A$(a);
							Point P=new Point(out);
							Point N=new Point(outN);
							Piece pieceP=bo.pieceAt(P);
							Piece pieceN=bo.pieceAt(N);
							if (pieceP!=null || !Board.contains(P)){
								if (pieceP!=null) {
									if (pieceP.white!=this.white) {
										moves.add(P);
									}
								}
							} else {
								moves.add(P);
							}
							if (pieceN!=null || !Board.contains(N)) {
								if (pieceN!=null) {
									if (pieceN.white!=this.white) {
										moves.add(N);
									}
								}
							} else {
								moves.add(N);
							}
						}
					}
				}
			break;
			case "K":
				//TODO king moves
				for (Point move : getPotentialMoves('D',bo)) {
					if (new Vector(move).$S$(vp).magnitude()<=2) {
						moves.add(move);
					}
				}
				for (Point move : getPotentialMoves('B',bo)) {
					if (new Vector(move).$S$(vp).magnitude()<=Math.sqrt(2)) {
						moves.add(move);
					}
				}
				for (Point move : getPotentialMoves('U',bo)) {
					if (new Vector(move).$S$(vp).magnitude()<=Math.sqrt(3)) {
						moves.add(move);
					}
				}
				for (Point move : getPotentialMoves('R',bo)) {
					if (new Vector(move).$S$(vp).magnitude()<=1) {
						moves.add(move);
					}
				}
			break;
			
		}
		
		Point[] moveArray=new Point[moves.size()];
		for (int i=0;i<moves.size();i++) {
			moveArray[i]=moves.get(i);
		}
		return moveArray;
	}
	
	public Image getImage() {
		return Piece.getImage(type);
	}
	
	public static Image getImage(char type) {
		switch (type) {
			case 'p':
				return Assets.PAWN_B;
			case 'r':
				return Assets.ROOK_B;
			case 'b':
				return Assets.BISHOP_B;
			case 'u':
				return Assets.UNICORN_B;
			case 'd':
				return Assets.DRAGON_B;
			case 'q':
				return Assets.QUEEN_B;
			case 'k':
				return Assets.KING_B;
			case 'n':
				return Assets.KNIGHT_B;
			case 'P':
				return Assets.PAWN_W;
			case 'R':
				return Assets.ROOK_W;
			case 'B':
				return Assets.BISHOP_W;
			case 'U':
				return Assets.UNICORN_W;
			case 'D':
				return Assets.DRAGON_W;
			case 'Q':
				return Assets.QUEEN_W;
			case 'K':
				return Assets.KING_W;
			case 'N':
				return Assets.KNIGHT_W;
			case 'g':
				return Assets.GHOST_B;
			case 'G':
				return Assets.GHOST_W;
		}
		return null;
	}
	
	public int getX() {
		return (int)this.pos.tuple.i(0);
	}
	
	public int getY() {
		return (int)this.pos.tuple.i(1);
	}
	
	public int getZ() {
		return (int)this.pos.tuple.i(2);
	}
	
	public int getW() {
		return (int)this.pos.tuple.i(3);
	}
	
	public boolean isWhite() {
		return white;
	}
	
	public Point getPos() {
		return pos.clone();
	}
	
	public char getType() {
		return type;
	}
	
	public boolean isFirstMove() {
		return firstMove;
	}
	
	public Piece clone() {
		Point pos=null;
		if (this.pos!=null) {
			pos=this.pos.clone();
		}
		Piece clone=new Piece(pos,type);
		clone.firstMove=this.firstMove;
		return clone;
	}
	
	public void setPos(Point pos) {
		this.pos=pos;
	}
}
