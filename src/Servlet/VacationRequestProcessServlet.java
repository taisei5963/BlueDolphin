package Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Bean.LoginBean;
import DAO.ApplicationApproverDAO;
import DAO.ApprovalApplicationDAO;
import Other.AcquisitionTime;
import Other.ApplicationNumber;

/**
 * 休暇申請処理行うサーブレットクラス
 *
 */

@WebServlet("/VacationRequestProcessServlet")
public class VacationRequestProcessServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(VacationRequestProcessServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String PROCESSNAME = "承認申請処理";
	private static final String APPLICATIONSTATUS = "申請中";
	private static final String VACATIONREQUEST = "休暇申請";
	
	private static final String PAIDHOLIDAY = "年次有給休暇";
	private static final String MORNINGHOLIDAY = "午前休";
	private static final String AFTERNOON = "午後休";
	private static final String KEIOVACATION = "慶弔休暇";
	private static final String PARENTALLEAVE = "育児休暇";
	private static final String LONGTERMCAREVACATION = "介護休暇";
	private static final String ABSENCE = "欠勤";
	private static final String CLOSINGWAITING = "休業待機";
	private static final String SPECIALVACATION = "特別休暇";
	private static final String POSTPARTUMLEAVE = "産前産後休暇";
	private static final String ANNIVERSARYVACATION = "アニバーサリー休暇";
	private static final String BIRTHDAYVACATION = "バースデー休暇";
	private static final String MENSTRUALVACATION = "生理休暇";
	private static final String SPECIALTIMELEAVE = "特別時間休暇";
	
	/**
	 * @param req クライアントが Servlet へ要求したリクエスト内容を含む HttpServletRequest オブジェクト
	 * @param resp Servlet がクライアントに返すレスポンス内容を含む HttpServletResponse オブジェクト
	 * @throws ServletException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 * @throws IOException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * @param req クライアントが Servlet へ要求したリクエスト内容を含む HttpServletRequest オブジェクト
	 * @param resp Servlet がクライアントに返すレスポンス内容を含む HttpServletResponse オブジェクト
	 * @throws ServletException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 * @throws IOException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		HttpSession session = req.getSession();
		
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
		Integer emp_num = null;
		String name = null;
		
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
			name = lb.getName();
		}
		
		String vacation_name = req.getParameter("vacation_name");
		String process = req.getParameter("process");
		String classification = req.getParameter("classification");
		String remarks = req.getParameter("remarks");
		String application_date = req.getParameter("application_date");
		String date1 = req.getParameter("date1");
		String date2 = req.getParameter("date2");
		String start_time = req.getParameter("start_time");
		String end_time = req.getParameter("end_time");
		String reason = req.getParameter("reason");
		
		if(PROCESSNAME.equals(process)) {
			//各インスタンス情報取得
			ApplicationNumber an = ApplicationNumber.getInstance();
			AcquisitionTime at = AcquisitionTime.getInstance();
			ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
			ApplicationApproverDAO adao = ApplicationApproverDAO.getInstance();

			//現在日付を取得
			LocalDate date = LocalDate.now();
			DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)");
			String formatDate = dtf1.format(date);
			
			switch(vacation_name) {
			//年次有給休の場合
			case PAIDHOLIDAY:
				try {
					aadao.dbConenect();
					aadao.createSt();
					adao.dbConenect();
					adao.createSt();
					//単日数申請の場合
					if(date1 != null && date2 == null && application_date == null) {
						application_date = date1;
					//範囲指定申請の場合	
					} else if(date1 != null && date2 != null && application_date == null) {
						application_date = date1 + "〜" + date2;
					//複数日申請の場合
					} else {}
					
					//申請者に該当する承認者を取得
					String corresponding_person = adao.getAuthorizer(name);
					
					//承認申請テーブルに申請情報を登録
					boolean result = aadao.setApplicationContents(
							//申請番号
							an.getAppNumber(emp_num),
							//申請日
							formatDate,
							//申請者
							name, 
							//申請名
							vacation_name,
							//申請種類
							classification,
							//申請区分
							VACATIONREQUEST,
							//申請状況
							APPLICATIONSTATUS,
							//対象日
							application_date,
							//申請理由
							reason, 
							//取得時間
							"",
							//振休取得日
							"",
							//総稼働時間
							"",
							//総残業時間
							"",
							//承認日
							"", 
							//承認者
							corresponding_person, 
							//申請者コメント
							remarks, 
							//承認者コメント
							"", 
							//作業内容
							"", 
							//連絡先
							"", 
							//作業時間
							"",
							//修正後出勤時刻
							"",
							//修正後退勤時刻
							""
							);
					log.debug("result : {}", result);
					if(result) {
						log.debug("申請情報を正常に登録いたしました。");
					} else {
						log.error("申請情報を正常に登録できませんでした。");
						resp.sendRedirect("various_application_error.jsp");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					log.error(SQLEXCEPTION);
					log.error("申請情報を正常に登録できませんでした。");
					resp.sendRedirect("various_application_error.jsp");
				} finally {
					aadao.dbDiscnct();
					adao.dbDiscnct();
				}
				break;
				
			//午前休、午後休、慶弔休暇、育児休暇、介護休暇、欠勤、休業待機、特別休暇、産前産後休暇、アニバーサリー休暇、バースデー休暇、生理休暇選択時
			case MORNINGHOLIDAY:
			case AFTERNOON:
			case KEIOVACATION:
			case PARENTALLEAVE:
			case LONGTERMCAREVACATION:
			case ABSENCE:
			case CLOSINGWAITING:
			case SPECIALVACATION:
			case POSTPARTUMLEAVE:
			case ANNIVERSARYVACATION:
			case BIRTHDAYVACATION:
			case MENSTRUALVACATION:
				try {
					aadao.dbConenect();
					aadao.createSt();
					adao.dbConenect();
					adao.createSt();
					
					//申請者に該当する承認者を取得
					String corresponding_person = adao.getAuthorizer(name);
					
					//承認申請テーブルに申請情報を登録
					boolean result = aadao.setApplicationContents(
							//申請番号
							an.getAppNumber(emp_num),
							//申請日
							formatDate,
							//申請者
							name, 
							//申請名
							vacation_name,
							//申請種類
							"",
							//申請区分
							VACATIONREQUEST,
							//申請状況
							APPLICATIONSTATUS,
							//対象日
							date1,
							//申請理由
							reason, 
							//取得時間
							"",
							//振休取得日
							"",
							//総稼働時間
							"",
							//総残業時間
							"",
							//承認日
							"", 
							//承認者
							corresponding_person, 
							//申請者コメント
							remarks, 
							//承認者コメント
							"", 
							//作業内容
							"", 
							//連絡先
							"", 
							//作業時間
							"",
							//修正後出勤時刻
							"",
							//修正後退勤時刻
							""
							);
					log.debug("result : {}", result);
					if(result) {
						log.debug("申請情報を正常に登録いたしました。");
					} else {
						log.error("申請情報を正常に登録できませんでした。");
						resp.sendRedirect("various_application_error.jsp");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					log.error(SQLEXCEPTION);
					log.error("申請情報を正常に登録できませんでした。");
					resp.sendRedirect("various_application_error.jsp");
				} finally {
					aadao.dbDiscnct();
					adao.dbDiscnct();
				}
				break;
				
			//特別時間休選択時
			case SPECIALTIMELEAVE:
				try {
					aadao.dbConenect();
					aadao.createSt();
					adao.dbConenect();
					adao.createSt();
					
					//申請者に該当する承認者を取得
					String corresponding_person = adao.getAuthorizer(name);
					
					//承認申請テーブルに申請情報を登録
					boolean result = aadao.setApplicationContents(
							//申請番号
							an.getAppNumber(emp_num),
							//申請日
							formatDate,
							//申請者
							name, 
							//申請名
							vacation_name,
							//申請種類
							"",
							//申請内容
							VACATIONREQUEST,
							//申請状況
							APPLICATIONSTATUS,
							//対象日
							date1,
							//申請理由
							reason,
							//取得時間
							at.calcAcquisitionTime(start_time, end_time),
							//振休取得日
							"",
							//総稼働時間
							"",
							//総残業時間
							"",
							//承認日
							"", 
							//承認者
							corresponding_person, 
							//申請者コメント
							remarks, 
							//承認者コメント
							"", 
							//作業内容
							"", 
							//連絡先
							"", 
							//作業時間
							"",
							//修正後出勤時刻
							"",
							//修正後退勤時刻
							""
							);
					log.debug("result : {}", result);
					
					if(result) {
						log.debug("申請情報を正常に登録いたしました。");
					} else {
						log.error("申請情報を正常に登録できませんでした。");
						resp.sendRedirect("various_application_error.jsp");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					log.error(SQLEXCEPTION);
					log.error("申請情報を正常に登録できませんでした。");
					resp.sendRedirect("various_application_error.jsp");
				} finally {
					aadao.dbDiscnct();
					adao.dbDiscnct();
				}
				break;
			}
			session.setAttribute("acquisition_time", at.calcAcquisitionTime(start_time, end_time));
			session.setAttribute("application_date", application_date);
			session.setAttribute("vacation_name", vacation_name);
			session.setAttribute("classification", classification);
			resp.sendRedirect("various_applications_completed.jsp");
		} else if(process == null){
			session.setAttribute("vacation_name", vacation_name);
			session.setAttribute("classification", classification);
			resp.sendRedirect("VacationRequestViewServlet");
		}
	}
}
