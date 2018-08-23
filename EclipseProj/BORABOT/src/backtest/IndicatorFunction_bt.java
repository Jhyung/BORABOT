package backtest;

import DB.DB_ohlc;

import java.sql.ResultSet;
import java.sql.SQLException;

class IndicatorFunction_bt {

   public static double[][] get_HLCV_HistoryArray(String exchange, String coin, String base, int interval,
         long startUt, long endUt) throws Exception {

      String symb;
      if (exchange.equals("binance") || exchange.equals("hitbtc") || exchange.equals("BINANCE") || exchange.equals("HITBTC")) {
         symb = coin + base;
      } else {
         symb = coin;
      }
      
      long period = (endUt - startUt) / interval;

      DB_ohlc db = new DB_ohlc();
      ResultSet rs = null;
      
//      String sql = String.format("SELECT h,l,c,v,uTime FROM %sOHLC_%s_%s ORDER BY uTime ASC LIMIT %s", exchange, interval,
//            symb, period);
      String sql = String.format(
    	      "SELECT h,l,c,v,uTime FROM %sOHLC_%s_%s WHERE uTime BETWEEN %s AND %s ORDER BY uTime ASC", exchange,
    	      interval, symb, startUt - 10, endUt + 10);
      
      rs = db.Query(sql, "select");
      rs.last();
      int size = rs.getRow();
      rs.first();

      ///////////////////////////////////////////// 매우중요
      ///////////////////////////////////////////// 매우중요
      ///////////////////////////////////////////// 매우중요
      ///////////////////////////////////////////// 매우중
      double ret[][] = new double[size - 1][5];    // SIZE - 1 ?????????????????? 를 해줘야 한다! 왜그런지는 rs.last 와 rs.first의 기능을
                                       // 잘 몰라서.
      int cnt = 0;
      try {
         while (rs.next()) {
            ret[cnt][0] = rs.getDouble(1);
            ret[cnt][1] = rs.getDouble(2);
            ret[cnt][2] = rs.getDouble(3);
            ret[cnt][3] = rs.getDouble(4);
            ret[cnt][4] = rs.getLong(5);
            cnt++;
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      db.clean();
      
      return ret;
   }
   
   public static double[][] get_HLCV_HistoryArrayModified(String exchange, String coin, String base, int interval,
         long startUt, long endUt) throws Exception {

      String symb;
      if (exchange.equals("binance") || exchange.equals("hitbtc")) {
         symb = coin + base;
      } else {
         symb = coin;
      }
      
      long period = (endUt - startUt) / interval;

      DB_ohlc db = new DB_ohlc();
      ResultSet rs = null;
      
      String sql = String.format(
            "SELECT h,l,c,v,uTime FROM %sOHLC_%s_%s ORDER BY uTime ASC LIMIT %s ", exchange,
            interval, symb, period);
      
      // System.out.println("error? " +sql);
      rs = db.Query(sql, "select");
      // int size = (int) (before - after) / interval;


      // System.out.println("indicatorFunc rs result number : " + rs.getRow());
      int size = (int)period;

      ///////////////////////////////////////////// 매우중요
      ///////////////////////////////////////////// 매우중요
      ///////////////////////////////////////////// 매우중요
      ///////////////////////////////////////////// 매우중
      double ret[][] = new double[size][5];    // SIZE - 1 ?????????????????? 를 해줘야 한다! 왜그런지는 rs.last 와 rs.first의 기능을
                                       // 잘 몰라서.
      int cnt = 0;
      try {
         while (rs.next()) {
            ret[cnt][0] = rs.getDouble(1);
            ret[cnt][1] = rs.getDouble(2);
            ret[cnt][2] = rs.getDouble(3);
            ret[cnt][3] = rs.getDouble(4);
            ret[cnt][4] = rs.getLong(5);
            cnt++;
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      db.clean();
      
      return ret;
   }

   // return[0] = 고가, return[1] = 저가, return[2] = 종가, return[3] = 볼륨
   // public static double[][] get_HLCV_HistoryArray(String exchange, String coin,
   // String base, long startUt, int interval, int period_day) throws Exception {
   //
   // long now = startUt;
   //
   // String symb;
   // if (exchange == "binance") {
   // symb = coin + base;
   // } else {
   // symb = coin;
   // }
   //
   // DB_ohlc db = new DB_ohlc();
   // ResultSet rs = null;
   // String sql = String.format("SELECT h,l,c,v,uTime FROM %sOHLC_%s_%s WHERE
   // uTime BETWEEN %s AND %s", exchange, interval, symb,
   // now - ( (period_day-1) * interval) - 100, now + 100);
   // //System.out.println("error? " +sql);
   // rs = db.Query(sql, "select");
   // //int size = (int) (before - after) / interval;
   //
   // rs.last();
   // //System.out.println("indicatorFunc rs result number : " + rs.getRow());
   // int size = rs.getRow();
   // rs.first();
   // double ret[][] = new double[size][5];
   // int cnt = size-1;
   //
   // try {
   // while (rs.next()) {
   // ret[cnt][0] = rs.getDouble(1);
   // ret[cnt][1] = rs.getDouble(2);
   // ret[cnt][2] = rs.getDouble(3);
   // ret[cnt][3] = rs.getDouble(4);
   // ret[cnt][4] = rs.getDouble(5);
   // //System.out.println("uT : " + (long)ret[cnt][4] + " : " + ret[cnt][0]);
   //
   // cnt++;
   // }
   // } catch (SQLException e) {
   // // TODO Auto-generated catch block
   // e.printStackTrace();
   // }
   // /*
   // for(int i = 0; i < ret.length; i++) {
   // for(int j = 0; j < ret[i].length; j++) {
   // System.out.print(ret[i][j]+" ");
   // }
   // System.out.println();
   // }
   // */
   // return ret;
   // }
   //
   // public static Queue<Double> getHistoryQueue_bt(String exchange, String coin,
   // String base, long startUt, int interval, int period_day){
   //
   // Queue<Double> historyQueue = new LinkedList<Double>();
   //
   // long now = startUt;
   // String symb;
   // if (exchange == "binance") {
   // symb = coin + base;
   // } else {
   // symb = coin;
   // }
   //
   // DB_ohlc db = new DB_ohlc();
   // ResultSet rs = null;
   // String sql = String.format("SELECT c FROM %sOHLC_%s_%s WHERE uTime BETWEEN %s
   // AND %s", exchange, interval, symb,
   // now - ((period_day-1) * interval) - 10, now + 10);
   // //System.out.println(sql);
   // rs = db.Query(sql, "select");
   // int cnt = 0;
   // try {
   // while (rs.next()) {
   //
   // historyQueue.add(rs.getDouble(1));
   // }
   // } catch (SQLException e) {
   // // TODO Auto-generated catch block
   // e.printStackTrace();
   // }
   //
   // //System.out.println("indicatorFunc rs result number : " + cnt);
   // db.clean();
   //
   //
   // return historyQueue;
   // }
   //
   // public static Queue<Double> getHistoryQueue(CryptowatchAPI crypt, String
   // exchange, String coin, String base, int interval, int period_day) throws
   // Exception{
   //
   // Gson gson = new Gson();
   // Date date = new Date();
   // long U_current = date.getTime() / 1000;
   // int size = period_day;
   //
   // String ohlc_string = crypt.getOHLC(exchange, coin, base, U_current -
   // (period_day * interval), interval);
   // JsonObject ohlc_json = new JsonParser().parse(ohlc_string).getAsJsonObject();
   // String ohlc_result_string = gson.toJson(ohlc_json.get("result"));
   // JsonObject ohlc_result_json = new
   // JsonParser().parse(ohlc_result_string).getAsJsonObject();
   // JsonArray ohlc_jsarr =
   // ohlc_result_json.get(Long.toString(interval)).getAsJsonArray();
   //
   // Queue<Double> historyQueue = new LinkedList<Double>();
   //
   // JsonArray jsarr;
   // int bojung = ohlc_jsarr.size() - size;
   // if(bojung < 0) {
   // System.out.println("cryptowatch error!");
   // //Error
   // throw new Exception();
   // }
   //
   //
   // for (int i = 0; i < size; i++) {
   // jsarr = ohlc_jsarr.get(i+bojung).getAsJsonArray();
   // // 4번째가 종가데이터
   // historyQueue.add(jsarr.get(4).getAsDouble());
   // }
   //
   // return historyQueue;
   // }
   //
   // //며칠치 startUt부터 period_day만큼의 갯수를 가지고 오는 것임!
   // public static double[] getHistoryArray(String exchange, String coin, String
   // base, long startUt, int interval, int period_day) throws Exception {
   //
   // int size = period_day;
   // double ret[] = new double[size];
   // long now = startUt;
   // String symb;
   // if (exchange == "binance") {
   // symb = coin + base;
   // } else {
   // symb = coin;
   // }
   //
   // DB_ohlc db = new DB_ohlc();
   // ResultSet rs = null;
   // String sql = String.format("SELECT c FROM %sOHLC_%s_%s WHERE uTime BETWEEN %s
   // AND %s", exchange, interval, symb,
   // now - ((period_day-1) * interval) - 10, now + 10);
   // //System.out.println(sql);
   // rs = db.Query(sql, "select");
   // int cnt = 0;
   // try {
   // while (rs.next()) {
   // //System.out.println(cnt);
   // ret[cnt++] = rs.getDouble(1);
   // }
   // } catch (SQLException e) {
   // // TODO Auto-generated catch block
   // e.printStackTrace();
   // }
   //
   // //System.out.println("indicatorFunc rs result number : " + cnt);
   // db.clean();
   // return ret;
   //
   // }
   //
   // public static double[] getHistoryVolumeArray(String exchange, String coin,
   // String base, long startUt, int interval, int period_day) throws Exception
   // {
   // int size = period_day;
   // double ret[] = new double[size];
   // long now = startUt;
   //
   // String symb;
   // if (exchange == "binance") {
   // symb = coin + base;
   // } else {
   // symb = coin;
   // }
   //
   // DB_ohlc db = new DB_ohlc();
   // ResultSet rs = null;
   // String sql = String.format("SELECT v FROM %sOHLC_%s_%s WHERE uTime BETWEEN %s
   // AND %s", exchange, interval, symb,
   // now - ((period_day-1) * interval) - 100, now + 100);
   //
   // rs = db.Query(sql, "select");
   //
   // int cnt = 0;
   // try {
   // while (rs.next()) {
   // ret[cnt++] = rs.getDouble(1);
   // }
   // } catch (SQLException e) {
   // // TODO Auto-generated catch block
   // e.printStackTrace();
   // }
   // db.clean();
   //
   // return ret;
   // }
   //
   // public static double[] getHistoryMeanPriceArray(String exchange, String coin,
   // String base, long startUt, int interval, int period_day) throws Exception {
   //
   // int size = period_day;
   // double ret[] = new double[size];
   // long now = System.currentTimeMillis() / 1000;
   // String symb;
   // if (exchange == "binance") {
   // symb = coin + base;
   // } else {
   // symb = coin;
   // }
   //
   // DB_ohlc db = new DB_ohlc();
   // ResultSet rs = null;
   // String sql = String.format("SELECT h,l,c FROM %sOHLC_%s_%s WHERE uTime
   // BETWEEN %s AND %s", exchange, interval, symb,
   // now - ((period_day-1) * interval) - 100, now + 100);
   //
   // rs = db.Query(sql, "select");
   //
   // int cnt = 0;
   // try {
   // while (rs.next()) {
   // ret[cnt++] = ( rs.getDouble(1) + rs.getDouble(2) + rs.getDouble(3) ) / 3;
   // }
   // } catch (SQLException e) {
   // // TODO Auto-generated catch block
   // e.printStackTrace();
   // }
   //
   // return ret;
   // }

   public static double getEMA(double[] arr) {

      int len = arr.length;
      double d[] = new double[len];
      d[0] = arr[0];

      for (int i = 1; i < len; i++) {
         double k;
         k = 2.0 / (i + 2);
         d[i] = d[i - 1] * (1 - k) + arr[i] * k;
      }

      return d[len - 1];
   }

   public static double getMACDEma(double[] arr) {

      int len = arr.length;
      double d[] = new double[len];
      d[0] = sumDouble(arr) / len;

      for (int i = 1; i < len; i++) {

         double factor = (2 / (i + 1 + 1));
         d[i] = (arr[i] - d[i - 1]) * factor + d[i - 1];
      }

      return d[len - 1];

   }

   public static double sumDouble(double[] arr) {

      double ret = 0;

      for (int i = 0; i < arr.length; i++) {

         ret += arr[i];
      }

      return ret;
   }

   public static double[] makeSublist(double[] arr, int s, int e) {

      double[] ret = new double[e - s + 1];

      for (int i = 0; i < e - s + 1; i++) {

         ret[i] = arr[i + s];

      }
      return ret;
   }

   public static double[][] makeSublist2d(double[][] arr, int s, int e) {

      double[][] ret = new double[e - s + 1][];

      for (int i = 0; i < e - s + 1; i++) {

         ret[i] = arr[i + s];
      }

      return ret;
   }

   public static double[] toPriceHistory(double[][] arr) {

      double[] ret = new double[arr.length];

      for (int i = 0; i < arr.length; i++) {
         ret[i] = arr[i][2];
      }

      return ret;
   }

   public static double[] toVolumeHistory(double[][] arr) {

      double[] ret = new double[arr.length];

      for (int i = 0; i < arr.length; i++) {
         ret[i] = arr[i][3];
      }

      return ret;
   }

}