package unicorns.screens;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import gameutil.math.geom.Point;
import gameutil.math.geom.Tuple;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentHashMap;

import graphics.screen.Screen;
import unicorns.Assets;
import unicorns.Board;
import unicorns.Main;
import unicorns.Panel;
import unicorns.Piece;
import unicorns.Textbox;
import unicorns.screens.Game.STATE;

public class TutorialIntro extends TutorialScreen{
	
	final int maxTranslate=612;
	
	int bound=15;
	
	public TutorialIntro(Panel observer) {
		super(observer,new String[]{
		//0
		"Welcome to the tutorial!",
		//1
		"Let's start with one dimension. In one dimension, you can only move back and fourth and you need one number to describe your position. Let's call that number x.",
		//2
		"So when you move right one square your x goes up by one and when you move left one square your x goes down by one.",
		//3
		"Go ahead and play around a little bit on this one dimensional board. Use the A and D keys to move left and right.",
		//4
		"Alright, now that you understand movement in position in one dimension, lets take it up a dimension. In two dimensions you need two numbers to describe your position. We can call these numbers x and y.",
		//5
		"Now there are four basic directions that you can move in: left, right, up, and down. Every dimension corresponds to two basic directions.",
		//6
		"In this case, one dimension corresponds to left and right while the other dimension corresponds to up and down. Moving left and right changes x just like in one dimension and moving up and down changes y. Because of this, it makes sense to call the dimension corresponding to left and right the x dimension and the dimension corresponding to up and down the y dimension.",
		//7
		"Go ahead and play around a little bit on this two dimensional board. Use the A and D keys to move left and right and the W and S keys to move up and down.",
		//8
		"Before continuing to three dimensions we need to go over dimensional squishification because without dimensional squishification, you can't look at anything with more than two dimensions all at the same time.",
		//9
		"There are many ways one can perform dimensional squishification but the kind used by most high dimensional board games (including 4D chess without multiverse timetravel) is to take all of the rows of a two dimensional board and lay them out next to each other in a line so that you can see every part of the two dimensional board in a one dimensional view.",
		//10
		"Go ahead and play around a little bit on this two dimensional board and pay attention to how the one dimensional view changes as you move.",
		//11
		"Hopefully you have a grasp on how to interpret a squishified view now. If you're still feeling confused, you can right click the dialogue box to go back to the last part of the tutorial and keep honing your intuitions.",
		//12
		"Above is a three dimensional board. The the x dimension and the z dimension have been squishified to make a two dimensional view of the entire board. The z dimension corresponds the basic directions forward and backward.",
		//13
		"Go ahead and play around a little bit on this three dimensional board and pay attention to how the position numbers and view change as you move. Use the A and D keys to move left and right, the Q and E keys to move forward and backward, and the W and S keys to move up and down.",
		//14
		"Now it's time four four dimensions!",
		//15
		"I mean... Now it's time for four dimensions!",
		//16
		"Above is four dimensional board. The x and z dimensions are squishified horizontally and the y and w dimensions are are squishified vertically. The w dimension corresponds to the directions ana and kata (also known by some as wint and zant or on and again).",
		//17
		"Go ahead and play around a little bit on this four dimensional board and pay attention to how the position numbers and view change as you move. Use the A and D keys to move left and right, the Q and E keys to move forward and backward, the W and S keys to move up and down, and the Space and Shift keys to move ana and kata.",
		//18
		"Congratulations! You finished the Intro to 4D tutorial! Once you're ready, left click the dialogue box to return to the tutorial selection screen.",
	});
		
	}
	
	public void paintScene(Graphics2D g) {
		super.paintScene(g);
		g.setColor(Color.black);
		int size;
		int length;
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
				g.drawImage(Assets.UNICORN_B, observer.smidX-size/2, midY-size/2-20,size,size, null);
			break;
			case 1,2:
				short s=(short)((dt/100)%20);
				if (s>10) {
					s=(short) (20-s);
				}
				
				length=(int)Math.min(dt/128, 16);
				int translate=(int)Math.min(dt/8, maxTranslate);
				
				
				size=(int) (128+s);
				
				int px=(int) Math.max(observer.smidX-(dt/10), 148);
				g.drawImage(Assets.UNICORN_B, px-size/2, observer.smidY-size/2-20,size,size, null);
				int drawSize=Math.min(translate,72);
				
				x=(int) (Math.floor(dt/1000)%30);
				if (x>15) {
					x=30-x;
				}
				g.drawImage(Assets.BOARD_1D, observer.smidX+200-translate, observer.smidY, (drawSize-4)*16+4, drawSize,null);
				
				int drawX=observer.smidX+200-translate+x*(drawSize-4)+4;
				int drawY=observer.smidY+4;
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
				g.drawImage(Assets.BOARD_1D, observer.smidX+200-maxTranslate, observer.smidY, (72-4)*16+4, 72,null);
				
				drawX=observer.smidX+200-maxTranslate+x*(72-4)+6;
				drawY=observer.smidY+4;
				
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
				
				g.drawImage(Assets.BOARD_2D,observer.smidX+300-translate,observer.smidY-320,548,548,observer);
				int drawStringX=observer.smidX+300-translate+x*68+4;
				int drawStringY=observer.smidY-320+548-64-y*68-4;
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,64,64, null);
				
