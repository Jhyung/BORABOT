package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import tass.Wallet;

/**
 * Servlet implementation class Wallet
 */
@WebServlet("/WalletInfo")
public class WalletInfo extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WalletInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");
	    
        HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		// 세션 유효성 확인
		if(session == null) {
			out.print("sessionExpired");
			return;
		}

        String email = (String) session.getAttribute("email");
        String exchange = request.getParameter("exchange");

        Wallet wlt = new Wallet(email);


		JSONArray jArray = new JSONArray();	// 전송용 json 배열
		JSONObject jObject = new JSONObject();	// 기축통화와 잔액 담는 객체
		
        switch(exchange) {
	        case "BINANCE":
	        	jObject.put("base", "USDT");
	        	jObject.put("balance", wlt.getBalance(exchange, "USDT"));
	        	jArray.add(jObject);
	    		break;
	        case "BITHUMB":
	        	jObject.put("base", "KRW");
	        	jObject.put("balance", wlt.getBalance(exchange, "KRW"));
	        	jArray.add(jObject);
	    		break;
	        case "HITBTC":
	        	String hitbtc[] = { "USDT", "BTC", "ETH" };
	        	for ( String b : hitbtc ) {
	        		jObject = new JSONObject(); // jObject 초기화
		        	jObject.put("base", b);
		        	jObject.put("balance", wlt.getBalance(exchange, b));
		        	jArray.add(jObject);
	        	}
	    		break;
			default: break;
        }
        
        out.print(jArray);
	}

}
