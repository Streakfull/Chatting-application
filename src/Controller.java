import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Controller implements ActionListener {
	private Client client;
	private ChatScreen chatScreen;
	private static final int SEND = 2;
	private static final int END = 8;

	public Controller(ChatScreen chatScreen) {
		this.chatScreen = chatScreen;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JPanel cards = chatScreen.getCards();
		CardLayout cardLayout = (CardLayout) cards.getLayout();
		String action = e.getActionCommand();
		switch (action) {
		case "ServerA":
			try {
				Client client = new Client("", 6789);
				this.client = client;
				cardLayout.show(cards, "signUp");
				break;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		case "ServerB":
			try {
				Client client = new Client("", 6790);
				this.client = client;
				cardLayout.show(cards, "signUp");
				break;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		case "Join":
			NamePanel namePanel = chatScreen.getNamePanel();
			ChatPanel chatPanel = chatScreen.getchatPanel();
			String inputName = namePanel.getName();
			try {
				client.join(inputName);
				if (client.checkUsedName()) {
					client.setName(inputName);
					client.messageLoop.start();
					client.receiveLoop.start();
					client.getClientMembers();
					chatPanel.setWelcomeName(inputName);
					cardLayout.show(cards, "chatPanel");
				} else
					namePanel.setErrorMessage("Name already Taken");
				break;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		case "refresh":
			try {
				client.getClientMembers();
				break;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		case "openChat":
			ChatPanel chat = chatScreen.getchatPanel();
			ChatContainer chatcont = chat.getChatContainer();
			JPanel chatCards = chatcont.getCards();
			CardLayout chatCardLayout = (CardLayout) chatCards.getLayout();
			String memberName = ((JButton) e.getSource()).getText();
			chatCardLayout.show(chatCards, memberName);
			break;

		}

	}

	public void addChat(PrivateChat privatechat) {
		ChatPanel chat = chatScreen.getchatPanel();
		ChatContainer chatcont = chat.getChatContainer();
		chatcont.addChatRoom(privatechat);
	}

	public void send(String message, String receiver,int ttl) throws IOException {
		String name = client.getName();
		client.sendMessage(message, SEND, name, receiver, ttl);
	}
	public void End() throws IOException {
		client.sendMessage("",END,null,null,2);
		
	}
}
