package luu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import luu.db.DbDao;
import luu.makehtml.MakeHtmlChinaWuLiu;

/**
 * ִ���ֶ�����
 * 
 * @author lp
 */
public class UpdatePzServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response, String method)
			throws ServletException, IOException {
		if(request.getSession().getAttribute("username")==null){
			RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
			rd.forward(request, response);
		}else{
			request.setCharacterEncoding("UTF-8");
			// �����������Ϣ�ĸ�ʽ���ַ���
			response.setContentType("text/xml; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = response.getWriter();
			String responseText = "�쳣�ж�";
	
			String cart_url = request.getParameter("cart_url");
			String cart_start = request.getParameter("cart_start");
			String cart_end = request.getParameter("cart_end");
			String source_url = request.getParameter("source_url");
			String source_start = request.getParameter("source_start");
			String source_end = request.getParameter("source_end");
			String id = request.getParameter("id");
	
			if (cart_start.equals("") || source_start.equals("")
					|| cart_end.equals("") || source_end.equals("")) {
				responseText = "ҳ�벻��Ϊ��,<br/>�ҿ�ʼҳ���ܴ��ڽ���ҳ";
			} else {
				try {
					MakeHtmlChinaWuLiu chinaWuLiu = new MakeHtmlChinaWuLiu();
					chinaWuLiu.deleteCart();
					int che_flag = chinaWuLiu.begin_che(cart_url, Integer
							.parseInt(cart_start), Integer.parseInt(cart_end));
					chinaWuLiu.deleteSource();
					int huo_flag = chinaWuLiu.begin_huo(source_url,
							Integer.parseInt(source_start), Integer
									.parseInt(source_end));
					Map<String, String> map = new HashMap<String, String>();
					map.put("start_page_huo",source_start);
					map.put("end_page_huo",source_end);
					map.put("start_page_che",cart_start);
					map.put("end_page_che",cart_end);
					map.put("url_huo",source_url);
					map.put("url_che",cart_url);
					map.put("id",id);
					new DbDao().savePzxx(map);
					if (che_flag != 1) {
						responseText = "ץȡ��Դ��Ϣʧ��";
					}
					if (huo_flag != 1) {
						responseText = "ץȡ��Դ��Ϣʧ��";
					}
					if (che_flag == 1 && huo_flag == 1) {
						responseText = "��Ϣץȡ�ɹ�";
					}
				} catch (NumberFormatException e) {
					responseText = "ҳ����������Ч������";
				}
			}
			out.println(responseText);
			out.close();
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response, "GET");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response, "POST");
	}
}
