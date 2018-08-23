
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


public class testCrypto {

   private static String exchangeList[] = {"borabit", "coinone", "binance", "bithumb", "bittrex"};
   private static String coinList[] = {"btc", "eth", "ltc", "xrp"};
   
   static String baseUrl = "https://api.cryptowat.ch/markets/";
   private static int retryAttempts = 10;
   private static int retryAttemptsLeft = 10;
   private static int retryDelaySeconds = 10;

   public static void main(String[] args) {
      
      int intervalList[] = {300,1800,3600, 21600, 43200, 86400};
      String exchangeList[] = {"bithumb", "binance", "coinone", "bitfinex"};
      String coinList[] = {"btc", "eth"};
      int interval = 300;
      
      while(true) {
         
         for(int i = 0; i < exchangeList.length; i++) {
            
            for(int j = 0; j < coinList.length; j++) {
               String result;
               
               if(exchangeList[i] == "binance") {
                  result = getOHLC(exchangeList[i], "bnb", coinList[j],  150000, interval);
               }
               else if(exchangeList[i] == "bithumb" || exchangeList[i] == "coinone") {
                  result = getOHLC(exchangeList[i], coinList[j], "krw", 150000, interval);
               }
               else {
                  result = getOHLC(exchangeList[i], coinList[j], "usd", 150000, interval);
               }
               //System.out.println(result);
               String first = result.split(":\\[\\[")[1];
               String second[] = first.split("\\],\\[");
               long initUnixTime = Long.parseLong(second[0].split(",")[0]);
               for(int k = 1; k<second.length; k++) {
                  
                  long unixTime = Long.parseLong(second[k].split(",")[0]);
                  //System.out.println(unixTime);
                  if(unixTime - initUnixTime == interval) {
                     
                  }
                  else {
                     System.out.println("error! - " + exchangeList[i] + " " + coinList[j] + " / prev :  " + initUnixTime + " now : " + unixTime);
                  }
                  
                  initUnixTime = unixTime;
               }
            }
         }
         try {
            Thread.sleep(10000);
            } catch(Exception e) {
               
            }
      }
   }
   
   public static String getOHLC(String exchange, String coin, String base, long after, int interval) {
      
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
            System.out.println("Maximum amount of attempts to connect to host exceeded.");
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
            System.out.println("Maximum amount of attempts to connect to host exceeded.");
            
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
            
            System.out.println("Maximum amount of attempts to connect to host exceeded.");
         }
         
      } catch (IOException e) {
         
         e.printStackTrace();
         
      } finally {
         
         retryAttemptsLeft = retryAttempts;
      }

      return result;
   }

}