package unicorns.screens;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import gameutil.math.geom.Point;
import gameutil.math.geom.Tuple;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentHashMap;

import graphics.screen.Screen;
import unicorns.Assets;
import unicorns.Board;
import unicorns.Main;
import unicorns.Panel;
import unicorns.Piece;
import unicorns.Textbox;
import unicorns.screens.Game.STATE;

public class TutorialScreen extends Screen{
	Panel observer;
	int oldWidth=0;
	int oldHeight=0;
	int midX=0;
	int midY=0;

	final int maxTranslate=612;
	final double ratio=((double)observer.sWidth)/observer.sHeight;
	int drawSceneWidth=observer.sWidth;
	int drawSceneX=0;
	static Textbox t=new Textbox("Dee");
	long time=System.currentTimeMillis();
	long dt=0;
	static int x;
	static int y;
	static int z;
	static int w;
	
	static int sq=44;
	static int gap=16;
	static int gap3d4=12;
	static int offY=0;
	static int sq4=176;
	
	boolean simple=true;
	
	
	Game.STATE state=Game.STATE.move;
	static Board b=new Board();
	static ConcurrentHashMap<Rectangle,Point> rects=new ConcurrentHashMap<>();
	int bound=15;
	
	
	protected String[] dialogue;
	
	static int step=0;
	
	public TutorialScreen(Panel observer,String[] dialogue) {
		this.observer=observer;
		this.dialogue=dialogue;
		int midX=observer.getWidth()/2;
		int midY=observer.getHeight()/2;
		t.setText(this.dialogue[step]);
		initScene();
		t.setPos(observer.smidX-300,observer.sHeight-205);
		
		
	}
	
	public static void initRects() {
		for (int i=0; i<4;i++) {
			for (int j=0; j<4;j++) {
				for (int k=0; k<4;k++) {
					for (int n=0; n<4;n++) {
						int x=i;
						int y=j;
						int z=k;
						int w=n;
						//if (wPersp) {
							y=3-y;
							w=3-w;
						/*} else {
							x=3-x;
							z=3-z;
						}*/
						
						
						
						rects.put(new Rectangle(Panel.smidX-384+x*sq+z*(gap3d4+sq4)+gap,offY+y*sq+gap+w*(gap3d4+sq4),sq-gap/4,sq-gap/4),new Point(new Tuple(new double[] {i,j,k,n})));
					}
				}
			}
		}
	}
	
	public void initScene(){
		observer.initScene();
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(new Color(170,70,180));
		g.fillRect(0, 0, oldWidth, oldHeight);
		//try {
		initScene();
		paintScene(observer.scene.createGraphics());
		//} catch (NullPointerException e) {
		//	initScene();
		//}
		int width=(int)(oldHeight*ratio);
		g.drawImage(observer.scene,(oldWidth-width)/2, 0,width,oldHeight, null);
	}
	
	public void paintScene(Graphics2D g) {
		g.setFont(g.getFont().deriveFont(16f));
		
		
		
	}
	
	public void drawTextbox(Graphics2D g) {
		if (t.getY()==observer.sHeight-40) {
			float opacity = 0.3f;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		}
		t.setGraphics(g);
		g.setColor(Color.white);
		t.draw();
	}
	
	public void drawBoard(Graphics g) {
		Image board=Assets.BOARD_S;
		if (!simple){
			board=Assets.BOARD;
		}
		g.drawImage(board,observer.smidX-384,0,768,768,null);
		
	}
	
	public void drawBoardAndPieces(Graphics g) {
		drawBoard(g);
		if (b.getGhost()!=null) {
			Image ghost=null;
			if (b.getGhostPiece().isWhite()) {
				ghost=Assets.GHOST_W;
			} else {
				ghost=Assets.GHOST_B;
			}
			g.drawImage(ghost, toScreenX((int)b.getGhost().tuple.i(0),(int)b.getGhost().tuple.i(2)), toScreenY((int)b.getGhost().tuple.i(1),(int)b.getGhost().tuple.i(3)), null);
		}
		for (Piece p:b.getPieces()) {
			g.drawImage(p.getImage(), toScreenX(p.getX(),p.getZ()), toScreenY(p.getY(),p.getW()), null);
		}
		for (Point p : b.moveableSpaces()) {
			int x;
			int y;
			int z;
			int w;
			
			x=(int)p.tuple.i(0);
			
			z=(int)p.tuple.i(2);
		
				
			y=(int)p.tuple.i(1);
			
			w=(int)p.tuple.i(3);
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
			
			y=3-y;
			w=3-w;
			
			
			
			g.fillRect(observer.smidX-384+x*sq+z*(gap3d4+sq4)+gap,offY+y*sq+gap+w*(gap3d4+sq4),sq-gap/4,sq-gap/4);
		}
	}
	
	public int toScreenX(int x,int z) {
		return observer.smidX-384+x*44+z*(188)+18;
	}
	
	public int toScreenY(int y,int w) {
		return 768-y*44-16-w*(188)-34;
	}
	
	public Point fromScreen(int x,int y) {
		for (Rectangle r:rects.keySet()) {
			//Console.s.println("lookin through rects");
			if (r.contains(x,y)) {
				//rects.get(r).printVals("click");
				return (rects.get(r));
				
			}
		}
		return new Point(new Tuple(new double[] {-1,-1,-1,-1}));
	}

	@Override
	public void update() {
		if (oldWidth!=observer.getWidth()||oldHeight!=observer.getHeight()) {
			oldWidth=observer.getWidth();
			oldHeight=observer.getHeight();
			midX=oldWidth/2;
			midY=oldHeight/2;
			drawSceneWidth=(int)(oldHeight*ratio);
			drawSceneX=(oldWidth-drawSceneWidth)/2;
		}
		dt=System.currentTimeMillis()-time;
	}
	
	public java.awt.Point convert(java.awt.Point p) {
		return new java.awt.Point((int)(((double)(p.x-drawSceneX)/drawSceneWidth)*observer.sWidth),(int)((double)(p.y)/oldHeight*observer.sHeight));
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_BACK_SLASH) {
			Main.cmd.setVisible(true);
		}  else if (e.getKeyCode()==KeyEvent.VK_M) {
			observer.toggleMusic();
		}  else if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
			observer.setScreen("tutorial");
		}
	}
	
	public void mousePressed(MouseEvent e) {
		java.awt.Point p=convert(e.getPoint());
		if (t.contains(p)) {
			if (e.getButton()==MouseEvent.BUTTON1) {
				if (step<dialogue.length-1) {
					step++;
					t.setText(dialogue[step]);
					dialogueStep(step);
					
				} else {
					observer.setScreen("tutorial");
				}
			} else if (e.getButton()==MouseEvent.BUTTON3) {
				if (step>0) {
					step--;
					t.setText(dialogue[step]);
					dialogueStepBack(step);
				}
			}
		}
	}
	
	public void dialogueStep(int step) {
		
	}
	
	public void dialogueStepBack(int step) {
		
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
	
	public void mouseMoved(MouseEvent e) {
		java.awt.Point p=convert(e.getPoint());
		if (t.contains(p)) {
			t.setPos(observer.smidX-300,observer.sHeight-205);
		} else {
			t.setPos(observer.smidX-300,observer.sHeight-40);
		}
	}
	
	@Override
	public void setInit() {
		observer.setSong("tutorial.wav");
		Main.f.setExtendedState(Frame.MAXIMIZED_BOTH);
		time=System.currentTimeMillis();
		step=0;
		t.setText(dialogue[step]);
	}

	@Override
	protected void keyDown(int key) {
		// TODO Auto-generated method stub
		
	}
}

