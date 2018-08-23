package servlet;


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

import DB.DB;
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
		// 접속한 계정의 현재 진행 중인 거래 정보 전송
		
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

		JSONObject jObject = new JSONObject();	// 전송용 json 객체
		JSONArray jArray = new JSONArray();	// 현재 거래 리스트 담는 json 배열
    	
		try {
	    	// DB에서 현재 거래 정보 가져옴
			String selectSql = String.format("SELECT * from trade where email=\'%s\'", (String) session.getAttribute("email"));
			DB useDB = new DB();		
			ResultSet rs = useDB.Query(selectSql, "select"); 
		
			while(rs.next()) {
				if (rs.getBoolean("status")) {
					JSONObject subObject = new JSONObject();
					subObject.put("bot_name", rs.getString("bot_name"));
					subObject.put("exchange_name", rs.getString("exchange_name"));
					subObject.put("coin", rs.getString("coin")+rs.getString("base"));
					subObject.put("strategy_name", rs.getString("strategy_name"));
					subObject.put("end_date", rs.getString("end_date"));
					subObject.put("interval", rs.getString("interval"));

					String profitSql = String.format("SELECT * from trans_log where email=\'%s\' and bot_name=\'%s\' order by trans_time limit 1", (String) session.getAttribute("email"), rs.getString("bot_name"));
					ResultSet pRs = useDB.Query(profitSql, "select");
						while(pRs.next()) subObject.put("profit", pRs.getDouble("now_asset"));
					jArray.add(subObject);
				}
			}
			
			// 전송용 json 객체에 현재 거래리스트 배열 담기
			jObject.put("nowTradingList", jArray);

			// DB에서 현재 읽지 않은 알림 개수 가져옴
			String countSql = String.format("SELECT count(*) from trans_log where email= \'%s\' and read_mark=0", (String) session.getAttribute("email"));

			ResultSet rsC = useDB.Query(countSql, "select");	
			while(rsC.next()) {
				jObject.put("alarmCount", rsC.getString("count(*)"));
			}
		
			// 5. DB 사용후 clean()을 이용하여 정리
			useDB.clean();
		} catch (SQLException e) {
			e.printStackTrace();			
		}		

		out.print(jObject.toJSONString());
	}
}
