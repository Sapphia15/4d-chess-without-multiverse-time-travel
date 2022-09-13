package unicorns.screens;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;

import graphics.screen.Screen;
import unicorns.Assets;
import unicorns.Main;
import unicorns.MultiplyComposite;
import unicorns.Panel;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

public class TutorialSelect extends Screen {
	
	int oldHeight=0;
	int oldWidth=0;
	Hashtable<Rectangle,String> chapters =new Hashtable<>();
	Panel observer;
	Rectangle hover=null;
	
	public TutorialSelect(Panel p) {
		this.observer=p;
		chapters.put(new Rectangle(140,100,600,32), "Intro To 4D");
		chapters.put(new Rectangle(140,164,600,32), "How To Play 4D Chess Without Multiverse Time Travel");
		//hover=new Rectangle(40,100,360,32);
	}
	
	
	@Override
	public void paint(Graphics g) {
		g.setColor(new Color(100,0,100));
		g.fillRect(0, 0, oldWidth, oldHeight);
		drawScene();
		g.drawImage(observer.scene,observer.drawSceneX,0,observer.drawSceneWidth,oldHeight,null);
		
	}
	
	public void drawScene() {
		observer.initScene();
		
		Graphics2D g=observer.scene.createGraphics();
		g.setFont(g.getFont().deriveFont(32f));
		g.setColor(Color.yellow);
		g.drawString("Tutorial Selection Screen",observer.smidX-100,64);
		g.setFont(g.getFont().deriveFont(16f));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		for (Rectangle r:chapters.keySet()) {
			
			g.drawImage(Assets.LABLE, r.x, r.y,120,32, null);
			g.drawImage(Assets.LABLE_CONTINUE, r.x+120, r.y,120,32, null);
			g.drawImage(Assets.LABLE_CONTINUE, r.x+240, r.y,120,32, null);
			g.drawImage(Assets.LABLE_CONTINUE, r.x+360, r.y,120,32, null);
			g.drawImage(Assets.LABLE_CONTINUE, r.x+480, r.y,120,32, null);
			if (hover==r) {
				g.setComposite(MultiplyComposite.Multiply);
				g.setColor(new Color(255,255,0,1));
				g.fillRect(r.x, r.y, r.width, r.height);
			}
			g.setColor(Color.white);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			g.drawString(chapters.get(r),r.x+22,r.y+22);
		}
	}

	@Override
	public void update() {
		if (oldWidth!=observer.getWidth()||oldHeight!=observer.getHeight()) {
			oldWidth=observer.getWidth();
			oldHeight=observer.getHeight();
			observer.drawSceneWidth=(int)(oldHeight*observer.ratio);
			observer.drawSceneX=(oldWidth-observer.drawSceneWidth)/2;
		}
		
	}

	@Override
	public void setInit() {
		observer.setSong("tutorial.wav");
		Main.f.setExtendedState(Frame.MAXIMIZED_BOTH);
		
	}
	
	@Override
	protected void keyDown(int key) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_BACK_SLASH) {
			Main.cmd.setVisible(true);
		}  else if (e.getKeyCode()==KeyEvent.VK_M) {
			observer.toggleMusic();
		}  else if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
			observer.setScreen("title");
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (hover!=null) {
			observer.setScreen(chapters.get(hover));
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		java.awt.Point p=observer.convert(e.getPoint());
		boolean chapterHovering=false;
		for (Rectangle r:chapters.keySet()) {
			if (r.contains(p)) {
				hover=r;
				chapterHovering=true;
				break;
			}
		}
		if (!chapterHovering) {
			hover=null;
		}
		
	}

}
