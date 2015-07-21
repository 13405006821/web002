package luu.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HttpHelper {
	HttpClient httpClient = new HttpClient();
	public Parser myParser;
	public PostMethod postMethod;
	public NodeList nodeList;
	public GetMethod getMethod;

	public int executePostMethod(String url, String[][] data) throws Exception {
		postMethod = new MyPostMethod(url);
		NameValuePair[] nvp = new NameValuePair[data.length];
		for (int i = 0; i < data.length; i++) {
			nvp[i] = new NameValuePair(data[i][0], data[i][1]);
		}
		postMethod.setRequestBody(nvp);
		return httpClient.executeMethod(postMethod);
	}

	public PostMethod getPostMethod(String url, String[][] data)
			throws Exception {
		PostMethod postMethod = new MyPostMethod(url);
		NameValuePair[] nvp = new NameValuePair[data.length];
		for (int i = 0; i < data.length; i++) {
			nvp[i] = new NameValuePair(data[i][0], data[i][1]);
		}
		postMethod.setRequestBody(nvp);
		httpClient.executeMethod(postMethod);
		return postMethod;
	}

	@SuppressWarnings("unchecked")
	public String doPost(String reqUrl, Map<String, String> parameters,
			String recvEncoding) {
		HttpURLConnection conn = null;
		String responseContent = null;
		try {
			StringBuffer params = new StringBuffer();
			for (Iterator iter = parameters.entrySet().iterator(); iter
					.hasNext();) {
				Entry element = (Entry) iter.next();
				params.append(element.getKey().toString());
				params.append("=");
				params.append(URLEncoder.encode(element.getValue().toString(),
						recvEncoding));
				params.append("&");
			}

			if (params.length() > 0) {
				params = params.deleteCharAt(params.length() - 1);
			}
			URL url = new URL(reqUrl);
			HttpURLConnection url_con = (HttpURLConnection) url
					.openConnection();
			url_con.setRequestMethod("POST");
			// System.setProperty("sun.net.client.defaultConnectTimeout", String
			// .valueOf(HttpRequestProxy.connectTimeOut));//
			// （单位：毫秒）jdk1.4换成这个,连接超时
			// System.setProperty("sun.net.client.defaultReadTimeout", String
			// .valueOf(HttpRequestProxy.readTimeOut)); //
			// （单位：毫秒）jdk1.4换成这个,读操作超时
			url_con.setConnectTimeout(5000);// （单位：毫秒）jdk
			// 1.5换成这个,连接超时
			url_con.setReadTimeout(5000);// （单位：毫秒）jdk 1.5换成这个,读操作超时
			url_con.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			url_con.getOutputStream().write(b, 0, b.length);
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return responseContent;
	}

	public String getResponseString(String url, String[][] data)
			throws HttpException, IOException {
		PostMethod postMethod = new MyPostMethod(url);
		NameValuePair[] nvp = new NameValuePair[data.length];
		for (int i = 0; i < data.length; i++) {
			nvp[i] = new NameValuePair(data[i][0], data[i][1]);
		}
		postMethod.setRequestBody(nvp);
		httpClient.executeMethod(postMethod);
		InputStream in = postMethod.getResponseBodyAsStream();
		byte[] responseBody = null;
		ByteArrayOutputStream buffer = null;
		if (in != null) {
			byte[] tmp = new byte[4096];
			int bytesRead = 0;
			buffer = new ByteArrayOutputStream(1024);
			while ((bytesRead = in.read(tmp)) != -1) {
				buffer.write(tmp, 0, bytesRead);
			}
			responseBody = buffer.toByteArray();
		}
		in.close();
		return new String(responseBody, "UTF-8");
	}

	public int executePostMethod(String url) throws Exception {
		postMethod = new MyPostMethod(url);
		return httpClient.executeMethod(postMethod);
	}

	public void releaseConnection() {
		postMethod.releaseConnection();
	}

	public String getResponseBodyAsString() throws Exception {
		InputStream in = postMethod.getResponseBodyAsStream();
		byte[] responseBody = null;
		ByteArrayOutputStream buffer = null;
		if (in != null) {
			byte[] tmp = new byte[4096];
			int bytesRead = 0;
			buffer = new ByteArrayOutputStream(1024);
			while ((bytesRead = in.read(tmp)) != -1) {
				buffer.write(tmp, 0, bytesRead);
			}
			responseBody = buffer.toByteArray();
		}
		in.close();
		return new String(responseBody, "gbk");
	}

	/**
	 * @Param bodyStr 返回HTML的bodyString
	 */
	public int getTableNum(String bodyStr) throws ParserException {
		myParser = Parser.createParser(bodyStr, "UTF-8");
		NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[] { tableFilter });
		nodeList = myParser.parse(lastFilter);
		Node[] nodes = nodeList.toNodeArray();
		return nodes.length;
	}

	public NodeList getTableNodes(String bodyStr) throws ParserException {
		myParser = Parser.createParser(bodyStr, "GBK");
		NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[] { tableFilter });
		return myParser.parse(lastFilter);
	}

	/**
	 * @Param t table在nodes中的索引
	 */
	public String[][] getTableArray(int t) throws ParserException {
		if (nodeList == null) {
			return null;
		}
		Node[] nodes = nodeList.toNodeArray();
		TableTag tg = (TableTag) nodes[t];
		TableRow[] tr = tg.getRows();
		String[][] tableArr = new String[tr.length][100];
		for (int i = 0; i < tr.length; i++) {
			TableColumn[] td = tr[i].getColumns();
			for (int j = 0; j < td.length; j++) {
				tableArr[i][j] = td[j].toPlainTextString();
			}
		}
		return tableArr;
	}

	/**
	 * @Param bodyStr 返回HTML的bodyString
	 * @Param t 第t个Table
	 */

	public String[][] getTableArray(String bodyStr, int t)
			throws ParserException {
		NodeList nodeList = null;
		myParser = Parser.createParser(bodyStr, "UTF-8");
		NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[] { tableFilter });
		nodeList = myParser.parse(lastFilter);
		Node[] nodes = nodeList.toNodeArray();
		TableTag tg = (TableTag) nodes[t - 1];
		TableRow[] tr = tg.getRows();
		String[][] tableArr = new String[tr.length][100];

		System.out.println("tr.length" + tr.length);
		for (int i = 0; i < tr.length; i++) {
			TableColumn[] td = tr[i].getColumns();
			for (int j = 0; j < td.length; j++) {
				tableArr[i][j] = td[j].toPlainTextString();
			}
		}
		return tableArr;
	}

	/**
	 * 解决参数提交乱码问题
	 */
	public static class MyPostMethod extends PostMethod {
		public MyPostMethod(String url) {
			super(url);
		}

		@Override
		public String getRequestCharSet() {
			return "utf-8";
		}
	}
	
	public static class MyGetMethod extends GetMethod {
		public MyGetMethod(String url) {
			super(url);
		}
		
		@Override
		public String getRequestCharSet() {
			return "utf-8";
		}
	}
	
	public int executeGetMethod(String url) throws Exception {
		getMethod = new MyGetMethod(url);
		return httpClient.executeMethod(getMethod);
	}
}
