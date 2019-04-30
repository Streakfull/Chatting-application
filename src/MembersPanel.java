import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class MembersPanel extends JPanel {
	private ArrayList<String> members;
	private Controller controller;

	public MembersPanel(Controller controller) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.controller = controller;
		JLabel memberTitle = new JLabel("Members");
		Border titleBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.blue);
		memberTitle.setBorder(titleBorder);
		Button refresh = new Button("refresh", controller);
		this.add(memberTitle);
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		this.add(refresh);
		this.members = new ArrayList<String>();
		Color black = new Color(0, 0, 0);
		Border border = BorderFactory.createMatteBorder(0, 0, 0, 1, black);
		this.setBorder(border);

	}

	public void setMembers(String Members) {
		System.out.println(Members);
		ArrayList<String> newMembers = new ArrayList<String>();
		String[] receivedMembers = Members.split(",");
		for (int i = 0; i < receivedMembers.length; i++) {
			if (!this.members.contains(receivedMembers[i])) {
				newMembers.add(receivedMembers[i]);
			}
		}
		;
		ArrayList<String> removed = new ArrayList<String>();
		members.forEach(member -> {
			for (int i = 0; i < receivedMembers.length; i++) {
				if (receivedMembers[i].equals(member))
					return;
			}
			removed.add(member);

		});
		Component[] comps = this.getComponents();
		for (int i = 0; i < comps.length; i++) {
			if(comps[i] instanceof JButton) {
				String buttonText = ((JButton)comps[i]).getText();
				if(removed.contains(buttonText))
				 this.remove(comps[i]);
			}
		}
		removed.forEach(member -> members.remove(member));
		newMembers.forEach(member -> {
			members.add(member);
			JButton memberJ = new JButton();
			memberJ.setText(member);
			memberJ.setActionCommand("openChat");
			memberJ.setBorderPainted(false);
			memberJ.setContentAreaFilled(false);
			memberJ.addActionListener(controller);
			PrivateChat privatechat = new PrivateChat(member, controller);
			controller.addChat(privatechat);
			this.add(memberJ);

		});
		this.revalidate();
		this.repaint();
	}

}
