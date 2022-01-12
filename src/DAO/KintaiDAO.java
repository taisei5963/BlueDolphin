package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Bean.KinmuAllTimeBean;
import Bean.KinmuTimeBean;
import Bean.KintaiUpdateBean;

/**
 * 社員勤怠情報データベースと接続するためのDAOクラス
 *
 */
public class KintaiDAO{
	
	//唯一のインスタンス生成
	private static KintaiDAO only_instance = new KintaiDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(KintaiDAO.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String DBWORDING = "Data acquisition failure";
	
	//日付・時刻オブジェクトの出力および解析用のフォーマッタ
	//フォーマット→ 日付：「yyyy-MM-dd」時刻：「HH:mm:ss」
	DateTimeFormatter dateForm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter timeForm = DateTimeFormatter.ofPattern("HH:mm");
	
	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	private ResultSet rs;
	
	/**
	 * privateのため、新規のインスタンスを作らせない
	 */
	private KintaiDAO() {}
	
	/**
	 * 唯一のインスタンスを取得(=シングルトン)
	 * @return KintaiDAOの唯一のインスタンス
	 */
	public static KintaiDAO getInstance() {
<<<<<<< HEAD
=======
		log.info("getInstance start");
		log.info("getInstance end");
>>>>>>> 80cffd47c5175d81e090e931f673087ded55d4bc
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
	 * 出勤時間をデータベースに登録するメソッド
	 * @param email 社用メールアドレス
	 * @return データベースに出社時刻が登録できたら「true」、登録できなかったら「false」
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public boolean setSyukkinTime(Integer emp_num) throws SQLException {
		log.info("setSyussyaTime start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		//日付・時刻の取得
		LocalDateTime now = LocalDateTime.now();
		
		//すでにその日のデータが追加されていたら「false」を返却
		String sql = "select * from emp_kintai where emp_num='" + emp_num + "' and emp_workday = '" + now.format(dateForm) + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		if(rs.next()) {
			return false;
		} else {
			sql = "insert into emp_kintai (emp_num, emp_workday, commuting_time) values ('" + emp_num 
					+ "', '" + now.format(dateForm) + "', '" 
					+ now.format(timeForm) + "');";
			st.executeUpdate(sql);
			cnct.commit();
			log.info("setSyussyaTime end");
			return true;
		}
	}
	
	/**
	 * 退勤時間をデータベースに登録するメソッド
	 * @param email 社用メールアドレス
	 * @return データベースに退勤時間が更新できたら「true」、更新できなかったら「false」
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public boolean setTaikinTime(Integer emp_num) throws SQLException {
		log.info("setTaikinTime start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//日付・時刻の取得
		LocalDateTime now = LocalDateTime.now();
		
		String sql = "select * from emp_kintai where emp_num = '" + emp_num 
				+ "' and emp_workday = '" + now.format(dateForm) + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		//出勤が登録されていない場合は、「false」を返却する
		if(!rs.next()) {
			return false;
		//出勤が登録されている場合は、退勤時刻をデータベースに登録し、「true」を返却する
		} else {
			sql = "update emp_kintai set leave_time = '" + now.format(timeForm) 
				+ "' where emp_num = '" + emp_num + "' and emp_workday = '" + now.format(dateForm) + "';";
			st.executeUpdate(sql);
			cnct.commit();
			log.info("setTaikinTime end");
			return true;
		}
	}
	
	/**
	 * データベースから出退勤時刻情報をそれぞれ取得するメソッド
	 * @param emp_num 社員番号
	 * @return kinmuTimeList 出退勤時刻情報を格納したリスト
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public List<KinmuTimeBean> getWorkTime(Integer emp_num) throws SQLException{
		log.info("getWorkTime start");
		
		//格納用リストのインスタンス生成
		List<KinmuTimeBean> kinmuTimeList = new ArrayList<KinmuTimeBean>();
		//日付・時刻の取得
		LocalDateTime now = LocalDateTime.now();
		
		String sql = "select commuting_time, leave_time from emp_kintai where emp_num = '" + emp_num 
				+ "' and emp_workday = '" + now.format(dateForm) + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		if(rs.next()) {
			//取得データをセットするためのBeanクラスのインスタンス生成
			KinmuTimeBean ktBean = new KinmuTimeBean();
			ktBean.setWorkStartTime(rs.getString("commuting_time"));
			ktBean.setWorkEndTime(rs.getString("leave_time"));
			kinmuTimeList.add(ktBean);
			log.info("getWorkTime end");
			return kinmuTimeList;
		} else {
			log.error(DBWORDING);
			return null;
		}
	}
	
	/**
	 * データベースから出退勤時刻、実動時間、残業時間情報をそれぞれ取得するメソッド
	 * @param emp_num 社員番号
	 * @return kinmuTimeList 出退勤時刻、実動時間、残業時間情報を格納したリスト
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public ArrayList<KinmuAllTimeBean> getAllWorkTime(Integer emp_num, String thisMonth) throws SQLException{
		log.info("getWorkTime start");
		log.info("thisMonth : {}", thisMonth);
		
		//格納用リストのインスタンス生成
		ArrayList<KinmuAllTimeBean> kinmuTimeList = new ArrayList<KinmuAllTimeBean>();
		
		String sql = "select emp_workday, commuting_time, leave_time, actual_hours, overtime_hours, remarks "
				+ "from emp_kintai where emp_num = '" + emp_num +
				"' and emp_workday like '" + thisMonth + "%';";
<<<<<<< HEAD
=======
		log.debug("sql : {}", sql);
>>>>>>> 80cffd47c5175d81e090e931f673087ded55d4bc
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		while(rs.next()) {
			//取得データをセットするためのBeanクラスのインスタンス生成
			KinmuAllTimeBean ktBean = new KinmuAllTimeBean();
			ktBean.setWorkDate(LocalDate.parse(rs.getString("emp_workday"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			if(rs.getString("commuting_time") != null) {
				ktBean.setStartTime(LocalTime.parse(rs.getString("commuting_time"), DateTimeFormatter.ofPattern("HH:mm")));
			}
			if(rs.getString("leave_time") != null) {
				ktBean.setEndTime(LocalTime.parse(rs.getString("leave_time"), DateTimeFormatter.ofPattern("HH:mm")));
			} else if(rs.getString("leave_time") == null) {
				ktBean.setEndTime(null);
			}
			if(rs.getString("actual_hours") != null) {
				ktBean.setActualTime(LocalTime.parse(rs.getString("actual_hours"), DateTimeFormatter.ofPattern("HH:mm")));
			} else if(rs.getString("actual_hours") == null) {
				ktBean.setActualTime(null);
			}
			if(rs.getString("overtime_hours") != null) {
				ktBean.setZangyoTime(LocalTime.parse(rs.getString("overtime_hours"), DateTimeFormatter.ofPattern("HH:mm")));
			} else if(rs.getString("overtime_hours") == null) {
				ktBean.setZangyoTime(null);
			}
			ktBean.setRemarks(rs.getString("remarks"));
			kinmuTimeList.add(ktBean);
			log.info("getWorkTime end");
		}
		
		return kinmuTimeList;
	}
	
	/**
	 * 稼働時間をデータベースに登録するメソッド
	 * @param email 社用メールアドレス
	 * @param actual 稼働時間
	 * @return データベースに稼働時間が更新できたら「true」、更新できなかったら「false」を返却
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public boolean setActualTime(Integer emp_num, String actual) throws SQLException {
		log.info("setActualTime start");
		log.info("actual:{}", actual);
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//日付・時刻の取得
		LocalDateTime now = LocalDateTime.now();
		
		String sql = "select * from emp_kintai where emp_num = '" + emp_num 
				+ "' and emp_workday = '" + now.format(dateForm) + "';";
		log.debug("sql:{}", sql);
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		//出勤・退勤が登録されていない場合は、「false」を返却する
		if(!rs.next()) {
			return false;
		//出勤が登録されている場合は、退勤時刻をデータベースに登録し、「true」を返却する
		} else {
			sql = "update emp_kintai set actual_hours = '" + actual 
				+ "' where emp_num = '" + emp_num + "' and emp_workday = '" + now.format(dateForm) + "';";
			log.debug("sql:{}", sql);
			st.executeUpdate(sql);
			cnct.commit();
			log.info("setActualTime end");
			return true;
		}
	}
	
	/**
	 * 残業時間をデータベースに登録するメソッド
	 * @param email 社用メールアドレス
	 * @param zangyo 残業時間
	 * @return データベースに残業時間が更新できたら「true」、更新できなかったら「false」を返却
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public boolean setZangyoTime(Integer emp_num, String zangyo) throws SQLException {
		log.info("setZangyoTime start");
		log.info("zangyo:{}", zangyo);
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//日付・時刻の取得
		LocalDateTime now = LocalDateTime.now();
		
		String sql = "select * from emp_kintai where emp_num = '" + emp_num 
				+ "' and emp_workday = '" + now.format(dateForm) + "';";
		log.debug("sql:{}", sql);
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		//出勤・退勤が登録されていない場合は、「false」を返却する
		if(!rs.next()) {
			return false;
		//出勤が登録されている場合は、退勤時刻をデータベースに登録し、「true」を返却する
		} else {
			sql = "update emp_kintai set overtime_hours = '" + zangyo 
				+ "' where emp_num = '" + emp_num + "' and emp_workday = '" + now.format(dateForm) + "';";
			log.debug("sql:{}", sql);
			st.executeUpdate(sql);
			cnct.commit();
			log.info("setZangyoTime end");
			return true;
		}
	}
	
	/**
	 * データベースから更新対象情報を取得するメソッド
	 * @param emp_num 社員番号
	 * @param syukkin 出勤時間
	 * @param taikin 退勤時間
	 * @param thisMonth 取得する月
	 * @return 更新情報を格納したリスト
	 * @throws SQLException
	 */
	public ArrayList<KintaiUpdateBean> selectWorkingTime(int emp_num, String thisMonth) throws SQLException{
		log.info("selectWorkingTime start");
		log.info("thisMonth : {}", thisMonth);
		
		ArrayList<KintaiUpdateBean> syuttaikinList = new ArrayList<KintaiUpdateBean>();
		String sql = null;
		
		sql = "select emp_workday, commuting_time, leave_time from emp_kintai where emp_num = '" + emp_num 
				+ "' and emp_workday = '" + thisMonth + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		if(rs.next()) {
			KintaiUpdateBean kub = new KintaiUpdateBean();
			kub.setDate(rs.getString("emp_workday"));
			kub.setSyukkin(rs.getString("commuting_time"));
			kub.setTaikin(rs.getString("leave_time"));
			syuttaikinList.add(kub);
			log.info("selectWorkingTime end");
			return syuttaikinList;
		} else {
			log.error(DBWORDING);
			return null;
		}
	}
	
	/**
	 * データベースの勤怠情報を勤怠更新情報に更新するメソッド
	 * @param emp_num 社員番号
	 * @param syukkin 出勤時間
	 * @param taikin 退勤時間
	 * @param kin_date 出勤日
	 * @param actual 実動時間
	 * @param zangyo 残業時間
	 * @param remarks 備考
	 * @return 更新が正常に行えた場合は「true」を返却し、それ以外の場合は「false」を返却する
	 * @throws SQLException
	 */
	public boolean WorkingTimeUpdate(int emp_num, String syukkin, String taikin, String kin_date, 
			String actual, String zangyo, String remarks) throws SQLException {
		log.info("WorkingTimeUpdate start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		String sql = "select * from emp_kintai where emp_num = '" + emp_num + "' and commuting_time = '" + syukkin 
				+ "' and leave_time = '" + taikin + "' and emp_workday = '" + kin_date + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		//データが取得できた場合は、「false」を返却する
		if(rs.next()) {
			return false;
		//出勤が登録されている場合は、退勤時刻をデータベースに登録し、「true」を返却する
		} else {
			sql = "select commuting_time, leave_time from emp_kintai where emp_num = '" + emp_num + "' and emp_workday = '" + kin_date + "';";
			rs = st.executeQuery(sql);
			log.debug("rs:{}", rs);
			//出勤時間または、退勤時間が取得できる場合
			if(rs.next()) {
				sql = "update emp_kintai set commuting_time = '" + syukkin + "', leave_time = '" + taikin 
						+ "', actual_hours = '" + actual + "', overtime_hours = '" + zangyo + "', remarks = '" + remarks 
						+ "' where emp_num = '" + emp_num + "' and emp_workday = '" + kin_date + "';"; 
				st.executeUpdate(sql);
				cnct.commit();
				log.info("WorkingTimeUpdate end");
			//出勤時間、退勤時間ともに取得できなかった場合
			} else {
				sql = "insert into emp_kintai (emp_num, emp_workday, commuting_time, leave_time, actual_hours, "
						+ "overtime_hours, remarks) values ('" 
						+ emp_num + "', '" + kin_date + "', '" + syukkin + "', '" + taikin 
						+ "', '" + actual + "', '" + zangyo + "', '" + remarks + "');";
				st.executeUpdate(sql);
				cnct.commit();
				log.info("WorkingTimeUpdate end");
			}
			return true;
		}
	}
	
	/**
	 * 各従業員の標準時間を取得するメソッド
	 * @param emp_num　社員番号
	 * @return standard_time 標準時間
	 * @throws SQLException　DB処理に問題が発生した場合に発生
	 */
	public String getStandardTime(Integer emp_num) throws SQLException {
		log.info("getStandardTime start");
		
		//取得結果格納用変数宣言（nullで初期化）
		String standard_time = null;
		
		//SQL文
		String sql = "select standard_time from contract_time where emp_num = '" + emp_num + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		
		//情報が取得できた場合は、変数に格納して呼び出し元に取得値を戻す
		if(rs.next()) {
			standard_time = rs.getString("standard_time");
		}
		log.info("getStandardTime end");
		return standard_time;
	}
}
