package unicorns.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.Rectangle;

import gameutil.math.geom.Point;
import gameutil.math.geom.Tuple;
import gameutil.text.Console;
import graphics.screen.Screen;
import unicorns.Assets;
import unicorns.Board;
import unicorns.Main;
import unicorns.Panel;
import unicorns.Piece;

public class Game extends Screen{

	Panel observer;
	Hashtable<Rectangle,Point> rects=new Hashtable<>();
	Rectangle board=new Rectangle(0,0,1,1);
	boolean wPersp=true;
	int oldWidth=0;
	int oldHeight=0;
	boolean oldPersp=true;
	String space="a0α0";
	boolean whiteTurn=true;
	boolean checked=false;
	boolean ai=false;
	Random rand=new Random();
	boolean aiColor=(1==rand.nextInt(2));
	static enum STATE {move,submit,pawnmove,detect,illegal,whiteWins,blackWins,draw,promote,detectPawn};
	STATE state=STATE.move;
	Point promoteSquare=null;
	String capture="";
	
	
	Hashtable<Rectangle,Piece> promotablesW=new Hashtable<>();
	Hashtable<Rectangle,Piece> promotablesB=new Hashtable<>();
	
	public Game(Panel observer) {
		this.observer=observer;
		if (ai) {
			wPersp=!aiColor;
		}
		
	}
	
