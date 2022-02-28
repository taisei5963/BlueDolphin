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

import Bean.ApplicationContentsBean;
import Bean.LoginBean;
import DAO.ApprovalApplicationDAO;
import Other.DiffDateCalculation;

/**
 * 各押下されたボタンに応じた処理を実行するサーブレットクラス
 */
@WebServlet("/ApprovalRejectedDetailsServlet")
public class ApprovalRejectedDetailsServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ApprovalRejectedDetailsServlet.class);
	
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String APPROVAL = "承認";
	private static final String REJECTED = "却下";
	private static final String APPROVALPROCESS = "承認処理";
	private static final String REJECTEDPROCESS = "却下処理";
	
	//休暇名称用定数宣言
	private static final String PAIDHOLIDAY = "年次有給休暇";
	private static final String MORNINGHOLIDAY = "午前休";
	private static final String AFTERNOON = "午後休";
	private static final String SPECIALTIMELEAVE = "特別時間休暇";
	
	//申請種類用定数宣言
	private static final String SINGLEDAY = "単日数申請";
	private static final String MULTIPLE = "複数日申請";
	private static final String RANGESPECIFICATION = "範囲指定申請";
	
	/**
	 * doPostメソッドを呼び出すメソッド
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException サーブレットで何らかの問題が発生した場合
	 * @throws IOException 入出力処理の失敗、または割り込みの発生が起きた場合
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//doPost(req, resp);
	}

	/**
	 * 承認・却下処理対応完了画面を表示するメソッド
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException サーブレットで何らかの問題が発生した場合
	 * @throws IOException 入出力処理の失敗、または割り込みの発生が起きた場合
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");

		//セッション情報の送受信を行うための変数宣言
		HttpSession session = req.getSession();
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
		ArrayList<ApplicationContentsBean> appList = (ArrayList<ApplicationContentsBean>)session.getAttribute("appList");
		
		LocalDate date;
		DateTimeFormatter dtf;
		String name = null, comment = null, formatDate = null, vacation_name = null, classification = null, application_date = null, acquisition_time = null;
		Integer AppNum = null, emp_num = null;
		
		for(LoginBean lb : userlist) {
			name = lb.getName();
			emp_num = lb.getNumber();
		}
		
		for(ApplicationContentsBean acb: appList) {
			AppNum = acb.getApplicationNumber();
			vacation_name = acb.getApplicationName();
			classification = acb.getApplicationType();
			application_date = acb.getApplicationDate();
			acquisition_time = acb.getAcquisitionTime();
		}
		
		String process = req.getParameter("process");
		log.debug("process : {}", process);
		
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		
		//DB登録結果格納用変数宣言
		boolean result = false;
		
		if(process != null) {
			switch(process) {
			//却下処理
			case REJECTEDPROCESS:
				//パラメータ取得
				comment = req.getParameter("comment");
				
				//現在日付を取得
				date = LocalDate.now();
				dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)");
				formatDate = dtf.format(date);
				
				//却下情報を申請情報テーブルに登録
				try {
					aadao.dbConenect();
					aadao.createSt();
					result = aadao.updateApplicationContents(AppNum, REJECTED, formatDate, name, comment);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					result = false;
				} finally {
					aadao.dbDiscnct();
				}
				
				//更新処理結果
				if(result) {
					resp.sendRedirect("approval_rejected_completed.jsp");
				} else {
					resp.sendRedirect("approval_rejected_error.jsp");
				}
				break;
				
			//承認処理
			case APPROVALPROCESS:
				//パラメータ取得
				comment = req.getParameter("comment");
				
				//日付の差分出力クラスのインスタンス取得
				DiffDateCalculation dc = DiffDateCalculation.getInstance();
				
				//現在日付を取得
				date = LocalDate.now();
				dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)");
				formatDate = dtf.format(date);
				
				//承認情報を申請情報テーブルに登録
				try {
					aadao.dbConenect();
					aadao.createSt();
					
					//年次有給休暇を申請した場合
					if(PAIDHOLIDAY.equals(vacation_name) && SINGLEDAY.equals(classification)) {
					 	result = aadao.updatePaidHolidayDays(1, emp_num);
					} else if(PAIDHOLIDAY.equals(vacation_name) && MULTIPLE.equals(classification)) {
						result = aadao.updatePaidHolidayDays(application_date.split(", ").length, emp_num);
					} else if(PAIDHOLIDAY.equals(vacation_name) && RANGESPECIFICATION.equals(classification)) {
						result = aadao.updatePaidHolidayDays(dc.diffDate(application_date.substring(0, 10), application_date.substring(11, 21)), emp_num);
					//午前休を申請した場合
					} else if(MORNINGHOLIDAY.equals(vacation_name)) {
						double time = 3.0;
						result = aadao.updateSpecialTimeHoliday(time, emp_num);
					//午後休を申請した場合
					} else if(AFTERNOON.equals(vacation_name)) {
						double time = 5.0;
						result = aadao.updateSpecialTimeHoliday(time, emp_num);
					//特別時間休暇を申請した場合
					} else if(SPECIALTIMELEAVE.equals(vacation_name)) {
						double hour = Double.parseDouble(acquisition_time.substring(0, 1));
						double min = Double.parseDouble(acquisition_time.substring(2, 4)) / 60;
						result = aadao.updateSpecialTimeHoliday((hour + min), emp_num);
					} else {
						result = true;
					}
					
					//各指定休暇時の更新処理が正常に行われた場合
					if(result) {
						result = aadao.updateApplicationContents(AppNum, APPROVAL, formatDate, name, comment);
						if(result) {
							resp.sendRedirect("approval_rejected_completed.jsp");
						} else {
							resp.sendRedirect("approval_rejected_error.jsp");
						}
					} else {
						resp.sendRedirect("approval_rejected_error.jsp");
					}
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					result = false;
				} finally {
					aadao.dbDiscnct();
				}
				break;
			default:
				break;
			}
		}
	}
}
