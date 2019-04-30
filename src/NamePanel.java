import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class NamePanel extends JPanel {
	private JTextField name;
	private JLabel errorMessage;

	public NamePanel(Controller controller) {
		super();
		setLayout(new BorderLayout());
		JLabel prompt = new JLabel("Please Enter your name");
		Color black = new Color(0, 0, 0);
		Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, black);
		prompt.setBorder(border);
		this.name = new JTextField(30);
		Button joinButton = new Button("Join", controller);
		errorMessage = new JLabel();
		JPanel content = new JPanel();
		content.setLayout(new GridLayout(2, 1));
		JPanel inputForm = new JPanel();
		inputForm.setLayout(new FlowLayout());
		inputForm.add(prompt);
		inputForm.add(name);
		inputForm.add(joinButton);
		content.add(inputForm);
		errorMessage.setHorizontalAlignment(JLabel.CENTER);
		content.add(errorMessage);
		this.add(content, BorderLayout.PAGE_START);

		setVisible(true);
	}

	public String getName() {
		return this.name.getText();
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage.setText(errorMessage);
	}

}
