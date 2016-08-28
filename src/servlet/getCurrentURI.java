package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import uri.CurrentURI;
import database.DBConnection;
import database.DBOperation;

public class getCurrentURI extends HttpServlet{
	
	public void destroy() {
		super.destroy();
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
			
			try {
				int cid = Integer.parseInt(req.getParameter("cid"));
				int uid = (Integer) req.getSession().getAttribute("uid");
				if(cid == -1) {
					cid = DBOperation.getCidByProcess(uid);
				}
				CurrentURI curURI = DBOperation.getCurrentURI(uid, cid);
				JSONObject json = JSONObject.fromObject(curURI);
				resp.getOutputStream().print(new String(json.toString().getBytes("ISO-8859-1"),"UTF-8"));
				DBConnection.shutdown();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
