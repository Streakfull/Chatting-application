import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatScreen extends JFrame {
	private int width;
	private int height;
	private String title;
	private Color Background;
	private static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	public static int ScreenWidth = (int) (0.5 * dim.getWidth());
	public static int ScreenHeight = (int) (0.5 * dim.getHeight());
	private JPanel cards;
	private CardLayout cardLayout;
	private NamePanel name;
	private ChatPanel chat;
	Controller controller;

	public ChatScreen(int width, int height, String title, Color color) {
		super();
		this.width = width;
		this.height = height;
		this.title = title;
		this.Background = color;
		setSize(width, height);
		setTitle(title);
		getContentPane().setBackground(color);
		WindowDestroyer Listener = new WindowDestroyer();
		addWindowListener(Listener);
		setBounds(100, 100, ScreenWidth, ScreenHeight);
		setLocationRelativeTo(null);
		cards = new JPanel();
		cards.setLayout(new CardLayout());
		this.add(cards);
		//Controller
		Controller controller = new Controller(this);
		// SCREEN IMPORTS GO HERE
		name = new NamePanel(controller);
		chat = new ChatPanel(controller);
		PortPanel port = new PortPanel(controller);
		// SCREEN ADDITIONS GO HERE
		cards.add(name, "signUp");
		cards.add(port, "portPanel");
		cards.add(chat,"chatPanel");
		CardLayout cardLayout = (CardLayout) cards.getLayout();
		cardLayout.show(cards, "portPanel");
		this.setVisible(true);

	}

	public JPanel getCards() {
		return cards;
	}
	public NamePanel getNamePanel() {
		return name;
	}
	public ChatPanel getchatPanel() {
		return chat;
	}
	
	

}
