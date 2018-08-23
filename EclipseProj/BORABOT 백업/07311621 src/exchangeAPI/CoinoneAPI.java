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
import java.util.Base64.Encoder;
import javax.net.ssl.HttpsURLConnection;
import com.google.gson.Gson;

public class CoinoneAPI implements exAPI{

	public static final String ORDERBOOK_BUY = "BUY", ORDERBOOK_SELL = "SELL", ORDERBOOK_BOTH = "BOTH";
	public static final int DEFAULT_RETRY_ATTEMPTS = 1;
	public static final int DEFAULT_RETRY_DELAY = 15;
	private static final Exception InvalidStringListException = new Exception("Must be in key-value pairs");
	private final String INITIAL_URL = "https://api.coinone.co.kr/";
	private final String encryptionAlgorithm = "HmacSHA512";
	private String apikey;
	private String secret;
	private final int retryAttempts;
	private int retryAttemptsLeft;
	private final int retryDelaySeconds;

	public CoinoneAPI(String apikey, String secret, int retryAttempts, int retryDelaySeconds) {

		this.apikey = apikey;
		this.secret = secret;
		this.retryAttempts = retryAttempts;
		this.retryDelaySeconds = retryDelaySeconds;
		
		retryAttemptsLeft = retryAttempts;
	}

	public CoinoneAPI(int retryAttempts, int retryDelaySeconds) {
		
		this.retryAttempts = retryAttempts;
		this.retryDelaySeconds = retryDelaySeconds;
		
		retryAttemptsLeft = retryAttempts;
	}
	
	public CoinoneAPI() {

		this(DEFAULT_RETRY_ATTEMPTS, DEFAULT_RETRY_DELAY);
	}

	public String getCurrencies() { // Returns all currencies currently on Bittrex with their metadata

		return getJson("getcurrencies");
	}

	public double getTicker(String coin, String base) { // Returns current tick values for a specific market

		String mod_coin = coin.toLowerCase();
		String result =  getJson("ticker", returnCorrectMap("currency", mod_coin));
		
		return 0;
	}

	public String buyCoin(String coin, String base, String quantity) { // Places a market buy in a specific market; returns the UUID of the order

		//String nonce = "12";
		String nonce = EncryptionUtility.generateNonce();
		String payload = generateSalesPayload(base, "0", quantity, nonce);
		
		return postJson( "v2/order/limit_buy", returnCorrectMap("access_token", base, "qty", quantity,"currency", coin, "price", "0", "nonce", nonce), payload);
	}

	public String sellCoin(String coin, String base, String quantity) { // Places a limit sell in a specific market; returns the UUID of the order

		String nonce = EncryptionUtility.generateNonce();
		String payload = generateSalesPayload(base, "0", quantity, nonce);
		
		return postJson( "v2/order/limit_sell", returnCorrectMap("access_token", base, "qty", quantity,"currency", coin, "price", "0", "nonce", nonce), payload);
	}

	public String cancelOrder(String uuid) { // Cancels a specific order based on its UUID

		return getJson( "cancel", returnCorrectMap("uuid", uuid));
	}

	public String getOpenOrders(String market) { // Returns your currently open orders in a specific market

		String method = "getopenorders";

		if(market.equals(""))

			return getJson( method);

		return getJson( method, returnCorrectMap("market", market));
	}

	public String getOpenOrders() { // Returns all your currently open orders

		return getOpenOrders("");
	}
	
	public String getBalances() { // Returns all balances in your account

		return getJson( "getbalances");
	}

	public double getBalance(String coin) { // Returns a specific balance in your account

		String nonce =  "105";
		String payload = generateBalancePayload(coin, nonce);
		String result =  postJson("v2/account/balance", returnCorrectMap("access_token", coin, "nonce", nonce), payload);
		
		return 0;
	}

	public String getOrder(String uuid) { // Returns information about a specific order (by UUID)

		return getJson( "getorder", returnCorrectMap("uuid", uuid));
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

	private String getJson(String method) {

		return getResponseBody(generateUrl(method));
	}

	private String getJson(String method, HashMap<String, String> parameters) {

		return getResponseBody(generateUrl(method, parameters));
	}
	
	private String postJson(String method, String payload) {

		return postResponseBody(generateUrl(method), payload);
	}
	
	private String postJson(String method, HashMap<String, String> parameters, String payload) {

		return postResponseBody(generateUrl(method, parameters), payload);
	}

	private String generateUrl(String method) {

		return generateUrl(method, new HashMap<String, String>());
	}

	private String generateUrl(String method, HashMap<String, String> parameters) {

		String url = INITIAL_URL;
 
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
	
	private String generateSalesPayload(String access_token, String price, String qty, String nonce) {
		
		saledsPayloadClass obj = new saledsPayloadClass(access_token, price, qty, nonce);
		Gson gson = new Gson();
		String payloadJson = gson.toJson(obj);
		
		Encoder enc = java.util.Base64.getEncoder();
		byte[] encodebyte = enc.encode(payloadJson.getBytes());
		
		String payload = new String(encodebyte).trim();
		
		return payload;
	}
	
	private String generateBalancePayload(String access_token, String nonce) {
		
		balancePayloadClass obj = new balancePayloadClass(access_token, nonce);
		Gson gson = new Gson();
		String payloadJson = gson.toJson(obj);
		
		Encoder enc = java.util.Base64.getEncoder();
		byte[] encodebyte = enc.encode(payloadJson.getBytes());
		
		String payload = new String(encodebyte).trim();
		
		return payload;
	}
	
	private String postResponseBody(final String baseUrl, String payload) {
		
		String result = null;
		String urlString = baseUrl;
		urlString+="format=json";
		
		try {
			URL url = new URL(urlString);
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
			httpsURLConnection.setRequestMethod("POST");
			httpsURLConnection.setRequestProperty("content-type", "application/json");
			httpsURLConnection.setRequestProperty("X-COINONE-PAYLOAD", payload);
			httpsURLConnection.setRequestProperty("X-COINONE-SIGNATURE", EncryptionUtility.calculateHash(secret.toUpperCase(), payload, encryptionAlgorithm));
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

			StringBuffer resultBuffer = new StringBuffer();
			String line = "";

			while ((line = reader.readLine()) != null)
				resultBuffer.append(line);
			result = resultBuffer.toString();
		}
		catch(Exception e) {
		}
		
		return result;
		
	}
	
	private String getResponseBody(final String baseUrl) {

		String result = null;
		String urlString = baseUrl;
		urlString += "format=json";
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

class balancePayloadClass{
	
	private String access_token;
	private String nonce;
	
	public balancePayloadClass(String access_token, String nonce) {
		
		this.nonce = nonce;
		this.access_token = access_token;
		
	}
	
}

class saledsPayloadClass{
	
	private String access_token;
	private String price;
	private String qty;
	private String nonce;
	
	public saledsPayloadClass(String access_token, String price, String qty, String nonce) {
		this.access_token = access_token;
		this.price = price;
		this.qty = qty;
		this.nonce = nonce;
	}
	
}
