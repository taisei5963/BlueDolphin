package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Bean.ApplicationContentsBean;
import Bean.ManHourRequestBean;
import Bean.WorkPatternBean;

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
	private static final String APPLICATIONSTATUSOK = "承認";
	private static final String APPLICATIONSTATUSNO = "却下";
	
	//アラートダイアログ用定数宣言
	private static final String ALERT_TITLE = "申請可能日数オーバー";
	private static final String ALERT_CONTENT = "申請可能日数を超えての申請は、できません。";
	private static final String ALERT_TITLE1 = "特別時間休暇使用可能上限オーバー";
	private static final String ALERT_CONTENT1 = "5日を超えての特別時間休暇の申請は、できません。";
	
	//ステータス用定数宣言
	private static final String STATUSALL = "すべて";
	private static final String STATUSAPPLYING = "申請中";
	private static final String STATUSAPPROVAL = "承認";
	private static final String STATUSREJECTED = "却下";
	
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
	public boolean setApplicationContents(Integer application_number, String application_date, String applicant, String application_name, 
			String application_type, String application_category, String application_status, String target_date, 
			String application_reason, String acquisition_time, String holiday_acquisition_date, String total_uptime, 
			String total_overtime_hours, String correspondence_date, String corresponding_person, 
			String applicant_comment, String approver_comment, String work_contents, String contact, String working_hours, 
			String corrected_work_time, String corrected_retreat_time) throws SQLException {
		log.info("setApprovalApplication start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		String sql;
		
		try {
			//SQL文
			sql = "insert into application_contents (application_number, application_date, applicant, application_name, "
					+ "application_type, application_category, application_status, target_date, application_reason, "
					+ "acquisition_time, holiday_acquisition_date, total_uptime, total_overtime_hours, correspondence_date, "
					+ "corresponding_person, applicant_comment, approver_comment, work_contents, contact, working_hours, "
					+ "corrected_work_time, corrected_retreat_time) values ('" + application_number + "', '"  
					+ application_date + "', '" + applicant + "', '" + application_name + "', '" + application_type 
					+ "', '" + application_category + "', '" + application_status + "', '" + target_date + "', '" 
					+ application_reason + "', '" + acquisition_time + "', '" + holiday_acquisition_date + "', '" 
					+ total_uptime + "', '" + total_overtime_hours + "', '" + correspondence_date + "', '" 
					+ corresponding_person + "', '" + applicant_comment + "', '" + approver_comment + "', '" 
					+ work_contents + "', '" + contact + "', '" + working_hours + "', '" + corrected_work_time + "', '" 
					+ corrected_retreat_time + "');";
			st.executeUpdate(sql);
			cnct.commit();
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			cnct.rollback();
			e.printStackTrace();
			return false;
		} finally {}
		
		log.info("setApprovalApplication end");
		return true;
	}
	
	/**
	 * 申請内容情報の更新を行うメソッド
	 * @param application_number　申請番号
	 * @param application_status　申請状況
	 * @param correspondence_date　対応日
	 * @param corresponding_person　対応者
	 * @return　正常に更新が完了した場合はTRUEを返却<br>正常に更新が完了できなかった場合はFLASEを返却
	 * @throws SQLException データベース接続に問題が発生した場合
	 */
	public boolean updateApplicationContents(Integer application_number, String application_status, 
			String correspondence_date, String corresponding_person, String approver_comment) throws SQLException {
		log.info("updateApplicationContents start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL文
		String sql = "select application_status from application_contents where "
				+ "application_number = '" + application_number + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		if(rs.next()) {
			//申請状況が「申請中」以外の場合は、処理を行わない。
			if(APPLICATIONSTATUSOK.equals(rs.getString("application_status")) || 
					APPLICATIONSTATUSNO.equals(rs.getString("application_status"))) {
				return false;
			} else {
				if(APPLICATIONSTATUSOK.equals(application_status)) {
					//SQL文
					sql = "update application_contents set correspondence_date = '" + correspondence_date 
							+ "', corresponding_person = '" + corresponding_person + "', application_status = '" 
							+ application_status + "', approver_comment = '" + approver_comment 
							+ "' where application_number = '" + application_number + "';";
				} else if(APPLICATIONSTATUSNO.equals(application_status)) {
					//SQL文
					sql = "update application_contents set correspondence_date = '" + correspondence_date 
							+ "', corresponding_person = '" + corresponding_person + "', application_status = '" 
							+ application_status + "', approver_comment = '" + approver_comment 
							+ "' where application_number = '" + application_number + "';";
				} else {
					return false;
				}
				
				st.executeUpdate(sql);
				cnct.commit();
				
				log.info("updateApplicationContents end");
				return true;
			}
		} else {
			log.error("NOT MATCH DATA.");
			return false;
		}
	}
	
	/**
	 * 申請情報を取得し、リスト化するメソッド
	 * @param corresponding_person 対応者
	 * @param applicant　申請者
	 * @return　申請情報を格納したリスト
	 * @throws SQLException　データベース接続に問題が発生した場合
	 */
	public ArrayList<ApplicationContentsBean> getApplicationContents(String corresponding_person, String applicant) throws SQLException{
		log.info("getApplicationContents start");
		
		ArrayList<ApplicationContentsBean> appList = new ArrayList<ApplicationContentsBean>();
		String sql;
		
		//対応者がNULLではない場合は、対応者に適合する情報を取得
		if(corresponding_person != null && applicant == null) {
			//SQL文
			sql = "select application_number, application_date, applicant, application_name, application_type, "
					+ "application_category, application_status, target_date, application_reason, acquisition_time, "
					+ "holiday_acquisition_date, total_uptime, total_overtime_hours, correspondence_date, "
					+ "corresponding_person, applicant_comment, approver_comment, work_contents, contact, working_hours, "
					+ "corrected_work_time, corrected_retreat_time from application_contents "
					+ "where corresponding_person = '" + corresponding_person + "';";
		//申請者がNULL出ない場合は、承認者に適合する情報を取得
		} else if(corresponding_person == null && applicant != null) {
			//SQL文
			sql = "select application_number, application_date, applicant, application_name, application_type, "
					+ "application_category, application_status, target_date, application_reason, acquisition_time, "
					+ "holiday_acquisition_date, total_uptime, total_overtime_hours, correspondence_date, "
					+ "corresponding_person, applicant_comment, approver_comment, work_contents, contact, working_hours, "
					+ "corrected_work_time, corrected_retreat_time from application_contents "
					+ "where applicant = '" + applicant + "';";
		//上記２つの条件分岐に適合しなかった場合は、NullPointerExceptionをthrowする。
		} else {
			log.error("The param is NULL.");
			throw new NullPointerException();
		}
		
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		while(rs.next()) {
			ApplicationContentsBean acb = new ApplicationContentsBean();
			acb.setApplicationNumber(rs.getInt("application_number"));
			acb.setApplicationDate(rs.getString("application_date"));
			acb.setApplicant(rs.getString("applicant"));
			acb.setApplicationName(rs.getString("application_name"));
			acb.setApplicationType(rs.getString("application_type"));
			acb.setApplicationCategory(rs.getString("application_category"));
			acb.setApplicationStatus(rs.getString("application_status"));
			acb.setTargetDate(rs.getString("target_date"));
			acb.setApplicationReason(rs.getString("application_reason"));
			acb.setAcquisitionTime(rs.getString("acquisition_time"));
			acb.setHolidayAcquisitionDate(rs.getString("holiday_acquisition_date"));
			acb.setTotalUptime(rs.getString("total_uptime"));
			acb.setTotalOvertimeHours(rs.getString("total_overtime_hours"));
			acb.setCorrespondenceDate(rs.getString("correspondence_date"));
			acb.setCorrespondingPerson(rs.getString("corresponding_person"));
			acb.setApplicantComment(rs.getString("applicant_comment"));
			acb.setApproverComment(rs.getString("approver_comment"));
			acb.setWorkContents(rs.getString("work_contents"));
			acb.setContact(rs.getString("contact"));
			acb.setWorkingHours(rs.getString("working_hours"));
			acb.setCorrectedWorkTime(rs.getString("corrected_work_time"));
			acb.setCorrectedRetreatTime(rs.getString("corrected_retreat_time"));
			appList.add(acb);
		}
		
		log.info("getApplicationContents end");
		return appList;
	}
	
	/**
	 * 有給休暇の使用可能日数及び使用済み日数の更新を行うメソッド
	 * @param fromDate 申請開始日
	 * @param toDate　申請終了日
	 * @param emp_num　更新を行う従業員番号
	 * @return　更新が正常に行われた場合は、TRUEを返却<br>更新が正常に行われなかった場合は、FALSEを返却
	 * @throws SQLException データベース接続に問題が発生した場合
	 */
	public boolean updatePaidHolidayDays(int days, int emp_num) throws SQLException {
		log.info("updatePaidHolidayDays start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//未消化日数を取得
		String sql = "select undigestion_days from paid_holiday where emp_num = '" + emp_num + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		if(rs.next()) {
			int max_days = rs.getInt("undigestion_days");
			int diff_days = 0;
			if(days > max_days) {
				//使用可能最大日数を超えている場合は、申請ができないためエラー扱いとする。
				log.error(ALERT_TITLE + " : " + ALERT_CONTENT + "\r\n申請可能最大日数は、" + max_days + "日分までです。");
				return false;
			} else {
				if(days == 1) {
					diff_days = max_days - days;
				} else if(1 < days && days <= max_days) {
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
	
	/**
	 * 申請番号における申請情報を取得し、リスト化するメソッド
	 * @param application_number　申請番号
	 * @return　申請情報を格納したリスト
	 * @throws SQLException　データベース接続に問題が発生した場合
	 */
	public ArrayList<ApplicationContentsBean> getApplicationAppNumContents(Integer application_number) throws SQLException{
		log.info("getApplicationContents start");
		
		ArrayList<ApplicationContentsBean> appList = new ArrayList<ApplicationContentsBean>();
		String sql;
		
		sql = "select application_number, application_date, applicant, application_name, application_type, "
					+ "application_category, application_status, target_date, application_reason, acquisition_time, "
					+ "holiday_acquisition_date, total_uptime, total_overtime_hours, correspondence_date, "
					+ "corresponding_person, applicant_comment, approver_comment, work_contents, contact, working_hours, "
					+ "corrected_work_time, corrected_retreat_time from application_contents "
					+ "where application_number = '" + application_number + "';";
		
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		while(rs.next()) {
			ApplicationContentsBean acb = new ApplicationContentsBean();
			acb.setApplicationNumber(rs.getInt("application_number"));
			acb.setApplicationDate(rs.getString("application_date"));
			acb.setApplicant(rs.getString("applicant"));
			acb.setApplicationName(rs.getString("application_name"));
			acb.setApplicationType(rs.getString("application_type"));
			acb.setApplicationCategory(rs.getString("application_category"));
			acb.setApplicationStatus(rs.getString("application_status"));
			acb.setTargetDate(rs.getString("target_date"));
			acb.setApplicationReason(rs.getString("application_reason"));
			acb.setAcquisitionTime(rs.getString("acquisition_time"));
			acb.setHolidayAcquisitionDate(rs.getString("holiday_acquisition_date"));
			acb.setTotalUptime(rs.getString("total_uptime"));
			acb.setTotalOvertimeHours(rs.getString("total_overtime_hours"));
			acb.setCorrespondenceDate(rs.getString("correspondence_date"));
			acb.setCorrespondingPerson(rs.getString("corresponding_person"));
			acb.setApplicantComment(rs.getString("applicant_comment"));
			acb.setApproverComment(rs.getString("approver_comment"));
			acb.setWorkContents(rs.getString("work_contents"));
			acb.setContact(rs.getString("contact"));
			acb.setWorkingHours(rs.getString("working_hours"));
			acb.setCorrectedWorkTime(rs.getString("corrected_work_time"));
			acb.setCorrectedRetreatTime(rs.getString("corrected_retreat_time"));
			appList.add(acb);
		}
		
		log.info("getApplicationContents end");
		return appList;
	}
	
	/**
	 * 申請状況に応じた申請情報を取得し、リスト化するメソッド
	 * @param status　申請状況
	 * @param corresponding_person　対応者名
	 * @param applicant　申請者名
	 * @return　申請情報を格納したリスト
	 * @throws SQLException　データベース接続に問題が発生した場合
	 */
	public ArrayList<ApplicationContentsBean> getApplicationStatusContents(String status, 
			String corresponding_person, String applicant ) throws SQLException {
		log.info("getApplicationStatusContents start");
		
		//リスト情報格納用変数宣言
		ArrayList<ApplicationContentsBean> appList = new ArrayList<ApplicationContentsBean>();
		
		//SQL文格納用変数宣言
		String sql;
		
		//対応者がNULL出ない場合
		if(corresponding_person != null && applicant == null) {
			//引数に応じたSQL文
			switch(status) {
			//すべて
			case STATUSALL:
				sql = "select application_number, application_date, applicant, application_name, application_type, "
						+ "application_category, application_status, application_status, target_date, application_reason, "
						+ "acquisition_time, holiday_acquisition_date, total_uptime, total_overtime_hours, "
						+ "correspondence_date, corresponding_person, applicant_comment, approver_comment, work_contents, "
						+ "contact, working_hours, corrected_work_time, corrected_retreat_time from application_contents "
						+ "where corresponding_person = '" + corresponding_person + "';";
				rs = st.executeQuery(sql);
				log.debug("rs : {}", rs);
				while(rs.next()){
					ApplicationContentsBean acb = new ApplicationContentsBean();
					acb.setApplicationNumber(rs.getInt("application_number"));
					acb.setApplicationDate(rs.getString("application_date"));
					acb.setApplicant(rs.getString("applicant"));
					acb.setApplicationName(rs.getString("application_name"));
					acb.setApplicationType(rs.getString("application_type"));
					acb.setApplicationCategory(rs.getString("application_category"));
					acb.setApplicationStatus(rs.getString("application_status"));
					acb.setTargetDate(rs.getString("target_date"));
					acb.setApplicationReason(rs.getString("application_reason"));
					acb.setAcquisitionTime(rs.getString("acquisition_time"));
					acb.setHolidayAcquisitionDate(rs.getString("holiday_acquisition_date"));
					acb.setTotalUptime(rs.getString("total_uptime"));
					acb.setTotalOvertimeHours(rs.getString("total_overtime_hours"));
					acb.setCorrespondenceDate(rs.getString("correspondence_date"));
					acb.setCorrespondingPerson(rs.getString("corresponding_person"));
					acb.setApplicantComment(rs.getString("applicant_comment"));
					acb.setApproverComment(rs.getString("approver_comment"));
					acb.setWorkContents(rs.getString("work_contents"));
					acb.setContact(rs.getString("contact"));
					acb.setWorkingHours(rs.getString("working_hours"));
					acb.setCorrectedWorkTime(rs.getString("corrected_work_time"));
					acb.setCorrectedRetreatTime(rs.getString("corrected_retreat_time"));
					appList.add(acb);
				}
				break;
			
			//申請中、承認、却下
			case STATUSAPPLYING:
			case STATUSAPPROVAL:
			case STATUSREJECTED:
				sql = "select application_number, application_date, applicant, application_name, application_type, "
						+ "application_category, application_status, application_status, target_date, application_reason, "
						+ "acquisition_time, holiday_acquisition_date, total_uptime, total_overtime_hours, "
						+ "correspondence_date, corresponding_person, applicant_comment, approver_comment, work_contents, "
						+ "contact, working_hours, corrected_work_time, corrected_retreat_time from application_contents "
						+ "where application_status = '" + status + "' and " + " corresponding_person = '" + corresponding_person + "';";
				rs = st.executeQuery(sql);
				log.debug("rs : {}", rs);
				while(rs.next()){
					ApplicationContentsBean acb = new ApplicationContentsBean();
					acb.setApplicationNumber(rs.getInt("application_number"));
					acb.setApplicationDate(rs.getString("application_date"));
					acb.setApplicant(rs.getString("applicant"));
					acb.setApplicationName(rs.getString("application_name"));
					acb.setApplicationType(rs.getString("application_type"));
					acb.setApplicationCategory(rs.getString("application_category"));
					acb.setApplicationStatus(rs.getString("application_status"));
					acb.setTargetDate(rs.getString("target_date"));
					acb.setApplicationReason(rs.getString("application_reason"));
					acb.setAcquisitionTime(rs.getString("acquisition_time"));
					acb.setHolidayAcquisitionDate(rs.getString("holiday_acquisition_date"));
					acb.setTotalUptime(rs.getString("total_uptime"));
					acb.setTotalOvertimeHours(rs.getString("total_overtime_hours"));
					acb.setCorrespondenceDate(rs.getString("correspondence_date"));
					acb.setCorrespondingPerson(rs.getString("corresponding_person"));
					acb.setApplicantComment(rs.getString("applicant_comment"));
					acb.setApproverComment(rs.getString("approver_comment"));
					acb.setWorkContents(rs.getString("work_contents"));
					acb.setContact(rs.getString("contact"));
					acb.setWorkingHours(rs.getString("working_hours"));
					acb.setCorrectedWorkTime(rs.getString("corrected_work_time"));
					acb.setCorrectedRetreatTime(rs.getString("corrected_retreat_time"));
					appList.add(acb);
				}
				break;
			default: 
				break;
			}
		} else if(corresponding_person == null && applicant != null) {
			//引数に応じたSQL文
			switch(status) {
			//すべて
			case STATUSALL:
				sql = "select application_number, application_date, applicant, application_name, application_type, "
						+ "application_category, application_status, application_status, target_date, application_reason, "
						+ "acquisition_time, holiday_acquisition_date, total_uptime, total_overtime_hours, "
						+ "correspondence_date, corresponding_person, applicant_comment, approver_comment, work_contents, "
						+ "contact, working_hours, corrected_work_time, corrected_retreat_time from application_contents "
						+ "where applicant = '" + applicant + "';";
				rs = st.executeQuery(sql);
				log.debug("rs : {}", rs);
				while(rs.next()){
					ApplicationContentsBean acb = new ApplicationContentsBean();
					acb.setApplicationNumber(rs.getInt("application_number"));
					acb.setApplicationDate(rs.getString("application_date"));
					acb.setApplicant(rs.getString("applicant"));
					acb.setApplicationName(rs.getString("application_name"));
					acb.setApplicationType(rs.getString("application_type"));
					acb.setApplicationCategory(rs.getString("application_category"));
					acb.setApplicationStatus(rs.getString("application_status"));
					acb.setTargetDate(rs.getString("target_date"));
					acb.setApplicationReason(rs.getString("application_reason"));
					acb.setAcquisitionTime(rs.getString("acquisition_time"));
					acb.setHolidayAcquisitionDate(rs.getString("holiday_acquisition_date"));
					acb.setTotalUptime(rs.getString("total_uptime"));
					acb.setTotalOvertimeHours(rs.getString("total_overtime_hours"));
					acb.setCorrespondenceDate(rs.getString("correspondence_date"));
					acb.setCorrespondingPerson(rs.getString("corresponding_person"));
					acb.setApplicantComment(rs.getString("applicant_comment"));
					acb.setApproverComment(rs.getString("approver_comment"));
					acb.setWorkContents(rs.getString("work_contents"));
					acb.setContact(rs.getString("contact"));
					acb.setWorkingHours(rs.getString("working_hours"));
					acb.setCorrectedWorkTime(rs.getString("corrected_work_time"));
					acb.setCorrectedRetreatTime(rs.getString("corrected_retreat_time"));
					appList.add(acb);
				}
				break;
			
			//申請中、承認、却下
			case STATUSAPPLYING:
			case STATUSAPPROVAL:
			case STATUSREJECTED:
				sql = "select application_number, application_date, applicant, application_name, application_type, "
						+ "application_category, application_status, application_status, target_date, application_reason, "
						+ "acquisition_time, holiday_acquisition_date, total_uptime, total_overtime_hours, "
						+ "correspondence_date, corresponding_person, applicant_comment, approver_comment, work_contents, "
						+ "contact, working_hours, corrected_work_time, corrected_retreat_time, from application_contents "
						+ "where application_status = '" + status + "' and " + " applicant = '" + applicant + "';";
				rs = st.executeQuery(sql);
				log.debug("rs : {}", rs);
				while(rs.next()){
					ApplicationContentsBean acb = new ApplicationContentsBean();
					acb.setApplicationNumber(rs.getInt("application_number"));
					acb.setApplicationDate(rs.getString("application_date"));
					acb.setApplicant(rs.getString("applicant"));
					acb.setApplicationName(rs.getString("application_name"));
					acb.setApplicationType(rs.getString("application_type"));
					acb.setApplicationCategory(rs.getString("application_category"));
					acb.setApplicationStatus(rs.getString("application_status"));
					acb.setTargetDate(rs.getString("target_date"));
					acb.setApplicationReason(rs.getString("application_reason"));
					acb.setAcquisitionTime(rs.getString("acquisition_time"));
					acb.setHolidayAcquisitionDate(rs.getString("holiday_acquisition_date"));
					acb.setTotalUptime(rs.getString("total_uptime"));
					acb.setTotalOvertimeHours(rs.getString("total_overtime_hours"));
					acb.setCorrespondenceDate(rs.getString("correspondence_date"));
					acb.setCorrespondingPerson(rs.getString("corresponding_person"));
					acb.setApplicantComment(rs.getString("applicant_comment"));
					acb.setApproverComment(rs.getString("approver_comment"));
					acb.setWorkContents(rs.getString("work_contents"));
					acb.setContact(rs.getString("contact"));
					acb.setWorkingHours(rs.getString("working_hours"));
					acb.setCorrectedWorkTime(rs.getString("corrected_work_time"));
					acb.setCorrectedRetreatTime(rs.getString("corrected_retreat_time"));
					appList.add(acb);
				}
				break;
			
			default: 
				break;
			}
		} else {
			log.error("data is NULL");
			throw new NullPointerException();
		}
		
		log.info("getApplicationStatusContents end");
		return appList;
	}
	
	/**
	 * 特別時間休における取得時間情報を更新するメソッド
	 * @param time　取得時間
	 * @param emp_num　従業員番号
	 * @return　更新が正常に完了した場合は、TRUEを返却<br>更新が正常に完了しなかった場合は、FALSEを返却
	 * @throws SQLException　データベース接続に問題が発生した場合
	 */
	public boolean updateSpecialTimeHoliday(double time, int emp_num) throws SQLException {
		log.info("updateSpecialTimeHoliday start");
		
		//処理結果格納用変数宣言
		boolean result = false;
		
		//自動コミットモード削除
		cnct.setAutoCommit(false);
		
		//使用可能残時間の取得
		String sql = "select undigestion_days, usdigestion_times from paid_holiday where emp_num = '" + emp_num + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		if(rs.next()) {
			double max_time = rs.getDouble("usdigestion_times");
			int max_day = rs.getInt("undigestion_days");
			
			//申請時間が所定労働時間よりも大きい場合
			if(time > max_time) {
				//何日分オーバーしているかを求める
				int overDay = (int)(time / max_time);
				//5日未満のオーバーであれば、申請可能なためデータベースの更新を行う。
				if(1 <= overDay && overDay < 5) {
					double diff = time - max_time - overDay;
					sql = "update paid_holiday set digestion_days = '" + overDay + "', undigestion_days = '" 
					+ (max_day - overDay) + "', digestion_times ='" + diff + "', usdigestion_times = '" 
					+ (max_day - diff) + "' where emp_num = '" + emp_num + "';";
					st.executeUpdate(sql);
					cnct.commit();
					result = true;
				//5日以上の場合は、申請できないためエラーとする。
				} else {
					log.error(ALERT_TITLE1 + " : " + ALERT_CONTENT1);
					result = false;
				}
			} else {
				int useDay = 1;
				sql = "update paid_holiday set digestion_days = '" + useDay + "', undigestion_days = '" 
						+ (max_day - useDay) + "', digestion_times ='" + time + "', usdigestion_times = '" 
						+ (max_day - time) + "' where emp_num = '" + emp_num + "';";
				st.executeUpdate(sql);
				cnct.commit();
				result = true;
			}
		} else {
			log.error("Not Data Matched.");
			result = false;
		}
		
		log.info("updateSpecialTimeHoliday end");
		return result;
	}
	
	/**
	 * 勤務パターン一覧取得メソッド
	 * @return　リスト化した勤務パターン一覧情報
	 * @throws SQLException　データベース接続に問題が発生した場合
	 */
	public ArrayList<WorkPatternBean> getWorkPatternList() throws SQLException {
		log.info("getWorkPatternList start");
		
		ArrayList<WorkPatternBean> patternList = new ArrayList<WorkPatternBean>();
		
		//SQL文
		String sql = "select pattern_name from work_pattern;";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		while(rs.next()) {
			WorkPatternBean wpb = new WorkPatternBean();
			wpb.setPatternName(rs.getString("pattern_name"));
			patternList.add(wpb);
		}
		
		return patternList;
	}
	
	/**
	 * 社員の勤務時間を更新および新規登録を行うメソッド
	 * @param emp_num 従業員番号
	 * @param commuting_time　出勤時間
	 * @param leave_time　退勤時間
	 * @param attendance_type　勤務種類
	 * @param scheduled_working_hours　所定労働時間
	 * @return 登録が正常に行われた場合はTRUEを返却<br>登録が正常に行われなかった場合はFALSEを返却
	 * @throws SQLException　データベース処理に問題が発生した場合
	 */
	public boolean updateContractTime(Integer emp_num, String commuting_time, String leave_time, 
			String attendance_type, String scheduled_working_hours) throws SQLException {
		log.info("updateContractTime start");
		
		//結果情報取得用変数宣言
		boolean result = false;
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL文
		String sql = "select commuting_time, leave_time, attendance_type, scheduled_working_hours from contract_time "
				+ "where emp_num = '" + emp_num + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		//データが取得できた場合は、内容を更新
		if(rs.next()) {
			try {
				sql = "update contract_time set commuting_time = '" + commuting_time + "', leave_time = '" + leave_time + "', "
						+ "attendance_type = '" + attendance_type + "', scheduled_working_hours = '" + scheduled_working_hours + "' "
						+ "where emp_num = '" + emp_num + "';";
				st.executeUpdate(sql);
				cnct.commit();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				cnct.rollback();
				result = false;
			}
		//データが取得できなかった場合は、内容を新規登録
		} else {
			try {
				sql = "insert into contract_time (emp_num, commuting_time, leave_time, attendance_type, "
						+ "scheduled_working_hours) values ('" + emp_num + "', '" + commuting_time + "', '" 
						+ leave_time + "', '" + attendance_type + "', '" + scheduled_working_hours + "');";
				st.executeUpdate(sql);
				cnct.commit();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				cnct.rollback();
				result = false;
			}
		}
		
		log.info("updateContractTime end");
		return result;
	}
	
	/**
	 * 従業員の所定労働時間情報を取得するメソッド
	 * @param emp_num　社員番号
	 * @return　所定労働時間
	 * @throws SQLException　データベース接続に問題が発生した場合
	 */
	public String getScheduledWorkingHours(Integer emp_num) throws SQLException {
		log.info("getScheduledWorkingHours start");
		
		String hours = "";
		
		//SQL文
		String sql = "select scheduled_working_hours from contract_time where emp_num = '" + emp_num + "';";
		rs = st.executeQuery(sql);
		log.debug("st : {}", st);
		
		//情報取得
		if(rs.next()) {
			hours = rs.getString("scheduled_working_hours");
		} else {
			log.error("Sorry, not much date.");
			hours = "00:00";
		}
		
		return hours;
	}
	
	/**
	 * 工数名及び工数時間情報を取得し、リスト化するメソッド
	 * @param emp_num　従業員番号
	 * @param date　取得対象月
	 * @return　リスト化した工数名および工数時間情報
	 * @throws SQLException　データベース接続に問題が発生した場合
	 */
	public ArrayList<ManHourRequestBean> getManHourInfo(Integer emp_num, String date) throws SQLException {
		log.info("getManHourInfo start");
		
		//取得データ格納用リスト変数宣言
		ArrayList<ManHourRequestBean> mhList = new ArrayList<ManHourRequestBean>();
		
		//SQL文
		String sql = "select registration_date, man_hour_name1, man_hour_time1, man_hour_name2, man_hour_time2, "
				+ "man_hour_name3, man_hour_time3, man_hour_name4, man_hour_time4, man_hour_name5, man_hour_time5, "
				+ "man_hour_name6, man_hour_time6, man_hour_name7, man_hour_time7, man_hour_name8, man_hour_time8, "
				+ "man_hour_name9, man_hour_time9, man_hour_name10, man_hour_time10 from emp_man_hour where "
				+ "emp_num = '" + emp_num + "' and registration_date like '" + date + "%';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		//データ取得
		while(rs.next()) {
			ManHourRequestBean mhrb = new ManHourRequestBean();
			mhrb.setRegistrationDate(rs.getString("registration_date"));
			mhrb.setManHourName1(rs.getString("man_hour_name1"));
			mhrb.setManHourTime1(rs.getString("man_hour_time1"));
			mhrb.setManHourName2(rs.getString("man_hour_name2"));
			mhrb.setManHourTime2(rs.getString("man_hour_time2"));
			mhrb.setManHourName3(rs.getString("man_hour_name3"));
			mhrb.setManHourTime3(rs.getString("man_hour_time3"));
			mhrb.setManHourName4(rs.getString("man_hour_name4"));
			mhrb.setManHourTime4(rs.getString("man_hour_time4"));
			mhrb.setManHourName5(rs.getString("man_hour_name5"));
			mhrb.setManHourTime5(rs.getString("man_hour_time5"));
			mhrb.setManHourName6(rs.getString("man_hour_name6"));
			mhrb.setManHourTime6(rs.getString("man_hour_time6"));
			mhrb.setManHourName7(rs.getString("man_hour_name7"));
			mhrb.setManHourTime7(rs.getString("man_hour_time7"));
			mhrb.setManHourName8(rs.getString("man_hour_name8"));
			mhrb.setManHourTime8(rs.getString("man_hour_time8"));
			mhrb.setManHourName9(rs.getString("man_hour_name9"));
			mhrb.setManHourTime9(rs.getString("man_hour_time9"));
			mhrb.setManHourName10(rs.getString("man_hour_name10"));
			mhrb.setManHourTime10(rs.getString("man_hour_time10"));
			mhList.add(mhrb);
		}
		return mhList;
	}
	
	/**
	 * 特定の申請の取消処理を行うメソッド
	 * @param emp_num　従業員番号
	 * @param application_number　申請番号
	 * @param application_status　申請状況
	 * @return　処理が正常終了した場合はTRUEを返却<br>異常終了した場合はFALSEを返却
	 * @throws SQLException　データベース接続に問題が発生した場合
	 */
	public boolean deleteApplicationContents(Integer application_number, String application_status, String applicant) 
			throws SQLException {
		log.info("deleteApplicationContents start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL文
		String sql = "select * from application_contents where application_number = '" + application_number + "' "
				+ "and application_status = '" + application_status + "' and applicant = '" + applicant + "';";
		
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		//データが存在していた場合は、削除処理を実行
		if(rs.next()) {
			sql = "delete from application_contents where application_number = '" + application_number + "' and "
					+ "application_status = '" + application_status + "' and applicant = '" + applicant + "';";
			try {
				cnct.commit();
				st.executeUpdate(sql);
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
				cnct.rollback();
				return false;
			} finally {}
			return true;
		} else {
			return false;
		}
	}
}
