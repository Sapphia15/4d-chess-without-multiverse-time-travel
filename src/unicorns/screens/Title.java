package unicorns.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import gameutil.text.Iru.Letter;
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
	Rectangle map;
	Rectangle hyperVox;
	Rectangle analyze;
	Rectangle lessTime;
	Rectangle moreTime;
	Rectangle lessDelay;
	Rectangle moreDelay;
	Rectangle lessBonus;
	Rectangle moreBonus;
	Rectangle nextVariant;
	Rectangle lastVariant;
	int oldWidth=0;
	int oldHeight=0;
	String[] variants={"standard","all pawns are queens"};
	int variantIndex=0;
	
	public Title(Panel observer) {
		this.observer=observer;
		int midX=observer.getWidth()/2;
		int midY=observer.getHeight()/2;/*
		human=new Rectangle(midX-100,midY-200,200,100);
		ai=new Rectangle(midX-100,midY-50,200,100);
		exit=new Rectangle(midX-100,midY+100,200,100);
		clocks=new Rectangle(10,10,100,50);
		online=new Rectangle(midX-100,midY-350,200,100);
		tutorial=new Rectangle(midX+150,midY-75,200,100);
		hyperVox=new Rectangle(10,midY*2-64,64,64);
		map=new Rectangle(midX+150,midY+75,200,100);
		*/
		
		human=new Rectangle(midX-100,midY-200,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
		ai=new Rectangle(midX-100,midY-50,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
		exit=new Rectangle(midX-100,midY+100,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
		
		
		clocks=new Rectangle(10,10,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
		lessTime=new Rectangle(100,Assets.BUTTON_HEIGHT+30,16,16);
		moreTime=new Rectangle(226,Assets.BUTTON_HEIGHT+30,16,16);
		lessDelay=new Rectangle(100,Assets.BUTTON_HEIGHT+30+16*2,16,16);
		moreDelay=new Rectangle(226,Assets.BUTTON_HEIGHT+30+16*2,16,16);
		lessBonus=new Rectangle(100,Assets.BUTTON_HEIGHT+30+16*4,16,16);
		moreBonus=new Rectangle(226,Assets.BUTTON_HEIGHT+30+16*4,16,16);
		lastVariant=new Rectangle(100,Assets.BUTTON_HEIGHT+30+16*6,16,16);
		nextVariant=new Rectangle(130,Assets.BUTTON_HEIGHT+30+16*6,16,16);
		
		
		online=new Rectangle(midX-100,midY-350,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
		tutorial=new Rectangle(midX+150,midY-75,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
		hyperVox=new Rectangle(10,midY*2-64,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
		map=new Rectangle(midX+150,midY+75,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
		analyze=new Rectangle(midX+150,midY-200,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
		
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		/*
		g.fillRoundRect(human.x, human.y, human.width, human.height, 20, 20);
		g.fillRoundRect(ai.x, ai.y, ai.width, ai.height, 20, 20);
		g.fillRoundRect(exit.x, exit.y, exit.width, exit.height, 20, 20);
		g.fillRoundRect(online.x, online.y, online.width, online.height, 20, 20);
		g.fillRoundRect(tutorial.x, tutorial.y, tutorial.width, tutorial.height, 20, 20);
		g.fillRoundRect(map.x, map.y, map.width, map.height, 20, 20);
		*/
		
		if (observer.clocks()) {
			g.drawImage(Assets.CLOCKS_ON_BUTTON,(int) clocks.x,(int)clocks.y,clocks.width,clocks.height,null);
			//g.setColor(Color.green);
		} else {
			g.drawImage(Assets.CLOCKS_OFF_BUTTON,(int) clocks.x,(int)clocks.y,clocks.width,clocks.height,null);
			//g.setColor(Color.red);
		}
		g.drawImage(Letter.LESS.img16(),lessTime.x,lessTime.y,null);
		g.drawImage(Letter.LESS.img16(),lessDelay.x,lessDelay.y,null);
		g.drawImage(Letter.LESS.img16(),lessBonus.x,lessBonus.y,null);
		g.drawImage(Letter.LESS.img16(),lastVariant.x,lastVariant.y,null);
		
		g.drawImage(Letter.MORE.img16(),moreTime.x,moreTime.y,null);
		g.drawImage(Letter.MORE.img16(),moreDelay.x,moreDelay.y,null);
		g.drawImage(Letter.MORE.img16(),moreBonus.x,moreBonus.y,null);		
		g.drawImage(Letter.MORE.img16(),nextVariant.x,nextVariant.y,null);
		
		Font newFont = g.getFont().deriveFont((16f));
		g.setFont(newFont);
		String clockTime=String.format("%01d", (int)Math.floor(observer.getClockTime()/60000))+":"+String.format("%01d",(int)Math.floor(observer.getClockTime()/1000)%60);
		int width=(int)(g.getFontMetrics().getStringBounds(clockTime, g).getWidth()/2);
		g.drawString(clockTime, lessTime.x+65-width, lessTime.y+15);
		String delayTime=String.format("%01d",(int)Math.floor(observer.getDelay()/1000));
		width=(int)(g.getFontMetrics().getStringBounds(delayTime, g).getWidth()/2);
		g.drawString(delayTime, lessDelay.x+65-width, lessDelay.y+15);
		String bonusTime=String.format("%01d",(int)Math.floor(observer.getBonus()/1000));
		width=(int)(g.getFontMetrics().getStringBounds(bonusTime, g).getWidth()/2);
		g.drawString(bonusTime, lessBonus.x+65-width, lessBonus.y+15);
		
		g.drawString("Clock Time", 10, lessTime.y+15);
		
		g.drawString("Delay (s)", 10, lessDelay.y+15);
	
		g.drawString("Bonus (s)", 10, lessBonus.y+15);
		
		g.drawString("Variant", 10, lastVariant.y+15);
		
		g.drawString(observer.getVariant(), nextVariant.x+30, nextVariant.y+15);
		
		
		//g.fillRoundRect(clocks.x, clocks.y, clocks.width, clocks.height, 20, 20);
		g.setColor(Color.white);
		newFont = g.getFont().deriveFont((float)25);
		g.setFont(newFont);
		g.drawImage(Assets.HUMAN_BUTTON,(int) human.x,(int)human.y,human.width,human.height,null);
		g.drawImage(Assets.AI_BUTTON,(int) ai.x,(int)ai.y,ai.width,ai.height,null);
		g.drawImage(Assets.EXIT_BUTTON,(int) exit.x,(int)exit.y,exit.width,exit.height,null);
		g.drawImage(Assets.ONLINE_BUTTON,(int) online.x,(int)online.y,online.width,online.height,null);
		g.drawImage(Assets.TUTORIAL_BUTTON,(int) tutorial.x,(int)tutorial.y,tutorial.width,tutorial.height,null);
		g.drawImage(Assets.ANALYZE_BUTTON,(int) analyze.x,(int)analyze.y,analyze.width,analyze.height,null);
		g.drawImage(Assets.MAP_BUTTON,(int) map.x,(int)map.y,map.width,map.height,null);
		
		/*g.drawString("Human",(int) human.getCenterX()-50,(int)human.getCenterY());
		g.drawString("AI",(int) ai.getCenterX()-50,(int)ai.getCenterY());
		g.drawString("Exit",(int) exit.getCenterX()-50,(int)exit.getCenterY());
		g.drawString("Online",(int) online.getCenterX()-50,(int)online.getCenterY());
		g.drawString("Tutorial",(int) tutorial.getCenterX()-50,(int)tutorial.getCenterY());
		g.drawString("Map Maker",(int) map.getCenterX()-50,(int)map.getCenterY());*/
		//g.setColor(Color.black);
		//g.drawString("Clocks",(int) clocks.getCenterX()-(int)g.getFontMetrics().getStringBounds("Clocks", g).getWidth()/2,(int)clocks.getCenterY()+(int)g.getFontMetrics().getStringBounds("Clocks", g).getHeight()/4);
		g.drawImage(Assets.UNICORN_B, hyperVox.x, hyperVox.y, hyperVox.width, hyperVox.height, observer);
	}

	@Override
	public void update() {
		if (oldWidth!=observer.getWidth()||oldHeight!=observer.getHeight()) {
			oldWidth=observer.getWidth();
			oldHeight=observer.getHeight();
			int midX=oldWidth/2;
			int midY=oldHeight/2;
			
			human=new Rectangle(midX-Assets.BUTTON_WIDTH/2,midY-150,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
			ai=new Rectangle(midX-Assets.BUTTON_WIDTH/2,midY,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
			exit=new Rectangle(midX-Assets.BUTTON_WIDTH/2,midY+150,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
			online=new Rectangle(midX-Assets.BUTTON_WIDTH/2,midY-300,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
			analyze=new Rectangle(midX+Assets.BUTTON_WIDTH/2+50,midY-225,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
			tutorial=new Rectangle(midX+Assets.BUTTON_WIDTH/2+50,midY-75,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
			map=new Rectangle(midX+Assets.BUTTON_WIDTH/2+50,midY+75,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
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
		} else if (map.contains(e.getPoint())) {
			observer.setScreen("map");
		} else if (analyze.contains(e.getPoint())) {
			observer.setScreen("game");
			observer.getGame().importGame();
		} else if (lessTime.contains(e.getPoint())) {
			int change=60000;
			if (e.isShiftDown()) {
				change=1000;
			}
			if (observer.getClockTime()>change) {
				observer.setClockTime(observer.getClockTime()-change);
			}
		} else if (moreTime.contains(e.getPoint())) {
			int change=60000;
			if (e.isShiftDown()) {
				change=1000;
			}
			observer.setClockTime(observer.getClockTime()+change);
		} else if (lessDelay.contains(e.getPoint())) {
			if (observer.getDelay()>999) {
				observer.setDelay(observer.getDelay()-1000);
			} else {
				observer.setDelay(0);
			}
		} else if (moreDelay.contains(e.getPoint())) {
			if (observer.getDelay()<60000) {
				observer.setDelay(observer.getDelay()+1000);
			}
		} else if (lessBonus.contains(e.getPoint())) {
			if (observer.getBonus()>999) {
				observer.setBonus(observer.getBonus()-1000);
			} else {
				observer.setBonus(0);
			}
		} else if (moreBonus.contains(e.getPoint())) {
			if (observer.getBonus()<60000) {
				observer.setBonus(observer.getBonus()+1000);
			}
		} else if (lastVariant.contains(e.getPoint())) {
			variantIndex--;
			if (variantIndex<0) {
				variantIndex=variants.length-1;
			}
			observer.setVariant(variants[variantIndex]);
			
		} else if (nextVariant.contains(e.getPoint())) {
			variantIndex++;
			if (variantIndex>=variants.length) {
				variantIndex=0;
			}
			observer.setVariant(variants[variantIndex]);
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
