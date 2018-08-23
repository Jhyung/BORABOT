package backtest;

import Indicator.IndicatorFunction;
import exchangeAPI.CryptowatchAPI;

// period

public class gdVCross_bt implements calcIndicator_bt {

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
	private int initialStart;
	private int initialEnd;
	private double[][] HLCVArr;
	private double[] phArr;
	
	//mT = 평균종류. 0이면 SMA, 1이면 EMA
	public gdVCross_bt(int longd, int shortd, int mT,CryptowatchAPI c, String e, String coin, String base, int interval, double[][] hArr, int initialStart, int initialEnd) throws Exception {
		this.longd = longd;
		this.shortd = shortd;
		this.crypt = c;
		this.exchange = e;
		this.coin = coin;
		this.base = base;
		this.interval = interval;
		this.meanType = mT;
		this.HLCVArr = hArr;
		this.phArr = IndicatorFunction_bt.toVolumeHistory(hArr);
		this.initialEnd = initialEnd;
		this.initialStart = initialStart;
		
		double[] longTempArr = IndicatorFunction_bt.makeSublist(phArr, initialStart, initialEnd);
		double[] shortTempArr = IndicatorFunction_bt.makeSublist(phArr, initialStart+(longd-shortd), initialEnd);
		initialStart++;
		initialEnd++;
		try {
		this.prevLong = getVolumeMA(mT, longTempArr);
		this.prevShort = getVolumeMA(mT, shortTempArr);
		} catch (Exception e2){
			throw new Exception();
		}
	}
	
	public int getDeterminConstant() throws Exception {
		
		double[] longTempArr = IndicatorFunction_bt.makeSublist(phArr, initialStart, initialEnd);
		double[] shortTempArr = IndicatorFunction_bt.makeSublist(phArr, initialStart+(longd-shortd), initialEnd);

		initialStart++;
		initialEnd++;
		
		// 0 - 대기 , 1 - 구매, -1 - 판매
		double longMA = getVolumeMA(meanType, longTempArr);
		double shortMA = getVolumeMA(meanType, shortTempArr);
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
		
		//System.out.print("prevShort : " + prevShort + " prevLong : " + prevLong + " nowShort : " + shortMA + " nowLong : " + longMA);
		//System.out.println(" / gdCross deterConstant : " + det);
		return det;
		
	}
	
	public double getVolumeMA(int mT, double[] historyArr) throws Exception {
		
		if(mT == 0) {
		
			//double[] historyArr = IndicatorFunction_bt.getHistoryArray(crypt, exchange, coin, base, interval, period);
			
			return sumDouble(historyArr) / historyArr.length;
		}
		
		else {
			//double[] historyArr = IndicatorFunction_bt.getHistoryArray(crypt, exchange, coin, base, interval, period);
			
			return IndicatorFunction_bt.getEMA(historyArr);
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
