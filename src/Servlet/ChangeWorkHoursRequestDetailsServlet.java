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
import Other.AcquisitionTime;

/**
 * 残業申請処理行うサーブレットクラス
 *
 */

@WebServlet("/ChangeWorkHoursRequestDetailsServlet")
public class ChangeWorkHoursRequestDetailsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ChangeWorkHoursRequestDetailsServlet.class);
	
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	//文言比較用定数宣言
	private static final String APPROVALPROCESS       = "承認処理";
	private static final String REJECTIONPROCESSING   = "却下処理";
	private static final String APPROVAL = "承認";
	private static final String REJECTED = "却下";
	
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
		ArrayList<ApplicationContentsBean> appList = (ArrayList<ApplicationContentsBean>)session.getAttribute("appList");
		
		//ApprovalApplicaitonDAOクラスのインスタンス取得
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		
		//AcquisitionTimeクラスのインスタンス取得
		AcquisitionTime at = AcquisitionTime.getInstance();
		
		//セッションリスト情報のNULLチェック
		if(userlist == null || appList == null) {
			resp.sendRedirect("various_applications_view.jsp");
		}
		
		Integer emp_num = null, AppNum = null;
		String name = null, commuting_time = null, leave_time = null, attendance_type = null, scheduled_working_hours = null;
		String work_contents = null, comment = null, formatDate = null;
		LocalDate date;
		DateTimeFormatter dtf;
		
		//従業員番号、ユーザ名取得
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
			name = lb.getName();
		}
		
		//勤務パターン情報取得
		for(ApplicationContentsBean acb : appList) {
			work_contents = acb.getWorkContents();
			AppNum = acb.getApplicationNumber();
		}
		
		switch(work_contents.length()) {
		case 14:
			attendance_type = work_contents.substring(0, 2);
			commuting_time = work_contents.substring(3, 8);
			leave_time = work_contents.substring(9, 14);
			
			break;
			
		case 15:
			attendance_type = work_contents.substring(0, 3);
			commuting_time = work_contents.substring(4, 9);
			leave_time = work_contents.substring(10, 15);
			break;
			
		case 16:
			attendance_type = work_contents.substring(0, 4);
			commuting_time = work_contents.substring(5, 10);
			leave_time = work_contents.substring(11, 16);
			break;
			
		default:
			break;
		}
		
		scheduled_working_hours = at.calcAcquisitionBreakTime(commuting_time, leave_time);
		log.debug("scheduled_working_hours : {}", scheduled_working_hours);
		
		//取得時間文字列の先頭２文字が10以上の場合
		if(Integer.parseInt(scheduled_working_hours.substring(0, 2)) >= 10) {
		} else {
			//それ以外の場合
			scheduled_working_hours = "0" + scheduled_working_hours;
		}
		
		//実行処理内容
		String process = req.getParameter("process");
		log.debug("process : {}", process);
		
		switch(process) {
		//承認処理
		case APPROVALPROCESS:
			//パラメータ取得
			comment = req.getParameter("comment");
			
			//現在日付を取得
			date = LocalDate.now();
			dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)");
			formatDate = dtf.format(date);
			
			//処理結果格納用変数宣言
			boolean result1 = false;
			boolean result2 = false;
			
			try {
				aadao.dbConenect();
				aadao.createSt();
				//勤務時間変更処理
				result1 = aadao.updateContractTime(emp_num, commuting_time, leave_time, attendance_type, scheduled_working_hours);
				//申請内容テーブルへの承認情報登録処理
				result2 = aadao.updateApplicationContents(AppNum, APPROVAL, formatDate, name, comment);
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
				result1 = false;
				result2 = false;
			} finally {
				aadao.dbDiscnct();
			}
			
			if(result1 == true && result2 == true) {
				resp.sendRedirect("approval_rejected_completed.jsp");
			} else {
				resp.sendRedirect("approval_rejected_error.jsp");
			}
			break;
			
		//却下処理
		case REJECTIONPROCESSING:
			//パラメータ取得
			comment = req.getParameter("comment");
			
			//処理結果格納用変数宣言
			boolean result = false;
			
			//現在日付を取得
			date = LocalDate.now();
			dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)");
			formatDate = dtf.format(date);
			
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
		
		default:
			break;
		}
	}
}