	@Override
	public void paint(Graphics g) {
		if (state==STATE.illegal||(state==STATE.move&&checked)) {
			g.setColor(Color.red);
			g.fillRect(0, 0, observer.getWidth(), observer.getHeight());
			g.setColor(Color.black);
		}
		// TODO Auto-generated method stub
		int num=384;
		int sq=22;
		int gap=8;
		Font currentFont = g.getFont();
		
		
		
		if (observer.getWidth()>768 && observer.getHeight()>768) {
			num=768;
			sq=44;
			gap=16;
		}
		int sq4=4*sq;
		int gap3d4=3*gap/4;
		int offX=(int)Math.floor(observer.getWidth()/2d)-num/2;
		int offY=(int)Math.floor(observer.getHeight()/2d)-num/2;
		if (wPersp) {
			g.drawImage(Assets.BOARD, offX, offY, offX+num, offY+num, 0, 192, 192, 0, observer);
		} else {
			g.drawImage(Assets.BOARD, offX,offY,num, num, null);
		}
		Font newFont = currentFont.deriveFont((float)sq);
		g.setFont(newFont);
		g.drawString(space,10,sq);
		
		Piece selected=Main.b.getSelectedPiece();
		if (selected!=null) {
			int x=selected.getX();
			int y=selected.getY();
			int z=selected.getZ();
			int w=selected.getW();
			if (wPersp) {
				y=3-y;
				w=3-w;
			} else {
				x=3-x;
				z=3-z;
			}
			g.setColor(new Color(255,255,100,100));
			g.fillRect(offX+x*sq+z*(gap3d4+sq4)+gap,offY+y*sq+gap+w*(gap3d4+sq4),sq-gap/4,sq-gap/4);
		}
		
		Point lastMoveStart=Main.b.lastMoveStart();
		
		if (lastMoveStart!=null) {
			
			int x=(int)lastMoveStart.tuple.i(0);
			int y=(int)lastMoveStart.tuple.i(1);
			int z=(int)lastMoveStart.tuple.i(2);
			int w=(int)lastMoveStart.tuple.i(3);
			g.setColor(new Color(255,255,150));
			
			if (wPersp) {
				y=3-y;
				w=3-w;
			} else {
				x=3-x;
				z=3-z;
			}
			
			g.fillRect(offX+x*sq+z*(gap3d4+sq4)+gap,offY+y*sq+gap+w*(gap3d4+sq4),sq-gap/4,sq-gap/4);
		}
		
		Point lastMoveEnd=Main.b.lastMoveEnd();
		if (lastMoveEnd!=null) {
			
			int x=(int)lastMoveEnd.tuple.i(0);
			int y=(int)lastMoveEnd.tuple.i(1);
			int z=(int)lastMoveEnd.tuple.i(2);
			int w=(int)lastMoveEnd.tuple.i(3);
			g.setColor(new Color(255,255,150));
			
			if (wPersp) {
				y=3-y;
				w=3-w;
			} else {
				x=3-x;
				z=3-z;
			}
			
			g.fillRect(offX+x*sq+z*(gap3d4+sq4)+gap,offY+y*sq+gap+w*(gap3d4+sq4),sq-gap/4,sq-gap/4);
		}
		
		for (Piece p : Main.b.getPieces()) {
			int x=p.getX();
			int y=p.getY();
			int z=p.getZ();
			int w=p.getW();
			if (wPersp) {
				y=3-y;
				w=3-w;
			} else {
				x=3-x;
				z=3-z;
			}
			g.drawImage(p.getImage(),offX+x*sq+z*(gap3d4+sq4)+gap,offY+y*sq+gap+w*(gap3d4+sq4),sq-gap/4,sq-gap/4,null);
		}
		
		if (Main.b.getGhost()!=null) {
			Image ghost=null;
			if (Main.b.getGhostPiece().isWhite()) {
				ghost=Assets.GHOST_W;
			} else {
				ghost=Assets.GHOST_B;
			}
			Point p=Main.b.getGhost();
			int x=(int)p.tuple.i(0);
			int y=(int)p.tuple.i(1);
			int z=(int)p.tuple.i(2);
			int w=(int)p.tuple.i(3);
			if (Main.b.pieceAt(p)==null) {
				g.setColor(new Color(0,255,0,100));
			} else if (state==STATE.move) {
				g.setColor(new Color(255,0,0,100));	
			} else {
				g.setColor(new Color(0,0,0,0));
			}
			
			if (wPersp) {
				y=3-y;
				w=3-w;
			} else {
				x=3-x;
				z=3-z;
			}
			
			g.drawImage(ghost,offX+x*sq+z*(gap3d4+sq4)+gap,offY+y*sq+gap+w*(gap3d4+sq4),sq-gap/4,sq-gap/4,null);

			
		}
		
		//Console.s.println(Main.b.moveableSpaces().length);
		for (Point p : Main.b.moveableSpaces()) {
			
			int x=(int)p.tuple.i(0);
			int y=(int)p.tuple.i(1);
			int z=(int)p.tuple.i(2);
			int w=(int)p.tuple.i(3);
			if (Main.b.pieceAt(p)==null) {
				g.setColor(new Color(0,255,0,100));
			} else if (state==STATE.move) {
				g.setColor(new Color(255,0,0,100));	
			} else {
				g.setColor(new Color(0,0,0,0));
			}
			
			if (wPersp) {
				y=3-y;
				w=3-w;
			} else {
				x=3-x;
				z=3-z;
			}
			
			g.fillRect(offX+x*sq+z*(gap3d4+sq4)+gap,offY+y*sq+gap+w*(gap3d4+sq4),sq-gap/4,sq-gap/4);
		}
		
		if (state==STATE.whiteWins) {
			g.setColor(Color.white);
			g.fillRoundRect(offX+num/2-g.getFontMetrics().stringWidth("White Wins!")/2-gap,offY+num/2, g.getFontMetrics().stringWidth("White Wins!")+gap*2, sq+gap, 20,20);
			g.setColor(Color.black);
			g.drawString("White Wins!",offX+num/2-g.getFontMetrics().stringWidth("White Wins!")/2,offY+num/2+sq);
			
		} else if (state==STATE.blackWins) {
			g.setColor(Color.black);
			g.fillRoundRect(offX+num/2-g.getFontMetrics().stringWidth("Black Wins!")/2-gap,offY+num/2, g.getFontMetrics().stringWidth("Black Wins!")+gap*2, sq+gap, 20,20);
			g.setColor(Color.white);
			g.drawString("Black Wins!",offX+num/2-g.getFontMetrics().stringWidth("Black Wins!")/2,offY+num/2+sq);
			
		} else if (state==STATE.draw){
			g.setColor(Color.gray);
			g.fillRoundRect(offX+num/2-g.getFontMetrics().stringWidth("Unicorns are amazing! (also it's a draw)")/2-gap,offY+num/2, g.getFontMetrics().stringWidth("Unicorns are amazing! (also it's a draw)")+gap*2, sq+gap, 20,20);
			g.setColor(Color.pink);
			g.drawString("Unicorns are amazing! (also it's a draw)",offX+num/2-g.getFontMetrics().stringWidth("Unicorns are amazing! (also it's a draw)")/2,offY+num/2+sq);
		} else if (state==STATE.promote) {
			if (whiteTurn) {
				for (Rectangle r:promotablesW.keySet()) {
					g.setColor(new Color(50,50,50,100));
					g.fillRoundRect(r.x,r.y,r.width,r.height,10,10);
					g.drawImage(promotablesW.get(r).getImage(), r.x, r.y, r.width,r.height, null);
				}
			} else {
				for (Rectangle r:promotablesB.keySet()) {
					g.setColor(new Color(205,205,205,100));
					g.fillRoundRect(r.x,r.y,r.width,r.height,10,10);
					g.drawImage(promotablesB.get(r).getImage(), r.x, r.y, r.width,r.height, null);
				}
			}
		}
		
	}

