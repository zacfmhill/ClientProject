package TestApplicationFiles;

import fileEdit.XLSXedit;
import java.io.*;
import org.apache.poi.xssf.usermodel.*;
public class TestApp {
	
	public static void main(String[] args) throws Exception {
		XLSXedit XMLeditor = new XLSXedit("C:\\Users\\zacfm\\OneDrive\\Documents\\GitHub\\ClientProject\\ClientProjectJava\\testXMLFile.xlsx");
		XMLeditor.write(0,10,"hello");
		XMLeditor.close();
	}
	
}



 
