package unicorns;

import java.awt.Frame;

import graphics.screen.SPanel;
import unicorns.screens.Game;

public class Panel extends SPanel{

	public Panel(Frame observer) {
		super(observer);
		// TODO Auto-generated constructor stub
		this.screens.put("game", new Game(this));
		this.currentScreen=screens.get("game");
		this.setScreen("game");
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4753098130173598977L;

}
