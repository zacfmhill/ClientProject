package fileEdit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;


public class DOCXedit {
	private String fileName;
	private File file;
	private XWPFDocument docx;
	private XWPFWordExtractor extractor;
	private XWPFRun run;
	private XWPFParagraph paragraph;
	private List<XWPFTable> tableList;
	private XWPFTableRow row;
	private XWPFTable table;
	private FileOutputStream outStream;
	
	//format rules for writing
	private boolean bold = false;
	private boolean italic = false;
	private boolean strike = false;
	private int fontSize = 12;
	private UnderlinePatterns underline = UnderlinePatterns.NONE;
	
	
	//creates new DOCX object to work on, only used for testing
	public DOCXedit() throws Exception{
		docx = new XWPFDocument(); 
		FileOutputStream outputStream = new FileOutputStream(new File("blankTest.docx"));
	    docx.write(outputStream);
	    outputStream.close();
	}
		
		
		//create object from existing Doc
	public DOCXedit(String myFileName) throws Exception{
		fileName = myFileName;
		file = new File(fileName);
		FileInputStream FIS= new FileInputStream(fileName);
		docx = new XWPFDocument(FIS);
		extractor = new XWPFWordExtractor(docx);
	}
	
	public String extract() throws IOException {
		String text = extractor.getText();
		extractor.close();
		return text;
		
	}
	//add text to replace specific text, without a pre-message condition
	public void writeTextReplace(String message, String replaceText) throws Exception {
		Map<Integer, CTR> map = new TreeMap<Integer,CTR>();
		for (XWPFParagraph para : docx.getParagraphs()) {
			paragraph = para;
			String paraContent = paragraph.getParagraphText();
			if(paraContent != null && paraContent.contains(replaceText)) {
				//if the paragraph contains our text, iterate through each run and add it to a map
				int startSearchIndex = 0;
				for(XWPFRun r: paragraph.getRuns()) {
					int startIndex = paraContent.indexOf(r.getText(0),startSearchIndex);
					startSearchIndex = startSearchIndex+r.getText(0).length();
					CTR ctr = r.getCTR();
					map.put(startIndex,ctr);
				}
				//index of where we want to add after.
				int indexOfContentReplace = paraContent.indexOf(replaceText);
				ArrayList<Integer> runs = findRuns(map,indexOfContentReplace,message);
				replace(runs,message,replaceText,indexOfContentReplace,map);
				break;
					
			}
		}
	}
	
	/*Note:
	 * Future Version Possible Method:
	 * Replace Specific Text After Specific Words
	 */

	//finds all runs needed, returns arrayList with each key for each run
	private ArrayList<Integer> findRuns(Map<Integer,CTR> map,int indexOfContentReplace, String message) throws Exception {
		ArrayList<Integer> modifying = new ArrayList<Integer>();
		Iterator<Map.Entry<Integer, CTR>> itr = map.entrySet().iterator(); 
		XWPFRun currentRun;
		while(itr.hasNext()) {
			Map.Entry<Integer, CTR> m = itr.next(); 
			currentRun = paragraph.getRun(m.getValue());
			int compareIndex = m.getKey()+currentRun.getText(0).length();
			if((compareIndex >= indexOfContentReplace)&& (m.getKey()<=indexOfContentReplace+message.length())) {
				modifying.add(m.getKey());
			}
		}
		return modifying;
	}

	private void replace(ArrayList<Integer>runs,String message,String replaceText,int indexOfContentReplace,Map<Integer,CTR> map ) throws Exception{
		outStream = new FileOutputStream(file);
		XWPFRun currentRun;
		String runText;
		int cursor = 0;
		for(Integer key: runs) {
			currentRun = paragraph.getRun(map.get(key));
			runText = currentRun.getText(0);
			for(int i = 0; i<runText.length();i++) {
				//if within the range of wanting to edit (<begin, >end, and cursor < message replacing length
				if(key+i >= indexOfContentReplace && key+i<=indexOfContentReplace+message.length()&& cursor < message.length()) {
					//if the cursor is greater than the actual replacement text
					if(cursor>=replaceText.length()) {
						//add the rest of the message past the cursor, since you can't find more text from replace Text!!
						runText = runText.substring(0,i)+message.substring(cursor)+runText.substring(i+1);
						currentRun.setText(runText,0);
						docx.write(outStream);
						return;
						
					}
					//if the cursor is less than the actual replacement text
					else {
						if(replaceText.substring(cursor,cursor+1).equals(runText.substring(i,i+1))) {
							runText = runText.substring(0,i)+message.substring(cursor,cursor+1)+runText.substring(i+1);
							currentRun.setText(runText,0);
							cursor++;
						}
					}
				}
			}
		}
			

				
		docx.write(outStream);
	}
	
	
	
	//add text after a specific string
	public void writeTextAdd(String message, String textBefore) throws Exception {
		Map<Integer, CTR> map = new TreeMap();
		outStream = new FileOutputStream(file);
		for (XWPFParagraph para : docx.getParagraphs()) {
			paragraph = para;
			String paraContent = paragraph.getParagraphText();
			
			if(paraContent != null && paraContent.contains(textBefore)) {
				//if the paragraph contains our text, iterate through each run and add it to a map
				for(XWPFRun r: paragraph.getRuns()) {
					int startIndex = paraContent.indexOf(r.getText(0));
					CTR ctr = r.getCTR();
					map.put(startIndex,ctr);
				}
				//initializa CTR for identifying needed runs 
				//index of where we want to add after.
				int indexOfContentAdd = paraContent.indexOf(textBefore)+ textBefore.length();
				for(Map.Entry<Integer, CTR> m : map.entrySet()){ ;
					if(m.getKey()+paragraph.getRun(m.getValue()).getText(0).length() >= indexOfContentAdd) {
						//run with the text before
						CTR runACTR = m.getValue();
						int runAKey = m.getKey();
						int editAtIndex = Math.abs(indexOfContentAdd-runAKey);
						XWPFRun runA = paragraph.getRun(runACTR);
						String runContent = runA.getText(0);
						runContent = runContent.substring(0,editAtIndex)+" "+message+runContent.substring(editAtIndex);
						runA.setText(runContent,0);
						break;
					}
				}
			}
		}
		docx.write(outStream);
		outStream.close();
	}
	//write message to a cell in a table in a doc
	public void writeTable(int tableNum, int settingRow, int settingCol, String message) throws Exception {
		outStream = new FileOutputStream(file);
		tableList = docx.getTables();
		table = tableList.get(tableNum);
		row = table.getRow(settingRow);
		XWPFTableCell cell = row.getCell(settingCol);
		message = " " + message;
		cell.setText(message);
		docx.write(outStream);
		outStream.close();
	}
	//sets the format data to a specific run
	private XWPFRun setRunFormat(XWPFRun run) {
		run.setBold(bold);
		run.setItalic(italic);
		run.setStrikeThrough(strike);
		run.setFontSize(fontSize);
		run.setUnderline(underline);
		return run;
	}
	//allows user to change format data
	public void format(boolean myBold,boolean myItalic,boolean myStrike,int myFontSize,UnderlinePatterns myUnderline) {
		bold = myBold;
		italic = myItalic;
		strike = myStrike;
		fontSize = myFontSize;
		underline = myUnderline;
	}
	//close extractor and writer
	public void close() throws IOException {
		
		
	}

		  
	
	

}
