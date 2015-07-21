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
public class MakeHtmlWx5656 {

	private HttpHelper httpHelper = new HttpHelper();

	// ��������
	public static void main(String[] args) throws Exception {
		MakeHtmlWx5656 makeHtmlWx5656 = new MakeHtmlWx5656();
		makeHtmlWx5656.insertIntoWxwl(new DbDao().getCartById(1));
		/*makeHtmlWx5656.deleteCart();
		makeHtmlWx5656.begin_che();
		makeHtmlWx5656.deleteSource();
		makeHtmlWx5656.begin_huo();
		makeHtmlWx5656.end();*/
	}

	public void run_data() {
		try {
			MakeHtmlWx5656 makeHtmlWx5656 = new MakeHtmlWx5656();
			makeHtmlWx5656.deleteCart();
			makeHtmlWx5656.begin_che();
			makeHtmlWx5656.deleteSource();
			makeHtmlWx5656.begin_huo();
			makeHtmlWx5656.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insertIntoWxwl(Map<String,String> map) {
		String title = map.get("title").toString();
		String start_address = map.get("start_address").toString();
		String dest_address = map.get("dest_address").toString();
		String link_man = map.get("link_man").toString();
		String link_tel = map.get("link_tel").toString();
		String content = map.get("content").toString();
		try {
			String url = " http://wxwl/che/publish";
			String[][] data = {
						{ "title", title },
						{ "pic", "filename=\"\" Content-Type: application/octet-stream" },
						{ "carType", "����" }, 
						{ "start_address", start_address },
						{ "dest_address", dest_address }, 
						{ "link_man", link_man },
						{ "link_tel", link_tel }, 
						{ "dest_city", "1" },
						{ "dest_dist", "0" },
						{ "dest_prov", "1" },
						{ "start_city", "1" },
						{ "start_dist", "0" },
						{ "start_prov", "1" },
						{ "dead_weight", "0" },
						{ "content", content }, 
						{ "cart_no", "" } 
					};
			httpHelper.executePostMethod(url, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �ɼ����� che
	public void begin_che() throws Exception {
		String url = null;

		// ��ȡҳ��
		url = "http://www.wx5656.com/che/index.asp?dprovince=&keywords=&type=?absolutepage=1";
		httpHelper.executePostMethod(url);
		String bodyString = httpHelper.getResponseBodyAsString();
		int sub1 = bodyString.indexOf("</option></select></TD>");
		String sub2 = bodyString.substring(sub1 - 20, sub1);
		sub2 = sub2.replace("bsolutepage=", "");
		int sub3 = sub2.indexOf("'>");
		int indexs = Integer.parseInt(sub2.substring(0, sub3));

		System.out.println(indexs);

		// ��ȡ����
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 1; i <= 4; i++) {
			url = "http://www.wx5656.com/che/index.asp?dprovince=&keywords=&type=?absolutepage="
					+ i;
			httpHelper.executePostMethod(url);
			bodyString = httpHelper.getResponseBodyAsString();
			extractTexts_wx5656_che(bodyString, list);

			System.out.println(list.size());

			Thread.sleep((int) (Math.random() * 4000));
		}

		saveToMySQL_che(list);
	}

	// �ɼ����� huo
	public void begin_huo() throws Exception {
		String url = null;
		String bodyString = null;

		// ��ȡ����
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 1; i <= 4; i++) {
			// url =
			// "http://www.wx5656.com/huo/info.asp?startplace=&endplace=&diqu=null&page="
			// + i;
			url = "http://www.wx5656.com/huo/info1.asp?startplace=&endplace=&diqu=null&page="
					+ i;
			httpHelper.executePostMethod(url);
			bodyString = httpHelper.getResponseBodyAsString();
			extractTexts_wx5656_huo(bodyString, list);

			System.out.println(list.size());
			if (!bodyString.contains(";\">��һҳ</a>")) {
				break;
			}

			// ��ͣ������
			Thread.sleep((int) (Math.random() * 4000));
		}

		saveToMySQL_huo(list);
	}

	// ��ȡ html ����
	public List<Map<String, String>> extractTexts_wx5656_che(String inputHtml,
			List<Map<String, String>> list) throws Exception {
		Parser parser = Parser.createParser(new String(inputHtml.getBytes(),
				"GBK"), "GBK");
		// ����table
		NodeList table_nodes = parser
				.extractAllNodesThatMatch(new TagNameFilter("table"));
		// ���ϵ�table ����
		String table_string = "<TABLE width=\"99%\" border=0 align=\"center\" cellPadding=0 cellSpacing=0>";
		// ���ϵ�td ����1
		String td_string1 = "<TD width=\"60%\" bgColor=\"#ffffff\" class=\"huoline\">";
		// ���ϵ�td ����2
		String td_string2 = "<TD width=\"60%\" bgColor=\"#f6f6f6\" class=\"huoline\">";
		String td_string3 = "��ϵ�ˣ�";
		String table_node_html = null;

		String td_string_3 = null;
		String td_string_5 = null;
		String td_string_7 = null;
		String td_string_9 = null;

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
					|| tr_node_html.contains(td_string3)) {
				tr_node_children = tr_node.getChildren();
				td_string_3 = new String(tr_node_children.elementAt(3)
						.toPlainTextString().getBytes("GBK"));
				td_string_5 = new String(tr_node_children.elementAt(5)
						.toPlainTextString().getBytes("GBK"));
				td_string_7 = new String(tr_node_children.elementAt(7)
						.toPlainTextString().getBytes("GBK"));
				td_string_9 = new String(tr_node_children.elementAt(9)
						.toPlainTextString().getBytes("GBK"));
				map = new HashMap<String, String>();
				td_string_3 = td_string_3.replaceAll("&nbsp;", "");
				map.put("td_string_3", td_string_3);
				map.put("td_string_5", td_string_5);
				map.put("td_string_7", td_string_7);
				map.put("td_string_9", td_string_9);

				list.add(map);
			}
		}
		return list;
	}

	// ��ȡ html ����
	public List<Map<String, String>> extractTexts_wx5656_huo(String inputHtml,
			List<Map<String, String>> list) throws Exception {
		Parser parser = Parser.createParser(new String(inputHtml.getBytes(),
				"GBK"), "GBK");
		// ����table
		NodeList table_nodes = parser
				.extractAllNodesThatMatch(new TagNameFilter("table"));
		Node table_node = table_nodes.elementAt(0);

		NodeList table_node_children = table_node.getChildren();
		String tr_string = "<tr oncontextmenu=\"return false\" ondragstart=\"return false\"";
		String tr_node_html = null;
		Map<String, String> map = null;

		String td_string_1 = null;
		String td_string_2 = null;
		String td_string_3 = null;

		// ����������table�µ�td�ڵ��·��ϵ�����
		NodeList tr_node_children = null;
		for (int i = 0; i < table_node_children.size(); i++) {
			Node tr_node = table_node_children.elementAt(i);
			tr_node_children = tr_node.getChildren();

			tr_node_html = tr_node.toHtml();

			if (tr_node_html.contains(tr_string)) {
				td_string_1 = new String(tr_node_children.elementAt(1)
						.toPlainTextString().getBytes("GBK"));
				td_string_2 = new String(tr_node_children.elementAt(2)
						.toPlainTextString().getBytes("GBK"));
				td_string_3 = new String(tr_node_children.elementAt(3)
						.toPlainTextString().getBytes("GBK"));
				map = new HashMap<String, String>();
				td_string_1 = td_string_1.replaceAll("&nbsp;", "");
				map.put("td_string_1", td_string_1);
				map.put("td_string_2", td_string_2);
				map.put("td_string_3", td_string_3);

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
				String s = map.get("td_string_3").toString();
				String[] ss = s.split("��ϵ�ˣ�");
				String title = "";
				String content = "";
				String link_man = "";
				String link_tel = "";
				if (ss.length == 2) {
					String tempTitle = ss[0];
					String[] splitTitle = tempTitle.split("˵����");
					if (splitTitle.length > 0) {
						if (splitTitle.length == 1) {
							title = splitTitle[0];
						} else if (splitTitle.length == 2) {
							title = splitTitle[0];
							content = splitTitle[1];
						}
					}

					String tempContact = ss[1];
					String[] splitContact = tempContact.split("��ϵ�绰��");
					if (splitContact.length >= 2) {
						link_man = splitContact[0];
						link_tel = splitContact[1];
					}
				}

				st.setString(1, title);
				st.setString(2, content);
				st.setString(3, link_man);
				st.setString(4, link_tel);
				st.setString(5, map.get("td_string_5").toString());
				st.setString(6, map.get("td_string_7").toString());
				st.setString(7, map.get("td_string_9").toString());
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
					.prepareStatement("insert into t_huo (title,link_man,link_tel,start_address,"
							+ "dest_address,modify_time) values(?,?,?,?,?,?)");
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				String s = map.get("td_string_1").toString();
				String[] ss = s.split("��ϵ�绰��");
				String title = "";
				String link_man = "";
				String link_tel = "";
				String start_address = "";
				String dest_address = "";
				if (ss.length == 2) {
					String tempTitle = ss[0];
					String splitTitle[] = tempTitle.split("��ϵ�ˣ�");
					if (splitTitle.length == 1) {
						title = splitTitle[0];
					} else if (splitTitle.length == 2) {
						title = splitTitle[0];
						link_man = splitTitle[1];
					}
					link_tel = ss[1];
				}

				String address = map.get("td_string_2").toString();
				String splitAddress[] = address.split("��");

				if (splitAddress.length == 2) {
					start_address = splitAddress[0];
					dest_address = splitAddress[1];
				}

				st.setString(1, title);
				st.setString(2, link_man);
				st.setString(3, link_tel);
				st.setString(4, start_address);
				st.setString(5, dest_address);
				st.setString(6, map.get("td_string_3").toString());

				if (!title.equals("")) {
					if (st.executeUpdate() <= 0) {
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