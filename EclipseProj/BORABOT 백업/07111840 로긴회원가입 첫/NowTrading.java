

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

/**
 * Servlet implementation class SendNowTrading
 */
@WebServlet("/NowTrading")
public class NowTrading extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NowTrading() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub.

		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");
	    
	    // cors 해결?? 아닌듯
//	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
//        response.setHeader("Access-Control-Allow-Origin", "*");
		
        HttpSession session = request.getSession();

    	ArrayList<TradingElement> nT = new ArrayList<TradingElement>();
    	nT.clear();
    	
    	//테스트용 트레이딩 정보
//    	nT.add(new TradingElement("s", "s", "s", 1.1, "s", new Date(), new Date(), 1.1));
//    	nT.add(new TradingElement("s", "s", "s", 2.2, "s", new Date(), new Date(), 2.2));
    	
    	// DB에서 현재 거래 정보 가져옴
    	String email = (String) session.getAttribute("email");
		String selectSql = String.format("SELECT * from trade where email=\'%s\'", email);

		ResultSet rs = DB.Query(selectSql, "select"); 
		
		try {
			while(rs.next()) {
				if (rs.getBoolean("status")) {
					TradingElement tE = new TradingElement(
						rs.getString("bot_name")+email,
						rs.getString("coin"),
						rs.getString("exchange_name"),
						rs.getDouble("start_asset"),
						rs.getString("strategy_name"),
						rs.getDate("start_date"),
						rs.getDate("end_date"),
						rs.getDouble("last_asset")
						);
					nT.add(tE);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}		
		
		// 5. DB 사용후 clean()을 이용하여 정리
		DB.clean();
    	
    	Gson gson = new Gson();
    	String ntJson = gson.toJson(nT);
		   	

		PrintWriter out = response.getWriter();
		out.print(ntJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
