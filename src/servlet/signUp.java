/**
 * 
 */
package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnection;
import database.DBOperation;

/**
 * @author "Cunxin Jia"
 *
 */
public class signUp extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		
		String email = req.getParameter("email");
		String passwd = req.getParameter("passwd");
		int uid = -1;
		try {
			uid = DBOperation.createNewUser(email, passwd);
			DBConnection.shutdown();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/HTML");
		
		if(uid != -1) {
			req.getSession().setAttribute("uid", uid);
			req.getSession().setAttribute("email", email);
		}
		resp.getOutputStream().print(uid);
	}

	@Override
	public void destroy() {
		super.destroy();
	}

}
