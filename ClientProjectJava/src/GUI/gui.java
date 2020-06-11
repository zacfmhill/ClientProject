package GUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import javax.swing.*;
//import fileEdit.GeneratorPDF;
import fileEdit.PDFgetCombos;
import fileEdit.Receiver;

@SuppressWarnings("serial")
public class gui extends JFrame implements ActionListener {
	private String folderPath = "";
	private JPanel panel;
	
	private static JFrame frame;
	private int width=800;
	private int height = 600;
	private JMenuItem FileAdd,save,preview,FieldAdd,Remove,Rename,Default;
	private ArrayList<File> fileInFolder = new ArrayList<File>();
	private Map<Integer, String> fieldMap = new TreeMap<Integer,String>();
	private Map<Integer, ArrayList<String>> inputToFileMap = new TreeMap<Integer,ArrayList<String>>();
	private Map<Integer, Component> idToCompMap = new TreeMap<Integer, Component>();
	private String setFilePath;
	private File settFile;
	private JFrame dAdd;
	//private JButton bGen;
	private JTextArea placing;
	private JTextArea fileName;
	private String currFieldName = "default name";
	private String actingType = "";
	private String actingID = "";
	@SuppressWarnings("rawtypes")
	private JComboBox typeCh;
	private JFrame dRem;
	private JPanel panelR;
	public static String filePathToNew;
	private static boolean allowed = false;
	private static String pass = "goggles";

	
	
