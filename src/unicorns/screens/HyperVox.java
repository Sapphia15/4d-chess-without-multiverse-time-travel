package unicorns.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import gameutil.Cloner;
import gameutil.math.geom.Point;
import gameutil.math.geom.Tuple;
import gameutil.math.geom.Vector;
import gameutil.text.Console;
import graphics.screen.Screen;
import unicorns.Assets;
import unicorns.Main;
import unicorns.Panel;
import unicorns.hypervox.Viewport;
import unicorns.hypervox.voxel.Break;
import unicorns.hypervox.voxel.HGen;
import unicorns.hypervox.voxel.Hypercrate;
import unicorns.hypervox.voxel.Idea;
import unicorns.hypervox.voxel.Pushable;
import unicorns.hypervox.voxel.Solid;
import unicorns.hypervox.voxel.Vox;
import unicorns.hypervox.voxel.Wall;
import unicorns.hypervox.voxel.Wire;
import unicorns.hypervox.world.Paravox;
import unicorns.hypervox.world.PointAndWorld;
import unicorns.hypervox.world.World;

public class HyperVox extends Screen{
	Color c=Color.blue;
	World w=new Paravox();//new World();
	
	Viewport v=new Viewport(5);
	Panel observer;
	boolean solid=false;
	boolean push=true;
	Point mousePoint=new Point(new double[] {0,0,0,0});
	java.awt.Point mousePos=new java.awt.Point();
	ConcurrentHashMap<Integer,CopyOnWriteArrayList<Vox>> screenVoxels=new ConcurrentHashMap<>();
	enum PLACE_MODE {none, place,destroy,grab};
	PLACE_MODE placeMode=PLACE_MODE.none;
	boolean ghost=false;
	Vox hand=new Hypercrate(c);
	Idea will=null;
	Vox mouseVox=null;
	
	public HyperVox(Panel p) {
		this.observer=p;
		v.setCenterSpace(new Point(new double[] {0,0,0,0}));
		w.getPlayer().setPos(new Point(v.getPos().tuple.$A$(2)));
		hand.setLayer(2);
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(Assets.BOARD_5, 0, 0,844,844, observer);
		for (int i:screenVoxels.keySet()) {
			for (Vox v:(CopyOnWriteArrayList<Vox>)screenVoxels.get(i).clone()) {
				drawVox(v,g);
			}
		}
		String coords="("+mousePoint.tuple.i(0)+","+mousePoint.tuple.i(1)+","+mousePoint.tuple.i(2)+","+mousePoint.tuple.i(3)+")"+ " Solid: "+solid;
		if (mouseVox instanceof Wire) {
			coords+=" Power: "+Math.floor(((Wire)mouseVox).getPower()*100)/100;
		}
		
		g.fillRoundRect(mousePos.x+10, mousePos.y-25, g.getFontMetrics().stringWidth(coords)+10, 21, 10, 10);
		g.setColor(Color.white);
		g.drawString(coords, mousePos.x+15, mousePos.y-11);
		drawVox(w.getPlayer(),g);
		g.setColor(Color.black);
		g.drawString("Hand", 900, 30);
		drawVox(hand,g,900,32);
		
		g.drawString("Will", 900, 80);
		if (will!=null) {
			drawVox(will,g,900,82);
		}
		
	}
	
	public void drawVox(Vox v,Graphics g,java.awt.Point sPoint) {
		
		if (v.getImage()==null) {
			g.setColor(v.getColor());
			g.fillRect(sPoint.x, sPoint.y, 32, 32);//subtract 32 from screen y to get upper left corner
		} else {
			if (v.getColor()==null) {
				g.drawImage(v.getImage(), sPoint.x, sPoint.y, 32, 32,observer);
			} else {
				g.drawImage(v.getImage(), sPoint.x, sPoint.y, 32, 32, v.getColor(),observer);//draw vox image with background color
			}
		}
	}
	
	public void drawVox(Vox v,Graphics g) {
		drawVox(v,g,boardToScreen(this.v.toBoardPoint(v.getPos())));
	}
	
	public void drawVox(Vox v,Graphics g,int x,int y) {
		drawVox(v,g,new java.awt.Point(x,y));
	}
	
	public java.awt.Point boardToScreen(Point p) {
		int x=(int)Math.floor(p.tuple.i(0))*33+(int)Math.floor(p.tuple.i(2))*168+4;
		int y=(4-(int)Math.floor(p.tuple.i(1)))*33+(4-(int)Math.floor(p.tuple.i(3)))*168+4;
		return new java.awt.Point(x,y);
	}
	
	public Point screenToBoard(java.awt.Point p) {
		double z=Math.floor((p.x-4)/168);
		double x=Math.floor((p.x-z*168-4)/33);
		double w=4-Math.floor((p.y-4)/168);
		double y=4-Math.floor((p.y-(4-w)*168-4)/33);
		return new Point(new double[] {x,y,z,w});
	}
	
