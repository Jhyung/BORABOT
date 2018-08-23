import java.sql.*;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import java.util.*;
import java.util.Date;

// ���� ���� �ŷ� ���� ����
@ServerEndpoint("/nthandle")
public class NowTrading {
	
	static boolean S = false;

    @OnMessage
    public String handleMessage(String id){
    	Gson gson = new Gson();
    			
    	ArrayList<TradingElement> nT = new ArrayList<TradingElement>();
    	nT.clear();

		// 1. DB ��� ��ü useDB ����
		DB useDB = new DB();	
		
		// 2. ������ String ����
		String selectSql = String.format("SELECT * from trade where user_id=\'%s\'", id);
		
		// 3. ResultSet rs�� useDB.Query(������, "select" or "insert")�� ����
		ResultSet rs = useDB.Query(selectSql, "select"); 
		
		// 4-1. SELECT���� ��� rs�� ����� ResultSet���� ���ϴ� ���� ����
		try {
			while(rs.next()) {
				if (rs.getBoolean("on_going")) {
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
		
		// 5. DB ����� clean()�� �̿��Ͽ� ����
		useDB.clean();	
		
    	String ntJson = gson.toJson(nT);
    	return ntJson;
    }
    
    @OnError
    public void handleError(Throwable t){
        t.printStackTrace();
    }
}