	public boolean getExtension(String filename) {
		String ext = filename.substring(filename.lastIndexOf(".")+1);
	    if(ext.equals("pdf")||ext.equals("xlsx")||ext.equals("docx")||ext.equals("doc")) {
	    	return true;
	    }
	    return false;
	}
	public static boolean setAllowed(String passEnter) {
		if(passEnter.equals(pass)) {
			allowed = true;
			return true;
		}
		else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public gui(String filePath,String folder,String newPath) throws FileNotFoundException {
		filePathToNew = newPath;
		folderPath = folder;
		setFilePath = filePath;
		try {
			 File dir = new File(folderPath);
			  File[] directoryListing = dir.listFiles();
			  if (directoryListing != null) {
			    for (File child : directoryListing) {
			    	if(getExtension(child.getPath())) {
			    	fileInFolder.add(child);
					File copy = null;
					try {	
						String fileName = child.getName();
						copy = new File(newPath+ "\\" +fileName);
						Files.copy(child.toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
					}
					catch (Exception e) {
						e.printStackTrace();
						
					}
			    	}
			    }
			  }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
			
			settFile = new File(setFilePath);
//Initial Settings Parse
			getSettIni();
			
//CREATE FRAME
		 	frame =new JFrame("Form Filler");

//CREATE MENU
		 	
	        JMenuBar menuBar=new JMenuBar(); 
	        menuBar.add(Fmenu());
	        
	        
	        
//ALL FIELD EDITS
	        JPanel listView = new JPanel();
	        BoxLayout b = new BoxLayout(listView, BoxLayout.Y_AXIS);
	        listView.setLayout(b); 
	        
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
	        	if(m.getKey() == 0) {
	        		splitPane.setVisible(false);
	        	}
	        	splitPane.setPreferredSize(new Dimension(750, 80));
	        	splitPane.setMaximumSize(new Dimension(780, 70));
	        	splitPane.setEnabled(false);
	        	splitPane.setDividerSize(0);
	        	listView.add(splitPane);
	        
}
			JButton saveButton = new JButton("Apply & Save");
			saveButton.addActionListener(new saveActionListener());
			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), saveButton){
				private static final long serialVersionUID = 1L;
				private final int location = 0;
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
	        splitPane.setEnabled(false);
	        splitPane.setMinimumSize(new Dimension(780,50));
	        splitPane.setPreferredSize(new Dimension(780,55));
	        splitPane.setMaximumSize(new Dimension(785,60));
        	splitPane.setDividerSize(0);

			listView.add(splitPane);
			
			
	        
//MAKE IT SCROLLABLE BY PUTTING EVERYTHING IN SCROLL PANE	        
	        JScrollPane scrollPane = new JScrollPane(listView);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
	        scrollPane.getVerticalScrollBar().setUnitIncrement(25);
	        
	        
//FRAME FINAL SETUP
	        ImageIcon img = new ImageIcon("res/img/file-signature-solid.png");
	        frame.setIconImage(img.getImage());
		 	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.add(scrollPane);
	        frame.setJMenuBar(menuBar); 
		    frame.setSize(width,height);  
		    frame.setResizable(false);
		    frame.setVisible(true); 
		    JLabel message = new JLabel("<html>\r\n" + 
		    		"<body>\r\n" + 
		    		"<h1 style= \" color: red; text-align: center;\"> Please note,<br>this program does not fill out every field in forms,<br>only common repeating ones!!<br>Enter the rest of the form data after using this program.</h1>\r\n" + 
		    		"</body>\r\n" + 
		    		"</html>");
		    JOptionPane.showMessageDialog(null, message,"Alert",JOptionPane.PLAIN_MESSAGE);
	}
	
	
//JFrame for Remove Field
private void dRemMake() {
	
    panelR = new JPanel();
    panelR.setLayout(new BoxLayout(panelR, BoxLayout.Y_AXIS));
    dRem = new JFrame("Remove Field");
    BoxLayout bR = new BoxLayout(dRem.getContentPane(), BoxLayout.Y_AXIS);
    dRem.getContentPane().setLayout(bR); 
    @SuppressWarnings("rawtypes")
	JComboBox box = getComboItems();
    panelR.add(box);
    JButton del = new JButton("Delete");
    del.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(0 == JOptionPane.showConfirmDialog(dRem, "Confirm Deleting "  + box.getSelectedItem().toString())) {
			deleteMenCom(box.getSelectedItem().toString());
			dRem.setVisible(false);
           	frame.setVisible(false);
           	try {
				new gui(setFilePath,folderPath,filePathToNew);
				fileInFolder = new ArrayList<File>();
				fieldMap = new TreeMap<Integer,String>();
				inputToFileMap = new TreeMap<Integer,ArrayList<String>>();
				idToCompMap = new TreeMap<Integer, Component>();
			} 
           	catch (FileNotFoundException e1) {
           		//this should never happen, if it does it means the user has somehow deleted the settings file after clicking submit.
				e1.printStackTrace();
			}
			}
			
		}
    });
    del.setAlignmentX(CENTER_ALIGNMENT);
    panelR.add(del);
    dRem.getContentPane().add(panelR);
    dRem.setAlwaysOnTop(true);
    dRem.setSize(300,200);
    dRem.setVisible(true);
	
}
private void deleteMenCom(String comboOption) {
	try {
	String idText = comboOption.substring(comboOption.lastIndexOf(":")+2);
	int id = Integer.parseInt(idText);
	fieldMap.remove(id);
	inputToFileMap.remove(id);
	idToCompMap.remove(id);
	File tempFile = new File(setFilePath.substring(0,setFilePath.lastIndexOf("/")+1)+"tempFile.txt");
	BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
	String line = null;
	Scanner sc = new Scanner(settFile); 
	while(sc.hasNextLine()){
		line = sc.nextLine();
		if(!line.contains("%$#$%"+id+"%$#$%")&& !line.contains("%$@$%"+id+"%$@$%")) {
			writer.write(line + System.getProperty("line.separator"));
		}
	}
	sc.close();
	writer.close();
	Files.move(tempFile.toPath(), settFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	tempFile.delete();
	} catch (Exception e) {
		e.printStackTrace();
	}
}
@SuppressWarnings({ "unchecked", "rawtypes" })
private JComboBox getComboItems() {
	@SuppressWarnings("rawtypes")
	JComboBox ret = new JComboBox();
	for(Map.Entry<Integer,String> m:fieldMap.entrySet()){  
		try {
			String nick = getNick(m.getValue());
			ret.addItem(nick + " : "+m.getKey());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	return ret;
}
	
	
	
//JFrame for Add Field
@SuppressWarnings({ "unchecked", "rawtypes" })
private void dAddMake() {
	 		currFieldName = "Default Field Name";
		    panel = new JPanel();
		    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		    dAdd = new JFrame("Add Field");
		    BoxLayout b2 = new BoxLayout(dAdd.getContentPane(), BoxLayout.Y_AXIS);
		    dAdd.getContentPane().setLayout(b2); 
		    JPanel panelSel = new JPanel();
		    panelSel.setLayout(new FlowLayout());
		    JLabel selLabel = new JLabel("Select type of field");
		    typeCh = new JComboBox();
		    String[] types = {"--choose one--",".docx-Table",".docx-Insert",".docx-Replace",".xlsx-Edit Cell","pdf-Check Box","pdf-Combo Box","pdf-Date","pdf-Text Box"};
		    for(String str: types) typeCh.addItem(str);
		    panelSel.add(selLabel);
		    panelSel.add(typeCh);
		    panelSel.setMinimumSize(new Dimension(500,50));
		    panelSel.setPreferredSize(new Dimension(505,53));
		    panelSel.setMaximumSize(new Dimension(510,55));
		    panel.add(panelSel);
		    JButton addFieldName = new JButton("Set Field Name");
		    addFieldName.addActionListener(new ActionListener()  {  
	            public void actionPerformed( ActionEvent e )  
	            {
	            	currFieldName = JOptionPane.showInputDialog("Enter Field Name:");
	            }  
	            	});
		    panel.add(new JLabel("Set the field name below. If non is set, default will be used."));
		    panel.add(addFieldName);
		    typeCh.addActionListener(new ActionListener()  {  
	            @SuppressWarnings("deprecation")
				public void actionPerformed( ActionEvent e )  
	            {  
	            	panelSel.setVisible(false);
	            	JPanel lb = new JPanel();
	            	lb.add(new JLabel("Enter Text Below To Enter Into All Fields"));
	            	panel.add(lb);
	            	JPanel panelSetAll = new JPanel();
	            	panelSetAll.setLayout(new FlowLayout());
	            	String placingIn = "";
	            	JTextArea enterAll = new JTextArea("      ");
	            	enterAll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	            	JButton buttonAll = new JButton("Apply To All");
	            	buttonAll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	            	buttonAll.addActionListener(new ActionListener()  {  
	       	            public void actionPerformed( ActionEvent e )  
	       	            {  
	       	            	for(Component panelIt:panel.getComponents()) {
	       	            		//if it's a panel
	       	            		if(panelIt instanceof JPanel) {
	       	            			JPanel pan = (JPanel)panelIt;
	       	            			//get the panels components
	       	            			Component[] box = pan.getComponents();
	       	            			if(box[0] instanceof JCheckBox) {
	       	            					if(box[1] instanceof JSplitPane) {
	       	            						((JTextArea)((JSplitPane)box[1]).getComponents()[1]).setText((enterAll.getText()).strip());
	       	            					}
	       	            				}
	       	            			}
	       	            		}
	       	            	}
	            	});
	            	JSplitPane splpan = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, enterAll, buttonAll);
	            	splpan.disable();
	            	splpan.setDividerSize(0);
	            	panelSetAll.add(splpan);
	            	panel.add(panelSetAll);
	            	
	               if(typeCh.getSelectedItem().toString().contains("pdf")) {
	            	   	//genLabel.setVisible(true);
	            	  // 	bGen.setVisible(true);
	            	  // 	panel.add(genLabel);
	       		    	//panel.add(bGen);
	       		    	for(File file: fileInFolder) {
	       		    		String name = file.getName();
	       		    		String ext = name.substring(name.indexOf(".")+1);
	       		    		if(ext.equals("pdf")) {
	       		    			JPanel panelField = new JPanel();
	       		    			panelField.setLayout(new FlowLayout());
	       		    			JCheckBox box = new JCheckBox("Use?");
	       		    			panelField.add(box);
	       		    			placing = new JTextArea(placingIn);
	       		    			fileName = new JTextArea(name);
	       		    			placing.setWrapStyleWord(true);
	       		    			placing.setLineWrap(true);
	       		    			fileName.setWrapStyleWord(true);
	       		    			fileName.setLineWrap(true);
	       		    			JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileName, placing);
	       		    			panelField.add(split);
	       		    			panel.add(panelField);
	       		    			if(typeCh.getSelectedItem().toString().equals("pdf-Date")) {
	    	       		    		placing.setText("Enter the three field ID's from the pdf of the date text field, seperated by dashes. NO SPACES Ex. 3-4-5");
	    	       		    	}
	    	       		    	else if(typeCh.getSelectedItem().toString().equals("pdf-Check Box")) {
	    	       		    		placing.setText("Enter the id of the checkbox, a dash, and then for each checkbox the title you want displayed each seperated by a dash. Ex: 5-title 1-title 2-title 3");
	    	       		    	}
	    	       		    	else if(typeCh.getSelectedItem().toString().equals("pdf-Combo Box")) {
	    	       		    		placing.setText("Enter the id of the combo box (most likely a name not #), the program will auto fill the options for you if you enter the ID it correctly. Use the tool above if you need help figuring out the ID.");
	    	       		    	}
	    	       		    	else if(typeCh.getSelectedItem().toString().equals("pdf-Text Box")) {
	    	       		    		placing.setText("just enter the id of the text field");
	    	       		    	}
	       		    		}
	       		    	}
	               }
	               else if(typeCh.getSelectedItem().toString().contains("docx")) {
	            	  // genLabel.setVisible(false);
	            	  // bGen.setVisible(false);
	            	   for(File file: fileInFolder) {
		       		    	String name = file.getName();
		       		    	String ext = name.substring(name.indexOf(".")+1);
		       		    	if(ext.equals("docx")) {
		       		    	JPanel panelField = new JPanel();
		       			    JCheckBox box = new JCheckBox("Use?");
		       			    panelField.add(box);
		       		        placing = new JTextArea(placingIn);
		       			    fileName = new JTextArea(name);
		       			    placing.setWrapStyleWord(true);
		       	        	placing.setLineWrap(true);
		       	        	fileName.setWrapStyleWord(true);
		       	        	fileName.setLineWrap(true);
		       			    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileName, placing);
		       			    panelField.add(split);
		       			    panel.add(panelField);
		       			    if(typeCh.getSelectedItem().toString().equals(".docx-Table")) {
		       		    		placing.setText("Enter the placement of the table, a dash, the row, a dash, and the column. ex. the second table in the document on row 2, column 3: 2-2-3");
		       		    	}
		            	   	else if(typeCh.getSelectedItem().toString().equals(".docx-Insert")) {
		            	   		placing.setText("Enter the text to insert after, spacing and capitilization are important!");
		            	   	}
		            	   	else if(typeCh.getSelectedItem().toString().equals(".docx-Replace")) {
		            	   		placing.setText("Enter the text to replace, spacing and capitilization are important!");
		            	   	}
		       		    	}
		       		    }
	               }
	               else if(typeCh.getSelectedItem().toString().contains("xlsx")) {
	            	 //  genLabel.setVisible(false);
	            	 //  bGen.setVisible(false);
	            	   for(File file: fileInFolder) {
	       		    		String name = file.getName();
	       		    		String ext = name.substring(name.indexOf(".")+1);
	       		    		if(ext.equals("xlsx")) {
	       		    			JPanel panelField = new JPanel();
	       		    			//panelField.setLayout(new FlowLayout());
	       		    			JCheckBox box = new JCheckBox("Use?");
	       		    			panelField.add(box);
	       		    			placing = new JTextArea(placingIn);
	       		    			fileName = new JTextArea(name);
	       		    			placing.setWrapStyleWord(true);
	       		    			placing.setLineWrap(true);
	       		    			placing.setText("enter the row number, a dash, and then column number. ex:3-5 for row 3, col 5");
	       		    			fileName.setWrapStyleWord(true);
	       		    			fileName.setLineWrap(true);
	       		    			JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileName, placing);
	       		    			panelField.add(split);
	       		    			panelField.setMinimumSize(new Dimension(500,50));
	       		    			panel.add(panelField);
	       		    		}
	       		    	}
	            	   	
	               }
	               SwingUtilities.updateComponentTreeUI(dAdd);
           	   		JButton bAdd = new JButton ("ADD");
      		    	//bAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
      		    	bAdd.addActionListener (new addingAction());
      		    	bAdd.setMinimumSize(new Dimension(400,50));
      		    	bAdd.setPreferredSize(new Dimension(405,55));
      		    	bAdd.setMaximumSize(new Dimension(410,60));
      		    	panel.add(bAdd); 
	            }  
	        });
		    
		    
		  //  genLabel = new JLabel("Generate a PDF from a file that has every field filled in with its ID");
		  //  genLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		 //   genLabel.setVisible(false);
		 //   bGen = new JButton("Generate Field Label PDF");
		 //   bGen.setVisible(false);
		   // bGen.setAlignmentX(Component.CENTER_ALIGNMENT);
		  //  bGen.addActionListener(new genActionListener());
		    
		    

	        JScrollPane scrollAdd = new JScrollPane(panel);
	        scrollAdd.getVerticalScrollBar().setUnitIncrement(300);

	        scrollAdd.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
	        scrollAdd.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        
		    dAdd.getContentPane().add(scrollAdd);
		  //  dAdd.setAlwaysOnTop(true);
	        dAdd.setSize(650,600);
	        dAdd.setVisible(true);
	
	}
 	

 	
 	
 	public class addingAction implements ActionListener {

 		@Override
		public void actionPerformed(ActionEvent e) {
			//iterate over everything
			int id = handleID();
			String selection = typeCh.getSelectedItem().toString();
			switch(selection){
//pdf cases
				case "pdf-Text Box":
					actingType = "TEXT";
   		    		actingID= "T-";
   		    		break;
				case "pdf-Combo Box":
					for(Component panelIt:panel.getComponents()) {
		           		//if it's a panel
		           		if(panelIt instanceof JPanel) {
		           			JPanel pan = (JPanel)panelIt;
		           			//get the panels components
		           			Component[] box = pan.getComponents();
		           			//if the first one is a check box
		           			if(box[0] instanceof JCheckBox) {
		           				//if it is checked
		           				if(((JCheckBox)box[0]).isSelected()) {
		           					if(box[1] instanceof JSplitPane) {
		           						Component[] splPaneComp = ((JSplitPane)box[1]).getComponents();
		           						String fileName = ((JTextArea)splPaneComp[0]).getText();
		           						String fileID = ((JTextArea)splPaneComp[1]).getText();
		           						for(File file:fileInFolder) {
		        			            	if(file.getName().equals(fileName)) {
		        			            		actingType = "COMBO_BOX"+PDFgetCombos.getCombo(fileID.strip(), file.getPath());
		        			            	}
		        			            }
		           		    		actingID = "L-";//+placing.getText().strip();
		           		    		break;
		           					}
		           				}
		           			}
		           		}
					}
					break;
				case "pdf-Check Box":
					for(Component panelIt:panel.getComponents()) {
		           		//if it's a panel
		           		if(panelIt instanceof JPanel) {
		           			JPanel pan = (JPanel)panelIt;
		           			//get the panels components
		           			Component[] box = pan.getComponents();
		           			//if the first one is a check box
		           			if(box[0] instanceof JCheckBox) {
		           				//if it is checked
		           				if(((JCheckBox)box[0]).isSelected()) {
		           					if(box[1] instanceof JSplitPane) {
		           						Component[] splPaneComp = ((JSplitPane)box[1]).getComponents();
		           						String fileName = ((JTextArea)splPaneComp[0]).getText();
		           						String fileID = ((JTextArea)splPaneComp[1]).getText();
		           						for(File file:fileInFolder) {
		        			            	if(file.getName().equals(fileName)) {
		        			            		actingType = "CHECK_BOX-"+ (fileID.substring(fileID.indexOf("-")+1)).strip();
		        			            	}
		        			            }
		           		    		actingID = "C-";
		           		    		break;
		           					}
		           				}
		           			}
		           		}
					}
					break;
				case "pdf-Date":
					actingID = "D-";
					actingType = "DATE";
					break;
//xlsx cases
				case ".xlsx-Edit Cell":
					actingType = "TEXT";
					actingID = "C-";
					break;
//docx cases
				case ".docx-Table":
					actingType = "TEXT";
					actingID = "T-";
					break;
				case ".docx-Insert":
					actingType = "TEXT";
					actingID = "I-";
					break;
				case ".docx-Replace":
					actingType = "TEXT";
					actingID = "R-";
					break;
				default:
					actingID = "error";
					actingType = "error";
			}
	        String nick = currFieldName;
	        String writingHash = "%$#$%"+id+"%$#$%"+actingType+"%$#$%"+nick+"%$#$%";
	        try {
	        	FileWriter writer = new FileWriter(settFile.getPath(), true);
	        	BufferedWriter bufferedWriter = new BufferedWriter(writer);
	        	bufferedWriter.newLine();
	        	bufferedWriter.write(writingHash);
           	for(Component panelIt:panel.getComponents()) {
           		//if it's a panel
           		if(panelIt instanceof JPanel) {
           			JPanel pan = (JPanel)panelIt;
           			//get the panels components
           			Component[] box = pan.getComponents();
           			//if the first one is a check box
           			if(box[0] instanceof JCheckBox) {
           				//if it is checked
           				if(((JCheckBox)box[0]).isSelected()) {
           					if(box[1] instanceof JSplitPane) {
           						Component[] splPaneComp = ((JSplitPane)box[1]).getComponents();
           						String fileName = ((JTextArea)splPaneComp[0]).getText();
           						String fileID = ((JTextArea)splPaneComp[1]).getText();
           						
           				            String filePath = "";
           				            for(File file:fileInFolder) {
           				            	if(file.getName().equals(fileName)) {
           				            		filePath = file.getPath();
           				            	}
           				            }
           				            String writingAt = "%$@$%"+id+"%$@$%"+filePath+"%$@$%"+actingID+fileID+"%$@$%";
           				            //check boxes are slightly inefficient in the way they are written through the GUI, need to be specially handled in this case.
           				            if(selection != null && selection.equals("pdf-Check Box")) writingAt = "%$@$%"+id+"%$@$%"+filePath+"%$@$%"+actingID+(fileID.strip()).substring(0,fileID.indexOf("-"))+"%$@$%";
           				            bufferedWriter.newLine();
           				            bufferedWriter.write(writingAt);
           				            bufferedWriter.newLine();
           				        
           					}
           				}
           			}
           			
           		}
           	}
           	bufferedWriter.close();
	        writer.close();
	        } catch (IOException error) {
		            error.printStackTrace();
		            
		        }
	        
	        //essentially deletes the two frames and redraws them so they show the now updated changes.
           	dAdd.setVisible(false);
           	frame.setVisible(false);
           	try {
           		new gui(setFilePath,folderPath,filePathToNew);
				fileInFolder = new ArrayList<File>();
				fieldMap = new TreeMap<Integer,String>();
				inputToFileMap = new TreeMap<Integer,ArrayList<String>>();
				idToCompMap = new TreeMap<Integer, Component>();
			} 
           	catch (FileNotFoundException e1) {
           		//this should never happen, if it does it means the user has somehow deleted the settings file after clicking submit.
				e1.printStackTrace();
			}
	}
 }
 	
 	
 	
 	
 	public static int handleID(){

 		int id = 0;
 		try {
 			FileReader fr = new FileReader(new File(doFileStuff.myPlace(),"IDuserProfiling.txt"));
 			BufferedReader br = new BufferedReader(fr);
 	 		String data = br.readLine();
 	 		id  = Integer.parseInt(data);
 	 		br.close();
 		    FileWriter writer = new FileWriter(doFileStuff.myPlace().getPath()+"/IDuserProfiling.txt");
 		    writer.write((id+1)+"");
 		    writer.close();
 		    } 
 		catch (Exception e) {
 		      e.printStackTrace();
 		    }
		return id;
 	}
	
