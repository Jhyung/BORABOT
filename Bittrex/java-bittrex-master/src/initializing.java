
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class initializing {

	// websocket ������� �޾ƿ��� �κ�
	// ���ÿ� db����
	// NowTrading�� ���̴� �κ�
	// nowTrading or Trade?
	private static String exchange = "bithumb";
	private static String coin_target = "btc";
	private static String coin_base = "krw";
	private static String _ID = "dirtyrobot00";
	private static double priceAmount = 7500000;
	private static String startDate = "180606";
	private static String endDate = "180704";
	private static String botName = "������1";
	private static String strategyName = "bollinger1";

	// db���� �޾ƿ�
	// SELECT APIKEY, Secret_KEY FROM Customer, APIKEY WHERE Customer._id =
	// APIKEY._id AND exchange = {exchange}
	//private static String API_KEY = "485f69323ae844f99f2ef3ae81692a1e";
	//private static String Secret_KEY = "3289895f108b435e8a4633df2b5cdf61";
	private static String API_KEY="";
	private static String Secret_KEY="";
	
	private static String url = "jdbc:mysql://127.0.0.1/practice_db?user=root&password=01028798551&serverTimezone=UTC";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		// �Լ����η�
		// ���Ŀ� tradingBot���ٰ� �� �� �Ѱ��൵ ��
		String APIK;
		String SecK;
		SQL_Select(); // key select

		String coin_ex = "";
		String coin_crypto = coin_target + coin_base;
		if (exchange.equals("bittrex")) {
			coin_ex = coin_base + "-" + coin_target;
		}
		else if(exchange.equals("bithumb")) {
			coin_ex = coin_target;
		}
		
		SQL_Insert(); // trade������ insert
		tradingBot trbot = new tradingBot(priceAmount, _ID, startDate, endDate, exchange, coin_crypto, coin_ex, "none",
				API_KEY, Secret_KEY, botName);
		trbot.Bollingertrade();
	}
	
	public static void SQL_Select() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null; // ResultSet ��ü ����
		
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("����̹� �ε� ����!");
			conn = DriverManager.getConnection(url);
			System.out.println("�����ͺ��̽� ���� ����!");
			stmt = conn.createStatement();
			//String selectSql = String.format("INSERT INTO trade VALUES(\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", %s, \"%s\", \"%s\", \"%s\" ,\"%s\")", _ID, coin_target, priceAmount, "null", strategyName, 1, exchange, botName, startDate, endDate);
			String selectSql = String.format("SELECT API_KEY, Secret_KEY FROM APIKEY WHERE _ID = \"%s\" and exchangeName = \"%s\" ;", _ID, exchange);
			System.out.println(selectSql);
			rs = stmt.executeQuery(selectSql);
			
			while(rs.next()) {
				
				API_KEY = rs.getString(1);
				Secret_KEY = rs.getString(2);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
	}
	
	public static void SQL_Insert() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null; // ResultSet ��ü ����
		
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("����̹� �ε� ����!");
			conn = DriverManager.getConnection(url);
			System.out.println("�����ͺ��̽� ���� ����!");
			stmt = conn.createStatement();
			String selectSql = String.format("INSERT INTO trade VALUES(\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", %s, \"%s\", \"%s\", \"%s\" ,\"%s\")", _ID, coin_target, priceAmount, "null", strategyName, 1, exchange, botName, startDate, endDate);
			System.out.println(selectSql);
			stmt.executeUpdate(selectSql);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
	}

}