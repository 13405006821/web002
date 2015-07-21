package luu;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import luu.db.DbDao;

/**
 * 获取车源信息列表
 * @author lp
 */
public class CartListServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(request.getSession().getAttribute("username")==null){
			RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
			rd.forward(request, response);
		}else{
			int page = Integer.parseInt(request.getParameter("p"));
			page = page<1?1:page;
			Integer pageSize = 15;
			int offset = (page-1) * pageSize;
			Map<String, Object> map=new DbDao().getCartList(offset,pageSize);
			int total =  Integer.parseInt(map.get("count").toString());
			int lastPage = (total % pageSize) > 0 ? (total / pageSize + 1) :(total / pageSize);
			request.setAttribute("lastPage", lastPage);
			request.setAttribute("total", total);
			request.setAttribute("cartList", map.get("dataList"));	
			request.setAttribute("p", page);
			RequestDispatcher rd = request.getRequestDispatcher("/cartList.jsp");
			rd.forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
}
