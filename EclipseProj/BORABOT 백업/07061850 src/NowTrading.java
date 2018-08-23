import java.sql.*;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import java.util.*;
import java.util.Date;

// 진행 중인 거래 정보 전송
@ServerEndpoint("/nthandle")
public class NowTrading {
	
	static boolean S = false;

    @OnMessage
    public String handleMessage(String id){
    	Gson gson = new Gson();
    			
    	ArrayList<TradingElement> nT = new ArrayList<TradingElement>();
    	nT.clear();

		
		String selectSql = String.format("SELECT * from trade where user_id=\'%s\'", id);
		
		ResultSet rs = DB.Query(selectSql, "select"); 
		
		try {
			while(rs.next()) {
				if (rs.getBoolean("status")) {
					TradingElement tE = new TradingElement(
						rs.getString("bot_name"),
						rs.getString("coin"),
						rs.getString("exchange_name"),
						rs.getDouble("start_asset"),
						rs.getString("strategy_name"),
						rs.getDate("start_date"),
						rs.getDate("end_date"),
						rs.getDouble("last_asset")
						);
					nT.add(tE);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}		
		
		// 5. DB 사용후 clean()을 이용하여 정리
		DB.clean();	
		
    	String ntJson = gson.toJson(nT);
    	return ntJson;
    }
    
    @OnError
    public void handleError(Throwable t){
        t.printStackTrace();
    }
}
