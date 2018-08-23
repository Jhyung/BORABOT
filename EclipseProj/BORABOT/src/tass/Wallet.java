package tass;

import java.sql.ResultSet;
import java.sql.SQLException;

import DB.DB;
import exchangeAPI.BinanceAPI;
import exchangeAPI.BithumbAPI;
import exchangeAPI.BittrexAPI;
import exchangeAPI.CoinoneAPI;
import exchangeAPI.exAPI;

public class Wallet {

	private static String email;
	
	public Wallet(String email){
		this.email = email;
	}
	
	public double getBalance(String exchange, String coin) {
		
		double result;
		
		/* DB에서 API Sec 키 가지고오기  */
		
		String KeySQL = String.format("Select api_key, secret_key from customer_key where email = '%s' and exchange_name = '%s'", email, exchange);
		DB dbkey = new DB();
		
		String apiKey = "";
		String secKey = "";
		
		try {
			ResultSet rsKey = dbkey.Query(KeySQL, "select");
			if(rsKey.next()) {
				apiKey = rsKey.getString(1);
				secKey = rsKey.getString(2);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		final exAPI exAPIobj;

		if (exchange.equals("bithumb")) {
			exAPIobj = (exAPI) new BithumbAPI(apiKey, secKey);
		} else if (exchange.equals("bittrex")) {
			exAPIobj = (exAPI) new BittrexAPI(apiKey, secKey, 10, 10);
		} else if (exchange.equals("binance")) {
			exAPIobj = (exAPI) new BinanceAPI(apiKey, secKey, 10, 10);
		} else {
			exAPIobj = (exAPI) new CoinoneAPI(apiKey, secKey, 10, 10);
		}
		
		
		result = exAPIobj.getBalance(coin);
		
		return result;
	}
	
}
