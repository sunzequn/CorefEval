package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONFunction;
import net.sf.json.JSONObject;

import uri.BaseURI;
import uri.CurrentURI;

import database.DBOperation;

public class test extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
			resp.getOutputStream().println("test");
	}

	public static void main(String[] args) {
		

			try {
				
				//List<BaseURI> baseURIList = DBOperation.listBaseURI(1);
				//JSONArray jsonArray = JSONArray.fromObject(baseURIList);
				//JSONObject json = JSONObject.fromObject(baseURIList);
//				System.out.println(DBOperation.listUser().toString());
				DBOperation.deleteBaseFromProcess(11, 2);
//				DBOperation.addNewProcess(2);
//				DBOperation.setProcess(1, 3);
//				System.out.println(DBOperation.isCorefed(1, 2));
//				DBOperation.markURI(1, 1, 0);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

	}
}
