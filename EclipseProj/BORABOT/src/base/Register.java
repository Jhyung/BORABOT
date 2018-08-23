package base;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Random;

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
	    
	    String result = "";

		HttpSession session = request.getSession();
		
	    if(Boolean.valueOf(request.getParameter("auth")).booleanValue()) {
			session.setAttribute("key", new TempKey().getKey(50, false));
			SendMail.sendEmail(request.getParameter("email"),
					"보라봇 회원가입 인증 메일입니다.",
					"인증 번호 : "+session.getAttribute("key"));	    	
	    }
	    
	    else {
	    	if(session.getAttribute("key").equals(request.getParameter("key"))) {
	    	    // 회원가입 정보 DB에 저장
	    
	    		try {
		    		String insertSql = String.format("INSERT INTO customer (email, password) VALUES('"
		    				+request.getParameter("email")+"', '"+request.getParameter("password")+"')");
		    		
		    		DB useDB = new DB();
		    		useDB.Query(insertSql, "insert");
		    		
		    		// BITHUMB, COINONE, BINANCE 거래소 개인 정보 레코드 생성 (키는 미입력->프로필에서 직접 입력)
		    		String insertSql2 = String.format("INSERT INTO customer_key (email,exchange_name, api_key, secret_key) VALUES('"
		    				+request.getParameter("email")+"', 'BITHUMB', '', '')");

		    		useDB.Query(insertSql2, "insert");

		    		String insertSql3 = String.format("INSERT INTO customer_key (email,exchange_name, api_key, secret_key) VALUES('"
		    				+request.getParameter("email")+"', 'COINONE', '', '')");

		    		useDB.Query(insertSql3, "insert");

		    		String insertSql4 = String.format("INSERT INTO customer_key (email,exchange_name, api_key, secret_key) VALUES('"
		    				+request.getParameter("email")+"', 'BINANCE', '', '')");

		    		useDB.Query(insertSql4, "insert");
		    		
		    		useDB.clean();	  
		    		result = "complete";
	    		} catch (SQLException e) {
	    			if(e.getErrorCode() == 1062)
	    				result = "dupError";
	    			else e.printStackTrace();
	    		}
	    	}
	    	else result = "authError";
	    }
	    
		PrintWriter out = response.getWriter();
		out.print(result);
	}
}


class TempKey {
    
    private boolean lowerCheck;	
    private int size;
    
    public String getKey(int size, boolean lowerCheck) {
        this.size = size;
        this.lowerCheck = lowerCheck;
        return init();
    }
    
    private String init() {
        Random ran = new Random();
        StringBuffer sb = new StringBuffer();
        int num = 0;
        do {
            num = ran.nextInt(75)+48;
            if((num>=48 && num<=57) || (num>=65 && num<=90) || (num>=97 && num<=122)) {
                sb.append((char)num);
            }else {
                continue;
            }
        } while (sb.length() < size);
        if(lowerCheck) {
            return sb.toString().toLowerCase();
        }
        return sb.toString();
    }    
}
