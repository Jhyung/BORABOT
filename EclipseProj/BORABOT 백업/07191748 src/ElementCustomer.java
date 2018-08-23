public class ElementCustomer {
	
	private String email;
	private String password;

	public String getEmail() { return email; }
	public String getPassword() { return password; }
	
	public ElementCustomer(String e, String p) {
		this.email = e;
		this.password = p;
	}
	
	// 거래 생성 시 DB 입력
	public void insertDB() {
		String insertSql = String.format("INSERT INTO customer (email, password) VALUES('"
				+email+"', '"+password+"')");

		DB.Query(insertSql, "insert");
		
		DB.clean();
	}
}