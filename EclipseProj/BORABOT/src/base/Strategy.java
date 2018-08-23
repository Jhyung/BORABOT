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
 * Servlet implementation class Strategy
 */
@WebServlet("/Strategy")
public class Strategy extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Strategy() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("application/json;charset=utf-8");
	    
		HttpSession session = request.getSession();
		
		JSONArray jArray = new JSONArray();

		String selectSql = String.format("SELECT * from custom_strategy where email=\'%s\'", (String) session.getAttribute("email"));

		try {
			DB useDB = new DB();
			ResultSet rs = useDB.Query(selectSql, "select"); 
			
				while(rs.next()) {
					JSONObject jObject = new JSONObject();
					jObject.put("data", rs.getString("strategy_content"));
					jObject.put("name", rs.getString("strategy_name"));
					jArray.add(jObject);
					}
			
			// 5. DB 사용후 clean()을 이용하여 정리
			useDB.clean();
		} catch (SQLException e) {
			e.printStackTrace();			
		}		

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
		
	    String email = (String) session.getAttribute("email"); 
		String name = request.getParameter("name");
		String data = request.getParameter("data");		
		
		String insertSql = String.format("INSERT INTO custom_strategy (email, strategy_name, strategy_content) VALUES('"
				+email+"', '"+name+"', '"+data+"')");

		try {
			DB useDB = new DB();
			useDB.Query(insertSql, "insert");		
			useDB.clean();		
		} catch (SQLException e) {
			e.printStackTrace();			
		}		
		
		doGet(request, response);
	}

}
