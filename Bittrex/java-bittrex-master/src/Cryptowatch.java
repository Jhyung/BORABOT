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
import bittrexAPI.*;


public class Cryptowatch {

	final String baseUrl = "https://api.cryptowat.ch/markets/";
	private final int retryAttempts;
	private int retryAttemptsLeft;
	private final int retryDelaySeconds;
	
	public Cryptowatch(int retryAttempts, int retryDelaySecond) {
		retryAttemptsLeft = retryAttempts;
		this.retryDelaySeconds = retryDelaySecond;
		this.retryAttempts = retryAttempts;
	}
	
	protected String getOHLC(String exchange, String coin, long after, int interval) {
		
		String result = null;
		final String urlString = baseUrl + exchange + "/" + coin + "/" + "ohlc" + "?after=" + after + "&periods="+interval;
		System.out.println(urlString);
		try {
			URL url = new URL(urlString);
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
			//httpsURLConnection.setRequestMethod("GET");
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
				result = getOHLC(exchange, coin,after,interval);
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
	
	protected String getCurrentPrice(String exchange, String coin_crypto) {
		
		String result = null;
		//final String urlString = baseUrl + exchange + "/" + coin + "/" + "ohlc" + "?after=" + after + "&periods="+interval;
		final String urlString = baseUrl + exchange + "/" + coin_crypto +"/price";
		System.out.println(urlString);
		try {
			URL url = new URL(urlString);
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
			//httpsURLConnection.setRequestMethod("GET");
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
				result = getCurrentPrice(exchange, coin_crypto);
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
