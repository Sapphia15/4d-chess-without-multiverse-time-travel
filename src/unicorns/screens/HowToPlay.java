package unicorns.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;

import gameutil.math.geom.Point;
import unicorns.Assets;
import unicorns.Board;
import unicorns.Main;
import unicorns.Panel;
import unicorns.Piece;
import unicorns.screens.Game.STATE;

public class HowToPlay extends TutorialScreen{

	String space="a0α0";
	
	Point promoteSquare=null;
	String promotePiece="";
	Hashtable<Rectangle,Piece> promotablesW=new Hashtable<>();
	
	public HowToPlay(Panel observer) {
		super(observer, new String[] {
				//0
				"Alright, now that you have a basic understanding a four dimensional space, lets talk chess. Before moving on I should note that in 4D chess the basic direction names are a little different from what I said a momement ago. In 4D chess the y axis corresponds to the forward and backward directions, the w axis corresponds to the up and down directions, the x axis corresponds to the right and left directions, and the z axis corresponds to the ana and kata directions.",
				//1
				"The reason for this is because both forward and up go toward your opponent's back square of pieces but the x and z axis are still squishified horizontally and the y and w axis are still squishified vertically.",
				//2
				"Okay, on to the first piece: the rook can move in any one of the 8 basic directions any number of spaces as long there isn't a piece in the way.",
				//3
				"Go ahead and play around a little bit with this rook. I've added some crystal blocks to the board so that you can see how obstacles affect your movement. Click the rook to select it. While the rook is selected you can click one of the green squares that show where it can move to move it there.",
				//4
				"Now that you are familiar with the rook, it's time to learn about bishops. Bishops move in two basic directions at the same time. For example, a bishop could move left and forward at the same time. Another way to think about this is to say that when bishops move, two of their position numbers must change by the same amount and the rest stay the same. So a bishop at (3,5,2,0) could move to (4,5,1,0) because the the x and z values both changed by 1 while all of the other position values stayed the same.",
				//5
				"Go ahead and play around a little bit with this bishop. Note that bishops can only access half of the squares on the board no matter how many times they move. As you continue on with the tutorial try to pay attention to the patterns in positional numbers of the starting positions and legal ending positions of the pieces that you're playing around with. You may have also noticed the mix of letters and numbers below the x y z w coordinates.",
				//6
				"Those are just another way of representing the coordinates which is more compact and it's what is used in 4D chess notation. The first letter is the x coordinate, the first number is the y coordinate, the greek letter is the z coordinate and the second number is the w coordinate. The letters correspond to numbers in the following way:\n\n"
				+ "a = α = 0         b = β = 1         c = γ = 2         d = δ = 3",
				//7
				"This is my favorite part! In 4D chess unicorns move in three basic directions at the same time. For example, a unicorn could move forward, up, and left at the same time. Similar to bishops, you can think of this as the unicorn only being able to move so that three of it's numbers change by the same amount and the rest stay the same. So a unicorn at (3,3,0,3) could move to (1,1,2,3) because the x, y, and z values all changed by 2 and the w value stayed the same.",
				//8
				"Go ahead and play around a little bit with this other cute little unicorn and remember to pay attention to how the numbers are allowed to change.",
				//9
				"Unicorns are cool because they can move in three dimensions at a time but they still have access to every square on the board.",
				//10
				"Aaaaaaah! It's a dragon!",
				//11
				"Just kidding, this is my friend Delilah and she's a very nice dragon that you don't need to fear.",
				//12 Delilah
				"Hi! Dragons like me move quadragonally!",
				//13
				"...",
				//14
				"Get it? Qua-DRAGON-ally?",
				//15
				"Haha! Okay, anyway, what that actually means is that dragons move in four basic directions at a time. Like, I could go backward, up, left, and kata all at the same time. That means that when I move all of my position values have to change by the same amount. Like, if I was at (2,1,2,1) I could go to (0,3,0,3) since all my positional numbers would change by 2.",
				//16
				"Alright, now try telling where to go until you feel very comfortable ordering me around. I recolored the board with the default coloring for 4D chess which shows the quadragonals",
				//17
				"I'm sure you must have noticed that I can't access very many squares. In fact, I can only get to an eighth of the spaces on the board only moving quadragonally because there are 8 quadragonalls.",
				//18
				"That's just a price we dragons have to pay to be able to move in all four dimensions at the same time...",
				//19 Dee
				"I hope you had a fun with Delilah but now it's time to spend some more time with me as I teach you the ways of the other pieces.",
				//20 Delilah
				"You mean you're not going to introduce them to the others?",
				//21 Dee
				"What do you mean? This is a tutorial, not a 'get to know your chess pieces event'!",
				//22 Queen Xeraphina
				"I, Queen Xeraphina, hereby declare this part of the tutorial as a get to know your pieces event. This is Bob the bishop, and those are his boyfriends Bert, Billy, and Brad. They always seem to be frowning because they still haven't figured out how to do a four way kiss.",
				//23 Queen Fnora
				"Xeraphina, my love, why don't you show the player how you move?",
				//24 Queen Xeraphina
				"Alright, hit the music! I can move like a rook, like a bishop, like a unicorn, or even like a dragon if I want. Dance with me.",
				//25 Dee (Xeraphina's dance music plays)
				"Help Queen Fnora dance with Queen Xeraphina. The dance will end when the queens kiss.",
				//26 Queen Fnora
				"Mua!",
				//27 Dee
				"Good job!  You helped Queen Fnora kiss Queen Xeraphina, completing their dance. Hopefully you understand how queens move now.",
				//28 Queen Xeraphina
				"Now I think it's time you met the kings. This is King Zion and that's his husband King Fnipper.",
				//29 King Fnipper
				"Us kings are gettin' old so we can only move one space at a time, but we can move in the same directions as them athletic queens. In a game of chess the goal is to protect the elderly -- which is just the two of us kings since all them other pieces are still young and spry. I sure do envy them whippersnappers...",
				//30
				"So if your king could be captured on the next turn then ya gotta stop that from happening and if ya can't, ya lose.",
				//31
				"Hows about ya give a go at moving a king around.",
				//32 King Zion
				"I hope you had fun with my husband. He's quite  the catch isn't he? Now I think Queen Xeraphina is going to introduce you to her knights.",
				//33 Queen Xeraphina
				"Meet Stella, her wife Bella, and Ben and his partner Zee. They can move two spaces in one basic direction, and then one space in a different basic direction (which must also correspond to a different dimension than the first basic direction) jumping over pieces. So basically, one position number must change by 2 and a different position number must change by 1. For example, a knight at (0,0,0,0) could move to (0,2,1,0).",
				//34 Zee
				"Hi! I'm Zee! Go ahead and try moving me around to see how I move. Pay attention to how my position numbers are allowed to change.",
				//35 Stella
				"Okay, now that you can move around a knight, a queen, a king, a dragon, a unicorn, a bishop, and a rook, the final piece you must learn about is the pawn.",
				//36
				"Before I tell you how pawns move I think it will good to review forward and sideways directions.",
				//37
				"The forward directions are forward and up while the sideways directions are left, right, ana, and kata. So when moving in a forwards direction, either y or w should increase and when moving in a horizontal direction.",
				//38
				"Pawns can move one space in a forward direction as long as they don't capture a piece. Pawns can capture one space diagonally as long as the diagonal consists of one forward direction and one sideways direction. For example, a pawn could capture a piece that is one space up and to the left of it but not a piece that is kata and to the right of it, nor could it take a piece that is forward and up from it.",
				//39
				"The first time a pawn moves, it may make move without capturing a piece twice in the same turn. For example, a pawn could move up and then forward on it's first turn or it could move forward twice but it could't move forward and then capture a piece. A ghost pawn appears in the first space a pawn moves through to indicate the path it took to get to it's current position. The ghost pawn is removed on the next move and it does not obstuct the movement of pieces, but an opposing pawn may capture it which will cause the pawn that created the ghost pawn to be captured as if it was captured in passing. This type of capture is called en passant.",
				//40
				"The pawn is the most complicated piece in the game, so I've layed out some captureable crystals and some pawns for you to play with. Once you get a pawn to the opposite plane it will promote to another piece of your choice. Press F to submit your move and press Z to undo your move if you haven't submitted it yet. You won't be able to make another move until you submit your current move; this system allows you to articulate the movement of your pawns on their first turn and these bindings are the same in an actual game of 4D chess.",
				//41
				"Now that you've had some practice with the basic movements of a pawn, it's time for you to get a little experience with en passant. Take the opposing pawn by capturing it's ghost pawn with your pawn.",
				//42 Dee
				"Congratulations! You completed the tutorial! Now go have fun playing 4D chess! Click the dialogue box to return to the tutorial selection screen.",
		});
		int offX=observer.smidX-384;
		int num=768;
		promotablesW.put(new Rectangle(offX+num+sq,offY,sq,sq),new Piece(null,'Q'));
		promotablesW.put(new Rectangle(offX+num+sq,offY+sq,sq,sq),new Piece(null,'D'));
		promotablesW.put(new Rectangle(offX+num+sq,offY+sq*2,sq,sq),new Piece(null,'U'));
		promotablesW.put(new Rectangle(offX+num+sq,offY+sq*3,sq,sq),new Piece(null,'B'));
		promotablesW.put(new Rectangle(offX+num+sq,offY+sq*4,sq,sq),new Piece(null,'R'));
		promotablesW.put(new Rectangle(offX+num+sq,offY+sq*5,sq,sq),new Piece(null,'N'));
		
	}
	
