//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
import java.sql.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@ServerEndpoint("/tassSqlSelect")
public class tassSqlSelect {
	//private static String url = "jdbc:mysql://127.0.0.1/practice_db?user=root&password=01028798551&serverTimezone=UTC";
	private String url = "jdbc:mysql://127.0.0.1/test_ljh?user=root&serverTimezone=UTC";
	
	@OnOpen
    public void handleOpen(){
        System.out.println("selectDB is now connected...");
    }
	
	@OnMessage
	public String handleSql(String sql) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null; //ResultSet 객체 선언
		Gson gson = new Gson();
		JsonObject jReturn = new JsonObject();
		
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("드라이버 로드 성공!");
            conn = DriverManager.getConnection(url);
            System.out.println("데이터베이스 접속 성공!");
            stmt = conn.createStatement();
            // sql : SELECT botName, startDate, coin, exchange, startAsset FROM Trade;
            String selectSql = sql;
            rs = stmt.executeQuery(selectSql); //rs에 executeQuery의 실행결과를 삽입
            
            JsonArray jArray = new JsonArray();
            
            while(rs.next()) {
            	JsonObject eleObj = new JsonObject();
            	eleObj.addProperty( "botName", rs.getString(0) );
            	eleObj.addProperty( "startDate", rs.getString(1) );
            	eleObj.addProperty( "coin", rs.getString(2) );
            	eleObj.addProperty( "exchange", rs.getString(3) );
            	eleObj.addProperty( "startAsset", rs.getString(4) );
            	jArray.add(eleObj);
            }
            jReturn.add("result", jArray);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            if(conn!=null) try {conn.close();} catch (SQLException e) {}
        }
        
        return jReturn.toString();
	}
	
	@OnClose
    public void handleClose(){
        System.out.println("client is now disconnected...");
    }
	
    @OnError
    public void handleError(Throwable t){
        t.printStackTrace();
    }
}
