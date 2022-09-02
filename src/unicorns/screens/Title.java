package unicorns.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import graphics.screen.Screen;
import unicorns.Assets;
import unicorns.Main;
import unicorns.Panel;

public class Title extends Screen{
	Panel observer;
	Rectangle human;
	Rectangle ai;
	Rectangle exit;
	Rectangle clocks;
	Rectangle online;
	Rectangle tutorial;
	Rectangle hyperVox;
	int oldWidth=0;
	int oldHeight=0;
	
	public Title(Panel observer) {
		this.observer=observer;
		int midX=observer.getWidth()/2;
		int midY=observer.getHeight()/2;
		human=new Rectangle(midX-100,midY-200,200,100);
		ai=new Rectangle(midX-100,midY-50,200,100);
		exit=new Rectangle(midX-100,midY+100,200,100);
		clocks=new Rectangle(10,10,100,50);
		online=new Rectangle(midX-100,midY-350,200,100);
		tutorial=new Rectangle(midX+150,midY-75,200,100);
		hyperVox=new Rectangle(10,midY*2-64,64,64);
		
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRoundRect(human.x, human.y, human.width, human.height, 20, 20);
		g.fillRoundRect(ai.x, ai.y, ai.width, ai.height, 20, 20);
		g.fillRoundRect(exit.x, exit.y, exit.width, exit.height, 20, 20);
		g.fillRoundRect(online.x, online.y, online.width, online.height, 20, 20);
		g.fillRoundRect(tutorial.x, tutorial.y, tutorial.width, tutorial.height, 20, 20);
		if (observer.clocks()) {
			g.setColor(Color.green);
		} else {
			g.setColor(Color.red);
		}
		g.fillRoundRect(clocks.x, clocks.y, clocks.width, clocks.height, 20, 20);
		g.setColor(Color.white);
		Font newFont = g.getFont().deriveFont((float)25);
		g.setFont(newFont);
		g.drawString("Human",(int) human.getCenterX()-50,(int)human.getCenterY());
		g.drawString("AI",(int) ai.getCenterX()-50,(int)ai.getCenterY());
		g.drawString("Exit",(int) exit.getCenterX()-50,(int)exit.getCenterY());
		g.drawString("Online",(int) online.getCenterX()-50,(int)online.getCenterY());
		g.drawString("Tutorial",(int) tutorial.getCenterX()-50,(int)tutorial.getCenterY());
		g.setColor(Color.black);
		g.drawString("Clocks",(int) clocks.getCenterX()-(int)g.getFontMetrics().getStringBounds("Clocks", g).getWidth()/2,(int)clocks.getCenterY()+(int)g.getFontMetrics().getStringBounds("Clocks", g).getHeight()/4);
		g.drawImage(Assets.UNICORN_B, hyperVox.x, hyperVox.y, hyperVox.width, hyperVox.height, observer);
	}

	@Override
	public void update() {
		if (oldWidth!=observer.getWidth()||oldHeight!=observer.getHeight()) {
			oldWidth=observer.getWidth();
			oldHeight=observer.getHeight();
			int midX=oldWidth/2;
			int midY=oldHeight/2;
			
			human=new Rectangle(midX-100,midY-150,200,100);
			ai=new Rectangle(midX-100,midY,200,100);
			exit=new Rectangle(midX-100,midY+150,200,100);
			online=new Rectangle(midX-100,midY-300,200,100);
			tutorial=new Rectangle(midX+150,midY-75,200,100);
			hyperVox=new Rectangle(10,midY*2-64,64,64);
			
		}
		
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_BACK_SLASH) {
			Main.cmd.setVisible(true);
		}  else if (e.getKeyCode()==KeyEvent.VK_M) {
			observer.toggleMusic();
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if (human.contains(e.getPoint())) {
			observer.setAi(false);
			observer.setScreen("game");
		} else if (ai.contains(e.getPoint())) {
			observer.setAi(true);
			observer.setScreen("game");
		} else if (exit.contains(e.getPoint())) {
			System.exit(0);
		} else if (clocks.contains(e.getPoint())) {
			observer.setClocks(!observer.clocks());
		} else if (online.contains(e.getPoint())) {
			observer.setScreen("online");
		} else if (hyperVox.contains(e.getPoint())) {
			observer.setScreen("hyperVox");
		} else if (tutorial.contains(e.getPoint())) {
			observer.setScreen("tutorial");
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		
	}
	
	@Override
	public void setInit() {
		observer.setSong("For_Dee.wav");
	}

	@Override
	protected void keyDown(int key) {
		// TODO Auto-generated method stub
		
	}

}
