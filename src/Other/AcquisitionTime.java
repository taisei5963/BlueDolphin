package Other;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 特別時間休暇における取得時間算出用クラス
 * 
 */
public class AcquisitionTime {
	
	private static final Logger log = LogManager.getLogger(AcquisitionTime.class);
	private static AcquisitionTime only_instance = new AcquisitionTime();
	
	private AcquisitionTime() {}
	
	public static AcquisitionTime getInstance() {
		return only_instance;
	}
	
	/**
	 * 特別時間休暇における取得時間算出メソッド（休憩時間計算なし）
	 * @param start_time 開始時刻
	 * @param end_time　終了時刻
	 * @return　取得時間
	 */
	public String calcAcquisitionTime(String start_time, String end_time) {
		log.info("calcAcquisitionTime start");
		Date sTime = null, eTime = null;
    	try {
    		sTime = new SimpleDateFormat("hh:mm").parse(start_time);
    		eTime = new SimpleDateFormat("hh:mm").parse(end_time);
    	} catch (ParseException e) {
			e.printStackTrace();
		} finally {}
    	
    	long fromTime = sTime.getTime();
    	long toTime = eTime.getTime();
    	
    	//ミリ秒を時間、分に変換
    	long hour = ((toTime - fromTime) / (1000 * 60 * 60)) % 24;
    	long minutes = ((toTime - fromTime) / (1000 * 60)) % 60;
    	
    	String acquisition_time = null;
    	
    	//Long型からString型への型キャスト
    	if(0<=minutes && minutes<= 9) {
			acquisition_time = Long.toString(hour) + ":0" + Long.toString(minutes);
		} else {
			acquisition_time = Long.toString(hour) + ":" + Long.toString(minutes);
		}
    	
    	log.info("calcAcquisitionTime end");
    	return acquisition_time;
	}
	
	/**
	 * 特別時間休暇における取得時間算出メソッド（休憩時間計算あり）
	 * @param start_time 開始時刻
	 * @param end_time　終了時刻
	 * @return　取得時間
	 */
	public String calcAcquisitionBreakTime(String start_time, String end_time) {
		log.info("calcAcquisitionBreakTime start");
		Date sTime = null, eTime = null;
    	try {
    		sTime = new SimpleDateFormat("hh:mm").parse(start_time);
    		eTime = new SimpleDateFormat("hh:mm").parse(end_time);
    	} catch (ParseException e) {
			e.printStackTrace();
		} finally {}
    	
    	long fromTime = sTime.getTime();
    	long toTime = eTime.getTime();
    	
    	//ミリ秒を時間、分に変換
    	long hour = (((toTime - fromTime) / (1000 * 60 * 60)) % 24) - 1;
    	long minutes = ((toTime - fromTime) / (1000 * 60)) % 60;
    	
    	String acquisition_time = null;
    	
    	//Long型からString型への型キャスト
    	if(0<=minutes && minutes<= 9) {
			acquisition_time = Long.toString(hour) + ":0" + Long.toString(minutes);
		} else {
			acquisition_time = Long.toString(hour) + ":" + Long.toString(minutes);
		}
    	
    	log.info("calcAcquisitionBreakTime end");
    	return acquisition_time;
	}
}
