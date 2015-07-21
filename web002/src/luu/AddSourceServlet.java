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
public class AddSourceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(request.getSession().getAttribute("username")==null){
			RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
			rd.forward(request, response);
		}else{
			int id = Integer.parseInt(request.getParameter("id"));
			PostIntoWxwl postIntoWxwl = new PostIntoWxwl();
			int flag = postIntoWxwl.insertIntoSourceWxwl(new DbDao().getSourceById(id));
			request.setCharacterEncoding("UTF-8");
			 //�����������Ϣ�ĸ�ʽ���ַ���
		    response.setContentType("text/xml; charset=UTF-8");
		    response.setHeader("Cache-Control","no-cache");
			PrintWriter out=response.getWriter();
			out.println("<HTML>");
		    out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		    out.println("  <BODY>");
		    if(flag==1){
		    	out.println("<content>"+"���ӳɹ�"+"</content>");
		    }else{
		    	out.println("<content>"+"����ʧ��"+"</content>");
		    }
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
