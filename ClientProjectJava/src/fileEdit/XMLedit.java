package fileEdit;

import java.io.*;
import java.io.*;
import java.util.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;


public class XMLedit {
	//Instance Variable for creating and opening Excel doc
	private XSSFWorkbook workbook;
	private XSSFSheet spreadsheet;
	private FileOutputStream out;
	private String fileName;
	private File file;

	
	//creates new XML object to work on, not used for most part
	public XMLedit() throws Exception{
		//Creates a Blank workbook
		fileName = "testName";
		workbook = new XSSFWorkbook();
		//Creates a blank spreadsheet
		spreadsheet = workbook.createSheet(fileName);
	}
	
	
	//create object from existing sheet, no specific sheet
	public XMLedit(String myFileName) throws Exception{
		//opens given XML File for operating on
		fileName = myFileName;
		file = new File(fileName);
		FileInputStream fIS = new FileInputStream(file);
		workbook = new XSSFWorkbook(fIS);
	    spreadsheet = workbook.getSheetAt(0);
	}
	//create object from existing sheet, specific sheet
	public XMLedit(String myFileName,int sheetNum) throws Exception{
		//opens given XML File for operating on
		fileName = myFileName;
		file = new File(fileName);
		FileInputStream fIS = new FileInputStream(file);
		workbook = new XSSFWorkbook(fIS);
	    spreadsheet = workbook.getSheetAt(0);
	}
	//write file stream to excel doc
	public void write(int rowNum, int colNum,String value) throws Exception {
		XSSFRow row = spreadsheet.createRow(rowNum);
		row.createCell(colNum).setCellValue(value);
	}
	//close file stream
	public void close() throws Exception {
		out = new FileOutputStream(file);
		workbook.write(out);
		out.close();
	}
}
