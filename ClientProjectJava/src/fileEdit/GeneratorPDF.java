package fileEdit;

import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import fileEdit.PDFedit;

public class GeneratorPDF{
	//this class will generate a pdf where all text boxes have what the ID is inside of their text, and
	//then open this file on the users computer
	//this will help the user in identifying the needed fields.
	//Will have instructiosn in GUI to help with understanding labels.
	public static ArrayList<String> generate(String filePath) {
		ArrayList<String> others = new ArrayList<String>();
		try {
			
			others.add("List of all fields that are not text fields:");
			PDFedit pdfGen = new PDFedit(filePath);
			List<PDField> fields = pdfGen.getFields();
			Iterator<PDField> fieldIterator = fields.iterator();
			while(fieldIterator.hasNext()) {
				PDField currField = (PDField)fieldIterator.next();
				if(currField.getFieldType().equals("Tx")) {
					//pdfGen.setField(currField.getPartialName(), currField.getPartialName());
				}
				else {
					others.add(currField.getFieldType()+" - Field Name/ID: "+currField.getFullyQualifiedName());
				}
			}
			pdfGen.save("./autoGenFieldNames.pdf");
			File file = new File("./autoGenFieldNames.pdf");
			Desktop desktop = Desktop.getDesktop();  
			
			if(file.exists()) {
				desktop.open(file);  
			}  
			file.deleteOnExit();
			

			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return others;
	}
}
