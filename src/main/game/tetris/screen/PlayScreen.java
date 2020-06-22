package main.game.tetris.screen;

import static main.game.tetris.util.Statistics.getHighScore;
import static main.game.tetris.util.Statistics.getUserName;
import static main.game.tetris.util.Statistics.updateHighScore;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import main.game.tetris.GameApp;
import main.game.tetris.tetrimino.Shape;
import main.game.tetris.tetrimino.Tetrimino;
import main.game.tetris.util.CustomizedDialog;
import main.game.tetris.util.Initialize;

public class PlayScreen extends Screen{
	private static final long serialVersionUID = 1L;

	private Tetrimino tetrimino;
	
	private JPanel playPanel;
	private HUDPanel nextTetrimino, holdTetrimino, level, score, lines, status;
	private JButton back;
	
	public PlayScreen(GameApp game) {
		super(game);
		tetrimino = new Tetrimino();
		setUpGUI();
	}

	@Override
	protected void setUpGUI() {
		/*
		 *	score			
		 *					next piece
		 *	play field		level
		 *					line
		 *					hold
		 *
		 *	pause/start 	back
		 */
		
		int pad = 20;
		
		int leftColumnWidth = 300;
		int rightColumnWidth = 100;
		
		int leftHudHeight = 40;
		int rightHudHeight = 70;
		int hudPieceHeight = 2 * rightHudHeight;
				
		int playFieldHeight = 544;
		playFieldHeight -= playFieldHeight % 10;	//makes the height divisible by 10
		
		String nextPiece = "<html><body style=\"font-family:Arial;font-size:" + rightHudHeight / 4 + "px;\"><center>NEXT<br>PIECE</center></body></html>";
		
		Shape nextShape = tetrimino.getNextShape();
		
		score = new HUDPanel("0", leftColumnWidth, leftHudHeight);
		status = new HUDPanel("Status", leftColumnWidth, leftHudHeight);
		level = new HUDPanel("Level", "0", rightColumnWidth, rightHudHeight);
		lines = new HUDPanel("Lines", "0", rightColumnWidth, rightHudHeight);
		nextTetrimino = new HUDPanel(nextPiece, Tetrimino.getImage(nextShape), Tetrimino.getDimension(nextShape), rightColumnWidth, hudPieceHeight);
		holdTetrimino = new HUDPanel("Hold", null, null, rightColumnWidth, hudPieceHeight);
		back = Initialize.createButton("BACK", Color.CYAN, BACK_COLOR, rightColumnWidth, leftHudHeight, 15);
		playPanel = new PlayField(tetrimino, this);
		
		int hudX;
		int hudY;
		//left HUD
		hudX = pad;
		
		hudY = pad / 2;
		score.setBounds(hudX, hudY, leftColumnWidth, leftHudHeight);
		
		hudY = score.getY() + score.getHeight() + pad / 2;
		playPanel.setBounds(hudX, hudY, leftColumnWidth, playFieldHeight);
		
		hudY = playPanel.getY() + playPanel.getHeight() + pad / 2;
		status.setBounds(hudX, hudY, leftColumnWidth, leftHudHeight);
		
		//right HUD
		hudX = status.getX() + status.getWidth() + pad;
		
		hudY = playPanel.getY();
		nextTetrimino.setBounds(hudX, hudY, rightColumnWidth, hudPieceHeight);
		
		hudY = nextTetrimino.getY() + nextTetrimino.getHeight() + (pad * 5 / 2); 
		level.setBounds(hudX, hudY, rightColumnWidth, rightHudHeight);
		
		hudY = level.getY() + level.getHeight() + pad;
		lines.setBounds(hudX, hudY, rightColumnWidth, rightHudHeight);
		
		hudY = lines.getY() + lines.getHeight() + (pad * 5 / 2);
		holdTetrimino.setBounds(hudX, hudY, rightColumnWidth, hudPieceHeight);
		
		hudY = status.getY();
		back.setBounds(hudX, hudY, rightColumnWidth, leftHudHeight);
		
		back.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				game.setCurrentScreen(new MainMenuScreen(game));
			}
		});
		
		add(playPanel);
		add(nextTetrimino);
		add(holdTetrimino);
		add(level);
		add(score);
		add(lines);
		add(status);
		add(back);
	}
	
	public JPanel getPlayPanel() {
		return playPanel;
	}
	
	public HUDPanel getHUD(String hud) {
		if(hud == "NEXT")	return nextTetrimino;
		if(hud == "HOLD")	return holdTetrimino;
		if(hud == "LEVEL")	return level;
		if(hud == "SCORE")	return score;
		if(hud == "LINES")	return lines;
		if(hud == "STATUS")	return status;
		
		return null;
	}
	
	public JButton getBackButton() {
		return back;
	}
	
	private class PlayField extends JPanel implements ActionListener, KeyListener{
		private static final long serialVersionUID = 1L;
		
		private final Color BORDER_COLOR = Color.LIGHT_GRAY;
		private final int BORDER_WIDTH = 3;
		
		private final int DIMENSION = 30;
		private final int GAME_OVER_SPEED = 10;

		private Timer gameTimer, lockTimer, clearTimer, gameOverTimer;
		private Tetrimino tetrimino;
		private PlayScreen playScreen;
		private HUDPanel nextTetrimino, holdTetrimino, level, score, lines, status;
		
		private Shape[][] shapesOnBoard;
		private BufferedImage[] tetriminoIcons;
		private int[][] originalPieceCoords;
		private int[][] movingPieceCoords;
		private int[][] ghostPieceCoords;
		
		//saved data
		private int[] tetriminoStat;
		private int[] lineStat;
		
		private LinkedList<Integer> rowsCleared = new LinkedList<>();
		private boolean hideRow;
		
		private int gameOverTopRow,		gameOverTopCol,
					gameOverBottomRow, 	gameOverBottomCol;
		
		private int[] incrementsInRow;
		private Shape currentShape;
		private int SPEED;
		private int scoreToAdd, numberOfLinesRemoved, levelCtr, 
					lockMovesLeft, clearAnimationTicksLeft;
		private boolean isOnFloor, isPaused, isDead, hasClearedLine, hasSwapPiece, showControls;
		
		public PlayField(Tetrimino tetrimino, PlayScreen playScreen) {
			setOpaque(false);
			setBorder(BorderFactory.createLineBorder(BORDER_COLOR, BORDER_WIDTH));
			addKeyListener(this);
			
			this.tetrimino = tetrimino;
			this.playScreen = playScreen;
			
			this.nextTetrimino = playScreen.getHUD("NEXT");
			this.holdTetrimino = playScreen.getHUD("HOLD");
			this.level = playScreen.getHUD("LEVEL");
			this.score = playScreen.getHUD("SCORE");
			this.lines = playScreen.getHUD("LINES");
			this.status = playScreen.getHUD("STATUS");
			
			originalPieceCoords = new int[4][2];
			movingPieceCoords = new int[4][2];
			ghostPieceCoords = new int[4][2];
			
			tetriminoStat = new int[7];
			lineStat = new int[4];
			
			tetriminoIcons = tetrimino.getTetriminoIcons();
			
			scoreToAdd = 0;
			initPlayField();
			
			incrementsInRow = new int[shapesOnBoard.length];
			
			gameTimer = new Timer(SPEED, this);
			lockTimer = new Timer(SPEED, this);
			clearTimer = new Timer(SPEED, this);
			gameOverTimer = new Timer(GAME_OVER_SPEED, this);

			setSpeed(level.getValue());
			
			gameTimer.setInitialDelay(0);
			lockTimer.setInitialDelay(0);
			clearTimer.setInitialDelay(0);
			
			//controls should only be shown before starting the game
			showControls = true;
		}
		
		private void initPlayField() {
			level.setValue(0);
			score.setValue("0");
			status.setValue("S - Start   P - Pause");
			lines.setValue(0);
			
			for(int i = 0; i < tetriminoStat.length; i++)
				tetriminoStat[0] = 0;
			
			for(int i = 0; i < lineStat.length; i++)
				lineStat[0] = 0;
			
			initProperties();
			setUpBoard();
			initNewPiece();
			initGameOverProperties();
		}
		
		private void initGameOverProperties(){
			gameOverTopRow = 0;		
			gameOverTopCol = 0;
			
			gameOverBottomRow = shapesOnBoard.length - 1;	
			gameOverBottomCol = shapesOnBoard[0].length - 1;
		}
		
		private void initProperties() {
			numberOfLinesRemoved = 0;
			lockMovesLeft = 5;	//ticks given during lock loop
			isOnFloor = false;
			clearAnimationTicksLeft = 5;	//ticks given during clear loop
			hideRow = true;	//clear animation flag
			isPaused = false;
			isDead = false;
			hasClearedLine = false;
			hasSwapPiece = false;
		}
		
		private void initNewPiece() {
			copyArray(tetrimino.getCurrentTetriminoCoords(), originalPieceCoords);
			copyArray(tetrimino.getCurrentTetriminoCoords(), movingPieceCoords);
			currentShape = tetrimino.getCurrentShape();
			
			int middle = shapesOnBoard[0].length / 2;
			
			for(int i = 0; i < 4; i++) 
				movingPieceCoords[i][0] += middle;
			
			for(int i = 0; i < 4; i++) {
				originalPieceCoords[i][1] *= -1;
				movingPieceCoords[i][1] = originalPieceCoords[i][1] - 2;
			}
			
			generateGhostPiece();
		}
		
		private void setUpNewPiece() {
			tetrimino.setCurrentShape();
			initNewPiece();	
			Shape nextShape = tetrimino.getNextShape();
			nextTetrimino.setTetrimino(Tetrimino.getImage(nextShape), Tetrimino.getDimension(nextShape));
			
			//increment the piece that has been displayed
			tetriminoStat[currentShape.ordinal()]++;
		}

		private void copyArray(int[][] src, int[][] dest) {
			for(int i = 0; i < src.length; i++) 
				for(int j = 0; j < src[i].length; j++) 
					dest[i][j] = src[i][j];
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			draw(g);
		}
		
		private void draw(Graphics g) {
			//play field
			g.setColor(Color.CYAN);
			for(int i = 0; i < shapesOnBoard.length; i++) {
				for(int j = 0; j < shapesOnBoard[i].length; j++) {
					if(shapesOnBoard[i][j] != Shape.NoShape) {
						BufferedImage tetriminoIcon = tetriminoIcons[shapesOnBoard[i][j].ordinal()];
						Image scaledTetrimino = tetriminoIcon.getScaledInstance(DIMENSION, DIMENSION, Image.SCALE_SMOOTH);
						g.drawImage(scaledTetrimino, j * DIMENSION, i * DIMENSION, null);
					}

					//border
					g.drawRect(j * DIMENSION, i * DIMENSION, DIMENSION, DIMENSION);
				}
			}
			
			//show controls before the start of the game 
			if(showControls) {
				int rectX = 30;
				int rectY = 130;
				int rectWidth = 240;
				int rectHeight = 280;
				
				g.setColor(Color.DARK_GRAY);
				g.fillRect(rectX, rectY, rectWidth, rectHeight);
				g.setColor(Color.YELLOW);
				g.drawRect(rectX, rectY, rectWidth, rectHeight);
								
				int x = 50;
				int y = 175;
				int interval = 25;
				
				g.setFont(new Font("Arial", Font.BOLD, 20));
				
				g.drawString("(left) - left", x, y);
				g.drawString("(right) - right", x, y + interval);
				g.drawString("(down) - bring down", x, y + interval * 2);
				g.drawString("(up) - rotate", x, y + interval * 3);
				
				g.drawString("Z - rotate left", x, y + interval * 5);
				g.drawString("X - rotate right", x, y + interval * 6);
				g.drawString("C - hold piece", x, y + interval * 7);
				g.drawString("(space) - hard drop", x, y + interval * 8);
			}
		}
		
		private void setUpBoard() {
			shapesOnBoard = new Shape[18][10];
			for(int i = 0; i < shapesOnBoard.length; i++)
				for(int j = 0; j < shapesOnBoard[i].length; j++)
					shapesOnBoard[i][j] = Shape.NoShape;
		}

		private void generateGhostPiece() {
			LinkedList<Integer> listToExamine = setListToExamine();
			
			//clears the ghostPiece
			for(int i = 0; i < movingPieceCoords.length; i++) {
				int currX = ghostPieceCoords[i][0];
				int currY = ghostPieceCoords[i][1];
				
				if(currY < 0 || currX < 0)	continue;
				
				if(shapesOnBoard[currY][currX] == Shape.GhostShape)
					shapesOnBoard[currY][currX] = Shape.NoShape;
			}
			
			int ghostY = Integer.MAX_VALUE;
			
			//compute for the optimal ghostPiece coords
			for(int piece = 0; piece < movingPieceCoords.length; piece++) {
				int currX = movingPieceCoords[piece][0];
				int currY = movingPieceCoords[piece][1];
				
				//find the distance between moving coords and ghost piece coords
				for(int row = currY; row < shapesOnBoard.length; row++) {
					int below = row + 1;
					
					if(listToExamine.contains(currX * 100 + below))
						break;
					
					if(row < 0)
						continue;
					
					if(below == shapesOnBoard.length || shapesOnBoard[below][currX] != Shape.NoShape) {
						int distance = row - currY;
						
						if(ghostY > distance)
							ghostY = distance;
						break;
					}	
				}
			}

			//generate new ghost piece
			for(int i =  ghostPieceCoords.length - 1; i >= 0; i--) {
				int currX = movingPieceCoords[i][0];
				int currY = ghostY + movingPieceCoords[i][1];

				//if the next piece would trigger gameOver, don't generate ghostPiece
				if(currY < 0) {
					ghostPieceCoords[0][0] = -1;
					return;
				}
				
				ghostPieceCoords[i][0] = currX;
				ghostPieceCoords[i][1] = currY;

				//generate only if the tile is not occupied, 
				if(shapesOnBoard[currY][currX] == Shape.NoShape) { 
					shapesOnBoard[currY][currX] = Shape.GhostShape;
				}
			}
		}

		private void moveLeft() {
			if(lockMovesLeft <= 0) return;
			if(!canGoSideways("LEFT"))	return;
			
			removeCurrentPieceOnBoard();
			replaceCurrentPieceOnBoard("COLUMN_LEFT");
			for(int i = 0; i < movingPieceCoords.length; i++)
				movingPieceCoords[i][0]--;
			
			generateGhostPiece();
			
			repaint();
		}
		
		private void moveRight() {
			if(lockMovesLeft <= 0) return;
			if(!canGoSideways("RIGHT"))	return;
			
			removeCurrentPieceOnBoard();
			replaceCurrentPieceOnBoard("COLUMN_RIGHT");
			for(int i = 0; i < movingPieceCoords.length; i++)
				movingPieceCoords[i][0]++;
			
			generateGhostPiece();
			repaint();
		}
		
		private void moveDown() {
			if(!canGoDown()) {
				if(isDead)	gameOver();
				else		isOnFloor = true;
			}else{
				removeCurrentPieceOnBoard();
				replaceCurrentPieceOnBoard("ROW");
				for(int i = 0; i < movingPieceCoords.length; i++) 
					movingPieceCoords[i][1]++;

				repaint();
			}
		}
		
		private void rotateRight() {
			if(currentShape == Shape.OShape)	return;
			if(currentShape == Shape.IShape || currentShape == Shape.ZShape || currentShape == Shape.SShape) {
				//if I piece is in default coordinates, rotate left 
				if(Math.abs(originalPieceCoords[0][0]) == 1 && originalPieceCoords[0][1] == 0) {
					rotateLeft();
					return;
				}
			}
			
			
			if(!canRotate("RIGHT"))	return;
			else {
				removeCurrentPieceOnBoard();
				replaceCurrentPieceOnBoard("ROTATE_RIGHT");
				generateGhostPiece();
			}
						
			repaint();
		}

		private void rotateLeft() {
			if(currentShape == Shape.OShape)	return;
			if(currentShape == Shape.IShape || currentShape == Shape.ZShape || currentShape == Shape.SShape) {
				//if I piece is not in default coordinates, rotate right to make it back to default
				if(!(Math.abs(originalPieceCoords[0][0]) == 1 && originalPieceCoords[0][1] == 0)) {
					rotateRight();
					return;
				}
			}
			
			if(!canRotate("LEFT"))	return;
			else {
				removeCurrentPieceOnBoard();
				replaceCurrentPieceOnBoard("ROTATE_LEFT");
				generateGhostPiece();
			}
			
			repaint();
		}
		
		private void hardDrop() {
			if(ghostPieceCoords[0][0] == -1) {
				gameOver();
				return;
			}
			
			scoreToAdd += ghostPieceCoords[0][1] - movingPieceCoords[0][1];
			
			removeCurrentPieceOnBoard();
			
			for(int i = 0; i < movingPieceCoords.length; i++) { 
				int col = ghostPieceCoords[i][0];
				int row = ghostPieceCoords[i][1];
				
				movingPieceCoords[i][0] = col;
				movingPieceCoords[i][1] = row;
				
				shapesOnBoard[row][col] = currentShape;
			}
			
			repaint();
			
			lockMovesLeft = 0;
			gameTimer.stop();
			lockTimer.start();
		}

		private void holdPiece() {
			if(hasSwapPiece)	return;
			
			Shape holdShape = tetrimino.getHoldShape();
			Shape tempShape = Shape.NoShape;
			
			hasSwapPiece = true;
			
			if(holdShape != Shape.NoShape)
				tempShape = holdShape;
				
			removeCurrentPieceOnBoard();
			tetrimino.setHoldShape(currentShape);
			
			holdTetrimino.setTetrimino(Tetrimino.getImage(currentShape), Tetrimino.getDimension(currentShape));
			
			if(holdShape == Shape.NoShape) {
				setUpNewPiece();
			}else {
				currentShape = tempShape;
				tetrimino.setCurrentShape(currentShape);
				initNewPiece();
			}
		}

		private void removeFullLine(int row) {
			// removes tiles on specified row
			for(int col = 0; col < shapesOnBoard[0].length; col++) { 
				if(shapesOnBoard[row][col] != Shape.NoShape)
					shapesOnBoard[row][col] = Shape.NoShape;
			}
		}
		
		private void removeCurrentPieceOnBoard() {
			// remove the current piece on the board
			for(int i = 0; i < movingPieceCoords.length; i++) {
				int col = movingPieceCoords[i][0];
				int row = movingPieceCoords[i][1];
				
				//	ROW ---- COLUMN_RIGHT ---- COLUMN_LEFT
				if(row >= 0 && col < shapesOnBoard[row].length && col >= 0)
					shapesOnBoard[row][col] = Shape.NoShape;
			}
		}
		
		private void replaceCurrentPieceOnBoard(String direction) {
			//place the pieces one position towards the direction
			for(int i = 0; i < movingPieceCoords.length; i++) {
				int col = movingPieceCoords[i][0];
				int row = movingPieceCoords[i][1];
				
				if(direction == "ROW") { 
					int destRow = row + 1;
					if(destRow >= 0)	
						shapesOnBoard[destRow][col] = currentShape;
				}else if(direction == "COLUMN_RIGHT" || direction == "COLUMN_LEFT") {
					int destCol;
					if(direction == "COLUMN_RIGHT")
						destCol = col + 1;
					else
						destCol = col - 1;
					
					if(row < 0)	continue;
					
					if(destCol >= 0)
						shapesOnBoard[row][destCol] = currentShape;
				}else if(direction == "ROTATE_RIGHT" || direction == "ROTATE_LEFT") {
					int origX = originalPieceCoords[i][0];
					int origY = originalPieceCoords[i][1];
					
					int increasedInX = col - origX;
					int increasedInY = row - origY;
					
					int rotatedX;
					int rotatedY;
					
					if(direction == "ROTATE_RIGHT") {
						rotatedX = increasedInX + origY;
						rotatedY = increasedInY - origX;
						
						originalPieceCoords[i][0] = origY;
						originalPieceCoords[i][1] = -origX;
					}else {
						rotatedX = increasedInX - origY;
						rotatedY = increasedInY + origX;
						
						originalPieceCoords[i][0] = -origY;
						originalPieceCoords[i][1] = origX;
					}
					
					movingPieceCoords[i][0] = rotatedX;
					movingPieceCoords[i][1] = rotatedY;
					
					if(rotatedY >= 0 && rotatedX >= 0)
						shapesOnBoard[rotatedY][rotatedX] = currentShape;
				}
			}
		}

		private boolean canGoDown() {
			LinkedList<Integer> listToExamine = setListToExamine(); 
			
			for(int j = 0; j < movingPieceCoords.length; j++) {
				int currX = movingPieceCoords[j][0];
				int currY = movingPieceCoords[j][1];
				int below = currY + 1;
				
				if(below < 0 || listToExamine.contains(currX * 100 + below))
					continue;

				if(below == shapesOnBoard.length)
					return false;

				if(shapesOnBoard[below][currX] != Shape.NoShape && shapesOnBoard[below][currX] != Shape.GhostShape) {
					if(below <= 0)	isDead = true;
					return false;
				}	
			}
			
			return true;
		}
		
		private boolean canGoSideways(String direction) {
			LinkedList<Integer> listToExamine = setListToExamine(); 
			
			for(int j = 0; j < movingPieceCoords.length; j++) {
				int currX = movingPieceCoords[j][0];
				int currY = movingPieceCoords[j][1];
				int side = direction == "LEFT" ? currX - 1 : currX + 1;
				
				if(side < 0 || side == shapesOnBoard[0].length)
					return false;

				if(currY < 0 || listToExamine.contains(side * 100 + currY))
					continue;
				
				if(shapesOnBoard[currY][side] != Shape.NoShape && shapesOnBoard[currY][side] != Shape.GhostShape) 
					return false;
			}
			
			return true;
		}
		
		private boolean canRotate(String direction) {
			LinkedList<Integer> listToExamine = setListToExamine();
			
			for(int j = 0; j < movingPieceCoords.length; j++) {
				int currX = movingPieceCoords[j][0];
				int currY = movingPieceCoords[j][1];
				int origX = originalPieceCoords[j][0];
				int origY = originalPieceCoords[j][1];
				
				int increasedInX = currX - origX;
				int increasedInY = currY - origY;
				
				int rotatedX = increasedInX + origY;
				int rotatedY = increasedInY - origX;
				
				//	original	 right		  left
				//	 (x, y)		(y, -x)		(-y, x)
				if(direction == "RIGHT") {
					rotatedX = increasedInX + origY;
					rotatedY = increasedInY - origX;
				}else {
					rotatedX = increasedInX - origY;
					rotatedY = increasedInY + origX;
				}
				
				if(rotatedY < 0 || listToExamine.contains(rotatedX * 100 + rotatedY)) 
					continue;
				
				if(rotatedX < 0) 
					return false;
				
				if(rotatedX >= shapesOnBoard[0].length) 
					return false;
					
				if(rotatedY >= shapesOnBoard.length) 
					return false;
					
				if(shapesOnBoard[rotatedY][rotatedX] != Shape.NoShape && shapesOnBoard[rotatedY][rotatedX] != Shape.GhostShape) 
					return false;
			}
			
			return true;
		}
		
		private void checkForFullLine() {
			for(int i = 0; i < incrementsInRow.length; i++)
				incrementsInRow[i] = 0;
			
			LinkedList<Integer> listToExamine = setListToExamine();
			
			for(int j = 0; j < movingPieceCoords.length; j++) {
				int currX = movingPieceCoords[j][0];
				int currY = movingPieceCoords[j][1];
				int right = currX + 1;
				
				if(currY < 0 || listToExamine.contains(right * 100 + currY))	continue;
				
				//check the column
				int col;
				for(col = 0; col < shapesOnBoard[0].length; col++) 
					if(shapesOnBoard[currY][col] == Shape.NoShape)
						break;
				
				//increments the row above the line
				if(col == shapesOnBoard[0].length) {
					for(int k = currY - 1; k >= 0; k--) 
						incrementsInRow[k]++;
					
					rowsCleared.add(currY);
					
					numberOfLinesRemoved++;
				}
			}
			
			hasClearedLine = !rowsCleared.isEmpty();
		}

		private LinkedList<Integer> setListToExamine() {
			LinkedList<Integer> listToExamine = new LinkedList<>(); 
			for(int i = 0; i < movingPieceCoords.length; i++) {
				int currX = movingPieceCoords[i][0];
				int currY = movingPieceCoords[i][1];
				listToExamine.add(currX * 100 + currY);
			}
			return listToExamine;
		}

		private void bringDownTiles(int[] increments) {
			for(int row = increments.length - 1; row >= 0; row--) {
				if(increments[row] == 0)	continue;
				
				int destRow = row + increments[row];
				
				for(int col = 0; col < shapesOnBoard[0].length; col++)
					shapesOnBoard[destRow][col] = shapesOnBoard[row][col];  
						
				removeFullLine(row);
			}
		}
		
		private void calculateScore() {
			int level = this.level.getValue();
			int multiplier = 0;
			
			//means that the player leveled up before calculating the score
			//therefore, decrementing it would bring accurate result
			if(levelCtr == 0)	level--;
			
			switch(numberOfLinesRemoved) {
			case 1:
				lineStat[0]++;
				multiplier = 40;
				break;
			case 2:
				lineStat[1]++;
				multiplier = 100;
				break;
			case 3:
				lineStat[2]++;
				multiplier = 300;
				break;
			case 4:
				lineStat[3]++;
				multiplier = 1200;
				break;
			}
			
			scoreToAdd += multiplier * (level + 1);
			score.setValue(score.getValue() + scoreToAdd);
			scoreToAdd = 0;
		}

		private void levelUp() {
			levelCtr = 0;
			level.setValue(level.getValue() + 1);
			setSpeed(level.getValue());
		}
		
		private void setSpeed(int level) {
			SPEED = 1000 - (level * 25);
			int lockSpeed = (SPEED / 4) / 5;
			int clearSpeed = (SPEED / 2) / (5 / 2);
			
			gameTimer.setDelay(SPEED);
			lockTimer.setDelay(lockSpeed);
			clearTimer.setDelay(clearSpeed);
		}
		
		private void gameOver() {
			isDead = true;
			gameTimer.stop();
			gameOverTimer.start();
			
			int highscore = getHighScore();
			int user_score = score.getValue(); 
			int user_level = level.getValue();
			int user_lines = lines.getValue();
			String old_user_name = getUserName();
			
			if(user_score > highscore) {
				JFrame parentFrame = playScreen.game.getFrameInstance();
				String header = "<html><center><span style=\font-family:Arial;font-size:30;\">NEW HIGH SCORE!!!</span>"
								+ "<br><span style=\"font-family:Arial;font-size:60;\">" + user_score + "</span></center></html>";
				String footer = "<html><body><center><h3>OLD HIGH SCORE<br>" + highscore + "<br>" + old_user_name + "</h3></center></body></html>";
				String user_name = 
						highscore == 0 ? 
						  (String) CustomizedDialog.getInput(parentFrame, "", "Enter name :", CustomizedDialog.OK_ONLY, header, null)
						: (String) CustomizedDialog.getInput(parentFrame, "", "Enter name :", CustomizedDialog.OK_ONLY, header, footer);
						  
				updateHighScore(user_name, user_score, user_level, user_lines, tetriminoStat, lineStat);
			}
			
			status.setValue("GAME OVER");
		}
		
		private void restart() {
			tetrimino.setCurrentShape();
			scoreToAdd = 0;
			initPlayField();
			setSpeed(level.getValue());
			
			Shape nextShape = tetrimino.getNextShape();
			nextTetrimino.setTetrimino(Tetrimino.getImage(nextShape), Tetrimino.getDimension(nextShape));
			
			repaint();
			
			gameTimer.start();
		}
		
		private void gameLoop() {
			if(isOnFloor) {
				lockMovesLeft = 5;
				
				gameTimer.stop();
				lockTimer.start();
			}else
				moveDown();
		}
		
		private void lockLoop() {
			if(lockMovesLeft == 0) {
				//if the piece can't go down, doChangePiece will be triggered
				//else, go back to the game loop
				if(!canGoDown()) {
					checkForFullLine();
					calculateScore();
				}else {
					initProperties();
					lockTimer.stop();
					gameTimer.start();
					return;
				}

				if(hasClearedLine) {
					if(numberOfLinesRemoved == 4)
							status.setValue("TETRIS");

					lockTimer.stop();
					clearTimer.start();
				}else {
					setUpNewPiece();
					initProperties();
					lockTimer.stop();
					gameTimer.start();
				}
			}
			lockMovesLeft--;
		}
		
		private void clearAnimation() {
			if(clearAnimationTicksLeft == 0) {
				for(int row = 0; row < rowsCleared.size(); row++)
					removeFullLine(rowsCleared.get(row));
				
				rowsCleared.clear();
				
				bringDownTiles(incrementsInRow);
				lines.setValue(lines.getValue() + numberOfLinesRemoved);
				
				levelCtr += numberOfLinesRemoved;
				if(levelCtr >= 10) 
					levelUp();

				status.setValue("S - Start   P - Pause");
				
				setUpNewPiece();
				initProperties();
				clearTimer.stop();
				gameTimer.start();
			}else {
				if(hideRow) 
					for(int row = 0; row < rowsCleared.size(); row++) 
						for(int col = 0; col < shapesOnBoard[0].length; col++) 
							shapesOnBoard[rowsCleared.get(row)][col] = Shape.NoShape;
				else 
					for(int row = 0; row < rowsCleared.size(); row++)  
						for(int col = 0; col < shapesOnBoard[0].length; col++)  
							shapesOnBoard[rowsCleared.get(row)][col] = Shape.ClearShape;
				
				hideRow = !hideRow;
				clearAnimationTicksLeft--;
				repaint();
			}
		}
		
		private void gameOverAnimation() {
			if(gameOverTopRow == shapesOnBoard.length / 2) {
				gameOverTimer.stop();
				initGameOverProperties();
				
				//reset the field
				for(int row = 0; row < shapesOnBoard.length; row++)
					for(int col = 0; col < shapesOnBoard[row].length; col++)
						shapesOnBoard[row][col] = Shape.NoShape;

				showControls = true;
				playScreen.getBackButton().setEnabled(true);
				
				repaint();
			}
			
			if(gameOverTopCol == shapesOnBoard[0].length) {
				gameOverTopRow++;
				gameOverTopCol = 0;
				
				gameOverBottomRow--;
				gameOverBottomCol = shapesOnBoard[0].length - 1;
			}
			
			shapesOnBoard[gameOverTopRow][gameOverTopCol] = Shape.ClearShape;
			shapesOnBoard[gameOverBottomRow][gameOverBottomCol] = Shape.ClearShape;
			gameOverTopCol++;
			gameOverBottomCol--;
			repaint();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == gameTimer)		gameLoop();
			if(e.getSource() == lockTimer) 		lockLoop();
			if(e.getSource() == clearTimer)		clearAnimation();
			if(e.getSource() == gameOverTimer)	gameOverAnimation();
		}
		
		@Override
		public void keyTyped(KeyEvent e) {	}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if(showControls) {
				if(key == KeyEvent.VK_S) {
					gameTimer.start();
					showControls = false;
					playScreen.getBackButton().setEnabled(false);
				}
			}
			
			//key events when the game is running
			if(gameTimer.isRunning() || lockTimer.isRunning()) {
				if(key == KeyEvent.VK_LEFT && lockMovesLeft > 0) 	moveLeft();
				if(key == KeyEvent.VK_RIGHT && lockMovesLeft > 0) 	moveRight(); 
				if(key == KeyEvent.VK_DOWN && !isOnFloor) {			moveDown();	scoreToAdd++;	}
				if(key == KeyEvent.VK_X || key == KeyEvent.VK_UP)	rotateRight();
				if(key == KeyEvent.VK_Z)							rotateLeft();
				if(key == KeyEvent.VK_SPACE) 						hardDrop();	
				if(key == KeyEvent.VK_C)							holdPiece();
				if(key == KeyEvent.VK_P) {							
					gameTimer.stop();	
					isPaused = true;	
					status.setValue("PAUSED press \"S\" to start");
					playScreen.getBackButton().setEnabled(true);
				}
			}
			
			//key events when the game is paused / stopped
			if(!gameTimer.isRunning() && isPaused) {
				if(key == KeyEvent.VK_R)	{
					if(gameOverTimer.isRunning())	gameOverTimer.stop();
					restart();
				}
				if(key == KeyEvent.VK_S)	gameTimer.start();
			}
			
			//key events if the player died
			if(isDead) {
				if(key == KeyEvent.VK_R || key == KeyEvent.VK_S) {
					restart();
				}
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {}
	}
	
	private class HUDPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		
		private JLabel title, content;
		private final int PAD = 5,	BORDER_WIDTH = 3;
		
		private final Color FORE_COLOR 	 = Color.WHITE,
							BORDER_COLOR = Color.LIGHT_GRAY;
		private final Font 	FONT;
		
		private int panelWidth, panelHeight;
		private int labelWidth, labelHeight, titleHeight, contentHeight;
		
		private int scaledDimension, scaledWidth, scaledHeight;
		
		/**
		 * For the 3 HUD's (level, score, lines)
		 * @param text		-> title
		 * @param value		-> initial value
		 * @param width		-> width of the panel
		 * @param height	-> height of the panel
		 */
		public HUDPanel(String text, int width, int height) {
			FONT = new Font("Arial", Font.BOLD, height / 2);
			
			panelWidth = width;
			panelHeight = height;
			
			labelWidth = panelWidth - 2 * PAD;
			labelHeight = panelHeight - 2 * PAD;
			
			content = new JLabel(text);
			initLabel(content, PAD, PAD, labelWidth, labelHeight);
			
			initPanel();			
		}
		
		/**
		 * For the left HUD's (score, status)
		 * @param text		-> title
		 * @param width		-> width of the panel
		 * @param height	-> height of the panel
		 */
		public HUDPanel(String text, String value, int width, int height) {
			FONT = new Font("Arial", Font.BOLD, height / 4);
			
			panelWidth = width;
			panelHeight = height;
			initDimensions();
			
			title = new JLabel(text);
			initLabel(title, PAD, PAD, labelWidth, titleHeight);
			
			content = new JLabel(value);
			initLabel(content, PAD, PAD + titleHeight, labelWidth, contentHeight);
			
			initPanel();			
		}
		
		/**
		 * For the right HUD's (level, lines)
		 * @param text		-> title
		 * @param tetrimino	-> Tetrimino color block
		 * @param coords	-> coordinates of the next Tetrimino
		 * @param width		-> width of the panel
		 * @param height	-> height of the panel
		 */
		public HUDPanel(String text, Image tetrimino, int[] dimension, int width, int height) {
			FONT = new Font("Arial", Font.BOLD, height / 8);
			
			panelWidth = width;
			panelHeight = height;
			initDimensions();
			
			title = new JLabel(text);
			initLabel(title, PAD, PAD, labelWidth, titleHeight);
			
			/*
			 * tetrimino is expected to be square
			 * therefore it's scaledDimension is the same
			 * 1/4 of the label's width 
			 */
			//dimension = width / 4;
			scaledDimension = 15;
			content = new JLabel();
			initLabel(content, PAD, PAD + titleHeight, labelWidth, contentHeight);
			setTetrimino(tetrimino, dimension);
			initPanel();
		}

		private void initPanel() {
			setLayout(null);
			setOpaque(false);
			setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(BORDER_COLOR, BORDER_WIDTH, true),
					new EmptyBorder(PAD, PAD, PAD, PAD)
					));
			
			if(title != null)	add(title);
			add(content);
		}
		
		private void initLabel(JLabel label, int x, int y, int width, int height) {
			label.setBounds(x, y, width, height);
			label.setFont(FONT);
			label.setForeground(FORE_COLOR);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, BORDER_WIDTH, true));
		}
		
		private void initDimensions() {
			labelWidth = panelWidth - 2 * PAD;
			labelHeight = panelHeight - 2 * PAD;
			titleHeight = labelHeight * 2 / 5;
			contentHeight = labelHeight - titleHeight;
		}

		public void setTetrimino(Image tetrimino, int[] dimension) {
			if(tetrimino == null)	return;
			
			scaledWidth = dimension[0] * scaledDimension;
			scaledHeight = dimension[1] * scaledDimension;
			
			Image scaledTetrimino = tetrimino.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
			content.setIcon(new ImageIcon(scaledTetrimino));
		}
		
		public void setValue(int value) {	
			content.setText(Integer.toString(value));
		}
		
		public void setValue(String value) {
			content.setText(value);
		}
		
		public int getValue() {
			return Integer.valueOf(content.getText());
		}
	}
}