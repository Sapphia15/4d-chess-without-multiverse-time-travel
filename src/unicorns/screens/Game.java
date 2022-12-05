package unicorns.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;

import java.awt.Rectangle;

import gameutil.math.geom.BezierCurve;
import gameutil.math.geom.Orthotope;
import gameutil.math.geom.Point;
import gameutil.math.geom.Tuple;
import gameutil.text.Console;
import gameutil.text.Iru.Letter;
import graphics.screen.Screen;
import unicorns.Assets;
import unicorns.Board;
import unicorns.Main;
import unicorns.Move;
import unicorns.Panel;
import unicorns.Piece;
import unicorns.Sounds;
import unicorns.ai.AI;
import unicorns.ai.CaptureAI;
import unicorns.ai.RandomAI;

public class Game extends Screen{

	Panel observer;
	ConcurrentHashMap<Rectangle,Point> rects=new ConcurrentHashMap<>();
	CopyOnWriteArrayList<BezierCurve> bezties=new CopyOnWriteArrayList<>();
	Rectangle board=new Rectangle(0,0,1,1);
	Rectangle menu=new Rectangle(0,0,1,1);
	Rectangle exit=new Rectangle(0,0,1,1);
	Rectangle next=new Rectangle(0,0,1,1);
	Rectangle back=new Rectangle(0,0,1,1);
	Rectangle moveNo=new Rectangle(0,0,1,1);
	Board b=new Board();
	boolean wPersp=true;
	int oldWidth=0;
	int oldHeight=0;
	boolean oldPersp=true;
	boolean oldInvertXZ=false;
	boolean oldInvertYW=false;
	String space="a0α0";
	boolean whiteTurn=true;
	boolean checked=false;
	boolean ai=true;
	boolean simpleBoard=false;
	boolean sounds=true;
	boolean highlightMoves=true;
	boolean drawChecks=true;
	boolean showCaptures=true;
	int lastButtonPressed=0;
	Point[] checkingMove=null;
	
	boolean oppColor=false;
	static enum STATE {move,submit,pawnmove,detect,illegal,whiteWins,blackWins,draw,promote,detectPawn,detectMate,analyze};//need to add analyze state still...
	STATE state=STATE.move;
	Point promoteSquare=null;
	String promotePiece="";
	String capture="";
	boolean clocks=true;
	boolean firstMove=true;
	boolean invertMetaXZ=false;
	boolean invertMetaYW=false;
	boolean online=false;
	boolean showIngameMenu=false;
	long lastMoveChange=System.currentTimeMillis();
	long whiteTime=60000*20;
	long blackTime=60000*20;//20 minutes
	long whiteDelay=0;
	long blackDelay=0;
	long timeIndex=0;
	int move=0;
	long bonus=3000;
	long delay=0;
	Move[] analyzeMoves;
	AI theAi=new CaptureAI();
	String gameLog="";
	//TODO need to add clocks!
	
	
	Hashtable<Rectangle,Piece> promotablesW=new Hashtable<>();
	Hashtable<Rectangle,Piece> promotablesB=new Hashtable<>();
	
