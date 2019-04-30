import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChatPanel extends JPanel {
	private JLabel welcome;
	private MembersPanel members;
	private ChatContainer chatContainer;

	public ChatPanel(Controller controller) {
		super();
		this.setLayout(new BorderLayout());
		welcome = new JLabel();
		members = new MembersPanel(controller);
		chatContainer = new ChatContainer();
		this.add(welcome, BorderLayout.PAGE_START);
		this.add(members, BorderLayout.LINE_START);
		this.add(chatContainer,BorderLayout.CENTER);
		this.setVisible(true);
	}

	public void setWelcomeName(String name) {
		welcome.setText("Welcome " + name);
	}

	public void setMembersParent(String memberList) {
		System.out.println(memberList);
		members.setMembers(memberList);
	}

	public ChatContainer getChatContainer() {
		return chatContainer;
	}
	public void setMessage(String message,String receiver) {
		chatContainer.forwardMessage(message, receiver);
	}
}
