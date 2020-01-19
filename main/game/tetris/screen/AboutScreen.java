package main.game.tetris.screen;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import main.game.tetris.GameApp;
import main.game.tetris.util.BufferedImageLoader;
import main.game.tetris.util.Initialize;

public class AboutScreen extends Screen{
	private static final long serialVersionUID = 1L;
	
	private final Color FORE_COLOR = Color.LIGHT_GRAY;
	private final Color BACK_COLOR = Color.BLACK;
	
	private final int PANEL_WIDTH = 450;
	private final int PADDING = 20;
	
	private JPanel aboutTetris, aboutDeveloper, link;
	private JButton back, gmailAddress;
	
	private boolean showGmailAddress;
	
	private URI FACEBOOK_URI, TWITTER_URI, GITHUB_URI, LINKEDIN_URI, GITHUB_REPOSITORY_URI;
	
	public AboutScreen(GameApp game) {
		super(game);
		showGmailAddress = false;
		setUpURI();
		setUpGUI();
	}

	private void setUpURI() {
		try {
			FACEBOOK_URI = new URI("https://web.facebook.com/PhiliksDCat");
			TWITTER_URI  = new URI("https://twitter.com/FELIXible69");
			GITHUB_URI 	 = new URI("https://github.com/Philiks");
			LINKEDIN_URI = new URI("https://www.linkedin.com/in/felix-janopol/");
			GITHUB_REPOSITORY_URI = new URI("https://github.com/Philiks/classic-games");
		} catch (URISyntaxException e) {	e.printStackTrace();	}
	}
	
	@Override
	protected void setUpGUI() {
		setUpAboutTetris();
		setUpAboutDeveloper();
		setUpLink();
		setUpButton();
	}
	
	private void setUpAboutTetris() {
		int x, y, width, height;
		
		x = PADDING;	y = PADDING;	width = PANEL_WIDTH;	height = 380;
		aboutTetris = Initialize.createPanel(BACK_COLOR, x, y, width, height);
		
		int fontSize = 30;
		width = 430;	height = 30;
		JLabel aboutTitle = Initialize.createLabel("ABOUT", FORE_COLOR, BACK_COLOR, false, width, height, fontSize);
		aboutTitle.setHorizontalAlignment(JLabel.CENTER);
		 
		String text = "<html><p style=\"font-family: Arial; font-size: 12px; text-indent: 30px; text-align: justify; "
				+ "word-break: break-all; white-space: normal; color: #D3D3D3;\">"
				+ "Tetris is a tile-based puzzle game programmed by Russian software engineer Alexey Pajitnov. "
				+ "Using left and right directions, the player can maneuver on the playing field to create a "
				+ "terrain of Tetrimino pieces and a create a full line and earn a score. Reaching the top "
				+ "with no available moves marks the end of the game. This tetris game have the techniques in present current game. "
				+ "However, SRS <i>(Super Rotation System)</i> is not present in this game. Therefore, T-Spins and "
				+ "the like can be done only once.<br><br>"
				+ "Disclaimer : This game is not for commercial used and therefore, all rights are reserved to "
				+ "the owner of the original Tetris game. This game only exhibits Tetris' texhniques with "
				+ "own interpretation and logic from the developer of this game <i>(Felix Janopol Jr., 2020).</i></p></html>";
	
		height = 320;
		JTextPane pane = initTextPane(text);
		JScrollPane aboutText = initScrollPane(pane, width, height);
		
		aboutTetris.add(aboutTitle);
		aboutTetris.add(aboutText);
		this.add(aboutTetris);
	}
	
