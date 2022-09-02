package unicorns;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public class Textbox{
	String speaker;
	String dialogue="";
	Graphics g=null;
	Rectangle r=new Rectangle(600,200);
	int ln=18;
	long lastTime=System.currentTimeMillis();
	static int slowness=20;
	long step=0;
	boolean compile=false;
	Image portrait=Assets.UNICORN_B;

	public Textbox(String speaker) {
		this.speaker=speaker;
	}
	
	public void setGraphics(Graphics g) {
		this.g=g;
	}
	
	public void setText(String s) {
		dialogue=s;
		compile=true;
		reset();
	}
	
	public void reset() {
		lastTime=System.currentTimeMillis();
	}
	
	private int getLength() {
		return (int) Math.min(step,dialogue.length());
	}
	
	private void updateStep() {
		step=(long) Math.floor((System.currentTimeMillis()-lastTime)/slowness);
	}
	
	
	
	public void draw(int x,int y,Graphics g) {
		updateStep();
		draw(x,y,g,getLength());
	}
	
	public String getDialogue() {
		return dialogue;
	}
	
	public void draw(int x,int y,Graphics g,int length) {
		g.drawImage(Assets.TEXT_BOX, x, y,600,200, null);
		g.drawImage(Assets.PORTRAIT, x-40, y-40, null);
		g.drawImage(portrait, x-30, y-30,64,64, null);
		g.drawString("     << ["+speaker+"] >>", x+20, y+30);
		if (compile) {
			compileText(g);
			length=Math.min(dialogue.length(), length);
		}
		//String d="";
		String[] toDraw=dialogue.substring(0,length).split("\n");
		int line=0;
		for (int i=0;i<toDraw.length;i++) {
			/*while (g.getFontMetrics().getStringBounds(toDraw[i], g).getWidth()>560) {
				int cut=toDraw[i].lastIndexOf(' ');
				if (cut<0) {
					cut=toDraw[i].length()-1;
				}
				boolean spaceCut=false;
				d=toDraw[i].substring(0, cut);
				while (g.getFontMetrics().getStringBounds(d, g).getWidth()>560) {
					cut--;
					int scut=toDraw[i].substring(0, cut).lastIndexOf(' ');
					if (scut>-1) {
						cut=scut+1;
						spaceCut=true;
					}
					d=toDraw[i].substring(0, cut);
					
				}
				
				toDraw[i]=toDraw[i].substring(cut);
				g.drawString(d, x+20, y+35+ln+line*ln);
				line++;
				//line+=drawText(d,x+20,y+40+line*14);
			}*/
			if (i==toDraw.length-1&&step%50>25) {
				toDraw[i]=toDraw[i]+"_";
			}
			g.drawString(toDraw[i], x+20, y+35+ln+line*ln);
			line++;
			//drawText(toDraw,x+20,y+40+line*14);
		}
	}
	
	private void compileText(Graphics g) {
		compile=false;
		String d="";
		String[] toDraw=dialogue.split("\n");
		String out="";
		int line=0;
		for (int i=0;i<toDraw.length;i++) {
			while (g.getFontMetrics().getStringBounds(toDraw[i], g).getWidth()>560) {
				int cut=toDraw[i].lastIndexOf(' ');
				if (cut<0) {
					cut=toDraw[i].length()-1;
				}
				boolean spaceCut=false;
				d=toDraw[i].substring(0, cut);
				while (g.getFontMetrics().getStringBounds(d, g).getWidth()>560) {
					cut--;
					int scut=toDraw[i].substring(0, cut).lastIndexOf(' ');
					if (scut>-1) {
						cut=scut+1;
						spaceCut=true;
					}
					d=toDraw[i].substring(0, cut);
					
				}
				
				toDraw[i]=toDraw[i].substring(cut);
				out=out+d+"\n";
				line++;
				//line+=drawText(d,x+20,y+40+line*14);
			}
			//g.drawString(toDraw[i], x+20, y+35+ln+line*ln);
			out=out+toDraw[i]+"\n";
			line++;
		}
		dialogue=out;
	}
	
	/*
	private int drawText(String s,int x,int y) {
		try {
			String[] lines=s.split("\n");
			for (int i=0;i<lines.length;i++) {
				g.drawString(lines[i], x, y+i*14);
			}
			return lines.length;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}*/
	
	public void draw(int x,int y) {
		draw(x,y,g);
	}
	
	public void draw() {
		draw(r.x,r.y,g);
	}
	
	public void setPos(int x,int y) {
		r.x=x;
		r.y=y;
	}
	
	public boolean contains(Point p) {
		return r.contains(p);
	}
	
	public int getX() {
		return r.x;
	}
	
	public int getY() {
		return r.y;
	}
	
	public int getWidth() {
		return r.width;
	}
	
	public int getHeight() {
		return r.height;
	}
}
