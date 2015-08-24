package gov.nist.healthcare.testing.servlets;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import gov.nist.healthcare.testing.helpers.Timer;
import gov.nist.healthcare.testing.model.LoadedResources;
import gov.nist.healthcare.testing.model.StringFormat;
import gov.nist.healthcare.testing.model.ValidationObject;
import gov.nist.healthcare.testing.wsclient.HL7V2WS;
import gov.nist.healthcare.unified.model.EnhancedReport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONObject;


/**
 * Servlet implementation class ValidationServlet
 */
@WebServlet("/validate")
public class ValidationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ValidationServlet() {
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
		ValidationObject vo = (ValidationObject) session.getAttribute("VO");
		HL7V2WS ws = (HL7V2WS) session.getAttribute("WS");
		LoadedResources lr = (LoadedResources) session.getAttribute("LR");
		
		vo.setMessage(request.getParameter("message"));
		vo.setProfilOID(request.getParameter("profile"));
		
		String tables = "";
		if(request.getParameter("tables") != null && !request.getParameter("tables").equals(""))
			tables = request.getParameter("tables").substring(0, request.getParameter("tables").length() - 2);
		
		/*if(request.getParameter("domain").equals("CUST")){
			String[] _tables = tables.split(":");
			for(String table : _tables){
				if(!lr.isLoaded("TABLE", table))
					System.out.println("Not Loaded : "+table);
			}
		}*/
		vo.setTablesOID(tables);
		
		if (request.getParameter("contextB").equals("false") && !request.getParameter("domain").equals("CUSTOM")) {
			String path = this.getServletContext().getRealPath(request.getParameter("domain").toLowerCase());
			String context = new String(readAllBytes(get(path+"/ValidationContext.xml")));
			vo.setValidationContext(context);
		}
		
		vo.setReady(true);
		try {
			Timer.start();
			if(ws.validate(vo)){
				long time = Timer.finish();
				if(vo.getResponse().startsWith("<"))
				{
					InputStream is = new ByteArrayInputStream(vo.getResponse().getBytes());
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					TransformerFactory tFactory = TransformerFactory.newInstance();
					Transformer transformer;
					try {
						System.out.println(this.getServletContext().getRealPath("style")+"/stylesheet.xsl");
						transformer = tFactory.newTransformer(new StreamSource(this.getServletContext().getRealPath("style")+"/stylesheet.xsl"));
		
						transformer.transform(new StreamSource(is), new StreamResult(os));
						String result = os.toString();
						result = result.replaceAll("<br>", " ");
						System.out.println(vo.getResponse());
						result = EnhancedReport.from("xml",vo.getResponse()).render("iz-report", null);
						JSONObject o = new JSONObject();
						o.put("type", "old");
						o.put("content", result);
						o.put("xml", vo.getResponse());
						o.put("time", time);
						response.setContentType("application/json");
						response.getOutputStream().print(o.toString());
					} catch (TransformerConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					//StringFormat sf = new StringFormat();
					//System.out.println(sf.format(vo.getResponse()));
					EnhancedReport er = EnhancedReport.from("json", vo.getResponse());
					JSONObject o = new JSONObject();
					o.put("type", "new");
					o.put("content", er.render("iz-report", null));
					o.put("xml", er.to("xml").toString());
					o.put("time", time);
					response.setContentType("application/json");
					response.getOutputStream().print(o.toString());
				}
			}
			else
				session.setAttribute("VO", new ValidationObject());
		}
		catch(Exception e){
			e.printStackTrace();
			response.setStatus(500);
		}
	}

}
