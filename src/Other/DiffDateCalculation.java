package Other;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 日付の差分日数を算出するクラス
 * 
 *
 */
public class DiffDateCalculation {
	
	//ログ出力用変数宣言
	private static final Logger log = LogManager.getLogger(DiffDateCalculation.class);
	
	//唯一のインスタンス宣言
	private static DiffDateCalculation only_instance = new DiffDateCalculation();
	
	private DiffDateCalculation() {}
	
	public static DiffDateCalculation getInstance() {
		return only_instance;
	}
	
	/**
	 * 引数の日付から差分を算出するメソッド
	 * @param fromDate　差分開始日付
	 * @param toDate　差分終了日付
	 * @return　差分日数
	 */
	public int diffDate(String fromDate, String toDate) {
		
		log.info("diffDate start");
		
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTo = null;
        Date dateFrom = null;

        // Date型に変換
        try {
            dateFrom = sdf.parse(fromDate);
            dateTo = sdf.parse(toDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        // 差分の日数を計算する
        long dateTimeTo = dateTo.getTime();
        long dateTimeFrom = dateFrom.getTime();
        long dayDiff = ( dateTimeTo - dateTimeFrom  ) / (1000 * 60 * 60 * 24 );
        
        log.info("diffDate end");
        return (int) (dayDiff + 1);
    }
}