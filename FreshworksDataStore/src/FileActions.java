import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileActions {
	
	static String thisFilePath="FileStore.txt";
	
	public void createFile() {
		
		
		try {
			File file = new File("FileStore.txt");
			boolean result = file.createNewFile(); 
			
			thisFilePath=file.getCanonicalPath();			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createFile(String filepath) {
		
		String fileSeperator=java.io.File.separator;
		
		String absoluteFilePath=filepath+fileSeperator+"FileStore.txt";
		thisFilePath=absoluteFilePath;
		try {
			
			File file = new File(absoluteFilePath);
			boolean result = file.createNewFile(); 
			
			thisFilePath=file.getCanonicalPath();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	}

}
