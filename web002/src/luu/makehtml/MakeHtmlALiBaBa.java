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
 * 处理html文件 阿里巴巴
 * 
 * @author luu
 * 
 */
public class MakeHtmlALiBaBa {

	private HttpHelper httpHelper = new HttpHelper();

	// 单例测试
	public static void main(String[] args) throws Exception {
		new MakeHtmlALiBaBa().excuteAll("不锈钢", 1, 1);
	}

	// 执行所有的操作
	@SuppressWarnings("deprecation")
	public void excuteAll(String keywords, int startnum, int endnum) {
		MakeHtmlALiBaBa makeHtml = new MakeHtmlALiBaBa();
		// 搜索关键字转码,相当于asp中server.URLEncode
		String keywords_URLEncoder = URLEncoder.encode(keywords);
		keywords_URLEncoder = keywords_URLEncoder.replaceAll("%", "");
		try {
			makeHtml.begin(keywords, keywords_URLEncoder, startnum, endnum);
			makeHtml.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 采集数据
	public void begin(String keywords, String keywords_URLEncoder,
			int startnum, int endnum) throws Exception {
		String url = null;
		// 获取阿里巴巴 登录参数
		url = "https://loginchina.alibaba.com/member/signin.htm";
		httpHelper.executePostMethod(url);
		String bodyString1 = httpHelper.getResponseBodyAsString();
		Parser parser = Parser.createParser(new String(bodyString1.getBytes(), "GBK"), "GBK");
		// 遍历form,获取登录需要的参数
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
		
		// 登录阿里巴巴（登录新增了验证码的功能）
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
					.contains("<span class=\"page-end\"><span>下一页</span></span>")) {
				break;
			}
		}
		this.deleteFromMySQL(keywords);
		this.saveToMySQL(list);
	}
	
