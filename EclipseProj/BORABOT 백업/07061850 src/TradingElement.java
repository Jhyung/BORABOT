
import java.text.SimpleDateFormat;
import java.util.*;

class TradingElement{
	private String id;
	private String name;
	private Boolean status;	// 봇 상태
	private String coin;
	private String exchange;
	private String strategy;
	private double startAsset;	// 시작 금액
	private Date startDate;
	private long period;
	private Date endDate = new Date();
	private double endAsset = this.startAsset*1.1;

	public String getId() { return id; }
	public String getName() { return name; }
	public Boolean getStatus() { return status; }
	public String getCoin() { return coin; }
	public String getExchange() { return exchange; }
	public String getStrategy() { return strategy; }
	public double getStartAsset() { return startAsset; }
	public Date getStartDate() { return startDate; }
	public Date getEndDate() { return endDate; }
//	
	public TradingElement() {
	}	
	
	public TradingElement(String n, String c, String e, double p, String s, Date sD, Date eD, double fP) {
		this.name = n;
		this.coin = c;
		this.exchange = e;
		this.startAsset = p;
		this.strategy = s;
		this.startDate = sD;
		this.endDate = eD;
		this.endAsset = fP;
	}
	
	// 거래 생성 시 DB 입력
	public void insertDB() {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String insertSql = String.format("INSERT INTO trade VALUES('"
				+id+"', '"+name+"', '"+coin+"', '"+exchange+"', '"+startAsset+"', '"+endAsset+"', '"+strategy
				+"', '"+dateFormat.format(startDate)+"', "+status+", '"+dateFormat.format(endDate)+"')");

		DB.Query(insertSql, "insert");
		
		DB.clean();
	}
		
//
//	public String getProfit() { return ""; }
	
//	public void setResDate() {
//		long ed = this.startDate.getTime()+ 24*60*60*1000*period;
//		this.residualDate = (ed - new Date().getTime()) / (24*60*60*1000);
//	}
	
}
