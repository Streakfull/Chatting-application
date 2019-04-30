import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class PrivateChat extends JPanel implements ActionListener {
	private String receiverName;
	private Controller controller;
	private JPanel messages;
	private JTextField message;
	private JTextField TTL;

	public PrivateChat(String receiverName, Controller controller) {
		super();
		System.out.println(receiverName + "PRIVATECHAT");
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(350, 350));
		this.receiverName = receiverName;
		this.controller = controller;
		JLabel name = new JLabel(receiverName);
		Border titleBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.blue);
		name.setBorder(titleBorder);
		messages = new JPanel();
		messages.setLayout(new BoxLayout(messages, BoxLayout.Y_AXIS));
		Border messageBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.blue);
		messages.setBorder(messageBorder);
		message = new JTextField(20);
		JPanel input = new JPanel();
		input.setLayout(new FlowLayout());
		JButton send = new JButton();
		send.setName(this.receiverName);
		send.setText("Send");
		send.setActionCommand("Send");
		send.setBorderPainted(false);
		send.setContentAreaFilled(false);
		send.addActionListener(this);
		TTL = new JTextField(2);
		TTL.setText("2");
		input.add(message);
		input.add(send);
		input.add(new JLabel("TTL:"));
		input.add(TTL);
		this.add(name, BorderLayout.NORTH);
		this.add(messages, BorderLayout.CENTER);
		this.add(input, BorderLayout.SOUTH);

	}

	public String getReceiverName() {
		return receiverName;
	}

	public void receiveMessage(String message) {
		JLabel receivedMessage = new JLabel(message);
		messages.add(receivedMessage);
		messages.revalidate();
		this.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		String memberName = ((JButton) e.getSource()).getName();
		if (action.equals("Send")) {
			try {
				if (message.getText().equals("Bye")) {
					controller.End();
					System.exit(0);
				} int ttl = 2;
				try {	
				  ttl = Integer.parseInt(TTL.getText());
				}
				catch (Exception inv) {
					ttl = 2;
				}
				controller.send(message.getText(), memberName,ttl);
				JLabel newMessage = new JLabel(message.getText());
				messages.add(newMessage);
				message.setText("");
				messages.revalidate();
				this.revalidate();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

}
