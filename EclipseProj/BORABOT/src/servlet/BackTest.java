package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import backtest.BackTestingPerform;
import tass.tradingBot;

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
		// 백테스트 시작 버튼을 누르면 해당 정보로 백테스팅
		
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
    	
		BackTestingPerform bt = new BackTestingPerform((String) session.getAttribute("email"),
				request.getParameter("exchange").toLowerCase(),
				request.getParameter("coin"),
				request.getParameter("base"),
				Double.parseDouble(request.getParameter("nowCash")),
				Integer.parseInt(request.getParameter("interval")),
				request.getParameter("startDate"),
				request.getParameter("endDate"),
				request.getParameter("strategyName"),
				request.getParameter("buyingSetting"),
				request.getParameter("sellingSetting"),	
        		priceBuyUnit, priceSellUnit, numBuyUnit, numSellUnit, 0);

		String res = bt.backTestRun(); 
		System.out.println(res);
		out.print(res);	

//		resBT.put("ReturnDetailMessage", bt.getReturnDetailMsg());	
//		resBT.put("ReturnMessage", bt.getReturnMsg());	

//		String test= "{\"status\":\"성공\",\"result\":{\"finalCoin\":-0.7684712739404679},\"error\":\"\",\"log\":[{\"time\":\"Thu Aug 09 14:05:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7428000.0KRW\",\"salingCoinNumber\":\"134.62574044157242개\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.62574044157242\",\"success\":\"성공\"},{\"time\":\"Thu Aug 09 14:10:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7410000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.62574044157242\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 14:15:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7389000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.62574044157242\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 14:20:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7394000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.62574044157242\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 14:25:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7400000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.62574044157242\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 14:30:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7371000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.62574044157242\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 14:35:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7392000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.62574044157242\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 14:40:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7374000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.62574044157242\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 14:45:00 KST 2018\",\"saleAction\":\"판매\",\"coinCurrentPrice\":\"7394000.0\",\"salingCoinNumber\":134.62574044157242,\"nowCash\":\"9.954227248249865E8\",\"nowCoin\":\"0.0\",\"success\":\"성공\"},{\"time\":\"Thu Aug 09 14:50:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7390000.0KRW\",\"salingCoinNumber\":\"134.69860958389532개\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.69860958389532\",\"success\":\"성공\"},{\"time\":\"Thu Aug 09 15:05:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7393000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.69860958389532\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 15:45:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7397000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.69860958389532\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 15:50:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7371000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.69860958389532\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 16:00:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7386000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"134.69860958389532\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 16:10:00 KST 2018\",\"saleAction\":\"판매\",\"coinCurrentPrice\":\"7394000.0\",\"salingCoinNumber\":134.69860958389532,\"nowCash\":\"9.95961519263322E8\",\"nowCoin\":\"0.0\",\"success\":\"성공\"},{\"time\":\"Thu Aug 09 16:25:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7375000.0KRW\",\"salingCoinNumber\":\"135.04562973061994개\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"성공\"},{\"time\":\"Thu Aug 09 16:30:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7360000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 16:35:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7355000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 16:40:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7356000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 16:45:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7353000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 16:50:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7350000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 16:55:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7350000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 17:00:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7344000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 17:15:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7342000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 17:20:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7340000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 17:25:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7341000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 17:30:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7341000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 17:35:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7330000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"},{\"time\":\"Thu Aug 09 17:40:00 KST 2018\",\"saleAction\":\"구매\",\"coinCurrentPrice\":\"7322000.0\",\"salingCoinNumber\":\"0\",\"nowCash\":\"0.0\",\"nowCoin\":\"135.04562973061994\",\"success\":\"실패 : 잔액 없음\"}]}";
//		out.print(test);		
	}

}
