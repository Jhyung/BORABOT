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

/**
 * Servlet implementation class DoLogin
 */
@WebServlet("/LogInOut")
public class LogInOut extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogInOut() {
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
		
		session.invalidate();
		response.sendRedirect("/");						
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// read form data
		
		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");

		String email = request.getParameter("email");
		String password = request.getParameter("password");

	    // DB의 사용자 비밀번호를 받아와서 비교
		String selectSql = String.format("SELECT password from customer where email=\'%s\'", email);

		String result="";
		
		String pwd= "";
		try {
			DB useDB = new DB();
			ResultSet rs = useDB.Query(selectSql, "select"); 
			
			if(rs.next()) {					
				if(password.equals(rs.getString("password"))) {
					// 세션에 사용자 정보 저장
					HttpSession session = request.getSession();
					session.setAttribute("email", email);	
					session.setAttribute("status", true);		
					result = "complete";
				} else result = "pwError";
			} else result = "emailError";
			
			// 5. DB 사용후 clean()을 이용하여 정리
			useDB.clean();	
		} catch (SQLException e) {
			e.printStackTrace();			
		}		
	    
		PrintWriter out = response.getWriter();
		out.print(result);
	}

}
