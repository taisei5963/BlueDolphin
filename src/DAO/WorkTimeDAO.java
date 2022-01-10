package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * 社員勤怠情報データベースと接続するためのDAOクラス
 *
 */
public class WorkTimeDAO{
	
	//唯一のインスタンス生成
	private static WorkTimeDAO only_instance = new WorkTimeDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(WorkTimeDAO.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String DBWORDING = "Already exists";
	
	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	private ResultSet rs;
	
	/**
	 * privateのため、新規のインスタンスを作らせない
	 */
	private WorkTimeDAO() {
	}
	
	/**
	 * 唯一のインスタンスを取得(=シングルトン)
	 * @return KintaiDAOの唯一のインスタンス
	 */
	public static WorkTimeDAO getInstance() {
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
	 * 出勤時間情報がすでに存在しているかを判断するメソッド
	 * @param email 社用メールアドレス
	 * @return 出勤時間情報がすでに存在していたら「invalid」、存在していないなら「null」を返却
	 * @throws SQLException データベース処理で問題が発生した場合
	 */
	public String selectSyukkinTime(Integer emp_num) throws SQLException {
		log.info("selectSyukkinTime start");
		String sql = "select * from emp_kintai where emp_num = '" + emp_num + "' and emp_workday = '" + LocalDate.now() + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		if(rs.next()) {
			log.error(DBWORDING);
			return "invalid";
		} else {
			log.info("selectSyukkinTime end");
			return null;
		}
	}
	
	/**
	 * 退勤時刻情報がすでに存在するかを判断するメソッド
	 * @param email 社用メールアドレス
	 * @return 退勤時刻情報がすでに存在する場合は「invalid」、存在しない場合は「null」を返却する
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public String selectTaikinTime(Integer emp_num) throws SQLException {
		log.info("selectTaikinTime start");
		String sql = "select * from emp_kintai where emp_num = '" + emp_num + "' and emp_workday = '" + LocalDate.now() + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		if(rs.next() && rs.getString("leave_time") != null) {
			log.error(DBWORDING);
			return "invalid";
		} else {
			log.info("selectTaikinTime end");
			return null;
		}
	}
}
