package main.game.tetris;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import main.game.tetris.screen.MainMenuScreen;
import main.game.tetris.screen.Screen;

/**
 * Classic Tetris
 * @author Felix Janopol Jr.
 */
public class GameApp {
	private static JFrame window;
	
	public static final int WINDOW_WIDTH = 500;
	public static final int WINDOW_HEIGHT = WINDOW_WIDTH / 3 * 4;
	
	/*
	 * This will always contain one screen.
	 * Its current screen will always on index 0;
	 */
	private Screen screen = null;
	
	public GameApp() {
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
	}
	
	private Screen getCurrentScreen() {
		return screen;
	}
	
	public JFrame getFrameInstance() {
		return window;
	}
	
	public void setCurrentScreen(Screen screen) {
		if(this.screen != null) {
			Screen prev = getCurrentScreen();
			screen.remove(prev);
			window.remove(prev);
		}
		this.screen = screen;
		window.setContentPane(screen);
		window.validate();
		window.pack();
		window.setVisible(true);
		window.setLocationRelativeTo(null);
	}
	
	private void start() {
		setCurrentScreen(new MainMenuScreen(this));
	}
	
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GameApp().start();
			}
		});
	}
}