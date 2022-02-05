package unicorns.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;

import gameutil.text.Console;
import graphics.screen.Screen;
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
			g.setColor(Color.green);
		} else {
			g.setColor(Color.red);
		}
		g.fillRoundRect(clocks.x, clocks.y, clocks.width, clocks.height,20,20);
		
		g.setColor(col2);
		
		g.fillRoundRect(menu.x, menu.y, menu.width, menu.height,20,20);
		g.fillRoundRect(join.x, join.y, join.width, join.height,20,20);
		g.fillRoundRect(create.x, create.y, create.width, create.height,20,20);
		g.fillRoundRect(color.x, color.y, color.width, color.height,20,20);
		g.setColor(col1);
		g.fillRoundRect(color.x+3, color.y+3, color.width-6, color.height-6,20,20);
		Font newFont = g.getFont().deriveFont((float)25);
		g.setFont(newFont);
		g.drawString("Menu",(int) menu.getCenterX()-(int)g.getFontMetrics().getStringBounds("Menu", g).getWidth()/2,(int)menu.getCenterY()+(int)g.getFontMetrics().getStringBounds("Menu", g).getHeight()/4);
		g.drawString("Join",(int) join.getCenterX()-(int)g.getFontMetrics().getStringBounds("Join", g).getWidth()/2,(int)join.getCenterY()+(int)g.getFontMetrics().getStringBounds("Join", g).getHeight()/4);
		g.drawString("Create",(int) create.getCenterX()-(int)g.getFontMetrics().getStringBounds("Create", g).getWidth()/2,(int)create.getCenterY()+(int)g.getFontMetrics().getStringBounds("Create", g).getHeight()/4);
		g.drawString("Clocks",(int) clocks.getCenterX()-(int)g.getFontMetrics().getStringBounds("Clocks", g).getWidth()/2,(int)clocks.getCenterY()+(int)g.getFontMetrics().getStringBounds("Clocks", g).getHeight()/4);
		
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
			menu=new Rectangle(5,5,200,100);
			join=new Rectangle(midX-300,midY-170,200,100);
			create=new Rectangle(midX+100,midY-170,200,100);
			clocks=new Rectangle(215,5,100,50);
			color=new Rectangle(325,5,100,100);
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
				clocks=60000*20;
			}
			Main.err.println("Clocks: "+clocks);
			Main.err.println("White: "+ white);
			observer.createGame(white, clocks);
		} else if (clocks.contains(e.getPoint())) {
			observer.setClocks(!observer.clocks());
		} else if (join.contains(e.getPoint())) {
			//attempt to join a game with the current code
			observer.join(code);
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

	
}
