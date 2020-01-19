package main.game.tetris.screen;

import static main.game.tetris.GameApp.WINDOW_HEIGHT;
import static main.game.tetris.GameApp.WINDOW_WIDTH;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

import main.game.tetris.GameApp;

public abstract class Screen extends JPanel{
	private static final long serialVersionUID = 1L;
	
	protected final Color BACK_COLOR = Color.BLACK;
	protected GameApp game;
	
	public Screen(GameApp game) {
		this.game = game;
		setLayout(null);
		setBackground(BACK_COLOR);
		setFocusable(true);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
	}
	
	protected abstract void setUpGUI(); 
}