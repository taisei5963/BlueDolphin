package Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
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

import Bean.ApprovalApplicationDetailsBean;
import Bean.LoginBean;
import DAO.ApprovalApplicationDAO;

/**
 * 各押下されたボタンに応じた処理を実行するサーブレットクラス
 */
@WebServlet("/ApprovalRejectedDetailsServlet")
public class ApprovalRejectedDetailsServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ApprovalRejectedDetailsServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String REGISTERPROCESS = "登録処理";
	private static final String REJECTIONPROCESS = "却下処理";
	private static final String APPROVALPROCESS = "承認処理";
	private static final String BACKPROCESS = "戻り処理";
	private static final String PAIDHOLIDAY = "年次有給休暇";
	
	/**
	 * doPostメソッドを呼び出すメソッド
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException サーブレットで何らかの問題が発生した場合
	 * @throws IOException 入出力処理の失敗、または割り込みの発生が起きた場合
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * 画面を表示するメソッド
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
		ArrayList<ApprovalApplicationDetailsBean> dateList = (ArrayList<ApprovalApplicationDetailsBean>)session.getAttribute("dateList");
		
		Integer emp_num = null;
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
		}
		
		String vacation_name = null;
		String request_start_date = null;
		String request_end_date = null;
		Integer application_number = null;
		for(ApprovalApplicationDetailsBean ab : dateList) {
			application_number = ab.getApplicationNumber();
			vacation_name = ab.getApplicationName();
			request_start_date = ab.getDate1();
			request_end_date = ab.getDate2();
		}
		
		//現在の日付及び時刻を取得
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm");
		String formatDate = dtf1.format(date);
		String formatTime = dtf2.format(time);
		
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		
		//パラーメータの取得
		String process = req.getParameter("process");
		log.debug("process : {}", process);
		
		String rejection_reason = req.getParameter("rejection_reason");
		
		//各処理の実行
		switch(process) {
		//戻り処理
		case BACKPROCESS:
			session.setAttribute("rejected", null);
			resp.sendRedirect("approval_rejected_view.jsp");
			break;
		//登録処理
		case REGISTERPROCESS:
			try {
				aadao.dbConenect();
				aadao.createSt();
				//却下理由をデータベースに登録
				boolean result = aadao.updateApprovalApplicationDetails(application_number, rejection_reason);
				
				if(result) {
					boolean bool = aadao.updateApprovalApplicationStatusOUT(application_number, formatDate, formatTime);
					if(bool) {
						resp.sendRedirect("approval_rejected_completed.jsp");
					} else {
						resp.sendRedirect("approval_rejected_error.jsp");
					}
				} else {
					resp.sendRedirect("approval_rejected_error.jsp");
				}
			} catch (SQLException e) {
				log.debug(SQLEXCEPTION);
				e.printStackTrace();
			} finally {
				aadao.dbDiscnct();
			}
			break;
		//却下処理
		case REJECTIONPROCESS:
			String rejected = REJECTIONPROCESS;
			session.setAttribute("rejected", rejected);
			resp.sendRedirect("approval_rejected_details_view.jsp");
			break;
		//承認処理
		case APPROVALPROCESS:
			try {
				aadao.dbConenect();
				aadao.createSt();
				if(PAIDHOLIDAY.equals(vacation_name)) {
					//有給休暇テーブルの有給情報の更新処理
					aadao.updatePaidHolidayDays(request_start_date, request_end_date, emp_num);
				}
				
				//承認情報の更新処理
				boolean result = aadao.updateApprovalApplicationStatusOK(application_number, formatDate, formatTime);
				
				//処理結果により、表示画面を確定させる
				if(result) {
					resp.sendRedirect("approval_rejected_completed.jsp");
				} else {
					resp.sendRedirect("approval_rejected_error.jsp");
				}
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
			} finally {
				aadao.dbDiscnct();
			}
			break;
		default:
			break;
		}
	}
}
