/**
 * 
 */
package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import uri.BaseURI;
import database.DBConnection;
import database.DBOperation;

/**
 * @author "Cunxin Jia"
 *
 */
public class getProcessOfUser extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			int uid = Integer.parseInt(req.getParameter("uid"));
			List<BaseURI> userBaseURIs = DBOperation.listBaseURIFromProcess(uid);
			List<BaseURI> allBaseURI = DBOperation.listAllBaseURI();
			List<BaseURI> hybridBaseURI = new ArrayList<BaseURI>();
			for(BaseURI baseURI : allBaseURI) {
				BaseURI curBase = baseURI;
				for(BaseURI userBaseURI : userBaseURIs) {
					if(userBaseURI.getBid() == baseURI.getBid()) {
						curBase = userBaseURI;
						break;
					}
				}
				hybridBaseURI.add(curBase);
			}
			JSONArray json = JSONArray.fromObject(hybridBaseURI);
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
		doGet(req, resp);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

}
