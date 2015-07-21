package luu.db;

import java.io.InputStream;
import java.sql.*;

import java.util.Properties;
/**
 * 数据库连接
 * @author lp
 */
public class DbUtil {
	public Connection conn;

	public DbUtil() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Properties ps = new Properties();
			InputStream is = DbUtil.this.getClass().getClassLoader().getResourceAsStream("config.properties");
			ps.load(is);
			is.close();
			String url = ps.getProperty("url");
			String user = ps.getProperty("user");
			String pwd = ps.getProperty("pwd"); 
			conn = DriverManager.getConnection(url, user, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeConn(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
