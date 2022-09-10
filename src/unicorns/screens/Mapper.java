package unicorns.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameutil.math.geom.Vector;
import graphics.screen.Screen;
import unicorns.Assets;
import unicorns.Main;
import unicorns.Panel;

public class Mapper extends Screen {
	
	Panel observer;
	
	int oldWidth=0;
	int oldHeight=0;
	final int sWidth=1360;
	final int sHeight=768;
	final int midX=sWidth/2;
	final int midY=sHeight/2;
	final int maxTranslate=612;
	boolean letterDisplay=true;	
	int drawSceneWidth=sWidth;
	int drawSceneX=0;
	final double ratio=((double)sWidth)/sHeight;
	
	BufferedImage scene;
	
	int x=0;
	int y=0;
	int z=0;
	
	int r=0;
	int g=0;
	int b=0;
	
	ArrayList<Vector> points=new ArrayList<>();
	Vector pointer=new Vector(new double[] {0,0,0});
	
	public Mapper(Panel p) {
		observer=p;
		initScene();
		points.add(new Vector(new double[] {10,10,3}));
	}
	
	
	@Override
	public void paint(Graphics g) {
		scene.flush();
		initScene();
		paintScene(scene.createGraphics());
		//} catch (NullPointerException e) {
		//	initScene();
		//}
		
		int width=(int)(oldHeight*ratio);
		g.drawImage(scene,(oldWidth-width)/2, 0,width,oldHeight, null);
	}
	
	public void paintScene(Graphics2D g) {
		g.setColor(new Color(100,20,100));
		g.fillRect(0, 0, midX, sHeight);
		g.setColor(Color.pink);
		g.fillRect(midX, 0, midX, sHeight);
		g.drawImage(Assets.UNICORN_B,midX/2-8,midY-8,16,16,null);
		g.drawImage(Assets.UNICORN_B,midX+midX/2-8,midY-8,16,16,null);
		g.setColor(Color.black);
		g.fillRoundRect(5, sHeight-40, 40, 40, 20, 20);
		g.setColor(new Color(255-r,255-this.g,255-b));
		g.fillRoundRect(9, sHeight-36, 32, 32, 20, 20);
		g.setColor(new Color(r,this.g,b));
		g.fillOval(17, sHeight-28, 16, 16);
		
		g.setColor(Color.black);
		g.setFont(g.getFont().deriveFont(40f));
		
		for (Vector v:points) {
			int x=(int)v.getSpds().i(0);
			int y=(int)v.getSpds().i(1);
			int z=(int)v.getSpds().i(2);
			
			int A=z-this.z+midX/2;
			int N=this.y-y+midY;
			int E=x-this.x+midX+midX/2;
			if (A>-1&&A<midX+1 && N>-1&&N<sHeight+1 && E>midX&&E<sWidth+1) {
				g.setColor(Color.black);
				g.fillOval(A-6, N-6, 13, 13);
				g.fillOval(E-6, N-6, 13, 13);
				g.setColor(new Color((int)v.getSpds().i(3),(int)v.getSpds().i(4),(int)v.getSpds().i(5)));
				g.fillOval(A-4, N-4, 9, 9);
				g.fillOval(E-4, N-4, 9, 9);
			}
		}
		g.setColor(Color.black);
		
		g.drawString("("+x+", "+y+", "+z+")", 5, 40);
		
		if (letterDisplay) {
			int hsouthWidth=(int)g.getFontMetrics().getStringBounds("S", g).getWidth()/2;
			g.drawString("N", midX/2-hsouthWidth, 50);
			g.drawString("N", midX+midX/2-hsouthWidth, 50);
			
			int hnorthWidth=(int)g.getFontMetrics().getStringBounds("N", g).getWidth()/2;
			g.drawString("S", midX/2-hnorthWidth, sHeight-20);
			g.drawString("S", midX+midX/2-hnorthWidth, sHeight-20);
			
			g.drawString("K",10,midY+40);

			
			g.drawString("A",midX-40,midY+40);
			
			g.drawString("W",midX+10,midY+40);
			
			g.drawString("E",sWidth-40,midY+40);
		} else {
			int hsouthWidth=(int)g.getFontMetrics().getStringBounds("North", g).getWidth()/2;
			g.drawString("North", midX/2-hsouthWidth, 50);
			g.drawString("North", midX+midX/2-hsouthWidth, 50);
			
			int hnorthWidth=(int)g.getFontMetrics().getStringBounds("South", g).getWidth()/2;
			g.drawString("South", midX/2-hnorthWidth, sHeight-20);
			g.drawString("South", midX+midX/2-hnorthWidth, sHeight-20);
			
			g.drawString("K",10,midY-40);
			g.drawString("e",10,midY);
			g.drawString("n",10,midY+40);
			g.drawString("t",10,midY+80);
			g.drawString("h",10,midY+120);
			
			g.drawString("A",midX-40,midY-30);
			g.drawString("n",midX-40,midY+10);
			g.drawString("t",midX-40,midY+50);
			g.drawString("h",midX-40,midY+90);
			
			g.drawString("W",midX+10,midY-30);
			g.drawString("e",midX+10,midY+10);
			g.drawString("s",midX+10,midY+50);
			g.drawString("t",midX+10,midY+90);
			
			g.drawString("E",sWidth-40,midY-30);
			g.drawString("a",sWidth-40,midY+10);
			g.drawString("s",sWidth-40,midY+50);
			g.drawString("t",sWidth-40,midY+90);
		}
	}
	
	public void initScene(){
		scene=new BufferedImage(sWidth,sHeight, BufferedImage.TYPE_INT_ARGB);
		//g.setBackground(new Color(255,255,255,0));
	}

	@Override
	public void update() {
		if (oldWidth!=observer.getWidth()||oldHeight!=observer.getHeight()) {
			oldWidth=observer.getWidth();
			oldHeight=observer.getHeight();
			drawSceneWidth=(int)(oldHeight*ratio);
			drawSceneX=(oldWidth-drawSceneWidth)/2;
		}
	}
	
	public void setInit() {
		observer.setSong("tutorial.wav");
		Main.f.setExtendedState(Frame.MAXIMIZED_BOTH);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_SPACE) {
			points.add(new Vector(new double[] {x,y,z,r,g,b}));
		} else if (e.getKeyCode()==KeyEvent.VK_R) {
			r+=1;
			if (r>255) {
				r=0;
			}
		} else if (e.getKeyCode()==KeyEvent.VK_G) {
			g+=1;
			if (g>255) {
				g=0;
			}
		} else if (e.getKeyCode()==KeyEvent.VK_B) {
			b+=1;
			if (b>255) {
				b=0;
			}
		} else if (e.getKeyCode()==KeyEvent.VK_L) {
			letterDisplay=!letterDisplay;
		} else if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
			observer.setScreen("title");
		}
	}
	
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	protected void keyDown(int key) {
		if (key==KeyEvent.VK_D) {
			x+=1;
		} else if (key==KeyEvent.VK_A) {
			x-=1;
		} else if (key==KeyEvent.VK_W) {
			y+=1;
		} else if (key==KeyEvent.VK_S) {
			y-=1;
		}  else if (key==KeyEvent.VK_E) {
			z+=1;
		} else if (key==KeyEvent.VK_Q) {
			z-=1;
		}
		
	}

}