	public Game(Panel observer) {
		this.observer=observer;
		if (ai) {
			wPersp=!oppColor;
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
		Image boardImg;
		if (simpleBoard) {
			boardImg=Assets.BOARD_S;
		} else {
			boardImg=Assets.BOARD;
		}
		if (wPersp) {
			g.drawImage(boardImg, offX, offY, offX+num, offY+num, 192, 192, 0, 0, observer);
		} else {
			g.drawImage(boardImg, offX,offY,num, num, null);
		}
		Font newFont = currentFont.deriveFont((float)sq);
		g.setFont(newFont);
		if (clocks) {
			g.setColor(Color.white);
			g.fillRoundRect(5, 5, g.getFontMetrics().stringWidth(" White: 20:00 +10  "), (int)Math.floor(sq*3.2),20,20);
			g.setColor(Color.black);
			String whiteDelay=" +"+String.format("%01d", (int)Math.floor(this.whiteDelay/1000));
			String blackDelay=" +"+String.format("%01d", (int)Math.floor(this.blackDelay/1000));
			if (this.whiteDelay==0) {
				whiteDelay="";
			}
			if (this.blackDelay==0) {
				blackDelay="";
			}
			
			g.drawString("White "+String.format("%01d", (int)Math.floor(whiteTime/60000))+":"+String.format("%01d",(int)Math.floor(whiteTime/1000)%60)+whiteDelay, 10, sq*2);
			g.drawString("Black "+String.format("%01d",(int)Math.floor(blackTime/60000))+":"+String.format("%01d",(int)Math.floor(blackTime/1000)%60)+blackDelay, 10, sq*3);
		} else {
			g.setColor(Color.white);
			g.fillRoundRect(5, 5, g.getFontMetrics().stringWidth(" White: 20:00 +10  "), (int)Math.floor(sq*1.2),20,20);
			g.setColor(Color.black);
		}
		g.drawString(space,10,sq);
		
		Piece selected=b.getSelectedPiece();
		if (selected!=null) {
			
			int x;
			int y;
			int z;
			int w;
			if (invertMetaXZ) {
				x=selected.getZ();
				
				z=selected.getX();
				
			} else {
				x=selected.getX();
				
				z=selected.getZ();
				
			}
			
			if (invertMetaYW) {
				
				y=selected.getW();
				
				w=selected.getY();
			} else {
				
				y=selected.getY();
				
				w=selected.getW();
			}
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
			
			int x;
			int y;
			int z;
			int w;
			if (invertMetaXZ) {
				x=(int)lastMoveStart.tuple.i(2);
				
				z=(int)lastMoveStart.tuple.i(0);
				
			} else {
				x=(int)lastMoveStart.tuple.i(0);
				
				z=(int)lastMoveStart.tuple.i(2);
				
			}
			if (invertMetaYW) {
				
				y=(int)lastMoveStart.tuple.i(3);
				
				w=(int)lastMoveStart.tuple.i(1);
			} else {
				
				y=(int)lastMoveStart.tuple.i(1);
				
				w=(int)lastMoveStart.tuple.i(3);
			}
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
			
			int x;
			int y;
			int z;
			int w;
			if (invertMetaXZ) {
				x=(int)lastMoveEnd.tuple.i(2);
				
				z=(int)lastMoveEnd.tuple.i(0);
				
			} else {
				x=(int)lastMoveEnd.tuple.i(0);
				
				z=(int)lastMoveEnd.tuple.i(2);
				
			}
			if (invertMetaYW) {
				
				y=(int)lastMoveEnd.tuple.i(3);
				
				w=(int)lastMoveEnd.tuple.i(1);
			} else {
				
				y=(int)lastMoveEnd.tuple.i(1);
				
				w=(int)lastMoveEnd.tuple.i(3);
			}
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
			int x;
			int y;
			int z;
			int w;
			if (invertMetaXZ) {
				x=p.getZ();
				
				z=p.getX();
				
			} else {
				x=p.getX();
				
				z=p.getZ();
				
			}
			
			if (invertMetaYW) {
				
				y=p.getW();
				
				w=p.getY();
			} else {
				
				y=p.getY();
				
				w=p.getW();
			}
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
			int x;
			int y;
			int z;
			int w;
			if (invertMetaXZ) {
				x=(int)p.tuple.i(2);
				
				z=(int)p.tuple.i(0);
				
			} else {
				x=(int)p.tuple.i(0);
				
				z=(int)p.tuple.i(2);
				
			}
			if (invertMetaYW) {
				
				y=(int)p.tuple.i(3);
				
				w=(int)p.tuple.i(1);
			} else {
				
				y=(int)p.tuple.i(1);
				
				w=(int)p.tuple.i(3);
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
		
		//Console.s.println(b.moveableSpaces().length);
		if (highlightMoves) {
			for (Point p : b.moveableSpaces()) {
				int x;
				int y;
				int z;
				int w;
				if (invertMetaXZ) {
					x=(int)p.tuple.i(2);
					
					z=(int)p.tuple.i(0);
					
				} else {
					x=(int)p.tuple.i(0);
					
					z=(int)p.tuple.i(2);
					
				}
				if (invertMetaYW) {
					
					y=(int)p.tuple.i(3);
					
					w=(int)p.tuple.i(1);
				} else {
					
					y=(int)p.tuple.i(1);
					
					w=(int)p.tuple.i(3);
				}
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
		} else if (state==STATE.analyze) {
			g.setColor(Color.pink);
			if (move>0) {
				g.fillRoundRect(back.x, back.y,back.width,back.height, 10,10);
				g.drawImage(Letter.LESS.img16(), back.x, back.y-2,back.width,back.height, null);
			}
			if (move<analyzeMoves.length) {
				g.fillRoundRect(next.x, next.y,next.width,next.height, 10,10);
				g.drawImage(Letter.MORE.img16(), next.x, next.y-2,next.width,next.height, null);
			}
			g.fillRoundRect(moveNo.x, moveNo.y, moveNo.width, moveNo.height, 20, 20);
			g.setColor(Color.black);
			g.drawString("Move: "+(move+1)+" / "+(analyzeMoves.length+1), moveNo.x, moveNo.y+sq);
			g.drawString("Turn: "+((move)/2d+1)+" / "+((analyzeMoves.length)/2d+1), moveNo.x, moveNo.y+sq*2+5);
		}
		
		if (showCaptures) {
			//draw material
			int[] captures=b.getCapturedPieces();
			g.drawImage(Assets.ROOK_W, 10, sq*4,sq,sq, null);
			g.drawImage(Assets.BISHOP_W, 10+2*sq, sq*4,sq,sq, null);
			g.drawImage(Assets.UNICORN_W, 10+4*sq, sq*4,sq,sq, null);
			g.drawImage(Assets.DRAGON_W, 10+6*sq, sq*4,sq,sq, null);
			g.drawImage(Assets.PAWN_W, 10, sq*6,sq,sq, null);
			g.drawImage(Assets.QUEEN_W, 10+2*sq, sq*6,sq,sq, null);
			g.drawImage(Assets.KNIGHT_W, 10+4*sq, sq*6,sq,sq, null);
			g.setColor(new Color(255,255,150));
			g.drawString(""+captures[1], 10, sq*6-5);
			g.drawString(""+captures[2], 10+2*sq, sq*6-5);
			g.drawString(""+captures[3], 10+4*sq, sq*6-5);
			g.drawString(""+captures[4], 10+6*sq, sq*6-5);
			g.drawString(""+captures[0], 10, sq*8-5);
			g.drawString(""+captures[5], 10+2*sq, sq*8-5);
			g.drawString(""+captures[6], 10+4*sq, sq*8-5);
			
			g.drawImage(Assets.ROOK_B, 10, sq*10,sq,sq, null);
			g.drawImage(Assets.BISHOP_B, 10+2*sq, sq*10,sq,sq, null);
			g.drawImage(Assets.UNICORN_B, 10+4*sq, sq*10,sq,sq, null);
			g.drawImage(Assets.DRAGON_B, 10+6*sq, sq*10,sq,sq, null);
			g.drawImage(Assets.PAWN_B, 10, sq*12,sq,sq, null);
			g.drawImage(Assets.QUEEN_B, 10+2*sq, sq*12,sq,sq, null);
			g.drawImage(Assets.KNIGHT_B, 10+4*sq, sq*12,sq,sq, null);
			
			g.drawString(""+captures[8], 10, sq*12-5);
			g.drawString(""+captures[9], 10+2*sq, sq*12-5);
			g.drawString(""+captures[10], 10+4*sq, sq*12-5);
			g.drawString(""+captures[11], 10+6*sq, sq*12-5);
			g.drawString(""+captures[7], 10, sq*14-5);
			g.drawString(""+captures[12], 10+2*sq, sq*14-5);
			g.drawString(""+captures[13], 10+4*sq, sq*14-5);
		}
		//if (bez!=null) {
		for (BezierCurve bez:bezties) {
			try {
				BezierCurve colorBez=new BezierCurve(new Point[] {new Point(new double[] {0,255}),new Point(new double[] {150,0,200}),new Point(new double[] {255})});
				int width=20;
				double dt=.001*Math.pow(bezties.size(),2)/4;
				
				Graphics2D g2 = (Graphics2D) g;
			    
			    Point start=bez.getPoint(0);
				for (double t=dt;t<=1;t+=dt) {
					
					start=bez.getPoint(t-dt);
					Point curvePoint=bez.getPoint(t);
					//g.fillOval((int)Math.round(curvePoint.tuple.i(0))-10, (int)Math.round(curvePoint.tuple.i(1))-10,5, 20);
					if (!curvePoint.equals(start)) {
						g2.setStroke(new BasicStroke(20-(float)(10*(1-t))));
						Point colorPoint=colorBez.getPoint(t);
						
						g2.setColor(new Color((int)colorPoint.tuple.i(0),(int)colorPoint.tuple.i(1),(int)colorPoint.tuple.i(2)));
						g2.drawLine((int)start.tuple.i(0), (int)start.tuple.i(1), (int)curvePoint.tuple.i(0), (int)curvePoint.tuple.i(1));
					}
					//g.fillOval((int)Math.round(curvePoint.tuple.i(0))-7, (int)Math.round(curvePoint.tuple.i(1))-7,2, 15);
					
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (checkingMove!=null&&drawChecks&&checked) {
			try {
			
				
				Rectangle r1=null;
				Rectangle r2=null;
				for (Rectangle r:rects.keySet()) {
					if (rects.get(r).equals(checkingMove[0])) {
						r1=r;
						break;
					}
				}
				for (Rectangle r:rects.keySet()) {
					if (rects.get(r).equals(checkingMove[1])) {
						r2=r;
						break;
					}
				}
				BezierCurve bez=new BezierCurve(new Point[] {new Point(new double[] {r1.getCenterX(),r1.getCenterY()}),new Point(new double[] {r1.getCenterX(),r1.getCenterY()}),new Point(new double[] {r2.getCenterX(),r2.getCenterY()})});
				CopyOnWriteArrayList<Point> points=bez.getPoints();
				Point intermed=points.get(0).lerp(points.get(2), .5);
				double dx=points.get(2).tuple.i(0)-points.get(0).tuple.i(0);
				double dy=points.get(2).tuple.i(1)-points.get(0).tuple.i(1);
				double y=intermed.tuple.i(1)-dy/2+dx;
				if (y<board.y) {
					y=board.y;
				}
				bez.setPoint(new Point(new double[] {intermed.tuple.i(0),y}),1);
				BezierCurve colorBez=new BezierCurve(new Point[] {new Point(new double[] {0,255}),new Point(new double[] {250,0,250}),new Point(new double[] {255})});
				int width=20;
				double dt=.001;
				
				Graphics2D g2 = (Graphics2D) g;
			    
			    Point start=bez.getPoint(0);
				for (double t=dt;t<=1;t+=dt) {
					
					start=bez.getPoint(t-dt);
					Point curvePoint=bez.getPoint(t);
					//g.fillOval((int)Math.round(curvePoint.tuple.i(0))-10, (int)Math.round(curvePoint.tuple.i(1))-10,5, 20);
					if (!curvePoint.equals(start)) {
						g2.setStroke(new BasicStroke(20-(float)(10*(1-t))));
						Point colorPoint=colorBez.getPoint(t);
						
						g2.setColor(new Color((int)colorPoint.tuple.i(0),(int)colorPoint.tuple.i(1),(int)colorPoint.tuple.i(2),5));
						g2.drawLine((int)start.tuple.i(0), (int)start.tuple.i(1), (int)curvePoint.tuple.i(0), (int)curvePoint.tuple.i(1));
					}
					//g.fillOval((int)Math.round(curvePoint.tuple.i(0))-7, (int)Math.round(curvePoint.tuple.i(1))-7,2, 15);
					
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (showIngameMenu) {
			g.setColor(Color.BLACK);
			g.fillRoundRect(menu.x-5, menu.y-5, menu.width+10, menu.height+10, 20, 20);
			g.setColor(Color.DARK_GRAY);
			g.fillRoundRect(menu.x, menu.y, menu.width, menu.height, 20, 20);
			g.setColor(Color.pink);
			g.drawString("Exit", exit.x, exit.y+g.getFontMetrics().getHeight());
			exit.setBounds(exit.x, exit.y, g.getFontMetrics().stringWidth("Exit"),g.getFontMetrics().getHeight());
			int exitHeight=g.getFontMetrics().getHeight();
			if (gap==8) {
				exitHeight/=2;
			}
			//int hotkeyWidth=(int) (num/3.84);
			//g.drawImage(Assets.HOTKEYS, (int)menu.getCenterX()-hotkeyWidth/2, exit.y+g.getFontMetrics().getHeight()*2, hotkeyWidth, hotkeyWidth, null);
			g.setColor(new Color(255,255,150));
			int hotkeyHeight=exitHeight*3/2;
			if(gap>8) {
				g.setFont(g.getFont().deriveFont(gap+12f));
			} else {
				hotkeyHeight=exitHeight*2;
				g.setFont(g.getFont().deriveFont(gap+4f));
			}
			g.drawString( "Hotkeys and Controls:",exit.x, exit.y+hotkeyHeight+g.getFontMetrics().getHeight());
			g.setFont(g.getFont().deriveFont(gap+4f));
			g.drawString( "F - submit",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*2);
			g.drawString( "Z - undo",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*3);
			g.drawString( "S - toggle sound effects",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*4);
			g.drawString( "M - toggle music",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*5);
			g.drawString( "P - flip perspective",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*6);
			g.drawString( "I - swap x and z perspective",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*7);
			g.drawString( "K - swap y and w perspective",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*8);
			g.drawString( "O - open game (only in analyze mode)",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*9);
			g.drawString( "Ctrl + S - save game (only out of analyze mode) *saving when a game is over will open analysis mode and go back to the first turn",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*10);
			g.drawString( "C - toggle show checks",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*11);
			g.drawString( "B - board style",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*12);
			g.drawString( "A/D - move forward/backward a turn (only in analyze mode)",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*13);
			g.drawString( "Q - toggle show captures",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*14);
			g.drawString( "H - toggle highlighting of valid moves of the selected piece",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*15);
			g.drawString( "\\ - open Command Line 4D *click the Command Line 4D window, type help, and then press enter to see a list of commands",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*16);
			g.drawString( "Left click - select a piece or move selected piece to the clicked squares",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*17);
			g.drawString( "*note, to move a pawn two spaces on its first turn you must move it twice before submitting your move",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*18);
			g.drawString( "Right click + drag - draw arrows. Right click off of the board to clear them.",exit.x, exit.y+exitHeight*2+g.getFontMetrics().getHeight()*19);

		}
	}

	@Override
	public void update() {
		if (oldWidth!=observer.getWidth()||oldHeight!=observer.getHeight()||oldPersp!=wPersp||oldInvertXZ!=invertMetaXZ||oldInvertYW!=invertMetaYW) {
			oldPersp=wPersp;
			oldWidth=observer.getWidth();
			oldHeight=observer.getHeight();
			oldInvertXZ=invertMetaXZ;
			oldInvertYW=invertMetaYW;
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
			int midY=(int)Math.floor(observer.getHeight()/2d);
			int offX=(int)Math.floor(observer.getWidth()/2d)-num/2;
			int offY=midY-num/2;
			rects.clear();
			board=new Rectangle(offX,offY,num,num);
			menu=new Rectangle((int)board.getCenterX()-sq4*4,(int)board.getCenterY()-sq4*2,sq*32,sq*16);
			exit.setLocation(menu.x+10, menu.y+10);
			moveNo=new Rectangle(board.x+board.width,10,observer.getWidth()-(board.x+board.width),sq*2+15);
			back=new Rectangle(board.x-32,midY-16,32,32);
			next=new Rectangle(board.x+board.width,midY-16,32,32);
			//menu buttons and stuff
			for (int i=0; i<4;i++) {
				for (int j=0; j<4;j++) {
					for (int k=0; k<4;k++) {
						for (int n=0; n<4;n++) {
							int x=i;
							int y=j;
							int z=k;
							int w=n;
							if (invertMetaXZ) {
								x=k;
								
								z=i;
								
							}
							if (invertMetaYW) {
								
								y=n;
								
								w=j;
							}
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
				java.awt.Point mouse=observer.getMousePosition();
				if (mouse!=null&&board.contains(mouse)) {
					Point boardPoint=screenToBoard((int)mouse.getX(),(int)mouse.getY());
					if (boardPoint!=null&&Board.contains(boardPoint)) {
						space=Board.pointToNotation(boardPoint);
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
			bezties.clear();
		}
		if ((state==STATE.move||state==STATE.pawnmove||state==STATE.promote||state==STATE.submit||state==STATE.detect||state==STATE.illegal)&&clocks&&!firstMove) {
			long oldIndex=timeIndex;
			timeIndex=System.currentTimeMillis();
			if (whiteTurn) {
				if (whiteDelay>0) {
					whiteDelay-=timeIndex-oldIndex;
					if (whiteDelay<0) {
						whiteTime=whiteTime+whiteDelay;//reduce the main time by the amount of time that the delay went over
						whiteDelay=0;
					}
				} else {
					whiteTime-=timeIndex-oldIndex;
				}
				if (whiteTime<=0) {
					whiteTime=0;
					state=STATE.blackWins;
				}
			} else {
				if (blackDelay>0) {
					blackDelay-=timeIndex-oldIndex;
					if (blackDelay<0) {
						blackTime=blackTime+blackDelay;//reduce the main time by the amount of time that the delay went over
						blackDelay=0;
					}
				} else {
					blackTime-=timeIndex-oldIndex;
				}
				if (blackTime<=0) {
					blackTime=0;
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
								
								checkingMove=new Point[] {p.getPos(),move};
								
									
						}
					}
				}
			}
			if (state!=STATE.illegal) {
				checkingMove=null;
				if (state==STATE.detectPawn) {
					state=STATE.submit;
					submit();
					
				} else {
					state=STATE.submit;
					
				}
			} else if (ai&&whiteTurn==oppColor) {
				b.undo();
				state=STATE.move;
			}
			
		} else if (state==STATE.move&&ai&&whiteTurn==oppColor) {
			ArrayList<Point[]> legalMoves=b.getAllLegalMoves(whiteTurn);
			
			Point[] move=theAi.getMove(legalMoves,b.clone());
			b.makeMove(move);
			state=STATE.submit;
			
		} else if (state==STATE.submit&&ai&&whiteTurn==oppColor) {
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
		} else if (state==STATE.promote&&ai&&oppColor==whiteTurn) {
			Piece newPiece=null;
			//remove the pawn
			b.getPieces().remove(b.pieceAt(promoteSquare));
			//for the sake of simplicity, ai will always promote to queen. It's probably the best in most situations anyway.
			if (oppColor) {
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
		} else if (e.getKeyCode()==KeyEvent.VK_F&&!(ai&&whiteTurn==oppColor&&(!(state==STATE.blackWins||state==STATE.whiteWins||state==STATE.draw)))) {
			if (state==STATE.pawnmove) {
				b.deselectPiece();
				state=STATE.detectPawn;
			} else if (state==STATE.submit&&(!(ai||online)||whiteTurn!=oppColor)) {
				submit();
				
			} else if (state==STATE.blackWins||state==STATE.whiteWins||state==STATE.draw) {
				if (state==STATE.blackWins) {
					Console.s.println("0-1");
					gameLog="[Result: 0-1]\n"+gameLog;
				} else if (state==STATE.whiteWins) {
					Console.s.println("1-0");
					gameLog="[Result: 1-0]\n"+gameLog;
				} else {
					Console.s.println("1/2-1/2");
					gameLog="[Result: 1/2-1/2]\n"+gameLog;
				}
				observer.setScreen("title");
			}
			
		} else if (e.getKeyCode()==KeyEvent.VK_Z && (state==STATE.submit || state==STATE.illegal||state==STATE.pawnmove)) {
			capture="";
			state=STATE.move;
			b.undo();
			checkingMove=b.checkingMove(whiteTurn);
		} else if (e.getKeyCode()==KeyEvent.VK_A&&state==STATE.analyze) {
			if (move>0&&System.currentTimeMillis()-lastMoveChange>100) {
				capture="";
				b.undo();
				move--;
				//Console.s.println(move+"   ::  "+analyzeMoves.length);
				checkingMove=b.checkingMove(whiteTurn);
				lastMoveChange=System.currentTimeMillis();
			}
		} else if (e.getKeyCode()==KeyEvent.VK_D&&state==STATE.analyze) {
			//redo move
			if (System.currentTimeMillis()-lastMoveChange>100) {
				if (move<analyzeMoves.length) {
					capture="";
					b.makeMove(analyzeMoves[move]);
					Point[] ps=analyzeMoves[move].getPoints();
					//Console.s.println(Board.pointToNotation(ps[0])+"   "+Board.pointToNotation(ps[1]));
					//Console.s.println(move+"   ::  "+analyzeMoves.length);
					move++;
					checkingMove=b.checkingMove(whiteTurn);
					lastMoveChange=System.currentTimeMillis();
				} else if (sounds&&System.currentTimeMillis()-lastMoveChange>500) {
					//Console.s.println(move+"   ::  "+analyzeMoves.length);
					Main.sounds.playSound("endturn.wav", 0);
					lastMoveChange=System.currentTimeMillis();
				}
			}
		} else if (e.getKeyCode()==KeyEvent.VK_I) {
			invertMetaXZ=!invertMetaXZ;
		} else if (e.getKeyCode()==KeyEvent.VK_K) {
			invertMetaYW=!invertMetaYW;
		} else if (e.getKeyCode()==KeyEvent.VK_B) {
			simpleBoard=!simpleBoard;
		} else if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
			showIngameMenu=!showIngameMenu;
		} else if (e.getKeyCode()==KeyEvent.VK_S) {
			if (e.isControlDown()&&state!=STATE.analyze) {
				if (state==STATE.blackWins||state==STATE.whiteWins||state==STATE.draw) {
					if (state==STATE.blackWins) {
						Console.s.println("0-1");
						gameLog="[Result: 0-1]\n"+gameLog;
					} else if (state==STATE.whiteWins) {
						Console.s.println("1-0");
						gameLog="[Result: 1-0]\n"+gameLog;
					} else {
						Console.s.println("1/2-1/2");
						gameLog="[Result: 1/2-1/2]\n"+gameLog;
					}
				}
				File save=Main.saveFile();
				try {
					FileWriter w=new FileWriter(save,StandardCharsets.UTF_8);
					w.write(gameLog);
					w.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (state==STATE.blackWins||state==STATE.whiteWins||state==STATE.draw) {
					importGame(save);
				}
				
			} else {
				sounds=!sounds;
			}
		} else if (e.getKeyCode()==KeyEvent.VK_H) {
			highlightMoves=!highlightMoves;
		} else if (e.getKeyCode()==KeyEvent.VK_BACK_SLASH) {
			Main.cmd.setVisible(true);
		} else if (e.getKeyCode()==KeyEvent.VK_C) {
			drawChecks=!drawChecks;
		} else if (e.getKeyCode()==KeyEvent.VK_M) {
			observer.toggleMusic();
		} else if (e.getKeyCode()==KeyEvent.VK_Q) {
			showCaptures=!showCaptures;
		} else if (e.getKeyCode()==KeyEvent.VK_O&&state==STATE.analyze) {
			importGame();
		}
	}
	
	
	public void mousePressed(MouseEvent e) {
		//Console.s.println("mouse pressed");
		if (e.getButton()==1) {
			if (showIngameMenu) {
				if (exit.contains(e.getPoint())) {
					if (online) {
						observer.exitOnlineGame();
					}
					observer.setScreen("title");
					showIngameMenu=false;
				}
			} else if (board.contains(e.getPoint())){
				Point boardPoint=screenToBoard(e.getX(),e.getY());
				Piece p=b.pieceAt(boardPoint);
				if (state==STATE.move && !((ai||online)&&whiteTurn==oppColor)) {
					//Console.s.println("clicked board");
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
							b.updateCaptures(p.getType());
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
								b.updateCaptures(ghostPawn.getType());
								
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
				} else if (state==STATE.analyze) {
					if (p!=null) {
						b.selectPiece(p);
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
			} else if (state==STATE.analyze) {
				if (next.contains(e.getPoint())&&move<analyzeMoves.length) {
					capture="";
					b.makeMove(analyzeMoves[move]);
					Point[] ps=analyzeMoves[move].getPoints();
					//Console.s.println(Board.pointToNotation(ps[0])+"   "+Board.pointToNotation(ps[1]));
					//Console.s.println(move+"   ::  "+analyzeMoves.length);
					move++;
					checkingMove=b.checkingMove(whiteTurn);
					lastMoveChange=System.currentTimeMillis();
				} else if (back.contains(e.getPoint())&&move>0) {
					capture="";
					b.undo();
					move--;
					//Console.s.println(move+"   ::  "+analyzeMoves.length);
					checkingMove=b.checkingMove(whiteTurn);
					lastMoveChange=System.currentTimeMillis();
				}
			}
			lastButtonPressed=e.getButton();
		} else if (e.getButton()==3) {
			java.awt.Point mPoint=e.getPoint();
			if (board.contains(mPoint)) {
				bezties.add(new BezierCurve(new Point[]{new Point(new double[] {mPoint.x,mPoint.y}),new Point(new double[] {mPoint.x,mPoint.y}),new Point(new double[] {mPoint.x,mPoint.y})}));
			} else {
				bezties.clear();
			}
			lastButtonPressed=e.getButton();
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
		String toLog="";
		if (whiteTurn) {
			if (b.getGhost()==null) {
				String pieceLetter=String.valueOf(b.lastPieceTypeMoved()).toUpperCase();
				if (pieceLetter.equals("P")) {
					pieceLetter="";
				}
				toLog=pieceLetter+Board.pointToNotation(b.lastMoveStart())+" "+capture+Board.pointToNotation(b.lastMoveEnd())+promotePiece+" / ";
			} else {
				toLog=Board.pointToNotation(b.lastMoveStart())+" ("+Board.pointToNotation(b.getGhost())+") "+Board.pointToNotation(b.lastMoveEnd())+promotePiece+" / ";
			}
		} else {
			if (b.getGhost()==null) {
				String pieceLetter=String.valueOf(b.lastPieceTypeMoved()).toUpperCase();
				if (pieceLetter.equals("P")) {
					pieceLetter="";
				}
				toLog=pieceLetter+Board.pointToNotation(b.lastMoveStart())+" "+capture+Board.pointToNotation(b.lastMoveEnd())+promotePiece+"\n";
			} else {
				toLog=Board.pointToNotation(b.lastMoveStart())+" ("+Board.pointToNotation(b.getGhost())+") "+Board.pointToNotation(b.lastMoveEnd())+promotePiece+"\n";
			}
		}
		gameLog+=toLog;
		Console.s.print(toLog);
		promotePiece="";
	}
	
	public void setInit(){
		
		state=STATE.detectMate;
		checked=false;
		whiteTurn=true;
		firstMove=true;
		this.online=observer.isOnline();
		b.setUp();
		gameLog="";
		//b.experimentSetUp();
		if (observer.clocks()) {
			whiteTime=observer.getClockTime();
			blackTime=observer.getClockTime();
			bonus=observer.getBonus();
			delay=observer.getDelay();
			whiteDelay=delay;
			blackDelay=delay;
			clocks=true;
		} else {
			clocks=false;
		}
		this.ai=observer.ai();
		this.clocks=observer.clocks();
		if (ai) {
			oppColor=(1==Main.rand.nextInt(2));
			theAi.setColor(oppColor);
			wPersp=!oppColor;
			if (oppColor) {
				gameLog+="[White : AI]\n"
						+ "[Black : You]\n";
				Console.s.println("[White : AI]");
				Console.s.println("[Black : You]");
			} else {
				gameLog+="[White : You]\n"
						+ "[Black : AI]\n";
				Console.s.println("[White : You]");
				Console.s.println("[Black : AI]");
			}
		} else if (online){
			wPersp=!oppColor;
			if (oppColor) {
				gameLog+="[White : Opponent]\n"
						+ "[Black : You]\n";
				Console.s.println("[White : Opponent]");
				Console.s.println("[Black : You]");
			} else {
				gameLog+="[White : You]\n"
						+ "[Black : Opponent]\n";
				Console.s.println("[White : You]");
				Console.s.println("[Black : Opponent]");
			}
		} else {
			wPersp=true;
			gameLog+="[White : A Local Human... probably]\n"
					+ "[Black : Another Local Human... probably]\n";
			Console.s.println("[White : A Local Human... probably]");
			Console.s.println("[Black : Another Local Human... probably]");
		}
		state=STATE.move;
	}
	
	public Board getBoard() {
		return this.b;
	}
	
	public void submit() {
		
		state=STATE.detectMate;
		char promoteChar='x';
		if (promotePiece.length()>=1) {
			if (whiteTurn) {
				promoteChar=promotePiece.toUpperCase().charAt(0);
			} else {
				promoteChar=promotePiece.toLowerCase().charAt(0);
			}
		}
		recNotation();
		if (sounds) {
			Main.sounds.playSound("endturn.wav", 0);
		}
		if (firstMove) {
			if (!whiteTurn) {
				firstMove=false;
				timeIndex=System.currentTimeMillis();
			}
		} else if (clocks){
			long oldIndex=timeIndex;
			timeIndex=System.currentTimeMillis();
			
			//update clock last time before turning over to the next player, apply bonus, and reset delay
			if (whiteTurn) {
				if (whiteDelay>0) {
					whiteDelay-=timeIndex-oldIndex;
					if (whiteDelay<0) {
						whiteTime=whiteTime+whiteDelay;//reduce the main time by the amount of time that the delay went over
						whiteDelay=0;
					}
				} else {
					whiteTime-=timeIndex-oldIndex;
				}
				whiteTime=whiteTime+bonus;
				whiteDelay=delay;
			} else {
				if (blackDelay>0) {
					blackDelay-=timeIndex-oldIndex;
					if (blackDelay<0) {
						blackTime=blackTime+blackDelay;//reduce the main time by the amount of time that the delay went over
						blackDelay=0;
					}
				} else {
					blackTime-=timeIndex-oldIndex;
				}
				blackTime=blackTime+bonus;
				blackDelay=delay;
			}
			timeIndex=System.currentTimeMillis();
			
		}
		
		whiteTurn=!whiteTurn;
		if (online&&whiteTurn==oppColor) {
			
			if (b.getGhost()==null) {
				
				observer.submitOnlineMove(new Point[] {b.lastMoveStart(),b.lastMoveEnd()},promoteChar);
			} else {
				observer.submitOnlineMove(new Point[] {b.lastMoveStart(),b.lastMoveEnd(),b.getGhost()},promoteChar);
			}
			
		}
		
		checked=b.playerInCheck(whiteTurn);
		if (!online||whiteTurn==!oppColor){
			boolean legalMove=b.playerHasLegalMove(whiteTurn);
			if (checked) {
				checkingMove=b.checkingMove(whiteTurn);
			}
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
				if (!(ai||online)) {
					wPersp=!wPersp;
				}
			}
		}
	}
	
	public void makeMove(Point[] p,char promote) {
		observer.getBoard().makeMove(p,promote);
		submit();
	}
	
	public void setOppColor(boolean white) {
		oppColor=white;
	}
	
	public void confirm(long oppTime) {
		boolean legalMove=b.playerHasLegalMove(whiteTurn);
		if (checked) {
			checkingMove=b.checkingMove(whiteTurn);
		}
		if ((!legalMove)&&checked) {
			if (whiteTurn) {
				state=STATE.blackWins;
			} else {
				state=STATE.whiteWins;
			}
		} else if (!legalMove) {
			state=STATE.draw;
		} else {
			if (whiteTurn) {
				whiteTime=oppTime+500;
			}else {
				blackTime=oppTime+500;
			}
			state=STATE.move;
		}
	}
	public long getWhiteTime() {
		return whiteTime;
	}
	
	public long getBlackTime() {
		return blackTime;
	}
	
	public void disconnect() {
		Main.err.println("Opponent dissconnected from game!");
		if (oppColor) {
			state=STATE.blackWins;
		} else {
			state=STATE.whiteWins;
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		if (board.contains(e.getPoint())) {
			Point boardPoint=screenToBoard(e.getX(),e.getY());
			if (boardPoint!=null&&Board.contains(boardPoint)) {
				space=Board.pointToNotation(boardPoint);
			}
			if (lastButtonPressed==3&&bezties.size()>0) {
				java.awt.Point mPoint=e.getPoint();
				BezierCurve bez=bezties.get(bezties.size()-1);
				CopyOnWriteArrayList<Point> points=bez.getPoints();
				Point intermed=points.get(0).lerp(points.get(2), .5);
				double dx=points.get(2).tuple.i(0)-points.get(0).tuple.i(0);
				double dy=points.get(2).tuple.i(1)-points.get(0).tuple.i(1);
				double y=intermed.tuple.i(1)-dy/2+dx;
				if (y<board.y) {
					y=board.y;
				}
				bez.setPoint(new Point(new double[] {mPoint.x,mPoint.y}),2);
				bez.setPoint(new Point(new double[] {intermed.tuple.i(0),y}),1);
			}
		}
		
	}

	@Override
	protected void keyDown(int key) {
		// TODO Auto-generated method stub
		
	}
	
	public void importGame(File f) {
		//file chooser to select a file
		
		try {
			analyzeMoves=b.notationToMoves(Files.readString(f.toPath(),StandardCharsets.UTF_8));
			move=0;
			checkingMove=null;
			capture=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state=STATE.analyze;
	}
	
	public void importGame() {
		importGame(Main.openFile());
	}
}
