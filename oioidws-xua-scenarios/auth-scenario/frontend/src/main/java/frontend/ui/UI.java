package frontend.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UI extends JPanel {
	private static final long serialVersionUID = 1L;
	private String password;
	private JTextArea log;
	private File file;

	public static void createAndShowGUI() {
		JFrame frame = new JFrame("XUA AUT-H Scenario Frontend");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 300);
		frame.add(new UI(), BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	public UI() {
		super(new BorderLayout(0,0)); 

		add(getButtonPanel(), BorderLayout.NORTH);
		add(getLogPanel(), BorderLayout.CENTER);
	}

	public void writeLog(String message) {
		log.append(message + "\n");
		log.setCaretPosition(log.getDocument().getLength());
	}

	public void writeLog(String message, Exception ex) {
		writeLog(message);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		writeLog(sw.toString());
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return file;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}

	private JPanel getButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		JButton keystoreButton = new JButton("Open Keystore");
		keystoreButton.addActionListener(new KeystoreButtonHandler(this));

		JButton runButton = new JButton("Run");
		runButton.addActionListener(new RunButtonHandler(this));

		buttonPanel.add(keystoreButton);
		buttonPanel.add(runButton);

		return buttonPanel;
	}

	private JScrollPane getLogPanel() {
		log = new JTextArea(5, 30);
		log.setEditable(false);

		return new JScrollPane(log);
	}
}