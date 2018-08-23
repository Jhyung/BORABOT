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

import tass.CoinRecommendation;

/**
 * Servlet implementation class CoinRecommend
 */
@WebServlet("/CoinRecommend")
public class CoinRecommend extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CoinRecommend() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 메인 화면 표시할 때 코인 정보 전송
		
		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");
	    
        HttpSession session = request.getSession(false);
		PrintWriter out = response.getWriter();
		
		// 세션 유효성 확인 (NowTrading과 같은 화면이어서 여기선 처리 안함)

	    JSONObject jObject = new JSONObject();

	    CoinRecommendation cr = new CoinRecommendation();
	    
	    jObject.put("volumeHigh", cr.getVolumeHighlyIncreasingCoin());
	    jObject.put("priceHigh", cr.getPriceHighlyIncreasingCoin());
	    jObject.put("biggestGap", cr.get24BiggestGapCoin());

	    JSONArray jArray = new JSONArray();
	    for (String exchange: cr.getExchangeList())	jArray.add(exchange);
	    jObject.put("exchangeList", jArray);
	    
		out.print(jObject.toJSONString());
	}
}
