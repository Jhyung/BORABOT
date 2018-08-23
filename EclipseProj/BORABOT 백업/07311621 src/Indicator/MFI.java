/*
 * negMF 가 0인 경우 예외처리 요망
 * 
 */

package Indicator;
import exchangeAPI.CryptowatchAPI;

public class MFI implements calcIndicator {
	
	private int period_day;
	private int interval;
	private String coin;
	private String exchange;
	private CryptowatchAPI crypt;
	private String base;
	private int buyIndex;
	private int sellIndex;
	private double prevMFI;
	
	public MFI (int day, int buyIndex, int sellIndex, CryptowatchAPI c, String e, String coin, String base, int interval) throws Exception{
		this.period_day = day;
		this.coin = coin;
		this.exchange = e;
		this.crypt = c;
		this.base = base;
		this.interval = interval;
		this.buyIndex = buyIndex;
		this.sellIndex = sellIndex;
		
		this.prevMFI = getMFI();
	}
	
	public int getDeterminConstant() throws Exception {
		try {
		double nowMFI = getMFI();
		int det;
		if(prevMFI < buyIndex) {
			
			if(nowMFI > buyIndex) {
				det = 1;
			}
			else {
				det = 0;
			}
		}
		else if(prevMFI > sellIndex) {
			
			if(nowMFI < sellIndex) {
				det = -1;
			}
			else {
				det =  0;
			}
		}
		else {
			det =  0;
		}
		
		System.out.print("prevMFI : " + prevMFI + " nowMFI : " + nowMFI);
		
		prevMFI = nowMFI;
		
		System.out.println(" / MFI deterConstant : " + det);
		return det;
		} catch(Exception e) {
			throw new Exception();
		}
	}
	
	public double getMFI() throws Exception{
		
		try {
		double[][] HLCVArr = IndicatorFunction.get_HLCV_HistoryArray(crypt, exchange, coin, base, interval, period_day+1);
		double posMF=0;
		double negMF=0;
		for(int i = 0; i < period_day; i++) {
			
			double prevTypPrice = (HLCVArr[i][0] + HLCVArr[i][1] + HLCVArr[i][2]) / 3;
			double nowTypPrice = (HLCVArr[i+1][0] + HLCVArr[i+1][1] + HLCVArr[i+1][2]) / 3;
			
			if( prevTypPrice < nowTypPrice ) {
				//positive
				posMF +=  (nowTypPrice) * HLCVArr[i+1][3];
			}
			else if(prevTypPrice > nowTypPrice){
				//negative
				negMF +=  (nowTypPrice) * HLCVArr[i+1][3];
			}
		}
		

		//double MFI = 100.0 - 100/(1+MFR);
		double MFI = posMF / (posMF + negMF) * 100.0;
		return MFI;
		}
	
		catch(Exception e) {
			throw new Exception();
		}
	}
}
