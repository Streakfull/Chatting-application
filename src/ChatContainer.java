import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class ChatContainer extends JPanel {
	private JPanel cards;
	private CardLayout cardLayout;
	private ArrayList<PrivateChat> privateChats;

	public ChatContainer() {
		super();
		cards = new JPanel();
		cards.setLayout(new CardLayout());
		privateChats = new ArrayList<PrivateChat>();
		this.add(cards);

	}

	public void addChatRoom(PrivateChat privateChat) {
		cards.add(privateChat, privateChat.getReceiverName());
		privateChats.add(privateChat);
	}
	public JPanel getCards() {
		return cards;
	}
	public void forwardMessage(String message,String receiver) {
		privateChats.forEach(privateChat -> {
			if(privateChat.getReceiverName().equals(receiver))
				privateChat.receiveMessage(message);
		});
	}
}
