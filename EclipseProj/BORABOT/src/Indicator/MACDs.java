package Indicator;

import exchangeAPI.CryptowatchAPI;

public class MACDs implements calcIndicator{

	private int longd;
	private int shortd;
	private int signald;
	private CryptowatchAPI crypt;
	private String exchange;
	private String coin;
	private String base;
	private int interval;
	
	public MACDs(int longd, int shortd, int signald, CryptowatchAPI c, String e, String coin, String base, int interval) {
		this.longd = longd;
		this.shortd = shortd;
		this.signald = signald;
		this.crypt = c;
		this.exchange = e;
		this.coin = coin;
		this.base = base;
		this.interval = interval;
	}
	
	@Override
	public int getDeterminConstant() {
		return 0;
	}
	
	// MACD 에서 Signal을 뺸 값을 리턴함.
	// MACD 자체는 함수의 PPO로 표현(Percent)
	// 0 을 기준으로 상향돌파 하향돌파를 보면 됨
	public double getPMACDOsc() throws Exception {
		double[] hArr = IndicatorFunction.getHistoryArray(crypt, exchange, coin, base, interval, longd+signald-1);
		
		double shortEMA = IndicatorFunction.getMACDEma(IndicatorFunction.makeSublist(hArr, longd+signald-1-shortd, longd+signald-2));
		double longEMA = IndicatorFunction.getMACDEma(IndicatorFunction.makeSublist(hArr, signald-1, longd+signald-2));
		int a1 = longd+signald-1-shortd+1;
		int b1 = longd+signald-1;
		int c1 = signald-1+1;
		int d1 = longd+signald-1;
		System.out.println("0th : " + a1 +"~"+ b1+" / "+c1+"~"+d1+" harr len " + hArr.length);
		
		
		double CDMAList[] = new double[signald];
		for(int i = 0; i < signald; i++) {
			
			double sEMATemp = IndicatorFunction.getMACDEma(IndicatorFunction.makeSublist(hArr, longd+signald-2-i+1-shortd, longd+signald-2-i));
			double lEMATemp = IndicatorFunction.getMACDEma(IndicatorFunction.makeSublist(hArr, longd+signald-2-i+1-longd, longd+signald-2-i));
			
			int a = longd+signald-2-i+1-shortd;
			int b = longd+signald-2-i;
			int c = longd+signald-2-i+1-longd;
			int d = longd+signald-2-i;
			//System.out.println(i + "th : " + a +"~"+ b+" / "+c+"~"+d);
			
			//CDMAList[i] = sEMATemp - lEMATemp;
			CDMAList[i] = sEMATemp - lEMATemp;
		}
		
		double signalEMA = IndicatorFunction.getMACDEma(CDMAList);
		double MACD = (shortEMA - longEMA);
		System.out.println("shorEMA : " + shortEMA + " / longEMA : " + longEMA + " / signalEMA : " + signalEMA + " / MACD : "+ MACD + " / testMACDlst : " + CDMAList[0]);
		return MACD - signalEMA;
	}
	
	private double getMean(double[] hArr) {
		
		double sum=0;
		
		for(int i = 0; i<hArr.length; i++) {
			sum += hArr[i];
		}
		double ret = sum / hArr.length;
		return ret;
	}
	
	public double getVMACDOsc() throws Exception{
		
		double[] hArr = IndicatorFunction.getHistoryVolumeArray(crypt, exchange, coin, base, interval, longd);
		
		double shortEMA = IndicatorFunction.getEMA(IndicatorFunction.makeSublist(hArr, longd-shortd, longd-1));
		double longEMA = IndicatorFunction.getEMA(hArr);
		double singalEMA = IndicatorFunction.getEMA(IndicatorFunction.makeSublist(hArr, longd-signald, longd-1));
		
		double PPO = (shortEMA - longEMA);
		
		//System.out.println("shorEMA : " + shortEMA + " / longEMA : " + longEMA + " / signalEMA : " + singalEMA + " / MACD : " + PPO);
		return PPO - singalEMA;
		
	}
}
