package unicorns.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import graphics.screen.Screen;
import unicorns.Assets;
import unicorns.Main;
import unicorns.Panel;
import unicorns.Textbox;

public class Tutorial extends Screen{
	Panel observer;
	int oldWidth=0;
	int oldHeight=0;
	int midX=0;
	int midY=0;
	Textbox t=new Textbox("Dee");
	long time=System.currentTimeMillis();
	long dt=0;
	
	int x;
	int y;
	int z;
	int w;
	
	int bound=15;
	
	
	String[] dialogue= {
		"Welcome to the tutorial!",
		"Let's start with one dimension. In one dimension, you can only move back and fourth and you need one number to describe your position. Let's call that number x.",
		"So when you move right one square your x goes up by one and when you move left one square your x goes down by one.",
		"Go ahead and play around a little bit on this one dimensional board. Use the A and D keys to move left and right.",
		"Alright, now that you understand movement in position in one dimension, lets take it up a dimension. In two dimensions you need two numbers to describe your position. We can call these numbers x and y.",
		"Now there are four basic directions that you can move in: left, right, up, and down. Every dimension corresponds to two basic directions.",
		"In this case, one dimension corresponds to left and right while the other dimension corresponds to up and down. Moving left and right changes x just like in one dimension and moving up and down changes y. Because of this, it makes sense to call the dimension corresponding to left and right the x dimension and the dimension corresponding to up and down the y dimension.",
		"Go ahead and play around a little bit on this two dimensional board. Use the A and D keys to move left and right and the W and S keys to move up and down.",
		"Before continuing to three dimensions we need to go over dimensional squishification because without dimensional squishification, you can't look at anything with more than two dimensions all at the same time.",
		"There are many ways one can perform dimensional squishification but the kind used by most high dimensional board games (including 4D chess without multiverse timetravel) is to take all of the rows of a two dimensional board and lay them out next to each other in a line so that you can see every part of the two dimensional board in a one dimensional view.",
		"Go ahead and play around a little bit on this two dimensional board and pay attention to how the one dimensional view changes as you move.",
		"Hopefully you have a grasp on how to interpret a squishified view now. If you're still feeling confused, you can right click the dialogue box to go back to the last part of the tutorial and keep honing your intuitions.",
		"Above is a three dimensional board. The the x dimension and the z dimension have been squishified to make a two dimensional view of the entire board. The z dimension corresponds the basic directions forward and backward.",
		"Go ahead and play around a little bit on this three dimensional board and pay attention to how the position numbers and view change as you move. Use the A and D keys to move left and right, the Q and E keys to move forward and backward, and the W and S keys to move up and down.",
		"Now it's time four four dimensions!",
		"I mean... Now it's time for four dimensions!",
		"Above is four dimensional board. The x and z dimensions are squishified horizontally and the y and w dimensions are are squishified vertically. The w dimension corresponds to the directions ana and kata (also known by some as wint and zant or on and again).",
		"Go ahead and play around a little bit on this four dimensional board and pay attention to how the position numbers and view change as you move. Use the A and D keys to move left and right, the Q and E keys to move forward and backward, the W and S keys to move up and down, and the Space and Shift keys to move ana and kata.",
		"Congratulations! You finished the tutorial! Once you're ready, left click the dialogue box to return to the menu.",
	};
	
	int step=0;
	
