package base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import backtest.BackTesting;

/**
 * Servlet implementation class BactTest
 */
@WebServlet("/BackTest")
public class BackTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BackTest() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("application/json;charset=utf-8");
	    
		HttpSession session = request.getSession();
		
		System.out.println(request.getParameter("buySetting"));
		
		BackTesting bt = new BackTesting((String) session.getAttribute("email"),
				request.getParameter("exchange"),
				request.getParameter("coin"),
				request.getParameter("base"),
				Integer.parseInt(request.getParameter("interval")),
				request.getParameter("startDate"),
				request.getParameter("endDate"),
				request.getParameter("strategyName"),
				request.getParameter("buyingSetting"),
				request.getParameter("sellingSetting"),
				Double.parseDouble(request.getParameter("nowCash")),				
				0.0, 0.0, 0.0, 0.0, 0);
		bt.backTestRun();

		while(bt.getFlag()) {}
		JSONObject resBT = new JSONObject();
		resBT.put("ReturnDetailMessage", bt.getReturnDetailMsg());	
		resBT.put("ReturnMessage", bt.getReturnMsg());	

		PrintWriter out = response.getWriter();
		out.print(resBT.toJSONString());		
	}

}
