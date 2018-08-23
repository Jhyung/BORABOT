import java.sql.*;
import java.time.LocalDate;
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

class tradingBot{
	
	Gson gson = new Gson();
	
	String botName;
	String _ID;
	String start;
	String end;
	String exchange;
	String coin_exchange;
	String coin_crypto;
	private String Algoset;
	private String API_KEY;
	private String Sec_KEY;
	private double priceAmount; // ó�� ���� �ϴ� ��
	
	// Algoset���� �̾Ƴ�����
	int _period_day = 20;
	int _mul = 2;
	int _interval = 60;
	
	//test
	int testNum = 0;
	int testStartPrice = 1000000000;
	
	//DB
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null; //ResultSet ��ü ����
	private String url = "jdbc:mysql://127.0.0.1/practice_db?user=root&password=01028798551&serverTimezone=UTC";
	
	public tradingBot(double priceAmount, String _ID, String start, String end, String exchange, String coin_crypto, String coin_exchange, String Algoset,
			String API_KEY, String Sec_KEY, String botName) {
		this.priceAmount = priceAmount;
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

            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("����̹� �ε� ����!");
            conn = DriverManager.getConnection(url);
            System.out.println("�����ͺ��̽� ���� ����!");
            stmt = conn.createStatement();
            String selectSql = "SELECT * FROM transaction_log;";

            rs = stmt.executeQuery(selectSql); //rs�� executeQuery�� �������� ����

            while(rs.next()) //next()�� ���� ������ ������
            {
                System.out.println
                (
                    rs.getString(1) + "\t" + //���� ����
                    rs.getString(2) + "\t" +
                    rs.getString(3) + "\t" +
                    rs.getString(4) + "\t" +
                    rs.getString(5)
                );
            }
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            //if(conn!=null) try {conn.close();} catch (SQLException e) {}
        }
	}
	
	public void Bollingertrade() {
		
		Bittrex brx = new Bittrex(API_KEY, Sec_KEY, 30,1); // �̺κ��� ���� ���� -> �������� �ŷ��ҵ� �����ϰ� �߻�ȭ �ʿ�
		Cryptowatch crypt = new Cryptowatch(10,1); // 1ȸ�� �� -> �̴�� ����ص� ������, �ŷ��� ���� �ʱ� ������ ����
        
		// ���͹� ���, ���͹� ����  ���ؼ� ť�� �־���
        Queue<Double> history_queue = getHistoryQueue(crypt, _interval, _period_day*_interval);
        
        // while
        // getBollinder -> queue���ְ� ���� �������� ����
        // wait 5.0001��
        // ���� �ް� �� -> ����
        // B S W ���� -> ����
        // queue ���μ���
        while(true) {
        	
        	double[] bollingerHL = getBollinger(crypt, history_queue,_mul);
        	//------------------
        	try {
        		Thread.sleep(1000*_interval);
        	}
        	catch(Exception e) {
        		System.out.println(e.toString());
        	}
        	//-------------------
            double currentLast = getCurrentPrice(crypt, coin_crypto);
            System.out.println("���簡 : " + currentLast);
            //-----------------
            // �ż�Ÿ�̹� 
            if(currentLast < bollingerHL[1]) {
            	//BUY
            	// �󸶳� ����� �˰��� ���ÿ� ����
            	buyCoin(brx, 10, currentLast);
            }
            else if(currentLast > bollingerHL[0]) {
            	//SELL
            	// �󸶳� �Ȱ��� �˰��� ���ÿ� ����
            	sellCoin(brx, 10, currentLast);
            }
            else {
            	//wait
            	doNothing();
            }
            //-----------------
            history_queue.remove();
            history_queue.add(currentLast);
            System.out.println();
        }
	}
	
	// IOC �����ϱ�
	public boolean IOC_commit(Bittrex brx) {
		
		return true;
	}
	
	public void buyCoin(Bittrex brx, double amount, double price) {
		
		System.out.println("BUY");
		//brx.buyMarket(coin, Double.toString(amount));
		IOC_commit(brx);
		
		//��� ���� �����ֱ�
		//transaction log�� ������
		ExecSQL_Insert(_ID, exchange, coin_crypto, 1, amount, price);
	}
	
	public void sellCoin(Bittrex brx, double amount, double price) {
		
		System.out.println("SELL");
		//brx.sellMarket(coin, Double.toString(amount));
		
		//��� ���� �����ֱ�
		//transaction log�� ������
		ExecSQL_Insert(_ID, exchange, coin_crypto, 2, amount, price);
	}
	
	public void doNothing() {
		System.out.println("WAIT");
		
		//��� ���� �����ֱ�
		ExecSQL_Insert(_ID, exchange, coin_crypto, 3, 0, 0);
	}
	
	// ���� �����丮�� ������ -> ohlc �� �� ����Ұ��� �߰� / interval�� ���� �Ķ����
	public Queue getHistoryQueue(Cryptowatch crypt, int interval, int period_day) {
		
		Date date = new Date();
		long U_current = date.getTime()/1000;
		System.out.println(U_current);
		System.out.println(date);
		
		String ohlc_string = crypt.getOHLC(exchange, coin_crypto, U_current - (_period_day * _interval), _interval);
		JsonObject ohlc_json = new JsonParser().parse(ohlc_string).getAsJsonObject();
        String ohlc_result_string = gson.toJson(ohlc_json.get("result"));
        JsonObject ohlc_result_json = new JsonParser().parse(ohlc_result_string).getAsJsonObject();
        JsonArray ohlc_jsarr = ohlc_result_json.get(Integer.toString(_interval)).getAsJsonArray();
        
        Queue<Double> history_queue = new LinkedList<Double>();
   
        JsonArray jsarr;
        System.out.println(ohlc_jsarr.size());
        for(int i = 0; i < ohlc_jsarr.size(); i++) {
        	
        	jsarr = ohlc_jsarr.get(i).getAsJsonArray();
        	history_queue.add(jsarr.get(4).getAsDouble()); // ����(C)�� ���
        }
        
        return history_queue;
	}
	
	// 1ȸ�� �������� �� , �̹� �����丮ť�� interval�� �����Ǿ�����. ���� ǥ������(mul)�� �ʿ�
	public double[] getBollinger(Cryptowatch crypt, Queue history_queue, int _mul) {
		
		Iterator<Double> iter = null;
        
        iter = history_queue.iterator();
        double sum = 0;
        while(iter.hasNext()) {
        	sum += iter.next();
        }
        double average = sum/history_queue.size();
        
        iter = history_queue.iterator();
        double devsqr = 0;
        
        while(iter.hasNext()) {
        	devsqr += (int)Math.pow(average-iter.next() , _mul);
        }
        
        double deviation = devsqr/(history_queue.size());
        double stddev = Math.sqrt(deviation);
        
        System.out.println("average : " + average);
        System.out.println("�л�  : " + deviation);
        System.out.println("ǥ������ : "+ stddev);
        System.out.println("���� ���� : " + (average + stddev));
        System.out.println("���� ���� : " + (average - stddev));
		
		double ret[] = {average + stddev, average - stddev};
		return ret;
	}
	
	// �ŷ��ϴ� �ŷ��Ҹ� ���Ͽ� ���簪�� ����. ���簪 �� ������ ��� (Bid, Ask, Last)
	public double getCurrentPrice_no(Bittrex brx, String coin_exchange) {
		String api_string = brx.getTicker(coin_exchange); // api�� -> string�� �������
        JsonObject json_result = new JsonParser().parse(api_string).getAsJsonObject(); // string�� json���� ����
        String result_string = gson.toJson(json_result.get("result")); // json �߿��� result�� string���� �Ľ�
        JsonObject result_json = new JsonParser().parse(result_string).getAsJsonObject(); // result�� �ٽ� json���� �Ľ�
        double currentLast = Double.parseDouble(result_json.get("Last").toString());
        
        return currentLast;
	}
	
	public double getCurrentPrice(Cryptowatch crypt, String coin_crypto) {
		
		String api_string = crypt.getCurrentPrice(exchange, coin_crypto); // api�� -> string�� �������
        JsonObject json_result = new JsonParser().parse(api_string).getAsJsonObject(); // string�� json���� ����
        String result_string = gson.toJson(json_result.get("result")); // json �߿��� result�� string���� �Ľ�
        JsonObject result_json = new JsonParser().parse(result_string).getAsJsonObject(); // result�� �ٽ� json���� �Ľ�
        double currentLast = Double.parseDouble(result_json.get("price").toString());
		
        return currentLast;
	}

	public void ExecSQL_Insert(String _ID, String exchange_name, String coin_name, int sale, double amount, double currentPrice) {
		
		//sale -> 1: BUY, 2: SELL, 3: WAIT
		String currentTime = LocalTime.now().toString();
		String val = "(\""+ botName + "\","+"\"" + _ID + "\",\"" + currentTime + "\", " + sale + "," +  "\"" + exchange_name  + "\",\"" + coin_name +"\");";
		String selectSql = "INSERT INTO transaction_log VALUES" + val;

		try {
		stmt = conn.createStatement();
        stmt.executeUpdate(selectSql);
        System.out.println("sql�� ���� ���� : " + selectSql);
		}

		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	
}
