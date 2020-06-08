package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.plaf.OptionPaneUI;



public class preDialog {
	private static JComboBox typeCh;
	private String folderPath = "";
	private String setFilePath;
	private JFrame preDial;
	private String newFolderPath = "";
	private JLabel c1;
	private JLabel c2;
	 public preDialog() throws FileNotFoundException{
		//JDialog for initial PDF forms chooser!
		int h = 0x2713;
		String c = Character.toString((char)h);
		c1 = new JLabel(c);
		c2 = new JLabel (c);
		c1.setForeground(Color.decode("0x008000"));
		c2.setForeground(Color.decode("0x008000"));
		c1.setFont(new Font("Serif", Font.BOLD, 20));
		c2.setFont(new Font("Serif", Font.BOLD, 20));
		c1.setVisible(false);
		c2.setVisible(false);
		preDial = new JFrame("Choose Field Trip Form Type"); 
		
		preDial.setLayout( new FlowLayout() );  
		String[] ops = {"User","Admin"};
		int choice = JOptionPane.showOptionDialog(preDial, "User or Editor?", "Editor Login", 1, 1, null,ops , null);
		if(choice == 1) {
			String passEnter = JOptionPane.showInputDialog("Enter the PIN");
			if(gui.setAllowed(passEnter)) {
				JOptionPane.showMessageDialog(preDial, "Logging In As Editor");
			}
			else {
				JOptionPane.showMessageDialog(preDial, "Logging In As User, PIN incorrect!");
			}
		}
		JLabel title = new JLabel("Please choose the type of form, the location");
		preDial.add(title);
		preDial.add(new JLabel("of the original files, and the new save location"));
		typeCh = new JComboBox();
		typeCh.addItem("General Field Trip");
		typeCh.addItem("Overnight Or Out Of State Field Trip");
		JSeparator sep = new JSeparator();
		preDial.add(typeCh);
		preDial.add(sep);
		JButton bFileSel = new JButton("Folder Location Of Original Forms");
		bFileSel.addActionListener(new selActionListener());
		JButton bNewSel = new JButton("Folder Location To Save New Forms");
		bNewSel.addActionListener(new newActionListener());
		preDial.add(bFileSel);
		preDial.add(c1);
		preDial.add(new JSeparator());
		preDial.add(bNewSel);
		preDial.add(c2);
		preDial.add(new JSeparator());
		JButton bConfirmStart = new JButton("Confirm");
		bConfirmStart.addActionListener(new confirmActionListener());
		preDial.add(bConfirmStart);
		ImageIcon img = new ImageIcon("res/img/file-signature-solid.png");
		
        preDial.setIconImage(img.getImage());
	 	preDial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		preDial.setSize(300,300);
		preDial.setVisible(true);
	}
	 public class confirmActionListener implements ActionListener{
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!folderPath.equals("")&&!newFolderPath.equals("")) {
				if(String.valueOf(typeCh.getSelectedItem()).equals("General Field Trip")) {
					setFilePath = doFileStuff.myPlace().getPath()+"/GenSettings.txt";
				}
				else {
					setFilePath = doFileStuff.myPlace().getPath()+"/OverOutSettings.txt";
				}
				try {
					new gui(setFilePath,folderPath,newFolderPath);
					preDial.setVisible(false);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
				else {
					JOptionPane.showMessageDialog(preDial,"Please select the folder with the forms for your field trip type and the location to save them!");  
				}
			}
			
		}
		public class selActionListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            int option = fileChooser.showOpenDialog(preDial);
	            
	            if(option == JFileChooser.APPROVE_OPTION){
	               File file = fileChooser.getSelectedFile();
	               folderPath = file.getPath();
	               c1.setVisible(true);
	            }else{
	            	JOptionPane.showMessageDialog(preDial,"Invalid Folder!!");  
	            }
			}	
	}
		public class newActionListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            int option = fileChooser.showOpenDialog(preDial);
	            if(option == JFileChooser.APPROVE_OPTION){
	               File file = fileChooser.getSelectedFile();
	               newFolderPath = file.getPath();
	               c2.setVisible(true);
	            }else{
	            	JOptionPane.showMessageDialog(preDial,"Invalid Folder!!");  
	            }
			}	
	}
}