	boolean white=true;
	
	public void paintScene(Graphics2D g) {
		super.paintScene(g);
		drawBoardAndPieces(g);
		g.setColor(Color.black);
		switch (step) {
			case 0:
				
				int length=(int)Math.min(dt/128, 4);
				int translate=(int)Math.min(dt/8, 256);
				
				int drawSize=Math.min(translate,32);
				
				//g.drawImage(Assets.BOARD_S,observer.smidX-384,0,768,768,null);
				/*sq=44;
				gap=16;
				sq4=4*sq;
				gap3d4=3*gap/4;*/
				int drawStringX=toScreenX(x,z);//observer.smidX-384+x*sq+z*(gap3d4+sq4)+gap+2;
				int drawStringY=toScreenY(y,w);//768-y*sq-gap-w*(gap3d4+sq4)-sq+10;
				
				
				g.drawImage(Assets.UNICORN_B, drawStringX, drawStringY,32,32, null);
				
				g.fillRoundRect(drawStringX+32,drawStringY,140,25,20, 20);
				g.setColor(Color.white);
				g.drawString("x: "+x+"  y: "+y+"  z: "+z+"  w: "+w, drawStringX+40,drawStringY+18);
				g.setColor(Color.black);
				
			break;
			case 1:
				translate=(int)(dt/500%7);
				if (translate>3) {
					translate=3;
				}
				int translateW=(int)(dt/500%7);
				if (translateW<4) {
					translateW=0;
				} else {
					translateW=translateW-3;
				}
				
				for (int i=0;i<4;i++) {
					for (int j=0;j<4;j++) {
						g.setColor(new Color(255,50,50,200));
						g.fillRect(toScreenX(i,j)-2, toScreenY(3,3)-6, 40, 40);
						if (translateW==3) {
							g.setColor(new Color(255,255,255,200));
							g.fillRect(toScreenX(i,j)-2, toScreenY(translate,translateW)-6, 40, 40);
						} else {
							g.setColor(new Color(50,255,50,200));
							g.fillRect(toScreenX(i,j)-2, toScreenY(translate,translateW)-6, 40, 40);
							
							g.setColor(new Color(50,50,255,200));
							g.fillRect(toScreenX(i,j)-2, toScreenY(translateW,translate)-6, 40, 40);
						}
					}
				}
			break;
			case 2:
				
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
				this.b.getSelectedPiece().setPos(new Point(new double[] {x,y,z,w}));
				this.b.selectPiece(this.b.getSelectedPiece());
				
				
			break;
			case 10,11,12,13,14,15:
				int size=(int) (dt/8);
				int drift=(size/10)%16;
				if (drift>8) {
					drift=16-drift;
				}
				if (size>128) {
					size=128;
				}
				g.drawImage(Assets.DRAGON_B, observer.smidX-size/2, observer.smidY-size/2+drift,size,size, null);
			break;
			case 19,42:
				size=(int) (dt/8);
				drift=(size/10)%16;
				if (drift>8) {
					drift=16-drift;
				}
				if (size>128) {
					size=128;
				}
				g.drawImage(Assets.UNICORN_B, observer.smidX-size/2, observer.smidY-size/2+drift,size,size, null);
			break;
			case 20,21:
				size=(int) (dt/8);
				drift=(size/10)%16;
				if (drift>8) {
					drift=16-drift;
				}
				if (size>128) {
					size=128;
				}
				g.drawImage(Assets.UNICORN_B, observer.smidX-64-size, observer.smidY-64+drift,128,128, null);
				g.drawImage(Assets.QUEEN_W, observer.smidX+64-size/2, observer.smidY-size/2+drift,size,size, null);
			break;
			case 22:
				size=(int) (dt/8);
				drift=(size/10)%16;
				if (drift>8) {
					drift=16-drift;
				}
				if (size>128) {
					size=128;
				}
				g.drawImage(Assets.UNICORN_B, observer.smidX-64-128, observer.smidY-64+drift,128,128, null);
				g.drawImage(Assets.QUEEN_W, observer.smidX, observer.smidY-64+drift,128,128, null);
				g.drawImage(Assets.BISHOP_B, observer.smidX-size/2,observer.smidY-192+drift,size,size,null);
				g.setFont(g.getFont().deriveFont(40f));
				g.setColor(Color.white);
				g.fillRoundRect(observer.smidX-64,observer.smidY-234+drift,128,40,20,20);
				g.setColor(Color.black);
				g.drawString("Bob", observer.smidX-32, observer.smidY-202+drift);
				
				size=size/2;
				g.drawImage(Assets.BISHOP_B, observer.smidX-size/2-128,observer.smidY-192-32+drift,size,size,null);
				g.setFont(g.getFont().deriveFont(40f));
				g.setColor(Color.white);
				g.fillRoundRect(observer.smidX-64-128,observer.smidY-234-32+drift,128,40,20,20);
				g.setColor(Color.black);
				g.drawString("Bert", observer.smidX-32-128, observer.smidY-202-32+drift);
				
				g.drawImage(Assets.BISHOP_W, observer.smidX-size/2+128,observer.smidY-192-32+drift,size,size,null);
				g.setFont(g.getFont().deriveFont(40f));
				g.setColor(Color.white);
				g.fillRoundRect(observer.smidX-64+128,observer.smidY-234-32+drift,128,40,20,20);
				g.setColor(Color.black);
				g.drawString("Billy", observer.smidX-32+128, observer.smidY-202-32+drift);
				
				
				size=size/2;
				g.drawImage(Assets.BISHOP_W, observer.smidX-size/2-128-128,observer.smidY-192-32-32+drift,size,size,null);
				g.setFont(g.getFont().deriveFont(40f));
				g.setColor(Color.white);
				g.fillRoundRect(observer.smidX-64-128-128,observer.smidY-234-32-32+drift,128,40,20,20);
				g.setColor(Color.black);
				g.drawString("Brad", observer.smidX-32-128-128, observer.smidY-202-32-32+drift);
				
				g.drawImage(Assets.HEART,observer.smidX-size-drift/2,observer.smidY-192-128-drift/2-size,size*2+drift,size*2+drift,null);
				
				g.setFont(g.getFont().deriveFont(16f));
			break;
			case 23:
				size=(int) (dt/8);
				drift=(size/10)%16;
				if (drift>8) {
					drift=16-drift;
				}
				if (size>128) {
					size=128;
				}
				g.drawImage(Assets.QUEEN_B, observer.smidX-size/2, observer.smidY-size/2+drift,size,size, null);
			break;
			case 24:
				
				size=(int) (dt/8);
				drift=(size/10)%16;
				if (drift>8) {
					drift=16-drift;
				}
				if (size>128) {
					size=128;
				}
				
				g.drawImage(Assets.QUEEN_B, observer.smidX-64-size, observer.smidY-64+drift,128,128, null);
				g.drawImage(Assets.QUEEN_W, observer.smidX+64-size/2, observer.smidY-size/2+drift,size,size, null);
			break;
			case 26,27:
				size=(int) (dt/8);
				drift=(size/10)%16;
				if (drift>8) {
					drift=16-drift;
				}
				if (size>128) {
					size=128;
				}
				
				g.drawImage(Assets.HEART,observer.smidX-size-drift/2,observer.smidY-192-drift/2-size,size*2+drift,size*2+drift,null);
				
				AffineTransform tx = AffineTransform.getRotateInstance(-Math.PI/8, 16, 16);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				ImageIcon icon=new ImageIcon(Assets.QUEEN_B);
				BufferedImage bi = new BufferedImage(
				    icon.getIconWidth(),
				    icon.getIconHeight(),
				    BufferedImage.TYPE_INT_ARGB);
				Graphics g2 = bi.createGraphics();
				// paint the Icon to the BufferedImage.
				icon.paintIcon(null, g2, 0,0);
				g2.dispose();
				g.drawImage(op.filter(bi,null), observer.smidX-size, observer.smidY-64+drift,size,size, null);
				
				
				tx = AffineTransform.getRotateInstance(Math.PI/8, 16, 16);
				op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				icon =new ImageIcon(Assets.QUEEN_W);
				bi = new BufferedImage(
				    icon.getIconWidth(),
				    icon.getIconHeight(),
				    BufferedImage.TYPE_INT_ARGB);
				g2 = bi.createGraphics();
				// paint the Icon to the BufferedImage.
				icon.paintIcon(null, g2, 0,0);
				g2.dispose();
				g.drawImage(op.filter(bi,null), observer.smidX-112+size/2, observer.smidY-size/2+drift,size,size, null);
			break;
			case 28,29,30:
				size=(int) (dt/8);
				drift=(size/10)%16;
				if (drift>8) {
					drift=16-drift;
				}
				if (size>128) {
					size=128;
				}
				g.drawImage(Assets.KING_B, observer.smidX-size/2-size, observer.smidY-size/2+drift,size,size, null);
				g.drawImage(Assets.KING_W, observer.smidX+64-size/2, observer.smidY-size/2+drift,size,size, null);
				g.setFont(g.getFont().deriveFont(40f));
				g.setColor(Color.white);
				g.fillRoundRect(observer.smidX-size-64-5,observer.smidY-size/2+drift-65,188,45,20,20);
				g.fillRoundRect(observer.smidX-5,observer.smidY-size/2+drift-65,228,45,20,20);
				g.setColor(Color.black);
				g.drawString("King Zion", observer.smidX-size-64, observer.smidY-size/2+drift-30);
				g.drawString("King Fnipper", observer.smidX, observer.smidY-size/2+drift-30);
				g.setFont(g.getFont().deriveFont(16f));
			break;
			case 33:
				size=(int) (dt/8);
				drift=(size/10)%16;
				if (drift>8) {
					drift=16-drift;
				}
				if (size>128) {
					size=128;
				}
				
				g.drawImage(Assets.QUEEN_W, observer.smidX, observer.smidY-64+drift,128,128, null);
				g.drawImage(Assets.KNIGHT_B, observer.smidX-size/2,observer.smidY-192+drift,size,size,null);
				g.setFont(g.getFont().deriveFont(40f));
				g.setColor(Color.white);
				g.fillRoundRect(observer.smidX-64,observer.smidY-234+drift,128,40,20,20);
				g.setColor(Color.black);
				g.drawString("Zee", observer.smidX-32, observer.smidY-202+drift);
				
				size=size/2;
				g.drawImage(Assets.KNIGHT_W, observer.smidX-size/2-128,observer.smidY-192-32+drift,size,size,null);
				g.setFont(g.getFont().deriveFont(40f));
				g.setColor(Color.white);
				g.fillRoundRect(observer.smidX-64-128,observer.smidY-234-32+drift,128,40,20,20);
				g.setColor(Color.black);
				g.drawString("Stella", observer.smidX-32-128-16, observer.smidY-202-32+drift);
				
				g.drawImage(Assets.KNIGHT_B, observer.smidX-size/2+128,observer.smidY-192-32+drift,size,size,null);
				g.setFont(g.getFont().deriveFont(40f));
				g.setColor(Color.white);
				g.fillRoundRect(observer.smidX-64+128,observer.smidY-234-32+drift,128,40,20,20);
				g.setColor(Color.black);
				g.drawString("Ben", observer.smidX-32+128, observer.smidY-202-32+drift);
				
				
				size=size/2;
				g.drawImage(Assets.KNIGHT_W, observer.smidX-size/2-128-128,observer.smidY-192-32-32+drift,size,size,null);
				g.setFont(g.getFont().deriveFont(40f));
				g.setColor(Color.white);
				g.fillRoundRect(observer.smidX-64-128-128,observer.smidY-234-32-32+drift,128,40,20,20);
				g.setColor(Color.black);
				g.drawString("Bella", observer.smidX-32-128-128-8, observer.smidY-202-32-32+drift);
				
				
				tx = AffineTransform.getRotateInstance(-Math.PI/8, 16, 16);
				op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				icon =new ImageIcon(Assets.HEART);
				bi = new BufferedImage(
				    icon.getIconWidth(),
				    icon.getIconHeight(),
				    BufferedImage.TYPE_INT_ARGB);
				g2 = bi.createGraphics();
				// paint the Icon to the BufferedImage.
				icon.paintIcon(null, g2, 0,0);
				g2.dispose();
				
				g.drawImage(op.filter(bi, null),observer.smidX-size-drift/2+32,observer.smidY-192-96-drift/2-size,size*2+drift,size*2+drift,null);
				
				tx = AffineTransform.getRotateInstance(Math.PI/8, 16, 16);
				op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				icon =new ImageIcon(Assets.HEART);
				bi = new BufferedImage(
				    icon.getIconWidth(),
				    icon.getIconHeight(),
				    BufferedImage.TYPE_INT_ARGB);
				g2 = bi.createGraphics();
				// paint the Icon to the BufferedImage.
				icon.paintIcon(null, g2, 0,0);
				g2.dispose();
				g.drawImage(op.filter(bi, null),observer.smidX-size-drift/2-134,observer.smidY-192-128-drift/2-size,size*2+drift,size*2+drift,null);
				
				g.setFont(g.getFont().deriveFont(16f));
			break;
			case 35,36,37,38,39:
				size=(int) (dt/8);
				drift=(size/10)%16;
				if (drift>8) {
					drift=16-drift;
				}
				if (size>128) {
					size=128;
				}
				g.drawImage(Assets.KNIGHT_W, observer.smidX-size/2, observer.smidY-size/2+drift,size,size, null);
			break;
			case 40,41:
				
				
				if (state==STATE.promote) {
					for (Rectangle r:promotablesW.keySet()) {
						g.setColor(new Color(50,50,50,100));
						g.fillRoundRect(r.x,r.y,r.width,r.height,10,10);
						g.drawImage(promotablesW.get(r).getImage(), r.x, r.y, r.width,r.height, null);
					}
				}
			break;
		}
		g.setColor(Color.black);
		g.fillRoundRect(20, 20, 200,40, 20, 20);
		g.fillRoundRect(20, 80, 200, (int)Math.floor(sq*1.2),40,40);
		g.setColor(Color.white);
		g.drawString("x: "+x+"  y: "+y+"  z: "+z+"  w: "+w, 50, 45);
		
		g.setFont(g.getFont().deriveFont(40f));
		g.drawString(space,70,120);
		g.setFont(g.getFont().deriveFont(16f));
		drawTextbox(g);
	}
	
