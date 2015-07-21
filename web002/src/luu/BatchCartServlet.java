package luu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import luu.db.DbDao;
import luu.http.PostIntoWxwl;

/**
 * ִ���ֶ�����
 * @author lp
 */
public class BatchCartServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(request.getSession().getAttribute("username")==null){
			RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
			rd.forward(request, response);
			}else{
			request.setCharacterEncoding("UTF-8");
			 //�����������Ϣ�ĸ�ʽ���ַ���
		    response.setContentType("text/xml; charset=UTF-8");
		    response.setHeader("Cache-Control","no-cache");
			PrintWriter out=response.getWriter();
			String content = "�쳣�ж�" ;
			
			PostIntoWxwl postIntoWxwl = new PostIntoWxwl();
			String operator = request.getParameter("op");
			String qstr = request.getParameter("qstr");
			if(!qstr.equals("")){
				String message = "";
				String split_id[] = qstr.split(",");
				int len = split_id.length;
				int flag = 0;
				int j = 0;
				if(len>0){
					for(int i=0;i<len;i++){
						String temp_id = split_id[i];
						int id = Integer.parseInt(temp_id);
						if(operator.equals("add")){
							message = "���" ;
							flag = postIntoWxwl.insertIntoCartWxwl(new DbDao().getCartById(id));
						}
						if(operator.equals("del")){
							message = "ɾ��" ;
							flag = new DbDao().delCartById(id);
						}
						if(flag == 1){
							j++;
						}
					}
				}
			    if(flag==1){
			    	content = "�ɹ�"+message+"��"+j+"����¼";
			    }else{
			    	content = "����ʧ��";
			    }
			}else{
				content = "��ѡ��һ����¼";
			}
			out.println("<HTML>");
		    out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		    out.println("  <BODY>"); 
		    out.println("<content>"+content+"</content>");
		    out.println("  </BODY>");
		    out.println("</HTML>");
		    out.flush();
		    out.close();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
}
