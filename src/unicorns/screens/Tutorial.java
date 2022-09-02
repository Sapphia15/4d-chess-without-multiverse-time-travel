package unicorns.screens;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

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
	
	final int sWidth=1360;
	final int sHeight=768;
	final int smidX=sWidth/2;
	final int smidY=sHeight/2;
	final int maxTranslate=612;
	final double ratio=((double)sWidth)/sHeight;
	int drawSceneWidth=sWidth;
	int drawSceneX=0;
	Textbox t=new Textbox("Dee");
	long time=System.currentTimeMillis();
	long dt=0;
	BufferedImage scene;
	int x;
	int y;
	int z;
	int w;
	Graphics2D g;
	
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
		"Congratulations! You finished the tutorial! A more in depth tutorial that explains piece movement and general 4D chess rules is coming soon! Once you're ready, left click the dialogue box to return to the menu.",
	};
	
	int step=0;
	
	public Tutorial(Panel observer) {
		this.observer=observer;
		int midX=observer.getWidth()/2;
		int midY=observer.getHeight()/2;
		t.setText(dialogue[step]);
		initScene();
		t.setPos(smidX-300,sHeight-205);
	}
	
	public void initScene(){
		scene=new BufferedImage(sWidth,sHeight, BufferedImage.TYPE_INT_ARGB);
		g=scene.createGraphics();
		//g.setBackground(new Color(255,255,255,0));
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(new Color(170,70,180));
		g.fillRect(0, 0, oldWidth, oldHeight);
		//try {
		scene.flush();
		initScene();
		paintScene();
		//} catch (NullPointerException e) {
		//	initScene();
		//}
		int width=(int)(oldHeight*ratio);
		g.drawImage(scene,(oldWidth-width)/2, 0,width,oldHeight, null);
	}
	
	public void paintScene() {
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
				g.drawImage(Assets.UNICORN_B, smidX-size/2, midY-size/2-20,size,size, null);
			break;
			case 1,2:
				short s=(short)((dt/100)%20);
				if (s>10) {
					s=(short) (20-s);
				}
				
				length=(int)Math.min(dt/128, 16);
				int translate=(int)Math.min(dt/8, maxTranslate);
				
				
				size=(int) (128+s);
				
				int px=(int) Math.max(smidX-(dt/10), 148);
				g.drawImage(Assets.UNICORN_B, px-size/2, smidY-size/2-20,size,size, null);
				int drawSize=Math.min(translate,72);
				
				x=(int) (Math.floor(dt/1000)%30);
				if (x>15) {
					x=30-x;
				}
				g.drawImage(Assets.BOARD_1D, smidX+200-translate, smidY, (drawSize-4)*16+4, drawSize,null);
				
				int drawX=smidX+200-translate+x*(drawSize-4)+4;
				int drawY=smidY+4;
				g.fillOval(drawX, drawY, drawSize-9, drawSize-9);
				g.fillRoundRect(drawX+7,drawY-32,47,25,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x, drawX+14,drawY-14);
				g.setColor(Color.black);
				/*
				for (int i=0;i<length;i++) {
					int thisSize=(int) Math.min(translate-i*32,32);
					g.drawRect(midX+100-translate/2+i*drawSize, midY-thisSize/2, thisSize, thisSize);
					if (i==x) {
						g.fillOval(midX+100-translate/2+i*drawSize, midY-thisSize/2, thisSize, thisSize);
						g.fillRoundRect(midX+100-translate/2+i*drawSize-7,midY-thisSize/2-10-17,47,25,20, 20);
						g.setColor(Color.white);
						g.drawString("x: "+x, midX+100-translate/2+i*drawSize,midY-thisSize/2-10);
						g.setColor(Color.black);
						
					}
					
				}*/
				
			break;
			case 3:
				size=128-(int)Math.min(dt/5, 128);
				int littleSize=(-size+128)/2;
				if (size>0) {
					g.drawImage(Assets.UNICORN_B, 148-size/2, midY-size/2-20,size,size, null);
				}
				g.drawImage(Assets.BOARD_1D, smidX+200-maxTranslate, smidY, (72-4)*16+4, 72,null);
				
				drawX=smidX+200-maxTranslate+x*(72-4)+6;
				drawY=smidY+4;
				
				if (littleSize<64) {
					g.fillOval(drawX+littleSize/2, drawY+littleSize/2, 64-littleSize, 64-littleSize);
				}
				if (littleSize>0) {
					g.drawImage(Assets.UNICORN_B, drawX+64-littleSize, drawY+64-littleSize,littleSize,littleSize, null);
				}
				g.fillRoundRect(drawX+7,drawY-32,47,25,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x, drawX+14,drawY-14);
				g.setColor(Color.black);
				/*
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
					
				}*/
				
				
				
			break;
			case 4,5,6:
				
				length=(int)Math.min(dt/128, 8);
				translate=(int)Math.min(dt/8, maxTranslate);
				
				drawSize=Math.min(translate,32);
				
				x=(int) (Math.floor(dt/1000)%16);
				if (x>7) {
					x=15-x;
				}
				
				y=(int) (Math.floor(dt/8000)%14);
				if (y>7) {
					y=14-y;
				}
				
				g.drawImage(Assets.BOARD_2D,smidX+300-translate,smidY-320,548,548,observer);
				int drawStringX=smidX+300-translate+x*68+4;
				int drawStringY=smidY-320+548-64-y*68-4;
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,64,64, null);
				
				/*
				for (int i=0;i<length;i++) {
					for (int j=0;j<8;j++) {
						
						int thisSize=(int) Math.min(translate-i*32,32);
						g.drawRect(smidX+100-translate+i*drawSize, smidY-thisSize/2-j*drawSize, thisSize, thisSize);
						if (i==x&&j==y&&thisSize>0) {
							g.drawImage(Assets.UNICORN_B, smidX+116-translate+i*drawSize-thisSize/2, smidY-thisSize/2-j*drawSize,thisSize,thisSize, null);
							
							drawStringX=smidX+100-translate+i*drawSize;
							drawStringY=smidY-thisSize/2-26-j*drawSize;
							
							
							
						}
					
					}
				}*/
				
				g.fillRoundRect(drawStringX+7,drawStringY-48,47,41,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x, drawStringX+16,drawStringY-32);
				g.drawString("y: "+y, drawStringX+16,drawStringY-16);
				g.setColor(Color.black);
			break;
			case 7:
				length=8;
				drawStringX=-1;
				drawStringY=-1;
				
				g.drawImage(Assets.BOARD_2D,smidX+300-maxTranslate,smidY-320,548,548,observer);
				drawStringX=smidX+300-maxTranslate+x*68+4;
				drawStringY=smidY-320+548-64-y*68-4;
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,64,64, null);
				
				g.fillRoundRect(drawStringX+7,drawStringY-48,47,41,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x, drawStringX+16,drawStringY-32);
				g.drawString("y: "+y, drawStringX+16,drawStringY-16);
				g.setColor(Color.black);
			break;
			case 8:
				
				translate=(int)Math.min(dt/8, maxTranslate);
				
				drawSize=Math.min(translate,32);
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
				
				
				
				g.drawImage(Assets.BOARD_2DSM,smidX+500-translate,smidY-348+32,276,276,observer);
				drawStringX=smidX+500-translate+x*68+4;
				drawStringY=smidY-620+548-32-y*68-4;
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,64,64, null);
				
				g.fillRoundRect(drawStringX+7,drawStringY-48,47,41,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x, drawStringX+16,drawStringY-32);
				g.drawString("y: "+y, drawStringX+16,drawStringY-16);
				g.setColor(Color.black);
				
			break;
			case 9:
				
				length=(int)Math.min(dt/128, 4);
				translate=(int)Math.min(dt/8, maxTranslate);
				
				drawSize=Math.min(translate,32);
				
				x=(int) (Math.floor(dt/1000)%8);
				if (x>3) {
					x=7-x;
				}
				
				y=(int) (Math.floor(dt/4000)%6);
				if (y>3) {
					y=6-y;
				}
				
				g.drawImage(Assets.BOARD_2DSM,smidX+500-maxTranslate,smidY-348+32,276,276,observer);
				g.drawImage(Assets.BOARD_2DFLAT,smidX+100-translate,smidY-48+132,1144,88,observer);
				drawStringX=smidX+500-maxTranslate+x*68+4;
				drawStringY=smidY-620+548-32-y*68-4;
				
				drawX=smidX+100-translate+x*68+12+y*(68*4+12);
				drawY=smidY-620+548+32+132+4;
				
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,64,64, null);
				g.drawImage(Assets.UNICORN_B, drawX, drawY,64,64, null);
				
				g.fillRoundRect(drawStringX+7,drawStringY-48,47,41,20, 20);
				g.fillRoundRect(drawX+7,drawY-48,47,41,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x, drawStringX+16,drawStringY-32);
				g.drawString("y: "+y, drawStringX+16,drawStringY-16);
				
				g.drawString("x: "+x, drawX+16,drawY-32);
				g.drawString("y: "+y, drawX+16,drawY-16);
				g.setColor(Color.black);
				
			break;
			case 10:
				
				
				g.drawImage(Assets.BOARD_2DSM,smidX+500-maxTranslate,smidY-348+32,276,276,observer);
				g.drawImage(Assets.BOARD_2DFLAT,smidX+100-maxTranslate,smidY-48+132,1144,88,observer);
				drawStringX=smidX+500-maxTranslate+x*68+4;
				drawStringY=smidY-620+548-32-y*68-4;
				
				drawX=smidX+100-maxTranslate+x*68+12+y*(68*4+12);
				drawY=smidY-620+548+32+132+4;
				
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,64,64, null);
				g.drawImage(Assets.UNICORN_B, drawX, drawY,64,64, null);
				
				g.fillRoundRect(drawStringX+7,drawStringY-48,47,41,20, 20);
				g.fillRoundRect(drawX+7,drawY-48,47,41,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x, drawStringX+16,drawStringY-32);
				g.drawString("y: "+y, drawStringX+16,drawStringY-16);
				
				g.drawString("x: "+x, drawX+16,drawY-32);
				g.drawString("y: "+y, drawX+16,drawY-16);
				g.setColor(Color.black);
				
			break;
			case 11,12:
				
				length=(int)Math.min(dt/128, 4);
				translate=(int)Math.min(dt/8, 256);
				
				drawSize=Math.min(translate,32);
				
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
				
				
				g.drawImage(Assets.BOARD_3D,8,smidY-348+32,1344,357,null);
				
				drawStringX=8+x*77+z*(76*4+26)+30;
				drawStringY=smidY+357-348-32-y*77-28;
				
				
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,64,64, null);
				
				g.fillRoundRect(drawStringX+7,drawStringY-64,47,57,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x, drawStringX+16,drawStringY-48);
				g.drawString("y: "+y, drawStringX+16,drawStringY-32);
				g.drawString("z: "+z, drawStringX+16,drawStringY-16);
				g.setColor(Color.black);
				
			break;
			case 13:
				
				
				g.drawImage(Assets.BOARD_3D,8,smidY-348+32,1344,357,null);
				
				drawStringX=8+x*77+z*(76*4+26)+30;
				drawStringY=smidY+357-348-32-y*77-28;
				
				
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,64,64, null);
				
				g.fillRoundRect(drawStringX+7,drawStringY-64,47,57,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x, drawStringX+16,drawStringY-48);
				g.drawString("y: "+y, drawStringX+16,drawStringY-32);
				g.drawString("z: "+z, drawStringX+16,drawStringY-16);
				g.setColor(Color.black);
				
			break;
			case 16:
				
				length=(int)Math.min(dt/128, 4);
				translate=(int)Math.min(dt/8, 256);
				
				drawSize=Math.min(translate,32);
				
				x=(int) (Math.floor(dt/1000)%8);
				if (x>3) {
					x=7-x;
				}
				
				y=(int) (Math.floor(dt/4000)%8);
				if (y>3) {
					y=7-y;
				}
				
				z=(int) (Math.floor(dt/16000)%8);
				if (z>3) {
					z=7-z;
				}
				
				w=(int) (Math.floor(dt/64000)%6);
				if (w>3) {
					w=6-w;
				}
				
				g.drawImage(Assets.BOARD_S,smidX-384,0,768,768,null);
				int sq=44;
				int gap=16;
				int sq4=4*sq;
				int gap3d4=3*gap/4;
				drawStringX=smidX-384+x*sq+z*(gap3d4+sq4)+gap+2;
				drawStringY=768-y*sq-gap-w*(gap3d4+sq4)-sq+10;
				
				
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,32,32, null);
				
				g.fillRoundRect(drawStringX+32,drawStringY,140,25,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x+"  y: "+y+"  z: "+z+"  w: "+w, drawStringX+40,drawStringY+18);
				g.setColor(Color.black);
				
			break;
			case 17,18:
				
				length=(int)Math.min(dt/128, 4);
				translate=(int)Math.min(dt/8, 256);
				
				drawSize=Math.min(translate,32);
				
				
				g.drawImage(Assets.BOARD_S,smidX-384,0,768,768,null);
				sq=44;
				gap=16;
				sq4=4*sq;
				gap3d4=3*gap/4;
				drawStringX=smidX-384+x*sq+z*(gap3d4+sq4)+gap+2;
				drawStringY=768-y*sq-gap-w*(gap3d4+sq4)-sq+10;
				
				
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,32,32, null);
				
				g.fillRoundRect(drawStringX+32,drawStringY,140,25,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x+"  y: "+y+"  z: "+z+"  w: "+w, drawStringX+40,drawStringY+18);
				g.setColor(Color.black);
				
			break;
		}
		if (t.getY()==sHeight-40) {
			float opacity = 0.3f;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
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
			drawSceneWidth=(int)(oldHeight*ratio);
			drawSceneX=(oldWidth-drawSceneWidth)/2;
		}
		dt=System.currentTimeMillis()-time;
		switch (step) {
			
		}
		
	}
	
	public Point convert(Point p) {
		return new Point((int)(((double)(p.x-drawSceneX)/drawSceneWidth)*sWidth),(int)((double)(p.y)/oldHeight*sHeight));
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_BACK_SLASH) {
			Main.cmd.setVisible(true);
		}  else if (e.getKeyCode()==KeyEvent.VK_M) {
			observer.toggleMusic();
		}  else if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
			observer.setScreen("title");
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
		Point p=convert(e.getPoint());
		if (t.contains(p)) {
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
					t.setText(dialogue[step]);
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
		Point p=convert(e.getPoint());
		if (t.contains(p)) {
			t.setPos(smidX-300,sHeight-205);
		} else {
			t.setPos(smidX-300,sHeight-40);
		}
		switch (step) {
		
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