	public void dialogueStep(int step) {
		switch (step) {
			case 11,37,38,39:
				
			break;
			case 0:
				bound=3;
			break;
			case 1:
				b.setState(new Board());
			break;
			case 2:
				Piece rook=new Piece(new Point(new double[] {2,2,2,2}), 'R');
				b.setUp(new Piece[] {rook});
				b.selectPiece(rook);
				time=System.currentTimeMillis();
			break;
			case 3:
				rook=new Piece(new Point(new double[] {2,2,2,2}), 'R');
				b.setUp(new Piece[] {
						rook,
						new Piece(new Point(new double[] {1,2,1,1}), 'C'),
						new Piece(new Point(new double[] {0,2,2,0}), 'C'),
						new Piece(new Point(new double[] {2,3,0,2}), 'C'),
				});
				b.deselectPiece();
				time=System.currentTimeMillis();
			break;
			case 4:
				b.setUp(new Piece[] {
						new Piece(new Point(new double[] {2,1,2,1}), 'B'),
						new Piece(new Point(new double[] {1,2,1,1}), 'C'),
						new Piece(new Point(new double[] {0,2,2,0}), 'C'),
						new Piece(new Point(new double[] {2,3,0,2}), 'C'),
				});
				b.deselectPiece();
				time=System.currentTimeMillis();
			break;
			case 7:
				white=false;
				b.setUp(new Piece[] {
						new Piece(new Point(new double[] {3,3,0,3}), 'u'),
						new Piece(new Point(new double[] {1,2,1,1}), 'c'),
						new Piece(new Point(new double[] {0,2,2,0}), 'c'),
						new Piece(new Point(new double[] {2,3,0,2}), 'c'),
				});
				b.deselectPiece();
				time=System.currentTimeMillis();
			break;
			case 10:
				b.setState(new Board());
				b.deselectPiece();
				time=System.currentTimeMillis();
			break;
			case 12:
				t.setSpeaker("Delilah");
				t.setPortrait(Assets.DRAGON_B);
			break;
			case 16:
				simple=false;
				white=false;
				b.setUp(new Piece[] {
						new Piece(new Point(new double[] {2,2,2,2}), 'd'),
						new Piece(new Point(new double[] {1,2,1,1}), 'c'),
						new Piece(new Point(new double[] {0,2,2,0}), 'c'),
						new Piece(new Point(new double[] {2,3,0,2}), 'c'),
				});
				b.deselectPiece();
				time=System.currentTimeMillis();
			break;
			case 19,42:
				b.setState(new Board());
				t.setSpeaker("Dee");
				t.setPortrait(Assets.UNICORN_B);
				time=System.currentTimeMillis();
			break;
			case 21,27:
				t.setSpeaker("Dee");
				t.setPortrait(Assets.UNICORN_B);
			break;
			case 20,24,22:
				t.setSpeaker("Queen Xeraphina");
				t.setPortrait(Assets.QUEEN_W);
				time=System.currentTimeMillis();
			break;
			case 28,33:
				b.setState(new Board());
				t.setSpeaker("Queen Xeraphina");
				t.setPortrait(Assets.QUEEN_W);
				time=System.currentTimeMillis();
			break;
			case 23:
				t.setSpeaker("Queen Fnora");
				t.setPortrait(Assets.QUEEN_B);
				time=System.currentTimeMillis();
			break;
			case 25:
				t.setSpeaker("Dee");
				t.setPortrait(Assets.UNICORN_B);
				b.setUp(new Piece[] {
						new Piece(new Point(new double[] {0,0,0,1}), 'Q'),
						new Piece(new Point(new double[] {3,3,3,2}), 'q'),
				});
				observer.setSong("Xeraphina's_Dancing_Song.wav");
				b.deselectPiece();
				time=System.currentTimeMillis();
			break;
			case 26:
				t.setSpeaker("Queen Fnora");
				t.setPortrait(Assets.QUEEN_B);
				observer.setSong("tutorial.wav");
				time=System.currentTimeMillis();
			break;
			case 29:
				t.setSpeaker("King Fnipper");
				t.setPortrait(Assets.KING_W);
			break;
			case 31:
				white=false;
				b.setUp(new Piece[] {
						new Piece(new Point(new double[] {2,1,2,2}), 'k'),
						new Piece(new Point(new double[] {2,2,3,3}), 'c'),
						new Piece(new Point(new double[] {0,2,2,0}), 'c'),
						new Piece(new Point(new double[] {2,3,0,2}), 'c'),
						new Piece(new Point(new double[] {2,3,1,2}), 'c'),
						new Piece(new Point(new double[] {1,2,0,3}), 'c'),
				});
			break;
			case 32:
				t.setSpeaker("King Zion");
				t.setPortrait(Assets.KING_B);
				time=System.currentTimeMillis();
			break;
			case 34:
				white=false;
				b.setUp(new Piece[] {
						new Piece(new Point(new double[] {0,0,0,0}), 'n'),
						new Piece(new Point(new double[] {2,2,3,3}), 'c'),
						new Piece(new Point(new double[] {0,2,2,0}), 'c'),
						new Piece(new Point(new double[] {2,3,0,2}), 'c'),
						new Piece(new Point(new double[] {2,3,1,2}), 'c'),
						new Piece(new Point(new double[] {1,2,0,3}), 'c'),
				});
				t.setSpeaker("Zee");
				t.setPortrait(Assets.KNIGHT_B);
				time=System.currentTimeMillis();
			break;
			case 35:
				t.setSpeaker("Stella");
				t.setPortrait(Assets.KNIGHT_W);
				time=System.currentTimeMillis();
			break;
			case 36:
				b.setState(new Board());
			break;
			case 40:
				white=true;
				b.setUpWhitePawnsWithCapturableCrystals();
			break;
			case 41:
				white=true;
				b.setUp(new Piece[] {
						new Piece(new Point(new double[] {0,1,1,2}), 'P'),
						new Piece(new Point(new double[] {0,2,0,3}), 'p'),
						new Piece(new Point(new double[] {0,1,1,3}), 'C'),
						new Piece(new Point(new double[] {0,2,1,2}), 'C'),
				});
				b.makeMove(new Point(new double[] {0,2,0,3}),new Point(new double[] {0,2,0,1}),new Point(new double[] {0,2,0,2}));
			break;
		}
	}
	
