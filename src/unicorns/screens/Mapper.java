package unicorns.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.swing.JOptionPane;

import gameutil.math.geom.Point;
import gameutil.math.geom.Vector;
import gameutil.text.Iru.*;
import graphics.screen.Screen;
import unicorns.Assets;
import unicorns.Main;
import unicorns.Panel;

public class Mapper extends Screen {
	
	Panel observer;
	
	int oldWidth=0;
	int oldHeight=0;
	final int sWidth=1360;
	final int sHeight=768;
	final int midX=sWidth/2;
	final int midY=sHeight/2;
	final int maxTranslate=612;
	boolean letterDisplay=false;
	int drawSceneWidth=sWidth;
	int drawSceneX=0;
	final double ratio=((double)sWidth)/sHeight;
	boolean showMenu=false;
	Rectangle menu;
	Rectangle load;
	Rectangle save;
	Rectangle help;
	
	BufferedImage scene;
	
	int x=0;
	int y=0;
	int z=0;
	
	int r=0;
	int g=0;
	int b=0;
	
	int mx=0;
	int my=0;
	int mz=0;
	
	int mxo=0;
	int myo=0;
	int mzo=0;
	
	int sizeBounce=0;
	
	java.awt.Point mousePoint=new java.awt.Point(0,0);
	
	ConcurrentHashMap<String,Vector> points=new ConcurrentHashMap<>();
	ArrayList<String> showLabel=new ArrayList<>();
	String hover=null;
	//ArrayList<String> names=new ArrayList<>();
	//Vector pointer=new Vector(new double[] {0,0,0});
	
	public Mapper(Panel p) {
		observer=p;
		initScene();
		//points.add(new Vector(new double[] {10,10,3}));
		menu=new Rectangle(sWidth-Assets.MENU.getWidth(null)-10,10,Assets.MENU.getWidth(null),Assets.MENU.getHeight(null));
		help=new Rectangle(sWidth-Assets.PORTRAIT.getWidth(null)-10,20+Assets.MENU.getHeight(null),Assets.PORTRAIT.getWidth(null),Assets.PORTRAIT.getHeight(null));
		save=new Rectangle(midX-Assets.BUTTON_WIDTH,midY-Assets.BUTTON_HEIGHT,Assets.BUTTON_WIDTH*2,Assets.BUTTON_HEIGHT*2);
		load=new Rectangle(midX-Assets.BUTTON_WIDTH,midY+Assets.BUTTON_HEIGHT,Assets.BUTTON_WIDTH*2,Assets.BUTTON_HEIGHT*2);
	}
	
	
	@Override
	public void paint(Graphics g) {
		scene.flush();
		initScene();
		paintScene(scene.createGraphics());
		//} catch (NullPointerException e) {
		//	initScene();
		//}
		
		int width=(int)(oldHeight*ratio);
		g.drawImage(scene,(oldWidth-width)/2, 0,width,oldHeight, null);
	}
	
