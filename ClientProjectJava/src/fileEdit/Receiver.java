package fileEdit;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import GUI.gui;

public class Receiver {
	
	public static String getFileType(String fileName) {
		try {
		int indexOfDot = fileName.indexOf(".")+1;
		String endFile = fileName.substring(indexOfDot);
		String type = endFile.toUpperCase();
		return type;
		}
		catch(Exception e){
			System.out.println("Invalid File Format!!");
			return null;
		}
	}
	
	
	public static void send(String content,ArrayList<String> fileLines) {
		System.out.println("Recieved: "+ content);
		String filePathNew = GUI.gui.filePathToNew;
		for(String str:fileLines) {
			String[] splits = str.split("%");
			ArrayList<String> parms = new ArrayList<String>();
			for(String s :splits) {
				if(!s.contentEquals("$@$")&&s.length()>0)
					parms.add(s);
			}
			System.out.println(parms);
			//checks if this is a valid file before sending it off!!!
			File file;
			File copy;
			try {
				file = new File(parms.get(1));
				String fileName = file.getName();
				copy = new File(filePathNew+ "\\" +fileName);
				Files.copy(file.toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
				String filePath = copy.getAbsolutePath();
				String type = getFileType(parms.get(1));
				String ID = parms.get(2);
				interpEdit(type,filePath,content,ID);	
			}
			catch (Exception e) {
				gui.invalid();
				
			}
			
		}
	}
	
	public static void interpEdit(String type, String filePath,String content,String ID) {
		switch (type){
		//if type DOCX
			case "DOCX":
				sendDOCX(filePath,content,ID);
				break;
		//if type XLSX
			case "XLSX":
				sendXLSX(filePath,content,ID);
				break;
		//if type PDF
			case "PDF":
				System.out.println(filePath);
				sendPDF(filePath,content,ID);
				break;
		}
	}
	public static void sendDOCX(String filePath,String content,String ID) {
		String typeDo = (ID.substring(0,1)).toUpperCase();
		ID = ID.substring(2);
		String[] IDparms = ID.split("-");
		try {
			DOCXedit docxEdit = new DOCXedit(filePath);
//edit table
		if(typeDo.equals("T")) {
			//subtracting 1 to make it counting starting at 0!
			int tableNum = Integer.parseInt(IDparms[0]) - 1;
			int row = Integer.parseInt(IDparms[1]) - 1;
			int col = Integer.parseInt(IDparms[2]) - 1;
			docxEdit.writeTable(tableNum, row, col, content);
		}
//insert text
		else if(typeDo.contentEquals("I")) {
			String insertingAfter = IDparms[0];
			docxEdit.writeTextAdd(content, insertingAfter);
		}
//replace text
		else if(typeDo.contentEquals("R")) {
			String replacing = IDparms[0];
			docxEdit.writeTextReplace(content, replacing);
		}
//issue with current Tagging
		else {
			System.out.println("Error, issue with current tagging!!");
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void sendXLSX(String filePath,String content,String ID) {
		String typeDo = (ID.substring(0,1)).toUpperCase();
		ID = ID.substring(2);
		String[] IDparms = ID.split("-");
		try {
			XLSXedit xlsxEdit;
			if(typeDo.equals("C")) {
				//-1 to get to start count @ 0
				int row = Integer.parseInt(IDparms[0]) -1;
				int col = Integer.parseInt(IDparms[1]) -1;
				xlsxEdit = new XLSXedit(filePath);
				xlsxEdit.write(row, col, content);
			}
			else if(typeDo.equals("S")) {
				int sheetNum = Integer.parseInt(IDparms[0]) -1;
				int row = Integer.parseInt(IDparms[1]) -1;
				int col = Integer.parseInt(IDparms[2]) -1;
				xlsxEdit = new XLSXedit(filePath,sheetNum);
				xlsxEdit.write(row, col, content);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void sendPDF(String filePath,String content,String ID) {
		String typeDo = (ID.substring(0,1)).toUpperCase();
		ID = ID.substring(2);
		try {
			PDFedit pdfTest = new PDFedit(filePath);
			switch(typeDo) {
			//checkbox
				case "C":
					pdfTest.setField(ID, content);
					pdfTest.save(filePath);
			//combobox
				case "L":
					pdfTest.setField(ID, content);
					pdfTest.save(filePath);
			//Date
				case "D":
					String[] contArr = content.split("/");
					String[] idArr = ID.split("-");
					for(int i = 0; i<idArr.length;i++) {
						pdfTest.setField(idArr[i], contArr[i]);
					}
					pdfTest.save(filePath);
			//text area
				case "T":
					pdfTest.setField(ID, content);
					pdfTest.save(filePath);
					
			}
		}
		//most likely, either the field does not exist under the given ID,
		//or the type of the field, ex checkbox, does not match what the user used!
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
