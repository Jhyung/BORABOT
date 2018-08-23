package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import DB.DB;

/**
 * Servlet implementation class Alarm
 */
@WebServlet("/Alarm")
public class Alarm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Alarm() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 알람 정보 요청했을 때 읽지 않은 알람 리스트 전송
		
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
        
		JSONArray jArray = new JSONArray();
		String selectSql = String.format("SELECT * from trans_log where email=\'%s\' and read_mark=0 ORDER BY trans_time DESC",
				(String) session.getAttribute("email"));

		
		DB useDB = new DB();

		try {
			ResultSet rs = useDB.Query(selectSql, "select"); 
		
			while(rs.next()) {
				JSONObject sObject = new JSONObject();
				sObject.put("exchange_name", rs.getString("exchange_name"));
				sObject.put("coin", rs.getString("coin"));
				sObject.put("sales_action", rs.getString("sales_action"));
				sObject.put("coin_intent", rs.getString("coin_intent"));
				sObject.put("coin_price", rs.getString("coin_price"));
				sObject.put("trans_time", rs.getString("trans_time"));
				
				jArray.add(sObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}				

		// 5. DB 사용후 clean()을 이용하여 정리
		useDB.clean();		
		
		out.print(jArray.toJSONString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 알람을 확인했을 때 DB에 알람을 읽은 표시
		
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
        
		String updateSql = String.format("update trans_log set read_mark=1 where email=\'%s\'",
				(String) session.getAttribute("email"));
		
		DB useDB = new DB();
		try {
			useDB.Query(updateSql, "insert");			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
