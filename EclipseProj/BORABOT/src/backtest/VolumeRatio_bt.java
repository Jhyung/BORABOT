package backtest;

import exchangeAPI.CryptowatchAPI;

// period + 1
// 간격은 하루로 고정
public class VolumeRatio_bt implements calcIndicator_bt {

	private int period_day;
	private CryptowatchAPI crypt;
	private String exchange;
	private String coin;
	private String base;
	private int interval;
	private int sellIndex;
	private int buyIndex;
	private int initialStart;
	private int initialEnd;
	private double[][] HLCArr; // 전체임!!
	
	
	//VR이 buyIndex 이하일 떄 -> 침체기 : 매수시점
	//VR이 sellIndex 이상일 떄 -> 과도기 : 매도시점
	public VolumeRatio_bt(int period, int buyIndex, int sellIndex, CryptowatchAPI c, String e, String coin, String base, int interval, double[][] hArr,  int initialStart, int initialEnd) {
		this.period_day = period;
		this.crypt = c;
		this.exchange = e;
		this.coin = coin;
		this.base = base;
		this.interval = interval;
		this.buyIndex = buyIndex;
		this.sellIndex = sellIndex;
		this.initialEnd = initialEnd; // breakThrough가 아니기 떄문~
		this.initialStart = initialStart;
		this.initialEnd++;
		this.initialStart++;
		this.HLCArr = hArr;
	}
	
	public int getDeterminConstant() throws Exception{
		
		int det;
		double[][] tempArr = IndicatorFunction_bt.makeSublist2d(HLCArr, initialStart++, initialEnd++);
		double VR = getVolumeRatio(tempArr);
		
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
	
	
	public double getVolumeRatio(double[][] hlcvArr) throws Exception{
		
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
