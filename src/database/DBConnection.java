package database;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

public class DBConnection {
	
	private static BasicDataSource ds = null;
	
	synchronized public static Connection getConnection() throws SQLException {
		if(ds == null) {
			init();
		}
		return ds.getConnection();
	}
	
	synchronized protected static void init() {
		ds = new BasicDataSource();
		
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl(Param.URL);
		ds.setUsername(Param.USER);
		ds.setPassword(Param.PASSWD);
		
		ds.setMaxActive(30);
		ds.setMinIdle(1);
		ds.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		
		ds.setTestOnBorrow(true);
	}
	
	synchronized public static void shutdown() throws Throwable{
		if (ds != null) {
			ds.close();
			ds = null;
		}
	}
}