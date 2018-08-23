package servlet;
import java.util.UUID; 

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DB.DB;
/**
 * Servlet implementation class FindInfo
 */
@WebServlet("/FindInfo")
public class FindInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");
	    
	    String result = "";
	    
		String email = request.getParameter("email");
		String tempPwd = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15);	// 15자리 영문, 숫자 혼합 임시 비밀번호
		
		String existsSql = String.format("select * from customer where email = '"+email+"'");	// 계정 존재 여부 쿼리
		
		// 임시 비밀번호 DB에 입력 쿼리
		String updateSql = String.format("update customer set password='"+tempPwd+
				"' where email='"+email+"'");
		
		DB useDB = new DB();
		try {
			if(useDB.Query(existsSql, "select").next()) {	// 계정이 존재하면
				useDB.Query(updateSql, "insert");	// 비밀번호 재설정
				result = "complete";
			}
			else result = "emailError";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		useDB.clean();	    	

		// 임시 비밀번호 메일 전송
		SendMail.sendEmail(email,
				"보라봇 임시 비밀번호 메일입니다.",
				"임시 비밀번호 : "+tempPwd);
	    
		PrintWriter out = response.getWriter();
		out.print(result);
	}

}
