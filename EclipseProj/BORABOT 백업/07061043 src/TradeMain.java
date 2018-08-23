import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.Gson;


// �� ���� Ŭ����
@ServerEndpoint("/mainhandle")
public class TradeMain extends Thread {
	
	private TradingElement tElement;
	
	public TradeMain() {}
	public TradeMain(TradingElement t) { 
		this.tElement = t;
		}	// �� �̸� �����ϴ� ������
	
	// �� ���� �Լ�
	public void run() {
        System.out.println("������ ����");

    	initializing bot = new initializing(tElement); 
    	bot.main();
	}

	// ������ ���� json ���� ��
    @OnMessage
    public void handleMessage(String message){
        System.out.println("�޽����� �޾ҽ��ϴ�");
        
        // json �Ľ�
        Gson gson = new Gson();
        TradingElement tInfo = gson.fromJson(message, TradingElement.class);

        if (tInfo.getStatus() == true) {	// �ŷ� ���� (DB�� �ŷ� ���� �Է�)
            tInfo.insertDB();
		    TradeMain bot = new TradeMain(tInfo);
		    bot.start();
        }
    
        else {	// �ŷ� ���� (DB�� �ŷ� ����, �ŷ� ���� �ð� ����) 
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		DB.Query(String.format(
    				"update trade set on_going=0, end_date=\'%s\' where user_id=\'%s\' and bot_name=\'%s\' and on_going=1" ,
        			dateFormat.format(new Date()), tInfo.getId(), tInfo.getName()), "insert");		
    		DB.clean();		
        }
    }
    
    @OnError
    public void handleError(Throwable t){
        t.printStackTrace();
    }
}