	public void dialogueStepBack(int step) {
		switch (step) {
			case 6:
				white=true;
				b.setUp(new Piece[] {
						new Piece(new Point(new double[] {2,2,2,2}), 'B'),
						new Piece(new Point(new double[] {1,2,1,1}), 'C'),
						new Piece(new Point(new double[] {0,2,2,0}), 'C'),
						new Piece(new Point(new double[] {2,3,0,2}), 'C'),
				});
				b.deselectPiece();
				time=System.currentTimeMillis();
			break;
			case 9:
				dialogueStep(7);
			break;
			case 15:
				simple=true;
				b.setState(new Board());
			break;
			case 18:
				t.setSpeaker("Delilah");
				t.setPortrait(Assets.DRAGON_B);
			break;
			
			case 11,19:
				time=System.currentTimeMillis();
			case 21:
				t.setSpeaker("Dee");
				t.setPortrait(Assets.UNICORN_B);
			break;
			case 20,22:
				t.setSpeaker("Queen Xeraphina");
				t.setPortrait(Assets.QUEEN_W);
				time=System.currentTimeMillis();
			break;
			case 24:
				t.setSpeaker("Queen Xeraphina");
				t.setPortrait(Assets.QUEEN_W);
				b.setState(new Board());
				observer.setSong("tutorial.wav");
				time=System.currentTimeMillis();
				
			break;
			
			case 23:
				t.setSpeaker("Queen Fnora");
				t.setPortrait(Assets.QUEEN_B);
				time=System.currentTimeMillis();
			break;
			case 41:
				t.setSpeaker("Stella");
				t.setPortrait(Assets.KNIGHT_W);
				dialogueStep(41);
			break;
			case 39:
				b.setState(new Board());
				time=System.currentTimeMillis();
			break;
			default:
				dialogueStep(step);
			break;
		}
	}
	
