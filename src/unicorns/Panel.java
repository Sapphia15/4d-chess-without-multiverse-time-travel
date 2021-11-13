package unicorns;

import java.awt.Frame;

import graphics.screen.SPanel;
import unicorns.screens.Game;
import unicorns.screens.Title;

public class Panel extends SPanel{

	boolean ai;
	
	public Panel(Frame observer) {
		super(observer);
		// TODO Auto-generated constructor stub
		this.screens.put("game", new Game(this));
		this.screens.put("title", new Title(this));
		this.currentScreen=screens.get("title");
		this.setScreen("title");
		
	}
	
	public boolean ai() {
		return ai;
	}
	
	public void setAi(boolean ai) {
		this.ai=ai;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4753098130173598977L;

}
