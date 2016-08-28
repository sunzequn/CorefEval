package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import uri.BaseURI;
import database.DBConnection;
import database.DBOperation;

public class listBaseURI extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Object uidObject = req.getSession().getAttribute("uid");
		if(uidObject == null) {
			resp.sendRedirect("../index.html");
			return;
		}
		int uid = (Integer) uidObject;
		try {
			List<BaseURI> baseURIList = DBOperation.listBaseURIFromProcess(uid);
			JSONArray json = JSONArray.fromObject(baseURIList);
			resp.getOutputStream().print(json.toString());
			DBConnection.shutdown();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

}
