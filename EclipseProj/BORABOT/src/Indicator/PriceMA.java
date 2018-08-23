package Indicator;

import exchangeAPI.CryptowatchAPI;

public class PriceMA {

	private int period_day;
	private CryptowatchAPI crypt;
	private String exchange;
	private String coin;
	private String base;
	private int interval;
	
	public PriceMA(int period, CryptowatchAPI c, String e, String coin, String base, int interval) {
		this.period_day = period;
		this.crypt = c;
		this.exchange = e;
		this.coin = coin;
		this.base = base;
		this.interval = interval;
	}
	
	public double getPriceMA() throws Exception{
		
		double[] historyArr = IndicatorFunction.getHistoryArray(crypt, exchange, coin, base, interval, period_day);
		
		return sumDouble(historyArr) / historyArr.length;
		
	}
	
	public double sumDouble(double[] arr) {
		
		double ret=0;
		
		for(int i = 0; i < arr.length; i++) {
			
			ret += arr[i];
		}
		
		return ret;
	}
	
	
}