//	public class genActionListener implements ActionListener{
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				JFileChooser fileChooser=new JFileChooser();
//				FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF", "pdf");
//				fileChooser.setFileFilter(filter);
//				fileChooser.addChoosableFileFilter(filter);
//				fileChooser.setCurrentDirectory(new File(folderPath));
//			    int i=fileChooser.showOpenDialog(dAdd);    
//			    if(i==JFileChooser.APPROVE_OPTION){    
//			        File file =fileChooser.getSelectedFile();    
//			        String filepath= file.getPath();   
//			        ArrayList<String> others;
//			        others = GeneratorPDF.generate(filepath);
//			        others.remove(1);
//			        JTextArea jt = new JTextArea();
//			        for(String str: others) {
//			        	jt.setText(jt.getText()+str+"\n");
//			        }
//			        JOptionPane.showMessageDialog(dAdd,jt);
//			    }
//			}
//	}



	
	
	
	
	
	
	
	
//Save Button Action Handling
	class saveActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(0==JOptionPane.showConfirmDialog(frame, "Are You Sure You Want To Apply Your Changes?","Confirm Applying?",0)) {
			  for(@SuppressWarnings("rawtypes") Map.Entry m:idToCompMap.entrySet()){  
	        	 Component currComp = (Component) m.getValue();
	        	 //if current item is a textArea
	        	 if(currComp instanceof JTextArea) {
	        		 JTextArea curr = (JTextArea) currComp;
	        		 String content = curr.getText();
	        		 Receiver.send(content,inputToFileMap.get(m.getKey()));
	        	 }
	        	
	        	 //if current item is a Panel
	        	 else {
	        		 JPanel currPan = (JPanel) currComp;
	        		 Component cFirs = currPan.getComponents()[0];
	        		 
	        		 //if combo box
	        		 if(cFirs instanceof JComboBox) {
	        			 @SuppressWarnings("rawtypes")
						JComboBox curr = (JComboBox)cFirs;
	        			 String content = String.valueOf(curr.getSelectedItem());
	        			 Receiver.send(content,inputToFileMap.get(m.getKey()));
	        			 
	        		 }
	        		 //if check box
	        		 else if(cFirs instanceof JCheckBox) {
	        			 String[] options = {"Yes","No","a","b","c","d","e","f"};
	        			 String content= "";
	        			 int i = 0;
	        			 for(Component currCom:currPan.getComponents()) {
	        				 JCheckBox currCh = (JCheckBox)currCom;
	        				 if(currCh.isSelected()) {
	        					 content= options[i];
	        				 }
	        				 i++;
	        			 }
	        			 if(content.equals("")) {
	        				 content = "Off";
	        			 }
	        			 Receiver.send(content, inputToFileMap.get(m.getKey()));
	        		 }
	        		 //date
	        		 else if(cFirs instanceof JPanel) {
	        			 String content = "";
	        			 for(Component panels: currPan.getComponents()) {
	        				 JPanel datePanel = (JPanel)panels;
	        			 for(Component currArr:datePanel.getComponents()) {
	        				 if(currArr instanceof JSpinner) {
	        					 JSpinner currSpin = (JSpinner)currArr;
	        					 String numThis = ""+(Integer)currSpin.getValue();
	        					 content = content+numThis+"/";
	        				 	}
	        			 	}
	        			 }
	        			 content = content.substring(0,content.length()-1);
	        			 Receiver.send(content, inputToFileMap.get(m.getKey()));

	        		 }
	        		 
	        	 }
	         }
			try {
				String retErr = "<html><dl>";
				FileReader fr;
			  if(setFilePath.contains("Gen")) {
				  fr = new FileReader(new File(doFileStuff.myPlace(),"notFilledGen.txt"));
	 			}
	         else {
	        	 fr = new FileReader(new File(doFileStuff.myPlace(),"notFilledOut.txt"));
	         }
			 Scanner sc = new Scanner(fr); 
	 		while(sc.hasNextLine()) {
	 			String nextLine = sc.nextLine();
	 			if(nextLine.contains(".pdf")||nextLine.contains(".xlsx")||nextLine.contains(".docx")||nextLine.contains(".doc")) {
	 				retErr += "<dt style=\"color:red; list-style-type: circle;\"> " + nextLine + " </dt>";
	 			}
	 			else {
	 				retErr += "<dd> " + ">"+nextLine + " </dd>";
	 			}

	 		}
	 		sc.close();
	 		retErr +="</dl></html>";
	 		JLabel message = new JLabel(retErr);
	        JOptionPane.showMessageDialog(null, message,"All Missing Data!!",2);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
			
	      }
	}
	
	
	
	
	
	
	
	
	
