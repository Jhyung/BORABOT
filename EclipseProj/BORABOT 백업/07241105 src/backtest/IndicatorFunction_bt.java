package backtest;

import exchangeAPI.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import com.google.gson.*;


class IndicatorFunction_bt {
	
	// return[0] = 고가, return[1] = 저가, return[2] = 종가, return[3] = 볼륨
	
	public static double[][] get_HLCV_HistoryArray(CryptowatchAPI crypt, String exchange, String coin, String base, int interval, long after, long before) throws Exception {

		Gson gson = new Gson();
		Date date = new Date();

		int size = (int) (before - after) / interval;
		
		String ohlc_string = crypt.getOHLC_bt(exchange, coin, base, after, before, interval);
		JsonObject ohlc_json = new JsonParser().parse(ohlc_string).getAsJsonObject();
		String ohlc_result_string = gson.toJson(ohlc_json.get("result"));
		JsonObject ohlc_result_json = new JsonParser().parse(ohlc_result_string).getAsJsonObject();
		JsonArray ohlc_jsarr = ohlc_result_json.get(Long.toString(interval)).getAsJsonArray();
		// System.out.println("제이슨 크기 " + ohlc_jsarr.size());
		double[][] historyArray = new double[size][5];

		//System.out.println("ohlc_jsarr : \n" + ohlc_jsarr);
		//System.out.println("jsarr size : " + ohlc_jsarr.size());
		//System.out.println("size : " + size); 
		
		JsonArray jsarr;
		int bojung = ohlc_jsarr.size() - size;
		if(bojung < 0) {
			System.out.println("cryptowatch error!");
			//Error
			throw new Exception();
		}

		for (int i = 0; i < size; i++) {
			jsarr = ohlc_jsarr.get(i+bojung).getAsJsonArray();
			// 4번째가 종가데이터
			historyArray[i][2] = jsarr.get(4).getAsDouble();
			// 3번째가 저가
			historyArray[i][1] = jsarr.get(3).getAsDouble();
			// 2번째가 고가
			historyArray[i][0] = jsarr.get(2).getAsDouble();
			// 5번쨰가 볼륨
			historyArray[i][3] = jsarr.get(5).getAsDouble();
			//
			historyArray[i][4] = jsarr.get(0).getAsLong();
		}

		// System.out.println("get historyarray done");
		return historyArray;
	}
	 
	
	
	public static Queue<Double> getHistoryQueue(CryptowatchAPI crypt, String exchange, String coin, String base, int interval, int period_day) throws Exception{

		Gson gson = new Gson();
		Date date = new Date();
		long U_current = date.getTime() / 1000;
		int size = period_day;

		String ohlc_string = crypt.getOHLC(exchange, coin, base, U_current - (period_day * interval), interval);
		JsonObject ohlc_json = new JsonParser().parse(ohlc_string).getAsJsonObject();
		String ohlc_result_string = gson.toJson(ohlc_json.get("result"));
		JsonObject ohlc_result_json = new JsonParser().parse(ohlc_result_string).getAsJsonObject();
		JsonArray ohlc_jsarr = ohlc_result_json.get(Long.toString(interval)).getAsJsonArray();

		Queue<Double> historyQueue = new LinkedList<Double>();

		JsonArray jsarr;
		int bojung = ohlc_jsarr.size() - size;
		if(bojung < 0) {
			System.out.println("cryptowatch error!");
			//Error
			throw new Exception();
		}


		for (int i = 0; i < size; i++) {
			jsarr = ohlc_jsarr.get(i+bojung).getAsJsonArray();
			// 4번째가 종가데이터
			historyQueue.add(jsarr.get(4).getAsDouble());
		}

		return historyQueue;
	}

	public static double[] getHistoryArray(CryptowatchAPI crypt, String exchange, String coin, String base, int interval, int period_day) throws Exception {

		Gson gson = new Gson();
		Date date = new Date();
		long U_current = date.getTime() / 1000;
		int size = period_day;
		
		String ohlc_string = crypt.getOHLC(exchange, coin, base, U_current - (period_day * interval), interval);
		JsonObject ohlc_json = new JsonParser().parse(ohlc_string).getAsJsonObject();
		String ohlc_result_string = gson.toJson(ohlc_json.get("result"));
		JsonObject ohlc_result_json = new JsonParser().parse(ohlc_result_string).getAsJsonObject();
		JsonArray ohlc_jsarr = ohlc_result_json.get(Long.toString(interval)).getAsJsonArray();
		// System.out.println("제이슨 크기 " + ohlc_jsarr.size());
		double[] historyArray = new double[size];

		JsonArray jsarr;
		int bojung = ohlc_jsarr.size() - size;
		if(bojung < 0) {
			System.out.println("cryptowatch error!");
			//Error
			throw new Exception();
		}

		for (int i = 0; i < size; i++) {
			jsarr = ohlc_jsarr.get(i+bojung).getAsJsonArray();
			// 4번째가 종가데이터
			historyArray[i] = jsarr.get(4).getAsDouble();
		}

		// System.out.println("get historyarray done");
		return historyArray;
	}
	
