package exchangeAPI;


public interface exAPI {
	
	public String buyCoin(String coin, String base, String qty);
	
	public String sellCoin(String coin, String base, String qty);
	
	public double getTicker(String coin, String base);

	public double getBalance(String currency);
	
}

class ExException extends Exception{
	
}