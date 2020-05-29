package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.*;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;

public class gui extends JFrame implements ActionListener {
	private int width=400;
	private int height = 600;
	private int horGap,verGap = 10;
	private JPanel contentPane;
	private JMenuItem FileAdd,save,preview,help,FieldAdd;
	private Map<Integer, String> fieldMap = new TreeMap<Integer,String>();
	private Map<Integer, ArrayList<String>> inputToFileMap = new TreeMap<Integer,ArrayList<String>>();
	private Map<Integer, Component> idToCompMap = new TreeMap<Integer, Component>();
	private File settFile = new File("res/settings.txt");
	public gui() throws FileNotFoundException {
//Initial Settings Parse
			getSettIni();
			
//CREATE FRAME
		 	JFrame frame=new JFrame("Form Filler");

//CREATE MENU
	        JMenuBar menuBar=new JMenuBar(); 
	        menuBar.add(Fmenu());
	        menuBar.add(Gmenu());
	        
//ALL FIELD EDITS
	        JPanel listView = new JPanel();
	        listView.setLayout(new BoxLayout(listView, BoxLayout.Y_AXIS)); 
	      
     //GENERATING EACH FIELD
for(Map.Entry<Integer,String> m:fieldMap.entrySet()){  
	        Component panelIn= getComp(m.getValue());
	        //Field Label
	        getFieldSettById(m.getKey());
	        //adds each component to the map mapping id's to specific components
	        idToCompMap.put(new Integer(m.getKey()), panelIn);
	        
	        
	        JTextArea fieldLabel =new JTextArea(); 
	        	fieldLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        	fieldLabel.setWrapStyleWord(true);
	        	fieldLabel.setLineWrap(true);
	        	fieldLabel.setEditable(false);
	        	fieldLabel.setText(getNick(m.getValue()));
	        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fieldLabel, panelIn) {
				private static final long serialVersionUID = 1L;
				private final int location = 80;
	            {
	                setDividerLocation( location );
	            }
	            @Override
	            public int getDividerLocation() {
	                return location ;
	            }
	            @Override
	            public int getLastDividerLocation() {
	                return location ;
	            }
	        };
	        	splitPane.setPreferredSize(new Dimension(300, 80));
	        	splitPane.setMaximumSize(new Dimension(350, 70));
	        	splitPane.setEnabled(false);
	        	splitPane.setDividerSize(0);
	        listView.add(splitPane);
	        
}
//Purely Testing, to make sure correct objects assigned!
	        for(Map.Entry<Integer,Component> fie:idToCompMap.entrySet()){  
	        	System.out.println(fie.getKey() + " ::: "+fie.getValue());
	        }
	        
//MAKE IT SCROLLABLE BY PUTTING EVERYTHING IN SCROLL PANE	        
	        JScrollPane scrollPane = new JScrollPane(listView);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
	        
	        
//FRAME FINAL SETUP
	        ImageIcon img = new ImageIcon("res/file-signature-solid.png");
	        frame.setIconImage(img.getImage());
		 	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.add(scrollPane);
	        frame.setJMenuBar(menuBar); 
		    frame.setSize(width,height);  
		    frame.setResizable(false);
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
	
	public static void main(String[] args) throws FileNotFoundException {  
		new gui();
	} 
	
	public JMenu Fmenu() {
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
        return Fmenu;
	}
	
	public JMenu Gmenu() {
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
        return guiMenu;
	}
	
	public void getSettIni() throws FileNotFoundException{
		Scanner sc = new Scanner(settFile); 
		while(sc.hasNextLine()) {
			String curr = sc.nextLine();
			//if it's an ID tag
			try {
			//Empty Space Syntax
				if(curr.contentEquals("")) {
					System.out.println();
				}
			//comment syntax
				else if(curr.substring(0,2).contentEquals("??")) {
					System.out.println("Comment: " + curr.substring(2));
				}
			//tag syntax
				else if(curr.substring(0,5).equals("%$#$%")) {
					int ID = Integer.parseInt(curr.substring(5,curr.indexOf("%$#$%",5)));
					System.out.println(ID+ " "+ curr);
					fieldMap.put(ID, curr);
				}
			}
			catch(Exception e) {
				System.out.println("Malformed ID in File Settings -- " + curr);
			}	
		}
	}
	public void getFieldSettById(int progID) throws FileNotFoundException {
		ArrayList<String> progIDStrings = new ArrayList<String>();
		Scanner sc = new Scanner(settFile); 
		while(sc.hasNextLine()) {
			String curr = sc.nextLine();
			try {
				if(curr.length()>5 && curr.substring(0,5).equals("%$@$%")) {
					int currID = Integer.parseInt(curr.substring(5,curr.indexOf("%$@$%",5)));
					if(currID == progID) {
						progIDStrings.add(curr);
					}
				}
			}
			catch (Exception e) {
				System.out.println("Malformed data in File Settings --> " + curr);
			}
			inputToFileMap.put(new Integer(progID), progIDStrings);
		}
	}
	
