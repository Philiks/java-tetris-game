package main.game.tetris.tetrimino;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.game.tetris.util.BufferedImageLoader;

public class Tetrimino {
	private Random rand;
	private static BufferedImageLoader loader;
	private BufferedImage[] tetriminoIcons;
	
	private Shape currentShape, nextShape, holdShape;
	
	private int[][] coord;
	
	private int[][][] shapeCoordsTable = {
			{{-1, 0},	{0, 0},		{1, 0},		{-1, 1}},	//JShape	
			{{-1, 0},	{0, 0},		{1, 0},		{1, 1}},	//LShape
			{{1, 0},	{0, 0},		{0, 1},		{-1, 1}},	//ZShape
			{{-1, 0},	{0, 0},		{0, 1},		{1, 1}},	//SShape
			{{-1, 0},	{0, 0},		{1, 0},		{2, 0}},	//IShape
			{{-1, 0},	{0, 0},		{1, 0},		{0, 1}},	//TShape
			{{-1, 0},	{0, 0},		{-1, 1},	{0, 1}},	//OShape
	};
	
	public Tetrimino() {
		rand = new Random();
		loader = new BufferedImageLoader();
		
		BufferedImage tetriminoSprite = loader.loadImage("/spriteSheet/tetrimino_sprite_sheet.png");
		tetriminoIcons = new BufferedImage[9];
		int spriteDimension = 64;
		for(int i = 0; i < tetriminoIcons.length; i++) 
			tetriminoIcons[i] = tetriminoSprite.getSubimage(i*spriteDimension, 0, spriteDimension, spriteDimension);
		
		coord = new int[4][2];
		
		holdShape = Shape.NoShape;
		nextShape = setRandomPiece();
		setCurrentShape();
	}
	
	private Shape setRandomPiece() {
		int next = rand.nextInt(7);
		
		switch(next) {
		case 0:
			return Shape.JShape;
		case 1:
			return Shape.LShape;
		case 2:
			return Shape.ZShape;
		case 3:
			return Shape.SShape;
		case 4:
			return Shape.IShape;
		case 5:
			return Shape.TShape;
		case 6:
			return Shape.OShape;
		default:
			return Shape.NoShape;
		}
	}
	
	public void setCurrentShape() {
		currentShape = nextShape;
		
		System.arraycopy(shapeCoordsTable[currentShape.ordinal()], 0, coord, 0, 4);
		
		nextShape = setRandomPiece();
	}
	
	//if user wants to swap from held piece
	public void setCurrentShape(Shape shape) {
		currentShape = shape;
		System.arraycopy(shapeCoordsTable[currentShape.ordinal()], 0, coord, 0, 4);
	}
	
	public void setHoldShape(Shape shape) {
		holdShape = shape;
	}
	
	public Shape getCurrentShape() {
		return currentShape;
	}
	
	public Shape getNextShape() {
		return nextShape;
	}
	
	public Shape getHoldShape() {
		return holdShape;
	}
	
	public int[][] getCurrentTetriminoCoords(){
		return shapeCoordsTable[currentShape.ordinal()];
	}
	
	public static Image getImage(Shape shape) {
		if(shape == Shape.JShape)		return new BufferedImageLoader().loadImage("/shape/JShape.png");
		else if(shape == Shape.LShape)	return new BufferedImageLoader().loadImage("/shape/LShape.png");
		else if(shape == Shape.ZShape)	return new BufferedImageLoader().loadImage("/shape/ZShape.png");
		else if(shape == Shape.SShape)	return new BufferedImageLoader().loadImage("/shape/SShape.png");
		else if(shape == Shape.IShape)	return new BufferedImageLoader().loadImage("/shape/IShape.png");
		else if(shape == Shape.TShape)	return new BufferedImageLoader().loadImage("/shape/TShape.png");
		else if(shape == Shape.OShape)	return new BufferedImageLoader().loadImage("/shape/OShape.png");
		else return null;
	}
	
	//dimension per unit i.e. OShape = 2x2
	public static int[] getDimension(Shape shape) {
		int[] dimension = new int[2];
		
		if(shape == Shape.JShape | shape == Shape.LShape | 
		   shape == Shape.ZShape | shape == Shape.SShape |
		   shape == Shape.TShape) {	
			dimension[0] = 3;	dimension[1] = 2;	
		}else if(shape == Shape.IShape) {	dimension[0] = 4;	dimension[1] = 1;	}
		else if(shape == Shape.OShape) {	dimension[0] = 2;	dimension[1] = 2;	}
		
		return dimension;
	}
	
	public BufferedImage[] getTetriminoIcons() {
		return tetriminoIcons;
	}
}