package fileEdit;

import java.io.*;
import java.io.*;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;


public class XLSXedit {
	//Instance Variable for creating and opening Excel doc
	private XSSFWorkbook workbook;
	private XSSFSheet spreadsheet;
	private FileOutputStream out;
	private String fileName;
	private File file;

	
	//creates new XLSX object to work on, only for testing
	public XLSXedit() throws Exception{
		//Creates a Blank workbook
		fileName = "testName";
		workbook = new XSSFWorkbook();
		//Creates a blank spreadsheet
		spreadsheet = workbook.createSheet(fileName);
	}
	
	
	//create object from existing sheet, no specific sheet
	public XLSXedit(String myFileName) throws Exception{
		//opens given XML File for operating on
		fileName = myFileName;
		file = new File(fileName);
		FileInputStream fIS = new FileInputStream(file);
		workbook = new XSSFWorkbook(fIS);
	    spreadsheet = workbook.getSheetAt(0);
	}
	//create object from existing sheet, specific sheet
	public XLSXedit(String myFileName,int sheetNum) throws Exception{
		//opens given XML File for operating on
		fileName = myFileName;
		file = new File(fileName);
	}
	//write file stream to excel doc
	public void write(int rowNum, int colNum,String value) throws Exception {
		if(!value.equals(" ")) {
			FileInputStream fIS = new FileInputStream(file);
			workbook = new XSSFWorkbook(fIS);
			spreadsheet = workbook.getSheetAt(0);
			out = new FileOutputStream(file);
			XSSFRow row = spreadsheet.getRow(rowNum);
			row.getCell(colNum).setCellValue(value);
			workbook.write(out);
			out.close();
			workbook.close();
			fIS.close();
			
		}
		
	}
	//close file stream
	public void close() throws Exception {
		out.close();
	}
}
