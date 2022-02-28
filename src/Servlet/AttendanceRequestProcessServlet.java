package Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Bean.KinmuAllTimeBean;
import Bean.LoginBean;
import DAO.ApplicationApproverDAO;
import DAO.ApprovalApplicationDAO;
import DAO.KintaiDAO;
import Other.ApplicationNumber;
import Other.BusinessDays;

/**
 * 勤怠申請処理行うサーブレットクラス
 *
 */

@WebServlet("/AttendanceRequestProcessServlet")
public class AttendanceRequestProcessServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AttendanceRequestProcessServlet.class);
	
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	//文言比較用定数宣言
	private static final String APPROVALPROCESS      = "申請処理";
	private static final String APPLICATIONEXECUTION = "申請実行";
	private static final String APPLICATIONNAME      = "勤怠申請";
	private static final String APPLICATIONSTATUS    = "申請中";
	private static final String PRINTPROCESS         = "印刷処理";
	
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
		ArrayList<KinmuAllTimeBean> kinmuTimeList = (ArrayList<KinmuAllTimeBean>) session.getAttribute("kinmuTimeList");
		
		KintaiDAO kdao = KintaiDAO.getInstance();
		Calendar thisMonthCaleneder = Calendar.getInstance();
		BusinessDays bd = BusinessDays.getInstance();
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		ApplicationNumber an = ApplicationNumber.getInstance();
		ApplicationApproverDAO adao = ApplicationApproverDAO.getInstance();
		
		Integer emp_num = null;
		String name = null, comment = null, hours = null;
		
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
			name = lb.getName();
		}
		
		try {
			aadao.dbConenect();
			aadao.createSt();
			hours = aadao.getScheduledWorkingHours(emp_num);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			resp.sendRedirect("/KinmuhyoServlet");
		} finally {
			aadao.dbDiscnct();
		}
		
		//今月の情報を取得
		LocalDate now = LocalDate.now();
		int days = bd.getBusinessDays(now.getMonthValue());
		String total = bd.getScheduledTotalWorkingHours(days, hours);
		
		//実行処理内容
		String process = req.getParameter("process");
		log.debug("process : {}", process);
		
		//申請処理
		if(APPROVALPROCESS.equals(process)) {
			session.setAttribute("total", total);
			resp.sendRedirect("time_and_attendance_request_view.jsp");
		//申請処理実行
		} else if(APPLICATIONEXECUTION.equals(process)) {
			//セッション情報の取得
			String display_date = (String)session.getAttribute("display_date");
			String total_actual = (String)session.getAttribute("total_actual");
			String total_overtime = (String)session.getAttribute("total_overtime");
			
			comment = req.getParameter("comment");
			
			if(comment.equals("") || comment == null) {
				comment = "";
			}

			//現在日付を取得
			LocalDate date = LocalDate.now();
			DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)");
			String formatDate = dtf1.format(date);
			
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
						APPLICATIONNAME,
						//申請種類
						"",
						//申請区分
						APPLICATIONNAME,
						//申請状況
						APPLICATIONSTATUS,
						//対象日
						display_date.substring(0, 4) + "年" + display_date.substring(5, 7) + "月",
						//申請理由
						"", 
						//取得時間
						"",
						//振休取得日
						"",
						//総稼働時間
						total_actual,
						//総残業時間
						total_overtime,
						//承認日
						"", 
						//承認者
						corresponding_person, 
						//申請者コメント
						comment, 
						//承認者コメント
						"", 
						//作業内容
						"", 
						//連絡先
						"", 
						//出勤時間
						"",
						//修正後出勤時刻
						"",
						//修正後退勤時刻
						""
						);
				log.debug("result : {}", result);
				if(result) {
					resp.sendRedirect("various_applications_completed.jsp");
				} else {
					log.error("申請情報を正常に登録できませんでした。");
					resp.sendRedirect("various_application_error.jsp");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(SQLEXCEPTION);
				resp.sendRedirect("various_application_error.jsp");
			} finally {
				aadao.dbDiscnct();
				adao.dbDiscnct();
			}
		//印刷処理
		} else if(PRINTPROCESS.equals(process)) {
			//プルダウン情報から月の情報を取得する。
			int monthValue = Integer.parseInt(req.getParameter("display_date").substring(5, 7));
			
			//印刷用プレビュー画面表示
			session.setAttribute("monthValue", monthValue);
			resp.sendRedirect("time_and_attendance_request_print.jsp");
		} else {
			String total_actual = "", total_overtime = "";
			String display_date = req.getParameter("display_date");
			log.debug("display_date : {}", display_date);
			try {
				kdao.dbConenect();
				kdao.createSt();
				kinmuTimeList = kdao.getAllWorkTime(emp_num, display_date);
				total_actual = kdao.getTotalActualHours(emp_num, display_date);
				total_overtime = kdao.getTotalOvertimeHours(emp_num, display_date);
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
				resp.sendRedirect("various_applications_error.jsp");
			} finally {
				kdao.dbDiscnct();
			}
			
			log.debug("total_actual : {}", total_actual);
			log.debug("total_overtime : {}", total_overtime);
			
			thisMonthCaleneder.set(Calendar.YEAR, Integer.parseInt(display_date.substring(0, 4)));
			thisMonthCaleneder.set(Calendar.MONTH, Integer.parseInt(display_date.substring(5, 7)));
			
			total = bd.getScheduledTotalWorkingHours(bd.getBusinessDays(thisMonthCaleneder.get(Calendar.MONTH)), hours);
			
			session.setAttribute("thisMonthCaleneder", thisMonthCaleneder);
			session.setAttribute("display_date", display_date);
			session.setAttribute("kinmuTimeList", kinmuTimeList);
			session.setAttribute("total_actual", total_actual);
			session.setAttribute("total_overtime", total_overtime);
			session.setAttribute("total", total);
			resp.sendRedirect("time_and_attendance_request_view.jsp");
		}
	}
}
