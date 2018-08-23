package Indicator;

import exchangeAPI.CryptowatchAPI;

public class gdCross implements calcIndicator {

	private int longd;
	private int shortd;
	private CryptowatchAPI crypt;
	private String exchange;
	private String coin;
	private String base;
	private int interval;
	private int meanType;
	private double prevLong;
	private double prevShort;
	
	//mT = 평균종류. 0이면 SMA, 1이면 EMA
	public gdCross(int longd, int shortd, int mT,CryptowatchAPI c, String e, String coin, String base, int interval) throws Exception {
		this.longd = longd;
		this.shortd = shortd;
		this.crypt = c;
		this.exchange = e;
		this.coin = coin;
		this.base = base;
		this.interval = interval;
		this.meanType = mT;
		try {
		this.prevLong = getPriceMA(longd, mT);
		this.prevShort = getPriceMA(shortd, mT);
		} catch (Exception e2){
			throw new Exception();
		}
	}
	
	public int getDeterminConstant() throws Exception {
		
		// 0 - 대기 , 1 - 구매, -1 - 판매
		double longMA = getPriceMA(longd, meanType);
		double shortMA = getPriceMA(shortd, meanType);
		int det;
		
		if(prevShort < prevLong) {
			if(shortMA > longMA) {
				//단기가 장기 위로 : 매수
				det= 1;
			}
			else {
				det= 0;
			}
		}
		else if(prevShort > prevLong){
			if(shortMA < longMA) {
				//장기가 단기 위로 : 매도
				det= -1;
			}
			else {
				det= 0;
			}
		}
		else {
			det= 0;
		}
		
		System.out.print("prevShort : " + prevShort + " prevLong : " + prevLong + " nowShort : " + shortMA + " nowLong : " + longMA);
		System.out.println(" / gdCross deterConstant : " + det);
		return det;
		
	}
	
	public double getPriceMA(int period, int mT) throws Exception {
		
		if(mT == 0) {
		
			double[] historyArr = IndicatorFunction.getHistoryArray(crypt, exchange, coin, base, interval, period);
			
			return sumDouble(historyArr) / historyArr.length;
		}
		
		else {
			double[] historyArr = IndicatorFunction.getHistoryArray(crypt, exchange, coin, base, interval, period);
			
			return IndicatorFunction.getEMA(historyArr);
		}
		
	}
	
	public double sumDouble(double[] arr) {
		
		double ret=0;
		
		for(int i = 0; i < arr.length; i++) {
			
			ret += arr[i];
		}
		
		return ret;
	}
	
	
	
}
