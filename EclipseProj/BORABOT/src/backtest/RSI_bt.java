package backtest;

import java.util.LinkedList;
import java.util.Queue;
import exchangeAPI.CryptowatchAPI;

// period + 1

public class RSI_bt implements calcIndicator_bt {

	private int period_day;
	private int interval;
	private String coin;
	private String exchange;
	private CryptowatchAPI crypt;
	private String base;
	private int buyIndex;
	private int sellIndex;
	private double[][] HLCArr;
	private int initialStart;
	private int initialEnd;
	
	// no breakthrough
	public RSI_bt (int day, int buyIndex, int sellIndex, CryptowatchAPI c, String e, String coin, String base, int interval, double[][] hArr,  int initialStart, int initialEnd) {
		this.period_day = day;
		this.coin = coin;
		this.exchange = e;
		this.crypt = c;
		this.base = base;
		this.interval = interval;
		this.buyIndex = buyIndex;
		this.sellIndex = sellIndex;
		this.initialEnd = initialEnd;
		this.initialStart = initialStart;
		this.HLCArr = hArr;
		this.initialEnd++;
		this.initialStart++;
	}
	
	public double[] toPriceHistory(double[][] arr) {
		
		double[] ret = new double[arr.length];
		
		for(int i = 0; i < arr.length; i++) {
			ret[i] = arr[i][2];
		}
		
		return ret;
	}
	
	public int getDeterminConstant() throws Exception {
		
		double[] phArr = toPriceHistory(HLCArr);
		//System.out.println("RSI : " + initialStart +" / "+initialEnd);
		System.out.println(initialStart + "/" + initialEnd);
		double[] tempArr = IndicatorFunction_bt.makeSublist(phArr, (initialStart++), (initialEnd++));
		System.out.println("phArr len : " + phArr.length);
		System.out.println("tempArr len : " + tempArr.length);
		System.out.println(initialStart + "/" + initialEnd+"\n");
		int det;
		double RSI;
		RSI = getRSI(tempArr);
		
		if(RSI < buyIndex) {
			det = 1;
		}
		else if(RSI > sellIndex) {
			det = -1;
		}
		else {
			det = 0;
		}
		//System.out.print("now RSI : " + RSI);
		//System.out.println(" / RSI deterConstant : " + det);
		return det;
	}
	
	public double getRSI(double[] hArr) throws Exception {
		
		//double[] hArr = null;
		//hArr = IndicatorFunction_bt.getHistoryArray(crypt, exchange, coin, base, interval, period_day+1);
		//initializing.printArray(hArr);
		//System.out.println("RSI : " + initialStart +" / "+initialEnd);
		double up=0;
		double down=0;
		System.out.println("plz" + hArr.length + "?" + period_day);
		for(int i = 0; i < period_day; i++) {
			
			if(hArr[i] < hArr[i+1]) {
				up += hArr[i+1] - hArr[i];
			}
			else if(hArr[i] > hArr[i+1]){
				down += hArr[i] - hArr[i+1];
			}
		}
		
		double RS = (up/period_day) / (down/period_day);
		
		double RSI = up / (up+down) * 100.0;
		RSI = 100 - (100 / (1 + RS));
		//System.out.println("RSI test cnt : " + cnt);
		return RSI;
	}
	
	public double sumDouble(double[] list) {
		
		double ret=0;
		
		for(int i = 0; i < list.length; i++) {
			
			ret += list[i];
			
		}
		
		return ret;
	}
}