	@Override
	public void update() {
		screenVoxels=w.getScreenVox(v);
		w.update();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		Point p=w.getPlayer().getPos();
		switch(e.getKeyCode()) {
			case KeyEvent.VK_W:
				p=new Point(p.tuple.$A$(new Tuple(new double[] {0,1,0,0})));
			break;
			case KeyEvent.VK_A:
				p=new Point(p.tuple.$A$(new Tuple(new double[] {-1,0,0,0})));
			break;
			case KeyEvent.VK_S:
				 p=new Point(p.tuple.$A$(new Tuple(new double[] {0,-1,0,0})));
			break;
			case KeyEvent.VK_D:
				p=new Point(p.tuple.$A$(new Tuple(new double[] {1,0,0,0})));
			break;
			case KeyEvent.VK_Q:
				 p=new Point(p.tuple.$A$(new Tuple(new double[] {0,0,-1,0})));
			break;
			case KeyEvent.VK_E:
				p=new Point(p.tuple.$A$(new Tuple(new double[] {0,0,1,0})));
			break;
			case KeyEvent.VK_SPACE:
				p=new Point(p.tuple.$A$(new Tuple(new double[] {0,0,0,1})));
			break;
			case KeyEvent.VK_SHIFT:
				p=new Point(p.tuple.$A$(new Tuple(new double[] {0,0,0,-1})));
			break;
			case KeyEvent.VK_C:
				c=new Color(Main.rand.nextInt(255),Main.rand.nextInt(255),Main.rand.nextInt(255));
				if (hand!=null) {
					hand.setColor(c);
				}
			break;
			case KeyEvent.VK_ENTER://need to make a place block will
				placeMode=PLACE_MODE.place;
				will=null;
			break;
			case KeyEvent.VK_BACK_SPACE:
				placeMode=PLACE_MODE.destroy;
				will=new Break();
			break;
			case KeyEvent.VK_1:
				hand=new Vox(c);
				hand.setLayer(2);
				
			break;
			case KeyEvent.VK_2:
				hand=new Hypercrate(c);
				hand.setLayer(2);
			break;
			case KeyEvent.VK_3:
				hand=new Paravox(c,w);
				hand.setLayer(2);
			break;
			case KeyEvent.VK_4:
				hand=new Wire(c);
				hand.setLayer(2);
			break;
			case KeyEvent.VK_5:
				hand=new HGen(c);
				hand.setLayer(2);
			break;
			case KeyEvent.VK_P:
				push=!push;
			break;
			case KeyEvent.VK_DELETE:
				w.removeVox(new Point(v.getPos().tuple.$A$(2)));
			break;
			case KeyEvent.VK_BACK_SLASH:
				Main.cmd.setVisible(true);
			break;
			
		}
		if (!w.getPlayer().getPos().equals(p)) {
			Point startPos=w.getPlayer().getPos();
			if (placeMode==PLACE_MODE.destroy) {
				w.removeVox(p, 1, 10);//remove all voxes in the layer range from 1 to 10 at the target location
				will=null;
			}
			PointAndWorld move=attemptMove(startPos,p,w,w.getPlayer());
			
			if (placeMode==PLACE_MODE.place) {
				Vox voxToPlace=hand.clone();
				if (voxToPlace instanceof Paravox) {
					((Paravox) voxToPlace).setParent(w);
				}
				voxToPlace.setPos(startPos);
				w.addVox(voxToPlace);
			}
			w=move.getWorld();
			w.getPlayer().setPos(move.getPoint());
			v.setPos(new Point(w.getPlayer().getPos().tuple.$S$(2)));
			placeMode=PLACE_MODE.none;
		}
		
		w.getPlayer().setPos(new Point(v.getPos().tuple.$A$(2)));
		mousePoint=v.toWorldPoint(screenToBoard(mousePos));
	}
	
	private PointAndWorld attemptMove(Point start,Point p, World w,Vox mover) {
		return attemptMove(start,p,w,mover,new ArrayList<>(),push);
	}
	
	private PointAndWorld attemptMove(Point start,Point p, World w,Vox mover,boolean push) {
		return attemptMove(start,p,w,mover,new ArrayList<>(),push);
	}
	
