import java.sql.ResultSet;
import java.sql.SQLException;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;


@ServerEndpoint("/loginhandle")
public class Login {

	// 웹소켓 통해 json 왔을 떄
    @OnMessage
    public String handleMessage(String email){        
		String selectSql = String.format("SELECT password from customer where email=\'%s\'", email);
				
		ResultSet rs = DB.Query(selectSql, "select"); 
		
		String pwd= "";
		try {
			while(rs.next()) {
				pwd = rs.getString("password");
				System.out.println("password = " + pwd);
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}		
		
		// 5. DB 사용후 clean()을 이용하여 정리
		DB.clean();	
		
    	Gson gson = new Gson();
    	
    	String loginJson = gson.toJson(pwd);
    	return loginJson;
    }
    
    @OnError
    public void handleError(Throwable t){
        t.printStackTrace();
    }

}
