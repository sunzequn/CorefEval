package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnection;
import database.DBOperation;

public class markURI extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Object uidS =  req.getSession().getAttribute("uid");
		if (uidS == null) {
			resp.sendRedirect("../index.html");
		}
		int uid = (Integer)uidS;
		
		int cid = Integer.parseInt(req.getParameter("cid"));
		int isCorefed = Integer.parseInt(req.getParameter("isCorefed"));
		try {
			DBOperation.markURI(cid, uid, isCorefed);
			DBOperation.setProcess(uid, cid);
			cid++;
			if(!DBOperation.isValidCid(cid)) {
				cid--;
			}
			resp.sendRedirect("../mark.html?cid=" + cid);
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
		super.doPost(req, resp);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

}