	public static double[] getHistoryVolumeArray(CryptowatchAPI crypt, String exchange, String coin, String base, int interval, int period_day) throws Exception {

		Gson gson = new Gson();
		Date date = new Date();
		long U_current = date.getTime() / 1000;
		int size = period_day;
		
		String ohlc_string = crypt.getOHLC(exchange, coin, base, U_current - (period_day * interval), interval);
		JsonObject ohlc_json = new JsonParser().parse(ohlc_string).getAsJsonObject();
		String ohlc_result_string = gson.toJson(ohlc_json.get("result"));
		JsonObject ohlc_result_json = new JsonParser().parse(ohlc_result_string).getAsJsonObject();
		JsonArray ohlc_jsarr = ohlc_result_json.get(Long.toString(interval)).getAsJsonArray();
		// System.out.println("제이슨 크기 " + ohlc_jsarr.size());
		double[] historyArray = new double[size];

		JsonArray jsarr;
		int bojung = ohlc_jsarr.size() - size;
		if(bojung < 0) {
			System.out.println("cryptowatch error!");
			//Error
			throw new Exception();
		}

		for (int i = 0; i < size; i++) {
			jsarr = ohlc_jsarr.get(i+bojung).getAsJsonArray();
			// 4번째가 종가데이터
			historyArray[i] = jsarr.get(5).getAsDouble();
		}

		//initializing.printArray(historyArray);
		// System.out.println("get historyarray done");
		return historyArray;
	}
	
	public static double[] getHistoryMeanPriceArray_bt(CryptowatchAPI crypt, String exchange, String coin, String base, int interval, long after, long before) throws Exception {

		Gson gson = new Gson();
		Date date = new Date();
		
		String ohlc_string = crypt.getOHLC_bt(exchange, coin, base, after, before, interval);
		JsonObject ohlc_json = new JsonParser().parse(ohlc_string).getAsJsonObject();
		String ohlc_result_string = gson.toJson(ohlc_json.get("result"));
		JsonObject ohlc_result_json = new JsonParser().parse(ohlc_result_string).getAsJsonObject();
		JsonArray ohlc_jsarr = ohlc_result_json.get(Long.toString(interval)).getAsJsonArray();
		// System.out.println("제이슨 크기 " + ohlc_jsarr.size());
		double[] historyArray = new double[ohlc_jsarr.size()];

		JsonArray jsarr;

		for (int i = 0; i < ohlc_jsarr.size(); i++) {
			jsarr = ohlc_jsarr.get(i).getAsJsonArray();
			// 4번째가 종가데이터
			historyArray[i] = ( jsarr.get(2).getAsDouble() + jsarr.get(3).getAsDouble() + jsarr.get(4).getAsDouble() ) / 3;
		}

		//initializing.printArray(historyArray);
		// System.out.println("get historyarray done");
		return historyArray;
	}
	
	public static double[] getHistoryMeanPriceArray(CryptowatchAPI crypt, String exchange, String coin, String base, int interval, int period_day) throws Exception {

		Gson gson = new Gson();
		Date date = new Date();
		long U_current = date.getTime() / 1000;
		int size = period_day;
		
		String ohlc_string = crypt.getOHLC(exchange, coin, base, U_current - (period_day * interval), interval);
		JsonObject ohlc_json = new JsonParser().parse(ohlc_string).getAsJsonObject();
		String ohlc_result_string = gson.toJson(ohlc_json.get("result"));
		JsonObject ohlc_result_json = new JsonParser().parse(ohlc_result_string).getAsJsonObject();
		JsonArray ohlc_jsarr = ohlc_result_json.get(Long.toString(interval)).getAsJsonArray();
		// System.out.println("제이슨 크기 " + ohlc_jsarr.size());
		double[] historyArray = new double[size];

		JsonArray jsarr;
		int bojung = ohlc_jsarr.size() - size;
		if(bojung < 0) {
			System.out.println("cryptowatch error!");
			//Error
			throw new Exception();
		}


		for (int i = 0; i < size; i++) {
			jsarr = ohlc_jsarr.get(i+bojung).getAsJsonArray();
			// 4번째가 종가데이터
			historyArray[i] = ( jsarr.get(2).getAsDouble() + jsarr.get(3).getAsDouble() + jsarr.get(4).getAsDouble() ) / 3;
		}

		//initializing.printArray(historyArray);
		// System.out.println("get historyarray done");
		return historyArray;
	}
	
	public static double getEMA(double[] arr) {
		
		int len = arr.length;
		double d[] = new double[len];
		d[0] = arr[0];

		for(int i = 1; i < len; i++) {
			double k;
			k = 2.0/(i+2);
			d[i] = d[i-1] * (1-k) + arr[i] * k;
		}
		
		return d[len-1];
	}
	
	public static double getMACDEma(double[] arr) {
		
		int len = arr.length;
		double d[] = new double[len];
		d[0] = sumDouble(arr) / len;
		
		for(int i = 1; i < len; i++) {
			
			double factor = ( 2 / (i+1 + 1));
			d[i] = (arr[i] - d[i-1]) * factor + d[i-1];
		}
		
		return d[len-1];
		
	}
	
	public static double sumDouble(double[] arr) {
		
		double ret=0;
		
		for(int i = 0; i < arr.length; i++) {
			
			ret += arr[i];
		}
		
		return ret;
	}
	
	public static double[] makeSublist(double[] arr, int s, int e) {
		
		double[] ret = new double[e-s+1];
		
		for(int i = 0; i < e-s+1; i++) {
			
			ret[i] = arr[i+s];
			
		}
		return ret;
	}
	
	public static double[][] makeSublist2d(double[][] arr, int s, int e){
		
		double[][] ret = new double[e-s+1][];
		
		for(int i = 0; i < e-s+1; i++) {
			
			ret[i] = arr[i+s];
		}
		
		return ret;
	}
	
	
	public static double[] toPriceHistory(double[][] arr) {
		
		double[] ret = new double[arr.length];
		
		for(int i = 0; i < arr.length; i++) {
			ret[i] = arr[i][2];
		}
		
		return ret;
	}

	public static double[] toVolumeHistory(double[][] arr) {
		
		double[] ret = new double[arr.length];
		
		for(int i = 0; i < arr.length; i++) {
			ret[i] = arr[i][3];
		}
		
		return ret;
	}



}
