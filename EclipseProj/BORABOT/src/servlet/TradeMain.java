package servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tass.tradingBot;
import DB.DB;

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
		// 거래 시작/종료 설정
		
		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");
	    
        HttpSession session = request.getSession(false);
		PrintWriter out = response.getWriter();
		
		// 세션 유효성 확인
		if(session == null) {
			out.print("sessionExpired");
			return;
		}

        if(Boolean.valueOf(request.getParameter("status"))) {
        	// 거래 세부 설정
        	double priceBuyUnit = 0.0;
        	double priceSellUnit = 0.0;
        	double numBuyUnit = 0.0;
        	double numSellUnit = 0.0;
        	        	
        	switch(request.getParameter("buyingSetting")) {
    	    	case "buyCertainPrice":
    	    		priceBuyUnit = Double.parseDouble(request.getParameter("buyingDetail"));
    	    		break;
    	    	case "buyCertainNum":
    	    		numBuyUnit = Double.parseDouble(request.getParameter("buyingDetail"));
    	    		break;
        		default: break;
        	}
        	
        	switch(request.getParameter("sellingSetting")) {
    	    	case "sellCertainPrice":
    	    		priceSellUnit = Double.parseDouble(request.getParameter("sellingDetail"));
    	    		break;
    	    	case "sellCertainNum":
    	    		numSellUnit = Double.parseDouble(request.getParameter("sellingDetail"));
    	    		break;
    			default: break;
    		}
        	
        	// 거래 객체 생성
            try {
				new tradingBot((String) session.getAttribute("email"),
						request.getParameter("exchange").toLowerCase(),
						request.getParameter("botname"),
						request.getParameter("coin"),
						request.getParameter("base"),
						Integer.parseInt(request.getParameter("interval")),
						request.getParameter("startDate"),
						request.getParameter("endDate"),
						request.getParameter("strategyName"),
						request.getParameter("buyingSetting"),
						request.getParameter("sellingSetting"),
						priceBuyUnit, priceSellUnit, numBuyUnit, numSellUnit, 0).botStart();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				System.out.println("servlet.TradeMain 95"+e.getErrorCode());
				System.out.println("servlet.TradeMain 96"+e.getMessage());
				System.out.println("servlet.TradeMain 97"+e.getSQLState());
				out.print(e.getErrorCode());
				return;				
			}        	
			out.print("success");
        }
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
