package gov.nist.healthcare.testing.model;

import java.util.ArrayList;

public class StringFormat {
	public ArrayList<String> errors = new ArrayList<String>();
	public ArrayList<String> warnings = new ArrayList<String>();
	public ArrayList<String> alerts = new ArrayList<String>();
	
	public String format(String str){
		String[] lines = str.split("\n");
		int i = 0;
		while(i < lines.length)
		{
			boolean hasF = false;
			boolean hasR = false;
			if(lines[i].startsWith("[Alert]"))
				alerts.add(lines[i]);
			else if(lines[i].startsWith("[Error]"))
			{
				String error = lines[i];
				if((i+1) < lines.length && lines[i+1].trim().startsWith("[Failed]"))
				{
					error = error + "<br/>"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+lines[i+1].trim();
					hasF = true;
				}
				if((i+2) < lines.length && lines[i+2].trim().startsWith("Reason"))
				{
					error = error + "<br/>"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+lines[i+2].trim();
					hasR = true;
				}
				errors.add(error);
			}
			else if(lines[i].startsWith("[Warning]")){
				warnings.add(lines[i]);
			}
			
			i++;
			if(hasF)
				i++;
			if(hasR)
				i++;
		}
		
		String errTable = "";
		String warningTable = "";
		String alertTable = "";
		
		String summaryTable = "<table>\n"
				+ "<tr>"
				+ "		<th colspan=2> Summary </th>"
				+ "</tr>"
				+ "<tr>"
				+ "		<td> Errors </td>"
				+ "		<td class='danger'> "+errors.size()+" </td>"
				+ "</tr>"
				+ "<tr>"
				+ "		<td> Warnings </td>"
				+ "		<td class='warning'> "+warnings.size()+" </td>"
				+ "</tr>"
				+ "<tr>"
				+ "		<td> Alerts </td>"
				+ "		<td class='info'> "+alerts.size()+" </td>"
				+ "</tr>"
				+ "</table></br>";
		
		
		if(errors.size() > 0){
			errTable = "<table>\n";
			errTable += "<tr><th class='danger'>"+errors.size() + " errors were detected </th></tr>\n";
			for(String error : errors)
				errTable +=  "<tr><td>" + error + "</tr></td>\n";
			errTable += "</table>\n<br/>";
		}
		
		if(warnings.size() > 0){
			warningTable = "<table>\n";
			warningTable += "<tr><th class='warning'>" + warnings.size() + " warnings were detected </th></tr>\n";
			for(String warning : warnings)
				warningTable +=  "<tr><td>" + warning + "</tr></td>\n";
			warningTable += "</table>\n<br/>";
		}
		
		if(alerts.size() > 0){
			alertTable = "<table>\n";
			alertTable += "<tr><th class='info'>"+ alerts.size() + " alerts were detected </th></tr>\n";
			for(String alert : alerts)
				alertTable +=  "<tr><td>" + alert + "</tr></td>\n";
			alertTable += "</table>\n<br/>";
		}
		
		
		return summaryTable+errTable+warningTable+alertTable;
	}
	
}
