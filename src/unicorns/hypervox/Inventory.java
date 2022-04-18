package unicorns.hypervox;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import unicorns.hypervox.voxel.Vox;

public class Inventory implements Serializable{
	ConcurrentHashMap<Vox,Integer> inventory=new ConcurrentHashMap<>();
	int slots=-1;
	public Inventory() {}
	
	public Inventory(int slots) {
		this.slots=slots;
	}
	
	public Inventory(Vox[] stuff) {
		for (int i=0;i<stuff.length;i++) {
			if (inventory.keySet().contains(stuff[i])) {
				if (inventory.get(stuff[i])>-1) {
					inventory.put(stuff[i], inventory.get(stuff[i])+1);
				}
			}
		}
		slots=inventory.keySet().size();
	}
	
	public Inventory(Vox[] stuff,int minSlots) {
		this(stuff);
		if (slots<minSlots) {
			slots=minSlots;
		} else if (minSlots==-1) {
			//-1 is inf slots
			slots=-1;
		}
	}
	
	public boolean addItem(Vox v) {
		return addItem(v,1);
	}
	
	public boolean addItem(Vox v,int amount) {
		if (inventory.keySet().contains(v)) {
			if (inventory.get(v)>-1) {
				inventory.put(v, inventory.get(v)+amount);
			}
		} else if (slots<0||inventory.keySet().size()<slots) {
			inventory.put(v, amount);
		} else {
			return false;
		}
		if (amount<0) {
			addInfItem(v);
		} else if (amount>0) {
			
		}
		
		return true;
	}
	
	public boolean addInfItem(Vox v) {
		if (slots<0||inventory.keySet().size()<slots) {
			//cardinal infinity
			inventory.put(v, -1);
		}
		return true;
	}
	
	public Vox retrieve(Vox v) {
		if (inventory.keySet().contains(v)) {
			int amount=inventory.get(v);
			if (amount>1) {
				inventory.put(v, amount-1);
			} else if (amount==1) {
				inventory.remove(v);//there will be zero of a voxel
			}
		}
		return null;
	}
	
	public Inventory retrieve(Vox v,int amountToLeaveInSlot) {
		Inventory i=new Inventory(1);
		if (hasInf(v)) {
			//cardinal infinity
			inventory.put(v, amountToLeaveInSlot);
			i.addInfItem(v);
			return i;
		}
		if (has(v,amountToLeaveInSlot)) {
			i.
			inventory.put(v, amountToLeaveInSlot);
		}
		
		return new Inventory();
	}
	
	public int getAmount(Vox v) {
		if (inventory.keySet().contains(v)) {
			return inventory.get(v);
		} else {
			return 0;
		}
	}
	
	public boolean has(Vox v) {
		return getAmount(v)!=0;
	}
	
	public boolean hasInf(Vox v) {
		return getAmount(v)<0;
	}
	
	public boolean has(Vox v, int amount) {
		if (hasInf(v)) {
			return true;
		}
		return amount>-1&&(getAmount(v)>=amount);
	}
	
}
