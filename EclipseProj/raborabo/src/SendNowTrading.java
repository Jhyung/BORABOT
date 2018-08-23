

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class SendNowTrading
 */
@WebServlet("/SendNowTrading")
public class SendNowTrading extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendNowTrading() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

//		// 데이터 인코딩 설정
//	    request.setCharacterEncoding("utf-8");
//	    response.setContentType("text/html;charset=utf-8");
//	    
//	    // 세션의 사용자 정보 확인
//	    String email = (String) request.getSession().getAttribute("Email");
//	    
//	    // 현재 진행중인 거래 리스트 초기화
//    	ArrayList<TradingElement> nT = new ArrayList<TradingElement>();
//    	nT.clear();
//		
//    	//현재 진행중인 거래 정보 DB에서 가져옴
//		String selectSql = String.format("SELECT * from trade where email=\'%s\'", email);
//
//		ResultSet rs = DB.Query(selectSql, "select"); 
//		
//		try {
//			while(rs.next()) {
//				if (rs.getBoolean("status")) {
//					TradingElement tE = new TradingElement(
//						rs.getString("bot_name"),
//						rs.getString("coin"),
//						rs.getString("exchange_name"),
//						rs.getDouble("start_asset"),
//						rs.getString("strategy_name"),
//						rs.getDate("start_date"),
//						rs.getDate("end_date"),
//						rs.getDouble("last_asset")
//						);
//					nT.add(tE);
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();			
//		}		
//		
//		// 5. DB 사용후 clean()을 이용하여 정리
//		DB.clean();	
//		
//    	Gson gson = new Gson();
//    	
//    	String ntJson = gson.toJson(nT);
//
//        request.setAttribute("data", ntJson);
//        request.getRequestDispatcher("/LoginTest").forward(request, response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
