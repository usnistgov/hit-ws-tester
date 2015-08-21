package gov.nist.healthcare.testing.model;

import java.util.ArrayList;
import java.util.Hashtable;

public class LoadedResources {
	public Hashtable<String,ArrayList<LoadedNode>> resources = new Hashtable<String,ArrayList<LoadedNode>>();
	public boolean isLoaded(String type,String OID){
		for(LoadedNode ln : resources.get(type)){
			if(ln.OID.equals(OID))
				return true;
		}
		return false;
	}
	
	public void add(LoadedNode ln){
		if(!resources.containsKey(ln.type))
			resources.put(ln.type, new ArrayList<LoadedNode>());
		
		resources.get(ln.type).add(ln);
	}
}