				/*
				for (int i=0;i<length;i++) {
					for (int j=0;j<8;j++) {
						
						int thisSize=(int) Math.min(translate-i*32,32);
						g.drawRect(observer.smidX+100-translate+i*drawSize, observer.smidY-thisSize/2-j*drawSize, thisSize, thisSize);
						if (i==x&&j==y&&thisSize>0) {
							g.drawImage(Assets.UNICORN_B, observer.smidX+116-translate+i*drawSize-thisSize/2, observer.smidY-thisSize/2-j*drawSize,thisSize,thisSize, null);
							
							drawStringX=observer.smidX+100-translate+i*drawSize;
							drawStringY=observer.smidY-thisSize/2-26-j*drawSize;
							
							
							
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
				
				g.drawImage(Assets.BOARD_2D,observer.smidX+300-maxTranslate,observer.smidY-320,548,548,observer);
				drawStringX=observer.smidX+300-maxTranslate+x*68+4;
				drawStringY=observer.smidY-320+548-64-y*68-4;
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
				
				
				
				g.drawImage(Assets.BOARD_2DSM,observer.smidX+500-translate,observer.smidY-348+32,276,276,observer);
				drawStringX=observer.smidX+500-translate+x*68+4;
				drawStringY=observer.smidY-620+548-32-y*68-4;
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
				
				g.drawImage(Assets.BOARD_2DSM,observer.smidX+500-maxTranslate,observer.smidY-348+32,276,276,observer);
				g.drawImage(Assets.BOARD_2DFLAT,observer.smidX+100-translate,observer.smidY-48+132,1144,88,observer);
				drawStringX=observer.smidX+500-maxTranslate+x*68+4;
				drawStringY=observer.smidY-620+548-32-y*68-4;
				
				drawX=observer.smidX+100-translate+x*68+12+y*(68*4+12);
				drawY=observer.smidY-620+548+32+132+4;
				
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
				
				
				g.drawImage(Assets.BOARD_2DSM,observer.smidX+500-maxTranslate,observer.smidY-348+32,276,276,observer);
				g.drawImage(Assets.BOARD_2DFLAT,observer.smidX+100-maxTranslate,observer.smidY-48+132,1144,88,observer);
				drawStringX=observer.smidX+500-maxTranslate+x*68+4;
				drawStringY=observer.smidY-620+548-32-y*68-4;
				
				drawX=observer.smidX+100-maxTranslate+x*68+12+y*(68*4+12);
				drawY=observer.smidY-620+548+32+132+4;
				
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
				
				
				g.drawImage(Assets.BOARD_3D,8,observer.smidY-348+32,1344,357,null);
				
				drawStringX=8+x*77+z*(76*4+26)+30;
				drawStringY=observer.smidY+357-348-32-y*77-28;
				
				
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,64,64, null);
				
				g.fillRoundRect(drawStringX+7,drawStringY-64,47,57,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x, drawStringX+16,drawStringY-48);
				g.drawString("y: "+y, drawStringX+16,drawStringY-32);
				g.drawString("z: "+z, drawStringX+16,drawStringY-16);
				g.setColor(Color.black);
				
			break;
			case 13:
				
				
				g.drawImage(Assets.BOARD_3D,8,observer.smidY-348+32,1344,357,null);
				
				drawStringX=8+x*77+z*(76*4+26)+30;
				drawStringY=observer.smidY+357-348-32-y*77-28;
				
				
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
				drawBoard(g);
				//g.drawImage(Assets.BOARD_S,observer.smidX-384,0,768,768,null);
				/*int sq=44;
				int gap=16;
				int sq4=4*sq;
				int gap3d4=3*gap/4;*/
				drawStringX=toScreenX(x,z);//observer.smidX-384+x*sq+z*(gap3d4+sq4)+gap+2;
				drawStringY=toScreenY(y,w);//768-y*sq-gap-w*(gap3d4+sq4)-sq+10;
				
				
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
				
				drawBoard(g);
				//g.drawImage(Assets.BOARD_S,observer.smidX-384,0,768,768,null);
				/*sq=44;
				gap=16;
				sq4=4*sq;
				gap3d4=3*gap/4;*/
				drawStringX=toScreenX(x,z);//observer.smidX-384+x*sq+z*(gap3d4+sq4)+gap+2;
				drawStringY=toScreenY(y,w);//768-y*sq-gap-w*(gap3d4+sq4)-sq+10;
				
				
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,32,32, null);
				
				g.fillRoundRect(drawStringX+32,drawStringY,140,25,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x+"  y: "+y+"  z: "+z+"  w: "+w, drawStringX+40,drawStringY+18);
				g.setColor(Color.black);
				
			break;
			
			
		}
		
		drawTextbox(g);
	}

	@Override
	public void update() {
		super.update();
		
	}
	
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
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
	
	public void dialogueStep(int step) {
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
				time=System.currentTimeMillis();
			break;
			
			default:
				time=System.currentTimeMillis();
			break;
		}
	}
	
	public void dialogueStepBack(int step) {
		switch (step) {
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
		
	}
	}
	
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
	}
	
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		java.awt.Point p=convert(e.getPoint());
		
	}

	@Override
	protected void keyDown(int key) {
		// TODO Auto-generated method stub
		
	}
}
