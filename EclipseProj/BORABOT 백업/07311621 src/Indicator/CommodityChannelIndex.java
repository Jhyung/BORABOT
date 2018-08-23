package Indicator;

import java.time.LocalDateTime;
import java.time.ZoneId;

import exchangeAPI.CryptowatchAPI;
import tass.initializing;

// 테스트기간 + 지표설정값(period) - 1 만큼 배열만 주기!! -> ?!?!
// UTC로 줘야해~

public class CommodityChannelIndex implements calcIndicator {

	private int period_day;
	private CryptowatchAPI crypt;
	private String exchange;
	private String coin;
	private String base;
	private int interval;
	private double prevCCI;
	private int buyIndex;
	private int sellIndex;
	private double[] hArr_bt;
	int cnt = 0;
	
	public CommodityChannelIndex(int period, int buyIndex, int sellIndex, CryptowatchAPI c, String e, String coin, String base, int interval) throws Exception{
		this.period_day = period;
		this.crypt = c;
		this.exchange = e;
		this.coin = coin;
		this.base = base;
		this.interval = interval;
		
		this.prevCCI = getCCI();
	
		this.buyIndex = buyIndex;
		this.sellIndex = sellIndex;
		//System.out.println("CCI obj created ! - prevCCI : " + prevCCI);
	}
	
	//Bt
	public CommodityChannelIndex(int period, int buyIndex, int sellIndex, CryptowatchAPI c, String e, String coin, String base, double[] hArr) throws Exception{
		this.period_day = period;
		this.crypt = c;
		this.exchange = e;
		this.coin = coin;
		this.base = base;
		
	    this.hArr_bt = hArr;
		this.buyIndex = buyIndex;
		this.sellIndex = sellIndex;
		
		this.prevCCI = getCCI_bt(makeSublist(this.hArr_bt, cnt, period+period-1+cnt));
		cnt++;
	}
	
	@Override
	public int getDeterminConstant() throws Exception{
		
		// 0 대기 , 1 구매, -1 판매
		double nowCCI = getCCI();
		//System.out.print("prevCCI : " + prevCCI + " nowCCI : " + nowCCI);
		int det;
		if(prevCCI < buyIndex) {
			if(nowCCI > buyIndex) {
				det = 1;
			}
			else {
				det = 0;
			}
		}
		else if ( prevCCI > sellIndex) {
			if(nowCCI < sellIndex) {
				det = -1;
			}
			else {
				det = 0;
			}
		}
		else {
			det = 0;
		}
		//System.out.println(" / CCI deterConstnat : " + det);
		return det;
	}
	
	public int getDeterminConstant_bt() throws Exception{
		
		// 0 대기 , 1 구매, -1 판매
		double nowCCI = getCCI_bt(makeSublist(hArr_bt, cnt, period_day+period_day-1+cnt));
		cnt++;
		//System.out.print("prevCCI : " + prevCCI + " nowCCI : " + nowCCI);
		int det;
		if(prevCCI < buyIndex) {
			if(nowCCI > buyIndex) {
				det = 1;
			}
			else {
				det = 0;
			}
		}
		else if ( prevCCI > sellIndex) {
			if(nowCCI < sellIndex) {
				det = -1;
			}
			else {
				det = 0;
			}
		}
		else {
			det = 0;
		}
		//System.out.println(" / CCI deterConstnat : " + det);
		return det;
	}
	
	public double getCCI() throws Exception{
		
		double[] hArr;

		hArr = IndicatorFunction.getHistoryMeanPriceArray(crypt, exchange, coin, base, interval, period_day+period_day-1);
		double M = hArr[hArr.length-1];
		double N = getMean(makeSublist(hArr, hArr.length-period_day, hArr.length-1));
	
		double sumDev = 0;
		
		for(int i = 0; i < period_day; i++) {
		
			sumDev += Math.abs(N - hArr[hArr.length-1-i]);
		}
		
		double D = sumDev / period_day;
		return (M-N) / (D * 0.015);
	}
	
	public double getCCI_bt(double[] hArr) {
		
		double M = hArr[hArr.length-1+cnt];
		double N = getMean(makeSublist(hArr, hArr.length-period_day+cnt, hArr.length-1+cnt));
	
		double sumDev = 0;
		
		for(int i = 0; i < period_day; i++) {
		
			sumDev += Math.abs(N - hArr[hArr.length-1-i+cnt]);
		}
		
		double D = sumDev / period_day;
		return (M-N) / (D * 0.015);
		
	}
	
	private double[] makeSublist(double[] arr, int s, int e) {
		
		double[] ret = new double[e-s+1];
		
		for(int i = 0; i < e-s+1; i++) {
			
			ret[i] = arr[i+s];
			
		}
		return ret;
	}
	
	
	private double getMean(double[] hArr) {
		
		double sum=0;
		
		for(int i = 0; i<hArr.length; i++) {
			sum += hArr[i];
		}
		double ret = sum / hArr.length;
		return ret;
	}
	
	
}
