import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@ServerEndpoint("/mainhandle")
public class Borabot {
	
    @OnOpen
    public void Borabot() {
    	
    }
	

}
