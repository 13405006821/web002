package luu;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 执行手动操作
 * @author lp
 */
public class ControlServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(request.getSession().getAttribute("username")==null){
			String tips= "";
			String username = request.getParameter("username").toString();
			String password = request.getParameter("password").toString();
			if(username.equals("wxwl")&&password.equals("68075015")){
				RequestDispatcher rd = request.getRequestDispatcher("/welcome.jsp");
				request.getSession().setAttribute("username", username);
				rd.forward(request, response);
			}else{
				RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
				tips = "用户名或密码错误";
				request.setAttribute("tips", tips);
				rd.forward(request, response);
			}
		}else{
			RequestDispatcher rd = request.getRequestDispatcher("/welcome.jsp");
			rd.forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
}
