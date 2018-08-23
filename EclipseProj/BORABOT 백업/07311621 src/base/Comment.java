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
 * Servlet implementation class Comment
 */
@WebServlet("/Comment")
public class Comment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Comment() {
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
    	
    	// DB에서 댓글 정보 가져옴
		String selectSql = String.format("SELECT * from comment where post_num=\'%s\'", request.getParameter("post_num"));

		DB useDB = new DB();
		
		ResultSet rs = useDB.Query(selectSql, "select"); 
		
		try {
			while(rs.next()) {
				JSONObject jObject = new JSONObject();
				jObject.put("email", rs.getString("email"));
				jObject.put("comment", rs.getString("comment"));
				jObject.put("comment_time", rs.getString("comment_time"));
				jObject.put("writer", rs.getString("email").equals(session.getAttribute("email")));
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
        
        String sql = "";

		DB useDB = new DB();

        if(request.getParameter("action").equals("enroll")) {
    		// comment DB에 입력
    		sql = String.format("INSERT INTO comment (email, post_num, comment, comment_time)VALUES('"
    				+session.getAttribute("email")+"', '"
    				+request.getParameter("post_num")+"', '"
    				+request.getParameter("comment")+"', '"
    				+request.getParameter("comment_time")+"')");
    		useDB.Query(sql, "insert");

    		// board DB의 댓글 개수 증가
    		sql = String.format("update board set comment_count=comment_count+1 where post_num=%s", request.getParameter("post_num"));
    		useDB.Query(sql, "insert");
	    } else if(request.getParameter("action").equals("delete")) {
			sql = String.format("delete from comment where post_num=%s and comment_time=\'%s\'",
					request.getParameter("post_num"),
					request.getParameter("comment_time"));
			useDB.Query(sql, "insert");			
    		// board DB의 댓글 개수 감소
			
    		sql = String.format("update board set comment_count=comment_count-1 where post_num=%s", request.getParameter("post_num"));
    		useDB.Query(sql, "insert");
	    } else System.out.println("Post 오류!!");
		
		useDB.clean();
		
		doGet(request,response);
	}

}
