package unicorns.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import graphics.screen.Screen;
import unicorns.Panel;

public class Title extends Screen{
	Panel observer;
	Rectangle human;
	Rectangle ai;
	Rectangle exit;
	int oldWidth=0;
	int oldHeight=0;
	
	public Title(Panel observer) {
		this.observer=observer;
		int midX=observer.getWidth()/2;
		int midY=observer.getHeight()/2;
		human=new Rectangle(midX-100,midY-200,200,100);
		ai=new Rectangle(midX-100,midY-50,200,100);
		exit=new Rectangle(midX-100,midY+100,200,100);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRoundRect(human.x, human.y, human.width, human.height, 20, 20);
		g.fillRoundRect(ai.x, ai.y, ai.width, ai.height, 20, 20);
		g.fillRoundRect(exit.x, exit.y, exit.width, exit.height, 20, 20);
		g.setColor(Color.white);
		Font newFont = g.getFont().deriveFont((float)25);
		g.setFont(newFont);
		g.drawString("Human",(int) human.getCenterX()-50,(int)human.getCenterY());
		g.drawString("AI",(int) ai.getCenterX()-50,(int)ai.getCenterY());
		g.drawString("Exit",(int) exit.getCenterX()-50,(int)exit.getCenterY());
	}

	@Override
	public void update() {
		if (oldWidth!=observer.getWidth()||oldHeight!=observer.getHeight()) {
			oldWidth=observer.getWidth();
			oldHeight=observer.getHeight();
			int midX=oldWidth/2;
			int midY=oldHeight/2;
			human=new Rectangle(midX-100,midY-200,200,100);
			ai=new Rectangle(midX-100,midY-50,200,100);
			exit=new Rectangle(midX-100,midY+100,200,100);
		}
		
	}
	
	public void keyPressed(KeyEvent e) {
		
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
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		
	}

}
