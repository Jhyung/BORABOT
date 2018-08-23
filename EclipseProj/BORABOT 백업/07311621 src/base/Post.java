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
 * Servlet implementation class Post
 */
@WebServlet("/Post")
public class Post extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Post() {
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

    	// DB에서 현재 거래 정보 가져옴
		String selectSql = String.format("SELECT email, content, title from board where post_num=%s", request.getParameter("post_num"));

		DB useDB = new DB();
		
		ResultSet rs = useDB.Query(selectSql, "select"); 

		JSONObject jObject = new JSONObject();
		try {
			while(rs.next()) {
				jObject.put("email", rs.getString("email"));
				jObject.put("title", rs.getString("title"));
				jObject.put("content", rs.getString("content"));
				jObject.put("writer", rs.getString("email").equals(session.getAttribute("email")));
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}		
		
		// 5. DB 사용후 clean()을 이용하여 정리
		useDB.clean();

		PrintWriter out = response.getWriter();
		out.print(jObject.toJSONString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");

        HttpSession session = request.getSession();
        
        String sql = "";

		DB useDB = new DB();
		
        if(request.getParameter("action").equals("write")) {
    		sql = String.format("INSERT INTO board (email, content, post_time, title, comment_count)VALUES('"
    				+session.getAttribute("email")+"', '"
    				+request.getParameter("content")+"', '"
    				+request.getParameter("post_time")+"', '"
    				+request.getParameter("title")+"', 0)");
    		useDB.Query(sql, "insert");    		
        } else if(request.getParameter("action").equals("modify")) {		// 수정 부분 만들어야
			sql = String.format("update board set title=\'%s\' where post_num=%s", request.getParameter("title"), request.getParameter("post_num"));
			useDB.Query(sql, "insert");
			sql = String.format("update board set content=\'%s\' where post_num=%s", request.getParameter("content"), request.getParameter("post_num"));
			useDB.Query(sql, "insert");
        } else if(request.getParameter("action").equals("delete")) {
			sql = String.format("delete from comment where post_num=%s", request.getParameter("post_num"));
			useDB.Query(sql, "insert");			
			sql = String.format("delete from board where post_num=%s", request.getParameter("post_num"));
			useDB.Query(sql, "insert");			
        } else System.out.println("Post 오류!!");        
		useDB.clean();		
	}

}
