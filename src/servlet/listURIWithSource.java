/**
 * 
 */
package servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import uri.CorefedURI;
import database.DBConnection;
import database.DBOperation;

/**
 * @author "Cunxin Jia"
 *
 */
public class listURIWithSource extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			int bid = Integer.parseInt(req.getParameter("bid"));
			int uid = (Integer) req.getSession().getAttribute("uid");
			Map<String, List<CorefedURI>> URIs = DBOperation.getURIWithSource(uid, bid);
			Set<String> keySet = URIs.keySet();
			JSONArray keySetJSON = JSONArray.fromObject(keySet.toArray());
			JSONObject URIJSON = JSONObject.fromObject(URIs);
			JSONObject json = new JSONObject();
			json.put("sources", keySetJSON);
			json.put("URIs", URIJSON);
			resp.getOutputStream().print(new String(json.toString().getBytes("ISO-8859-1"),"UTF-8")); 
//			resp.getOutputStream().print(URLEncoder.encode(json.toString()));
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
		super.destroy();
	}

}
