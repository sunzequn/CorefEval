package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uri.BaseURI;
import uri.CorefedURI;
import uri.CurrentURI;
import user.User;

public class DBOperation {
	
	public static String getMid(String uri) throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "SELECT fbmid FROM fb WHERE fburi = '" + uri + "'";
		ResultSet rs = stmt.executeQuery(sql);
		String res = null;
		if(rs.next()) {
			res =  rs.getString(1);
		}
		conn.close();
		stmt.close();
		rs.close();
		return res;
	}
	
	public static int countCid(int cid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "SELECT COUNT(*) FROM evaluation WHERE cid = " + cid + " AND isCorefed <> 0";
//		String sql = "SELECT COUNT(*) FROM evaluation WHERE cid = " + cid;
		ResultSet rs = stmt.executeQuery(sql);
		int res = -1;
		if(rs.next()) {
			res =  rs.getInt(1);
		}
		conn.close();
		stmt.close();
		rs.close();
		return res;
	}
	
	public static List<BaseURI> listAllBaseURI() throws SQLException {
		List<BaseURI> baseURIList = new ArrayList <BaseURI>();
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select bid,URI,sumURI from base_uri";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			int bid = rs.getInt(1);
			String baseURI = rs.getString(2);
			int sum = rs.getInt(3);
			baseURIList.add(new BaseURI(bid, baseURI, 0, sum, 0));
		}
		conn.close();
		stmt.close();
		rs.close();
		return baseURIList;
	}
	
	public static List<BaseURI> listBaseURIFromProcess(int uid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select base_uri.bid,base_uri.URI,process.position,sumURI,cid " +
				"from corefed_uri,base_uri,process where base_uri.bid=corefed_uri.bid and " +
				"process.position=corefed_uri.position and base_uri.bid=process.bid and uid=" + uid;
		ResultSet rs = stmt.executeQuery(sql);
		List<BaseURI> baseURIList = new ArrayList <BaseURI>();
		while(rs.next()) {
			int bid = rs.getInt(1);
			String baseURI = rs.getString(2);
			int position = rs.getInt(3);
			int sum = rs.getInt(4);
			int cid = rs.getInt(5);
			baseURIList.add(new BaseURI(bid,baseURI,position,sum,cid));
		}
		conn.close();
		stmt.close();
		rs.close();
		return baseURIList;
	}
	
	public static ArrayList<String> listCorefedURI(int baseId) throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select * from corefed_uri where bid = " + baseId;
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList baseURIList = new ArrayList <String>();
		while(rs.next()) {
			baseURIList.add(rs.getString(2));
		}
		conn.close();
		stmt.close();
		rs.close();
		return baseURIList;
	}
	
	public static ArrayList<String> listSources() throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select * from source";
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList baseURIList = new ArrayList <String>();
		while(rs.next()) {
			baseURIList.add(rs.getString(2));
		}
		conn.close();
		stmt.close();
		rs.close();
		return baseURIList;
	}
	
	public static int getSourceId(String sourceName) throws SQLException {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("select sid from source where name = ?");
		int sid;
		pstmt.setString(1, sourceName);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			sid = rs.getInt(1);
		}
		else {
			pstmt = conn.prepareStatement("insert into source(name) values(?)");
			pstmt.setString(1, sourceName);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("select sid from source where name = ?");
			pstmt.setString(1, sourceName);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				sid = rs.getInt(1);
			}
			else {
				sid = -1;
			}
		}
		conn.close();
		pstmt.close();
		rs.close();
		return sid;
	}
	
	public static void reset() throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		
		stmt.addBatch("delete from process;");
		stmt.addBatch("delete from uri_source;");
		stmt.addBatch("delete from evaluation;");
		stmt.addBatch("delete from corefed_uri;");
		stmt.addBatch("delete from base_uri;");
		stmt.addBatch("delete from source;");
		stmt.addBatch("alter table corefed_uri AUTO_INCREMENT = 1;");
		stmt.addBatch("alter table base_uri AUTO_INCREMENT = 1;");
		stmt.addBatch("alter table source AUTO_INCREMENT = 1;");
		stmt.executeBatch();
		stmt.close();
		conn.close();
	}
	 
	public static ArrayList<String> listCorefedURIbySource(int baseId, int sourceId) throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select * from corefed_uri,uri_source where corefed_uri.bid=" + baseId + 
					" and corefed_uri.cid=uri_source.cid and uri_source.sid=" + sourceId + 
					" order by uri_source.position";
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList baseURIList = new ArrayList <String>();
		while(rs.next()) {
			baseURIList.add(rs.getString(2));
		}
		conn.close();
		stmt.close();
		rs.close();
		return baseURIList;
	}
	
	public static int validatePwd(String email, String pwd) throws SQLException {
		Connection conn = DBConnection.getConnection();
		PreparedStatement stmt = conn.prepareStatement("select uid from user where email=? and passwd=?");
		stmt.setString(1, email);
		stmt.setString(2, pwd);
		ResultSet rs = stmt.executeQuery();
		int uid = -1;
		if(rs.next()) {
			uid = rs.getInt(1);
		}
		conn.close();
		stmt.close();
		rs.close();
		return uid;
	}
	
	public static int createNewUser(String email, String pwd) throws SQLException {
		Connection conn = DBConnection.getConnection();
		PreparedStatement stmt = conn.prepareStatement("select uid from user where email=?");
		stmt.setString(1, email);		
		ResultSet rs = stmt.executeQuery();
		int uid = 0;
		if(rs.next()) {
			uid = -1;
		}
		else {
			stmt = conn.prepareStatement("insert into user(email,passwd) values(?,?)");
			stmt.setString(1, email);
			stmt.setString(2, pwd);
			stmt.executeUpdate();
			
			stmt = conn.prepareStatement("select uid from user where email=?");
			stmt.setString(1, email);		
			rs = stmt.executeQuery();
			if(rs.next()) {
				uid = rs.getInt(1);
				addAllBaseToProcess(uid);
			}
			else {
				uid = -2;
			}
		}
		conn.close();
		stmt.close();
		rs.close();
		return uid;
	}
	
	public static CurrentURI getCurrentURI(int uid, int cid) throws Exception {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "SELECT base_uri.bid,corefed_uri.URI,base_uri.URI,corefed_uri.position FROM corefed_uri,base_uri where corefed_uri.bid=base_uri.bid and cid=" + cid;
		ResultSet rs = stmt.executeQuery(sql);
		CurrentURI curURI = null;
		if(rs.next()) {
			int bid = rs.getInt(1);
			String corefedURI = rs.getString(2);
			String baseURI = rs.getString(3);
			int position = rs.getInt(4);
			int sum = getSumCorefedURI(bid);
			int isCorefed = isCorefed(uid, cid);
			boolean hasPrev = (isValidCid(cid-1))?true:false;
			boolean hasNext = (isValidCid(cid+1))?true:false;
			curURI = new CurrentURI(cid,bid,corefedURI,baseURI,position,sum,isCorefed,hasPrev,hasNext);
		}
		conn.close();
		stmt.close();
		rs.close();
		return curURI;
	}
	
	public static int getSumCorefedURI(int bid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select count(*) from corefed_uri where bid =" + bid;
		ResultSet rs = stmt.executeQuery(sql);
		int sum = 0;
		if(rs.next()) {
			sum = rs.getInt(1);			
		}
		conn.close();
		stmt.close();
		rs.close();
		return sum;
	}
	
	public static int isCorefed(int uid, int cid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select isCorefed from evaluation where cid =" + cid + " and uid=" + uid;
		ResultSet rs = stmt.executeQuery(sql);
		int sum = -100;
		if(rs.next()) {
			sum = rs.getInt(1);		
		} 
		conn.close();
		stmt.close();
		rs.close();
		return sum;
	}
	
	public static void markURI(int cid, int uid, int isCorefed) throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select count(*) from evaluation where uid=" + uid + " and cid=" + cid;
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()) {
//			if(rs.getInt(1) == 0 && isCorefed != 0) {
			if(rs.getInt(1) == 0 ) {
				sql = "insert into evaluation(uid,cid,isCorefed) values(?,?,?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, uid);
				pstmt.setInt(2, cid);
				pstmt.setInt(3, isCorefed);
				pstmt.executeUpdate();
			}
			else {
//				if(isCorefed == 0) {
//					sql = "delete from evaluation where uid=" + uid + " and cid=" + cid;
//					stmt.executeUpdate(sql);
//				}
//				else{
					sql = "update evaluation set isCorefed = " + isCorefed + " where uid=" + uid + " and cid=" + cid;
					stmt.executeUpdate(sql);
//				}
			}
		}
		conn.close();
		stmt.close();
		rs.close();
	}
	
	public static void setProcess(int uid, int cid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select bid,position from corefed_uri where cid =" + cid;
		ResultSet rs = stmt.executeQuery(sql);
		int bid = 0;
		int position = 0;
		if(rs.next()) {
			bid = rs.getInt(1);
			position = rs.getInt(2);
		}
		sql = "select count(*) from process where uid=" + uid + " and bid=" + bid;
		rs = stmt.executeQuery(sql);
		if(rs.next()) {
			if(rs.getInt(1) == 0) {
				sql = "insert into process(uid,bid,position) values(?,?,?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, uid);
				pstmt.setInt(2, bid);
				pstmt.setInt(3, position);
				pstmt.executeUpdate();
			}
			else {
				sql = "update process set position = " + position + " where uid=" + uid + " and bid=" + bid;
				stmt.executeUpdate(sql);
			}
		}
		conn.close();
		stmt.close();
		rs.close();
	}
	
	public static int getCidByProcess(int uid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select cid from corefed_uri,process where corefed_uri.bid=process.bid and corefed_uri.position=process.position and uid=" + uid;
		ResultSet rs = stmt.executeQuery(sql);
		int cid = 0;
		while(rs.next()) {
			cid = rs.getInt(1);	
		}
		conn.close();
		stmt.close();
		rs.close();
		return cid;
	}
	
	public static boolean isValidCid(int cid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "select count(*) from corefed_uri where cid=" + cid;
		ResultSet rs = stmt.executeQuery(sql);
		boolean isValidCid = false;
		if(rs.next()) {
			int count = rs.getInt(1);
			if(count == 0) {
				isValidCid =  false;
			}
			else 
				isValidCid = true;
		}
		conn.close();
		stmt.close();
		rs.close();
		return isValidCid;
	}
	
	public static void addAllBaseToProcess(int uid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		PreparedStatement stmt = conn.prepareStatement("select bid from base_uri");
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			int bid = rs.getInt(1);
			stmt = conn.prepareStatement("insert into process(uid,bid,position) values (?,?,?)");
			stmt.setInt(1, uid);
			stmt.setInt(2, bid);
			stmt.setInt(3, 1);
			stmt.executeUpdate();
		}
		conn.close();
		stmt.close();
		rs.close();
	}
	
	public static void addBaseToProcess(int uid, int bid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		PreparedStatement stmt = conn.prepareStatement("select bid from base_uri");
		stmt = conn.prepareStatement("insert ignore into process(uid,bid,position) values (?,?,?)");
		stmt.setInt(1, uid);
		stmt.setInt(2, bid);
		stmt.setInt(3, 1);
		stmt.executeUpdate();
		conn.close();
		stmt.close();
	}
	
	public static void deleteBaseFromProcess(int uid, int bid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		PreparedStatement stmt = conn.prepareStatement("select bid from base_uri");
		stmt = conn.prepareStatement("delete from process where uid = ? and bid = ?");
		stmt.setInt(1, uid);
		stmt.setInt(2, bid);
		stmt.executeUpdate();
		conn.close();
		stmt.close();
	}
	
	
	public static Map<String, List<CorefedURI>> getURIWithSource(int uid, int bid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		PreparedStatement stmt = conn.prepareStatement("SELECT corefed_uri.cid,corefed_uri.uri,source.name FROM source,corefed_uri,uri_source where corefed_uri.cid = uri_source.cid and source.sid = uri_source.sid and corefed_uri.bid = ? order by uri_source.sid,uri_source.position");
		stmt.setInt(1, bid);
		ResultSet rs = stmt.executeQuery();
		Map<String, List<CorefedURI>> URIMap = new HashMap<String, List<CorefedURI>> ();
		while(rs.next()) {
			int cid = rs.getInt(1);			
			String uri = rs.getString(2);
			String source = rs.getString(3);
			int isCorefed = isCorefed(uid, cid);			
			CorefedURI curURI = new CorefedURI(cid, uri, isCorefed);
			int sumMarked = DBOperation.countCid(cid);
			curURI.setSumMarked(sumMarked);
			List<CorefedURI> URIList = URIMap.get(source);
			if(URIList == null) {
				URIList = new ArrayList<CorefedURI>();
			}
			URIList.add(curURI);
			URIMap.put(source, URIList);
		}
		conn.close();
		stmt.close();
		rs.close();
		return URIMap;
	}
	
	public static List<User> listUser() throws SQLException {
		Connection conn = DBConnection.getConnection();
		Statement stmt = conn.createStatement();
		List<User> userList = new ArrayList<User>();
		String sql = "select uid,email from user";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			int uid = rs.getInt(1);
			String email = rs.getString(2);
			User user = new User(uid, email);
			userList.add(user);
		}
		conn.close();
		stmt.close();
		rs.close();
		return userList;
	}
	
	public static int insertMid(String uri, String mid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("select fburi from fb where fburi = ?");
		pstmt.setString(1, uri);
		ResultSet rs = pstmt.executeQuery();
		int bid;
		if(rs.next()) {
			bid = 0;
		}
		else {
			pstmt = conn.prepareStatement("insert into fb(fburi, fbmid) values(?,?)");
			pstmt.setString(1, uri);
			pstmt.setString(2, mid);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement("SELECT LAST_INSERT_ID();");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				bid = rs.getInt(1);
			}
			else {
				bid = -1;
			}
		}
		conn.close();
		pstmt.close();
		rs.close();
		return bid;
	}
	
	public static int insertBaseURI(String baseURI) throws SQLException {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("select bid from base_uri where URI = ?");
		pstmt.setString(1, baseURI);
		ResultSet rs = pstmt.executeQuery();
		int bid;
		if(rs.next()) {
			bid = rs.getInt(1);
		}
		else {
			pstmt = conn.prepareStatement("insert into base_uri(URI) values(?)");
			pstmt.setString(1, baseURI);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement("SELECT LAST_INSERT_ID();");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				bid = rs.getInt(1);
			}
			else {
				bid = -1;
			}
		}
		conn.close();
		pstmt.close();
		rs.close();
		return bid;
	}
	
	public static void updateSumURI(int bid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("select max(position) from corefed_uri where bid = ?");
		pstmt.setInt(1, bid);
		int sum = 0;
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			sum = rs.getInt(1);
		}
		pstmt = conn.prepareStatement("update base_uri set sumURI = ? where bid = ?");
		pstmt.setInt(1, sum);
		pstmt.setInt(2, bid);
		pstmt.executeUpdate();
		conn.close();
		pstmt.close();
		rs.close();
	}
	
	public static int insertCorefedURI(String corefedURI, int bid) throws SQLException {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("select cid from corefed_uri where bid = ? and URI = ?");
		pstmt.setInt(1, bid);
		pstmt.setString(2, corefedURI);
		ResultSet rs = pstmt.executeQuery();
		int cid;
		if(rs.next()) {
			cid = rs.getInt(1);
		}
		else {
			pstmt = conn.prepareStatement("select max(position) from corefed_uri where bid = ?");
			pstmt.setInt(1, bid);
			rs = pstmt.executeQuery();
			int position = 1;
			if(rs.next()) {
				position = rs.getInt(1) + 1;
			}
			pstmt = conn.prepareStatement("insert into corefed_uri(URI, bid, position) values(?,?,?)");
			pstmt.setString(1, corefedURI);
			pstmt.setInt(2, bid);
			pstmt.setInt(3, position);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement("SELECT LAST_INSERT_ID();");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				cid = rs.getInt(1);
			}
			else {
				cid = -1;
			}
		}
		conn.close();
		pstmt.close();
		rs.close();
		return cid;
	}
	
	public static void insertURISource(int cid, int sid, int position) throws SQLException {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("insert into uri_source(cid, sid, position) values(?,?,?)");
		pstmt.setInt(1, cid);
		pstmt.setInt(2, sid);
		pstmt.setInt(3, position);
		pstmt.executeUpdate();
		conn.close();
		pstmt.close();
	}
}
