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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Base64.Encoder;

public class BittrexAPI implements exAPI{

	public static final String ORDERBOOK_BUY = "buy", ORDERBOOK_SELL = "sell", ORDERBOOK_BOTH = "both";
	public static final int DEFAULT_RETRY_ATTEMPTS = 1;
	public static final int DEFAULT_RETRY_DELAY = 15;
	private static final Exception InvalidStringListException = new Exception("Must be in key-value pairs");
	
	private final String API_VERSION = "1.1", INITIAL_URL = "https://bittrex.com/api/";
	private final String PUBLIC = "public", MARKET = "market", ACCOUNT = "account";
	private final String encryptionAlgorithm = "HmacSHA512";
	
	private String apikey;
	private String secret;
	private final int retryAttempts;
	private int retryAttemptsLeft;
	private final int retryDelaySeconds;

	public BittrexAPI(String apikey, String secret, int retryAttempts, int retryDelaySeconds) {

		this.apikey = apikey;
		this.secret = secret;
		this.retryAttempts = retryAttempts;
		this.retryDelaySeconds = retryDelaySeconds;
		retryAttemptsLeft = retryAttempts;
	}

	public BittrexAPI(int retryAttempts, int retryDelaySeconds) {
		
		this.retryAttempts = retryAttempts;
		this.retryDelaySeconds = retryDelaySeconds;
		retryAttemptsLeft = retryAttempts;
	}
	
	public BittrexAPI() {

		this(DEFAULT_RETRY_ATTEMPTS, DEFAULT_RETRY_DELAY);
	}

	
	public String getCurrencies() { // Returns all currencies currently on Bittrex with their metadata

		return getJson(API_VERSION, PUBLIC, "getcurrencies");
	}

	public double getTicker(String coin, String base) { // Returns current tick values for a specific market

		String market = base + "-" + coin;
		String result = getJson(API_VERSION, PUBLIC, "getticker", returnCorrectMap("market", market));
		JsonObject ohlc_json = new JsonParser().parse(result).getAsJsonObject();
		JsonObject jsResult = ohlc_json.get("result").getAsJsonObject();
		double jsLast = jsResult.get("Last").getAsDouble();
		
		return jsLast;
	}

	public String buyCoin(String coin, String base, String quantity, String rate) { // Places a limit buy in a specific market; returns the UUID of the order

		String market = base + "-" + coin;
		return getJson(API_VERSION, MARKET, "buymarket", returnCorrectMap("market", market, "quantity", quantity, "rate", rate));
	}

	@Override
	public String buyCoin(String coin, String base, String quantity) // throws ExException // Places a market buy in a specific market; returns the UUID of the order
	{
		String market = base + "-" + coin;
		String ret = getJson(API_VERSION, MARKET, "buymarket", returnCorrectMap("market", market, "quantity", quantity));
		return ret;
	}

	
	public String sellCoin(String coin, String base, String quantity, String rate) { // Places a limit sell in a specific market; returns the UUID of the order

		String market = base + "-" + coin;
		return getJson(API_VERSION, MARKET, "selllimit", returnCorrectMap("market", market, "quantity", quantity, "rate", rate));
	}

	@Override
	public String sellCoin(String coin, String base, String quantity) { // Places a market sell in a specific market; returns the UUID of the order

		String market = base + "-" + coin;
		return getJson(API_VERSION, MARKET, "sellmarket", returnCorrectMap("market", market, "quantity", quantity));
	}

	public String cancelOrder(String uuid) { // Cancels a specific order based on its UUID

		return getJson(API_VERSION, MARKET, "cancel", returnCorrectMap("uuid", uuid));
	}

	public String getOpenOrders(String market) { // Returns your currently open orders in a specific market

		String method = "getopenorders";

		if(market.equals(""))

			return getJson(API_VERSION, MARKET, method);

		return getJson(API_VERSION, MARKET, method, returnCorrectMap("market", market));
	}

	public String getOpenOrders() { // Returns all your currently open orders

		return getOpenOrders("");
	}

	public String getAllBalances() { // Returns all balances in your account

		return getJson(API_VERSION, ACCOUNT, "getbalances");
	}

	public double getBalance(String currency) { // Returns a specific balance in your account

		String result = getJson(API_VERSION, ACCOUNT, "getbalance", returnCorrectMap("currency", currency));
		JsonObject ohlc_json = new JsonParser().parse(result).getAsJsonObject();
		String jsResult = ohlc_json.get("result").getAsJsonObject().get("Available").toString();
		
		if(jsResult.equals("null")) {
			return 0.0;
		}
		else {
			return ohlc_json.get("result").getAsJsonObject().get("Available").getAsDouble();
		}
	}


	public String getOrder(String uuid) { // Returns information about a specific order (by UUID)

		return getJson(API_VERSION, ACCOUNT, "getorder", returnCorrectMap("uuid", uuid));
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

	private String getJson(String apiVersion, String type, String method) {

		
		return getResponseBody(generateUrl(apiVersion, type, method));
	}

	private String getJson(String apiVersion, String type, String method, HashMap<String, String> parameters) {

		return getResponseBody(generateUrl(apiVersion, type, method, parameters));
	}

	private String generateUrl(String apiVersion, String type, String method) {

		return generateUrl(apiVersion, type, method, new HashMap<String, String>());
	}

	private String generateUrl(String apiVersion, String type, String method, HashMap<String, String> parameters) {

		String url = INITIAL_URL;

		url += "v" + apiVersion + "/";
		url += type + "/";
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
		final String urlString = baseUrl + "apikey=" + apikey + "&nonce=" + EncryptionUtility.generateNonce();

		try {

            URL url = new URL(urlString);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setRequestProperty("apisign", EncryptionUtility.calculateHash(secret, urlString, encryptionAlgorithm));
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
				// Error 알람
				// tradingBot.terminateBot();
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