	// 仿照提交留言
	public void tijiaoliuyan_alibaba(String bodyString) {
		try {
			if (!bodyString.contains("联系电话：<em class=\"fd-bold\">")) {
				Parser parser1 = Parser.createParser(new String(bodyString.getBytes(), "GBK"), "GBK");
				// 遍历form,获取登录需要的参数
				NodeList nodes_form = parser1.extractAllNodesThatMatch(new TagNameFilter("form"));
				int i = 0;
				// 当有“输入验证码”的时候，就多出一个form
				if(bodyString.contains("<em class=\"fd-bold\">输入验证码</em>")){
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
						// 获取_csrf_token
						Node nodes_input_0 = nodes_form_children.elementAt(m);
						String ss0 = nodes_input_0.toHtml();
						_csrf_token = ss0.substring(ss0.indexOf("value='") + 7, ss0.length()-2);
					}
					if(ss.contains("buyOfferId") && ss.contains("toVaccountId")){
						NodeList nodes_input = nodes_form_children.elementAt(m).getChildren();
						// 获取buyOfferId
						Node nodes_input_1 = nodes_input.elementAt(1);
						String ss1 = nodes_input_1.toHtml();
						buyOfferId = ss1.substring(ss1.indexOf("value=\"") + 7, ss1.length()-3);
						// 获取toVaccountId
						Node nodes_input_3 = nodes_input.elementAt(3);
						String ss3 = nodes_input_3.toHtml();
						toVaccountId = ss3.substring(ss3.indexOf("value=\"") + 7, ss3.length()-3);
						
						break;
					}
				}
				
				// 仿照阿里巴巴留言 ajax交互
				String url = "http://caigou.china.alibaba.com/quotation/json/offer_leave_message.jsx?_input_charset=UTF-8&_csrf_token="
						   + _csrf_token + "&buyOfferId=" + buyOfferId + "&detailMessage=东西不错&toVaccountId=" + toVaccountId ;
				httpHelper.executePostMethod(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	// 执行所有的操作
	@SuppressWarnings("deprecation")
	public void excuteAll(String keywords) {
		MakeHtmlALiBaBa makeHtml = new MakeHtmlALiBaBa();
		// 搜索关键字转码,相当于asp中server.URLEncode
		String keywords_URLEncoder = URLEncoder.encode(keywords);
		keywords_URLEncoder = keywords_URLEncoder.replaceAll("%", "");
		try {
			makeHtml.begin(keywords, keywords_URLEncoder);
			makeHtml.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 采集数据
	public void begin(String keywords, String keywords_URLEncoder) throws Exception {
		String url = null;
		// 获取阿里巴巴 登录参数
		url = "https://loginchina.alibaba.com/member/signin.htm";
		httpHelper.executePostMethod(url);
		String bodyString1 = httpHelper.getResponseBodyAsString();
		Parser parser = Parser.createParser(new String(bodyString1.getBytes(), "GBK"), "GBK");
		// 遍历form,获取登录需要的参数
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
		
		// 登录阿里巴巴
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
					.contains("<span class=\"page-end\"><span>下一页</span></span>")) {
				break;
			}
		}
		this.deleteFromMySQL(keywords);
		this.saveToMySQL(list);
	}
	
	// 删除关键字下的所有的信息
	public void deleteFromMySQL(String keywords) {
//		new DbDao().deleteMessage(keywords);
	}

	// 提取 html 解析
	public List<Map<String, String>> extractTexts_alibaba(String keywords, String _csrf_token, 
			String inputHtml, List<Map<String, String>> list) throws Exception {
		Parser parser = Parser.createParser(new String(inputHtml.getBytes(),
				"GBK"), "GBK");
		// 遍历h3
		NodeList nodes_div = parser.extractAllNodesThatMatch(new TagNameFilter(
				"div"));

		for (int i = 0; i < nodes_div.size(); i++) {

			Map<String, String> map = new HashMap<String, String>();

			Node node_div = nodes_div.elementAt(i);
			String node_html = node_div.toHtml();
			NodeList nodes_div_children = node_div.getChildren();
			// 每个需要的信息的div判断
			if (node_html.startsWith("<div class=\"main-content fd-clr\">")
					&& node_html.contains("h3")
					&& node_html.contains("onmousedown=\"aliclick(this,")
					&& nodes_div_children.size() == 5) {
				String regEx = "\\s+"; // 去多个空格 这里的空格表示 \t \n 等等
				Pattern p = Pattern.compile(regEx);

				// 获取标题和报价时间
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
				// 关键字
				map.put("keywords", keywords);
				// 获取标题
				map.put("col1", s1_1);
				// 获取报价时间
				map.put("col2", s1_3);

				// 获取采购量信息
				NodeList nodes_div_3_children = nodes_div_children.elementAt(3).getChildren();
				String s3_1 = new String(nodes_div_3_children.elementAt(1)
						.toPlainTextString().getBytes("GBK"));
				Matcher m3_1 = p.matcher(s3_1);
				s3_1 = m3_1.replaceAll(" ");
				s3_1 = s3_1.replaceAll("&nbsp;", "");
				map.put("col3", s3_1);

				// 获取div下的url
				String contain_url_string = nodes_div_1_children.elementAt(1).toHtml();
				String url = contain_url_string.substring(contain_url_string
						.indexOf("<a href=") + 9, contain_url_string
						.indexOf("\" onmousedown=\""));
				
				// 仿照提交留言
				httpHelper.executePostMethod(url);
				String bodyString1 = httpHelper.getResponseBodyAsString();
				this.tijiaoliuyan_alibaba(bodyString1);
				
				// 解析div下的url的内容
				httpHelper.executePostMethod(url);
				String bodyString = httpHelper.getResponseBodyAsString();
				
				Parser parser2 = Parser.createParser(new String(bodyString.getBytes(), "GBK"), "GBK");
				// 遍历div
				NodeList nodes_div_1 = parser2
						.extractAllNodesThatMatch(new TagNameFilter("div"));
				String ss = null;
				int ff = 4;
				for (int m = 0; m < nodes_div_1.size(); m++) {
					Node node_div_1 = nodes_div_1.elementAt(m);
					String node_html_1 = node_div_1.toHtml();
					String ss_1 = "";
					String ss_2 = "";
					// 获取联系方式
					if (node_html_1.startsWith("<div class=\"contact-title\" id=\"contact-show\">")) {
						ss = new String(node_div_1.toPlainTextString().getBytes("GBK"));
						String[] strings = ss.split("联系电话：");
						
						ss_1 = strings[0];
						// 姓名
						Matcher mm = p.matcher(ss_1);
						ss_1 = mm.replaceAll(" ");
						ss_1 = ss_1.replaceAll("联系方式：", "");
						ss_1 = ss_1.replaceAll("联系电话：", "");
						map.put("col7", ss_1);
						
						ss_2 = strings[1];
						// 联系电话
						mm = p.matcher(ss_2);
						ss_2 = mm.replaceAll(" ");
						ss_2 = ss_2.replaceAll("联系方式：", "");
						ss_2 = ss_2.replaceAll("联系电话：", "");
						map.put("col8", ss_2);
					}
					
					// 一共三个信息，采购商/详细要求/采购商信息，分别是col4，col5，col6
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
								if (aa.contains("采购量：")) {
									ss1 = mm1.replaceAll(" ");
									String[] ss1s = ss1.split("&nbsp;");
									
									ss1_1 = ss1s[0];
									ss1_1 = ss1_1.replace("&nbsp;", "");
									ss1_1 = ss1_1.replace("采购量：", "");
									ss1_1 = ss1_1.replace("<div>", "");
									ss1_1 = ss1_1.replace("</div>", "");
									
									ss1_2 = ss1s[1];
									ss1_2 = ss1_2.replace("&nbsp;", "");
									ss1_2 = ss1_2.replace("采购量：", "");
									ss1_2 = ss1_2.replace("<div>", "");
									ss1_2 = ss1_2.replace("</div>", "");
								}
								if (aa.contains("收货地：")) {
									ss2 = mm1.replaceAll(" ");
									ss2 = ss2.replace("&nbsp;", "");
									ss2 = ss2.replace("收货地：", "");
									ss2 = ss2.replace("<div>", "");
									ss2 = ss2.replace("</div>", "");
								}
								if (aa.contains("询价单有效期：")) {
									ss3 = mm1.replaceAll(" ");
									ss3 = ss3.replace("&nbsp;", "");
									ss3 = ss3.replace("询价单有效期：", "");
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

	// 结束连接
	public void end() throws Exception {
		httpHelper.releaseConnection();
	}

	// 保存到mysql数据库中
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
					throw new Exception("保存数据错误!");
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
	 * 连接宽带
	 * 
	 * @param cname: 连接名，比如“宽带连接”
	 * @param uname: 宽带连接的用户名
	 * @param pwd: 宽带连接的密码
	 */
	public void connect(String cname, String uname, String pwd) {
		try {
			Process p = Runtime.getRuntime().exec("rasdial.exe " + cname + " " + uname + " " + pwd);
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 断开宽带 */
	public void disconnect() {
		try {
			Process p = Runtime.getRuntime().exec("rasdial.exe /DISCONNECT");
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}