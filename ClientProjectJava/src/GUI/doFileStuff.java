package GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import GUI.gui;

public class doFileStuff {
	public static File myPlace() {
		File myCurrentPlace;
		try {
			System.out.println(gui.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			myCurrentPlace = new File(gui.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			System.out.println(myCurrentPlace.getParent());
			return myCurrentPlace.getParentFile();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void makeEnsureFiles() {
		try {
			boolean creating = true;
			InputStream genSettStream = gui.class.getResourceAsStream("/GenSettings.txt");
			InputStream IDuserProfilingStream = gui.class.getResourceAsStream("/IDuserProfiling.txt");
			InputStream masterStream = gui.class.getResourceAsStream("/master.txt");
			InputStream overOutSettStream = gui.class.getResourceAsStream("/OverOutSettings.txt");
			InputStream errorOutStream = gui.class.getResourceAsStream(doFileStuff.myPlace().getPath()+"/notFilledOut.txt");
			InputStream errorGenStream = doFileStuff.class.getResourceAsStream("/notFilledGen.txt");
			File genSett = new File(myPlace(),"GenSettings.txt");
			File IDuser = new File(myPlace(),"IDuserProfiling.txt");
			File masterSett = new File(myPlace(),"master.txt");
			File OverOutSett = new File(myPlace(),"OverOutSettings.txt");
			File errorOut = new File(myPlace(),"notFilledOut.txt");
			File errorGen = new File(myPlace(),"notFilledGen.txt");
			
			System.out.println(errorOut);
			System.out.println(errorGen);
			System.out.println(errorOutStream);
			System.out.println(errorGenStream);
			if(genSett.exists()&&genSett.isFile()) {
				if(IDuser.exists()&&IDuser.isFile()) {
					if(masterSett.exists()&&masterSett.isFile()) {
						if(OverOutSett.exists()&&OverOutSett.isFile()) {
							if(errorGen.exists()&&errorGen.isFile()) {
								if(errorOut.exists()&&errorOut.isFile()) {
									creating = false;
								}
							}
						}
					}
				}
			}
			System.out.println(creating);
			if(creating) {
				Files.copy(genSettStream, genSett.toPath(), StandardCopyOption.REPLACE_EXISTING);
				System.out.println("g");
				Files.copy(IDuserProfilingStream, IDuser.toPath(), StandardCopyOption.REPLACE_EXISTING);
				System.out.println("g");
				Files.copy(masterStream, masterSett.toPath(), StandardCopyOption.REPLACE_EXISTING);
				System.out.println("g");
				Files.copy(overOutSettStream, OverOutSett.toPath(), StandardCopyOption.REPLACE_EXISTING);
				System.out.println("g");
				Files.copy(errorGenStream, errorGen.toPath(),StandardCopyOption.REPLACE_EXISTING);
				System.out.println("g");
				Files.copy(errorOutStream, errorOut.toPath(),StandardCopyOption.REPLACE_EXISTING);
				System.out.println("g");
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws FileNotFoundException {  
	}
}
