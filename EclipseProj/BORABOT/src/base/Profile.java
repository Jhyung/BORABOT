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
		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");
	    
		HttpSession session = request.getSession();
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

		PrintWriter out = response.getWriter();
		out.print(jsonObject.toJSONString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 데이터 인코딩 설정
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");
	    // 회원가입 정보 DB에 저장
	    
		HttpSession session = request.getSession();
	    
		try {
		    if(Boolean.valueOf(request.getParameter("profile"))) {
				String updateSql = String.format("update customer set name='"+request.getParameter("name")+
						"', phone_number='"+request.getParameter("phone_number")+
						"' where email='"+session.getAttribute("email")+"'");
				
				DB useDB = new DB();
				useDB.Query(updateSql, "insert");
				
				useDB.clean();	    	
		    }
		    
		    else {
				String updateSql = String.format("update customer_key set api_key='"+request.getParameter("api_key")+
						"', secret_key='"+request.getParameter("secret_key")+
						"' where email='"+session.getAttribute("email")+
						"' and exchange_name='"+request.getParameter("exchange_name")+"'");
	
				DB useDB = new DB();
				useDB.Query(updateSql, "insert");
				
				useDB.clean();	    
		    }
		} catch (SQLException se) {
			se.printStackTrace();
		}
	    doGet(request, response);
	}

}