	public Tutorial(Panel observer) {
		this.observer=observer;
		int midX=observer.getWidth()/2;
		int midY=observer.getHeight()/2;
		t.setText(dialogue[step]);
		
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(new Color(170,70,180));
		g.fillRect(0, 0, oldWidth, oldHeight);
		g.setColor(Color.black);
		int size;
		int length;
		g.setFont(g.getFont().deriveFont(16f));
		switch (step) {
			case 0,14,15:
				size=(int)Math.min(dt/5, 128);
				if (size==128) {
					short s=(short)((dt/100)%20);
					if (s>10) {
						s=(short) (20-s);
					}
					size=(int) (size+s);
				}
				g.drawImage(Assets.UNICORN_B, midX-size/2, midY-size/2-20,size,size, null);
			break;
			case 1,2:
				short s=(short)((dt/100)%20);
				if (s>10) {
					s=(short) (20-s);
				}
				
				length=(int)Math.min(dt/128, 16);
				int spaceSize=(int)Math.min(dt/8, 512);
				
				
				size=(int) (128+s);
				
				int px=(int) Math.max(midX-(dt/10), 148);
				g.drawImage(Assets.UNICORN_B, px-size/2, midY-size/2-20,size,size, null);
				int drawSize=Math.min(spaceSize,32);
				
				x=(int) (Math.floor(dt/1000)%30);
				if (x>15) {
					x=30-x;
				}
				
				for (int i=0;i<length;i++) {
					int thisSize=(int) Math.min(spaceSize-i*32,32);
					g.drawRect(midX+100-spaceSize/2+i*drawSize, midY-thisSize/2, thisSize, thisSize);
					if (i==x) {
						g.fillOval(midX+100-spaceSize/2+i*drawSize, midY-thisSize/2, thisSize, thisSize);
						g.fillRoundRect(midX+100-spaceSize/2+i*drawSize-7,midY-thisSize/2-10-17,47,25,20, 20);
						g.setColor(Color.white);
						g.drawString("x: "+x, midX+100-spaceSize/2+i*drawSize,midY-thisSize/2-10);
						g.setColor(Color.black);
						
					}
					
				}
				
			break;
			case 3:
				size=128-(int)Math.min(dt/5, 128);
				int littleSize=(-size+128)/4;
				if (size>0) {
					g.drawImage(Assets.UNICORN_B, 148-size/2, midY-size/2-20,size,size, null);
				}
				for (int i=0;i<16;i++) {
					g.drawRect(midX+-156+i*32, midY-16, 32, 32);
					if (i==x) {
						if (littleSize<32) {
							g.fillOval(midX-156+i*32+littleSize/2, midY-16+littleSize/2, 32-littleSize, 32-littleSize);
						}
						if (littleSize>0) {
							g.drawImage(Assets.UNICORN_B, midX-172+32+i*32-littleSize/2, midY-littleSize/2,littleSize,littleSize, null);
						}
						g.fillRoundRect(midX-156+i*32-7,midY-26-17,47,25,20, 20);
						g.setColor(Color.white);
						g.drawString("x: "+x, midX-156+i*32,midY-26);
						g.setColor(Color.black);
					}
					
				}
				
				
				
			break;
			case 4,5,6:
				
				length=(int)Math.min(dt/128, 8);
				spaceSize=(int)Math.min(dt/8, 256);
				
				drawSize=Math.min(spaceSize,32);
				
				x=(int) (Math.floor(dt/1000)%16);
				if (x>7) {
					x=15-x;
				}
				
				y=(int) (Math.floor(dt/8000)%14);
				if (y>7) {
					y=14-y;
				}
				
				int drawStringX=-1;
				int drawStringY=-1;
				for (int i=0;i<length;i++) {
					for (int j=0;j<8;j++) {
						
						int thisSize=(int) Math.min(spaceSize-i*32,32);
						g.drawRect(midX+100-spaceSize+i*drawSize, midY-thisSize/2-j*drawSize, thisSize, thisSize);
						if (i==x&&j==y&&thisSize>0) {
							g.drawImage(Assets.UNICORN_B, midX+116-spaceSize+i*drawSize-thisSize/2, midY-thisSize/2-j*drawSize,thisSize,thisSize, null);
							
							drawStringX=midX+100-spaceSize+i*drawSize;
							drawStringY=midY-thisSize/2-26-j*drawSize;
							
							
							
						}
					
					}
				}
				if (drawStringX>-1&&drawStringY>-1) {
					g.fillRoundRect(drawStringX-7,drawStringY-17,47,41,20, 20);
					g.setColor(Color.white);
					g.drawString("x: "+x, drawStringX,drawStringY);
					g.drawString("y: "+y, drawStringX,drawStringY+16);
					g.setColor(Color.black);
				}
			break;
			case 7:
				length=8;
				drawStringX=-1;
				drawStringY=-1;
				for (int i=0;i<length;i++) {
					for (int j=0;j<8;j++) {
						
						int thisSize=32;
						g.drawRect(midX+100-256+i*32, midY-32/2-j*32, 32, 32);
						if (i==x&&j==y&&32>0) {
							g.drawImage(Assets.UNICORN_B, midX+116-256+i*32-32/2, midY-32/2-j*32,32,32, null);
							
							drawStringX=midX+100-256+i*32;
							drawStringY=midY-32/2-26-j*32;
							
							
							
						}
					
					}
				}
				if (drawStringX>-1&&drawStringY>-1) {
					g.fillRoundRect(drawStringX-7,drawStringY-17,47,41,20, 20);
					g.setColor(Color.white);
					g.drawString("x: "+x, drawStringX,drawStringY);
					g.drawString("y: "+y, drawStringX,drawStringY+16);
					g.setColor(Color.black);
				}
			break;
			case 8:
				
				length=(int)Math.min(dt/128, 4);
				spaceSize=(int)Math.min(dt/8, 256);
				
				drawSize=Math.min(spaceSize,32);
				
				x=(int) (Math.floor(dt/1000)%8);
				if (x>3) {
					x=7-x;
				}
				
				y=(int) (Math.floor(dt/4000)%6);
				if (y>3) {
					y=6-y;
				}
				
				drawStringX=-1;
				drawStringY=-1;
				for (int i=0;i<length;i++) {
					for (int j=0;j<4;j++) {
						
						int thisSize=(int) Math.min(spaceSize-i*32,32);
						g.drawRect(midX+100-spaceSize/2+i*drawSize, midY-thisSize/2-j*drawSize-40, thisSize, thisSize);
						if (i==x&&j==y&&thisSize>0) {
							g.drawImage(Assets.UNICORN_B, midX+116-spaceSize/2+i*drawSize-thisSize/2, midY-thisSize/2-j*drawSize-40,thisSize,thisSize, null);
							
							drawStringX=midX+100-spaceSize/2+i*drawSize;
							drawStringY=midY-thisSize/2-26-j*drawSize-40;
							
							
							
						}
					
					}
				}
				if (drawStringX>-1&&drawStringY>-1) {
					g.fillRoundRect(drawStringX-7,drawStringY-17,47,41,20, 20);
					g.setColor(Color.white);
					g.drawString("x: "+x, drawStringX,drawStringY);
					g.drawString("y: "+y, drawStringX,drawStringY+16);
					g.setColor(Color.black);
				}
				
			break;
			case 9:
				
				length=(int)Math.min(dt/128, 4);
				spaceSize=(int)Math.min(dt/8, 256);
				
				drawSize=Math.min(spaceSize,32);
				
				x=(int) (Math.floor(dt/1000)%8);
				if (x>3) {
					x=7-x;
				}
				
				y=(int) (Math.floor(dt/4000)%6);
				if (y>3) {
					y=6-y;
				}
				
				drawStringX=-1;
				drawStringY=-1;
				for (int i=0;i<4;i++) {
					for (int j=0;j<4;j++) {
						
						
						
						g.drawRect(midX+100-128+i*32, midY-32/2-j*32-40, 32, 32);
						if (i==x&&j==y&&32>0) {
							g.drawImage(Assets.UNICORN_B, midX+116-128+i*32-32/2, midY-32/2-j*32-40,32,32, null);
							
							drawStringX=midX+100-128+i*32;
							drawStringY=midY-32/2-26-j*32-40;
							
							
							
						}
					
					}
				}
				if (drawStringX>-1&&drawStringY>-1) {
					g.fillRoundRect(drawStringX-7,drawStringY-17,47,41,20, 20);
					g.setColor(Color.white);
					g.drawString("x: "+x, drawStringX,drawStringY);
					g.drawString("y: "+y, drawStringX,drawStringY+16);
					g.setColor(Color.black);
				}
				
				drawStringX=-1;
				drawStringY=-1;
				for (int i=0;i<length;i++) {
					for (int j=0;j<4;j++) {
						
						int thisSize=(int) Math.min(spaceSize-i*32,32);
						int slide=(int)(spaceSize*1.5);
						g.drawRect(midX+100-slide+i*drawSize+(int)(j*drawSize*4.5), midY-thisSize/2+40, thisSize, thisSize);
						if (i==x&&j==y&&thisSize>0) {
							g.drawImage(Assets.UNICORN_B, midX+116-slide+i*drawSize+(int)(j*drawSize*4.5)-thisSize/2, midY-thisSize/2+40,thisSize,thisSize, null);
							
							drawStringX=midX+100-slide+i*drawSize+(int)(j*drawSize*4.5);
							drawStringY=midY-thisSize/2-26+40;
							
							
							
						}
					
					}
				}
				if (drawStringX>-1&&drawStringY>-1) {
					g.fillRoundRect(drawStringX-7,drawStringY-17,47,41,20, 20);
					g.setColor(Color.white);
					g.drawString("x: "+x, drawStringX,drawStringY);
					g.drawString("y: "+y, drawStringX,drawStringY+16);
					g.setColor(Color.black);
				}
				
			break;
			case 10:
				
				
				drawStringX=-1;
				drawStringY=-1;
				for (int i=0;i<4;i++) {
					for (int j=0;j<4;j++) {
						
						
						
						g.drawRect(midX+100-128+i*32, midY-32/2-j*32-40, 32, 32);
						if (i==x&&j==y&&32>0) {
							g.drawImage(Assets.UNICORN_B, midX+116-128+i*32-32/2, midY-32/2-j*32-40,32,32, null);
							
							drawStringX=midX+100-128+i*32;
							drawStringY=midY-32/2-26-j*32-40;
							
							
							
						}
					
					}
				}
				if (drawStringX>-1&&drawStringY>-1) {
					g.fillRoundRect(drawStringX-7,drawStringY-17,47,41,20, 20);
					g.setColor(Color.white);
					g.drawString("x: "+x, drawStringX,drawStringY);
					g.drawString("y: "+y, drawStringX,drawStringY+16);
					g.setColor(Color.black);
				}
				
				drawStringX=-1;
				drawStringY=-1;
				for (int i=0;i<4;i++) {
					for (int j=0;j<4;j++) {
						g.drawRect(midX+100-384+i*32+(int)(j*32*4.5), midY-32/2+40, 32, 32);
						if (i==x&&j==y&&32>0) {
							g.drawImage(Assets.UNICORN_B, midX+116-384+i*32+(int)(j*32*4.5)-32/2, midY-32/2+40,32,32, null);
							
							drawStringX=midX+100-384+i*32+(int)(j*32*4.5);
							drawStringY=midY-32/2-26+40;
							
							
							
						}
					
					}
				}
				if (drawStringX>-1&&drawStringY>-1) {
					g.fillRoundRect(drawStringX-7,drawStringY-17,47,41,20, 20);
					g.setColor(Color.white);
					g.drawString("x: "+x, drawStringX,drawStringY);
					g.drawString("y: "+y, drawStringX,drawStringY+16);
					g.setColor(Color.black);
				}
				
			break;
			case 11,12:
				
				length=(int)Math.min(dt/128, 4);
				spaceSize=(int)Math.min(dt/8, 256);
				
				drawSize=Math.min(spaceSize,32);
				
				x=(int) (Math.floor(dt/1000)%8);
				if (x>3) {
					x=7-x;
				}
				
				y=(int) (Math.floor(dt/4000)%8);
				if (y>3) {
					y=7-y;
				}
				
				z=(int) (Math.floor(dt/16000)%6);
				if (z>3) {
					z=6-z;
				}
				
				
				drawStringX=-1;
				drawStringY=-1;
				for (int i=0;i<4;i++) {
					for (int j=0;j<length;j++) {
						for (int k=0;k<4;k++) {
							
							int thisSize=(int) Math.min(spaceSize-i*32,32);
							int slide=(int)(spaceSize*1.5);
							g.drawRect(midX+100-slide+i*drawSize+(int)(k*drawSize*4.5), midY-thisSize/2+40-j*drawSize, thisSize, thisSize);
							if (i==x&&j==y&&k==z&&thisSize>0) {
								g.drawImage(Assets.UNICORN_B, midX+116-slide+i*drawSize+(int)(k*drawSize*4.5)-thisSize/2, midY-thisSize/2+40-j*drawSize,thisSize,thisSize, null);
								
								drawStringX=midX+100-slide+i*drawSize+(int)(k*drawSize*4.5);
								drawStringY=midY-thisSize/2-48+40-j*drawSize;
								
								
								
							}
						}
					
					}
				}
				if (drawStringX>-1&&drawStringY>-1) {
					g.fillRoundRect(drawStringX-7,drawStringY-17,47,57,20, 20);
					g.setColor(Color.white);
					g.drawString("x: "+x, drawStringX,drawStringY);
					g.drawString("y: "+y, drawStringX,drawStringY+16);
					g.drawString("z: "+z, drawStringX,drawStringY+32);
					g.setColor(Color.black);
				}
				
			break;
			case 13:
				
				
				drawStringX=-1;
				drawStringY=-1;
				for (int i=0;i<4;i++) {
					for (int j=0;j<4;j++) {
						for (int k=0;k<4;k++) {
							
							g.drawRect(midX+100-384+i*32+(int)(k*32*4.5), midY-32/2+40-j*32, 32, 32);
							if (i==x&&j==y&&k==z&&32>0) {
								g.drawImage(Assets.UNICORN_B, midX+116-384+i*32+(int)(k*32*4.5)-32/2, midY-32/2+40-j*32,32,32, null);
								
								drawStringX=midX+100-384+i*32+(int)(k*32*4.5);
								drawStringY=midY-32/2-48+40-j*32;
								
								
								
							}
						}
					
					}
				}
				if (drawStringX>-1&&drawStringY>-1) {
					g.fillRoundRect(drawStringX-7,drawStringY-17,47,57,20, 20);
					g.setColor(Color.white);
					g.drawString("x: "+x, drawStringX,drawStringY);
					g.drawString("y: "+y, drawStringX,drawStringY+16);
					g.drawString("z: "+z, drawStringX,drawStringY+32);
					g.setColor(Color.black);
				}
				
			break;
			case 16:
				
				length=(int)Math.min(dt/128, 4);
				spaceSize=(int)Math.min(dt/8, 256);
				
				drawSize=Math.min(spaceSize,32);
				
				x=(int) (Math.floor(dt/100)%8);
				if (x>3) {
					x=7-x;
				}
				
				y=(int) (Math.floor(dt/400)%8);
				if (y>3) {
					y=7-y;
				}
				
				z=(int) (Math.floor(dt/1600)%8);
				if (z>3) {
					z=7-z;
				}
				
				w=(int) (Math.floor(dt/6400)%6);
				if (w>3) {
					w=6-w;
				}
				
				int yOff=t.getY()-80;
				drawStringX=-1;
				drawStringY=-1;
				for (int i=0;i<4;i++) {
					for (int j=0;j<4;j++) {
						for (int k=0;k<4;k++) {
							for(int l=0;l<length;l++) {
								int thisSize=(int) Math.min(spaceSize-i*32,32);
								int slide=(int)(spaceSize*1.5);
								g.drawRect(midX+100-slide+i*drawSize+(int)(k*drawSize*4.5), yOff-thisSize/2+40-j*drawSize-(int)(l*drawSize*4.5), thisSize, thisSize);
								if (i==x&&j==y&&k==z&&l==w&&thisSize>0) {
									g.drawImage(Assets.UNICORN_B, midX+116-slide+i*drawSize+(int)(k*drawSize*4.5)-thisSize/2, yOff-thisSize/2+40-j*drawSize-(int)(l*drawSize*4.5),thisSize,thisSize, null);
									
									drawStringX=midX+100-slide+i*drawSize+(int)(k*drawSize*4.5);
									drawStringY=yOff-thisSize/2+40-j*drawSize-(int)(l*drawSize*4.5);
									
									
									
								}
							}
						}
					
					}
				}
				
				if (drawStringX>-1&&drawStringY>-1) {
					drawStringX+=48;
					g.fillRoundRect(drawStringX-7,drawStringY-17,47,73,20, 20);
					g.setColor(Color.white);
					g.drawString("x: "+x, drawStringX,drawStringY);
					g.drawString("y: "+y, drawStringX,drawStringY+16);
					g.drawString("z: "+z, drawStringX,drawStringY+32);
					g.drawString("w: "+w, drawStringX,drawStringY+48);
					g.setColor(Color.black);
				}
				
			break;
			case 17,18:
				
				length=(int)Math.min(dt/128, 4);
				spaceSize=(int)Math.min(dt/8, 256);
				
				drawSize=Math.min(spaceSize,32);
				
				
				yOff=t.getY()-80;
				drawStringX=-1;
				drawStringY=-1;
				for (int i=0;i<4;i++) {
					for (int j=0;j<4;j++) {
						for (int k=0;k<4;k++) {
							for(int l=0;l<4;l++) {
								g.drawRect(midX+100-384+i*32+(int)(k*32*4.5), yOff-32/2+40-j*32-(int)(l*32*4.5), 32, 32);
								if (i==x&&j==y&&k==z&&l==w) {
									g.drawImage(Assets.UNICORN_B, midX+116-384+i*32+(int)(k*32*4.5)-32/2, yOff-32/2+40-j*32-(int)(l*32*4.5),32,32, null);
									
									drawStringX=midX+100-384+i*32+(int)(k*32*4.5);
									drawStringY=yOff-32/2+40-j*32-(int)(l*32*4.5);
									
									
									
								}
							}
						}
					
					}
				}
				
				if (drawStringX>-1&&drawStringY>-1) {
					drawStringX+=48;
					g.fillRoundRect(drawStringX-7,drawStringY-17,47,73,20, 20);
					g.setColor(Color.white);
					g.drawString("x: "+x, drawStringX,drawStringY);
					g.drawString("y: "+y, drawStringX,drawStringY+16);
					g.drawString("z: "+z, drawStringX,drawStringY+32);
					g.drawString("w: "+w, drawStringX,drawStringY+48);
					g.setColor(Color.black);
				}
				
			break;
		}
		
		t.setGraphics(g);
		g.setColor(Color.white);
		t.draw();
	}

