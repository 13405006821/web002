package luu.http;

import java.util.Map;

public class PostIntoWxwl {
	private HttpHelper httpHelper = new HttpHelper();
	private String cart_url = "http://www.wxwl.net/che/publish";
	private String source_url = "http://www.wxwl.net/huo/publish";

	public int insertIntoCartWxwl(Map<String, String> map) {
		int flag = 0;
		String title = map.get("title").toString();
		String start_address = map.get("start_address").toString();
		String dest_address = map.get("dest_address").toString();
		String link_man = map.get("link_man").toString();
		String link_tel = map.get("link_tel").toString();
		String content = map.get("content").toString();
		try {
			String[][] data = {
					{ "title", title },
					{ "pic","filename=\"\" Content-Type: application/octet-stream" },
					{ "carType", "»õ³µ" }, 
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
			int statusCode = httpHelper.executePostMethod(cart_url, data);

			if (statusCode == 200|| statusCode == 302) {
				flag = 1;
			}else{
				flag = 0;
			}
		} catch (Exception e) {
			flag = 0;
			e.printStackTrace();
		}
		return flag;
	}

	public int insertIntoSourceWxwl(Map<String, String> map) {
		int flag = 0;
		String title = map.get("title").toString();
		String start_address = map.get("start_address").toString();
		String dest_address = map.get("dest_address").toString();
		String link_man = map.get("link_man").toString();
		String link_tel = map.get("link_tel").toString();
		try {
			String[][] data = { 
					{ "title", title },
					{ "start_address", start_address },
					{ "dest_address", dest_address }, 
					{ "link_man", link_man },
					{ "link_tel", link_tel }, 
					{ "dest_city", "1" },
					{ "dest_dist", "0" }, 
					{ "dest_prov", "1" },
					{ "start_city", "1" }, 
					{ "start_dist", "0" },
					{ "start_prov", "1" } 
				};
			int statusCode = httpHelper.executePostMethod(source_url, data);

			if (statusCode == 200|| statusCode == 302) {
				flag = 1;
			}else{
				flag = 0;
			}

		} catch (Exception e) {
			flag = 0;
			e.printStackTrace();
		}
		return flag;
	}
}
