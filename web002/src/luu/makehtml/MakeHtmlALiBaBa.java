package luu.makehtml;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import luu.db.DbUtil;
import luu.http.HttpHelper;

import org.htmlparser.*;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.*;

/**
 * ����html�ļ� ����Ͱ�
 * 
 * @author luu
 * 
 */
public class MakeHtmlALiBaBa {

	private HttpHelper httpHelper = new HttpHelper();

	// ��������
	public static void main(String[] args) throws Exception {
		new MakeHtmlALiBaBa().excuteAll("�����", 1, 1);
	}

	// ִ�����еĲ���
	@SuppressWarnings("deprecation")
	public void excuteAll(String keywords, int startnum, int endnum) {
		MakeHtmlALiBaBa makeHtml = new MakeHtmlALiBaBa();
		// �����ؼ���ת��,�൱��asp��server.URLEncode
		String keywords_URLEncoder = URLEncoder.encode(keywords);
		keywords_URLEncoder = keywords_URLEncoder.replaceAll("%", "");
		try {
			makeHtml.begin(keywords, keywords_URLEncoder, startnum, endnum);
			makeHtml.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �ɼ�����
	public void begin(String keywords, String keywords_URLEncoder,
			int startnum, int endnum) throws Exception {
		String url = null;
		// ��ȡ����Ͱ� ��¼����
		url = "https://loginchina.alibaba.com/member/signin.htm";
		httpHelper.executePostMethod(url);
		String bodyString1 = httpHelper.getResponseBodyAsString();
		Parser parser = Parser.createParser(new String(bodyString1.getBytes(), "GBK"), "GBK");
		// ����form,��ȡ��¼��Ҫ�Ĳ���
		NodeList nodes_form = parser.extractAllNodesThatMatch(new TagNameFilter("form"));
		Node node_form = nodes_form.elementAt(0);
		
		NodeList nodes_form_children = node_form.getChildren();
		
		String _csrf_token = null;
		for (int i = 0; i < nodes_form_children.size(); i++) {
			Node nodes_form_children_o = nodes_form_children.elementAt(i);
			String node_html = nodes_form_children_o.toHtml();
			if(node_html.contains("name='_csrf_token' ")){
				_csrf_token = node_html.substring(node_html.indexOf("value='") + 7, node_html.length()-2);
				break;
			}
		}
		
		// ��¼����Ͱͣ���¼��������֤��Ĺ��ܣ�
		url = "https://loginchina.alibaba.com/member/signin.htm";
		String[][] data = { { "_csrf_token", _csrf_token },
				{ "action", "Signin" },
				{ "Done", "http://china.alibaba.com/" },
				{ "eventSubmitDoPost", "any" }, { "formSubmit", "Y" },
				{ "LoginId", "bxgtg" }, { "Password", "83632299" },
				{ "urlType", "" } };
		httpHelper.executePostMethod(url, data);
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = startnum; i < endnum + 1; i++) {
			url = "http://search.china.alibaba.com/newbuyoffer/-"
					+ keywords_URLEncoder + ".html?beginPage=" + i;
			httpHelper.executePostMethod(url);
			String bodyString = httpHelper.getResponseBodyAsString();
			this.extractTexts_alibaba(keywords, _csrf_token, bodyString, list);
			Thread.sleep(500);
			// this.connect(cname, uname, pwd);
			// this.disconnect();
			if (bodyString
					.contains("<span class=\"page-end\"><span>��һҳ</span></span>")) {
				break;
			}
		}
		this.deleteFromMySQL(keywords);
		this.saveToMySQL(list);
	}
	
	// �����ύ����
	public void tijiaoliuyan_alibaba(String bodyString) {
		try {
			if (!bodyString.contains("��ϵ�绰��<em class=\"fd-bold\">")) {
				Parser parser1 = Parser.createParser(new String(bodyString.getBytes(), "GBK"), "GBK");
				// ����form,��ȡ��¼��Ҫ�Ĳ���
				NodeList nodes_form = parser1.extractAllNodesThatMatch(new TagNameFilter("form"));
				int i = 0;
				// ���С�������֤�롱��ʱ�򣬾Ͷ��һ��form
				if(bodyString.contains("<em class=\"fd-bold\">������֤��</em>")){
					i = 1;
				}
				Node node_form = nodes_form.elementAt(i);
				NodeList nodes_form_children = node_form.getChildren();
				String buyOfferId = "";
				String toVaccountId = "";
				String _csrf_token = "";
				for (int m = 0; m < nodes_form_children.size(); m++) {
					String ss  = nodes_form_children.elementAt(m).toHtml();
					if(ss.contains("_csrf_token")){
						// ��ȡ_csrf_token
						Node nodes_input_0 = nodes_form_children.elementAt(m);
						String ss0 = nodes_input_0.toHtml();
						_csrf_token = ss0.substring(ss0.indexOf("value='") + 7, ss0.length()-2);
					}
					if(ss.contains("buyOfferId") && ss.contains("toVaccountId")){
						NodeList nodes_input = nodes_form_children.elementAt(m).getChildren();
						// ��ȡbuyOfferId
						Node nodes_input_1 = nodes_input.elementAt(1);
						String ss1 = nodes_input_1.toHtml();
						buyOfferId = ss1.substring(ss1.indexOf("value=\"") + 7, ss1.length()-3);
						// ��ȡtoVaccountId
						Node nodes_input_3 = nodes_input.elementAt(3);
						String ss3 = nodes_input_3.toHtml();
						toVaccountId = ss3.substring(ss3.indexOf("value=\"") + 7, ss3.length()-3);
						
						break;
					}
				}
				
				// ���հ���Ͱ����� ajax����
				String url = "http://caigou.china.alibaba.com/quotation/json/offer_leave_message.jsx?_input_charset=UTF-8&_csrf_token="
						   + _csrf_token + "&buyOfferId=" + buyOfferId + "&detailMessage=��������&toVaccountId=" + toVaccountId ;
				httpHelper.executePostMethod(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	// ִ�����еĲ���
	@SuppressWarnings("deprecation")
	public void excuteAll(String keywords) {
		MakeHtmlALiBaBa makeHtml = new MakeHtmlALiBaBa();
		// �����ؼ���ת��,�൱��asp��server.URLEncode
		String keywords_URLEncoder = URLEncoder.encode(keywords);
		keywords_URLEncoder = keywords_URLEncoder.replaceAll("%", "");
		try {
			makeHtml.begin(keywords, keywords_URLEncoder);
			makeHtml.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �ɼ�����
	public void begin(String keywords, String keywords_URLEncoder) throws Exception {
		String url = null;
		// ��ȡ����Ͱ� ��¼����
		url = "https://loginchina.alibaba.com/member/signin.htm";
		httpHelper.executePostMethod(url);
		String bodyString1 = httpHelper.getResponseBodyAsString();
		Parser parser = Parser.createParser(new String(bodyString1.getBytes(), "GBK"), "GBK");
		// ����form,��ȡ��¼��Ҫ�Ĳ���
		NodeList nodes_form = parser.extractAllNodesThatMatch(new TagNameFilter("form"));
		Node node_form = nodes_form.elementAt(0);
		
		NodeList nodes_form_children = node_form.getChildren();
		
		String _csrf_token = null;
		for (int i = 0; i < nodes_form_children.size(); i++) {
			Node nodes_form_children_o = nodes_form_children.elementAt(i);
			String node_html = nodes_form_children_o.toHtml();
			if(node_html.contains("name='_csrf_token' ")){
				_csrf_token = node_html.substring(node_html.indexOf("value='") + 7, node_html.length()-2);
				break;
			}
		}
		
		// ��¼����Ͱ�
		url = "https://loginchina.alibaba.com/member/signin.htm";
		String[][] data = { { "_csrf_token", _csrf_token },
				{ "action", "Signin" },
				{ "Done", "http://china.alibaba.com/" },
				{ "eventSubmitDoPost", "any" }, { "formSubmit", "Y" },
				{ "LoginId", "bxgtg" }, { "Password", "83632299" },
				{ "urlType", "" } };
		httpHelper.executePostMethod(url, data);
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 1; ;i++) {
			url = "http://search.china.alibaba.com/newbuyoffer/-"
					+ keywords_URLEncoder + ".html?beginPage=" + i;
			httpHelper.executePostMethod(url);
			String bodyString = httpHelper.getResponseBodyAsString();
			this.extractTexts_alibaba(keywords, _csrf_token, bodyString, list);
			Thread.sleep(500);
			// this.connect(cname, uname, pwd);
			// this.disconnect();
			if (bodyString
					.contains("<span class=\"page-end\"><span>��һҳ</span></span>")) {
				break;
			}
		}
		this.deleteFromMySQL(keywords);
		this.saveToMySQL(list);
	}
	
	// ɾ���ؼ����µ����е���Ϣ
	public void deleteFromMySQL(String keywords) {
//		new DbDao().deleteMessage(keywords);
	}

	// ��ȡ html ����
	public List<Map<String, String>> extractTexts_alibaba(String keywords, String _csrf_token, 
			String inputHtml, List<Map<String, String>> list) throws Exception {
		Parser parser = Parser.createParser(new String(inputHtml.getBytes(),
				"GBK"), "GBK");
		// ����h3
		NodeList nodes_div = parser.extractAllNodesThatMatch(new TagNameFilter(
				"div"));

		for (int i = 0; i < nodes_div.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();

			Node node_div = nodes_div.elementAt(i);
			String node_html = node_div.toHtml();
			NodeList nodes_div_children = node_div.getChildren();
			// ÿ����Ҫ����Ϣ��div�ж�
			if (node_html.startsWith("<div class=\"main-content fd-clr\">")
					&& node_html.contains("h3")
					&& node_html.contains("onmousedown=\"aliclick(this,")
					&& nodes_div_children.size() == 5) {
				String regEx = "\\s+"; // ȥ����ո� ����Ŀո��ʾ \t \n �ȵ�
				Pattern p = Pattern.compile(regEx);

				// ��ȡ����ͱ���ʱ��
				NodeList nodes_div_1_children = nodes_div_children.elementAt(1)
						.getChildren();
				String s1_1 = new String(nodes_div_1_children.elementAt(1)
						.toPlainTextString().getBytes("GBK"));
				String s1_3 = new String(nodes_div_1_children.elementAt(3)
						.toPlainTextString().getBytes("GBK"));
				Matcher m1_1 = p.matcher(s1_1);
				Matcher m1_3 = p.matcher(s1_3);
				s1_1 = m1_1.replaceAll(" ");
				s1_1 = s1_1.replaceAll("&nbsp;", "");
				s1_3 = m1_3.replaceAll(" ");
				s1_3 = s1_3.replaceAll("&nbsp;", "");
				// �ؼ���
				map.put("keywords", keywords);
				// ��ȡ����
				map.put("col1", s1_1);
				// ��ȡ����ʱ��
				map.put("col2", s1_3);

				// ��ȡ�ɹ�����Ϣ
				NodeList nodes_div_3_children = nodes_div_children.elementAt(3).getChildren();
				String s3_1 = new String(nodes_div_3_children.elementAt(1)
						.toPlainTextString().getBytes("GBK"));
				Matcher m3_1 = p.matcher(s3_1);
				s3_1 = m3_1.replaceAll(" ");
				s3_1 = s3_1.replaceAll("&nbsp;", "");
				map.put("col3", s3_1);

				// ��ȡdiv�µ�url
				String contain_url_string = nodes_div_1_children.elementAt(1).toHtml();
				String url = contain_url_string.substring(contain_url_string
						.indexOf("<a href=") + 9, contain_url_string
						.indexOf("\" onmousedown=\""));
				
				// �����ύ����
				httpHelper.executePostMethod(url);
				String bodyString1 = httpHelper.getResponseBodyAsString();
				this.tijiaoliuyan_alibaba(bodyString1);
				
				// ����div�µ�url������
				httpHelper.executePostMethod(url);
				String bodyString = httpHelper.getResponseBodyAsString();
				
				Parser parser2 = Parser.createParser(new String(bodyString.getBytes(), "GBK"), "GBK");
				// ����div
				NodeList nodes_div_1 = parser2
						.extractAllNodesThatMatch(new TagNameFilter("div"));
				String ss = null;
				int ff = 4;
				for (int m = 0; m < nodes_div_1.size(); m++) {
					Node node_div_1 = nodes_div_1.elementAt(m);
					String node_html_1 = node_div_1.toHtml();
					String ss_1 = "";
					String ss_2 = "";
					// ��ȡ��ϵ��ʽ
					if (node_html_1.startsWith("<div class=\"contact-title\" id=\"contact-show\">")) {
						ss = new String(node_div_1.toPlainTextString().getBytes("GBK"));
						String[] strings = ss.split("��ϵ�绰��");
						
						ss_1 = strings[0];
						// ����
						Matcher mm = p.matcher(ss_1);
						ss_1 = mm.replaceAll(" ");
						ss_1 = ss_1.replaceAll("��ϵ��ʽ��", "");
						ss_1 = ss_1.replaceAll("��ϵ�绰��", "");
						map.put("col7", ss_1);
						
						ss_2 = strings[1];
						// ��ϵ�绰
						mm = p.matcher(ss_2);
						ss_2 = mm.replaceAll(" ");
						ss_2 = ss_2.replaceAll("��ϵ��ʽ��", "");
						ss_2 = ss_2.replaceAll("��ϵ�绰��", "");
						map.put("col8", ss_2);
					}
					
					// һ��������Ϣ���ɹ���/��ϸҪ��/�ɹ�����Ϣ���ֱ���col4��col5��col6
					if (node_html_1.startsWith("<div class=\"block-content\">")) {
						ss = new String(node_div_1.toPlainTextString().getBytes("GBK"));
						Matcher mm = p.matcher(ss);
						ss = mm.replaceAll(" ");
						ss = ss.replaceAll("&nbsp;", "");
						map.put("col" + ff, ss);
						if(ff == 4){
							NodeList nodeList = node_div_1.getChildren().elementAt(1).getChildren();
							String aa = "";
							String ss1 = "";
							String ss1_1 = "";
							String ss1_2 = "";
							String ss2 = "";
							String ss3 = "";
							for (int y = 0; y < nodeList.size(); y ++ ){
								aa = nodeList.elementAt(y).toHtml();
								Matcher mm1 = p.matcher(aa);
								if (aa.contains("�ɹ�����")) {
									ss1 = mm1.replaceAll(" ");
									String[] ss1s = ss1.split("&nbsp;");
									
									ss1_1 = ss1s[0];
									ss1_1 = ss1_1.replace("&nbsp;", "");
									ss1_1 = ss1_1.replace("�ɹ�����", "");
									ss1_1 = ss1_1.replace("<div>", "");
									ss1_1 = ss1_1.replace("</div>", "");
									
									ss1_2 = ss1s[1];
									ss1_2 = ss1_2.replace("&nbsp;", "");
									ss1_2 = ss1_2.replace("�ɹ�����", "");
									ss1_2 = ss1_2.replace("<div>", "");
									ss1_2 = ss1_2.replace("</div>", "");
								}
								if (aa.contains("�ջ��أ�")) {
									ss2 = mm1.replaceAll(" ");
									ss2 = ss2.replace("&nbsp;", "");
									ss2 = ss2.replace("�ջ��أ�", "");
									ss2 = ss2.replace("<div>", "");
									ss2 = ss2.replace("</div>", "");
								}
								if (aa.contains("ѯ�۵���Ч�ڣ�")) {
									ss3 = mm1.replaceAll(" ");
									ss3 = ss3.replace("&nbsp;", "");
									ss3 = ss3.replace("ѯ�۵���Ч�ڣ�", "");
									ss3 = ss3.replace("<div>", "");
									ss3 = ss3.replace("</div>", "");
								}
							}
							map.put("col9", ss1_1);
							map.put("col10", ss1_2);
							map.put("col11", ss2);
							map.put("col12", ss3);
						}
						ff = ff + 1;
						if (ff == 7) {
							break;
						}
					}
				}
				list.add(map);
			}
		}
		return list;
	}

	// ��������
	public void end() throws Exception {
		httpHelper.releaseConnection();
	}

	// ���浽mysql���ݿ���
	public void saveToMySQL(List<Map<String, String>> list) {
		DbUtil dbUtil = new DbUtil();
		Connection con = dbUtil.conn;
		PreparedStatement st = null;
		try {
			st = con
					.prepareStatement("insert into t_message (col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12,keywords) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				st.setString(1, map.get("col1").toString());
				st.setString(2, map.get("col2").toString());
				st.setString(3, map.get("col3").toString());
				st.setString(4, map.get("col4").toString());
				st.setString(5, map.get("col5").toString());
				st.setString(6, map.get("col6").toString());
				st.setString(7, map.get("col7").toString());
				st.setString(8, map.get("col8").toString());
				st.setString(9, map.get("col9").toString());
				st.setString(10, map.get("col10").toString());
				st.setString(11, map.get("col11").toString());
				st.setString(12, map.get("col12").toString());
				st.setString(13, map.get("keywords").toString());

				if (st.executeUpdate() <= 0) {
					throw new Exception("�������ݴ���!");
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

	/**
	 * ���ӿ��
	 * 
	 * @param cname: �����������硰������ӡ�
	 * @param uname: ������ӵ��û���
	 * @param pwd: ������ӵ�����
	 */
	public void connect(String cname, String uname, String pwd) {
		try {
			Process p = Runtime.getRuntime().exec("rasdial.exe " + cname + " " + uname + " " + pwd);
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** �Ͽ���� */
	public void disconnect() {
		try {
			Process p = Runtime.getRuntime().exec("rasdial.exe /DISCONNECT");
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}