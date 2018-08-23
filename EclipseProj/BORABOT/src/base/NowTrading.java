package base;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");
	    
        HttpSession session = request.getSession();

		JSONArray jArray = new JSONArray();
    	
    	// DB에서 현재 거래 정보 가져옴
		String selectSql = String.format("SELECT * from trade where email=\'%s\'", (String) session.getAttribute("email"));

		try {
			DB useDB = new DB();		
			ResultSet rs = useDB.Query(selectSql, "select"); 
		
			while(rs.next()) {
				if (rs.getBoolean("status")) {
					JSONObject jObject = new JSONObject();
					jObject.put("bot_name", rs.getString("bot_name"));
					jObject.put("exchange_name", rs.getString("exchange_name"));
					jObject.put("coin", rs.getString("coin"));
					jObject.put("strategy_name", rs.getString("strategy_name"));
					jObject.put("end_date", rs.getString("end_date"));

					String profitSql = String.format("SELECT * from trans_log where email=\'%s\' and bot_name=\'%s\' order by trans_time limit 1", (String) session.getAttribute("email"), rs.getString("bot_name"));
					ResultSet pRs = useDB.Query(profitSql, "select");
						while(pRs.next()) jObject.put("profit", pRs.getDouble("now_asset"));
					jArray.add(jObject);
				}
			}
		
			// 5. DB 사용후 clean()을 이용하여 정리
			useDB.clean();
		} catch (SQLException e) {
			e.printStackTrace();			
		}		

		PrintWriter out = response.getWriter();
		out.print(jArray.toJSONString());
	}
}
