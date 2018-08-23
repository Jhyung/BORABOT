package base;


import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tass.tradingBot;

/**
 * Servlet implementation class TradeMain
 */
@WebServlet("/TradeMain")
public class TradeMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TradeMain() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");

        HttpSession session = request.getSession();

        if(Boolean.valueOf(request.getParameter("status")))
            new tradingBot((String) session.getAttribute("email"),
            		request.getParameter("exchange"),
            		request.getParameter("botname"),
            		request.getParameter("coin"),
            		request.getParameter("base"),
            		Integer.parseInt(request.getParameter("interval")),
            		request.getParameter("startDate"),
            		request.getParameter("endDate"),
            		request.getParameter("strategyName"),
            		request.getParameter("buyingSetting"),
            		request.getParameter("sellingSetting"),
            		0.0, 0.0, 0.0, 0.0, 0).botStart();

        else {	// 거래 종료 (DB의 거래 상태, 거래 종료 시간 변경) 
    		try {
        		DB useDB = new DB();
        		useDB.Query(String.format(
        				"update trade set status=0 where email=\'%s\' and bot_name=\'%s\'" ,
        				(String) session.getAttribute("email"),
        				request.getParameter("botname")), "insert");		
        		useDB.clean();
    		} catch(SQLException se) {
    			se.printStackTrace();
    		}
        }

	}
}
