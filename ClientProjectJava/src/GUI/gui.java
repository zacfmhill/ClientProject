package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class gui extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */

	public static void main(String args[]){
		JFrame frame = new JFrame("Form Filler");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300,300);
		JButton button = new JButton("Press");
	
		frame.setVisible(true);
	}

}
