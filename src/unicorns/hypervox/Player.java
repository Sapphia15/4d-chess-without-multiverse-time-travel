package unicorns.hypervox;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import unicorns.Assets;
import unicorns.Piece;
import unicorns.hypervox.voxel.Vox;
import unicorns.hypervox.world.Paravox;

public class Player extends Vox {
	
	static Paravox imagination=new Paravox();
	
	public Player() {
		super(0,0,0,0,Assets.UNICORN_B);
		setColor(null);
	}
}