	private PointAndWorld attemptMove(Point start,Point p, World w,Vox mover,ArrayList<Vox> pushedVoxels,boolean push) {
		ConcurrentHashMap<Integer, CopyOnWriteArrayList<Vox>> voxels=w.getVoxelsAt(new Point(p.tuple));
		if (!World.hasSolid(voxels)) {
			return new PointAndWorld(p,w);
		} else {
			for (int i:voxels.keySet()) {
				for (Vox v:voxels.get(i)) {
					if (v instanceof Pushable) {
						
						if (push) {
							Vector vec=new Vector(p.tuple.$S$(start.tuple));
							if (pushedVoxels.contains(v)) {
								return new PointAndWorld(new Point(p.tuple.$S$(vec.getSpds())),w);
							}
							
							Point newStart=p;
							pushedVoxels.add(v);
							PointAndWorld attempt=attemptMove(newStart, new Point(vec.normalize().$A$(new Vector(newStart))), w,v,pushedVoxels,push);
							if (!attempt.getPoint().equals(newStart)) {
								v.setPos(attempt.getPoint());
								if (!attempt.getWorld().equals(w)) {
									w.removeVox(v);
									attempt.getWorld().addVox(v);
									if (v instanceof Paravox) {
										((Paravox) v).setParent(attempt.getWorld());
									}
								}
								return new PointAndWorld(p,w);
							}
						}
						if (v instanceof Paravox) {
							Vector vec=new Vector(start.tuple.$S$(p.tuple));
							Point newStart=new Point(vec.normalize().$X$(3));
							//Console.s.println("Try world change in ("+v.getColor().getRed()+", "+v.getColor().getGreen()+", "+v.getColor().getBlue()+")");
							PointAndWorld attempt=attemptMove(newStart, new Point(vec.normalize().$X$(2)), (Paravox) v,mover,pushedVoxels,push);
							if (!attempt.getPoint().equals(newStart)) {
								//Console.s.println("Success world change in");
								return attempt;
							}
						}
					}
				}
			}
			if (w instanceof Paravox) {
				//still need to resolve a paradox
				//if you have a paravox, A within itself, two pushable voxes B and C, walls W, empty floors F, and exits E
				
				//WWEWW
				//FFCFF
				//FFBFF
				//FFAFF
				//FFFFF
				
				//when you push up on A, B and C should swap places. Currently, nothing happens because of a stack overflow error.
				//probably should make something for checking if a pushed block gets back to where it started or something?
				
				if (Paravox.isExit(new Point(start.tuple))) {
					World parent=((Paravox)w).getParent();
					Vector vec=new Vector(start.tuple).normalize();
					Point newStart=new Point(w.getPos().tuple);
					Console.s.println("Try world change out");
					boolean paradox=false;
					if (mover instanceof Paravox) {
						paradox=((Paravox)mover).getParent().equals(mover);
					}
					if (!paradox) {
						PointAndWorld attempt=attemptMove(newStart, new Point(vec.$A$(new Vector(newStart.tuple))), parent,mover,pushedVoxels,push);
						if (!attempt.getPoint().equals(newStart)) {
							if (mover instanceof Paravox) {
								World moverParent=((Paravox) mover).getParent();
								if (!attempt.getWorld().equals(moverParent)) {
									moverParent.removeVox(mover);
									((Paravox) mover).setParent(attempt.getWorld());
								}
							}
							return attempt;
						} else {
							//if you would run into a wall then try to push the paravox out of the way and move into it's spot
							pushedVoxels.add(w);
							attempt=attemptMove(newStart, new Point(new Vector(newStart.tuple).$S$(vec)), parent,w,pushedVoxels,push);
							if (!attempt.getPoint().equals(newStart)) {
								w.setPos(attempt.getPoint());
								boolean difWorld=false;
								difWorld=!attempt.getWorld().equals(w);
								/*if (w instanceof Paravox) {
									difWorld=difWorld||(!attempt.getWorld().equals(((Paravox) w).getParent()));
								}*/
								if (difWorld) {
									w.removeVox(w);
									attempt.getWorld().addVox(w);
									if (w instanceof Paravox) {
										((Paravox) w).setParent(attempt.getWorld());
									}
								}
								return new PointAndWorld(newStart,parent);
							}
							
						}
					} else {
						Main.err.println("System prevented a paradox from occuring");
					}
				}
			}
		}
		//if move failed return the starting position
		return new PointAndWorld(start,w);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (e.getX()<Assets.BOARD_5.getWidth(observer)&&e.getY()<Assets.BOARD_5.getHeight(observer)) {
			mousePos=e.getPoint();
			mousePoint=v.toWorldPoint(screenToBoard(mousePos));
			solid=w.solidAt(mousePoint);
			CopyOnWriteArrayList<Vox> voxels=w.getVoxelsAt(mousePoint, 2);
			if (voxels!=null) {
				for (Vox vox:voxels){
					mouseVox=vox;
					break;
				}
			}
		}
	}
	
	public World getWorld() {
		return w;
	}
	
	public void loadWorld(String name) {
		World w=World.load(name);
		if (w!=null) {
			this.w=w;
			v.setPos(new Point(w.getPlayer().getPos().tuple.$S$(2)));
		}
		
	}
	
	@Override
	protected void keyDown(int key) {
		// TODO Auto-generated method stub
		
	}

}
