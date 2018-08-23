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


public class CryptowatchAPI {

	private static String exchangeList[] = {"borabit", "coinone", "binance", "bithumb", "bittrex"};
	private static String coinList[] = {"btc", "eth", "ltc", "xrp"};
	
	final String baseUrl = "https://api.cryptowat.ch/markets/";
	private final int retryAttempts;
	private int retryAttemptsLeft;
	private final int retryDelaySeconds;

	public CryptowatchAPI(int retryAttempts, int retryDelaySecond) {
		retryAttemptsLeft = retryAttempts;
		this.retryDelaySeconds = retryDelaySecond;
		this.retryAttempts = retryAttempts;
	}
	
	public String getOHLC(String exchange, String coin, String base, long after, int interval) {
		
		String market = coin + base;
		String result = null;
		final String urlString = baseUrl + exchange + "/" + market + "/" + "ohlc" + "?after=" + after + "&periods="+interval;
		//System.out.println(urlString);
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
				result = getOHLC(exchange, coin,base,after,interval);
			} else {
				//Error 알람 전송 및 terminate;
				//tradingBot.terminateBot();
				throw new ReconnectionAttemptsExceededException("Maximum amount of attempts to connect to host exceeded.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			retryAttemptsLeft = retryAttempts;
		}
		
		//System.out.println("crypto result : \n" + result);
		return result;
	}

	
	public String getOHLC_bt(String exchange, String coin, String base, long after, long before, int interval) {
		
		String market = coin + base;
		String result = null;
		final String urlString = baseUrl + exchange + "/" + market + "/" + "ohlc" + "?after=" + after + "&periods=" + interval + "&before=" + before;
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
				result = getOHLC(exchange, coin,base,after,interval);
			} else {
				//Error 알람 전송 및 terminate;
				//tradingBot.terminateBot();
				throw new ReconnectionAttemptsExceededException("Maximum amount of attempts to connect to host exceeded.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			retryAttemptsLeft = retryAttempts;
		}
		
		//System.out.println("crypto result : \n" + result);
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	protected String getOHLC() {
	
		String result = null;
		final String urlString = baseUrl;// + exchange + "/" + market + "/" + "ohlc?" + "periods=" + period + "&after=" + after;
		//System.out.println(urlString);
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
				
				result = getOHLC();
				
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
