package main.game.tetris.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CustomizedDialog implements ActionListener{
	private JDialog dialog;
	private JPanel panel;

	//source of event
	private JTextField textField = null;
	private JButton okButton = null, yesButton = null, noButton = null;
	
	public static final int OK_ONLY = 0;	//there is only an OK button 
	public static final int YES_OR_NO = 1;	//there are two buttons -- YES and NO
	
	private static int SELECTED_BUTTON_STYLE = -1;
	
	private final Color FORE_COLOR = Color.WHITE;
	private final Color BACK_COLOR = Color.BLACK;
	
	private static String strValue;		//return value if the source is text field
	private static boolean boolValue;	//return value if the source are yes/no buttons
	
	private CustomizedDialog(JFrame parentFrame, String title, String text, String header, String footer) {
		boolean hasHeader = header != null;
		boolean hasFooter = footer != null;
		
		initPanel(hasHeader, hasFooter);
		setUpGUI(text, header, footer);
		initDialog(parentFrame, title);
	}
	
	private void initPanel(boolean hasHeader, boolean hasFooter) {
		final int PADDING = 10;
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				if(hasHeader && hasFooter)	return new Dimension(500, 300);
				else if((hasHeader && !hasFooter) || (!hasHeader && hasFooter))	return new Dimension(500, 200);
				else	return new Dimension(300, 100);
			}
		};
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, PADDING, PADDING));
		panel.setBackground(BACK_COLOR);
	}

	private void initDialog(JFrame parentFrame, String title) {
		dialog = new JDialog(parentFrame, title, true);
		dialog.setContentPane(panel);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
		dialog.setLocationRelativeTo(parentFrame);
		dialog.setVisible(true);
	}

	private void setUpGUI(String text, String header, String footer) {
		JLabel lblHeader = null, lblText = null, lblFooter = null; 
		
		int width = 480, height = 110, fontSize = -1;
		
		if(header != null) {
			lblHeader = Initialize.createLabel(header, FORE_COLOR, BACK_COLOR, false, width, height, fontSize);
			lblHeader.setHorizontalAlignment(JLabel.CENTER);
		}
		
		if(footer != null) {
			lblFooter = Initialize.createLabel(footer, FORE_COLOR, BACK_COLOR, true, width, height, fontSize);
			lblFooter.setHorizontalAlignment(JLabel.CENTER);
		}
		
		width = 100; height = 30; fontSize = 15;
		
		int buttonWidth = 60;
		
		switch(SELECTED_BUTTON_STYLE) {
		case OK_ONLY:
			lblText = Initialize.createLabel(text, FORE_COLOR, BACK_COLOR, false, width, height, fontSize);
			
			int textFieldWidth = 250;
			textField = Initialize.createTextField(FORE_COLOR, BACK_COLOR, textFieldWidth, height, fontSize);
			okButton = Initialize.createButton("OK", Color.GREEN, BACK_COLOR, buttonWidth, height, fontSize);
			
			textField.addActionListener(this);
			okButton.addActionListener(this);
			break;
			
		case YES_OR_NO:
			buttonWidth = 100;
			yesButton = Initialize.createButton("YES", Color.GREEN, BACK_COLOR, buttonWidth, height, fontSize);
			noButton = Initialize.createButton("NO", Color.RED, BACK_COLOR, buttonWidth, height, fontSize);
			
			yesButton.addActionListener(this);
			noButton.addActionListener(this);
			
			width = 300;
			lblText = Initialize.createLabel(text, FORE_COLOR, BACK_COLOR, false, width, height, fontSize);
			//makes the text centered since it is now a question
			lblText.setHorizontalAlignment(JLabel.CENTER);
			break;
		}
		
		if(lblHeader != null)	panel.add(lblHeader);
		panel.add(lblText);
		
		if(SELECTED_BUTTON_STYLE == OK_ONLY) {
			panel.add(textField);
			panel.add(okButton);
		}else {
			panel.add(yesButton);
			panel.add(noButton);
		}
		
		if(lblFooter != null) panel.add(lblFooter);
	}

	/**
	 * The content pane could (optional) display a header and a footer.
	 * The dialog show have a type of button, either an OK_ONLY or a YES_OR_NO
	 * @param parentFrame	-> the parent frame to where the modal dialog should depend
	 * @param title			-> the title of the dialog (could be null)
	 * @param text			-> the text that corresponds to the textField or button 
	 * @param BUTTON_STYLE	-> to identify whether to use an OK_ONLY or a YES_OR_NO buttons 
	 * @param header		-> additional text above the text label
	 * @param footer		-> additional text below the text label or buttons
	 */
	public static Object getInput(JFrame parentFrame, String title, String text, int BUTTON_STYLE, String header, String footer) {
		SELECTED_BUTTON_STYLE = BUTTON_STYLE;
		new CustomizedDialog(parentFrame, title, text, header, footer);
		return getValue();
	}
	
	public static Object getValue() {
		return SELECTED_BUTTON_STYLE == OK_ONLY ? strValue : boolValue;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(SELECTED_BUTTON_STYLE == OK_ONLY)	strValue = textField.getText().trim();
		if(SELECTED_BUTTON_STYLE == YES_OR_NO)	boolValue = e.getSource() == yesButton ? true : false;
		
		dialog.dispose();
	}
}