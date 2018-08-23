//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;

import java.sql.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/insertDB")
public class 곧지워야댐insertDB {

	
	private static String url = "jdbc:mysql://127.0.0.1/test?user=root&password= &autoReconnect=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
	
	@OnOpen
    public void handleOpen(){
        System.out.println("insertDB is now connected...");
    }
	
	
	@OnMessage
	public void handleSql(String sql) {
		Connection conn = null;
		Statement stmt = null;
//		ResultSet rs = null; //ResultSet 객체 선언
		
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("드라이버 로드 성공!");
            conn = DriverManager.getConnection(url);
            System.out.println("데이터베이스 접속 성공!");
            stmt = conn.createStatement();
            String selectSql = sql;
            stmt.executeUpdate(selectSql);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            if(conn!=null) try {conn.close();} catch (SQLException e) {}
        }
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
