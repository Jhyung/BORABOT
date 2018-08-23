import java.sql.*;
//import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Queue;
import bittrexAPI.*;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
//import java.lang.Object;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import java.io.*;

class tradingBot {

	Gson gson = new Gson();

	private String botName;
	private String _ID;
	private Date start;
	private Date end;
	private String exchange;
	private String coin_exchange;
	private String coin_crypto;
	private String Algoset;
	private String API_KEY;
	private String Sec_KEY;
	private double priceAmount; // ó�� ���� �ϴ� ��
	private double coinmany = 0;
	private double testStartAsset;

	// Algoset���� �̾Ƴ�����
	private int _period_day = 20;
	private int _mul = 2;
	private int _interval = 60;
	private int _corrInterval = 60; // �����ν��ϴµ� 1�к��� ���, �� 1�и��� ��ġ
	private int _totalLength = 1440; // �Ϸ�ġ �������� ������ ��
	private int _intervalNumber = 15; // 15���� ������ ���ٴ� ��

	// test �ʱⰪ
	int testNum = 0; // ��Ʈ���� 0��

	public tradingBot(double priceAmount, String _ID, Date start, Date end, String exchange, String coin_crypto,
			String coin_exchange, String Algoset, String API_KEY, String Sec_KEY, String botName) {
		this.priceAmount = priceAmount;
		this.testStartAsset = priceAmount;
		this._ID = _ID;
		this.start = start;
		this.end = end;
		this.exchange = exchange;
		this.coin_crypto = coin_crypto;
		this.coin_exchange = coin_exchange;
		this.Algoset = Algoset;
		this.API_KEY = API_KEY;
		this.Sec_KEY = Sec_KEY;
		this.botName = botName;
		this.priceAmount = priceAmount;

		try {

			String selectSql = "SELECT * FROM trans_log;";
			ResultSet rs = DB.Query(selectSql, "select");

			while (rs.next()) // next()�� ���� ������ ������
			{
				System.out.println(rs.getString(1) + "\t" + // ���� ����
						rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4) + "\t" + rs.getString(5));
			}

		} catch (SQLException se) {
			se.printStackTrace();
		} 
	}

	public double corrPatternAnalysis(Cryptowatch crypt, int interval, int totalLength, int intervalNumber) {
		double[] currentData = getHistoryArray(crypt, interval, intervalNumber, intervalNumber);
		double[] dayData = getHistoryArray(crypt, interval, totalLength, totalLength);

		System.out.print("������� 15�� ������ ������ : ".toString());
		for (int k = 0; k < intervalNumber; k++) {
			System.out.print(currentData[k] + " ");
		}

		double currentSum = 0;
		for (double i : currentData) {
			currentSum += i;
		}
		double currentMean = currentSum / intervalNumber;
		for (int i = 0; i < intervalNumber; i++) {
			currentData[i] -= currentMean;
		}

		int idx = 0;
		double max = 0;
		for (int i = 0; i < totalLength - 40; i++) {

			double sum = 0;
			double[] temp = new double[intervalNumber];

			for (int j = 0; j < intervalNumber; j++) {
				temp[j] = dayData[i + j];
				sum += dayData[i + j];
			}

			for (int j = 0; j < intervalNumber; j++) {
				temp[j] -= sum / intervalNumber;
			}

			DoubleMatrix1D a = new DenseDoubleMatrix1D(currentData);
			DoubleMatrix1D b = new DenseDoubleMatrix1D(temp);
			double cosineDistance = a.zDotProduct(b) / Math.sqrt(a.zDotProduct(a) * b.zDotProduct(b));
			if (max < cosineDistance) {
				max = cosineDistance;
				idx = i;
			}
		}


		System.out.print("\n���� ��ġ�ϴ� ������ : ");
		for (int k = idx; k < idx + intervalNumber; k++) {
			System.out.print(dayData[k] + " ");
		}


		// �߼����� idx+intervalNumber ~ 15��
		idx += 15;
		double[] x = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		double[] y = new double[intervalNumber];
		for (int j = 0; j < intervalNumber; j++) {
			y[j] = dayData[idx++];
		}
		double corr = new PearsonsCorrelation().correlation(y, x);


		System.out.print("\n���� ������ : ");
		for (int k = 0; k < intervalNumber; k++) {
			System.out.print(y[k] + " ");
		}
		System.out.println();

		return corr;
	}

	public void patterNakedTrade() {

		System.out.println("pattrn����!");
		Bittrex brx = new Bittrex(API_KEY, Sec_KEY, 30, 1); // �̺κ��� ���� ���� -> �������� �ŷ��ҵ� �����ϰ� �߻�ȭ �ʿ�
		Cryptowatch crypt = new Cryptowatch(10, 1); // 1ȸ�� �� -> �̴�� ����ص� ������, �ŷ��� ���� �ʱ� ������ ����


		while (tradingBot.isRun(_ID, botName)) {
			double[] currentData = getHistoryArray(crypt, _corrInterval, _intervalNumber, _intervalNumber);
			double corr = corrPatternAnalysis(crypt, _corrInterval, _totalLength, _intervalNumber);
			System.out.println();
			if (corr > 0.5) {
				buyCoin(brx, 10, currentData[_intervalNumber - 1]); // buy
			} else if (corr < -0.5) {
				sellCoin(brx, 10, currentData[_intervalNumber - 1]); // sell
			} else {
				doNothing();
			}        

			try {
				Thread.sleep(6000*10+50);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			System.out.println();
					
		}
	}

	public void trendFollowing(int less_ave, int more_ave) {

		Bittrex brx = new Bittrex(API_KEY, Sec_KEY, 30, 1); // �̺κ��� ���� ���� -> �������� �ŷ��ҵ� �����ϰ� �߻�ȭ �ʿ�
		Cryptowatch crypt = new Cryptowatch(10, 1); // 1ȸ�� �� -> �̴�� ����ص� ������, �ŷ��� ���� �ʱ� ������ ����

		Queue<Double> more_que = new LinkedList<Double>();
		Queue<Double> less_que = new LinkedList<Double>();

		more_que = getHistoryQueue(crypt, 14400, more_ave);
		less_que = getHistoryQueue(crypt, 14400, less_ave);

		Iterator<Double> iter = null;
		iter = more_que.iterator();
		double more_sum = 0;
		while (iter.hasNext()) {
			more_sum += iter.next();
		}
		double more_average = more_sum / more_ave;

		iter = less_que.iterator();
		double less_sum = 0;
		while (iter.hasNext()) {
			more_sum += iter.next();
		}
		double less_average = more_sum / more_ave;

		double currentLast = getCurrentPrice(crypt, coin_crypto);
		System.out.println(" -->  ���簡 : " + currentLast);

		if (less_average > more_average) {
			buyCoin(brx, 10, currentLast); // buy
		} else {
			sellCoin(brx, 10, currentLast); // sell
		}

	}

	public void bollingerPatternNaked() {
		Bittrex brx = new Bittrex(API_KEY, Sec_KEY, 30, 1); // �̺κ��� ���� ���� -> �������� �ŷ��ҵ� �����ϰ� �߻�ȭ �ʿ�
		Cryptowatch crypt = new Cryptowatch(10, 1); // 1ȸ�� �� -> �̴�� ����ص� ������, �ŷ��� ���� �ʱ� ������ ����

		// ���͹� ���, ���͹� ���� ���ؼ� ť�� �־��� (������)
		Queue<Double> history_queue = getHistoryQueue(crypt, _interval, _period_day * _interval);

		// while
		// getBollinder -> queue���ְ� ���� �������� ����
		// wait 5.0001��
		// ���� �ް� �� -> ����
		// B S W ���� -> ����		
		// queue ���μ���



		while (tradingBot.isRun(_ID, botName)) {
			double[] bollingerHL = getBollinger(crypt, history_queue, _mul);
			// ------------------
			try {
				Thread.sleep(1000 * _interval);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			// -------------------
			double currentLast = getCurrentPrice(crypt, coin_crypto);
			System.out.println(" --> ���簡 : " + currentLast);
			// -----------------
			// �ż�Ÿ�̹�
			if (currentLast < bollingerHL[1]) {
				// BUY
				// �󸶳� ����� �˰��� ���ÿ� ����
				System.out.println("������ �ż� Ÿ�̹�");
				double corr = corrPatternAnalysis(crypt, _corrInterval, _totalLength, _intervalNumber);
				if (corr > 0.4) {
					System.out.print("Perason Correlation Coefficieint : " + corr+ " ");
					buyCoin(brx, 50, currentLast);
				} else if (corr < -0.5) {
					System.out.print("Perason Correlation Coefficieint : " + corr+ " ");
					sellCoin(brx, 40, currentLast);

				} else {
					doNothing();
				}
			}
			// �ŵ�Ÿ�̹�
			else if (currentLast > bollingerHL[0]) {
				// SELL
				// �󸶳� �Ȱ��� �˰��� ���ÿ� ����
				System.out.println("������ �ŵ� Ÿ�̹�");
				double corr = corrPatternAnalysis(crypt, _corrInterval, _totalLength, _intervalNumber);
				if (corr > 0.3) {
					System.out.print("Perason Correlation Coefficieint : " + corr+ " ");
					buyCoin(brx, 20, currentLast);
				} else if (corr < -0.5) {
					System.out.print("Perason Correlation Coefficieint : " + corr+ " ");
					sellCoin(brx, 80, currentLast);
				} else {
					doNothing();
				}
			}
			// ���Ÿ�̹�
			else {
				// wait
				System.out.println("������ ��� Ÿ�̹�");
				double corr = corrPatternAnalysis(crypt, _corrInterval, _totalLength, _intervalNumber);
				if (corr > 0.75) {
					//4������
					System.out.print("Perason Correlation Coefficieint : " + corr+ " ");
					buyCoin(brx, 30, currentLast);
				} else {
					System.out.print("Perason Correlation Coefficieint : " + corr+ " ");
					doNothing();
				}
			}
			// -----------------
			history_queue.remove();
			history_queue.add(currentLast);
			System.out.println("������ ȯ���� �� ���� ��� : " + (int)(currentLast * testNum + testStartAsset) + "KRW");
			System.out.println();
		}
	}

	public void Bollingertrade() {

		Bittrex brx = new Bittrex(API_KEY, Sec_KEY, 30, 1); // �̺κ��� ���� ���� -> �������� �ŷ��ҵ� �����ϰ� �߻�ȭ �ʿ�
		Cryptowatch crypt = new Cryptowatch(10, 1); // 1ȸ�� �� -> �̴�� ����ص� ������, �ŷ��� ���� �ʱ� ������ ����

		// ���͹� ���, ���͹� ���� ���ؼ� ť�� �־���
		Queue<Double> history_queue = getHistoryQueue(crypt, _interval, _period_day * _interval);

		// while
		// getBollinder -> queue���ְ� ���� �������� ����
		// wait 5.0001��
		// ���� �ް� �� -> ����
		// B S W ���� -> ����
		// queue ���μ���
		while (tradingBot.isRun(_ID, botName)) {

			double[] bollingerHL = getBollinger(crypt, history_queue, _mul);
			// ------------------
			try {
				Thread.sleep(1000 * _interval);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			// -------------------
			double currentLast = getCurrentPrice(crypt, coin_crypto);
			System.out.println("���簡 : " + currentLast);
			// -----------------
			// �ż�Ÿ�̹�
			if (currentLast < bollingerHL[1]) {
				// BUY
				// �󸶳� ����� �˰��� ���ÿ� ����

				buyCoin(brx, 10, currentLast);
			} else if (currentLast > bollingerHL[0]) {
				// SELL
				// �󸶳� �Ȱ��� �˰��� ���ÿ� ����
				sellCoin(brx, 10, currentLast);
			} else {
				// wait
				doNothing();
			}
			// -----------------
			history_queue.remove();
			history_queue.add(currentLast);

			System.out.println();
		}
	}

	// ���� �ݺ� ����
	static public boolean isRun(String id, String botname) {
		String selectSql = String.format(
				"select on_going from trade where user_id = \'%s\' and bot_name = \'%s\'", id, botname);

		ResultSet rs = DB.Query(selectSql, "select");
		boolean b = true;
		try {
			System.out.println("try ����\n" + selectSql);
			while(rs.next()) {  
				b = rs.getBoolean("on_going");
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}
		return b;		
	}
	
	// IOC �����ϱ�
	public boolean IOC_commit(Bittrex brx) {

		return true;
	}

	public void buyCoin(Bittrex brx, double amount, double price) {

		System.out.println("--> BUY");
		// brx.buyMarket(coin, Double.toString(amount));
		IOC_commit(brx);
		if (testStartAsset > price * amount) {
			testNum += amount;
			testStartAsset -= price * amount;
			System.out.print("������ ������ �� : "+amount+", ������ ������ ���� ���� : " + price + " --> ");
		}
		else if (testStartAsset < price * 1.5) {
			System.out.println("have no money to buy(or less than price of 1 coin)");
		}
		else {
			int how = (int)((int)testStartAsset / price);
			testNum += how;
			testStartAsset -= price * how;
			System.out.print("������ ������ �� : "+how+", ������ ������ ���� ���� : " + price + " --> ");
		}

		Cryptowatch crypt = new Cryptowatch(10, 1);
		double currentLast = getCurrentPrice(crypt, coin_crypto);

		// ��� ���� �����ֱ�
		// transaction log�� ����    	
		System.out.println("���� ���� ���� �� : " + testNum + " / ���� �� : " + testStartAsset + "KRW");
//		ExecSQL_Insert(_ID, exchange, coin_crypto, 1, amount, price);
	}

	public void sellCoin(Bittrex brx, double amount, double price) {

		System.out.println("--> SELL");
		// brx.sellMarket(coin, Double.toString(amount));
		if (testNum > amount) {
			testNum -= amount;
			testStartAsset += amount * price;
			System.out.print("�Ǹ��� ������ �� : "+amount+", �Ǹ��� ������ ���� ���� : " + price + " --> ");
		} 
		else if (testNum == 0){
			System.out.println("have no coin to sell");
		}
		else{
			int temp = testNum;
			testStartAsset += testNum * price;
			testNum -= testNum;
			System.out.print("�Ǹ��� ������ �� : "+temp+", �Ǹ��� ������ ���� ���� : " + price + " --> ");
		}

		Cryptowatch crypt = new Cryptowatch(10, 1);
		double currentLast = getCurrentPrice(crypt, coin_crypto);
		// ��� ���� �����ֱ�
		// transaction log�� ������
		System.out.println("���� ���� ���� �� : " + testNum + " / ���� �� : " + testStartAsset + "KRW");
//		ExecSQL_Insert(_ID, exchange, coin_crypto, 2, amount, price);

	}

	public void doNothing() {
		System.out.println("--> WAIT");

		System.out.println("���� ���� ���� �� : " + testNum + " / ���� �� : " + testStartAsset + "KRW");
		// ��� ���� �����ֱ�
//		ExecSQL_Insert(_ID, exchange, coin_crypto, 3, 0, 0);

		Cryptowatch crypt = new Cryptowatch(10, 1);
		double currentLast = getCurrentPrice(crypt, coin_crypto);

//		for(int i = TradeMain.nowTrading.size() - 1; i >= 0; i--) {
//
//			if((TradeMain.nowTrading.get(i).getId()+TradeMain.nowTrading.get(i).getName()).equals(botName)) {
//				TradeMain.nowTrading.get(i).setProfit(String.format("%.2f", ((currentLast * (double)testNum + testStartAsset)/priceAmount*100.0)));
//				double asd = ((currentLast * (double)testNum + testStartAsset)/priceAmount*100.0);
//				System.out.println();
//				System.out.println(String.format("%.2f", asd));
//				System.out.println();
//			}
//		} 
	}

	// ���� �����丮�� ������ -> ohlc �� �� ����Ұ��� �߰� / interval�� ���� �Ķ����
	public Queue getHistoryQueue(Cryptowatch crypt, int interval, int period_day) {

		Date date = new Date();
		long U_current = date.getTime() / 1000;
		// System.out.println(U_current);
		// System.out.println(date);

		String ohlc_string = crypt.getOHLC(exchange, coin_crypto, U_current - (_period_day * _interval), _interval);
		JsonObject ohlc_json = new JsonParser().parse(ohlc_string).getAsJsonObject();
		String ohlc_result_string = gson.toJson(ohlc_json.get("result"));
		JsonObject ohlc_result_json = new JsonParser().parse(ohlc_result_string).getAsJsonObject();
		JsonArray ohlc_jsarr = ohlc_result_json.get(Integer.toString(_interval)).getAsJsonArray();

		Queue<Double> history_queue = new LinkedList<Double>();

		JsonArray jsarr;
		System.out.println(ohlc_jsarr.size());
		for (int i = 0; i < ohlc_jsarr.size(); i++) {

			jsarr = ohlc_jsarr.get(i).getAsJsonArray();
			history_queue.add(jsarr.get(4).getAsDouble()); // ����(C)�� ���
		}

		return history_queue;
	}

	public double[] getHistoryArray(Cryptowatch crypt, int interval, int period_day, int size) {

		Date date = new Date();
		long U_current = date.getTime() / 1000;

		String ohlc_string = crypt.getOHLC(exchange, coin_crypto, U_current - (period_day * interval), interval);
		JsonObject ohlc_json = new JsonParser().parse(ohlc_string).getAsJsonObject();
		String ohlc_result_string = gson.toJson(ohlc_json.get("result"));
		JsonObject ohlc_result_json = new JsonParser().parse(ohlc_result_string).getAsJsonObject();
		JsonArray ohlc_jsarr = ohlc_result_json.get(Integer.toString(_interval)).getAsJsonArray();
		// System.out.println("���̽� ũ�� " + ohlc_jsarr.size());
		double[] historyArray = new double[ohlc_jsarr.size()];

		JsonArray jsarr;

		int bojung = ohlc_jsarr.size() - size;
		for (int i = 0; i < size; i++) {
			jsarr = ohlc_jsarr.get(i + bojung).getAsJsonArray();
			historyArray[i] = jsarr.get(4).getAsDouble();
		}

		// System.out.println("get historyarray done");
		return historyArray;
	}

	// 1ȸ�� �������� �� , �̹� �����丮ť�� interval�� �����Ǿ�����. ���� ǥ������(mul)�� �ʿ�
	public double[] getBollinger(Cryptowatch crypt, Queue history_queue, int _mul) {

		Iterator<Double> iter = null;

		iter = history_queue.iterator();
		double sum = 0;
		while (iter.hasNext()) {
			sum += iter.next();
		}
		double average = sum / history_queue.size();

		iter = history_queue.iterator();
		double devsqr = 0;

		while (iter.hasNext()) {
			devsqr += (int) Math.pow(average - iter.next(), _mul);
		}

		double deviation = devsqr / (history_queue.size());
		double stddev = Math.sqrt(deviation);

		System.out.print("average : " + average);
		//System.out.print(" / �л�  : " + deviation);
		//System.out.print(" / ǥ������ : " + stddev);
		System.out.print(" / ���� ���� : " + (average + stddev));
		System.out.print(" / ���� ���� : " + (average - stddev));

		double ret[] = { average + stddev, average - stddev };
		return ret;
	}

	// �ŷ��ϴ� �ŷ��Ҹ� ���Ͽ� ���簪�� ����. ���簪 �� ������ ��� (Bid, Ask, Last)
	public double getCurrentPrice(Cryptowatch crypt, String coin_crypto) {

		String api_string = crypt.getCurrentPrice(exchange, coin_crypto); // api�� -> string�� �������
		JsonObject json_result = new JsonParser().parse(api_string).getAsJsonObject(); // string�� json���� ����
		String result_string = gson.toJson(json_result.get("result")); // json �߿��� result�� string���� �Ľ�
		JsonObject result_json = new JsonParser().parse(result_string).getAsJsonObject(); // result�� �ٽ� json���� �Ľ�
		double currentLast = Double.parseDouble(result_json.get("price").toString());

		return currentLast;
	}
}
