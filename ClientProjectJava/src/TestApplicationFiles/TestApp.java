package TestApplicationFiles;

import fileEdit.XMLedit;
import java.io.*;
import org.apache.poi.xssf.usermodel.*;
public class TestApp {
	
	public static void main(String[] args) throws Exception {
		XMLedit XMLeditor = new XMLedit("C:\\Users\\zacfm\\OneDrive\\Documents\\GitHub\\ClientProject\\ClientProjectJava\\testXMLFile.xlsx");
		XMLeditor.write(0,1,"hello");
		XMLeditor.close();
	}
	
}



 
