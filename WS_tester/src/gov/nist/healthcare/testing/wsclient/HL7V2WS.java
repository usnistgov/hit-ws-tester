package gov.nist.healthcare.testing.wsclient;

import java.net.URL;

import javax.xml.namespace.QName;

import org.json.JSONObject;
import org.json.XML;

import gov.nist.healthcare.hl7ws.messagevalidation.MessageValidationV2Interface;
import gov.nist.healthcare.testing.model.LoadedNode;
import gov.nist.healthcare.testing.model.ValidationObject;
import hl7ws.healthcare.nist.gov.validation.message.MessageValidationV2;

public class HL7V2WS {
	
	private MessageValidationV2 ws;
	private MessageValidationV2Interface port;
	
	private HL7V2WS(){
		URL wsdlURL = MessageValidationV2.WSDL_LOCATION;
		QName SERVICE_NAME = new QName("http://gov.nist.healthcare.hl7ws/validation/message", "MessageValidationV2");
		ws = new MessageValidationV2(wsdlURL, SERVICE_NAME);
		port = ws.getMessageValidationV2ServicePort();  
	}
	
	private HL7V2WS(String endpoint){
		URL wsdlURL = MessageValidationV2.WSDL_LOCATION;
		QName SERVICE_NAME = new QName("http://gov.nist.healthcare.hl7ws/validation/message", "MessageValidationV2");
		ws = new MessageValidationV2(wsdlURL, SERVICE_NAME,endpoint);
		port = ws.getMessageValidationV2ServicePort();  
	}
	
	public static HL7V2WS getInstance(String endp){
		return new HL7V2WS(endp);
	}
	
	public boolean validate(ValidationObject vo){
		if(vo.isReady()){
			System.out.println("NEW Validation :");
			System.out.println("Profile : "+vo.getProfilOID());
			System.out.println("Tables  : "+vo.getTablesOID());
			System.out.println("Context : "+vo.getValidationContext());
			System.out.println("Message : "+vo.getMessage());
			System.out.println("DQA 	: "+vo.isDQA());
			System.out.println("DQAf 	: "+vo.getDQAFilter());
			String response = "";
			if(!vo.isDQA())
				response = port.validate(vo.getMessage(), vo.getProfilOID(), vo.getTablesOID(), vo.getValidationContext());
			else
				response = port.validateDQA(vo.getMessage(), vo.getProfilOID(), vo.getTablesOID(), vo.getValidationContext(), vo.getDQAFilter());

			if(response != null && !response.equals("")){
				vo.setResponse(response);
				return true;
			}
		}
		return false;
	}
	
	public JSONObject load(LoadedNode ln){
		String response = port.loadResource(ln.content, ln.OID, ln.type);
		JSONObject jo = XML.toJSONObject(response);
		return jo.getJSONObject("xmlLoadResource");
	}
	
	public String getServiceStatus(){
		return port.getServiceStatus();
	}

}