	@Override
	public void update() {
		if (oldWidth!=observer.getWidth()||oldHeight!=observer.getHeight()||oldPersp!=wPersp) {
			oldPersp=wPersp;
			oldWidth=observer.getWidth();
			oldHeight=observer.getHeight();
			int num=384;
			int sq=22;
			int gap=8;
			
			if (observer.getWidth()>768 && observer.getHeight()>768) {
				num=768;
				sq=44;
				gap=16;
			}
			int sq4=4*sq;
			int gap3d4=3*gap/4;
			int offX=(int)Math.floor(observer.getWidth()/2d)-num/2;
			int offY=(int)Math.floor(observer.getHeight()/2d)-num/2;
			rects.clear();
			board=new Rectangle(offX,offY,num,num);
			for (int i=0; i<4;i++) {
				for (int j=0; j<4;j++) {
					for (int k=0; k<4;k++) {
						for (int n=0; n<4;n++) {
							int x=i;
							int y=j;
							int z=k;
							int w=n;
							if (wPersp) {
								y=3-y;
								w=3-w;
							} else {
								x=3-x;
								z=3-z;
							}
							rects.put(new Rectangle(offX+x*sq+z*(gap3d4+sq4)+gap,offY+y*sq+gap+w*(gap3d4+sq4),sq-gap/4,sq-gap/4),new Point(new Tuple(new double[] {i,j,k,n})));
						}
					}
				}
			}
			promotablesW.clear();
			promotablesB.clear();
			
			promotablesW.put(new Rectangle(offX+num+sq,offY,sq,sq),new Piece(null,'Q'));
			promotablesW.put(new Rectangle(offX+num+sq,offY+sq,sq,sq),new Piece(null,'D'));
			promotablesW.put(new Rectangle(offX+num+sq,offY+sq*2,sq,sq),new Piece(null,'U'));
			promotablesW.put(new Rectangle(offX+num+sq,offY+sq*3,sq,sq),new Piece(null,'B'));
			promotablesW.put(new Rectangle(offX+num+sq,offY+sq*4,sq,sq),new Piece(null,'R'));
			promotablesW.put(new Rectangle(offX+num+sq,offY+sq*5,sq,sq),new Piece(null,'N'));
			
			promotablesB.put(new Rectangle(offX+num+sq,offY,sq,sq),new Piece(null,'q'));
			promotablesB.put(new Rectangle(offX+num+sq,offY+sq,sq,sq),new Piece(null,'d'));
			promotablesB.put(new Rectangle(offX+num+sq,offY+sq*2,sq,sq),new Piece(null,'u'));
			promotablesB.put(new Rectangle(offX+num+sq,offY+sq*3,sq,sq),new Piece(null,'b'));
			promotablesB.put(new Rectangle(offX+num+sq,offY+sq*4,sq,sq),new Piece(null,'r'));
			promotablesB.put(new Rectangle(offX+num+sq,offY+sq*5,sq,sq),new Piece(null,'n'));
		}
		if (state==STATE.detect||state==STATE.detectPawn) {
			Point king=null;
			if (whiteTurn) {
				king=Main.b.getWhiteKing().getPos();
			} else {
				king=Main.b.getBlackKing().getPos();
			}
			for (Piece p:Main.b.getPieces()) {
				if (p.isWhite()!=whiteTurn) {
					for (Point move:p.getLegalMoves(p.getType())) {
						if (move.equals(king)){
							
								state=STATE.illegal;
						}
					}
				}
			}
			if (state!=STATE.illegal) {
				if (state==STATE.detectPawn) {
					state=STATE.submit;
					submit();
				} else {
					state=STATE.submit;
				}
			} else if (ai&&whiteTurn==aiColor) {
				Main.b.undo();
				state=STATE.move;
			}
			
		} else if (state==STATE.move&&ai&&whiteTurn==aiColor) {
			Piece p=Main.b.getPieces().get(rand.nextInt(Main.b.getPieces().size()));
			if (p.isWhite()==aiColor) {
				Main.b.selectPiece(p);
				Point[] legalMoves=p.getLegalMoves();
				if (legalMoves.length>0) {
					Point move=legalMoves[rand.nextInt(legalMoves.length)];
					if (String.valueOf(p.getType()).toUpperCase().equals("P")){
						Piece target=Main.b.pieceAt(move);
						if (p.isFirstMove()&&target==null) {
							state=STATE.pawnmove;
							Main.b.move(move);
							Main.b.selectPiece(p);
							capture="";
						} else if(Main.b.getGhost()!=null&&move.equals(Main.b.getGhost())) {
							capture="x";
							Piece ghostPawn=Main.b.getGhostPiece();
							Main.b.move(move);
							Main.b.getPieces().remove(ghostPawn);
							Main.b.deselectPiece();
							state=STATE.detect;
						} else {
							Main.b.move(move);
							Main.b.deselectPiece();
							if (target!=null) {
								if (target.isWhite()!=whiteTurn) {
									//capture
									Main.b.getPieces().remove(target);
								}
								capture="x";
							} else {
								capture="";
							}
							if ((move.tuple.i(1)==3&&move.tuple.i(3)==3&&whiteTurn)||(move.tuple.i(1)==0&&move.tuple.i(3)==0&&!whiteTurn)) {
								promoteSquare=move;
								state=STATE.promote;
								
							} else {
								state=STATE.detect;
							}
						}
					} else {
						Main.b.move(move);
						
						Piece target=Main.b.pieceAt(move);
						if (target!=null) {
							capture="x";
							if (target.isWhite()!=whiteTurn) {
								//capture
								Main.b.getPieces().remove(target);
								state=STATE.detect;
								
							}
							
						} else {
							capture="";
						}
						if (Main.b.playerInCheck(aiColor)) {
							Main.b.undo();
							//Console.s.println("Checking move "+Board.pointToNotation(move));
						} else {
							//Console.s.println("Found Move");
							state=STATE.submit;
						}
					}
					
				} else {
					Main.b.deselectPiece();
				}
			} 
		} else if (state==STATE.pawnmove&&ai&&whiteTurn==aiColor) {
			int move=rand.nextInt(Main.b.moveableSpaces().length+1);
			if (move>=Main.b.moveableSpaces().length) {
				if (Main.b.playerInCheck(aiColor)) {
					Main.b.undo();
					state=STATE.move;
				} else {
					//Console.s.println("Found Move");
					state=STATE.submit;
					
				}
			} else {
				if (Main.b.pieceAt(Main.b.moveableSpaces()[move])==null) {
					Main.b.secondPawnMove(Main.b.moveableSpaces()[move]);
					if (Main.b.playerInCheck(aiColor)) {
						Main.b.undo();
						state=STATE.move;
					} else {
						//Console.s.println("Found Move");
						state=STATE.submit;
					}
				}else {
					Main.b.undo();
					state=STATE.move;
				}
				
			}
		} else if (state==STATE.submit&&ai&&whiteTurn==aiColor) {
			Main.b.deselectPiece();
			recNotation();
			whiteTurn=!whiteTurn;
			//Console.s.println("searching for legal moves...");
			
			boolean legalMove=Main.b.playerHasLegalMove(whiteTurn);
			checked=Main.b.playerInCheck(whiteTurn);
			if ((!legalMove)&&checked) {
				if (whiteTurn) {
					state=STATE.blackWins;
				} else {
					state=STATE.whiteWins;
				}
			} else if (!legalMove) {
				state=STATE.draw;
			} else {
				state=STATE.move;
				Console.s.println(whiteTurn);
				wPersp=(!wPersp&&!ai)||(ai&&!aiColor);
			}
		} else if (state==STATE.promote&&ai&&aiColor==whiteTurn) {
			Piece newPiece=null;
			if (aiColor) {
				newPiece=new Piece(promoteSquare,'Q');
			} else {
				newPiece=new Piece(promoteSquare,'q');
			}
			Main.b.getPieces().add(newPiece);
			promoteSquare=null;
			state=STATE.detect;
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_P) {
			wPersp=!wPersp;
		} else if (e.getKeyCode()==KeyEvent.VK_F&&!(ai&&whiteTurn==aiColor)) {
			if (state==STATE.pawnmove) {
				Main.b.deselectPiece();
				state=STATE.detectPawn;
			} else if (state==STATE.submit&&(!ai||whiteTurn!=aiColor)) {
				submit();
				
			}
			
		} else if (e.getKeyCode()==KeyEvent.VK_Z && (state==STATE.submit || state==STATE.illegal||state==STATE.pawnmove)) {
			capture="";
			state=STATE.move;
			Main.b.undo();
		}
	}
	
	
	public void mousePressed(MouseEvent e) {
		//Console.s.println("mouse pressed");
		if (board.contains(e.getPoint())){
			Point boardPoint=screenToBoard(e.getX(),e.getY());
			if (state==STATE.move && !(ai&&whiteTurn==aiColor)) {
				//Console.s.println("clicked board");
				Piece p=Main.b.pieceAt(boardPoint);
				if (p!=null) {
					if (whiteTurn==p.isWhite()) {
						Main.b.selectPiece(p);
						capture="";
					} else if (Main.b.spaceMoveable(boardPoint)){
						//capture
						capture="x";
						Main.b.move(boardPoint);
						Piece selectedPiece=Main.b.getSelectedPiece();
						Main.b.getPieces().remove(p);
						
						Main.b.deselectPiece();
						if (String.valueOf(selectedPiece.getType()).toUpperCase().equals("P")&&((boardPoint.tuple.i(1)==3&&boardPoint.tuple.i(3)==3&&whiteTurn)||(boardPoint.tuple.i(1)==0&&boardPoint.tuple.i(3)==0&&!whiteTurn))) {
							promoteSquare=boardPoint;
							state=STATE.promote;
							
						} else {
							state=STATE.detect;
						}
						
					}
				} else if (Main.b.spaceMoveable(boardPoint)) {
					
					Piece selected=Main.b.getSelectedPiece();
					if (String.valueOf(selected.getType()).toUpperCase().equals("P")){
						if(Main.b.getGhost()!=null&&boardPoint.equals(Main.b.getGhost())) {
							capture="x";
							Piece ghostPawn=Main.b.getGhostPiece();
							Main.b.move(boardPoint);
							Main.b.getPieces().remove(ghostPawn);
							
							
							Main.b.deselectPiece();
							state=STATE.detect;
						} else if (selected.isFirstMove()) {
							state=STATE.pawnmove;
							Main.b.move(boardPoint);
							Main.b.selectPiece(selected);
						} else {
							Main.b.move(boardPoint);
							Main.b.deselectPiece();
							if ((boardPoint.tuple.i(1)==3&&boardPoint.tuple.i(3)==3&&whiteTurn)||(boardPoint.tuple.i(1)==0&&boardPoint.tuple.i(3)==0&&!whiteTurn)) {
								promoteSquare=boardPoint;
								state=STATE.promote;
								
							} else {
								state=STATE.detect;
							}
						}
					} else {
						Main.b.move(boardPoint);
						Main.b.deselectPiece();
						state=STATE.detect;
						
					}
					
					
					
				} else {
					Main.b.deselectPiece();
				}
			} else if (state==STATE.pawnmove) {
				if (Main.b.spaceMoveable(boardPoint)&&Main.b.pieceAt(boardPoint)==null) {
					Main.b.secondPawnMove(boardPoint);
					Main.b.deselectPiece();
					state=STATE.detect;
				}
			}
		} else if (state==STATE.promote) {
			if (whiteTurn) {
				for (Rectangle r:promotablesW.keySet()) {
					if (r.contains(e.getPoint())) {
						if (r.contains(e.getPoint())) {
							Main.b.getPieces().remove(Main.b.pieceAt(promoteSquare));
							Piece newPiece=promotablesW.get(r).clone();
							newPiece.setPos(promoteSquare);
							Main.b.getPieces().add(newPiece);
							promoteSquare=null;
							state=STATE.detect;
						}
					}
				}
			} else {
				for (Rectangle r:promotablesB.keySet()) {
					if (r.contains(e.getPoint())) {
						Main.b.getPieces().remove(Main.b.pieceAt(promoteSquare));
						Piece newPiece=promotablesB.get(r).clone();
						newPiece.setPos(promoteSquare);
						Main.b.getPieces().add(newPiece);
						promoteSquare=null;
						state=STATE.detect;
					}
				}
			}
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		if (board.contains(e.getPoint())) {
			Point boardPoint=screenToBoard(e.getX(),e.getY());
			if (Board.contains(boardPoint)) {
				space=Board.pointToNotation(boardPoint);
			}
		}
	}
	
	public Point screenToBoard(int x,int y) {
		for (Rectangle r:rects.keySet()) {
			//Console.s.println("lookin through rects");
			if (r.contains(x,y)) {
				//rects.get(r).printVals("click");
				return (rects.get(r));
				
			}
		}
		return new Point(new Tuple(new double[] {-1,-1,-1,-1}));
	}

	public void recNotation() {
		if (whiteTurn) {
			if (Main.b.getGhost()==null) {
				String pieceLetter=String.valueOf(Main.b.lastPieceTypeMoved()).toUpperCase();
				if (pieceLetter.equals("P")) {
					pieceLetter="";
				}
				Console.s.print(pieceLetter+Board.pointToNotation(Main.b.lastMoveStart())+" "+capture+Board.pointToNotation(Main.b.lastMoveEnd())+" / ");
			} else {
				Console.s.print(Board.pointToNotation(Main.b.lastMoveStart())+" ("+Board.pointToNotation(Main.b.getGhost())+") "+Board.pointToNotation(Main.b.lastMoveEnd())+" / ");
			}
		} else {
			if (Main.b.getGhost()==null) {
				String pieceLetter=String.valueOf(Main.b.lastPieceTypeMoved()).toUpperCase();
				if (pieceLetter.equals("P")) {
					pieceLetter="";
				}
				Console.s.println(pieceLetter+Board.pointToNotation(Main.b.lastMoveStart())+" "+capture+Board.pointToNotation(Main.b.lastMoveEnd()));
			} else {
				Console.s.println(Board.pointToNotation(Main.b.lastMoveStart())+" ("+Board.pointToNotation(Main.b.getGhost())+") "+Board.pointToNotation(Main.b.lastMoveEnd()));
			}
		}
	}
	
	public void submit() {
		recNotation();
		if (ai) {
			state=STATE.move;
		}
		whiteTurn=!whiteTurn;
		boolean legalMove=Main.b.playerHasLegalMove(whiteTurn);
		checked=Main.b.playerInCheck(whiteTurn);
		if (!legalMove&&checked) {
			if (whiteTurn) {
				state=STATE.blackWins;
			} else {
				state=STATE.whiteWins;
			}
		} else if (!legalMove) {
			state=STATE.draw;
		} else {
			state=STATE.move;
			
			wPersp=(!wPersp&&!ai)||(ai&&!aiColor);
		}
	}
}
