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

import database.DBConnection;
import database.DBOperation;

/**
 * @author "Cunxin Jia"
 *
 */
public class manageUser extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String op = req.getParameter("op");
		int uid = Integer.parseInt(req.getParameter("uid"));
//		int bid = Integer.parseInt(req.getParameter("bid"));
		List<Integer> bids = new ArrayList<Integer> ();
		String[] bidStrings = req.getParameter("bid").split(",");
		for(String bidString : bidStrings) {
			bids.add(Integer.parseInt(bidString));
		}
		try {
			if(op.equals("grant")) {
				for(Integer bid : bids) {
					DBOperation.addBaseToProcess(uid, bid);
				}
			}
			else if(op.equals("revoke")) {
				for(Integer bid : bids) {
					DBOperation.deleteBaseFromProcess(uid, bid);
				}
			}
			DBConnection.shutdown();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}

}
