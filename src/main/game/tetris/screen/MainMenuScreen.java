package main.game.tetris.screen;

import static main.game.tetris.GameApp.WINDOW_HEIGHT;
import static main.game.tetris.GameApp.WINDOW_WIDTH;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.game.tetris.GameApp;
import main.game.tetris.util.BufferedImageLoader;

public class MainMenuScreen extends Screen{
	private static final long serialVersionUID = 1L;

	private BufferedImageLoader loader;
	
	public MainMenuScreen(GameApp game) {
		super(game);
		loader = new BufferedImageLoader();
		setUpGUI();
	}
	
	@Override
	protected void setUpGUI() {
		/*
		 * image size (1600 x 448)
		 * scaled by dividing screen's width by 3.5
		 */
		int logoWidth = WINDOW_WIDTH;
		int logoHeight = WINDOW_WIDTH * 2 / 7;
		
		BufferedImage icon = loader.loadImage("/logo/tetris_logo.png");
		Image resizedIcon = icon.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH); 
		
		JLabel logo = new JLabel(new ImageIcon(resizedIcon));
		logo.setBounds(0, 50, logoWidth, logoHeight);
		
		int buttonWidth = 200;
		int buttonHeight = 50;
		int buttonX = WINDOW_WIDTH / 2 - buttonWidth / 2;
		int buttonY = WINDOW_HEIGHT * 3 / 5;
		int gap = buttonHeight / 2;
		
		ScreenButton play = new ScreenButton("PLAY");
		play.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
		play.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				PlayScreen playScreen = new PlayScreen(game); 
				game.setCurrentScreen(playScreen);
				playScreen.getPlayPanel().requestFocusInWindow();
			}
		});
		
		ScreenButton score = new ScreenButton("SCORE");
		score.setBounds(buttonX, buttonY + buttonHeight + gap, buttonWidth, buttonHeight);
		score.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				game.setCurrentScreen(new ScoreScreen(game));
			}
		});
		
		ScreenButton about = new ScreenButton("ABOUT");
		about.setBounds(buttonX, buttonY + 2*buttonHeight + 2*gap, buttonWidth, buttonHeight);
		about.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				game.setCurrentScreen(new AboutScreen(game));
			}
		});
		
		add(logo);
		add(play);
		add(score);
		add(about);
	}
	
	private class ScreenButton extends JLabel implements MouseListener{
		private static final long serialVersionUID = 1L;
		
		private final Color BACK_COLOR 	 = Color.DARK_GRAY,
		 					FORE_COLOR 	 = Color.WHITE,
		 					BORDER_COLOR = Color.LIGHT_GRAY;
		
		private final Font FONT = new Font("Arial", Font.BOLD, 30);
		
		public ScreenButton(String text) {
			setText(text);
			setFont(FONT);
			setHorizontalAlignment(JLabel.CENTER);
			setOpaque(true);
			setBackground(BACK_COLOR);
			setForeground(FORE_COLOR);
			setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 3, true));
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			addMouseListener(this);
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println("FOO");
		}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			setBackground(FORE_COLOR);
			setForeground(BACK_COLOR);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setBackground(BACK_COLOR);
			setForeground(FORE_COLOR);
		}
	}
}