	public void paintScene(Graphics2D g) {
		g.setColor(new Color(100,20,100));
		g.fillRect(0, 0, midX, sHeight);
		g.setColor(Color.pink);
		g.fillRect(midX, 0, midX, sHeight);
		g.drawImage(Assets.UNICORN_B,midX/2-8,midY-8,16,16,null);
		g.drawImage(Assets.UNICORN_B,midX+midX/2-8,midY-8,16,16,null);
		g.setColor(Color.black);
		g.fillRoundRect(5, sHeight-40, 40, 40, 20, 20);
		g.setColor(new Color(255-r,255-this.g,255-b));
		g.fillRoundRect(9, sHeight-36, 32, 32, 20, 20);
		g.setColor(new Color(r,this.g,b));
		g.fillOval(17, sHeight-28, 16, 16);
		
		g.setColor(Color.black);
		g.setFont(g.getFont().deriveFont(14f));
		
		for (String k:points.keySet()) {
			Vector v=points.get(k);
			int x=(int)v.getSpds().i(0);
			int y=(int)v.getSpds().i(1);
			int z=(int)v.getSpds().i(2);
			
			int A=z-this.z+midX/2;
			int N=this.y-y+midY;
			int E=x-this.x+midX+midX/2;
			if (A>-1&&A<midX+1 && N>-1&&N<sHeight+1 && E>midX&&E<sWidth+1) {
				g.setColor(Color.black);
				g.fillOval(A-6, N-6, 13, 13);
				g.fillOval(E-6, N-6, 13, 13);
				g.setColor(new Color((int)v.getSpds().i(3),(int)v.getSpds().i(4),(int)v.getSpds().i(5)));
				g.fillOval(A-4, N-4, 9, 9);
				g.fillOval(E-4, N-4, 9, 9);
				if (k.equals(hover)){
					sizeBounce=(sizeBounce+1)%32;
					int bounce=sizeBounce/4;
					if (bounce>4) {
						bounce=8-bounce;
					}
					g.drawImage(Assets.LABLE, A+6, N+6,Assets.LABLE.getWidth(null)+bounce,Assets.LABLE.getHeight(null)+bounce, null);
					g.setColor(Color.WHITE);
					g.drawString(k,A+14,N+6+13);
					g.setColor(Color.BLACK);
					g.drawImage(Assets.LABLE, E+6, N+6,Assets.LABLE.getWidth(null)+bounce,Assets.LABLE.getHeight(null)+bounce, null);
					g.drawString(k,E+14,N+6+13);
				} else if (showLabel.contains(k)) {
					
					g.drawImage(Assets.LABLE, A+6, N+6, null);
					g.setColor(Color.WHITE);
					g.drawString(k,A+14,N+6+13);
					g.setColor(Color.BLACK);
					g.drawImage(Assets.LABLE, E+6, N+6, null);
					g.drawString(k,E+14,N+6+13);
				}
			}
		}
		int A=mz-this.z+midX/2;
		int N=this.y-my+midY;
		int E=mx-this.x+midX+midX/2;
		g.drawImage(Assets.HEART, A-4, N,8,8, null);
		g.drawImage(Assets.HEART, E-4, N,8,8, null);
		g.setColor(Color.white);
		g.fillRoundRect(0, 5, 250, 22, 20, 20);
		g.setColor(Color.black);
		
		g.drawString("("+mx+", "+my+", "+mz+")", 10, 20);
		g.setFont(g.getFont().deriveFont(40f));
		
		
		if (letterDisplay) {
			int hsouthWidth=(int)g.getFontMetrics().getStringBounds("S", g).getWidth()/2;
			g.drawString("N", midX/2-hsouthWidth, 50);
			g.drawString("N", midX+midX/2-hsouthWidth, 50);
			
			int hnorthWidth=(int)g.getFontMetrics().getStringBounds("N", g).getWidth()/2;
			g.drawString("S", midX/2-hnorthWidth, sHeight-20);
			g.drawString("S", midX+midX/2-hnorthWidth, sHeight-20);
			
			g.drawString("K",10,midY+40);

			
			g.drawString("A",midX-40,midY+40);
			
			g.drawString("W",midX+10,midY+40);
			
			g.drawString("E",sWidth-40,midY+40);
		} else {
			int hsouthWidth=(int)g.getFontMetrics().getStringBounds("North", g).getWidth()/2;
			g.drawString("North", midX/2-hsouthWidth, 50);
			g.drawString("North", midX+midX/2-hsouthWidth, 50);
			
			int hnorthWidth=(int)g.getFontMetrics().getStringBounds("South", g).getWidth()/2;
			g.drawString("South", midX/2-hnorthWidth, sHeight-20);
			g.drawString("South", midX+midX/2-hnorthWidth, sHeight-20);
			
			g.drawString("K",10,midY-40);
			g.drawString("e",10,midY);
			g.drawString("n",10,midY+40);
			g.drawString("t",10,midY+80);
			g.drawString("h",10,midY+120);
			
			g.drawString("A",midX-40,midY-30);
			g.drawString("n",midX-40,midY+10);
			g.drawString("t",midX-40,midY+50);
			g.drawString("h",midX-40,midY+90);
			
			g.drawString("W",midX+10,midY-30);
			g.drawString("e",midX+10,midY+10);
			g.drawString("s",midX+10,midY+50);
			g.drawString("t",midX+10,midY+90);
			
			g.drawString("E",sWidth-40,midY-30);
			g.drawString("a",sWidth-40,midY+10);
			g.drawString("s",sWidth-40,midY+50);
			g.drawString("t",sWidth-40,midY+90);
			
			g.drawImage(Assets.MENU, menu.x, menu.y, null);
			if (showMenu) {
				g.drawImage(Assets.SAVE_BUTTON,save.x, save.y,save.width,save.height, null);
				g.drawImage(Assets.LOAD_BUTTON,load.x, load.y,load.width,load.height, null);
				g.drawImage(Assets.PORTRAIT,help.x, help.y,help.width,help.height, null);
				g.drawImage(Letter.QUESTION.img16(),help.x+8,help.y+8,help.width-16,help.height-16,null);
			}
		}
	}
	
