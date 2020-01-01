import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Client {
	
	static FileStore fs;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		fs=new FileStore("/Users/Meens/Documents/Freshworks_Assignment");
		JSONObject obj1 = new JSONObject();
		
		JSONArray hobbies1 = new JSONArray();
		hobbies1.add("Reading books");
		hobbies1.add("Listening to music");
		obj1.put("Hobbies List", hobbies1);
		JSONObject address1 = new JSONObject();
		address1.put("country", "IN");
		address1.put("city", "Chennai");
		obj1.put("Address", address1);
		obj1.put("id", "1");
		obj1.put("name", "abc");

		System.out.println(fs.create("1", obj1,"86400"));
		
		JSONObject obj2 = new JSONObject();
		JSONArray hobbies2 = new JSONArray();
		hobbies2.add("Reading books");
		hobbies2.add("Listening to music");
		obj2.put("Hobbies List", hobbies1);
		JSONObject address2 = new JSONObject();
		address2.put("country", "IN");
		address2.put("city", "Chennai");
		obj2.put("Address", address2);
		obj2.put("id", "2");
		obj2.put("name", "def");
		System.out.println(fs.create("2", obj2,"-86400"));
		
		JSONObject obj3 = new JSONObject();
		JSONArray hobbies3 = new JSONArray();
		hobbies3.add("Reading books");
		hobbies3.add("Listening to music");
		obj3.put("Hobbies List", hobbies3);
		JSONObject address3 = new JSONObject();
		address3.put("country", "IN");
		address3.put("city", "Chennai");
		obj3.put("Address", address3);
		obj3.put("id", "3");
		obj3.put("name", "def");
		System.out.println(fs.create("3", obj3,"0"));
		
		JSONObject obj4 = new JSONObject();
		JSONArray hobbies4 = new JSONArray();
		hobbies4.add("Reading books");
		hobbies4.add("Listening to music");
		obj4.put("Hobbies List", hobbies4);
		JSONObject address4 = new JSONObject();
		address4.put("country", "IN");
		address4.put("city", "Chennai");
		obj4.put("Address", address4);
		obj4.put("id", "4");
		obj4.put("name", "def");
		System.out.println(fs.create("4", obj4,"86400"));
		
		System.out.println("read function - "+fs.read("1"));
		System.out.println("delete function - "+fs.delete("1"));
		
	}

}
