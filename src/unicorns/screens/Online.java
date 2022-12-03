package unicorns.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;

import gameutil.text.Console;
import gameutil.text.Iru.Letter;
import graphics.screen.Screen;
import unicorns.Assets;
import unicorns.Main;
import unicorns.Panel;
import unicorns.Piece;
import unicorns.net.ClientController;
import unicorns.net.Server;

public class Online extends Screen {

	
	Panel observer;
	Hashtable<Rectangle, Piece> pieceRects=new Hashtable<>();
	String code="";
	Rectangle selectables;
	Rectangle codeRect;
	
	Rectangle menu;
	Rectangle join;
	Rectangle create;
	Rectangle clocks;
	Rectangle color;
	Rectangle lessTime;
	Rectangle moreTime;
	Rectangle lessDelay;
	Rectangle moreDelay;
	Rectangle lessBonus;
	Rectangle moreBonus;
	Rectangle copy;
	static enum COLOR {white,black,rand};
	COLOR col=COLOR.rand;
	
	int oldWidth=0;
	int oldHeight=0;
	boolean generated=false;
	
	public Online(Panel observer) {
		
		this.observer=observer;
		addPieceRect(50,50,new Piece(null,'g'));
		addPieceRect(100,50,new Piece(null,'p'));
		addPieceRect(150,50,new Piece(null,'d'));
		addPieceRect(200,50,new Piece(null,'u'));
		addPieceRect(250,50,new Piece(null,'n'));
		addPieceRect(300,50,new Piece(null,'b'));
		addPieceRect(350,50,new Piece(null,'r'));
		addPieceRect(400,50,new Piece(null,'q'));
		addPieceRect(450,50,new Piece(null,'k'));

		addPieceRect(50,100,new Piece(null,'G'));
		addPieceRect(100,100,new Piece(null,'P'));
		addPieceRect(150,100,new Piece(null,'D'));
		addPieceRect(200,100,new Piece(null,'U'));
		addPieceRect(250,100,new Piece(null,'N'));
		addPieceRect(300,100,new Piece(null,'B'));
		addPieceRect(350,100,new Piece(null,'R'));
		addPieceRect(400,100,new Piece(null,'Q'));
		addPieceRect(450,100,new Piece(null,'K'));
		selectables=new Rectangle(0,0,0,0);
		codeRect=new Rectangle(0,0,0,0);
		menu=new Rectangle(0,0,0,0);
		join=new Rectangle(0,0,0,0);
		create=new Rectangle(0,0,0,0);
		clocks=new Rectangle(0,0,0,0);
		color=new Rectangle(0,0,0,0);
		copy=new Rectangle(0,0,0,0);
		
		lessTime=new Rectangle(100,Assets.BUTTON_HEIGHT*3+20,16,16);
		moreTime=new Rectangle(226,Assets.BUTTON_HEIGHT*3+20,16,16);
		lessDelay=new Rectangle(100,Assets.BUTTON_HEIGHT*3+20+16*2,16,16);
		moreDelay=new Rectangle(226,Assets.BUTTON_HEIGHT*3+20+16*2,16,16);
		lessBonus=new Rectangle(100,Assets.BUTTON_HEIGHT*3+20+16*4,16,16);
		moreBonus=new Rectangle(226,Assets.BUTTON_HEIGHT*3+20+16*4,16,16);
	}
	
