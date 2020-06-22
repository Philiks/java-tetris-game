package main.game.tetris.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Initialize {
	private final static Border outsideBorder = BorderFactory.createLineBorder(Color.GRAY, 2, true); 
	private final static Border insideBorder = BorderFactory.createEmptyBorder(0, 5, 0, 0);	//padding (top-left-bottom-right)
	
	public static JButton createButton(String text, Color foreColor, Color backColor, int width, int height, int fontSize) {
		JButton button = new JButton() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(width, height);
			}
		};
		button.setText(text);
		button.setFont(new Font("Arial", Font.BOLD, fontSize));
		button.setForeground(foreColor);
		button.setBackground(backColor);
		button.setOpaque(true);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		return button;
	}
	
	public static JTextField createTextField(Color foreColor, Color backColor, int width, int height, int fontSize) {
		JTextField textField = new JTextField(){
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(width, height);
			}
		};
		
		textField.setFont(new Font("Arial", Font.BOLD, fontSize));
		textField.setForeground(Color.WHITE);
		textField.setBackground(backColor);
		textField.setOpaque(true);
		textField.setBorder(outsideBorder);
		
		return textField;
	}
	
	public static JLabel createLabel(String text, Color foreColor, Color backColor, boolean hasBorder, int width, int height, int fontSize) {
		JLabel label = new JLabel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(width, height);
			}
		};
		
		//if fontSize == -1, therefore, it used an html string format
		if(fontSize > -1)	label.setFont(new Font("Arial", Font.BOLD, fontSize));
		
		label.setText(text);
		label.setForeground(foreColor);
		label.setBackground(backColor);
		label.setOpaque(true);
		
		//if border shown be shown, display
		//otherwise create only the border for padding
		if(hasBorder)	label.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
		else			label.setBorder(insideBorder);
		
		return label;
	}
	
	public static JLabel createLabel(ImageIcon image, Color backColor, boolean hasBorder, int width, int height) {
		JLabel label = new JLabel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(width, height);
			}
		};
		
		label.setIcon(image);
		label.setBackground(backColor);
		label.setOpaque(true);
		
		//if border shown be shown, display
		//otherwise create only the border for padding
		if(hasBorder)	label.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
		
		return label;
	}
	
	public static JPanel createPanel(Color backColor, int x, int y, int width, int height) {
		final int HORIZONTAL_PADDING = 10;
		final int VERTICAL_PADDING = 7;
		
		JPanel panel = new JPanel();
		panel.setBounds(x, y, width, height);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, HORIZONTAL_PADDING, VERTICAL_PADDING));
		panel.setBackground(backColor);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2, true));
		
		return panel;
	}
}