package gov.nist.healthcare.testing.helpers;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.json.JSONException;
import org.json.JSONObject;

import gov.nist.healthcare.testing.model.ValidationObject;
import gov.nist.healthcare.testing.wsclient.HL7V2WS;
import gov.nist.healthcare.unified.model.EnhancedReport;

public class BatchHandler {
	
	private String user_id;
	private ValidationObject configO;
	private HL7V2WS ws;
	private String path;
	private boolean folderExists;
	private int report_id;
	
	public BatchHandler(String user_id,String path,HL7V2WS ws) {
		super();
		this.user_id = user_id;
		this.ws = ws;
		this.folderExists = false;
		this.path = path;
		report_id = 1;
		this.createFolder();
	}
	
	public void createFolder(){
		File file = new File(path+"/"+user_id);
		File file1 = new File(path+"/"+user_id+"/reports");
		File file2 = new File(path+"/"+user_id+"/zips");
		if(!file.exists() && !file1.exists() && !file2.exists()){		
			folderExists = file.mkdir() && file1.mkdir() && file2.mkdir();
		}
		else
			folderExists = true;
	}
	
	public void clearFolder(){
		File f = new File(path+"/"+user_id+"/reports");
		String[]entries = f.list();
		for(String s: entries){
		    File currentFile = new File(f.getPath(),s);
		    currentFile.delete();
		}
	}
	
	public Object getBatchedResult(){
		return null;
	}
	
	public void setConfig(ValidationObject config,Hashtable<String,String> params){
		try {
			
			this.configO = config;
			
			configO.setProfilOID(params.get("profile"));
			
			String tables = "";
			if(params.get("tables") != null && !params.get("tables").equals(""))
				tables = params.get("tables").substring(0, params.get("tables").length() - 2);
			configO.setTablesOID(tables);
			
			if (params.get("contextB").equals("false") && !params.get("domain").equals("CUSTOM")) {
				String context;
				context = new String(readAllBytes(get(path+params.get("domain").toLowerCase()+"/ValidationContext.xml")));
				configO.setValidationContext(context);
			}
			
			configO.setReady(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			configO.setReady(false);
			e.printStackTrace();
		}
	}
	
	public String validate(String msg,String name) throws JSONException, JAXBException{
		try {
			if(!folderExists)
				return "Error Folder";
			
			PrintWriter writer = new PrintWriter(path+"/"+user_id+"/reports/"+name+".xml", "UTF-8");
			configO.setMessage(msg);
			if(ws.validate(configO)) {
				if(configO.getResponse().trim().startsWith("<"))
					writer.write(configO.getResponse());
				else
				{
					System.out.println(configO.getResponse());
					EnhancedReport r = EnhancedReport.from("json",configO.getResponse());
					writer.write(r.to("xml").toString());
				}
				writer.flush();
				writer.close();
				return "";
			}
			writer.close();
			return "Error Validation";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error FileNF";
		}
	}
	
	public JSONObject toZip(){
		JSONObject o = new JSONObject();
		File directoryToZip = new File(path+"/"+user_id+"/reports");
		List<File> fileList = new ArrayList<File>();
		ZipDirectory.getAllFiles(directoryToZip, fileList);
		System.out.println("---Creating zip file");
		
		int nb = ZipDirectory.writeZipFile(directoryToZip, fileList,path+"/"+user_id+"/zips/Reports_"+report_id);
		o.put("nbR", nb);
		File f = new File(path+"/"+user_id+"/zips/Reports_"+report_id+".zip");
		o.put("size", humanReadableByteCount(f.length(),true));
		report_id++;
		this.clearFolder();
		return o;
	}
	
	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	public String path() {
		return user_id+"/zips/Reports_"+(report_id-1)+".zip";
	}
}
