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
			return null;
		}
	}
	
	
	public static void send(String content,ArrayList<String> fileLines) {
		String filePathNew = GUI.gui.filePathToNew;
		if(content.equals(null)) {
			content = " ";
		}
		for(String str:fileLines) {
			String[] splits = str.split("%");
			ArrayList<String> parms = new ArrayList<String>();
			for(String s :splits) {
				if(!s.contentEquals("$@$")&&s.length()>0)
					parms.add(s);
			}
				File file = new File(parms.get(1));
				String filePath = file.getPath();
				String type = getFileType(parms.get(1));
				String ID = parms.get(2);
				interpEdit(type,filePath,content,ID);	
		}
	}
	
	public static void interpEdit(String type, String filePath,String content,String ID) {
		switch (type){
		//if type DOCX
			case "DOCX":
				sendDOCX(filePath,content,ID);
				break;
			case "DOC":
				sendDOCX(filePath,content,ID);
				break;
		//if type XLSX
			case "XLSX":
				sendXLSX(filePath,content,ID);
				break;
		//if type PDF
			case "PDF":
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
		else if(typeDo.equals("I")) {
			String insertingAfter = IDparms[0];
			docxEdit.writeTextAdd(content, insertingAfter);
		}
//replace text
		else if(typeDo.equals("R")) {
			String replacing = IDparms[0];
				while(content.length()<replacing.length()) {
					content = " " + content + "  ";
				}
			docxEdit.writeTextReplace(content, replacing);
			try {
				docxEdit.writeTextReplace(content, replacing);
			}
			catch(Exception e){}
		}
//issue with current Tagging
		else {
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
				int row = Integer.parseInt(IDparms[0].strip()) -1;
				int col = Integer.parseInt(IDparms[1].strip()) -1;
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
					break;
			//combobox
				case "L":
					pdfTest.setField(ID, content);
					pdfTest.save(filePath);
					break;
			//Date
				case "D":
					String[] contArr = content.split("/");
					String[] idArr = ID.split("-");
					for(int i = 0; i<idArr.length;i++) {
						pdfTest.setField(idArr[i], contArr[i]);
					}
					pdfTest.save(filePath);
					break;
			//text area
				case "T":
					pdfTest.setField(ID, content);
					pdfTest.save(filePath);
					break;
					
			}
		}
		//most likely, either the field does not exist under the given ID,
		//or the type of the field, ex checkbox, does not match what the user used!
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
