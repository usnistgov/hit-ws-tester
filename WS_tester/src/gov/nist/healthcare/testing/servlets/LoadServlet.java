package gov.nist.healthcare.testing.servlets;

import gov.nist.healthcare.testing.model.LoadedNode;
import gov.nist.healthcare.testing.model.LoadedResources;
import gov.nist.healthcare.testing.model.Resources;
import gov.nist.healthcare.testing.model.ValidationObject;
import gov.nist.healthcare.testing.wsclient.HL7V2WS;

import java.io.IOException;
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
 * Servlet implementation class LoadServlet
 */
@WebServlet("/load")
public class LoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadServlet() {
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
		HL7V2WS ws = (HL7V2WS) session.getAttribute("WS");
		//ValidationObject l = (ValidationObject) session.getAttribute("VO");
		LoadedNode ln = new LoadedNode();
		boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
		if (!isMultipartContent) {
			System.out.println("Not Multipart");
			return;
		}

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
				if (!isFormField && !fileItem.getString().equals("")) {
					session.setAttribute("LastAction", "up_res");
					//System.out.println(fileItem.getName());
					//session.setAttribute("VO", o);
					ln.content = fileItem.getString();
				}
				else if(isFormField){
					
					if(fileItem.getFieldName().equals("type"))
						ln.type = fileItem.getString().toUpperCase();
					else if(fileItem.getFieldName().equals("name"))
						ln.name = fileItem.getString();
				}
			}
			ln.OID = UUID.randomUUID().toString();
			JSONObject result = ws.load(ln);

			if(result.getBoolean("status"))
			{
				lr.add(ln);
				rs.customize(ln.name, ln.OID, ln.type, "needed");
			}
			
			response.setContentType("application/json");
			response.getOutputStream().print(result.toString());
			
		} catch (FileUploadException e) {
			System.out.println("Error");
			e.printStackTrace();
		}
	}

}
