package tass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import exchangeAPI.HitbtcAPI;

public class initializing {

	public static void printArray(double[] arr) {

		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static long dateToUt(LocalDateTime ldt) {

		ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
		long epoch = ldt.atZone(zoneId).toEpochSecond();

		return epoch;
	}

	public static Date utToDate(long Utime) {

		Date date = new Date();
		date.setTime((long) Utime * 1000);

		return date;
	}

	// websocket 통신으로 받아오는 부분
	// 동시에 db저장(INSERT INTO trade VALUES(); )
	private static String email = "dirtyrobot00@gmail.com";
	private static String botName = "mybo12t";
	private static String exchange = "binance";
	private static String coin = "eth";
	private static String base = "btc";
	private static double initialCash = 1000000000;
	private static String strategyName = "johnbur1";
	private static int interval = 300;
	private static String startDate = "2018-08-09T12:50:00";
	private static String endDate = "2018-08-20T18:20:00";

	// 매매량 세팅
	// "buyAll", "buyCertainNumber", "buyCertainPrice"
	// "sellAll", "sellCertainNumber", "sellCertainPrice"
	private static String buySetting = "buyAll";
	private static String sellSetting = "sellAll";

	// optional
	private static double priceBuyUnit;
	private static double priceSellUnit;
	private static double numBuyUnit;
	private static double numSellUnit;

	// errorHandle : 1 => 대기
	// 0 => shutdown
	private static int errorHandling = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// System.out.println(new BackTestingPerform(email, exchange, coin, base,
		// initialCash, interval, startDate,
		// endDate, strategyName, buySetting, sellSetting, priceBuyUnit, priceSellUnit,
		// numBuyUnit, numSellUnit,
		// errorHandling).backTestRun());

//		 new tradingBot(email, exchange, botName, coin, base, interval, startDate,
//		 endDate, strategyName, buySetting, sellSetting, priceBuyUnit, priceSellUnit,
//		 numBuyUnit, numSellUnit, errorHandling).botStart();

		// get24BiggstGap : 24시간동안 가격 차이가 가장 큰 (상승) 한 코인
//		Map<String, String> ret = new CoinRecommendation().get24BiggestGapCoin();
//		System.out.println(ret.get("binance"));
//		System.out.println(ret.get("bithumb"));
//		System.out.println(ret.get("coinone"));
		
		HitbtcAPI hapi = new HitbtcAPI("9753ad544eac3cae547d684ea422b35a", "2f23d39615c2fc406899e8dd6c375d88");
		
		System.out.println(hapi.getAllBalances());
		
		System.out.println(hapi.sellCoin("btc", "usdt", "1"));
		
		
		
		
		
	}

}