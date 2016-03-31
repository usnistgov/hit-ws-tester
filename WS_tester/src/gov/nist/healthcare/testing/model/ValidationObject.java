package gov.nist.healthcare.testing.model;

public class ValidationObject {
	private String profilOID;
	private String tablesOID;
	private String validationContext;
	private String message;
	private String response;
	private boolean DQA;
	private String DQAFilter;
	private boolean ready;
	
	public boolean isReady() {
		return ready;
	}
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	public String getProfilOID() {
		return profilOID;
	}
	public void setProfilOID(String profilOID) {
		this.profilOID = profilOID;
	}
	public String getTablesOID() {
		return tablesOID;
	}
	public void setTablesOID(String tablesOID) {
		this.tablesOID = tablesOID;
	}
	public String getValidationContext() {
		return validationContext;
	}
	public void setValidationContext(String validationContext) {
		this.validationContext = validationContext;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public boolean isDQA() {
		return DQA;
	}
	public void setDQA(boolean dQA) {
		DQA = dQA;
	}
	public String getDQAFilter() {
		return DQAFilter;
	}
	public void setDQAFilter(String dQAFilter) {
		DQAFilter = dQAFilter;
	}
	
}
