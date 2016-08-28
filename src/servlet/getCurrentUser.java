/**
 * 
 */
package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author "Cunxin Jia"
 *
 */
public class getCurrentUser extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Object emailObject = req.getSession().getAttribute("email");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/HTML");
		if(emailObject != null) {
			String email = (String) emailObject;
			resp.getWriter().print(email);
		}
		else {
			resp.getWriter().print("");
		}		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

}
