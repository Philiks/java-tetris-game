package main.game.tetris.screen;

import static main.game.tetris.util.Statistics.getHighScore;
import static main.game.tetris.util.Statistics.getIShape;
import static main.game.tetris.util.Statistics.getJShape;
import static main.game.tetris.util.Statistics.getLShape;
import static main.game.tetris.util.Statistics.getLevel;
import static main.game.tetris.util.Statistics.getLines;
import static main.game.tetris.util.Statistics.getOShape;
import static main.game.tetris.util.Statistics.getOneLine;
import static main.game.tetris.util.Statistics.getSShape;
import static main.game.tetris.util.Statistics.getTShape;
import static main.game.tetris.util.Statistics.getTetris;
import static main.game.tetris.util.Statistics.getThreeLines;
import static main.game.tetris.util.Statistics.getTwoLines;
import static main.game.tetris.util.Statistics.getUserName;
import static main.game.tetris.util.Statistics.getZShape;
import static main.game.tetris.util.Statistics.resetPreferences;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.game.tetris.GameApp;
import main.game.tetris.tetrimino.Shape;
import main.game.tetris.tetrimino.Tetrimino;
import main.game.tetris.util.CustomizedDialog;
import main.game.tetris.util.Initialize;

public class ScoreScreen extends Screen{
	private static final long serialVersionUID = 1L;
	
	private JPanel userInfo, tetriminoInfo, lineInfo; 
	
	private final int HORIZONTAL_FRAME_PADDING = 20;
	private final int VERTICAL_FRAME_PADDING = 20;
	private final int PANEL_WIDTH = 460;
	
	private final Color FORE_COLOR = Color.LIGHT_GRAY;
	private final Color BACK_COLOR = Color.BLACK;
	
	public ScoreScreen(GameApp game) {
		super(game);
		setUpGUI();
	}

	@Override
	protected void setUpGUI() {
		setUpUserInfo();
		setUpTetriminoStat();
		setUpLineStat();
		setUpButton();
	}
	
	private void setUpUserInfo() {
		userInfo = Initialize.createPanel(BACK_COLOR, HORIZONTAL_FRAME_PADDING, VERTICAL_FRAME_PADDING, PANEL_WIDTH, 90);
		
		JLabel name, level, score, lines;
		int width, height = 30, fontSize = 15;
		
		width = 300;
		String nameText = "NAME : " + getUserName();
		name = Initialize.createLabel(nameText, FORE_COLOR, BACK_COLOR, true, width, height, fontSize);
		
		width = 100;
		String levelText = "LEVEL : " + String.valueOf(getLevel());
		level = Initialize.createLabel(levelText, FORE_COLOR, BACK_COLOR, true, width, height, fontSize);
		
		width = 160;
		String scoreText = "SCORE : " + String.valueOf(getHighScore());
		score = Initialize.createLabel(scoreText, FORE_COLOR, BACK_COLOR, true, width, height, fontSize);
		
		width = 100;
		String lineText = "LINES : " + String.valueOf(getLines());
		lines = Initialize.createLabel(lineText, FORE_COLOR, BACK_COLOR, true, width, height, fontSize);
		
		userInfo.add(name);
		userInfo.add(level);
		userInfo.add(score);
		userInfo.add(lines);
		this.add(userInfo);
	}
	
	private void setUpTetriminoStat() {
		int panelY = userInfo.getY() + userInfo.getHeight() + VERTICAL_FRAME_PADDING;
		tetriminoInfo = Initialize.createPanel(BACK_COLOR, HORIZONTAL_FRAME_PADDING, panelY, PANEL_WIDTH, 320);
		
		int width = 215, height = 70;
		JPanel jShape, lShape, zShape, sShape, iShape, tShape, oShape;
		
		jShape = initInsidePanel(width, height, Shape.JShape, getJShape());
		lShape = initInsidePanel(width, height, Shape.LShape, getLShape());
		zShape = initInsidePanel(width, height, Shape.ZShape, getZShape());
		sShape = initInsidePanel(width, height, Shape.SShape, getSShape());
		iShape = initInsidePanel(width, height, Shape.IShape, getIShape());
		tShape = initInsidePanel(width, height, Shape.TShape, getTShape());
		oShape = initInsidePanel(width, height, Shape.OShape, getOShape());
		
		tetriminoInfo.add(jShape);
		tetriminoInfo.add(lShape);
		tetriminoInfo.add(zShape);
		tetriminoInfo.add(sShape);
		tetriminoInfo.add(iShape);
		tetriminoInfo.add(tShape);
		tetriminoInfo.add(oShape);
		this.add(tetriminoInfo);
	}
	
