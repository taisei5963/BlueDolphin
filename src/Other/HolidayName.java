package Other;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 日付を、国民の休日、振替休日、祝祭日、土曜日、日曜日、平日のいずれかに判別するクラス
 *
 */
public class HolidayName {
	
	private static final Logger log = LogManager.getLogger(HolidayName.class);
	//唯一のインスタンス宣言
	private static HolidayName only_instance = new HolidayName();
	
	//privateで新規のインスタンスを生成させない
	private HolidayName() {}
	
	/**
	 * 唯一のインスタンスの取得（＝シングルトン）
	 * @return HolidayName唯一のインスタンス
	 */
	public static HolidayName getInstance() {
		return only_instance;
	}
	
	/**
     * 国民の祝日であればその名前を返すメソッド
     * @param cal 名前を取得したい日
     * @return 国民の祝日の名前
     */
    public String getHolidayName(Calendar cal){
    	log.info("getHolidayName start");
        Calendar first_day = Calendar.getInstance();
        first_day.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        //if(cal.compareTo(first_day)>=0) return "値が不正";
        int year = cal.get(Calendar.YEAR);
        int date = cal.get(Calendar.DATE);
        switch (cal.get(Calendar.MONTH)-1) {
        case Calendar.JANUARY:
            switch (date) {
            case 1:
            	log.info("getHolidayName end");
                return "元日";
            default:
            	if(year==2022&&date==10)				return "成人の日";
                if(year>=2023&&isMondayHoliday(cal,2)) 	return "成人の日";
                log.info("getHolidayName end");
            }
            break;
        case Calendar.FEBRUARY:
            if (year>=1967 && date==11) return "建国記念の日";
            log.info("getHolidayName end");
            if (year>=2020 && date==23) return "天皇誕生日"; 
            log.info("getHolidayName end");
            break;
        case Calendar.MARCH:
            if(getVernalEquinoxHoliday(cal) == date) return "春分の日";
            log.info("getHolidayName end");
            break;
        case Calendar.APRIL:
            if(date==29) return "昭和の日";
            log.info("getHolidayName end");
            break;
        case Calendar.MAY:
            switch (date) {
            case 3:
            	log.info("getHolidayName end");
                return "憲法記念日";
            case 4:
            	log.info("getHolidayName end");
                return "みどりの日";
            case 5:
            	log.info("getHolidayName end");
                return "こどもの日";
            default:
                break;
            }
            break;
        case Calendar.JULY:
        	if(year == 2021 && date == 23)		return "スポーツの日";
        	log.info("getHolidayName end");
        	if(year>=2022 && isMondayHoliday(cal, 3) || year==2021 && date==22)		return "海の日";
        	log.info("getHolidayName end");
        	break;
        case Calendar.AUGUST:
        	if(year==2021 && date==8)	return "山の日";
        	log.info("getHolidayName end");
        	if(year==2021 && date==9)	return "振替休日";
        	log.info("getHolidayName end");
        	if(year>2021 && date==11)	return "山の日";
        	log.info("getHolidayName end");
        	break;
        case Calendar.SEPTEMBER:
        	if(year>=2003 && isSeptemberMondayHoliday(cal, 3)) return "敬老の日";
            log.info("getHolidayName end");
            if(getAutumnEquinoxDay(cal) == date) return "秋分の日";
            log.info("getHolidayName end");
            break;
        case Calendar.OCTOBER:
            if(year>2021 && isMondayHoliday(cal, 2)) return "体育の日";
            log.info("getHolidayName end");
            break;
        case Calendar.NOVEMBER:
            switch(date){
            case 3:
            	log.info("getHolidayName end");
                return "文化の日";
            case 23:
            	log.info("getHolidayName end");
                return "勤労感謝の日";
            default:
                break;
            }
            break;
        case Calendar.DECEMBER:
        	if(year==2021 && date==29) return "年末・年始休暇";
        	if(year==2021 && date==30) return "年末・年始休暇";
        	if(year==2021 && date==31) return "年末・年始休暇";
        	log.info("getHolidayName end");
            break;
        default:
            break;
        }
        if(isTransferHoliday(first_day)) return "振替休日";
        if(isNationalHoliday(first_day)) return "国民の休日";
        if(isSaturdayAndSunday(first_day).equals("土曜日"))	return "土曜日";
        if(isSaturdayAndSunday(first_day).equals("日曜日"))	return "日曜日";
        log.info("getHolidayName end");
        return "平日";
    }