	public Component getComp(String str) {
		int idIndex = str.indexOf("%$#$%",5) +5;
		String type = str.substring(idIndex,str.indexOf("%$#$%",idIndex));
		//because combo box takes it's inner contents as part of the type argument, 
		//needs to be handled specially!
		if(type.contains("COMBO_BOX")) {
			return comboBox(type);
		}
		//because check box takes it's inner contents as part of the type argument, 
		//needs to be handled specially!
		else if(type.contains("CHECK_BOX")) {
			return checkBoxCreate(type);
		}
		switch(type) {
		case "TEXT":
			return textAreaCreate();
		case "YN_BOX":
			return YNboxCreate();
		case "DATE":
			return dateCreate();
		default:
			return new JLabel("Error On Current ID's Type");
		}	
	}
	
	public Component checkBoxCreate(String type) {
		String text = "";
		if(type.length()>10) {
			text = type.substring(10,type.length());
		}
		JCheckBox checkBox = new JCheckBox(text);
		return checkBox;
	}
	
	public Component YNboxCreate() {
		JPanel box = new JPanel();
		JCheckBox checkBox1 = new JCheckBox("Yes");  
	    JCheckBox checkBox2 = new JCheckBox("No");
	    box.add(checkBox1);
	    box.add(checkBox2);
	    return box;
	}
	public Component textAreaCreate() {
		JTextArea textArea = new JTextArea();
		textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	textArea.setWrapStyleWord(true);
    	textArea.setLineWrap(true);
    	textArea.setRows(5);
		return textArea;
	}
	
	public Component comboBox(String typeArg) {
		JPanel box = new JPanel();
		JComboBox comboBox= new JComboBox();
		String argString= typeArg.substring(10,typeArg.length()-1);
		String[] args = argString.split(",");
		for(String arg:args) {
			comboBox.addItem(arg);
		}
	    box.add(comboBox);
	    return box;
	}
	
	public Component dateCreate() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS)); 
		Date today = new Date();
		SpinnerModel  valuesM = new SpinnerNumberModel(1,1,12,1);
		SpinnerModel  valuesD = new SpinnerNumberModel(1,1,31,1);
		SpinnerModel  valuesY = new SpinnerNumberModel(today.getYear()+1900,1900,4000,1);
		JPanel b1 = new JPanel();
		JSpinner spinnerM = new JSpinner(valuesM);
		spinnerM.setMaximumSize(new Dimension(50,20)); 
		spinnerM.setAlignmentX(LEFT_ALIGNMENT);
		b1.setLayout(new BoxLayout(b1, BoxLayout.Y_AXIS));
		b1.add(new JLabel("Month"));
		b1.add(spinnerM);
		JPanel b2 = new JPanel();
		JSpinner spinnerD = new JSpinner(valuesD);
		spinnerD.setMaximumSize(new Dimension(50,20)); 
		spinnerD.setAlignmentX(LEFT_ALIGNMENT);
		b2.setLayout(new BoxLayout(b2, BoxLayout.Y_AXIS));
		b2.add(new JLabel("Day"));
		b2.add(spinnerD);
		JPanel b3 = new JPanel();
		JSpinner spinnerY = new JSpinner(valuesY);
		spinnerY.setMaximumSize(new Dimension(50,20)); 
		b3.setLayout(new BoxLayout(b3, BoxLayout.Y_AXIS));
		JLabel y = new JLabel("Year");
		spinnerY.setAlignmentX(LEFT_ALIGNMENT);
		b3.add(y);
		//b3.add(new JLabel("Year"));
		b3.add(spinnerY);
		panel.add(b1);
		panel.add(b2);
		panel.add(b3);
		return panel;
	}
	public String getNick(String text) throws FileNotFoundException {
			try {
				int indexStart = text.indexOf("%$#$%",text.indexOf("%$#$%",5)+5)+5;
				int indexEnd = text.indexOf("%$#$%",indexStart);
				return text.substring(indexStart,indexEnd);
			}
			catch(Exception E) {
				System.out.println("Malformed data in Settings File --> "+text);
			}
		return "Malformed Input String Led to Nick Error";
	}
	
}
