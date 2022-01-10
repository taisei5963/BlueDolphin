package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Bean.LoginBean;

/**
 * 削除対象従業員情報を持つデータベースと連携するDAOクラス
 *
 */
public class DeleteAccountDAO{
	
	//唯一のインスタンス生成
	private static DeleteAccountDAO only_instance = new DeleteAccountDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(DeleteAccountDAO.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String NULLPOINTEREXCEPTION = "NullPointerException occurred";
	private static final String DBWORDING = "Data acquisition failure";
	
	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	private ResultSet rs;
	
	/**
	 * privateのため、新規のインスタンスを作らせない
	 */
	private DeleteAccountDAO() {}
	
	/**
	 * 唯一のインスタンスを取得(=シングルトン)
	 * @return KintaiDAOの唯一のインスタンス
	 */
	public static DeleteAccountDAO getInstance() {
		log.info("getInstance start");
		log.info("getInstance end");
		return only_instance;
	}
	
	/**
	 * 特定のデータベースとの接続（セッション）を生成
	 * @throws SQLException データベース処理で問題が発生した場合
	 */
	public void dbConenect() throws SQLException {
		log.info("dbConenect start");
		ConnectMySQL cm = ConnectMySQL.getInstance();
		cnct = cm.cnct();
		log.debug("cnct : {}", cnct);
		log.info("dbConenect end");
	}
	
	/**
	 * SQL文を実行し、結果を返却するためのオブジェクト生成
	 * @throws SQLException データベース処理で問題が発生
	 */
	public void createSt() throws SQLException {
		log.info("createSt start");
		st = cnct.createStatement();
		log.debug("st : {}", st);
		log.info("createSt end");
	}
	
	/**
	 * 特定データベースとの接続（セッション）をクローズする
	 */
	public void dbDiscnct() {
		log.info("dbDiscnct start");
		try {
			if(st != null)		st.close();
			if(cnct != null)	cnct.close();
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
		}
		log.info("dbDiscnct end");
	}
	
	/**
	 * 入力された従業員番号から従業員情報を取得しリストに格納するメソッド
	 * @param num　従業員番号
	 * @return empList　従業員情報を格納したリスト
	 * @throws SQLException　データベース処理に問題が発生した場合
	 */
	public ArrayList<LoginBean> getEmployeeInfo(Integer num) throws SQLException{
		log.debug("getEmployeeInfo start");
		
		//データ格納用リスト変数宣言
		ArrayList<LoginBean> empList = new ArrayList<LoginBean>();
		
		//引数のNULLチェック（NULLの場合は、Exceptionをthrowする。）
		if(num == null) {
			log.error(NULLPOINTEREXCEPTION);
			throw new NullPointerException();
		}
		
		//SQL文
		String sql = "select emp_num, emp_name, emp_mail, emp_pass from employee where emp_num = '" + num + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		if(rs.next()) {
			LoginBean lb = new LoginBean();
			lb.setNumber(rs.getInt("emp_num"));
			lb.setName(rs.getString("emp_name"));
			lb.setEmail(rs.getString("emp_mail"));
			lb.setPassword(rs.getString("emp_pass"));
			empList.add(lb);
		} else {
			log.error(DBWORDING);
			return null;
		}
		log.debug("getEmployeeInfo end");
		return empList;
	}
	
	/**
	 * 該当する従業員の契約時間情報を削除するメソッド
	 * @param num　従業員番号
	 * @return　TRUE: 削除が正常終了した場合<br>FALSE: データが存在しないもしくは、削除が正常終了しなかった場合
	 * @throws SQLException　データベース処理で問題が発生した場合
	 */
	public boolean deleteContractTime(Integer num) throws SQLException {
		log.debug("deleteEmployeeInfo start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL
		String sql = "select start_time, end_time, standard_time from contract_time where emp_num = '" + num + "';";
		rs = st.executeQuery(sql);
		//データが取得できた場合は、該当データを削除
		if(rs.next()) {
			sql = "delete from contract_time where emp_num = '" + num + "'";
			st.executeUpdate(sql);
			cnct.commit();
			log.debug("deleteEmployeeInfo end");
			return true;
		} else {
			log.error("データが存在しない、もしくは削除が正常に終了しませんでした。");
			return false;
		}
	}
	
	/**
	 * 従業員の勤怠情報を削除するメソッド
	 * @param num　従業員番号
	 * @return　TRUE: 削除が正常終了した場合<br>FALSE: データが存在しないもしくは、削除が正常終了しなかった場合
	 * @throws SQLException　データベース処理で問題が発生した場合
	 */
	public boolean deleteEmployeeKintai(Integer num) throws SQLException {
		log.debug("deleteEmployeeKintai start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL
		String sql = "select emp_workday, commuting_time, leave_time, actual_hours, overtime_hours, remarks from emp_kintai where emp_num = '" + num + "';";
		rs = st.executeQuery(sql);
		//データが取得できた場合は、該当データを削除
		if(rs.next()) {
			sql = "delete from emp_kintai where emp_num = '" + num + "'";
			st.executeUpdate(sql);
			cnct.commit();
			log.debug("deleteEmployeeKintai end");
			return true;
		} else {
			log.error("データが存在しない、もしくは削除が正常に終了しませんでした。");
			return false;
		}
	}
	
	/**
	 * 従業員の工数情報を削除するメソッド
	 * @param num　従業員番号
	 * @return　TRUE: 削除が正常終了した場合<br>FALSE: データが存在しないもしくは、削除が正常終了しなかった場合
	 * @throws SQLException　データベース処理で問題が発生した場合
	 */
	public boolean deleteEmpManHour(Integer num) throws SQLException {
		log.debug("deleteEmpManHour start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL
		String sql = "select registration_date, man_hour_name1, man_hour_time1, man_hour_name2, man_hour_time2, man_hour_name3,"
				+ " man_hour_time3, man_hour_name4, man_hour_time4, man_hour_name5, man_hour_time5, man_hour_name6, man_hour_time6,"
				+ " man_hour_name7, man_hour_time7, man_hour_name8, man_hour_time8, man_hour_name9, man_hour_time9, man_hour_name10,"
				+ " man_hour_time10 from emp_man_hour where emp_num = '" + num + "';";
		rs = st.executeQuery(sql);
		//データが取得できた場合は、該当データを削除
		if(rs.next()) {
			sql = "delete from emp_man_hour where emp_num = '" + num + "'";
			st.executeUpdate(sql);
			cnct.commit();
			log.debug("deleteEmpManHour end");
			return true;
		} else {
			log.error("データが存在しない、もしくは削除が正常に終了しませんでした。");
			return false;
		}
	}
	
	/**
	 * 従業員情報を削除するメソッド
	 * @param num　従業員番号
	 * @return　TRUE: 削除が正常終了した場合<br>FALSE: データが存在しないもしくは、削除が正常終了しなかった場合
	 * @throws SQLException　データベース処理で問題が発生した場合
	 */
	public boolean deleteEmployee(Integer num) throws SQLException {
		log.debug("deleteEmployee start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL
		String sql = "select emp_num, emp_name, emp_mail, emp_pass, authority from employee where emp_num = '" + num + "';";
		rs = st.executeQuery(sql);
		//データが取得できた場合は、該当データを削除
		if(rs.next()) {
			sql = "delete from employee where emp_num = '" + num + "'";
			st.executeUpdate(sql);
			cnct.commit();
			log.debug("deleteEmployee end");
			return true;
		} else {
			log.error("データが存在しない、もしくは削除が正常に終了しませんでした。");
			return false;
		}
	}
	
	/**
	 * 従業員有給休暇情報を削除するメソッド
	 * @param num　従業員番号
	 * @return　TRUE: 削除が正常終了した場合<br>FALSE: データが存在しないもしくは、削除が正常終了しなかった場合
	 * @throws SQLException　データベース処理で問題が発生した場合
	 */
	public boolean deletePaidHoliday(Integer num) throws SQLException {
		log.debug("deletePaidHoliday start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL
		String sql = "select digestion_days, undigestion_days, period_use from paid_holiday where emp_num = '" + num + "';";
		rs = st.executeQuery(sql);
		//データが取得できた場合は、該当データを削除
		if(rs.next()) {
			sql = "delete from paid_holiday where emp_num = '" + num + "'";
			st.executeUpdate(sql);
			cnct.commit();
			log.debug("deletePaidHoliday end");
			return true;
		} else {
			log.error("データが存在しない、もしくは削除が正常に終了しませんでした。");
			return false;
		}
	}
}
