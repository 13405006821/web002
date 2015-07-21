package luu.makehtml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luu.db.DbDao;
import luu.db.DbUtil;
import luu.http.HttpHelper;

import org.htmlparser.*;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.*;

/**
 * ����html�ļ� wx5656
 * 
 * @author luu
 * 
 */
public class MakeHtmlChinaWuLiu {

	private HttpHelper httpHelper = new HttpHelper();

	// ��������
	public static void main(String[] args) throws Exception {
		MakeHtmlChinaWuLiu chinaWuLiu = new MakeHtmlChinaWuLiu();
		chinaWuLiu.deleteCart();
		chinaWuLiu.begin_che("http://www.chinawuliu.cn/?type=2",1,4);
		chinaWuLiu.deleteSource();
		chinaWuLiu.begin_huo("http://www.chinawuliu.cn/?type=1",1,4);
		chinaWuLiu.end();
	}

	public void run_data() {
		try {
			DbDao dbDao = new DbDao();
			Map<String, String> pzxx = dbDao.getPzxx();
			MakeHtmlChinaWuLiu chinaWuLiu = new MakeHtmlChinaWuLiu();
			chinaWuLiu.deleteCart();
			String url_che = pzxx.get("url_che").toString();
			String url_huo = pzxx.get("url_huo").toString();
			String start_page_che = pzxx.get("start_page_che").toString() == null ? "1" : pzxx.get("start_page_che").toString() ;
			String end_page_che = pzxx.get("end_page_che").toString() == null ? "1" : pzxx.get("end_page_che").toString() ;
			String start_page_huo = pzxx.get("start_page_huo").toString() == null ? "1" : pzxx.get("start_page_huo").toString() ;
			String end_page_huo = pzxx.get("end_page_huo").toString() == null ? "1" : pzxx.get("end_page_huo").toString() ;
			chinaWuLiu.begin_che(url_che, Integer.parseInt(start_page_che), Integer.parseInt(end_page_che));
			chinaWuLiu.deleteSource();
			chinaWuLiu.begin_huo(url_huo, Integer.parseInt(start_page_huo), Integer.parseInt(end_page_huo));
			chinaWuLiu.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �ɼ����� che
	public int begin_che(String url, int start_page_che, int end_page_che)  {
		int flag = 0 ;
		try {
			httpHelper.executePostMethod(url);
			String bodyString = httpHelper.getResponseBodyAsString();

			// ��ȡ����
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (int i = start_page_che; i <= end_page_che; i++) {
				url = url+"&Page=" + i;
				httpHelper.executePostMethod(url);
				bodyString = httpHelper.getResponseBodyAsString();
				extractTexts_chinawl(bodyString, list);
				System.out.println(list.size());
				Thread.sleep((int) (Math.random() * 6000));
			}
			saveToMySQL_che(list);
			flag = 1;
		} catch (Exception e) {
			flag = 0 ;
			e.printStackTrace();
		}
		
		return flag;
	}

	// �ɼ����� huo
	public int begin_huo(String url, int start_page_huo, int end_page_huo) {
		int flag = 0;
		try {
			httpHelper.executePostMethod(url);
			String bodyString = httpHelper.getResponseBodyAsString();

			// ��ȡ����
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (int i = start_page_huo; i <= end_page_huo; i++) {
				url = url+"&Page=" + i;
				httpHelper.executePostMethod(url);
				bodyString = httpHelper.getResponseBodyAsString();
				extractTexts_chinawl(bodyString, list);

				System.out.println(list.size());

				Thread.sleep((int) (Math.random() * 6000));
			}
			saveToMySQL_huo(list);
			flag = 1;
		} catch (Exception e) {
			flag = 0;
			e.printStackTrace();
		}
		return flag;
	}
	
	// ��ȡ html ����
	public List<Map<String, String>> extractTexts_chinawl(String inputHtml,
			List<Map<String, String>> list) throws Exception {
		Parser parser = Parser.createParser(new String(inputHtml.getBytes(),
				"GBK"), "GBK");
		// ����table
		NodeList table_nodes = parser
				.extractAllNodesThatMatch(new TagNameFilter("table"));
		// ���ϵ�table ����
		String table_string = "<table width=\"100%\" border=\"0\" cellspacing=\"2\" cellpadding=\"5\" align=\"center\" bgcolor=\"#D8E4FE\" >";
		// ���ϵ�td ����
		String td_string1 = "<td style=\"BORDER-BOTTOM: #cccccc 1px dashed;\">";
		String td_string2 = "<td width=\"150\" style=\"BORDER-BOTTOM: #cccccc 1px dashed;\">";
		String td_string3 = "<td width=\"140\" style=\"BORDER-BOTTOM: #cccccc 1px dashed;\">";
		String td_string4 = "<span class=\"tel\">";
		String table_node_html = null;

		String string_�������ϵ��ʽ = null;
		String string_�����غ�Ŀ�ĵ� = null;
		String string_���� = null;
		String string_���� = null;
		String string_��ϵ��ʽ = null;

		NodeList table_node_children = null;
		// ����������table�µ�td�ڵ�
		for (int i = 0; i < table_nodes.size(); i++) {
			Node table_node = table_nodes.elementAt(i);
			table_node_html = table_node.toHtml();
			if (table_node_html.startsWith(table_string)) {
				table_node_children = table_nodes.elementAt(i).getChildren();
				break;
			}
		}

		Map<String, String> map = null;
		// ����������table�µ�td�ڵ��·��ϵ�����
		String tr_node_html = null;
		NodeList tr_node_children = null;
		for (int i = 0; i < table_node_children.size(); i++) {
			Node tr_node = table_node_children.elementAt(i);
			tr_node_html = tr_node.toHtml();
			if (tr_node_html.contains(td_string1)
					|| tr_node_html.contains(td_string2)
					|| tr_node_html.contains(td_string3) || tr_node_html.contains(td_string4)) {
				tr_node_children = tr_node.getChildren();
				string_�������ϵ��ʽ = new String(tr_node_children.elementAt(3).toPlainTextString().getBytes("GBK")); 
				string_�����غ�Ŀ�ĵ� = new String(tr_node_children.elementAt(5).toPlainTextString().getBytes("GBK"));
				string_���� = new String(tr_node_children.elementAt(7).toPlainTextString().getBytes("GBK"));
				map = new HashMap<String, String>();
				string_���� = string_�������ϵ��ʽ;
				string_��ϵ��ʽ = string_�������ϵ��ʽ;
				map.put("title", string_����);
				map.put("link_tel", string_��ϵ��ʽ);
				map.put("point", string_�����غ�Ŀ�ĵ�);
				map.put("modify_time", string_����);
				list.add(map);
			}
		}
		return list;
	}
	
	// ���浽mysql���ݿ���
	public void saveToMySQL_che(List<Map<String, String>> list) {
		DbUtil dbUtil = new DbUtil();
		Connection con = dbUtil.conn;
		PreparedStatement st = null;
		try {
			st = con
					.prepareStatement("insert into t_che (title,content,link_man,link_tel,"
							+ "start_address,dest_address,modify_time) values(?,?,?,?,?,?,?)");
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				String title = map.get("title").toString();
				String start_address = "" ;
				String dest_address = "" ;
				String point = map.get("point").toString();
				String splitPoint[] = point.split("��");
				String content = " ";
				String link_man = " ";
				String link_tel = map.get("link_tel").toString();
				if(splitPoint.length==1){
					start_address = splitPoint[0];
				}
				if(splitPoint.length== 2) {
					start_address = splitPoint[0];
					dest_address = splitPoint[1] ;
				}
				String modify_time = map.get("modify_time").toString() ;
				st.setString(1, title);
				st.setString(2, content);
				st.setString(3, link_man);
				st.setString(4, link_tel);
				st.setString(5, start_address);
				st.setString(6, dest_address);
				st.setString(7, modify_time);
				if (!title.equals("")) {
					if (st.executeUpdate() <= 0) {
						System.out.println("�������ݴ���!");
						throw new Exception("�������ݴ���!");
					}
				}
				st.clearParameters();
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
			if (con != null) {
				dbUtil.closeConn();
			}
		}
	}
	
	// ���浽mysql���ݿ���
	public void saveToMySQL_huo(List<Map<String, String>> list) {
		DbUtil dbUtil = new DbUtil();
		Connection con = dbUtil.conn;
		PreparedStatement st = null;
		try {
			st = con
					.prepareStatement("insert into t_huo (title,content,link_man,link_tel,"
							+ "start_address,dest_address,modify_time) values(?,?,?,?,?,?,?)");
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				String title = map.get("title").toString();
				String start_address = "" ;
				String dest_address = "" ;
				String point = map.get("point").toString();
				String splitPoint[] = point.split("��");
				String content = " ";
				String link_man = " ";
				String link_tel = map.get("link_tel").toString();
				if(splitPoint.length==1){
					start_address = splitPoint[0];
				}
				if(splitPoint.length== 2) {
					start_address = splitPoint[0];
					dest_address = splitPoint[1] ;
				}
				String modify_time = map.get("modify_time").toString() ;
				st.setString(1, title);
				st.setString(2, content);
				st.setString(3, link_man);
				st.setString(4, link_tel);
				st.setString(5, start_address);
				st.setString(6, dest_address);
				st.setString(7, modify_time);
				if (!title.equals("")) {
					if (st.executeUpdate() <= 0) {
						System.out.println("�������ݴ���!");
						throw new Exception("�������ݴ���!");
					}
				}
				st.clearParameters();
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
			if (con != null) {
				dbUtil.closeConn();
			}
		}
	}

	// ɾ��һ���ؼ����µ�������Ϣ
	public int deleteCart() {
		DbUtil dbUtil = new DbUtil();
		Connection con = dbUtil.conn;
		int flag = 0;
		String sql = "truncate table t_che";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			flag = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	// ɾ��һ���ؼ����µ�������Ϣ
	public int deleteSource() {
		DbUtil dbUtil = new DbUtil();
		Connection con = dbUtil.conn;
		int flag = 0;
		String sql = "truncate table t_huo";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			flag = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	// ��������
	public void end() throws Exception {
		httpHelper.releaseConnection();
	}
}