	@Override
	public void paint(Graphics g) {
		//draw thing for making game / joining game
		Color col1=Color.DARK_GRAY;
		Color col2=Color.pink;
		if (col==COLOR.black) {
			col1=Color.black;
			col2=Color.white;
		} else if (col==COLOR.white) {
			col1=Color.white;
			col2=Color.black;
		}
		g.setColor(col1);
		g.fillRect(0, 0, observer.getWidth(), observer.getHeight());
		g.setColor(Color.pink);
		g.fillRoundRect(selectables.x, selectables.y, selectables.width, selectables.height,50,50);
		for (Rectangle r:pieceRects.keySet()) {
			g.drawImage(pieceRects.get(r).getImage(), selectables.x+r.x, selectables.y+r.y, r.width, r.height,null);
		}
		if (generated) {
			//grey out selection bar
			g.setColor(new Color(127,127,127,150));
			g.fillRoundRect(selectables.x, selectables.y, selectables.width, selectables.height,50,50);
		}
		g.setColor(Color.gray);
		g.fillRoundRect(codeRect.x, codeRect.y, codeRect.width, codeRect.height,50,50);
		
		
		int x=codeRect.x+50;
		int y=codeRect.y+50;
		for (char c:code.toCharArray()) {
			g.drawImage(Piece.getImage(c), x, y, 40, 40,null);
			x=x+50;
		}
		
		if(observer.clocks()) {
			g.drawImage(Assets.CLOCKS_ON_BUTTON,(int) clocks.x,(int)clocks.y,clocks.width,clocks.height,null);
			//g.setColor(Color.green);
		} else {
			g.drawImage(Assets.CLOCKS_OFF_BUTTON,(int) clocks.x,(int)clocks.y,clocks.width,clocks.height,null);
			//g.setColor(Color.red);
		}
		//g.fillRoundRect(clocks.x, clocks.y, clocks.width, clocks.height,20,20);
		
		g.setColor(col2);
		
		g.drawImage(Letter.LESS.img16(),lessTime.x,lessTime.y,null);
		g.drawImage(Letter.LESS.img16(),lessDelay.x,lessDelay.y,null);
		g.drawImage(Letter.LESS.img16(),lessBonus.x,lessBonus.y,null);
		
		g.drawImage(Letter.MORE.img16(),moreTime.x,moreTime.y,null);
		g.drawImage(Letter.MORE.img16(),moreDelay.x,moreDelay.y,null);
		g.drawImage(Letter.MORE.img16(),moreBonus.x,moreBonus.y,null);
		
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
		
		g.drawString("Clock Time", lessTime.x-100, lessTime.y+15);
		
		g.drawString("Delay (s)", lessDelay.x-100, lessDelay.y+15);
	
		g.drawString("Bonus (s)", lessBonus.x-100, lessBonus.y+15);
		
		/*g.fillRoundRect(menu.x, menu.y, menu.width, menu.height,20,20);
		g.fillRoundRect(join.x, join.y, join.width, join.height,20,20);
		g.fillRoundRect(create.x, create.y, create.width, create.height,20,20);
		g.fillRoundRect(color.x, color.y, color.width, color.height,20,20);
		*/
		g.drawImage(Assets.MENU,menu.x, menu.y, menu.width, menu.height,null);
		g.drawImage(Assets.JOIN_BUTTON,join.x, join.y, join.width, join.height,null);
		g.drawImage(Assets.CREATE_BUTTON,create.x, create.y, create.width, create.height,null);
		if (generated) {
			g.drawImage(Assets.COPY_BUTTON, copy.x,copy.y,copy.width,copy.height, null);
		}
		g.fillRoundRect(color.x, color.y, color.width, color.height,20,20);
		g.setColor(col1);
		g.fillRoundRect(color.x+3, color.y+3, color.width-6, color.height-6,20,20);
		newFont= g.getFont().deriveFont((float)25);
		g.setFont(newFont);
		//g.drawString("Menu",(int) menu.getCenterX()-(int)g.getFontMetrics().getStringBounds("Menu", g).getWidth()/2,(int)menu.getCenterY()+(int)g.getFontMetrics().getStringBounds("Menu", g).getHeight()/4);
		//g.drawString("Join",(int) join.getCenterX()-(int)g.getFontMetrics().getStringBounds("Join", g).getWidth()/2,(int)join.getCenterY()+(int)g.getFontMetrics().getStringBounds("Join", g).getHeight()/4);
		//g.drawString("Create",(int) create.getCenterX()-(int)g.getFontMetrics().getStringBounds("Create", g).getWidth()/2,(int)create.getCenterY()+(int)g.getFontMetrics().getStringBounds("Create", g).getHeight()/4);
		//g.drawString("Clocks",(int) clocks.getCenterX()-(int)g.getFontMetrics().getStringBounds("Clocks", g).getWidth()/2,(int)clocks.getCenterY()+(int)g.getFontMetrics().getStringBounds("Clocks", g).getHeight()/4);
		
	}
	
