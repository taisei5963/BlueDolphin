package Other;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * 土・日・祝祭日を除いた営業日数および所定労働時間算出メソッド
 *
 */
public class BusinessDays {
	
	private static final Logger log = LogManager.getLogger(BusinessDays.class);
	private static BusinessDays only_instance = new BusinessDays();
	
	private BusinessDays() {}
	
	public static BusinessDays getInstance() {
		return only_instance;
	}
	
	/**
	 * 今月の営業日数を算出するメソッド
	 * @param thisMonth
	 * @return
	 */
	public int getBusinessDays(int thisMonth) {
		log.info("getBusinessDays start");
    	int business_days = 0;
    	switch(thisMonth) {
    	case 1:
    		business_days = 20;
    		break;
    	case 2:
    		business_days = 18;
    		break;
    	case 3:
    		business_days = 22;
    		break;
    	case 4:
    		business_days = 20;
    		break;
    	case 5:
    		business_days = 19;
    		break;
    	case 6:
    		business_days = 22;
    		break;
    	case 7:
    		business_days = 20;
    		break;
    	case 8:
    		business_days = 22;
    		break;
    	case 9:
    		business_days = 20;
    		break;
    	case 10:
    		business_days = 20;
    		break;
    	case 11:
    		business_days = 20;
    		break;
    	case 12:
    		business_days = 22;
    		break;
    	}
    	log.info("getBusinessDays end");
    	return business_days;
    }
	
	/**
	 * 所定総労働時間を算出するメソッド
	 * @param days 算出する月
	 * @param work_hours　所定労働時間
	 * @return 所定総労働時間
	 */
	public String getScheduledTotalWorkingHours(int days, String work_hours) {
    	log.info("getScheduledTotalWorkingHours start");
		
    	//60進数を10進数に変換
    	double dhour = Double.parseDouble(work_hours.substring(0, 2));
		double dminutes = Double.parseDouble(work_hours.substring(3, 5)) / 60;
		
		//所定総労働時間を計算
		double total = days * (dhour + dminutes);
		
		//10進数を60進数に変換
		String totalTime = "";
		String strHour = String.valueOf((int)total);
		String strMinutes = String.valueOf((int)(total * 60) % 60);
		if("0".equals(strMinutes)) {
			totalTime = strHour + ":0" + strMinutes;
		} else {
			totalTime = strHour + ":" + strMinutes;
		}
		
		log.info("getScheduledTotalWorkingHours end");
		return totalTime;
    }
}
