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
 * Servlet implementation class Board
 */
@WebServlet("/Board")
public class Board extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Board() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 게시판을 눌렀을 때 게시판 목록 전송
		
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
        
        JSONObject jObject = new JSONObject();
		JSONArray jArray = new JSONArray();
    	
    	// DB에서 요청한 페이지의 게시불 정보 가져옴
		String selectSql = String.format("SELECT email, post_time, title, post_num, comment_count from board ORDER BY post_num DESC limit %s,10",
				((Integer.parseInt(request.getParameter("pageNum"))-1)*10));

		DB useDB = new DB();
		
		try {
			ResultSet rs = useDB.Query(selectSql, "select");	
			while(rs.next()) {
				JSONObject sObject = new JSONObject();
				sObject.put("email", rs.getString("email"));
				sObject.put("post_time", rs.getString("post_time"));
				sObject.put("title", rs.getString("title"));
				sObject.put("post_num", rs.getString("post_num"));
				sObject.put("comment_count", rs.getString("comment_count"));

				jArray.add(sObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}
		useDB.clean();
		
		jObject.put("postList", jArray);
		
		selectSql = String.format("SELECT count(*) from board");	
		try {
			ResultSet rs = useDB.Query(selectSql, "select");	
			while(rs.next()) {
				jObject.put("count", rs.getString("count(*)"));
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}
		useDB.clean();

		out.print(jObject.toJSONString());
	}
}
