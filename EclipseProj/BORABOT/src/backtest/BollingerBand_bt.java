package backtest;

import java.util.Iterator;
import java.util.Queue;

import Indicator.calcIndicator;
import exchangeAPI.CryptowatchAPI;

// period_day

public class BollingerBand_bt implements calcIndicator_bt, calcIndicator{
	
	private int period_day;
	private CryptowatchAPI crypt;
	private String exchange;
	private String coin;
	private String base;
	private int interval;
	private int mul;
	private int prevStatus; // 직전이 초과상태 1, 직전이 미만상태 2, 직전이 보통상태 0
	private int nowStatus;
	private double prevBotm;
	private double prevTop;
	
	private int initialStart;
	private int initialEnd;
	private double[] hpArr;
	
	public BollingerBand_bt(int period,int mul, CryptowatchAPI c, String e, String coin, String base, int interval,  double[][] HLCVarr, int initialStart, int initialEnd) throws Exception{
		this.period_day = period;
		this.crypt = c;
		this.exchange = e;
		this.coin = coin;
		this.base = base;
		this.interval = interval;
		this.mul = mul;
		this.prevStatus = 0;
		this.nowStatus = 0;
		this.initialEnd = initialEnd;
		this.initialStart = initialStart;
		
		hpArr = IndicatorFunction_bt.toPriceHistory(HLCVarr);
		
		double[] tempArr = IndicatorFunction_bt.makeSublist(hpArr, initialStart, initialEnd);
		
		
		double[] bolinger = getBollinger(tempArr);
		this.prevBotm = bolinger[0];
		this.prevTop = bolinger[1];
	}
	
	public int getDeterminConstant() throws Exception{

		double[] tempArr = IndicatorFunction_bt.makeSublist(hpArr, initialStart++, initialEnd++);
		// 0 - 대기 , 1 - 구매, -1 - 판매
		double[] bollinger = getBollinger(tempArr);
		
		double nowPrice = bollinger[2];
		double bottom = bollinger[0];
		double top = bollinger[1];
		int det;
		
		if(nowPrice > prevTop) {
			prevTop = top;
			prevBotm = bottom;
			nowStatus = 1;
		}
		else if(nowPrice < prevBotm) {
			prevTop = top;
			prevBotm = bottom;
			nowStatus = -1;
		}
		else {
			prevTop = top;
			prevBotm = bottom;
			nowStatus = 0;
		}
		
		if(prevStatus == 1 && nowStatus == 0) { 
			det =  -1;
		}
		else if (prevStatus == 2 && nowStatus == 0) {
			det =  1;
		}
		else {
			det = 0;
		}

		//System.out.println("BB deterConstnat : " + det);
		prevStatus = nowStatus;
		return det;
	}
	
	public double[] getBollinger(double[] hArr) throws Exception {
		//System.out.println("bb : " + initialStart +" / "+initialEnd);
		//double[] hArr;
		double nowPrice = hArr[hArr.length-1];
		double average = IndicatorFunction_bt.sumDouble(hArr) / hArr.length;
		
		double devsqr = 0;
		for(int i = 0; i < hArr.length; i++) {
			
			devsqr += (int)Math.pow(average-hArr[i], mul);
			
		}

		double deviation = devsqr / hArr.length;
		double stddev = Math.sqrt(deviation);
		
		//System.out.print("average : " + average);
		//System.out.print(" / 분산  : " + deviation);
		//System.out.print(" / 표준편차 : " + stddev);
		//System.out.print(" / 다음 상한 : " + (average + stddev));
		//System.out.print(" / 다음 하한 : " + (average - stddev));

		double ret[] = { average + stddev, average - stddev, nowPrice };
		return ret;
	}
	
}
