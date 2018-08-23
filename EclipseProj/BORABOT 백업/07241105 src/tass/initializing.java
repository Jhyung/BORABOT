package tass;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Indicator.MACDs;
import backtest.BackTesting;
import exchangeAPI.*;
public class initializing {
	
	public static void printArray(double[] arr) {
		
		for(int i = 0; i < arr.length; i++) {
			System.out.print(arr[i]+" ");
		}
		System.out.println();
	}
	
	// websocket 통신으로 받아오는 부분
	// 동시에  db저장(INSERT INTO trade VALUES(); )
	private static String email = "test";
	private static String botName = "wqetqwet";
	private static String exchange = "bithumb";
	private static String coin = "ltc";
	private static String base = "krw";
	private static String strategyName = "test1";
	private static int interval = 1800;
	private static String startDate = "2018-07-23T00:00:00.000";
	private static String endDate = "2018-07-27T14:25:00.000";
	
	// 매매량 세팅
	private static String buySetting = "buyAll";
	private static String sellSetting = "sellAll";
	
	//optional
	private static double priceBuyUnit = 0.0;
	private static double priceSellUnit = 0.0;
	private static double numBuyUnit = 0.0;
	private static double numSellUnit = 0.0;
	
	//errorHandle : 1 => 대기
	//				0 => shutdown
	private static int errorHandling = 0;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		new BackTesting(email, exchange, coin, base, interval, startDate, endDate, strategyName, buySetting, sellSetting, 10000000, priceBuyUnit, priceSellUnit, numBuyUnit,  numSellUnit, errorHandling).backTestRun();
		new tradingBot(email, exchange, botName, coin, base, interval, startDate, endDate, strategyName, buySetting, sellSetting,  priceBuyUnit, priceSellUnit, numBuyUnit,  numSellUnit, errorHandling).botStart();
		
		
	}

}