    /**
     * ハッピーマンデーを判定するメソッド(9月以外)
     * @param cal Calendarクラスにセットされた日付
     * @param i その月の週数
     * @return 曜日が月曜日かつ、日数が「週数*7-6」以上かつ、週数が「i*7」以下の場合「true」を返却し<br>
     *         それ以外の場合「false」を返却する
     */
    private boolean isMondayHoliday(Calendar cal, int i){
    	int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        
        System.out.println(cal.get(Calendar.DAY_OF_WEEK));
        System.out.println(cal.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        
        if(month == 1 || month == 7 || month == 10) {
        	if(cal.get(Calendar.DAY_OF_WEEK) == 5) {
        		if(cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 2) {
        			cal.set(Calendar.DAY_OF_WEEK, 2);
        		} else if(cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 3) {
        			cal.set(Calendar.DAY_OF_WEEK, 2);
        		}
                return cal.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY && day>=i*7-6 && day<=i*7;
        	}
        }
        return false;
    }
    
    /**
     * ハッピーマンデーを判定するメソッド(9月)
     * @param cal Calendarクラスにセットされた日付
     * @param i その月の週数
     * @return 曜日が月曜日かつ、日数が「週数*7-6」以上かつ、週数が「i*7」以下の場合「true」を返却し<br>
     *         それ以外の場合「false」を返却する
     */
    private boolean isSeptemberMondayHoliday(Calendar cal, int i){
    	int day = cal.get(Calendar.DATE);
    	
    	System.out.println(cal.get(Calendar.DAY_OF_WEEK));
        System.out.println(cal.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        
        if(cal.get(Calendar.DAY_OF_WEEK) == 4) {
        	cal.set(Calendar.DAY_OF_WEEK, 2);
        	log.debug("isMondayHoliday end");
            return cal.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY && day>=i*7-6 && day<=i*7;
        }
        return false;
    }
    
    /**
     * 振替休日かどうかを判断するメソッド
     * @param cal 判断する日付
     * @return 判断する日付が日曜日かつ、祝日出会った場合「true」を返し<br>
     *         そうでない場合は、「false」を返却する
     */
    private boolean isTransferHoliday(Calendar cal) {
    	int year = cal.get(Calendar.YEAR);
    	int month = cal.get(Calendar.MONTH);
    	int date  = cal.get(Calendar.DATE) - 1;
    	Calendar transferDay = Calendar.getInstance();
    	transferDay.set(year, month-1, date);
    	if((transferDay.get(Calendar.DAY_OF_WEEK)) == Calendar.SUNDAY) {
    		transferDay.set(year, month, date);
    		if(isHolidayName(transferDay)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 国民の祝日か否かを判断するメソッド
     * @param cal 判断する日付
     * @return 平日が祝日に挟まれている場合は、「true」を返却し、<br>
     *         それ以外の場合は、「false」を返却する
     */
    private boolean isNationalHoliday(Calendar cal) {
    	Calendar previousDay = Calendar.getInstance();
    	Calendar afterDay = Calendar.getInstance();
    	
    	int year = cal.get(Calendar.YEAR);
    	int month = cal.get(Calendar.MONTH);
    	int previousDate  = cal.get(Calendar.DATE) - 1;
    	int afterDate  = cal.get(Calendar.DATE) + 1;
    	
    	previousDay.set(year, month, previousDate);
    	afterDay.set(year, month, afterDate);
    	
    	if(isHolidayName(previousDay) && isHolidayName(afterDay)) {
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * 日付が国民の祝日か否かを判断するメソッド
     * @param cal 判断するメソッド
     * @return previousholidayName 祝日名
     */
    private boolean isHolidayName(Calendar cal) {
    	
    	String holidayName = null;
    	int year = cal.get(Calendar.YEAR);
    	int date = cal.get(Calendar.DATE);
        switch (cal.get(Calendar.MONTH)-1) {
        case Calendar.FEBRUARY:
        	switch(date) {
        	case 11:
        		holidayName = "建国記念の日";
        	case 23:
        		holidayName = "天皇誕生日";
        	default:
        		holidayName = null;
        	}
            break;
        case Calendar.APRIL:
        	switch(date) {
        	case 29:
        		holidayName = "昭和の日";
        	default:
        		holidayName = null;
        	}
            break;
        case Calendar.MAY:
            switch (date) {
            case 3:
            	holidayName = "憲法記念日";
            case 4:
            	holidayName = "みどりの日";
            case 5:
            	holidayName = "こどもの日";
            default:
            	holidayName = null;
            }
            break;
        case Calendar.AUGUST:
        	if(year==2021 && date==8)	holidayName = "山の日";
        	if(year>2021 && date==11)	holidayName = "山の日";
        	break;
        case Calendar.SEPTEMBER:
        	if(year>=2003 && isSeptemberMondayHoliday(cal, 3)) holidayName = "敬老の日";
            if(getAutumnEquinoxDay(cal) == date) holidayName = "秋分の日";
            break;
        case Calendar.NOVEMBER:
            switch(date){
            case 3:
            	holidayName = "文化の日";
            case 23:
            	holidayName = "勤労感謝の日";
            default:
            	holidayName = null;
            }
            break;
        case Calendar.DECEMBER:
        	if(year==2021 && date==29) holidayName = "年末・年始休暇";
        	if(year==2021 && date==30) holidayName = "年末・年始休暇";
        	if(year==2021 && date==31) holidayName = "年末・年始休暇";
        	break;
        default:
        	holidayName = null;
        }
        if(holidayName != null) {
        	log.info("holidayName : {}", holidayName);
        	return true;
        }
    	return false;
    }
    
    /**
     * その年の春分の日の日にちを算出するメソッド
     * @param cal
     * @return result その年の春分の日の日にち
     */
    private int getVernalEquinoxHoliday(Calendar cal) {
    	int year = cal.get(Calendar.YEAR);
    	int result = 0;
    	if(1980 <= year && year <= 2099) {
    		result = (int) (20.8431 + 0.242194 * (year - 1980) - ((year - 1980) / 4));
    	} else if(2100 <= year && year <= 2150) {
    		result = (int) (21.8510 + 0.242194 * (year - 1980) - ((year - 1980) / 4));
    	}
    	return result;
    }
    
    /**
     * その年の秋分の日の日にちを算出するメソッド
     * @param cal
     * @return result その年の秋分の日の日にち
     */
    private int getAutumnEquinoxDay(Calendar cal) {
    	int year = cal.get(Calendar.YEAR);
    	int result = 0;
    	if(1980 <= year && year <= 2099) {
    		result = (int) (23.2488 + 0.242194 * (year - 1980) - ((year - 1980) / 4));
    	} else if(2100 <= year && year <= 2150) {
    		result = (int) (24.2488 + 0.242194 * (year - 1980) - ((year - 1980) / 4));
    	}
    	return result;
    }
    
    /**
     * 日付が土曜日か日曜日かを判断して予備もとに返却するメソッド
     * @param cal 判断する日付
     * @return result 「土曜日」か「日曜日」かの文字列
     */
    private String isSaturdayAndSunday(Calendar cal) {
    	String result = null;
    	
    	int year = cal.get(Calendar.YEAR);
    	int month = cal.get(Calendar.MONTH);
    	int date  = cal.get(Calendar.DATE);
    	Calendar transferDay = Calendar.getInstance();
    	transferDay.set(year, month-1, date);
    	if((transferDay.get(Calendar.DAY_OF_WEEK)) == Calendar.SUNDAY) {
    		result = "日曜日";
    	} else if((transferDay.get(Calendar.DAY_OF_WEEK)) == Calendar.SATURDAY){
    		result = "土曜日";
    	} else {
    		result = "";
    	}
    	return result;
    }
}