	private void setUpLineStat() {
		int panelY = tetriminoInfo.getY() + tetriminoInfo.getHeight() + VERTICAL_FRAME_PADDING;
		lineInfo = Initialize.createPanel(BACK_COLOR, HORIZONTAL_FRAME_PADDING, panelY, PANEL_WIDTH, 130);
		
		JLabel oneLine, twoLines, threeLines, tetris;
		int width = 215, height = 50, fontSize = 20;
		
		String text = "";
		
		text = "ONE LINE        " + String.valueOf(getOneLine());
		oneLine 	= Initialize.createLabel(text , FORE_COLOR, BACK_COLOR, true, width, height, fontSize);
		
		text = "TWO LINES     " + String.valueOf(getTwoLines());
		twoLines 	= Initialize.createLabel(text, FORE_COLOR, BACK_COLOR, true, width, height, fontSize);
		
		text = "THREE LINES    " + String.valueOf(getThreeLines());
		threeLines 	= Initialize.createLabel(text, FORE_COLOR, BACK_COLOR, true, width, height, fontSize);
		
		text = "TETRIS             " + String.valueOf(getTetris());
		tetris 		= Initialize.createLabel(text, FORE_COLOR, BACK_COLOR, true, width, height, fontSize);
		
		lineInfo.add(oneLine);		lineInfo.add(threeLines);
		lineInfo.add(twoLines);		lineInfo.add(tetris);
		this.add(lineInfo);
	}
	
	private void setUpButton() {
		JButton reset, play, back;
		
		int width = 100, height = 30, fontSize = 15;
		
		//bottom-left
		int x = HORIZONTAL_FRAME_PADDING;
		int y = lineInfo.getY() + lineInfo.getHeight() + VERTICAL_FRAME_PADDING;
		back = Initialize.createButton("BACK", Color.GREEN, BACK_COLOR, width, height, fontSize);
		back.setBounds(x, y, width, height);
		
		//bottom-middle
		x = (lineInfo.getX() + lineInfo.getWidth() / 2) - (width / 2);
		play = Initialize.createButton("PLAY", Color.CYAN, BACK_COLOR, width, height, fontSize);
		play.setBounds(x, y, width, height);
		
		//bottom-right
		x = lineInfo.getX() + lineInfo.getWidth() - width;
		reset = Initialize.createButton("RESET", Color.RED, BACK_COLOR, width, height, fontSize);
		reset.setBounds(x, y, width, height);
		
		Action action = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == reset) {
					JFrame parentFrame = game.getFrameInstance();
					if((boolean) CustomizedDialog.getInput(parentFrame, "", "Do you really want to erase the data?", CustomizedDialog.YES_OR_NO, null, null)) {
						resetPreferences();
						game.setCurrentScreen(new ScoreScreen(game));
					}
				}
				
				if(e.getSource() == back)	game.setCurrentScreen(new MainMenuScreen(game));
				if(e.getSource() == play) {
					PlayScreen playScreen = new PlayScreen(game); 
					game.setCurrentScreen(playScreen);
					playScreen.getPlayPanel().requestFocusInWindow();
				}
			}
		};
		
		reset.addActionListener(action);
		play.addActionListener(action);
		back.addActionListener(action);
		
		this.add(reset);
		this.add(play);
		this.add(back);
	}
	
	private JPanel initInsidePanel(int width, int height, Shape shape, int value) {
		final int HORIZONTAL_PADDING = 10;
		final int VERTICAL_PADDING = 5;
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(width, height);
			}
		};
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, HORIZONTAL_PADDING, VERTICAL_PADDING));
		panel.setBackground(BACK_COLOR);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2, true));
		
		int lblHeight = 60, fontSize = 25;
		int lblIconWidth = 100, lblTextWidth = width - lblIconWidth * 3 / 2;
		
		JLabel shapeIcon, shapeValue;
		ImageIcon scaledShape = null;
		
		scaledShape = scaleImage(Tetrimino.getImage(shape), Tetrimino.getDimension(shape));
		shapeIcon = Initialize.createLabel(scaledShape, BACK_COLOR, false, lblIconWidth, lblHeight);
		shapeValue = Initialize.createLabel(String.valueOf(value), FORE_COLOR, BACK_COLOR, false, lblTextWidth, lblHeight, fontSize);
		
		shapeIcon.setHorizontalAlignment(JLabel.CENTER);
		
		panel.add(shapeIcon);
		panel.add(shapeValue);
		
		return panel;
	}
	
	private ImageIcon scaleImage(Image image, int[] dimension) {
		int scale = 20;
		int scaledWidth = dimension[0] * scale;
		int scaledHeight = dimension[1] * scale;
		return new ImageIcon(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));
	}
}