	private void setUpAboutDeveloper() {
		int x, y, width, height;
		
		y = aboutTetris.getY() + aboutTetris.getHeight() + PADDING;
		x = PADDING;	width = PANEL_WIDTH;	height = 180;
		aboutDeveloper = Initialize.createPanel(BACK_COLOR, x, y, width, height);
		
		int fontSize = 20;
		width = 430;	height = 20;
		JLabel aboutTitle = Initialize.createLabel("DEVELOPER", FORE_COLOR, BACK_COLOR, false, width, height, fontSize);
		aboutTitle.setHorizontalAlignment(JLabel.CENTER);
		
		String text = "<html><p style=\"font-family: Arial; font-size: 12px; text-indent: 30px; text-align: justify; "
				+ "word-break: break-all; white-space: normal; color: #D3D3D3;\">"
				+ "The developer, Felix Janopol Jr. expresses his greatest gratitude for looking into this app. "
				+ "You, the user, have all the freedom to edit the source file in order to improve the current source code "
				+ "and to help the community. Just go to the GitHub repository "
				+ "<a href=" + GITHUB_REPOSITORY_URI + ">link</a> of this project. </p></html>";
	
		height = 130;
		JTextPane pane = initTextPane(text);
		pane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) 
					open(GITHUB_REPOSITORY_URI);
			}
		});
		
		JScrollPane aboutText = initScrollPane(pane, width, height);
		
		aboutDeveloper.add(aboutTitle);
		aboutDeveloper.add(aboutText);
		this.add(aboutDeveloper);
	}

	private void setUpLink() {
		BufferedImageLoader loader = new BufferedImageLoader();
		int x, y, width, height;
		
		y = aboutDeveloper.getY() + aboutDeveloper.getHeight() + PADDING / 2;
		x = PADDING;	width = PANEL_WIDTH - 100;	height = 60;
		link = Initialize.createPanel(BACK_COLOR, x, y, width, height);
		link.setBorder(null);
		
		setUpGmailAddressButton();
		
		JLabel facebook, twitter, gitHub, linkedIn, gmail;
		
		width = 30;	height = 30;	
		facebook	= 	initLogo(loader.loadImage("/logo/facebook_logo.png"), "Facebook", FACEBOOK_URI, width, height);
		twitter		= 	initLogo(loader.loadImage("/logo/twitter_logo.png"), "Twitter", TWITTER_URI, width, height);
		gitHub		= 	initLogo(loader.loadImage("/logo/github_logo.png"), "GitHub", GITHUB_URI, width, height);
		linkedIn	=	initLogo(loader.loadImage("/logo/linkedin_logo.png"), "LinkedIn", LINKEDIN_URI, width, height);
		gmail		=	initLogo(loader.loadImage("/logo/gmail_logo.png"), "Gmail", null, width, height);
		
		link.add(facebook);
		link.add(twitter);
		link.add(gitHub);
		link.add(linkedIn);
		link.add(gmail);
		link.add(gmailAddress);
		
		this.add(link);
	}
	
	private void setUpButton() {
		int x, y, width, height, fontSize;
		
		width = 100;	height = 30;	fontSize = 15;
		x = aboutDeveloper.getX() + aboutDeveloper.getWidth() - width;
		y = link.getY() + 7;
		back = Initialize.createButton("BACK", FORE_COLOR, BACK_COLOR, width, height, fontSize);
		back.setBounds(x, y, width, height);
		
		back.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				game.setCurrentScreen(new MainMenuScreen(game));
			}
		});
		
		this.add(back);
	}
	
	private JScrollPane initScrollPane(JTextPane pane, int width, int height) {
		JScrollPane scroll = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(width, height);
			}
		};
		scroll.getViewport().setOpaque(false);
		scroll.setOpaque(false);
		
		return scroll;
	}

	private JTextPane initTextPane(String text) {
		JTextPane pane = new JTextPane();
		pane.setContentType("text/html");
		pane.setEditable(false);

		pane.setBackground(BACK_COLOR);
		pane.setText(text);
		
		return pane;
	}

	private JLabel initLogo(Image logo, String toolTipText, URI uri, int width, int height) {
		ImageIcon icon = new ImageIcon(logo.getScaledInstance(width, height, Image.SCALE_SMOOTH));
		
		JLabel label = Initialize.createLabel(icon, BACK_COLOR, false, width, height);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label.setToolTipText(toolTipText);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//if uri is null, then the gmail was clicked
				//else, the rest of the logo was clicked
				if(uri == null)	{
					showGmailAddress = !showGmailAddress;
					gmailAddress.setVisible(showGmailAddress);
				}else	open(uri);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				label.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				label.setBorder(null);
			}
		});
		return label;
	}
	
	private void setUpGmailAddressButton() {
		String text = "fjanopol@gmail.com";
		int width = 130, height = 30, fontSize = 12;
		
		gmailAddress = Initialize.createButton(text, FORE_COLOR, BACK_COLOR, width, height, fontSize);
		gmailAddress.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		gmailAddress.setVisible(false);
		gmailAddress.setFocusable(false);
		gmailAddress.setToolTipText("copy email address to clipboard");
		gmailAddress.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				StringSelection stringSelection = new StringSelection(text);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
	}
	
	private void open(URI uri) {
		if(Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e1) {	e1.printStackTrace();	}
		}
	}
}