//Menu Bar actions
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()== FileAdd){    
		    JFileChooser fileChooser=new JFileChooser();    
		    int i=fileChooser.showOpenDialog(this);    
		    if(i==JFileChooser.APPROVE_OPTION){    
		        File file =fileChooser.getSelectedFile();    
		        file.getPath();    
		    }
		}
		else if (e.getSource()== FieldAdd) {
			if(allowed) {
			dAddMake();
			}
		}
		else if (e.getSource()== Remove) {
			if(allowed) {
			dRemMake();
			}
		}
		else if(e.getSource()==Default){
			if(allowed) {
			if(0 == JOptionPane.showConfirmDialog(frame, "Are you sure you want to reset all the fields back to the default for 2020?")) {
				
				try {
					File master = new File(doFileStuff.myPlace(),"master.txt");
					Files.copy(master.toPath(), settFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(frame, "You must restart the program in order to apply changes");
			}
		}
	} 
	

	
	public JMenu Fmenu() {
		//file options start
        JMenu Fmenu=new JMenu("File Options");
        JMenu fieldEdit=new JMenu("Field Edit");  
        Remove  = new JMenuItem("Remove");
        Rename = new JMenuItem("Rename");
        FileAdd=new JMenuItem("Add File");  
        Default = new JMenuItem("Default Fields");
        FieldAdd=new JMenuItem("Add Field");
        save=new JMenuItem("Save");
        preview = new JMenuItem("Preview");
       // help = new JMenuItem("Help");
       // Fmenu.add(FileAdd);
       // Fmenu.addSeparator();
       // Fmenu.add(save);
       // Fmenu.addSeparator();
        if(allowed) {
        fieldEdit.add(FieldAdd);
        fieldEdit.addSeparator();
        fieldEdit.add(Remove);
        //fieldEdit.addSeparator();
      //  fieldEdit.add(Rename);  
//        fieldEdit.addSeparator();
        Fmenu.add(fieldEdit); 
        Fmenu.addSeparator();
       // Fmenu.add(preview);
        Fmenu.add(Default);
        Fmenu.addSeparator();
        }
        
        //Fmenu.add(help);
       // Fmenu.addSeparator();
        //file options end
        FileAdd.addActionListener(this);  
        FieldAdd.addActionListener(this);  
        save.addActionListener(this);    
        preview.addActionListener(this);
       // help.addActionListener(this);
        Remove.addActionListener(this);
        Rename.addActionListener(this);
        Default.addActionListener(this);
        return Fmenu;
	}
	
	
	
	public void getSettIni() throws FileNotFoundException{
		Scanner sc = new Scanner(settFile); 
		while(sc.hasNextLine()) {
			String curr = sc.nextLine();
		
			try {
			//Empty Space Syntax
				if(curr.contentEquals("")) {
					//System.out.println();
				}
			//comment syntax
				else if(curr.substring(0,2).contentEquals("??")) {
					//System.out.println("Comment: " + curr.substring(2));
				}
			//tag syntax
				else if(curr.substring(0,5).equals("%$#$%")) {
					int ID = Integer.parseInt(curr.substring(5,curr.indexOf("%$#$%",5)));
					curr = curr.replace("DIRBEFOREREPLACEGEN", filePathToNew);
					//System.out.println(ID+ " "+ curr);
					
					fieldMap.put(ID, curr);
				}
			}
			catch(Exception e) {
				//System.out.println("Malformed ID in File Settings -- " + curr);
			}	
		}
		sc.close();
	}
	@SuppressWarnings("deprecation")
	public void getFieldSettById(int progID) throws FileNotFoundException {
		ArrayList<String> progIDStrings = new ArrayList<String>();
		Scanner sc = new Scanner(settFile); 
		while(sc.hasNextLine()) {
			String curr = sc.nextLine();
			try {
				if(curr.length()>5 && curr.substring(0,5).equals("%$@$%")) {
					int currID = Integer.parseInt(curr.substring(5,curr.indexOf("%$@$%",5)));
					if(currID == progID) {
						curr = curr.replace("DIRBEFOREREPLACEGEN", filePathToNew);
						progIDStrings.add(curr);
					}
				}
			}
			catch (Exception e) {
			}
			inputToFileMap.put(new Integer(progID), progIDStrings);
		}
		sc.close();
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
		String[] splits;
		if(type.length()>10) {
			splits = type.split("-");
			JPanel box = new JPanel();
			for(int i = 1; i<splits.length; i++) {
				String text = splits[i];
				JCheckBox check= new JCheckBox(text);
				box.add(check);
			}
			return box;
		}
		else {
			JCheckBox check = new JCheckBox();
			return check;
		}
		
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	
	@SuppressWarnings("deprecation")
	public Component dateCreate() {
		JPanel panel = new JPanel();
		//panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS)); 
		Date today = new Date();
		SpinnerModel  valuesM = new SpinnerNumberModel(1,1,12,1);
		SpinnerModel  valuesD = new SpinnerNumberModel(1,1,31,1);
		SpinnerModel  valuesY = new SpinnerNumberModel(today.getYear()+1900,1900,4000,1);
		JPanel b1 = new JPanel();
		JSpinner spinnerM = new JSpinner(valuesM);
		spinnerM.setMaximumSize(new Dimension(50,20)); 
		//spinnerM.setAlignmentX(LEFT_ALIGNMENT);
		b1.setLayout(new BoxLayout(b1, BoxLayout.Y_AXIS));
		b1.add(new JLabel("Month"));
		b1.add(spinnerM);
		JPanel b2 = new JPanel();
		JSpinner spinnerD = new JSpinner(valuesD);
		spinnerD.setMaximumSize(new Dimension(50,20)); 
		//spinnerD.setAlignmentX(LEFT_ALIGNMENT);
		b2.setLayout(new BoxLayout(b2, BoxLayout.Y_AXIS));
		b2.add(new JLabel("Day"));
		b2.add(spinnerD);
		JPanel b3 = new JPanel();
		JSpinner spinnerY = new JSpinner(valuesY);
		spinnerY.setMaximumSize(new Dimension(50,20)); 
		b3.setLayout(new BoxLayout(b3, BoxLayout.Y_AXIS));
		JLabel y = new JLabel("Year");
		//spinnerY.setAlignmentX(LEFT_ALIGNMENT);
		b3.add(y);
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
			}
		return "Malformed Input String Led to Nick Error";
	}
	public static void main(String[] args) throws FileNotFoundException {  
		doFileStuff.makeEnsureFiles();
		handleID();
		new preDialog();
	} 
	
}


