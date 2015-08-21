package gov.nist.healthcare.testing.model;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class Resources {
	JSONObject resources;
	public boolean isCustom = false;
	
	public void init(String path){
		try {
			String test = new String(readAllBytes(get(path+"/resources.json")));
			resources = new JSONObject(test);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getResources(){
		return resources.toString();
	}
	
	public void customize(String name,String oid, String type,String needed){
		if(!isCustom){
			JSONObject cust_dom = new JSONObject();
			cust_dom.put("name", "CUSTOM");
			cust_dom.put("profiles", new JSONArray());
			cust_dom.put("tables", new JSONArray());
			resources.getJSONArray("domains").put(cust_dom);
			isCustom = true;
		}
		
		JSONObject node = new JSONObject();
		node.put("name", name);
		node.put("OID", oid);
		if(type.equals("PROFILE"))
			node.put("params", needed);
		
		for(int i = 0; i < resources.getJSONArray("domains").length(); i++){
			if(resources.getJSONArray("domains").getJSONObject(i).getString("name").equals("CUSTOM")){
				resources.getJSONArray("domains").getJSONObject(i).getJSONArray(type.toLowerCase()+"s").put(node);
				return;
			}
		}
	}
}
