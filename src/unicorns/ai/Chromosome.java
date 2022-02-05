package unicorns.ai;

import java.util.concurrent.CopyOnWriteArrayList;

import gameutil.math.geom.Tuple;
import gameutil.math.geom.Vector;
import unicorns.Main;

public class Chromosome {
	
	CopyOnWriteArrayList<Vector> vecs;
	int layers;
	
	public Chromosome(int layers) {
		this.layers=layers;
	}
	
	public void genVecs() {
		for (int i=0;i<layers;i++) {
			Tuple t=new Tuple(256);
			for (int j=0;j<256;j++) {
				t.set(j, Main.rand.nextDouble()*2-1);
			}
		}
	}
	
	public void process(Vector v) {
		
	}
}
