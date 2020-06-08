package fileEdit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDChoice;
import org.apache.pdfbox.pdmodel.interactive.form.PDComboBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.pdmodel.interactive.form.PDTerminalField;

public class PDFedit {
	//Instance variables
	PDDocument document;
	PDPage page;
	PDPageContentStream contentStream;
	PDFont font = PDType1Font.TIMES_ROMAN;
	PDDocumentCatalog docCatalog;
	
	// only for testing, not for final!
	public PDFedit() throws Exception {
		document = new PDDocument();
		page = new PDPage();
	}
	//create object from given path
	public PDFedit(String pathName) throws Exception{
		File file = new File(pathName);
		document = PDDocument.load(file);
		page = new PDPage();
		docCatalog = document.getDocumentCatalog();
		
	}
	//re assigns the document that you opened from te original file path to a new file path, and assigns doc to this new file path
	public void duplicateToEditPreserve(String originalFilePath, String newFilePath) throws Exception {
		File fileOld = new File(originalFilePath);
		document = PDDocument.load(fileOld);
		document.save(newFilePath);
		File fileNew = new File(newFilePath);
		document = PDDocument.load(fileNew);
		document.save(newFilePath);
	}
	
	public void addText(String message, int pageNum,int offsetX,int offsetY) throws Exception {
		page = document.getPage(pageNum);
		contentStream = new PDPageContentStream(document, page);
		contentStream.beginText();
		contentStream.newLineAtOffset(25, 700);
		contentStream.setFont(font, 12);
		contentStream.showText(message);
		contentStream.endText();
		contentStream.close();
	}
	
	public boolean setField(String fieldName, String fieldValue) throws Exception{
		document.getNumberOfPages();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        PDField field = acroForm.getField(fieldName);
        
        if( field != null ) {
        	
        	//dropdown
        	if(field.getFieldType().equals("Ch")) {
        		((PDComboBox)field).setValue(fieldValue);
        	}
        	//checkbox
        	else if(field.getFieldType().equals("Btn")) {
        		((PDCheckBox)field).setValue(fieldValue);
        	}
        	//text
        	else {
            field.setValue(fieldValue);
            
        	}
        }
        else {
            return false;
        }
		return true;
	}
	public String getCombo(String fieldName) {
		document.getNumberOfPages();
		PDAcroForm acroForm = docCatalog.getAcroForm();
		PDField field = acroForm.getField(fieldName);
		if(field.getFieldType().equals("Ch")) {
        		return ""+(((PDComboBox)field).getOptionsDisplayValues());
        }
		return null;
	}
	
	public List getFields(){
        PDAcroForm acroForm = docCatalog.getAcroForm();
        List allFieldsList = acroForm.getFields();
        try {
			document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return allFieldsList;
        
	}
	
	
	//adds object's page to pdf
	private void addPage(PDPage newPage) {
		document.addPage(page);
	}
	//adds a new blank page to pdf
	public void newPage() {
		PDPage newPage = new PDPage();
		document.addPage(newPage);
	}
	//saves the pdf to the given path
	public void save(String path) throws Exception {
		document.save(path);
	}
	public String strip() throws Exception {
		PDFTextStripper pdfStripper = new PDFTextStripper();
	    String content = pdfStripper.getText(document);
	    return content;
	}
}
