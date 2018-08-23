package base;

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

/**
 * Servlet implementation class Log
 */
@WebServlet("/Log")
public class Log extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Log() {
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

		JSONArray jArray = new JSONArray();
    	
    	// DB에서 현재 거래 정보 가져옴
		String selectSql = String.format("SELECT DISTINCT bot_name, exchange_name, coin from trans_log where email=\'%s\'", (String) session.getAttribute("email"));

		DB useDB = new DB();

		try {
			ResultSet rs = useDB.Query(selectSql, "select"); 
		
			while(rs.next()) {
				JSONObject jObject = new JSONObject();
				jObject.put("bot_name", rs.getString("bot_name"));
				jObject.put("exchange_name", rs.getString("exchange_name"));
				jObject.put("coin", rs.getString("coin"));
				jArray.add(jObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}				
		// 5. DB 사용후 clean()을 이용하여 정리
		useDB.clean();

		PrintWriter out = response.getWriter();
		out.print(jArray.toJSONString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");

        HttpSession session = request.getSession();

        JSONObject jObject = new JSONObject();
		JSONArray jArray = new JSONArray();
    	
    	// DB에서 현재 거래 정보 가져옴
		String selectSql = String.format("SELECT * from trans_log where email=\'%s\' and bot_name=\'%s\' ORDER BY trans_time DESC limit %s,10",
				(String) session.getAttribute("email"),request.getParameter("bot_name"), ((Integer.parseInt(request.getParameter("pageNum"))-1)*10));

//		String selectSql = String.format("SELECT email, post_time, title, post_num, comment_count from board ORDER BY post_num DESC limit %s,10",
//				((Integer.parseInt(request.getParameter("pageNum"))-1)*10));
		
		DB useDB = new DB();

		try {
			ResultSet rs = useDB.Query(selectSql, "select"); 
		
			while(rs.next()) {
				JSONObject sObject = new JSONObject();
				sObject.put("trans_time", rs.getString("trans_time"));
				sObject.put("sales_action", rs.getString("sales_action"));
				sObject.put("coin_price", rs.getString("coin_price"));
				sObject.put("coin_intent", rs.getString("coin_intent"));
				sObject.put("now_balance", rs.getString("now_balance"));
				sObject.put("now_coin_number", rs.getString("now_coin_number"));
				
				jArray.add(sObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}				
		// 5. DB 사용후 clean()을 이용하여 정리
		useDB.clean();
		jObject.put("logList", jArray);
		
		selectSql = String.format("SELECT count(*) from trans_log where email=\'%s\' and bot_name=\'%s\'",
				(String) session.getAttribute("email"),request.getParameter("bot_name"));	
		try {
			ResultSet rs = useDB.Query(selectSql, "select");	
			while(rs.next()) {
				jObject.put("count", rs.getString("count(*)"));
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}
		useDB.clean();

		PrintWriter out = response.getWriter();
		out.print(jObject.toJSONString());
	}
}
