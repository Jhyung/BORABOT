import java.util.Date;

class initializing {
	private String exchange;
	private String coin_target;
	private String coin_base = "krw";
	private String _ID;
	private double priceAmount;
	private Date startDate;
	private Date endDate;
	private String botName;
	private String strategyName;
	
	public initializing(TradingElement t) {
		this.exchange = t.getExchange();
		this.coin_target = t.getCoin();
		this._ID = t.getId();
		this.priceAmount = t.getPrice();
		this.startDate = t.getStartDate();
		this.endDate = t.getEndDate();
		this.botName = t.getName();
		this.strategyName = t.getStrategy();		
	}

	private String API_KEY="";
	private String Secret_KEY="";
	
	public void main() {
		String coin_ex = "";
		String coin_crypto = coin_target + coin_base;
		if (exchange.equals("bittrex")) {
			coin_ex = coin_base + "-" + coin_target;
		}
		else if(exchange.equals("bithumb")) {
			coin_ex = coin_target;
		}

//		DB.useDB(selectSql, "insert"); // trade봇정보 insert
		tradingBot trbot = new tradingBot(priceAmount, _ID, startDate, endDate, exchange, coin_crypto, coin_ex, "none",
				API_KEY, Secret_KEY, botName);
		if (strategyName.equals("bollingerPatternNaked"))
			trbot.bollingerPatternNaked();
		else if (strategyName.equals("Bollingertrade"))
			trbot.Bollingertrade();
		else if (strategyName.equals("patterNakedTrade"))
			trbot.patterNakedTrade();
		else 
			System.out.println("전략 선택 오류다!!!");
	}

}