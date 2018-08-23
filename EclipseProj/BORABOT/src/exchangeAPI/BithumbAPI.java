package exchangeAPI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import DB.DB_ohlc;

public class BithumbAPI implements exAPI {

	private String api_key;
	private String secret_key;
	private Api_Client api;

	public BithumbAPI(String api_key, String secret_key) {
		api = new Api_Client(api_key, secret_key);
	}

	@Override
	public double getTicker(String coin, String base) {

		// HashMap<String, String> rgParams = new HashMap<String, String>();
		// // order : coin
		// // payment : base
		// rgParams.put("order_currency", coin);
		// rgParams.put("payment_currency", base);
		//
		// String result = null;
		// try {
		// result = api.callApi("/public/ticker/"+coin, rgParams);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// if(result == null) {
		// //tradingBot.terminateBot();
		// //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ에러 알람!
		// }
		//
		// //System.out.println(result);
		// JsonObject ohlc_json = new JsonParser().parse(result).getAsJsonObject();
		// JsonObject data = ohlc_json.get("data").getAsJsonObject();
		// double balance = data.get("closing_price").getAsDouble();
		//
		// return balance;

		String symb = coin;

		DB_ohlc db = new DB_ohlc();
		String sql = String.format("SELECT price from bithumbOneMinute%s ORDER BY t_id DESC LIMIT 1",
				symb.toUpperCase());
		double res = 0;

		try {
			ResultSet rs = db.Query(sql, "select");
			while (rs.next()) {
				res = rs.getDouble(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	@Override
	public String buyCoin(String coin, String base, String qty) {

		String units = qty;
		String currency = coin;

		HashMap<String, String> rgParams = new HashMap<String, String>();

		rgParams.put("order_currency", coin);
		rgParams.put("payment_currency", base);
		rgParams.put("units", units);
		rgParams.put("currency", currency);

		String result = null;
		try {
			result = api.callApi("/trade/market_buy/", rgParams);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public double manageAsset(double nowAsset, String result) {

		double ret = 0;

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(result);
		JsonObject jsnObj = element.getAsJsonObject();

		return ret;
	}

	public String sellCoin(String coin, String base, String qty) {

		String units = qty;
		String currency = coin;

		HashMap<String, String> rgParams = new HashMap<String, String>();

		rgParams.put("order_currency", coin);
		rgParams.put("payment_currency", base);
		rgParams.put("units", units);
		rgParams.put("currency", currency);

		String result = null;
		try {
			result = api.callApi("/trade/market_sell/", rgParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public double getBalance(String coin) {

		HashMap<String, String> rgParams = new HashMap<String, String>();

		if(coin.equals("KRW") || coin.equals("krw")) {
			coin = "btc";
		}
		rgParams.put("currency", coin);

		String result = null;
		try {
			result = api.callApi("/info/balance", rgParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 System.out.println(result);
		JsonObject ohlc_json = new JsonParser().parse(result).getAsJsonObject();
		JsonObject data = ohlc_json.get("data").getAsJsonObject();

		double balance;
		if (coin.equals("krw") || coin.equals("KRW")) {
			balance = data.get("available_krw").getAsDouble();
		} else {
			balance = data.get("available_" + coin).getAsDouble();
		}

		return balance;
	}

	@Override
	public String getAllBalances() { // Returns all balances in your account

		HashMap<String, String> rgParams = new HashMap<String, String>();

		rgParams.put("order_currency", "ALL");
		rgParams.put("payment_currency", "krw");

		String result = null;
		try {
			result = api.callApi("/info/balance", rgParams);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
