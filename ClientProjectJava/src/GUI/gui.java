package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.*;

public class gui extends JFrame implements ActionListener {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */

	JMenuItem FileAdd,save,preview,help,FieldAdd;
	
	
	
	public gui() {
		 	JFrame frame=new JFrame("Form Filler");
		 	
		 	
//MENU START
	        JMenuBar menuBar=new JMenuBar(); 
	        //file options start
	        JMenu Fmenu=new JMenu("File Options");
	        JMenu fieldEdit=new JMenu("Field Edit");  
	        JMenu Remove = new JMenu("Remove");
	        JMenu Rename = new JMenu("Rename");
	        FileAdd=new JMenuItem("Add File");  
	        FieldAdd=new JMenuItem("Add Field");
	        save=new JMenuItem("Save");
	        preview = new JMenuItem("Preview");
	        help = new JMenuItem("Help");
	        Fmenu.add(FileAdd);
	        Fmenu.addSeparator();
	        Fmenu.add(save);
	        Fmenu.addSeparator();
	        fieldEdit.add(FieldAdd);
	        fieldEdit.addSeparator();
	        fieldEdit.add(Remove);
	        fieldEdit.addSeparator();
	        fieldEdit.add(Rename);  
	        fieldEdit.addSeparator();
	        Fmenu.add(fieldEdit); 
	        Fmenu.addSeparator();
	        Fmenu.add(preview);
	        Fmenu.addSeparator();
	        Fmenu.add(help);
	        Fmenu.addSeparator();
	        //file options end
	        
	        //gui menu start
	        JMenu guiMenu=new JMenu("GUI");
	        JMenuItem item1,item2;
	        item1 =new JMenuItem("Item 1");
	        item2=new JMenuItem("Item 2");
	        guiMenu.add(item1);
	        guiMenu.addSeparator();
	        guiMenu.add(item2);
	        guiMenu.addSeparator();
	        //gui menu end
	        //add action listeners
	        FileAdd.addActionListener(this);  
	        FieldAdd.addActionListener(this);  
	        save.addActionListener(this);    
	        preview.addActionListener(this);
	        help.addActionListener(this);
	        item1.addActionListener(this);
	        item2.addActionListener(this);
	        //add menus to bar
	        menuBar.add(Fmenu);
	        menuBar.add(guiMenu);
//MENU END
	        frame.setJMenuBar(menuBar); 
	        frame.setLayout(null); 
		    frame.setSize(400,400);  
		    frame.setVisible(true); 
		
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()== FileAdd){    
		    JFileChooser fileChooser=new JFileChooser();    
		    int i=fileChooser.showOpenDialog(this);    
		    if(i==JFileChooser.APPROVE_OPTION){    
		        File file =fileChooser.getSelectedFile();    
		        String filepath= file.getPath();    
		        System.out.println(filepath);
		    }
		}    
	}
	
	
	
	public static void main(String[] args) {  
	     new gui();
	} 
	 

}
