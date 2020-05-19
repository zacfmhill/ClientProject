package fileEdit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
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
		outStream = new FileOutputStream(file);
		for (XWPFParagraph para : docx.getParagraphs()) {
			paragraph = para;
			List<XWPFRun> runs = paragraph.getRuns();
			if (runs != null) {
				for (XWPFRun r : runs) {
					run = r;
					String content = r.getText(0);
					System.out.println(content + "jjgiofguhfi");
					if(content != null && content.contains(replaceText)) {
						content = content.replace(replaceText, message);
						System.out.println("doing this");
						setRunFormat(run);
						run.setText(content,0);
						docx.write(outStream);
						return;
					}
				}
			}
		}
		docx.write(outStream);
	}
	//add text to replace specific text that occurs after a specific phrase.
	public void writeTextReplace(String message, String replaceText, String textBefore) {
			
	}
	//add text after a specific string
	public void writeTextAdd(String message, String textBefore) throws Exception {
		outStream = new FileOutputStream(file);
		for (XWPFParagraph para : docx.getParagraphs()) {
			paragraph = para;
			String paraContent = paragraph.getParagraphText();
			if(paraContent != null && paraContent.contains(textBefore)) {
				int index = paraContent.indexOf(textBefore)+textBefore.length();
				run = paragraph.insertNewRun(3);
				setRunFormat(run);
				run.setText(message);
				break;
			}
		}
		docx.write(outStream);
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
