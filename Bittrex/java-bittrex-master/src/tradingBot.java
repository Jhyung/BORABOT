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
	private double priceAmount; // 처음 시작 하는 돈
	
	// Algoset에서 뽑아내야함
	int _period_day = 20;
	int _mul = 2;
	int _interval = 60;
	
	//test
	int testNum = 0;
	int testStartPrice = 1000000000;
	
	//DB
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null; //ResultSet 객체 선언
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
            System.out.println("드라이버 로드 성공!");
            conn = DriverManager.getConnection(url);
            System.out.println("데이터베이스 접속 성공!");
            stmt = conn.createStatement();
            String selectSql = "SELECT * FROM transaction_log;";

            rs = stmt.executeQuery(selectSql); //rs에 executeQuery의 실행결과를 삽입

            while(rs.next()) //next()에 대한 설명은 본문에
            {
                System.out.println
                (
                    rs.getString(1) + "\t" + //본문 설명
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
		
		Bittrex brx = new Bittrex(API_KEY, Sec_KEY, 30,1); // 이부분은 차차 개선 -> 여러가지 거래소도 동일하게 추상화 필요
		Cryptowatch crypt = new Cryptowatch(10,1); // 1회성 콜 -> 이대로 사용해도 괜찮음, 거래를 하지 않기 때문에 괜춘
        
		// 인터벌 몇분, 인터벌 갯수  정해서 큐에 넣어줌
        Queue<Double> history_queue = getHistoryQueue(crypt, _interval, _period_day*_interval);
        
        // while
        // getBollinder -> queue를주고 다음 상하한을 받음
        // wait 5.0001초
        // 값을 받고 비교 -> 로직
        // B S W 리턴 -> 실행
        // queue 새로세팅
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
            System.out.println("현재가 : " + currentLast);
            //-----------------
            // 매수타이밍 
            if(currentLast < bollingerHL[1]) {
            	//BUY
            	// 얼마나 살건지 알고리즘 셋팅에 따라
            	buyCoin(brx, 10, currentLast);
            }
            else if(currentLast > bollingerHL[0]) {
            	//SELL
            	// 얼마나 팔건지 알고리즘 셋팅에 따라
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
	
	// IOC 구현하기
	public boolean IOC_commit(Bittrex brx) {
		
		return true;
	}
	
	public void buyCoin(Bittrex brx, double amount, double price) {
		
		System.out.println("BUY");
		//brx.buyMarket(coin, Double.toString(amount));
		IOC_commit(brx);
		
		//디비 쿼리 날려주기
		//transaction log로 날려줌
		ExecSQL_Insert(_ID, exchange, coin_crypto, 1, amount, price);
	}
	
	public void sellCoin(Bittrex brx, double amount, double price) {
		
		System.out.println("SELL");
		//brx.sellMarket(coin, Double.toString(amount));
		
		//디비 쿼리 날려주기
		//transaction log로 날려줌
		ExecSQL_Insert(_ID, exchange, coin_crypto, 2, amount, price);
	}
	
	public void doNothing() {
		System.out.println("WAIT");
		
		//디비 쿼리 날려주기
		ExecSQL_Insert(_ID, exchange, coin_crypto, 3, 0, 0);
	}
	
	// 가격 히스토리를 보여줌 -> ohlc 중 뭘 사용할건지 추가 / interval과 갯수 파라미터
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
        	history_queue.add(jsarr.get(4).getAsDouble()); // 종가(C)만 사용
        }
        
        return history_queue;
	}
	
	// 1회성 볼린저값 겟 , 이미 히스토리큐에 interval이 설정되어있음. 따라서 표준편차(mul)만 필요
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
        System.out.println("분산  : " + deviation);
        System.out.println("표준편차 : "+ stddev);
        System.out.println("다음 상한 : " + (average + stddev));
        System.out.println("다음 하한 : " + (average - stddev));
		
		double ret[] = {average + stddev, average - stddev};
		return ret;
	}
	
	// 거래하는 거래소를 통하여 현재값을 받음. 현재값 중 종가를 사용 (Bid, Ask, Last)
	public double getCurrentPrice_no(Bittrex brx, String coin_exchange) {
		String api_string = brx.getTicker(coin_exchange); // api콜 -> string을 가지고옴
        JsonObject json_result = new JsonParser().parse(api_string).getAsJsonObject(); // string을 json으로 변경
        String result_string = gson.toJson(json_result.get("result")); // json 중에서 result를 string으로 파싱
        JsonObject result_json = new JsonParser().parse(result_string).getAsJsonObject(); // result를 다시 json으로 파싱
        double currentLast = Double.parseDouble(result_json.get("Last").toString());
        
        return currentLast;
	}
	
	public double getCurrentPrice(Cryptowatch crypt, String coin_crypto) {
		
		String api_string = crypt.getCurrentPrice(exchange, coin_crypto); // api콜 -> string을 가지고옴
        JsonObject json_result = new JsonParser().parse(api_string).getAsJsonObject(); // string을 json으로 변경
        String result_string = gson.toJson(json_result.get("result")); // json 중에서 result를 string으로 파싱
        JsonObject result_json = new JsonParser().parse(result_string).getAsJsonObject(); // result를 다시 json으로 파싱
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
        System.out.println("sql문 삽입 성공 : " + selectSql);
		}

		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	
}