	@Override
	public void update() {
		if (oldWidth!=observer.getWidth()||oldHeight!=observer.getHeight()) {
			oldWidth=observer.getWidth();
			oldHeight=observer.getHeight();
			midX=oldWidth/2;
			midY=oldHeight/2;
			t.setPos(midX-300, oldHeight-205);
		}
		dt=System.currentTimeMillis()-time;
		switch (step) {
			
		}
		
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_BACK_SLASH) {
			Main.cmd.setVisible(true);
		}  else if (e.getKeyCode()==KeyEvent.VK_M) {
			observer.toggleMusic();
		}
		switch (step) {
			case 3:
				if (e.getKeyCode()==KeyEvent.VK_A) {
					if (x>0) {
						x--;
					}
				} else if (e.getKeyCode()==KeyEvent.VK_D) {
					if (x<bound) {
						x++;
					}
					
				}
			break;
			case 7,10:
				if (e.getKeyCode()==KeyEvent.VK_A) {
					if (x>0) {
						x--;
					}
				} else if (e.getKeyCode()==KeyEvent.VK_D) {
					if (x<bound) {
						x++;
					}
					
				} else if (e.getKeyCode()==KeyEvent.VK_S) {
					if (y>0) {
						y--;
					}
				} else if (e.getKeyCode()==KeyEvent.VK_W) {
					if (y<bound) {
						y++;
					}
					
				}
			break;
			case 13:
				if (e.getKeyCode()==KeyEvent.VK_A) {
					if (x>0) {
						x--;
					}
				} else if (e.getKeyCode()==KeyEvent.VK_D) {
					if (x<bound) {
						x++;
					}
					
				} else if (e.getKeyCode()==KeyEvent.VK_S) {
					if (y>0) {
						y--;
					}
				} else if (e.getKeyCode()==KeyEvent.VK_W) {
					if (y<bound) {
						y++;
					}
					
				} else if (e.getKeyCode()==KeyEvent.VK_Q) {
					if (z>0) {
						z--;
					}
				} else if (e.getKeyCode()==KeyEvent.VK_E) {
					if (z<bound) {
						z++;
					}
					
				}
			break;
			case 17,18:
				if (e.getKeyCode()==KeyEvent.VK_A) {
					if (x>0) {
						x--;
					}
				} else if (e.getKeyCode()==KeyEvent.VK_D) {
					if (x<bound) {
						x++;
					}
					
				} else if (e.getKeyCode()==KeyEvent.VK_S) {
					if (y>0) {
						y--;
					}
				} else if (e.getKeyCode()==KeyEvent.VK_W) {
					if (y<bound) {
						y++;
					}
					
				} else if (e.getKeyCode()==KeyEvent.VK_Q) {
					if (z>0) {
						z--;
					}
				} else if (e.getKeyCode()==KeyEvent.VK_E) {
					if (z<bound) {
						z++;
					}
					
				} else if (e.getKeyCode()==KeyEvent.VK_SHIFT) {
					if (w>0) {
						w--;
					}
				} else if (e.getKeyCode()==KeyEvent.VK_SPACE) {
					if (w<bound) {
						w++;
					}
					
				}
			break;
 		}
	}
	
	public void mousePressed(MouseEvent e) {
		if (t.contains(e.getPoint())) {
			if (e.getButton()==MouseEvent.BUTTON1) {
				if (step<dialogue.length-1) {
					step++;
					t.setText(dialogue[step]);
					switch (step) {
						case 2,5,6,7,12,15,18:
						break;
						case 4:
							if (x>7) {
								x=7;
							}
							bound=7;
							time=System.currentTimeMillis();
						break;
						case 8:
							bound=3;
						default:
							time=System.currentTimeMillis();
						break;
					}
					
				} else {
					observer.setScreen("title");
				}
			} else if (e.getButton()==MouseEvent.BUTTON3) {
				if (step>0) {
					step--;
					switch (step) {
						case 2,5,6:
						break;
						case 3:
							if (x>15) {
								x=15;
							}
							bound=15;
						break;
						case 7:
							if (x>7) {
								x=7;
							}
							bound=7;
							time=System.currentTimeMillis();
						break;
						case 8:
							bound=3;
						default:
							time=System.currentTimeMillis();
						break;
					}
					time=System.currentTimeMillis();
				}
			}
		}
		switch (step) {
		
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		switch (step) {
		
		}
	}
	
	@Override
	public void setInit() {
		observer.setSong("tutorial.wav");
		Main.f.setExtendedState(Frame.MAXIMIZED_BOTH);
		time=System.currentTimeMillis();
		step=0;
	}

	@Override
	protected void keyDown(int key) {
		// TODO Auto-generated method stub
		
	}
}
