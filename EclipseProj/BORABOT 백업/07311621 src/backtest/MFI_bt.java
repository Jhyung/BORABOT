/*
 * negMF 가 0인 경우 예외처리 요망
 * 
 */

package backtest;
import exchangeAPI.CryptowatchAPI;

// period + 1

public class MFI_bt implements calcIndicator_bt {
	
	private int period_day;
	private int interval;
	private String coin;
	private String exchange;
	private CryptowatchAPI crypt;
	private String base;
	private int buyIndex;
	private int sellIndex;
	private double prevMFI;
	private int initialStart;
	private int initialEnd;
	private double[][] HLCArr; // 전체임!!
	
	public MFI_bt (int day, int buyIndex, int sellIndex, CryptowatchAPI c, String e, String coin, String base, int interval, double[][] hArr,  int initialStart, int initialEnd) throws Exception{
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
		double[][] tempArr = IndicatorFunction_bt.makeSublist2d(hArr, initialStart++, initialEnd++);
		this.prevMFI = getMFI(tempArr);
	}
	
	public int getDeterminConstant() throws Exception {
		try {
			double[][] tempArr = IndicatorFunction_bt.makeSublist2d(HLCArr, initialStart++, initialEnd++);
			double nowMFI = getMFI(tempArr);
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
			
			//System.out.print("prevMFI : " + prevMFI + " nowMFI : " + nowMFI);
			
			prevMFI = nowMFI;
			
			//System.out.println(" / MFI deterConstant : " + det);
			return det;
		} catch(Exception e) {
			throw new Exception();
		}
	}
	
	public double getMFI(double[][] HLCVArr) throws Exception{
		
		try {
		//double[][] HLCVArr = IndicatorFunction_bt.get_HLCV_HistoryArray(crypt, exchange, coin, base, interval, period_day+1);
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
