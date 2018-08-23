import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.Gson;


// 봇 메인 클래스
@ServerEndpoint("/mainhandle")
public class TradeMain extends Thread {
	
	private TradingElement tElement;
	
	public TradeMain() {}
	public TradeMain(TradingElement t) { 
		this.tElement = t;
		}	// 봇 이름 설정하는 생성자
	
	// 봇 실행 함수
	public void run() {
        System.out.println("스레드 실행");

    	TradeBot bot = new TradeBot(tElement); 
    	bot.main();
	}

	// 웹소켓 통해 json 왔을 떄
    @OnMessage
    public void handleMessage(String message){
        System.out.println("메시지를 받았습니다");
        
        // json 파싱
        Gson gson = new Gson();
        TradingElement tInfo = gson.fromJson(message, TradingElement.class);

        if (tInfo.getStatus() == true) {	// 거래 시작 (DB에 거래 정보 입력)
            tInfo.insertDB();
//		    TradeMain bot = new TradeMain(tInfo);
//		    bot.start(); 
        }
    
        else {	// 거래 종료 (DB의 거래 상태, 거래 종료 시간 변경) 
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		DB.Query(String.format(
    				"update trade set status=0, end_date=\'%s\' where email=\'%s\' and bot_name=\'%s\' and status=1" ,
        			dateFormat.format(new Date()), tInfo.getEmail(), tInfo.getName()), "insert");		
    		DB.clean();		
        }
    }
    
    @OnError
    public void handleError(Throwable t){
        t.printStackTrace();
    }
}
