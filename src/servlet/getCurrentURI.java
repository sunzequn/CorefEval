package servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;
import uri.CurrentURI;
import uri.FbUri2Mid;
import database.DBConnection;
import database.DBOperation;

public class getCurrentURI extends HttpServlet{
	
	private static final String PREFIX= "http://114.212.190.38:8892/describe/?url=";
	private static final String SUFFIX= "&sid=2&urilookup=1";
	
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
				int sumMarked = DBOperation.countCid(curURI.getCid());
				curURI.setSumMarked(sumMarked);
				
				if (curURI.getCurrentURI().contains("freebase")) {
					String mid = null;
					if (curURI.getCurrentURI().contains("/ns/m/")) {
						mid = StringUtils.replace(curURI.getCurrentURI(), "/m/", "/m.");
					} else {
						mid = FbUri2Mid.parseMid(curURI.getCurrentURI());
					}
	
					if (mid != null) {
						curURI.setCurrentFbMid(PREFIX + URLEncoder.encode(mid) + SUFFIX);
						DBOperation.insertMid(curURI.getCurrentURI(), mid);
					}
				}
				
				if (curURI.getBaseURI().contains("freebase")) {
					String mid = FbUri2Mid.parseMid(curURI.getBaseURI());
					if (mid != null) {
						curURI.setBaseMid(PREFIX + URLEncoder.encode(mid) + SUFFIX);
						DBOperation.insertMid(curURI.getBaseURI(), mid);
					}
				}
				
				System.out.println(curURI);
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
