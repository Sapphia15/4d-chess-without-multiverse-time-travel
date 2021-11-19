package unicorns.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;
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
import unicorns.Sounds;

public class Game extends Screen{

	Panel observer;
	Hashtable<Rectangle,Point> rects=new Hashtable<>();
	Rectangle board=new Rectangle(0,0,1,1);
	Board b=new Board();
	boolean wPersp=true;
	int oldWidth=0;
	int oldHeight=0;
	boolean oldPersp=true;
	String space="a0α0";
	boolean whiteTurn=true;
	boolean checked=false;
	boolean ai=true;
	
	boolean aiColor=false;
	static enum STATE {move,submit,pawnmove,detect,illegal,whiteWins,blackWins,draw,promote,detectPawn,detectMate,analyze};//need to add analyze state still...
	STATE state=STATE.move;
	Point promoteSquare=null;
	String promotePiece="";
	String capture="";
	boolean clocks=true;
	boolean firstMove=true;
	boolean online=false;
	long whiteTime=60000*20;
	long blackTime=60000*20;//20 minutes
	long timeIndex=0;
	//TODO need to add clocks!
	
	
	Hashtable<Rectangle,Piece> promotablesW=new Hashtable<>();
	Hashtable<Rectangle,Piece> promotablesB=new Hashtable<>();
	
