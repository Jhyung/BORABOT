import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

class TradeBot {
	private String exchange;
	private String coin_target;
	private String coin_base = "krw";
	private String email;
	private double sAsset;
	private Date startDate;
	private Date endDate;
	private String botName;
	private String strategyName;
	
	public TradeBot(ElementTrading t) {
		this.exchange = t.getExchange();
		this.coin_target = t.getCoin();
		this.email = t.getEmail();
		this.sAsset = t.getStartAsset();
		this.startDate = t.getStartDate();
		this.endDate = t.getEndDate();
		this.botName = t.getName();
		this.strategyName = t.getStrategy();		
	}

	private String API_KEY="";
	private String Secret_KEY="";
	
	public void main() {
		
		// 거래소별 API용 값
		String coin_ex = "";
		String coin_crypto = coin_target + coin_base;
		if (exchange.equals("bittrex")) {
			coin_ex = coin_base + "-" + coin_target;
		}
		else if(exchange.equals("bithumb")) {
			coin_ex = coin_target;
		}

		while(isRun(email, botName)) {
			
			try {
				Thread.sleep(60*10+50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (strategyName.equals("bollingerPatternNaked"))
				System.out.println("bollingerPatternNaked");
			else if (strategyName.equals("Bollingertrade"))
				System.out.println("Bollingertrade");
			else if (strategyName.equals("patterNakedTrade"))
				System.out.println("patterNakedTrade");
			else 
				System.out.println("전략 선택 오류다!!!");
			
			// 거래 종류, 거래 로그
		}
	}


	// 루프 반복 결정
	static public boolean isRun(String email, String botname) {
		String selectSql = String.format(
				"select status from trade where email = \'%s\' and bot_name = \'%s\'", email, botname);

		ResultSet rs = DB.Query(selectSql, "select");
		boolean b = true;
		try {
			while(rs.next()) {  
				b = rs.getBoolean("status");
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}
		return b;		
	}
	
	public void buyCoin() {	}

	public void sellCoin() {}

	public void doNothing() {}

}