package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * 休暇申請情報データベースと接続するためのDAOクラス
 *
 */

import Bean.PaidHolidayBean;
import Bean.VacationRequestBean;
public class VacationRequestDAO{
	
	//唯一のインスタンス生成
	private static VacationRequestDAO only_instance = new VacationRequestDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(VacationRequestDAO.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	private ResultSet rs;
	
	/**
	 * privateのため、新規のインスタンスを作らせない
	 */
	private VacationRequestDAO() {
	}
	
	/**
	 * 唯一のインスタンスを取得(=シングルトン)
	 * @return KintaiDAOの唯一のインスタンス
	 */
	public static VacationRequestDAO getInstance() {
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
	 * 休暇申請における休暇名称を取得し、リスト化するメソッド
	 * @return 取得した休暇名称のリスト
	 * @throws SQLException データベース接続に問題が発生した場合
	 */
	public ArrayList<VacationRequestBean> getVacationRequest() throws SQLException{
		log.info("getVacationRequest start");
		
		//休暇申請情報格納用リスト変数宣言
		ArrayList<VacationRequestBean> nameList = new ArrayList<VacationRequestBean>();
		
		//SQL文
		String sql = "select vacation_name from vacation_request;";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		//取得した情報をリストに追加
		while(rs.next()) {
			VacationRequestBean vrb = new VacationRequestBean();
			vrb.setVacationName(rs.getString("vacation_name"));
			nameList.add(vrb);
		}
		
		return nameList;
	}
	
	/**
	 * 有給休暇情報を取得し、リスト化するメソッド
	 * @param emp_num 取得する対象の従業員番号
	 * @return　取得した従業員の有給休暇消化状況情報を格納したリスト
	 * @throws SQLException　データベース接続に問題が発生した場合
	 */
	public ArrayList<PaidHolidayBean> getPaidHolidayInfo(Integer emp_num) throws SQLException{
		log.info("getPaidHolidayInfo start");
		
		//有給休暇情報格納用リスト変数宣言
		ArrayList<PaidHolidayBean> phList = new ArrayList<PaidHolidayBean>();
		
		//SQL文
		String sql = "select digestion_days, undigestion_days, digestion_times, undigestion_times, period_use from paid_holiday where emp_num = '" + emp_num + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		//取得した情報をリストに追加
		if(rs.next()) {
			PaidHolidayBean phb = new PaidHolidayBean();
			phb.setDigestionDays(rs.getInt("digestion_days"));
			phb.setUndigestionDays(rs.getInt("undigestion_days"));
			phb.setDigestionTimes(rs.getDouble("digestion_times"));
			phb.setUndigestionTimes(rs.getDouble("undigestion_times"));
			phb.setPeriodUse(rs.getString("period_use"));
			phList.add(phb);
		} else {
			return null;
		}
		
		log.info("getPaidHolidayInfo end");
		return phList;
	}
}