	public void update() {
		super.update();
		switch(step) {
			case 0:
				space=Board.pointToNotation(new Point(new double[] {x,y,z,w}));
			break;
		}
	}
	
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		switch (step) {
			case 0:
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
			case 40,41:
				if (e.getKeyCode()==KeyEvent.VK_F&&(state==STATE.submit||state==STATE.pawnmove)) {
					//submit
					state=STATE.move;
					b.deselectPiece();
					if (observer.isMusicOn()) {
						Main.sounds.playSound("endturn.wav", 0);
					}
					
				} else if (e.getKeyCode()==KeyEvent.VK_Z&&(state==STATE.submit||state==STATE.pawnmove||state==STATE.promote)) {
					//undo
					state=STATE.move;
					b.undo();
				}
			break;
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if (step!=25||e.getButton()==MouseEvent.BUTTON3) {
			super.mousePressed(e);
		}
		java.awt.Point p=convert(e.getPoint());
		Point bp=screenToBoard(p.x,p.y);
		Piece pc=b.pieceAt(bp);
		switch(step) {
			case 3,5,6,8,9,16,17,18,31,34,35:
				if (pc!=null&&pc.isWhite()==white) {
					b.selectPiece(pc);
				} else if (b.getSelectedPiece()!=null && b.spaceMoveable(bp)) {
					b.makeMove(b.getSelectedPiece().getPos(), bp);
					b.deselectPiece();
				} else {
					b.deselectPiece();
				}
			break;
			
			case 25:
				if (pc!=null&&pc.isWhite()==white) {
					b.selectPiece(pc);
				} else if (b.getSelectedPiece()!=null && b.spaceMoveable(bp)) {
					Piece blackQueen=b.getSelectedPiece();
					b.makeMove(b.getSelectedPiece().getPos(), bp);
					
					b.deselectPiece();
					for (Piece piece:b.getPieces()) {
						if (piece.getType()=='Q') {
							b.selectPiece(piece);
							Point space=b.moveableSpaces()[Main.rand.nextInt(b.moveableSpaces().length)];
							while (space.equals(blackQueen.getPos())) {
								space=b.moveableSpaces()[Main.rand.nextInt(b.moveableSpaces().length)];
							}
							b.move(space);
							b.deselectPiece();
						}
					}
					if (b.getPieces().size()<2) {
						step++;
						t.setText(dialogue[step]);
						dialogueStep(step);
					}
					
				} else {
					b.deselectPiece();
				}
			break;
			case 40,41:
				if (state==STATE.promote) {
					for (Rectangle r:promotablesW.keySet()) {
						if (r.contains(p)) {
							if (r.contains(p)) {
								b.getPieces().remove(b.pieceAt(promoteSquare));
								Piece newPiece=promotablesW.get(r).clone();
								promotePiece=String.valueOf(newPiece.getType()).toUpperCase();
								newPiece.setPos(promoteSquare);
								b.getPieces().add(newPiece);
								promoteSquare=null;
								state=STATE.submit;
							}
						}
					}
				} else if (pc!=null&&pc.isWhite()==white&&state==STATE.move) {
					b.selectPiece(pc);
				} else if (b.getSelectedPiece()!=null && b.spaceMoveable(bp)) {
					Piece selected=b.getSelectedPiece();
					boolean enPassant=false;
					if (b.getGhost()!=null) {
						if (bp.equals(b.getGhost())&&b.getGhostPiece().isWhite()!=selected.isWhite()&&String.valueOf(selected.getType()).toUpperCase().equals("P")&&bp.distance(selected.getPos())>1) {
							enPassant=true;
							System.out.println("en passant!");
						}
					}
					if (b.pieceAt(bp)==null&&!enPassant&&state==STATE.move&&selected.isFirstMove()&&String.valueOf(selected.getType()).toUpperCase().equals("P")) {
						//first pawn move and not a capturing move
						b.move(bp);
						b.selectPiece(selected);
						if (selected.getPos().tuple.i(1)==3&&selected.getPos().tuple.i(3)==3) {
							promoteSquare=selected.getPos();
							state=STATE.promote;
						} else {
							state=STATE.pawnmove;
						}
					} else {
						if (state==STATE.pawnmove) {
							b.secondPawnMove(bp);
						} else {
							if (enPassant) {
								//System.out.println("removing ghost piece");
									b.getPieces().remove(b.getGhostPiece());
							}
							b.move(bp);
							
							if (pc!=null) {
								b.getPieces().remove(pc);
							}
						}
						b.deselectPiece();
						if (selected.getPos().tuple.i(1)==3&&selected.getPos().tuple.i(3)==3&&String.valueOf(selected.getType()).toUpperCase().equals("P")) {
							promoteSquare=selected.getPos();
							state=STATE.promote;
						} else {
							state=STATE.submit;
						}
					}
					
				} else if (state!=STATE.pawnmove){
					b.deselectPiece();
				}
			break;
		}
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		java.awt.Point p=convert(e.getPoint());
		Point bp=screenToBoard(p.x,p.y);
		if (step>0&&bp.tuple.i(0)>-1) {
			x=(int)bp.tuple.i(0);
			y=(int)bp.tuple.i(1);
			z=(int)bp.tuple.i(2);
			w=(int)bp.tuple.i(3);
			space=Board.pointToNotation(bp);
		}
	}
	
	@Override
	public void setInit() {
		super.setInit();
		//always clear the board at the start
		b.setState(new Board());
		dialogueStep(step);
	}

}
