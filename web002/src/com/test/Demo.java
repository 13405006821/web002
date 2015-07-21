package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Demo {

  public static void main(String[] args) throws Exception {
    Map<String, String> m = new HashMap<String, String>();
    String url = "http://wxwl/che/publish";
    String code = "utf-8";


    m.put("title", "数据");
    m.put("carType", "货车" );
    m.put("start_address", "数据");
    m.put("dest_address", "数据");
    m.put("link_man", "数据");
    m.put("link_tel", "数据");
    m.put("dest_city", "1");
    m.put("dest_dist", "0");
    m.put("dest_prov", "1");
    m.put("start_prov", "1");
    m.put("start_city", "1");
    m.put("start_dist", "0" );
    m.put("dead_weight", "0" );
    m.put("content", "0" );
    m.put("cart_no", "" );
    String rus = doPost(url, m, code);

    System.out.println(rus);
  }

  @SuppressWarnings("unchecked")
public static String doPost(String reqUrl, Map<String, String> parameters, String recvEncoding) {
    HttpURLConnection conn = null;
    String responseContent = null;
    try {
      StringBuffer params = new StringBuffer();
      for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
        Entry element = (Entry) iter.next();
        params.append(element.getKey().toString());
        params.append("=");
        params.append(URLEncoder.encode(element.getValue().toString(), recvEncoding));
        params.append("&");
      }

      if (params.length() > 0) {
        params = params.deleteCharAt(params.length() - 1);
      }
      URL url = new URL(reqUrl);
      HttpURLConnection url_con = (HttpURLConnection) url.openConnection();
      url_con.setRequestMethod("POST");
      // System.setProperty("sun.net.client.defaultConnectTimeout", String
      // .valueOf(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
      // System.setProperty("sun.net.client.defaultReadTimeout", String
      // .valueOf(HttpRequestProxy.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
      url_con.setConnectTimeout(5000);//（单位：毫秒）jdk
      // 1.5换成这个,连接超时
      url_con.setReadTimeout(5000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
      url_con.setDoOutput(true);
      byte[] b = params.toString().getBytes();
      url_con.getOutputStream().write(b, 0, b.length);
      url_con.getOutputStream().flush();
      url_con.getOutputStream().close();

      InputStream in = url_con.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
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

}

