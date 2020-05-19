package TestApplicationFiles;

import fileEdit.DOCXedit;
import fileEdit.XLSXedit;
import java.io.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
public class TestApp {
	
	public static void main(String[] args) throws Exception {
		DOCXedit docxTest = new DOCXedit("C:\\Users\\zacfm\\OneDrive\\Desktop\\Chaperone Parking Information.docx");
		System.out.println(docxTest.extract());
		//docxTest.format(true, true, true, 12, UnderlinePatterns.SINGLE);
		//docxTest.writeTable(0,1,0,"test enter");
		docxTest.writeTextAdd("towels","insert items");
	}
	
}



 
