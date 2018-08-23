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
import DB.Hash;
/**
 * Servlet implementation class Profile
 */
@WebServlet("/Profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 접속한 계정의 개인 정보 전송
		
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
		
		JSONObject jsonObject = new JSONObject();
		
		String profileSql = String.format("SELECT * from customer where email=\'%s\'", (String) session.getAttribute("email"));
		String keySql = String.format("SELECT * from customer_key where email=\'%s\'", (String) session.getAttribute("email"));

		DB useDB = new DB();
		try {
			ResultSet rsP = useDB.Query(profileSql, "select"); 	

			while(rsP.next()) {
				jsonObject.put("name", rsP.getString("name"));
				jsonObject.put("phone_number", rsP.getString("phone_number"));
				}
		} catch (SQLException e) {
			e.printStackTrace();			
		}		
		// 5. DB 사용후 clean()을 이용하여 정리
		useDB.clean();
		
		JSONArray eArray = new JSONArray();
		try {
			ResultSet rsK = useDB.Query(keySql, "select"); 
			while(rsK.next()) {
				JSONObject subObject = new JSONObject();
				subObject.put("exchange_name", rsK.getString("exchange_name"));
				subObject.put("api_key", rsK.getString("api_key"));
				subObject.put("secret_key", rsK.getString("secret_key"));
				eArray.add(subObject);
				}
		} catch (SQLException e) {
			e.printStackTrace();			
		}
		jsonObject.put("exchange", eArray);
		
		// 5. DB 사용후 clean()을 이용하여 정리
		useDB.clean();

		out.print(jsonObject.toJSONString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 접속한 계정의 개인 정보 저장/삭제
		
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
		
		String result = "";
		
		try {
			String updateSql = "";
			DB useDB = new DB();
			
			switch(request.getParameter("item")) {
				// 신상 정보 변경
				case "profile":
					updateSql = String.format("update customer set name='"+request.getParameter("name")+
							"', phone_number='"+request.getParameter("phone_number")+
							"' where email='"+session.getAttribute("email")+"'");
					result = "complete";
					break;
				// 비밀번호 변경
				case "password":
					// DB에서 현재 비밀번호 가져옴
					ResultSet rs = useDB.Query("select password from customer where email='"+session.getAttribute("email")+"'", "select");
					String password = "";
					if(rs.next()) password = rs.getString("password");

	    			Hash h = new Hash();
					if(!h.hashing(request.getParameter("old_password")).equals(password)) result = "wrongError";	// 현재 비밀번호가 일치하지 않을 경우
					else if(h.hashing(request.getParameter("password")).equals(password)) result = "sameError";	// 새로운 비밀번호가 현재 비밀번호와 같은 경우
					else {	// 정상적인 경우
						updateSql = String.format("update customer set password='"+h.hashing(request.getParameter("password"))+
								"' where email='"+session.getAttribute("email")+"'");		
						result = "complete";				
					} 
					break;
				// 거래소 정보 변경
				case "exchange":
					updateSql = String.format("update customer_key set api_key='"+request.getParameter("api_key")+
							"', secret_key='"+request.getParameter("secret_key")+
							"' where email='"+session.getAttribute("email")+
							"' and exchange_name='"+request.getParameter("exchange_name")+"'");
					result = "complete";
					break;
				default:
					break;
			}
			
			// 정상적인 경우 DB를 수정하고 성공 메세지 전송
			if(result == "complete") {
				useDB.Query(updateSql, "insert");			
				useDB.clean();
				doGet(request, response);
			}
			else {	// 오류가 발생한 경우 오류 메세지 전송
				out.print(result);
			}   
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
