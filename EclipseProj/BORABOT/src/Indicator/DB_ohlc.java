package Indicator;

import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DB_ohlc {
   // DB SELECT INSERT 사용가이드 (1~5단계) 
   public void UsingDB() {
//      String API_KEY = "";
//      String Secret_KEY= "";
//      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//      
//   // SELECT문
//      // 1-1. SELECT 쿼리문 String 생성
//      String selectSql = String.format("SELECT API_KEY, "
//            + "Secret_KEY FROM APIKEY WHERE _ID = \"%s\" and exchangeName = \"%s\" ;", "dirtyrobot00", "bithumb");
//
//      DB useDB = new DB();
//      
//      // 2-1. SELECT문은 ResultSet rs를 useDB.Query(쿼리문, "select")로 생성 후 필요한 값 추출
//      ResultSet rs = useDB.Query(selectSql, "select");
//      try {
//         while(rs.next()) {  
//            API_KEY = rs.getString("API_KEY");
//            Secret_KEY = rs.getString("Secret_KEY");
//         }
//      } catch (SQLException e) {
//         e.printStackTrace();         
//      }
//      
//   // INSERT, UPDATE문
//      // 1-2. INSERT 쿼리문 String 생성
//      String insertSql = String.format("INSERT INTO Trade VALUES(\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", %s, \"%s\", \"%s\", \"%s\" ,\"%s\")",
//            "dirtyrobot00", "test", "btc", "bithumb", "123123123", "321321321", "bollingerPatternNaked", dateFormat.format(new Date()), "", dateFormat.format(new Date()));
//            
//      // 2-2. INSERT문의 경우 useDB.Query(쿼리문, "select")로 DB에 입력
//      useDB.Query(insertSql, "insert");      
//      
//
//   /* 공통
//    * 3. DB 사용후 clean()을 이용하여 정리
//    * */
//      useDB.clean();
//      
//      System.out.println(API_KEY + " && " + Secret_KEY);
   }
   
   // DB Query 함수
   private Connection con = null;
   private Statement stmt = null;
   private ResultSet rs = null; //ResultSet 객체 선언

   public ResultSet Query(String Sql, String INSSEL) {
      
      // 드라이버 로드
       try {   
           Class.forName("com.mysql.cj.jdbc.Driver");
       } catch (ClassNotFoundException e) {
          System.out.println(e.getMessage()+"드라이버 로드 실패");       
       }
      
       try {   
          // DB 접속
          String url = "jdbc:mysql://localhost:3306/borabot_ohlc?autoReconnect=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
           con = DriverManager.getConnection(url,"root","01028798551");   
   
           stmt = con.createStatement();
           
           // INSERT문
           if (INSSEL == "insert") {
            stmt.executeUpdate(Sql);
           }
           // SELECT문
           else if (INSSEL == "select"){
            rs = stmt.executeQuery(Sql);              
           } else { System.out.println("데이터베이스 구문 오류!"); }

                      
       } catch (SQLException se) {
            se.printStackTrace();
        } 
        return rs;   
   }
   
   public void clean() {

      try {
         if(rs !=null) { rs.close(); }
         if(stmt != null) { stmt.close(); }
         if(con!= null) { con.close(); }
      } catch (SQLException se) {
           se.printStackTrace();
       }       
   }
}