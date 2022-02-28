package Other;

import java.sql.SQLException;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import DAO.ApplicationNumberDAO;

/**
 * 承認申請を行った際に必要な申請番号を作成するクラス
 * 
 *
 */
public class ApplicationNumber {
	private static final Logger log = LogManager.getLogger(ApplicationNumber.class);
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	private static ApplicationNumber only_instance = new ApplicationNumber();
	
	private ApplicationNumber() {}
	
	public static ApplicationNumber getInstance(){
		return only_instance;
	}
	
	/**
	 * 申請番号作成メソッド
	 * @param emp_num 従業員番号
	 * @return　作成した申請番号
	 */
	public Integer getAppNumber(int emp_num) {
		log.debug("getAppNumber start");
		
		//整数型の従業員番号を文字列型に型キャスト
		String empNum = String.valueOf(emp_num);
		//Randomクラスのインスタンス生成
		Random rand = new Random();
		//1000 ~ 9999までの乱数を１つ生成
		String num = String.valueOf(rand.nextInt(9000) + 1000);
		//1 ~ 9までの乱数を１つ生成
		String sub_num = String.valueOf(rand.nextInt(9) + 1);
		
		//最終的な整数型の申請番号格納用変数宣言
		int resultNum = 0;
		
		if(empNum.length() == 3) {
			num = empNum + num + sub_num;
		} else if(empNum.length() == 4) {
			num = empNum + num;
		} else {
			num = empNum.substring(0, 4) + num;
		}
		
		ApplicationNumberDAO andao = ApplicationNumberDAO.getInstance();
		
		try {
			andao.dbConenect();
			andao.createSt();
			while(andao.checkApplicationNumber(Integer.parseInt(num)) == false) {
				//1000 ~ 9999までの乱数を１つ生成
				num = String.valueOf(rand.nextInt(9000) + 1000);
				//1 ~ 9までの乱数を１つ生成
				sub_num = String.valueOf(rand.nextInt(9) + 1);
				
				if(empNum.length() == 3) {
					num = empNum + num + sub_num;
				} else if(empNum.length() == 4) {
					num = empNum + num;
				} else {
					num = empNum.substring(0, 4) + num;
				}
			}
			resultNum = Integer.parseInt(num);
			andao.setApplicationNumber(resultNum);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
		} finally {}
		return resultNum;
	}
}
