package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Bean.ApprovalApplicationBean;
import Bean.ApprovalApplicationDetailsBean;
import Other.DiffDateCalculation;

/**
 * 承認申請テーブルに接続するためのDAOクラス 
 *
 */
public class ApprovalApplicationDAO{
	
	//唯一のインスタンス生成
	private static ApprovalApplicationDAO only_instance = new ApprovalApplicationDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(ApprovalApplicationDAO.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String APPLICATIONSTATUSOK = "承認済み";
	private static final String APPLICATIONSTATUSOUT = "承認却下";
	
	//アラートダイアログ用定数宣言
	private static final String ALERT_TITLE = "申請可能日数オーバー";
	private static final String ALERT_CONTENT = "申請可能日数を超えての申請はできません。";
	
	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	private ResultSet rs;
	
	/**
	 * privateのため、新規のインスタンスを作らせない
	 */
	private ApprovalApplicationDAO() {
	}
	
	/**
	 * 唯一のインスタンスを取得(=シングルトン)
	 * @return KintaiDAOの唯一のインスタンス
	 */
	public static ApprovalApplicationDAO getInstance() {
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
	 * 申請内容をデータベースに登録するメソッド
	 * @param applicant　申請者
	 * @param application_number　申請番号
	 * @param application_date　申請日
	 * @param application_time　申請時間
	 * @param authorizer　承認者
	 * @param application_status　承認状況
	 * @return 同じ申請番号が存在する場合は、FALSEを返却<br>同じ申請番号が存在していない場合は、TRUEを返却
	 * @throws SQLException データベース接続に問題が発生した場合
	 */
	public boolean setApprovalApplication(String applicant, String application_title, Integer application_number, String application_date,
			String application_time, String authorizer, String application_status) throws SQLException {
		log.info("setApprovalApplication start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL文
		String sql = "select application_number from approval_application where application_number = '" 
		+ application_number + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		//登録しようとしている申請番号が、すでに存在している場合は、FALSEを返却
		if(rs.next()) {
			return false;
		} else {
			sql = "insert into approval_application (applicant, application_title, application_number, application_date, "
					+ "application_time, authorizer, application_status) values ('" + applicant + "', '" + application_title 
					+ "', '" + application_number + "', '" + application_date + "', '" + application_time 
					+ "', '" + authorizer + "', '" + application_status + "');";
			st.executeUpdate(sql);
			cnct.commit();
			log.info("setApprovalApplication end");
			return true;
		}
	}
	
	/**
	 * 承認状況情報を更新するメソッド（承認済みの場合）
	 * @param application_number 更新対象の申請番号
	 * @param approval_date 
	 * @param approval_time
	 * @throws SQLException
	 */
	public boolean updateApprovalApplicationStatusOK(Integer application_number, String approval_date, String approval_time) 
			throws SQLException {
		log.info("updateApprovalApplicationStatusOK start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL文
		String sql = "select application_status from approval_application where "
				+ "application_number = '" + application_number + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		if(rs.next()) {
			//すでに承認済みである場合は、更新処理を行わない
			if(APPLICATIONSTATUSOK.equals(rs.getString("application_status"))) {
				return false;
			} else {
				//SQL文
				sql = "update approval_application set approval_date = '" + approval_date + "', approval_time = '" 
				+ approval_time + "', application_status = '" + APPLICATIONSTATUSOK 
				+ "' where application_number = '" + application_number + "';";
				
				st.executeUpdate(sql);
				cnct.commit();
				
				log.info("updateApprovalApplicationStatusOK end");
				return true;
			}
		} else {
			log.error("NOT MATCH DATA.");
			return false;
		}
	}
	
	/**
	 * 承認状況情報を更新するメソッド（申請却下の場合）
	 * @param application_number 更新対象の申請番号
	 * @param approval_date 
	 * @param approval_time
	 * @throws SQLException
	 */
	public boolean updateApprovalApplicationStatusOUT(Integer application_number, String approval_date, String approval_time) 
			throws SQLException {
		log.info("updateApprovalApplicationStatusOUT start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL文
		String sql = "select application_status from approval_application where "
				+ "application_number = '" + application_number + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		if(rs.next()) {
			//すでに承認済みである場合は、更新処理を行わない
			if(APPLICATIONSTATUSOK.equals(rs.getString("application_status"))) {
				return false;
			} else {
				//SQL文
				sql = "update approval_application set approval_date = '" + approval_date + "', approval_time = '" 
				+ approval_time + "', application_status = '" + APPLICATIONSTATUSOUT 
				+ "' where application_number = '" + application_number + "';";
				
				st.executeUpdate(sql);
				cnct.commit();
				
				log.info("updateApprovalApplicationStatusOUT end");
				return true;
			}
		} else {
			log.error("NOT MATCH DATA.");
			return false;
		}
	}
	
	/**
	 * 申請情報を取得し、リスト化するメソッド
	 * @param authorizer　承認者
	 * @return　承認者が担当する申請内容リスト
	 * @throws SQLException　データベース接続に問題が発生した場合
	 */
	public ArrayList<ApprovalApplicationBean> getApprovalApplication(String authorizer) throws SQLException{
		log.info("getApprovalApplication start");
		
		ArrayList<ApprovalApplicationBean> appList = new ArrayList<ApprovalApplicationBean>();
		
		//SQL文
		String sql = "select applicant, application_title, application_number, application_date, application_time, approval_date, "
				+ "approval_time, application_status from approval_application where authorizer = '" + authorizer + "';";
		
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		while(rs.next()) {
			ApprovalApplicationBean aab = new ApprovalApplicationBean();
			aab.setApplicant(rs.getString("applicant"));
			aab.setApplication_title(rs.getString("application_title"));
			aab.setApplication_number(rs.getInt("application_number"));
			aab.setApplication_date(rs.getString("application_date"));
			aab.setApplication_time(rs.getString("application_time"));
			aab.setApproval_date(rs.getString("approval_date"));
			aab.setApproval_time(rs.getString("approval_time"));
			aab.setApplication_status(rs.getString("application_status"));
			appList.add(aab);
		}
		
		return appList;
	}
	
	/**
	 * 承認申請詳細情報をデータベースに登録を行うメソッド
	 * @param application_number 申請番号
	 * @param application_name　申請名
	 * @param application_date　申請日
	 * @param application_time_1　休暇開始時刻（特別時間休暇選択時に使用）
	 * @param application_time_2　休暇終了時刻（特別時間休暇選択時に使用）
	 * @param all_actual_hours　総稼働時間（勤怠申請時に使用）
	 * @param all_overtime_hours　総残業時間（勤怠申請時に使用）
	 * @param remarks　備考
	 * @throws SQLException　データベース接続に問題が発生した場合
	 */
	public void setApprovalApplicationDetails(Integer application_number, String application_name, String classification,
			String application_date, String application_time_1, String application_time_2, 
			String all_actual_hours, String all_overtime_hours, String remarks, String rejection_reason) throws SQLException {
		log.info("setApprovalApplicationDetails start");
		
		//文字列から曜日部分を削除
		application_date = application_date.replaceAll("\\（.*?\\）", "");
		
		String [] dates = application_date.split("、");
		
		String date1 = "-", date2 = "-", date3 = "-", date4 = "-", date5 = "-", 
				date6 = "-", date7 = "-", date8 = "-", date9 = "-", date10 = "-";
		
		switch(dates.length) {
		case 0:
			break;
		case 1:
			date1 =  application_date.substring(0, 4) + "-" + application_date.substring(5, 7) + "-" + application_date.substring(8, 10);
			break;
		case 2:
			date1 =  application_date.substring(0, 4) + "-" + application_date.substring(5, 7) + "-" + application_date.substring(8, 10);
			date2 =  application_date.substring(11, 15) + "-" + application_date.substring(16, 18) + "-" + application_date.substring(19, 21);
			break;
		case 3:
			date1 =  application_date.substring(0, 4) + "-" + application_date.substring(5, 7) + "-" + application_date.substring(8, 10);
			date2 =  application_date.substring(11, 15) + "-" + application_date.substring(16, 18) + "-" + application_date.substring(19, 21);
			date3 =  application_date.substring(22, 26) + "-" + application_date.substring(27, 29) + "-" + application_date.substring(30, 32);
			break;
		case 4:
			date1 =  application_date.substring(0, 4) + "-" + application_date.substring(5, 7) + "-" + application_date.substring(8, 10);
			date2 =  application_date.substring(11, 15) + "-" + application_date.substring(16, 18) + "-" + application_date.substring(19, 21);
			date3 =  application_date.substring(22, 26) + "-" + application_date.substring(27, 29) + "-" + application_date.substring(30, 32);
			date4 =  application_date.substring(33, 37) + "-" + application_date.substring(38, 40) + "-" + application_date.substring(41, 43);
			break;
		case 5:
			date1 =  application_date.substring(0, 4) + "-" + application_date.substring(5, 7) + "-" + application_date.substring(8, 10);
			date2 =  application_date.substring(11, 15) + "-" + application_date.substring(16, 18) + "-" + application_date.substring(19, 21);
			date3 =  application_date.substring(22, 26) + "-" + application_date.substring(27, 29) + "-" + application_date.substring(30, 32);
			date4 =  application_date.substring(33, 37) + "-" + application_date.substring(38, 40) + "-" + application_date.substring(41, 43);
			date5 =  application_date.substring(44, 48) + "-" + application_date.substring(49, 51) + "-" + application_date.substring(52, 54);
			break;
		case 6:
			date1 =  application_date.substring(0, 4) + "-" + application_date.substring(5, 7) + "-" + application_date.substring(8, 10);
			date2 =  application_date.substring(11, 15) + "-" + application_date.substring(16, 18) + "-" + application_date.substring(19, 21);
			date3 =  application_date.substring(22, 26) + "-" + application_date.substring(27, 29) + "-" + application_date.substring(30, 32);
			date4 =  application_date.substring(33, 37) + "-" + application_date.substring(38, 40) + "-" + application_date.substring(41, 43);
			date5 =  application_date.substring(44, 48) + "-" + application_date.substring(49, 51) + "-" + application_date.substring(52, 54);
			date6 =  application_date.substring(55, 59) + "-" + application_date.substring(60, 62) + "-" + application_date.substring(63, 65);
			break;
		case 7:
			date1 =  application_date.substring(0, 4) + "-" + application_date.substring(5, 7) + "-" + application_date.substring(8, 10);
			date2 =  application_date.substring(11, 15) + "-" + application_date.substring(16, 18) + "-" + application_date.substring(19, 21);
			date3 =  application_date.substring(22, 26) + "-" + application_date.substring(27, 29) + "-" + application_date.substring(30, 32);
			date4 =  application_date.substring(33, 37) + "-" + application_date.substring(38, 40) + "-" + application_date.substring(41, 43);
			date5 =  application_date.substring(44, 48) + "-" + application_date.substring(49, 51) + "-" + application_date.substring(52, 54);
			date6 =  application_date.substring(55, 59) + "-" + application_date.substring(60, 62) + "-" + application_date.substring(63, 65);
			date7 =  application_date.substring(66, 70) + "-" + application_date.substring(71, 73) + "-" + application_date.substring(74, 76);
			break;
		case 8:
			date1 =  application_date.substring(0, 4) + "-" + application_date.substring(5, 7) + "-" + application_date.substring(8, 10);
			date2 =  application_date.substring(11, 15) + "-" + application_date.substring(16, 18) + "-" + application_date.substring(19, 21);
			date3 =  application_date.substring(22, 26) + "-" + application_date.substring(27, 29) + "-" + application_date.substring(30, 32);
			date4 =  application_date.substring(33, 37) + "-" + application_date.substring(38, 40) + "-" + application_date.substring(41, 43);
			date5 =  application_date.substring(44, 48) + "-" + application_date.substring(49, 51) + "-" + application_date.substring(52, 54);
			date6 =  application_date.substring(55, 59) + "-" + application_date.substring(60, 62) + "-" + application_date.substring(63, 65);
			date7 =  application_date.substring(66, 70) + "-" + application_date.substring(71, 73) + "-" + application_date.substring(74, 76);
			date8 =  application_date.substring(77, 81) + "-" + application_date.substring(82, 84) + "-" + application_date.substring(85, 87);
			break;
		case 9:
			date1 =  application_date.substring(0, 4) + "-" + application_date.substring(5, 7) + "-" + application_date.substring(8, 10);
			date2 =  application_date.substring(11, 15) + "-" + application_date.substring(16, 18) + "-" + application_date.substring(19, 21);
			date3 =  application_date.substring(22, 26) + "-" + application_date.substring(27, 29) + "-" + application_date.substring(30, 32);
			date4 =  application_date.substring(33, 37) + "-" + application_date.substring(38, 40) + "-" + application_date.substring(41, 43);
			date5 =  application_date.substring(44, 48) + "-" + application_date.substring(49, 51) + "-" + application_date.substring(52, 54);
			date6 =  application_date.substring(55, 59) + "-" + application_date.substring(60, 62) + "-" + application_date.substring(63, 65);
			date7 =  application_date.substring(66, 70) + "-" + application_date.substring(71, 73) + "-" + application_date.substring(74, 76);
			date8 =  application_date.substring(77, 81) + "-" + application_date.substring(82, 84) + "-" + application_date.substring(85, 87);
			date9 =  application_date.substring(88, 92) + "-" + application_date.substring(93, 95) + "-" + application_date.substring(96, 98);
			break;
		case 10:
			date1 =  application_date.substring(0, 4) + "-" + application_date.substring(5, 7) + "-" + application_date.substring(8, 10);
			date2 =  application_date.substring(11, 15) + "-" + application_date.substring(16, 18) + "-" + application_date.substring(19, 21);
			date3 =  application_date.substring(22, 26) + "-" + application_date.substring(27, 29) + "-" + application_date.substring(30, 32);
			date4 =  application_date.substring(33, 37) + "-" + application_date.substring(38, 40) + "-" + application_date.substring(41, 43);
			date5 =  application_date.substring(44, 48) + "-" + application_date.substring(49, 51) + "-" + application_date.substring(52, 54);
			date6 =  application_date.substring(55, 59) + "-" + application_date.substring(60, 62) + "-" + application_date.substring(63, 65);
			date7 =  application_date.substring(66, 70) + "-" + application_date.substring(71, 73) + "-" + application_date.substring(74, 76);
			date8 =  application_date.substring(77, 81) + "-" + application_date.substring(82, 84) + "-" + application_date.substring(85, 87);
			date9 =  application_date.substring(88, 92) + "-" + application_date.substring(93, 95) + "-" + application_date.substring(96, 98);
			date10 = application_date.substring(99, 103) + "-" + application_date.substring(104, 106) + "-" + application_date.substring(107, 109);
			break;
		default:
			break;
		}
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL文
		String sql = "insert into approval_application_details values ('" + application_number + "', '" 
				+ application_name + "', '" + classification + "', '" + date1 + "', '" + date2 + "', '" + date3 + "', '" + date4 + "', '" 
				+ date5 + "', '" + date6 + "', '" + date7 + "', '" + date8 + "', '" + date9 + "', '" + date10 + "', '" 
				+ application_time_1 + "', '" + application_time_2 + "', '" 
				+ all_actual_hours + "', '" + all_overtime_hours + "', '" + remarks + "', '" + rejection_reason + "');";
		
		st.executeUpdate(sql);
		cnct.commit();
		
		log.info("setApprovalApplicationDetails end");
	}
	
	/**
	 * 承認申請詳細情報を取得し、リスト化するメソッド
	 * @param application_number 申請番号
	 * @return リスト化した承認申請詳細情報
	 * @throws SQLException　データベース接続に問題が発生した場合
	 */
	public ArrayList<ApprovalApplicationDetailsBean> getApprovalApplicationDetails(Integer application_number) throws SQLException {
		log.info("getApprovalApplicationDetails start");
		
		//取得結果格納用リスト変数宣言
		ArrayList<ApprovalApplicationDetailsBean> detailist = new ArrayList<ApprovalApplicationDetailsBean>();
		
		//SQL文
		String sql = "select application_number, application_name, classification, "
				+ "application_date_1, application_date_2, application_date_3, application_date_4, application_date_5, "
				+ "application_date_6, application_date_7, application_date_8, application_date_9, application_date_10, "
				+ "all_actual_hours, all_overtime_hours, remarks, rejection_reason from approval_application_details "
				+ "where application_number = '" + application_number + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		//該当情報の取得
		if(rs.next()) {
			ApprovalApplicationDetailsBean adb = new ApprovalApplicationDetailsBean();
			adb.setApplicationNumber(rs.getInt("application_number"));
			adb.setApplicationName(rs.getString("application_name"));
			adb.setClassification(rs.getString("classification"));
			adb.setDate1(rs.getString("application_date_1"));
			adb.setDate2(rs.getString("application_date_2"));
			adb.setDate3(rs.getString("application_date_3"));
			adb.setDate4(rs.getString("application_date_4"));
			adb.setDate5(rs.getString("application_date_5"));
			adb.setDate6(rs.getString("application_date_6"));
			adb.setDate7(rs.getString("application_date_7"));
			adb.setDate8(rs.getString("application_date_8"));
			adb.setDate9(rs.getString("application_date_9"));
			adb.setDate10(rs.getString("application_date_10"));
			adb.setAllActualHours(rs.getString("all_actual_hours"));
			adb.setAllOvertimeHours(rs.getString("all_overtime_hours"));
			adb.setRemarks(rs.getString("remarks"));
			adb.setRejectionReason(rs.getString("rejection_reason"));
			detailist.add(adb);
		}
		
		log.info("getApprovalApplicationDetails end");
		return detailist;
	}
	
	/**
	 * 承認却下理由を登録するメソッド
	 * @param application_number 申請番号
	 * @param rejection_reason 承認却下理由
	 * @return 登録が正常に完了した場合は、TRUEを返却<br>登録が正常に完了できなかった場合は、FALSEを返却
	 * @throws SQLException データベース接続に問題が発生した場合
	 */
	public boolean updateApprovalApplicationDetails(Integer application_number, String rejection_reason) throws SQLException{
		log.debug("updateApprovalApplicationDetails start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL文
		String sql = "update approval_application_details set rejection_reason = '【申請却下理由】" + rejection_reason 
				+ "' where application_number = '" + application_number + "';";
		
		st.executeUpdate(sql);
		cnct.commit();
		log.debug("updateApprovalApplicationDetails end");
		return true;
	}
	
	/**
	 * 有給休暇の使用可能日数及び使用済み日数の更新を行うメソッド
	 * @param fromDate 申請開始日
	 * @param toDate　申請終了日
	 * @param emp_num　更新を行う従業員番号
	 * @return　更新が正常に行われた場合は、TRUEを返却<br>更新が正常に行われなかった場合は、FALSEを返却
	 * @throws SQLException データベース接続に問題が発生した場合
	 */
	public boolean updatePaidHolidayDays(String fromDate, String toDate, int emp_num) throws SQLException {
		log.info("updatePaidHolidayDays start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		DiffDateCalculation diff = DiffDateCalculation.getInstance();
		
		//未消化日数を取得
		String sql = "select undigestion_days from paid_holiday where emp_num = '" + emp_num + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		if(rs.next()) {
			int max_days = rs.getInt("undigestion_days");
			int diff_days = 0;
			int days = diff.diffDate(fromDate, toDate);
			if(days > max_days) {
				JOptionPane.showMessageDialog(null, ALERT_CONTENT, ALERT_TITLE, JOptionPane.WARNING_MESSAGE);
				return false;
			} else {
				if(days == 0) {
					diff_days = 1;
				} else {
					diff_days = max_days - days;
				}
				sql = "update paid_holiday set digestion_days = '" + days + "', undigestion_days = '" + diff_days + "' where emp_num = '" + emp_num + "';";
				st.executeUpdate(sql);
				cnct.commit();
				return true;
			}
		} else {
			return false;
		}
	}
}