	public Game(Panel observer) {
		this.observer=observer;
		if (ai) {
			wPersp=!aiColor;
		}
		b.setUp();
		
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, observer.getWidth(), observer.getHeight());
		g.setColor(Color.black);
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
			g.drawImage(Assets.BOARD, offX, offY, offX+num, offY+num, 192, 192, 0, 0, observer);
		} else {
			g.drawImage(Assets.BOARD, offX,offY,num, num, null);
		}
		Font newFont = currentFont.deriveFont((float)sq);
		g.setFont(newFont);
		if (clocks) {
			g.setColor(Color.white);
			g.fillRoundRect(5, 5, g.getFontMetrics().stringWidth(" White: 20:00  "), (int)Math.floor(sq*3.2),20,20);
			g.setColor(Color.black);
			g.drawString("White "+String.format("%01d", (int)Math.floor(whiteTime/60000))+":"+String.format("%01d",(int)Math.floor(whiteTime/1000)%60), 10, sq*2);
			g.drawString("Black "+String.format("%01d",(int)Math.floor(blackTime/60000))+":"+String.format("%01d",(int)Math.floor(blackTime/1000)%60), 10, sq*3);
		} else {
			g.setColor(Color.white);
			g.fillRoundRect(5, 5, g.getFontMetrics().stringWidth(" White: 20:00  "), (int)Math.floor(sq*1.2),20,20);
			g.setColor(Color.black);
		}
		g.drawString(space,10,sq);
		
		Piece selected=b.getSelectedPiece();
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
		
		Point lastMoveStart=b.lastMoveStart();
		
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
		
		Point lastMoveEnd=b.lastMoveEnd();
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
		
		for (Piece p : b.getPieces()) {
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
		
		if (b.getGhost()!=null) {
			Image ghost=null;
			if (b.getGhostPiece().isWhite()) {
				ghost=Assets.GHOST_W;
			} else {
				ghost=Assets.GHOST_B;
			}
			Point p=b.getGhost();
			int x=(int)p.tuple.i(0);
			int y=(int)p.tuple.i(1);
			int z=(int)p.tuple.i(2);
			int w=(int)p.tuple.i(3);
			
			if (wPersp) {
				y=3-y;
				w=3-w;
			} else {
				x=3-x;
				z=3-z;
			}
			
			g.drawImage(ghost,offX+x*sq+z*(gap3d4+sq4)+gap,offY+y*sq+gap+w*(gap3d4+sq4),sq-gap/4,sq-gap/4,null);

			
		}
		
		//Console.s.println(b.moveableSpaces().length);
		for (Point p : b.moveableSpaces()) {
			
			int x=(int)p.tuple.i(0);
			int y=(int)p.tuple.i(1);
			int z=(int)p.tuple.i(2);
			int w=(int)p.tuple.i(3);
			if (b.pieceAt(p)==null) {
				g.setColor(new Color(0,255,0,100));
			} else if (state==STATE.move) {
				g.setColor(new Color(255,0,0,100));	
			} else {
				g.setColor(new Color(0,0,0,0));
			}
			if (b.getGhost()!=null) {
				
				if (p.equals(b.getGhost())&&String.valueOf(b.getSelectedPiece().getType()).toUpperCase().equals("P")) {
					if (p.distance(b.getSelectedPiece().getPos())>1) {//make sure the pawn move is diagonal
						g.setColor(new Color(255,0,0,100));	
					}
				}
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
			g.setColor(new Color(255,255,255,250));
			g.fillRoundRect(10,offY+num/2, g.getFontMetrics().stringWidth("White Wins!")+gap*2, sq+gap, 20,20);
			g.setColor(new Color(0,0,0,250));
			g.drawString("White Wins!",10+gap,offY+num/2+sq);
			
		} else if (state==STATE.blackWins) {
			g.setColor(new Color(0,0,0,250));
			g.fillRoundRect(10,offY+num/2, g.getFontMetrics().stringWidth("Black Wins!")+gap*2, sq+gap, 20,20);
			g.setColor(new Color(255,255,255,250));
			g.drawString("Black Wins!",10+gap,offY+num/2+sq);
			
		} else if (state==STATE.draw){
			g.setColor(new Color(127,127,127,250));
			g.fillRoundRect(10,offY+num/2, g.getFontMetrics().stringWidth("Unicorns are amazing! (also it's a draw)")+gap*2, sq+gap, 20,20);
			g.setColor(new Color(255,100,100,250));
			g.drawString("Unicorns are amazing! (also it's a draw)",10+gap,offY+num/2+sq);
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
		if ((state==STATE.move||state==STATE.pawnmove||state==STATE.promote||state==STATE.submit||state==STATE.detect)&&clocks&&!firstMove) {
			long oldIndex=timeIndex;
			timeIndex=System.currentTimeMillis();
			if (whiteTurn) {
				whiteTime-=timeIndex-oldIndex;
				if (whiteTime<=0) {
					whiteTime=0;
					state=STATE.blackWins;
				}
			} else {
				blackTime-=timeIndex-oldIndex;
				if (blackTime<=0) {
					whiteTime=0;
					state=STATE.whiteWins;
				}
			}
		}
		if (state==STATE.detect||state==STATE.detectPawn) {
			Point king=null;
			if (whiteTurn) {
				king=b.getWhiteKing().getPos();
			} else {
				king=b.getBlackKing().getPos();
			}
			for (Piece p:b.getPieces()) {
				if (p.isWhite()!=whiteTurn) {
					for (Point move:p.getPotentialMoves(p.getType(),b)) {
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
				b.undo();
				state=STATE.move;
			}
			
		} else if (state==STATE.move&&ai&&whiteTurn==aiColor) {
			ArrayList<Point[]> legalMoves=b.getAllLegalMoves(whiteTurn);
			Point[] move=legalMoves.get(Main.rand.nextInt(legalMoves.size()));
			b.makeMove(move);
			state=STATE.submit;
			
		} else if (state==STATE.submit&&ai&&whiteTurn==aiColor) {
			b.deselectPiece();
			submit();
			/*recNotation();
			whiteTurn=!whiteTurn;
			//Console.s.println("searching for legal moves...");
			
			boolean legalMove=b.playerHasLegalMove(whiteTurn);
			checked=b.playerInCheck(whiteTurn);
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
				//Console.s.println(whiteTurn);
				wPersp=(!wPersp&&!ai)||(ai&&!aiColor);
			}*/
		} else if (state==STATE.promote&&ai&&aiColor==whiteTurn) {
			Piece newPiece=null;
			//remove the pawn
			b.getPieces().remove(b.pieceAt(promoteSquare));
			//for the sake of simplicity, ai will always promote to queen. It's probably the best in most situations anyway.
			if (aiColor) {
				newPiece=new Piece(promoteSquare,'Q');
			} else {
				newPiece=new Piece(promoteSquare,'q');
			}
			b.getPieces().add(newPiece);
			promoteSquare=null;
			state=STATE.detect;
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_P) {
			wPersp=!wPersp;
		} else if (e.getKeyCode()==KeyEvent.VK_F&&!(ai&&whiteTurn==aiColor&&(!(state==STATE.blackWins||state==STATE.whiteWins||state==STATE.draw)))) {
			if (state==STATE.pawnmove) {
				b.deselectPiece();
				state=STATE.detectPawn;
			} else if (state==STATE.submit&&(!ai||whiteTurn!=aiColor)) {
				submit();
				
			} else if (state==STATE.blackWins||state==STATE.whiteWins||state==STATE.draw) {
				if (state==STATE.blackWins) {
					Console.s.println("0-1");
				} else if (state==STATE.whiteWins) {
					Console.s.println("1-0");
				} else {
					Console.s.println("1/2-1/2");
				}
				observer.setScreen("title");
			}
			
		} else if (e.getKeyCode()==KeyEvent.VK_Z && (state==STATE.submit || state==STATE.illegal||state==STATE.pawnmove)) {
			capture="";
			state=STATE.move;
			b.undo();
		}
	}
	
	
	public void mousePressed(MouseEvent e) {
		//Console.s.println("mouse pressed");
		if (board.contains(e.getPoint())){
			Point boardPoint=screenToBoard(e.getX(),e.getY());
			if (state==STATE.move && !(ai&&whiteTurn==aiColor)) {
				//Console.s.println("clicked board");
				Piece p=b.pieceAt(boardPoint);
				if (p!=null) {
					if (whiteTurn==p.isWhite()) {
						b.selectPiece(p);
						capture="";
					} else if (b.spaceMoveable(boardPoint)){
						//capture
						capture="x";
						b.move(boardPoint);
						Piece selectedPiece=b.getSelectedPiece();
						b.getPieces().remove(p);
						
						b.deselectPiece();
						if (String.valueOf(selectedPiece.getType()).toUpperCase().equals("P")&&((boardPoint.tuple.i(1)==3&&boardPoint.tuple.i(3)==3&&whiteTurn)||(boardPoint.tuple.i(1)==0&&boardPoint.tuple.i(3)==0&&!whiteTurn))) {
							promoteSquare=boardPoint;
							state=STATE.promote;
							
						} else {
							state=STATE.detect;
						}
						
					}
				} else if (b.spaceMoveable(boardPoint)) {
					
					Piece selected=b.getSelectedPiece();
					if (String.valueOf(selected.getType()).toUpperCase().equals("P")){
						if(b.getGhost()!=null&&boardPoint.equals(b.getGhost())&&boardPoint.distance(selected.getPos())>1) {
							capture="x";
							Piece ghostPawn=b.getGhostPiece();
							b.move(boardPoint);
							b.getPieces().remove(ghostPawn);
							
							
							b.deselectPiece();
							state=STATE.detect;
						} else if (selected.isFirstMove()) {
							state=STATE.pawnmove;
							b.move(boardPoint);
							b.selectPiece(selected);
						} else {
							//TODO this looks like it should work but promotion just didn't happen once during a test run...
							b.move(boardPoint);
							b.deselectPiece();
							if ((boardPoint.tuple.i(1)==3&&boardPoint.tuple.i(3)==3&&whiteTurn)||(boardPoint.tuple.i(1)==0&&boardPoint.tuple.i(3)==0&&!whiteTurn)) {
								promoteSquare=boardPoint;
								state=STATE.promote;
								
							} else {
								state=STATE.detect;
							}
						}
					} else {
						b.move(boardPoint);
						b.deselectPiece();
						state=STATE.detect;
						
					}
					
					
					
				} else {
					b.deselectPiece();
				}
			} else if (state==STATE.pawnmove) {
				if (b.spaceMoveable(boardPoint)&&b.pieceAt(boardPoint)==null) {
					b.secondPawnMove(boardPoint);
					b.deselectPiece();
					state=STATE.detect;
				}
			}
		} else if (state==STATE.promote) {
			if (whiteTurn) {
				for (Rectangle r:promotablesW.keySet()) {
					if (r.contains(e.getPoint())) {
						if (r.contains(e.getPoint())) {
							b.getPieces().remove(b.pieceAt(promoteSquare));
							Piece newPiece=promotablesW.get(r).clone();
							promotePiece=String.valueOf(newPiece.getType()).toUpperCase();
							newPiece.setPos(promoteSquare);
							b.getPieces().add(newPiece);
							promoteSquare=null;
							state=STATE.detect;
						}
					}
				}
			} else {
				for (Rectangle r:promotablesB.keySet()) {
					if (r.contains(e.getPoint())) {
						b.getPieces().remove(b.pieceAt(promoteSquare));
						Piece newPiece=promotablesB.get(r).clone();
						promotePiece=String.valueOf(newPiece.getType()).toUpperCase();
						newPiece.setPos(promoteSquare);
						b.getPieces().add(newPiece);
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
			if (boardPoint!=null&&Board.contains(boardPoint)) {
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
			if (b.getGhost()==null) {
				String pieceLetter=String.valueOf(b.lastPieceTypeMoved()).toUpperCase();
				if (pieceLetter.equals("P")) {
					pieceLetter="";
				}
				Console.s.print(pieceLetter+Board.pointToNotation(b.lastMoveStart())+" "+capture+Board.pointToNotation(b.lastMoveEnd())+promotePiece+" / ");
			} else {
				Console.s.print(Board.pointToNotation(b.lastMoveStart())+" ("+Board.pointToNotation(b.getGhost())+") "+Board.pointToNotation(b.lastMoveEnd())+promotePiece+" / ");
			}
		} else {
			if (b.getGhost()==null) {
				String pieceLetter=String.valueOf(b.lastPieceTypeMoved()).toUpperCase();
				if (pieceLetter.equals("P")) {
					pieceLetter="";
				}
				Console.s.println(pieceLetter+Board.pointToNotation(b.lastMoveStart())+" "+capture+Board.pointToNotation(b.lastMoveEnd())+promotePiece);
			} else {
				Console.s.println(Board.pointToNotation(b.lastMoveStart())+" ("+Board.pointToNotation(b.getGhost())+") "+Board.pointToNotation(b.lastMoveEnd())+promotePiece);
			}
		}
		promotePiece="";
	}
	
	public void setInit(){
		state=STATE.detectMate;
		checked=false;
		whiteTurn=true;
		firstMove=true;
		b.setUp();
		//b.experimentSetUp();
		whiteTime=60000*20;
		blackTime=60000*20;
		this.ai=observer.ai();
		this.clocks=observer.clocks();
		if (ai) {
			aiColor=(1==Main.rand.nextInt(2));
			wPersp=!aiColor;
		} else {
			wPersp=true;
		}
		state=STATE.move;
	}
	
	public Board getBoard() {
		return this.b;
	}
	
	public void submit() {
		
		state=STATE.detectMate;
		recNotation();
		if (firstMove) {
			if (!whiteTurn) {
				firstMove=false;
				timeIndex=System.currentTimeMillis();
			}
		} else if (clocks){
			long oldIndex=timeIndex;
			timeIndex=System.currentTimeMillis();
			new Sounds().playSound("endturn.wav", 0);
			//3 second move increment and update clock last time before turning over to the next player
			if (whiteTurn) {
				whiteTime+=3000-(timeIndex-oldIndex);
			} else {
				blackTime+=3000-(timeIndex-oldIndex);
			}
			timeIndex=System.currentTimeMillis();
		}
		whiteTurn=!whiteTurn;
		boolean legalMove=b.playerHasLegalMove(whiteTurn);
		checked=b.playerInCheck(whiteTurn);
		
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
			
			wPersp=(!wPersp&&!ai)||(ai&&!aiColor);
		}
	}
}
