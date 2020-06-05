package fileEdit;

public class PDFgetCombos {
	public static String getCombo(String fieldID,String filePath) {
		try {
			PDFedit p = new PDFedit(filePath);
			return (p.getCombo(fieldID));
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
