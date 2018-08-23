package Indicator;

import java.util.Iterator;
import java.util.Queue;

import exchangeAPI.CryptowatchAPI;

public class BollingerBand implements calcIndicator{
	
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
	
	public BollingerBand(int period,int mul, CryptowatchAPI c, String e, String coin, String base, int interval) throws Exception{
		this.period_day = period;
		this.crypt = c;
		this.exchange = e;
		this.coin = coin;
		this.base = base;
		this.interval = interval;
		this.mul = mul;
		this.prevStatus = 0;
		this.nowStatus = 0;
		double[] bolinger = getBollinger();
		
		bolinger = getBollinger();
		
		this.prevBotm = bolinger[0];
		this.prevTop = bolinger[1];
	}
	
	public int getDeterminConstant() throws Exception{
		
		// 0 - 대기 , 1 - 구매, -1 - 판매
		double[] bollinger = getBollinger();
		
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
	
	public double[] getBollinger() throws Exception {

		Queue<Double> history_queue;
		
		history_queue = IndicatorFunction.getHistoryQueue(exchange,coin, base, interval, period_day);
		Iterator<Double> iter = null;
		double nowPrice=-1;
		iter = history_queue.iterator();
		double sum = 0;
		
		while (iter.hasNext()) {
			double temp = iter.next();
			sum += temp;
			if(!iter.hasNext()) {
				nowPrice = temp;
			}
		}
		
		double average = sum / history_queue.size();

		iter = history_queue.iterator();
		double devsqr = 0;

		while (iter.hasNext()) {
			devsqr += (int) Math.pow(average - iter.next(), mul);
		}

		double deviation = devsqr / (history_queue.size());
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
