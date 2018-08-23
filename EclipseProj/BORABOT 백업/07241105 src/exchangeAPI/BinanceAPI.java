package exchangeAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import com.binance.api.client.*;
import com.binance.api.client.domain.account.*;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.request.AllOrdersRequest;
import com.binance.api.client.domain.account.request.CancelOrderRequest;
import com.binance.api.client.domain.account.request.OrderRequest;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.binance.api.client.exception.BinanceApiException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BinanceAPI implements exAPI{

	public static final String ORDERBOOK_BUY = "BUY", ORDERBOOK_SELL = "SELL", ORDERBOOK_BOTH = "BOTH";
	public static final int DEFAULT_RETRY_ATTEMPTS = 1;
	public static final int DEFAULT_RETRY_DELAY = 15;
	private static final Exception InvalidStringListException = new Exception("Must be in key-value pairs");
	private final String API_VERSION = "1", INITIAL_URL = "https://www.binance.com/api/";
	private final String PUBLIC = "public", MARKET = "market", ACCOUNT = "account";
	private final String encryptionAlgorithm = "HmacSHA256";
	private String apikey;
	private String secret;
	private final int retryAttempts;
	private int retryAttemptsLeft;
	private final int retryDelaySeconds;
	private BinanceApiClientFactory factory;
	private BinanceApiRestClient client;

	public BinanceAPI(String apikey, String secret, int retryAttempts, int retryDelaySeconds) {

		factory = BinanceApiClientFactory.newInstance(apikey, secret);
		client = factory.newRestClient();
		
		this.apikey = apikey;
		this.secret = secret;
		this.retryAttempts = retryAttempts;
		this.retryDelaySeconds = retryDelaySeconds;
		this.retryAttemptsLeft = retryAttempts;
	}

	public BinanceAPI(int retryAttempts, int retryDelaySeconds) {
		
		this.retryAttempts = retryAttempts;
		this.retryDelaySeconds = retryDelaySeconds;
		
		this.retryAttemptsLeft = retryAttempts;
	}
	
	public BinanceAPI() {

		this(DEFAULT_RETRY_ATTEMPTS, DEFAULT_RETRY_DELAY);
	}

	public String getCurrencies() { // Returns all currencies currently on Bittrex with their metadata

		return getJson(API_VERSION, "getcurrencies");
	}

	public double getTicker(String coin, String base) { // Returns current tick values for a specific market

		String symbol = coin+base;
		String result = getJson(API_VERSION, "ticker/price", returnCorrectMap("symbol", symbol.toUpperCase()));
		
		JsonObject ohlc_json = new JsonParser().parse(result).getAsJsonObject();
		double price = ohlc_json.get("price").getAsDouble();
	
		return price;
	}

	public String buyLimit(String market, String quantity, String rate) { // Places a limit buy in a specific market; returns the UUID of the order

		return getJson(API_VERSION, "buylimit", returnCorrectMap("market", market, "quantity", quantity, "rate", rate));
	}

	public String buyMarket(String market, String quantity) { // Places a market buy in a specific market; returns the UUID of the order

		return getJson(API_VERSION,  "buymarket", returnCorrectMap("market", market, "quantity", quantity));
	}
	
	public String buyCoin(String coin, String base, String qty) {
		
		String market = coin+base;
		Account account = client.getAccount();
		NewOrderResponse newOrderResponse = client.newOrder(NewOrder.marketBuy(market, qty).newOrderRespType(NewOrderResponseType.FULL));
		List<Trade> fills = newOrderResponse.getFills();
		System.out.println(newOrderResponse.getClientOrderId());

		return newOrderResponse.getClientOrderId();
	}
	
	public String sellCoin(String coin, String base, String qty) {
		
		String market = coin+base;
		Account account = client.getAccount();
		NewOrderResponse newOrderResponse = client.newOrder(NewOrder.marketSell(market, qty).newOrderRespType(NewOrderResponseType.FULL));
		List<Trade> fills = newOrderResponse.getFills();
		System.out.println(newOrderResponse.getClientOrderId());

		return newOrderResponse.getClientOrderId();
	}

	public String sellLimit(String market, String quantity, String rate) { // Places a limit sell in a specific market; returns the UUID of the order

		return getJson(API_VERSION, "selllimit", returnCorrectMap("market", market, "quantity", quantity, "rate", rate));
	}

	public String sellMarket(String market, String quantity) { // Places a market sell in a specific market; returns the UUID of the order

		return getJson(API_VERSION,  "sellmarket", returnCorrectMap("market", market, "quantity", quantity));
	}

	public String cancelOrder(String uuid) { // Cancels a specific order based on its UUID

		return getJson(API_VERSION, "cancel", returnCorrectMap("uuid", uuid));
	}

	public String getOpenOrders(String market) { // Returns your currently open orders in a specific market

		String method = "getopenorders";

		if(market.equals(""))

			return getJson(API_VERSION, method);

		return getJson(API_VERSION, method, returnCorrectMap("market", market));
	}

	public String getOpenOrders() { // Returns all your currently open orders

		return getOpenOrders("");
	}
	
	public String getBalances() { // Returns all balances in your account

		return getJson(API_VERSION, "getbalances");
	}

	public double getBalance(String currency) { // Returns a specific balance in your account
		
		Account account = client.getAccount();
		System.out.println(account.getBalances());
		System.out.println(account.getAssetBalance(currency).getFree());
		
		return Double.parseDouble(account.getAssetBalance(currency).getFree());
	}

	public String getOrder(String uuid) { // Returns information about a specific order (by UUID)

		return getJson(API_VERSION, "getorder", returnCorrectMap("uuid", uuid));
	}

	private HashMap<String, String> returnCorrectMap(String...parameters) { // Handles the exception of the generateHashMapFromStringList() method gracefully as to not have an excess of try-catch statements

		HashMap<String, String> map = null;

		try {

			map = generateHashMapFromStringList(parameters);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return map;
	}

	private HashMap<String, String> generateHashMapFromStringList(String...strings) throws Exception { // Method to easily create a HashMap from a list of Strings

		if(strings.length % 2 != 0)

			throw InvalidStringListException;

		HashMap<String, String> map = new HashMap<>();

		for(int i = 0; i < strings.length; i += 2) // Each key will be i, with the following becoming its value

			map.put(strings[i], strings[i + 1]);

		return map;
	}

	private String getJson(String apiVersion, String method) {

		return getResponseBody(generateUrl(apiVersion,method));
	}

	private String getJson(String apiVersion, String method, HashMap<String, String> parameters) {

		return getResponseBody(generateUrl(apiVersion, method, parameters));
	}

	private String generateUrl(String apiVersion, String method) {

		return generateUrl(apiVersion, method, new HashMap<String, String>());
	}

	private String generateUrl(String apiVersion, String method, HashMap<String, String> parameters) {

		String url = INITIAL_URL;
 
		url += "v" + apiVersion + "/";
		url += method;
		url += generateUrlParameters(parameters);

		return url;
	}

	private String generateUrlParameters(HashMap<String, String> parameters) { // Returns a String with the key-value pairs formatted for URL

		String urlAttachment = "?";

		Object[] keys = parameters.keySet().toArray();

		for(Object key : keys)

			urlAttachment += key.toString() + "=" + parameters.get(key) + "&";

		return urlAttachment;
	}

	public static List<HashMap<String, String>> getMapsFromResponse(String response) {

		final List<HashMap<String, String>> maps = new ArrayList<>();

		if(!response.contains("[")) {

			maps.add(jsonMapToHashMap(response.substring(response.lastIndexOf("\"result\":") + "\"result\":".length(), response.indexOf("}") + 1))); // Sorry.

		} else {

			final String resultArray = response.substring(response.indexOf("\"result\":") + "\"result\":".length() + 1, response.lastIndexOf("]"));

			final String[] jsonMaps = resultArray.split(",(?=\\{)");

			for(String map : jsonMaps)

				maps.add(jsonMapToHashMap(map));
		}

		return maps;
	}

	private static HashMap<String, String> jsonMapToHashMap(String jsonMap) {

		final HashMap<String, String> map = new HashMap<>();
		
		final String[] keyValuePairs = jsonMap.replaceAll("[{}]", "").split(",");
		
		for(String pair : keyValuePairs) {
			
			pair = pair.replaceAll("\"", "");
			final String[] pairValues = pair.split(":");
			
			map.put(pairValues[0], pairValues[1]);
		}
	    
	    return map;
	}
	
	private String getResponseBody(final String baseUrl) {

		String result = null;
		String urlString = baseUrl;

		try {
            URL url = new URL(urlString);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

			StringBuffer resultBuffer = new StringBuffer();
			String line = "";

			while ((line = reader.readLine()) != null)
				resultBuffer.append(line);

			result = resultBuffer.toString();

		} catch (UnknownHostException | SocketException e) {
			
			if(retryAttemptsLeft-- > 0) {
				
				System.err.printf("Could not connect to host - retrying in %d seconds... [%d/%d]%n", retryDelaySeconds, retryAttempts - retryAttemptsLeft, retryAttempts);
				
				try {
					
					Thread.sleep(retryDelaySeconds * 1000);
					
				} catch (InterruptedException e1) {
					
					e1.printStackTrace();
				}
				
				result = getResponseBody(baseUrl);
				
			} else {
				//Error 알람 
				//tradingBot.terminateBot();
				throw new ReconnectionAttemptsExceededException("Maximum amount of attempts to connect to host exceeded.");
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} finally {
			
			retryAttemptsLeft = retryAttempts;
		}

		return result;
	}
}

