import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException; 

public class FileStore {
	
	static FileActions fa;
	
	public FileStore() {
		fa=new FileActions();
		fa.createFile();
	}
	public FileStore(String filepath) {
		fa=new FileActions();
		fa.createFile(filepath);
	}
	
	public synchronized String create(String key, JSONObject jsonObj, String TTL) {
		
		boolean validKey=validateKey(key);
		boolean validJson=validateJsonObject(jsonObj);
		
		if(validKey && validJson) {
			
			try {
				
					BufferedWriter out = new BufferedWriter(new FileWriter(FileActions.thisFilePath,true));
					
					//writing the key in file
					out.write("Key - "+key);
					
					//writing the create date in file
					Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
					out.write(", Create Date - "+calendar.getTime());
					
					//writing the TTL date in file
					calendar.add(Calendar.SECOND, Integer.parseInt(TTL));
					out.write(", TTL Date - "+calendar.getTime());
					         
					out.write("\n");
			
					//writing the JSON OBject to the file
					out.write(jsonObj.toJSONString());
					//System.out.println(jsonObj.toJSONString());
					
					out.write("\n");
					
					out.close();
					deleteIfFileExceeds();
						
				return "Successfully created";
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "error while creating file";
			} 
			
		}
		else if(!validKey){
			return "Please provide a valid key (should not exceed 32 characters)";
		}
		else if(!validJson){
			return "Please provide a valid JSON Object (should not exceed 16 KB)";
		}
		else {
			return "Please provide a valid key (should not exceed 32 characters) and a valid JSON Object (should not exceed 16 KB)";
		}
	}

	public JSONObject read(String key) {
		
			File file = new File(FileActions.thisFilePath); 
		  
		  BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		  
		  String st; 
		  
		  try {
			while ((st = br.readLine()) != null) {
			    
			    if(st.contains("Key - "+key)) {
			    	
			    	String jsonString;
			    	jsonString=br.readLine();
			    	
			    	JSONParser parser = new JSONParser();
			    	JSONObject json = (JSONObject) parser.parse(jsonString);
			    	
			    	return json;
			    }
			  }
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;	
		
	}
	

	public String delete(String key) {
		
	
		File file = new File(FileActions.thisFilePath); 
		File tempFile = new File(file.getAbsolutePath() + ".tmp");  
		try {
			BufferedReader br = new BufferedReader(new FileReader(FileActions.thisFilePath));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				
				String startDate=null;
				String endDate=null;
				boolean flag=false;
				
				
				if(line.contains("Key - ")) {
					startDate = line.substring(23, 51);
					endDate = line.substring(64, 92);
					
					SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
					Calendar calendar = Calendar.getInstance();
					
					if(!startDate.equals(endDate)) {
						if(line.contains("Key - "+key) || sdf.parse(endDate).before(calendar.getTime())) {					
							line = br.readLine();
							flag=true;
						}
					}
				}	
				
				if(flag==false) {
					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
            br.close();
            file.delete();
            tempFile.renameTo(file);
            return "Deleted successfully";
            
		} catch (IOException | java.text.ParseException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return "Error in deletion";
		}
		
}
	
	public void deleteIfFileExceeds() {
		try {
			
			Path filePath = Paths.get(FileActions.thisFilePath);
			FileChannel fileChannel=FileChannel.open(filePath);
			long fileSize = fileChannel.size();
			
			long div=fileSize/1073741824 ;
			
			if(div>1) {
				File file = new File(FileActions.thisFilePath); 
				File tempFile = new File(file.getAbsolutePath() + ".tmp");  
				
				BufferedReader br = new BufferedReader(new FileReader(FileActions.thisFilePath));
				PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
				String line = null;
				while ((line = br.readLine()) != null) {
					
					String startDate=null;
					String endDate=null;
					boolean flag=false;
					
					if(line.contains("Key - ")) {
						startDate = line.substring(23, 51);
						endDate = line.substring(64, 92);
						
						SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
						Calendar calendar = Calendar.getInstance();
						
						if(!startDate.equals(endDate)) {
							if(sdf.parse(endDate).before(calendar.getTime())) {					
								line = br.readLine();
								flag=true;
							}
						}
					}	
					
					if(flag==false) {
						pw.println(line);
						pw.flush();
					}
				}
				pw.close();
	            br.close();
	            file.delete();
	            tempFile.renameTo(file);
				
			}		
					 
			
		} catch (IOException | java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	private boolean validateJsonObject(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("jsonFile.txt"));
			out.write(jsonObj.toJSONString());
			out.close();
			Path filePath = Paths.get("jsonFile.txt");
			FileChannel fileChannel=FileChannel.open(filePath);
			long fileSize = fileChannel.size();
			
			if((fileSize/1024)>16) {
				Files.deleteIfExists(Paths.get("jsonFile.txt"));
				return false;
			}
			else {
				Files.deleteIfExists(Paths.get("jsonFile.txt"));
				return true;
			}
					 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	private boolean validateKey(String key2) {
		// TODO Auto-generated method stub
		if(key2.length()>32)
			return false;
		else
			return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
