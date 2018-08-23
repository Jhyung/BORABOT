package exchangeAPI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import hitbtc.api.TradeAPI;
import hitbtc.internal.APIMode;
import DB.DB_ohlc;

public class HitbtcAPI implements exAPI {

	private static HitBTC ht;

	public HitbtcAPI(String APIKey, String SecKey) {
		ht = new HitBTC(APIMode.PRODUCTION, APIKey, SecKey);

	}

	@Override
	public String buyCoin(String coin, String base, String qty) {
		// TODO Auto-generated method stub
		String result = "";
		String symb;

		if (base.equals("usdt")) {
			symb = coin + "usd";
		}

		else {
			symb = coin + base;
		}
		
		
		try {
			TradeAPI tApi = ht.tradeAPI();
			Map<String, String> orderData = new HashMap<String, String>();
			orderData.put("symbol", symb);
			orderData.put("side", "buy");
			orderData.put("type", "market");
			orderData.put("timeInForce", "IOC");
			orderData.put("quantity", qty);

			result = tApi.createNewOrder(orderData).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public String sellCoin(String coin, String base, String qty) {
		// TODO Auto-generated method stub

		String result = "";
		if (base.equals("usdt")) {
			base = "usd";
		}
		String symb = coin + base;

		try {
			TradeAPI tApi = ht.tradeAPI();
			Map<String, String> orderData = new HashMap<String, String>();
			orderData.put("symbol", symb);
			orderData.put("side", "sell");
			orderData.put("type", "market");
			orderData.put("timeInForce", "IOC");
			orderData.put("quantity", qty);

			result = tApi.createNewOrder(orderData).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public double getTicker(String coin, String base) {
		// TODO Auto-generated method stub

		String symb = coin + base;

		DB_ohlc db = new DB_ohlc();
		String sql = String.format("SELECT price from hitbtcOneMinute%s ORDER BY t_id DESC LIMIT 1",
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

	public double getBalance(String currency) {
		// TODO Auto-generated method stub
		double resultBalance = 0;

		try {
			TradeAPI tApi = ht.tradeAPI();
			String res = tApi.getAllBalances2();
			JsonArray jsArr = new JsonParser().parse(res).getAsJsonObject().get("balance").getAsJsonArray();

			for (int i = 0; i < jsArr.size(); i++) {
				JsonObject tempObj = jsArr.get(i).getAsJsonObject();
				if (tempObj.get("currency_code").getAsString().equals(currency.toUpperCase())) {
					resultBalance = tempObj.get("cash").getAsDouble();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultBalance;

	}

	@Override
	public String getAllBalances() {
		// TODO Auto-generated method stub

		String result = "";
		try {
			TradeAPI tApi = ht.tradeAPI();
			result = tApi.getAllBalances2();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}
}
