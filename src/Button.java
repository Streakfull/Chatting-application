import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

public class Button extends JButton {
	public Button(String name,Controller controller) {
		super(name);
		Color black = new Color(0, 0, 0);
		this.setForeground(black);
		Border Buttonborder = BorderFactory.createLineBorder(black, 2);
		this.setBorder(Buttonborder);
		this.setContentAreaFilled(false);
		this.addActionListener(controller);
	}
}