	@Override
	public void update() {
		if (oldWidth!=observer.getWidth()||oldHeight!=observer.getHeight()) {
			oldWidth=observer.getWidth();
			oldHeight=observer.getHeight();
			int midX=oldWidth/2;
			int midY=oldHeight/2;
			selectables=new Rectangle(midX-270,midY+80,540,190);
			codeRect=new Rectangle(midX-170,midY-20,340,140);
			copy=new Rectangle(midX-170,midY-40-Assets.BUTTON_HEIGHT,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
			menu=new Rectangle(20,20,Assets.MENU.getWidth(null),Assets.MENU.getHeight(null));
			join=new Rectangle(midX-300,midY-170,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
			create=new Rectangle(midX+100,midY-170,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
			clocks=new Rectangle(155,20,Assets.BUTTON_WIDTH,Assets.BUTTON_HEIGHT);
			color=new Rectangle(325,20,100,100);
			/*
			lessTime=new Rectangle(100,Assets.BUTTON_HEIGHT+join.y+10,16,16);
			moreTime=new Rectangle(226,Assets.BUTTON_HEIGHT+join.y+10,16,16);
			lessDelay=new Rectangle(100,Assets.BUTTON_HEIGHT+join.y+10+16*2,16,16);
			moreDelay=new Rectangle(226,Assets.BUTTON_HEIGHT+join.y+10+16*2,16,16);
			lessBonus=new Rectangle(100,Assets.BUTTON_HEIGHT+join.y+10+16*4,16,16);
			moreBonus=new Rectangle(226,Assets.BUTTON_HEIGHT+join.y+10+16*4,16,16);*/
			
			
			lessTime=new Rectangle(535,20,16,16);
			moreTime=new Rectangle(661,20,16,16);
			lessDelay=new Rectangle(535,20+16*2,16,16);
			moreDelay=new Rectangle(661,20+16*2,16,16);
			lessBonus=new Rectangle(535,20+16*4,16,16);
			moreBonus=new Rectangle(661,20+16*4,16,16);
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
		if (selectables.contains(e.getPoint())&&!generated) {
			int x=e.getX()-selectables.x;
			int y=e.getY()-selectables.y;
			for (Rectangle r:pieceRects.keySet()) {
				if (r.contains(x,y)) {
					if (code.length()<5) {
						code=code+pieceRects.get(r).getType();
					} else {
						code="";
					}
				}
			}
		} else if (color.contains(e.getPoint())) {
			if (col==COLOR.rand) {
				col=COLOR.white;
			} else if (col==COLOR.white) {
				col=COLOR.black;
			} else {
				col=COLOR.rand;
			}
		} else if (menu.contains(e.getPoint())) {
			observer.setScreen("title");
		} else if (create.contains(e.getPoint())){
			boolean white=(col==COLOR.white);
			if (col==COLOR.rand) {
				white=(Main.rand.nextBoolean());
			}
			long clocks=0;
			if (observer.clocks()) {
				clocks=observer.getClockTime();
			}
			Main.err.println("Clocks: "+clocks);
			Main.err.println("White: "+ white);
			observer.createGame(white, clocks);
		} else if (generated){
			if (copy.contains(e.getPoint())) {
				copyDiscordCode();
			}
		} else if (clocks.contains(e.getPoint())) {
			observer.setClocks(!observer.clocks());
		} else if (join.contains(e.getPoint())) {
			//attempt to join a game with the current code
			observer.join(code);
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
		}
	}
	
	private void addPieceRect(int x,int y,Piece p) {
		pieceRects.put(new Rectangle(x,y,40,40),p);
	}
	
	public void setCode(String code) {
		this.code=code;
	}
	
	public void setGenerated(boolean g) {
		this.generated=g;
	}

	@Override
	protected void keyDown(int key) {
		// TODO Auto-generated method stub
		
	}
	
	public String getDiscordCode() {
		char[] chars=new char[code.length()];
		code.getChars(0, code.length(), chars, 0);
		String discordCode="";
		for (char c:chars) {
			discordCode+=Piece.getEmoji(c);
		}
		return discordCode;
	}
	
	public void copyDiscordCode() {
		Clipboard clipboard=Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(getDiscordCode()), null);
	}
	
}
