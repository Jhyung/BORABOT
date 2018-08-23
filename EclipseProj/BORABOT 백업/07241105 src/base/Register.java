package base;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DoAuth
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");
	    // 회원가입 정보 DB에 저장

		String insertSql = String.format("INSERT INTO customer (email, password) VALUES('"
				+request.getParameter("email")+"', '"+request.getParameter("password")+"')");
		
		DB useDB = new DB();
		useDB.Query(insertSql, "insert");

		String insertSql2 = String.format("INSERT INTO customer_key (email,exchange_name) VALUES('"
				+request.getParameter("email")+"', 'bitthumb')");

		useDB.Query(insertSql2, "insert");

		String insertSql3 = String.format("INSERT INTO customer_key (email,exchange_name) VALUES('"
				+request.getParameter("email")+"', 'bittrex')");

		useDB.Query(insertSql3, "insert");

		String insertSql4 = String.format("INSERT INTO customer_key (email,exchange_name) VALUES('"
				+request.getParameter("email")+"', 'coinone')");

		useDB.Query(insertSql4, "insert");

		String insertSql5 = String.format("INSERT INTO customer_key (email,exchange_name) VALUES('"
				+request.getParameter("email")+"', 'binance')");

		useDB.Query(insertSql5, "insert");
		
		useDB.clean();
	}
}
