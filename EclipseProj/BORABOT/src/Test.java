import java.sql.SQLException;

import base.DB;

public class Test {

	public static void main(String[] args) {
		DB useDB = new DB();

		int i = 1;
		while(i++<60) {
			String sql = String.format("INSERT INTO trans_log VALUES(\"%s\", \"%s\", \"%s\", \"%s\", %s, \"%s\", \"%s\", %s, %s, %s, %s)", "test", "tqtqtqtq", "bithumb", "2018-07-27 15:20:"+i, 1, "btckrw", 8857000.0, 0, 0, 0, 0 );
			System.out.println(sql);
			try {
				useDB.Query(sql, "insert");
			} catch (SQLException e) {
				e.printStackTrace();			
			}			
			useDB.clean();
		}		
		
	}

}
