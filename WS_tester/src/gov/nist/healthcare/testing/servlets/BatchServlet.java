package gov.nist.healthcare.testing.servlets;

import gov.nist.healthcare.testing.helpers.BatchHandler;
import gov.nist.healthcare.testing.helpers.Timer;
import gov.nist.healthcare.testing.model.LoadedNode;
import gov.nist.healthcare.testing.model.LoadedResources;
import gov.nist.healthcare.testing.model.Resources;
import gov.nist.healthcare.testing.model.ValidationObject;
import gov.nist.healthcare.testing.wsclient.HL7V2WS;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

/**
 * Servlet implementation class BatchServler
 */
@WebServlet("/batch")
public class BatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BatchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ValidationObject o = (ValidationObject) session.getAttribute("VO");
		LoadedResources lr = (LoadedResources) session.getAttribute("LR");
		Resources rs = (Resources) session.getAttribute("RS");
		BatchHandler bh = (BatchHandler) session.getAttribute("BH");
		HL7V2WS ws = (HL7V2WS) session.getAttribute("WS");

		Hashtable<String,String> params = new Hashtable<String,String>();
		JSONObject responseO = new JSONObject();
		response.setContentType("application/json");
		
		boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
		if (!isMultipartContent) {
			if(request.getParameter("message").equals("init")){
				for(String n : request.getParameterMap().keySet()){
					params.put(n,request.getParameterMap().get(n)[0]);
				}
				bh.setConfig(o, params);
				Timer.start();
			}
			else if(request.getParameter("message").equals("finish")){
				JSONObject obj = bh.toZip();
				responseO.put("path", bh.path());
				responseO.put("size", obj.getString("size"));
				responseO.put("nbR", obj.getInt("nbR"));
				responseO.put("time", Timer.finish());
				responseO.put("type", "zip");
			}
			
			responseO.put("status", true);
			response.getOutputStream().print(responseO.toString());
			response.getOutputStream().flush();
			
		}
		else {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				List<FileItem> fields = upload.parseRequest(request);
				Iterator<FileItem> it = fields.iterator();
				if (!it.hasNext()) {
					return;
				}
				while (it.hasNext()) {
					FileItem fileItem = it.next();
					boolean isFormField = fileItem.isFormField();
					if(!isFormField)
						params.put("filename", fileItem.getName());
					params.put(fileItem.getFieldName(),fileItem.getString());
				}
				
				String r = bh.validate(params.get("file"), params.get("filename"));
				responseO.put("status", r.equals(""));
				responseO.put("error", r);
				response.getOutputStream().print(responseO.toString());
				response.getOutputStream().flush();
				
			} catch (Exception e) {
				System.out.println("Error");
				e.printStackTrace();
			}
		}

		
	}

}
