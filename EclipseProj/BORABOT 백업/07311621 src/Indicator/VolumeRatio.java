package Indicator;

import exchangeAPI.CryptowatchAPI;

// 간격은 하루로 고정
public class VolumeRatio implements calcIndicator {

	private int period_day;
	private CryptowatchAPI crypt;
	private String exchange;
	private String coin;
	private String base;
	private int interval;
	private int sellIndex;
	private int buyIndex;
	
	
	//VR이 buyIndex 이하일 떄 -> 침체기 : 매수시점
	//VR이 sellIndex 이상일 떄 -> 과도기 : 매도시점
	public VolumeRatio(int period, int buyIndex, int sellIndex, CryptowatchAPI c, String e, String coin, String base, int interval) {
		this.period_day = period;
		this.crypt = c;
		this.exchange = e;
		this.coin = coin;
		this.base = base;
		this.interval = interval;
		this.buyIndex = buyIndex;
		this.sellIndex = sellIndex;
	}
	
	public int getDeterminConstant() throws Exception{
		
		int det;
		
		double VR = getVolumeRatio();
		
		if(VR < buyIndex) {
			det = 1;
		}
		else if (VR > sellIndex) {
			det = -1;
		}
		else {
			det = 0;
		}

		System.out.print("now VR : " + VR);
		System.out.println(" / VR deterConstant : " + det);
		return det;
	}
	
	
	public double getVolumeRatio() throws Exception{
		
		double[][] hlcvArr = IndicatorFunction.get_HLCV_HistoryArray(crypt, exchange, coin, base, interval, period_day+1);
		
		double sumOfIncreasing = 0;
		double sumOfDecreasing = 0;
		
		for(int i = 0; i < period_day; i++) {
			
			if(hlcvArr[i][2] < hlcvArr[i+1][2]) {
				sumOfIncreasing += hlcvArr[i+1][3];
			}
			else if (hlcvArr[i][2] > hlcvArr[i+1][2]) {
				sumOfDecreasing += hlcvArr[i+1][3];
			}
			else {
				sumOfIncreasing += hlcvArr[i+1][3] / 2;
				sumOfDecreasing += hlcvArr[i+1][3] / 2;
			}
		}
		
		double volumeRatio = sumOfIncreasing / sumOfDecreasing * 100;
		
		return volumeRatio;
	}
	
}
