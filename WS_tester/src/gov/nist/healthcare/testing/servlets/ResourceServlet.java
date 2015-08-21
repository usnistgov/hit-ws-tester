package gov.nist.healthcare.testing.servlets;

import gov.nist.healthcare.testing.helpers.BatchHandler;
import gov.nist.healthcare.testing.model.LoadedResources;
import gov.nist.healthcare.testing.model.Resources;
import gov.nist.healthcare.testing.model.ValidationObject;
import gov.nist.healthcare.testing.wsclient.HL7V2WS;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ResourceServlet
 */
@WebServlet("/resources")
public class ResourceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResourceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String ep = "";
		if(session.getAttribute("EP") != null)
			ep = (String) session.getAttribute("EP");
		session.setAttribute("VO", new ValidationObject());
		try{
			session.setAttribute("WS", HL7V2WS.getInstance(ep));
		}
		catch(Exception e){
			
		}
		
		session.setAttribute("LastAction", "resources");
		
		
		if(session.getAttribute("RS") == null){
			Resources rs = new Resources();
			rs.init(this.getServletContext().getRealPath("properties"));
			session.setAttribute("RS", rs);
		}
		if(session.getAttribute("BH") == null){
			BatchHandler bh = new BatchHandler(UUID.randomUUID().toString(),this.getServletContext().getRealPath("/"),(HL7V2WS) session.getAttribute("WS"));
			session.setAttribute("BH", bh);
		}
		if(session.getAttribute("LR") == null){
			session.setAttribute("LR", new LoadedResources());
		}
			
		
		Resources rs = (Resources) session.getAttribute("RS");

		//System.out.println(test);
		response.setContentType("application/json");
		response.getOutputStream().print(rs.getResources());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
