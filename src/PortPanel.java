import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class PortPanel extends JPanel {
	int port;

	public PortPanel(Controller controller) {
		super();
		setLayout(new FlowLayout());
		JLabel prompt = new JLabel("Please choose a server");
		Color black = new Color(0, 0, 0);
		Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, black);
		prompt.setBorder(border);
		add(prompt);
		Button serverA = new Button("ServerA",controller);
		Button serverB = new Button("ServerB",controller);
		add(serverA);
		add(serverB);
		setVisible(true);
	}

}
