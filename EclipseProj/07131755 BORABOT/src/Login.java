

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
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// read form data
		
		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");

		String email = request.getParameter("email");
		String password = request.getParameter("password");


	    // DB의 사용자 비밀번호를 받아와서 비교
		String selectSql = String.format("SELECT password from customer where email=\'%s\'", email);
				
		ResultSet rs = DB.Query(selectSql, "select"); 
		
		String pwd= "";
		try {
			while(rs.next()) {
				pwd = rs.getString("password");
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}		
		
		// 5. DB 사용후 clean()을 이용하여 정리
		DB.clean();	

		if(password.equals(pwd)) {
			// 세션에 사용자 정보 저장
			HttpSession session = request.getSession();
			session.setAttribute("email", email);			
		}
		
		// 로그인 버튼 후 화면 지정
		response.sendRedirect("/main");
	}

}
