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
		double[] tempArr = IndicatorFunction_bt.makeSublist(phArr, initialStart++, initialEnd++);
		
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
	
	public double getWilderRSI() throws Exception {
		
		double[] hArr = null;
		
		hArr = IndicatorFunction_bt.getHistoryArray(crypt, exchange, coin, base, interval, period_day+1);
		Queue<Double> upList = new LinkedList<Double>();
		Queue<Double> downList = new LinkedList<Double>();
		double up = 0;
		double down = 0;
		
		for(int i = 0; i < period_day; i++) {
			
			if(hArr[i] < hArr[i+1]) {
				upList.add(hArr[i+1] - hArr[i]);
				up += (hArr[i+1] - hArr[i]);
			}
			else if(hArr[i] > hArr[i+1]){
				downList.add(hArr[i] - hArr[i+1]);
				down += (hArr[i] - hArr[i+1]);
			}
		}
		
		double[] upD = new double[upList.size()];
		double[] downD = new double[downList.size()];
		upD[0] = up / period_day;//upList.remove();
		downD[0] = down / period_day;//downList.remove();
		
		for(int i = 1; i < upD.length; i++) {
		
			upD[i] = (upD[i-1] * (period_day-1) + upList.remove())/period_day;
		}
		for(int i = 1; i < downD.length; i++) {
			
			downD[i] = (downD[i-1] * (period_day-1) + downList.remove())/period_day;
		}
		
		double RS = upD[upD.length-1] / downD[downD.length-1];
		double RSI = 100 - (100 / (1 + RS));
		return RSI;
	}
	
	public double getWilderedRSI() throws Exception {
		
		double[] hArr = null;
		
		hArr = IndicatorFunction_bt.getHistoryArray(crypt, exchange, coin, base, interval, period_day+1);
		
		Queue<Double> upList = new LinkedList<Double>();
		Queue<Double> downList = new LinkedList<Double>();

		for(int i = 0; i < period_day; i++) {
			
			if(hArr[i] < hArr[i+1]) {
				upList.add(hArr[i+1] - hArr[i]);
				downList.add((double) 0);
			}
			else if(hArr[i] > hArr[i+1]){
				downList.add(hArr[i] - hArr[i+1]);
				upList.add((double) 0);
			}
		}
		
		double[] upD = new double[upList.size()];
		double[] downD = new double[downList.size()];
		
		for(int i = 1; i < upD.length; i++) {
			upD[i] = upList.remove();
		}
		for(int i = 1; i < downD.length; i++) {
			downD[i] = downList.remove();
		}
		
		double emaUP = IndicatorFunction_bt.getEMA(upD);
		double emaDown = IndicatorFunction_bt.getEMA(downD);
		
		double RS = emaUP / emaDown;
		double RSI = 100 - (100 / (1 + RS));
		
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
