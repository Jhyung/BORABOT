package backtest;
import exchangeAPI.*;

// n + m + t - 2

public class StochasticOsillator_bt implements calcIndicator_bt {

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
	private double[] phArr;
	private int initialStart;
	private int initialEnd;
	
	public StochasticOsillator_bt (int n, int m, int t, CryptowatchAPI c, String e, String coin, String base, int interval, double[][] hArr, int initialStart, int initialEnd) throws Exception{
		
		this.n = n;
		this.m = m;
		this.t = t;
		this.crypt = c;
		this.exchange = e;
		this.coin = coin;
		this.base = base;
		this.interval = interval;
		
		this.initialEnd = initialEnd;
		this.initialStart = ++initialStart; // 인덱스와 길이의 차이!!
		
		this.phArr = IndicatorFunction_bt.toPriceHistory(hArr);
		double[] tempArr = IndicatorFunction_bt.makeSublist(phArr, initialStart, initialEnd);
		//System.out.println(tempArr.length + "tempArrlen");
		double[] nowStochKD = getStochasticKD(tempArr);
		
		this.prevK = nowStochKD[0];
		this.prevD = nowStochKD[1];
		//System.out.println(tempArr[0] + " / / " + tempArr[1]);
		//System.out.println("prevK : " + prevK);
		//System.out.println("prevD : " + prevD);
	}
	
	@Override
	public int getDeterminConstant() throws Exception{
		//System.out.println("Stoch getdeter creat!");
		double[] tempArr = IndicatorFunction_bt.makeSublist(phArr, initialStart++, initialEnd++);
		
		int det;
		double[] nowStochKD = getStochasticKD(tempArr);
		
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
	
	public double[] getStochasticKD(double[] hArr1) throws Exception{
		
		//double[] hArr1 = IndicatorFunction_bt.getHistoryArray(crypt, exchange, coin, base, interval, n+m+t-2);
		//System.out.println("getSt");
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
		
		//System.out.println(SlowK + " / " + SlowD);
		
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
