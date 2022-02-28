package Other;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 日付から曜日を取得するクラス
 *
 */
public class DayOfWeek {
	
	private static final Logger log = LogManager.getLogger(DayOfWeek.class);
	private static DayOfWeek only_instance = new DayOfWeek();
	private static final String NUMBERFORMATEXCEPTION = "NumberFormatException occurred";
	private static final String PARSEEXCEPTION = "ParseException occurred";
	
	//privateで新規のインスタンスを生成させない
	private DayOfWeek () {}
	
	/**
	 * 唯一のインスタンスを取得（＝シングルトン）
	 * @return　DayOfWeek唯一のインスタンス
	 */
	public static DayOfWeek getInstance() {
		return only_instance;
	}
	
	/**
	 * 日付から曜日を取得するメソッド
	 * @param date 曜日を取得する日付
	 * @return 各曜日を格納した配列
	 */
	public String getYobi(String date) {
		try {
			//取得する曜日を配列で設定
			String yobi[] = {"(日)","(月)","(火)","(水)","(木)","(金)","(土)"};
			
			//年・月を取得する
		    int year = Integer.parseInt(date.substring(0,4));
		    int month = Integer.parseInt(date.substring(5,7))-1;
		    int day = Integer.parseInt(date.substring(8,10));
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    sdf.setLenient(false);
		    sdf.parse(date);
			
			//取得した年月の最終年月日を取得する
		    Calendar cal = Calendar.getInstance();
		    cal.set(year, month, day);
		    //YYYY-MM-DD形式にして変換して返す
		    return yobi[cal.get(Calendar.DAY_OF_WEEK)-1];
		} catch (NumberFormatException e) {
			log.error(NUMBERFORMATEXCEPTION);
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			log.error(PARSEEXCEPTION);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 日付から曜日を取得するメソッド
	 * @param date 曜日を取得する日付
	 * @return 各曜日を格納した配列
	 */
	public String getYobiWithoutParentheses(String date) {
		try {
			//取得する曜日を配列で設定
			String yobi[] = {"日曜日","月曜日","火曜日","水曜日","木曜日","金曜日","土曜日"};
			
			//年・月を取得する
		    int year = Integer.parseInt(date.substring(0,4));
		    int month = Integer.parseInt(date.substring(5,7))-1;
		    int day = Integer.parseInt(date.substring(8,10));
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    sdf.setLenient(false);
		    sdf.parse(date);
			
			//取得した年月の最終年月日を取得する
		    Calendar cal = Calendar.getInstance();
		    cal.set(year, month, day);
		    
		    log.info("getYobi end");
		    
		    //YYYY-MM-DD形式にして変換して返す
		    return yobi[cal.get(Calendar.DAY_OF_WEEK)-1];
		} catch (NumberFormatException e) {
			log.error(NUMBERFORMATEXCEPTION);
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			log.error(PARSEEXCEPTION);
			e.printStackTrace();
			return null;
		}
	}
}
