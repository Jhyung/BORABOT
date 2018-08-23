package Indicator;
import exchangeAPI.*;

public class StochasticOsillator implements calcIndicator {

	private int interval;
	private int n;
	private int m;
	private int t;
	private CryptowatchAPI crypt;
	private String exchange;
	private String coin;
	private String base;
	private double prevK;
	private double prevD;
	
	public StochasticOsillator (int n, int m, int t, CryptowatchAPI c, String e, String coin, String base, int interval) throws Exception{
		this.n = n;
		this.m = m;
		this.t = t;
		this.crypt = c;
		this.exchange = e;
		this.coin = coin;
		this.base = base;
		this.interval = interval;
		
		double[] nowStochKD = getStochasticKD();
		
		this.prevK = nowStochKD[0];
		this.prevD = nowStochKD[1];
	}
	
	
	public int getDeterminConstant() throws Exception{
		
		int det;
		double[] nowStochKD = getStochasticKD();
		
		double nowK = nowStochKD[0];
		double nowD = nowStochKD[1];
		
		if(prevK < prevD) {
			if(nowK > nowK) {
				det = 1;
			}
			else {
				det = 0;
			}
		}
		else if(prevK > prevD) {
			if(nowK<nowD) {
				det = -1;
			}
			else {
				det = 0;
			}
		}
		else {
			det = 0;
		}
		
		//System.out.print("prevK : " + prevK + " prevD : " + prevD + " nowK : " + nowK + " nowD" + nowD);
		
		prevK = nowK;
		prevD = nowD;
		//System.out.println(" / StochOsc deterConstant : " + det);
		return det;
		
	}
	
	public double[] getStochasticKD() throws Exception{
		
		
		double[] hArr1 = IndicatorFunction.getHistoryArray(crypt, exchange, coin, base, interval, n+m+t-2);
		double[] hArr = new double[n+m+t-2];
		
		for(int n = 0; n < hArr1.length; n++) {
			hArr[n] = hArr1[hArr1.length-1-n];
		}
		
		for(int i = 0; i < hArr.length; i++) {
			//System.out.print(" " + hArr[i]);
		}
		
		double[] SlowKList = new double[t];
		
		for(int i = 0; i < t; i++) {
			
			double[] FastKList = new double[m];
			
			for(int j = i; j < m+i; j++) {
				
				double minPrice = getMinwithinIndex(hArr, j, j+n-1);
				double maxPrice = getMaxwithinIndex(hArr, j, j+n-1);
				double nowPrice = hArr[j];
				
				double FastK = (nowPrice - minPrice) / (maxPrice - minPrice) * 100;
				FastKList[j-i] = FastK;
			}
			
			SlowKList[i] = sum(FastKList)/m;
		}
		
		double SlowK = SlowKList[0];
		double SlowD = sum(SlowKList)/t;
		
		double[] ret = {SlowK, SlowD};
		return ret;
	}
	
	public double getMaxwithinIndex(double[] arr, int start, int end) {
		
		double max = arr[start];
		
		for(int i = start; i <= end; i++) {
			
			if(max < arr[i]) {
				max = arr[i];
			}
		}
		
		return max;
	}
	
	public double getMinwithinIndex(double[] arr, int start, int end) {
		
		double min = arr[start];
		
		for(int i = start; i <= end; i++) {
			
			if(min > arr[i]) {
				min = arr[i];
			}
		}
		
		return min;
		
	}

	public double sum(double[] list) {
		
		double ret=0;
		
		for(int i = 0; i < list.length; i++) {
			
			ret += list[i];
			
		}
		
		return ret;
	}
	
}
