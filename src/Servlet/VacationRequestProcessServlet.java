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

import Bean.AuthorizerPropertyBean;
import Bean.LoginBean;
import DAO.ApplicationApproverDAO;
import DAO.ApplicationNumberDAO;
import DAO.ApprovalApplicationDAO;
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
	private static final String APPLICATIONSTATUS = "承認待ち";
	private static final String PAIDHOLIDAY = "年次有給休暇";
	private static final String VACATIONREQUEST = "休暇申請";
	
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
		
		String all_actual_hours = "-";
		String all_overtime_hours = "-";
		String rejection_reason = "-";
		
		log.debug("vacation_name : {}", vacation_name);
		log.debug("process : {}", process);
		log.debug("classification : {}", classification);
		log.debug("application_date : {}", application_date);
		
		if(PROCESSNAME.equals(process)) {
			String request_title = VACATIONREQUEST;
			
			//各インスタンス情報取得
			ApplicationNumber an = ApplicationNumber.getInstance();
//			SendGmail mail = SendGmail.getInstance();
			ApplicationApproverDAO adao = ApplicationApproverDAO.getInstance();
			ApplicationNumberDAO andao = ApplicationNumberDAO.getInstance();
			ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
			
			AuthorizerPropertyBean apb = new AuthorizerPropertyBean();
			ArrayList<AuthorizerPropertyBean> apList = apb.getProperty();
			String mail_add = null, mail_pass = null;
			
			//property情報の取得（承認権限が持っているユーザのメールアドレス、及びめーアドレス用のパスワード）
			for(AuthorizerPropertyBean pb : apList) {
				mail_add = pb.getMailadd();
				mail_pass = pb.getMailpass();
			}
			
			try {
				adao.dbConenect();
				adao.createSt();
				aadao.dbConenect();
				aadao.createSt();
				andao.dbConenect();
				andao.createSt();
				
				//申請者（ユーザ）名から該当の承認者名を取得
				String authorizer = adao.getAuthorizer(name);
				
				//取得した承認者名のメールアドレスを取得
				String mailAdd = adao.getAuthorizerMailAdd(authorizer);
				
				//申請番号の作成（重複チェック、データベース登録処理を含む）
				int number = an.getAppNumber(emp_num);
				
				//現在の日付及び時刻を取得
				LocalDate date = LocalDate.now();
				LocalTime time = LocalTime.now();
				DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm");
				String formatDate = dtf1.format(date);
				String formatTime = dtf2.format(time);
				
				//承認申請テーブルに申請情報を登録
				boolean result = aadao.setApprovalApplication(name, request_title, number, formatDate, formatTime, authorizer, APPLICATIONSTATUS);
				log.debug("result : {}", result);
				
				if(result) {
					if(PAIDHOLIDAY.equals(vacation_name)) {
						//単日数申請の場合
						if(date1 != null && date2 == null && application_date == null) {
							application_date = date1;
						//範囲指定申請の場合	
						} else if(date1 != null && date2 != null && application_date == null) {
							application_date = date1 + "、" + date2;
						//複数日申請の場合
						} else {}
					}
					//申請情報詳細テーブルに情報を登録
					aadao.setApprovalApplicationDetails(number, vacation_name, classification, application_date, start_time, end_time
							, all_actual_hours, all_overtime_hours, remarks, rejection_reason);
					
					//メール送信処理
				} else {
					log.error("申請情報を正常に登録できませんでした。");
					return;
				}
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
			} finally {
				adao.dbDiscnct();
				aadao.dbDiscnct();
				andao.dbDiscnct();
			}
			resp.sendRedirect("various_applications_completed.jsp");
		} else 
			if(process == null){
			session.setAttribute("vacation_name", vacation_name);
			session.setAttribute("classification", classification);
			resp.sendRedirect("VacationRequestViewServlet");
		}
	}
}
