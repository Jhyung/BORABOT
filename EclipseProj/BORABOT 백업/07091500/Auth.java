import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

@ServerEndpoint("/authhandle")
public class Auth {

	// 웹소켓 통해 json 왔을 떄
    @OnMessage
    public void handleMessage(String message){        
        // json 파싱
        Gson gson = new Gson();
        CustomerElement aInfo = gson.fromJson(message, CustomerElement.class);

        System.out.println(aInfo.getEmail() + aInfo.getPassword());
        aInfo.insertDB();
    }
    
    @OnError
    public void handleError(Throwable t){
        t.printStackTrace();
    }
}