	public boolean inViewPort(int x,int y,int z) {
		int A=z-this.z+midX/2;
		int N=this.y-y+midY;
		int E=x-this.x+midX+midX/2;
		return (A>-1&&A<midX+1 && N>-1&&N<sHeight+1 && E>midX&&E<sWidth+1);
	}
	
	public void initScene(){
		scene=new BufferedImage(sWidth,sHeight, BufferedImage.TYPE_INT_ARGB);
		//g.setBackground(new Color(255,255,255,0));
	}

	@Override
	public void update() {
		if (oldWidth!=observer.getWidth()||oldHeight!=observer.getHeight()) {
			oldWidth=observer.getWidth();
			oldHeight=observer.getHeight();
			drawSceneWidth=(int)(oldHeight*ratio);
			drawSceneX=(oldWidth-drawSceneWidth)/2;
		}
		updateMouse(mousePoint);
	}
	
	public void setInit() {
		observer.setSong("tutorial.wav");
		Main.f.setExtendedState(Frame.MAXIMIZED_BOTH);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int d=1;
		if (e.isControlDown()) {
			d=-1;
		}
		if (e.isShiftDown()) {
			d=d*5;
		}
		if (e.getKeyCode()==KeyEvent.VK_SPACE) {
			String name= JOptionPane.showInputDialog("Enter a location name:");
			if (name!=null) {
				points.put(name,new Vector(new double[] {mx,my,mz,r,g,b}));
			}
			
		} else if (e.getKeyCode()==KeyEvent.VK_R) {
			r+=d;
			if (r>255) {
				r=0;
			} else if (r<0) {
				r=255;
			}
		} else if (e.getKeyCode()==KeyEvent.VK_G) {
			g+=d;
			if (g>255) {
				g=0;
			} else if (g<0) {
				g=255;
			}
		} else if (e.getKeyCode()==KeyEvent.VK_B) {
			b+=d;
			if (b>255) {
				b=0;
			} else if (b<0) {
				b=255;
			}
		}else if (e.getKeyCode()==KeyEvent.VK_M) {
			observer.toggleMusic();
		} else if (e.getKeyCode()==KeyEvent.VK_N) {
			String hover=this.hover;
			Vector v=points.get(hover);
			if (v!=null) {
				String name=JOptionPane.showInputDialog("Enter a location name:");
				if (name!=null) {
					points.put(name, v);
					this.hover=name;
					points.remove(hover);
				}
			
			}
		} else if (e.getKeyCode()==KeyEvent.VK_C) {
			Vector v=points.get(hover);
			if (v!=null) {
				r=(int)v.getSpds().i(3)%256;
				g=(int)v.getSpds().i(4)%256;
				b=(int)v.getSpds().i(5)%256;
			}
		} else if (e.getKeyCode()==KeyEvent.VK_DELETE) {
			points.remove(hover);
			this.hover="";
			
		} else if (e.getKeyCode()==KeyEvent.VK_L) {
			letterDisplay=!letterDisplay;
		} else if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
			observer.setScreen("title");
		}
	}
	
	public java.awt.Point convert(java.awt.Point p) {
		return new java.awt.Point((int)(((double)(p.x-drawSceneX)/drawSceneWidth)*observer.sWidth),(int)((double)(p.y)/oldHeight*observer.sHeight));
	}
	
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		java.awt.Point p=convert(e.getPoint());
		if (e.getButton()==MouseEvent.BUTTON1) {
			if (menu.contains(p)) {
				showMenu=!showMenu;
			}
			if (showMenu) {
				if (save.contains(p)) {
					save(Main.saveFile());
				} else if (load.contains(p)) {
					open(Main.openFile());
				} else if (help.contains(p)) {
					JOptionPane.showMessageDialog(observer, 
							  "Use WASDQE to move.\n"
							+ "Use the scroll wheel to move your pointer along the axis ignored by the side of the map that your mouse is on (e.g. scrolling with your mouse on the anth/kenth side with move your pointer east/west).\n"
							+ "     - Press shift while scrolling to move the pointer slower\n"
							+ "Press space to create a marker at the location of your pointer.\n"
							+ "Press N to rename a marker that you're pointer is hovering over.\n"
							+ "Use the R,G and B keys to change the color of your next marker (this color is shown in the circle at the bottom left of the screen).\n"
							+ "     - Press shift while changing color to change color faster.\n"
							+ "     - Press control while changing color to change color in the opposite direction.\n"
							+ "Press delete while hovering your pointer over a marker to delete it.\n"
							+ "Right click a marker while hovering over it to toggle if its label should stay visible when you aren't hovering over it.\n"
							+ "Press M to toggle music.\n"
							+ "Press L to toggle direction lable display mode.\n"
							+ "Press escape to return to the title screen.\n"
							+ "Click the save button to save your map.\n"
							+ "Click the load button to load an existing map.","Help",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} else if (e.getButton()==MouseEvent.BUTTON3) {
			if (showLabel.contains(hover)) {
				showLabel.remove(hover);
			} else {
				showLabel.add(hover);
			}
		}
		
	}
	
	public void updateMouse(java.awt.Point p) {
		
		if (p.x<midX&&p.x>=drawSceneX) {
			mz=p.x+this.z-midX/2;
			my=oldHeight-p.y+this.y-midY-26;
		} else if (p.x>=midX&&p.x<drawSceneX+drawSceneWidth) {
			mx=p.x+this.x-(midX+midX/2);
			my=oldHeight-p.y+this.y-midY-26;
		}
		
		if (!(mx==mxo&&my==myo&&mz==mzo)) {
			hover="";
			for (String k:points.keySet()) {
				Vector v=points.get(k);
				if (new Point(new double[] {v.getSpds().i(0),v.getSpds().i(1),v.getSpds().i(2) }).distance(new Point(new double[] {mx,my,mz}))<6) {
					hover=k;
					break;
				}
				
			}
			mxo=mx;
			myo=my;
			mzo=mz;
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		super.mousePressed(e);
		java.awt.Point p=convert(e.getPoint());
		mousePoint=(p);
		
	}
	
	public void mouseScrolled(MouseWheelEvent e) {
		java.awt.Point p=convert(e.getPoint());
		double rotation=e.getPreciseWheelRotation();
		if (!e.isShiftDown()) {
			rotation=rotation*32;
		}
		int shift=(int) rotation;
		int newVal=mx;
		if (p.x<midX&&p.x>=drawSceneX) {
			newVal-=shift;
			if (inViewPort(newVal,my,mz)) {
				mx=newVal;
			}
		} else if (p.x>=midX&&p.x<drawSceneX+drawSceneWidth) {
			newVal=mz-shift;
			if (inViewPort(mx,my,newVal)) {
				mz=newVal;
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
		
	}
	
	public void save(File f) {
		try {
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(f));
			out.writeObject(points);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void open(File f) {
		try {
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(f));
			points=(ConcurrentHashMap<String, Vector>) in.readObject();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void keyDown(int key) {
		if (key==KeyEvent.VK_D) {
			x+=1;
			mx+=1;
		} else if (key==KeyEvent.VK_A) {
			x-=1;
			mx-=1;
		} else if (key==KeyEvent.VK_W) {
			y+=1;
			my+=1;
		} else if (key==KeyEvent.VK_S) {
			y-=1;
			my-=1;
		}  else if (key==KeyEvent.VK_E) {
			z+=1;
			mz+=1;
		} else if (key==KeyEvent.VK_Q) {
			z-=1;
			mz-=1;
		}
		
	}

}
