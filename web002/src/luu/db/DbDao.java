package luu.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库的操作
 * @author lp
 */
public class DbDao {
	public Connection conn = null; 
	// 查询所有的车源列表
	public Map<String,Object> getCartList(int offset,int pageSize){
		Map<String,Object> m = new HashMap<String, Object>();
		conn = new DbUtil().conn;
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		PreparedStatement st = null;
		PreparedStatement st_cont = null;
		try {
			String sql ="select id,title,start_address,dest_address," +
						"link_man,link_tel,modify_time,content from t_che order by modify_time desc";
			st = conn.prepareStatement( sql + " limit " + offset + "," + pageSize);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString(1));
				map.put("title", rs.getString(2));
				map.put("start_address", rs.getString(3));
				map.put("dest_address", rs.getString(4));
				map.put("link_man", rs.getString(5));
				map.put("link_tel", rs.getString(6));
				map.put("modify_time", rs.getString(7));
				map.put("content", rs.getString(8));
				list.add(map);
			}
			m.put("dataList", list);
			
			st_cont = conn.prepareStatement("select count(id) from t_che");
			ResultSet rsSet = st_cont.executeQuery();
			while (rsSet.next()){
				m.put("count", rsSet.getString(1));
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (Exception ex) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return m;
	}

	//查询车源
	public Map<String,String> getCartById(int id){
		Map<String,String> map = new HashMap<String, String>();
		conn = new DbUtil().conn;
		PreparedStatement st = null;
		try {
			String sql ="select id,title,start_address,dest_address,link_man,link_tel,modify_time,content from t_che where id = " + id;
			st = conn.prepareStatement( sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				map.put("id", rs.getString(1));
				map.put("title", rs.getString(2));
				map.put("start_address", rs.getString(3));
				map.put("dest_address", rs.getString(4));
				map.put("link_man", rs.getString(5));
				map.put("link_tel", rs.getString(6));
				map.put("modify_time", rs.getString(7));
				map.put("content", rs.getString(8));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (Exception ex) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	
	public int delCartById(int id){
		conn = new DbUtil().conn;
		int flag = 0;
		String sql = "delete from t_che where id = ?";
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			flag = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	// 查询所有的货源列表
	public Map<String,Object> getSourceList(int offset,int pageSize){
		Map<String,Object> m = new HashMap<String, Object>();
		conn = new DbUtil().conn;
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		PreparedStatement st = null;
		PreparedStatement st_cont = null;
		try {
			st = conn.prepareStatement("select id,title,start_address,dest_address," +
					"link_man,link_tel,modify_time from t_huo order by modify_time desc limit " + offset + "," + pageSize);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString(1));
				map.put("title", rs.getString(2));
				map.put("start_address", rs.getString(3));
				map.put("dest_address", rs.getString(4));
				map.put("link_man", rs.getString(5));
				map.put("link_tel", rs.getString(6));
				map.put("modify_time", rs.getString(7));
				list.add(map);
			}
			m.put("dataList", list);
			
			st_cont = conn.prepareStatement("select count(id) from t_huo");
			ResultSet rsSet = st_cont.executeQuery();
			while (rsSet.next()){
				m.put("count", rsSet.getString(1));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (Exception ex) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return m;
	}
	
	public Map<String,String> getSourceById(int id){
		Map<String,String> map = new HashMap<String, String>();
		conn = new DbUtil().conn;
		PreparedStatement st = null;
		try {
			String sql ="select id,title,start_address,dest_address,link_man,link_tel,modify_time,content from t_huo where id = " + id;
			st = conn.prepareStatement( sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				map.put("id", rs.getString(1));
				map.put("title", rs.getString(2));
				map.put("start_address", rs.getString(3));
				map.put("dest_address", rs.getString(4));
				map.put("link_man", rs.getString(5));
				map.put("link_tel", rs.getString(6));
				map.put("modify_time", rs.getString(7));
				map.put("content", rs.getString(8));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (Exception ex) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	
	public int delSourceById(int id){
		conn = new DbUtil().conn;
		int flag = 0;
		String sql = "delete from t_huo where id = ?";
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			flag = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	// 插入一个配置信息
	public Map<String, String> getPzxx() {
		Map<String,String> map = new HashMap<String, String>();
		conn = new DbUtil().conn;
		String sql = "select url_che,url_huo,start_page_che,end_page_che,start_page_huo,end_page_huo,time_hh,time_mm,time_ss,id from t_pzxx";
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				map.put("url_che", rs.getString(1));
				map.put("url_huo", rs.getString(2));
				map.put("start_page_che", rs.getString(3));
				map.put("end_page_che", rs.getString(4));
				map.put("start_page_huo", rs.getString(5));
				map.put("end_page_huo", rs.getString(6));
				map.put("time_hh", rs.getString(7));
				map.put("time_mm", rs.getString(8));
				map.put("time_ss", rs.getString(9));
				map.put("id", rs.getString(10));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public int savePzxx(Map<String, String> map) {
		int flag = 0;
		DbUtil dbUtil = new DbUtil();
		Connection con = dbUtil.conn;
		PreparedStatement st = null;
		try {
			String sql = "update t_pzxx set start_page_huo=?,end_page_huo=?,end_page_che=?,start_page_che=?,"
				+ "url_huo=?,url_che=? where id = ?";
			st = con.prepareStatement(sql);
			st.setString(1, map.get("start_page_huo"));
			st.setString(2, map.get("end_page_huo"));
			st.setString(3, map.get("end_page_che"));
			st.setString(4, map.get("start_page_che"));
			st.setString(5, map.get("url_huo"));
			st.setString(6, map.get("url_che"));
			st.setString(7, map.get("id"));
			if(st.executeUpdate()>0){
				flag = 1;
			}else{
				flag = 0;
				System.out.println("保存数据错误!");
				throw new Exception("保存数据错误!");
			}
			st.clearParameters();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (Exception ex) {
				}
			}
			if (con != null) {
				dbUtil.closeConn();
			}
		}
		return flag;
	}
}
