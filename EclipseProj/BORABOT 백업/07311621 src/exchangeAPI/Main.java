package exchangeAPI;
import java.util.HashMap;

public class Main {
    public static void main(String args[]) {
		Api_Client api = new Api_Client("c25a72871440aeb07b06c2e0a2f09c99",
			"3ce982f1549ef6db53d218db6d496f6b");
	
		HashMap<String, String> rgParams = new HashMap<String, String>();
		rgParams.put("currency", "BTC");
		rgParams.put("apiKey", "c25a72871440aeb07b06c2e0a2f09c99");
		rgParams.put("secretKey", "3ce982f1549ef6db53d218db6d496f6b");
	
	
		try {
		    String result = api.callApi("/info/ticker", rgParams);
		    System.out.println(result);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
    }
}

