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
import Bean.ManHourRequestBean;
import DAO.ApplicationApproverDAO;
import DAO.ApprovalApplicationDAO;
import DAO.KintaiDAO;
import Other.ApplicationNumber;

/**
 * 工数申請処理を行うサーブレットクラス
 *
 */

@WebServlet("/ManHourRequestServlet")
public class ManHourRequestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ManHourRequestServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	private static final String APPROVALPROCESS      = "申請処理";
	private static final String APPLICATIONSTATUS    = "申請中";
	private static final String APPLICATIONNAME      = "工数申請";
	
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
	 * 勤怠関連処理の基底メソッド
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

		//セッション情報の送受信を行うための変数宣言
		HttpSession session = req.getSession();
		
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
		ArrayList<KinmuAllTimeBean> kinmuTimeList = (ArrayList<KinmuAllTimeBean>) session.getAttribute("kinmuTimeList");
		ArrayList<ManHourRequestBean> mhList = (ArrayList<ManHourRequestBean>)session.getAttribute("mhList");
		String display_date = null, total_actual = null;
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		ApplicationNumber an = ApplicationNumber.getInstance();
		ApplicationApproverDAO adao = ApplicationApproverDAO.getInstance();
		
		Integer emp_num = null;
		String name = null;
		
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
			name = lb.getName();
		}
		
		String process = req.getParameter("process");
		
		
		if(APPROVALPROCESS.equals(process)) {
			display_date = (String)session.getAttribute("display_date");
			total_actual = (String)session.getAttribute("total_actual");
			
			String comment = req.getParameter("comment");
			
			//コメントが未入力の場合は、ブランク設定
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
						//総工数
						total_actual,
						//総残業時間
						"",
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
		} else {
			display_date = req.getParameter("display_date");
			log.debug("display_date : {}", display_date);
			
			KintaiDAO kdao = KintaiDAO.getInstance();
			Calendar thisMonthCaleneder = Calendar.getInstance();
			total_actual = "";
			
			//プルダウン選択されなかった場合（初期表示）
			if(display_date == null || display_date.equals("")) {
				LocalDate now = LocalDate.now();
				if(now.getMonthValue() < 10) {
					display_date = now.getYear() + "-0" + now.getMonthValue();
				} else {
					display_date = now.getYear() + "-" + now.getMonthValue();
				}
				try {
					kdao.dbConenect();
					kdao.createSt();
					aadao.dbConenect();
					aadao.createSt();
					kinmuTimeList = kdao.getAllWorkTime(emp_num, display_date);
					total_actual = kdao.getTotalActualHours(emp_num, display_date);
					mhList = aadao.getManHourInfo(emp_num, display_date);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					resp.sendRedirect("various_applications_error.jsp");
				} finally {
					kdao.dbDiscnct();
					aadao.dbDiscnct();
				}
				
				thisMonthCaleneder.set(Calendar.YEAR, Integer.parseInt(display_date.substring(0, 4)));
				thisMonthCaleneder.set(Calendar.MONTH, Integer.parseInt(display_date.substring(5, 7)));
				
				session.setAttribute("kinmuTimeList", kinmuTimeList);
				session.setAttribute("mhList", mhList);
				session.setAttribute("thisMonthCaleneder", thisMonthCaleneder);
				session.setAttribute("total_actual", total_actual);
				session.setAttribute("display_date", display_date);
				resp.sendRedirect("man_hour_request_view.jsp");
			} else if(display_date != null) {
				System.out.println("display_date : " + display_date);
				try {
					kdao.dbConenect();
					kdao.createSt();
					aadao.dbConenect();
					aadao.createSt();
					kinmuTimeList = kdao.getAllWorkTime(emp_num, display_date);
					total_actual = kdao.getTotalActualHours(emp_num, display_date);
					mhList = aadao.getManHourInfo(emp_num, display_date);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					resp.sendRedirect("various_applications_error.jsp");
				} finally {
					kdao.dbDiscnct();
					aadao.dbDiscnct();
				}
				
				thisMonthCaleneder.set(Calendar.YEAR, Integer.parseInt(display_date.substring(0, 4)));
				thisMonthCaleneder.set(Calendar.MONTH, Integer.parseInt(display_date.substring(5, 7)));
				
				session.setAttribute("thisMonthCaleneder", thisMonthCaleneder);
				session.setAttribute("kinmuTimeList", kinmuTimeList);
				session.setAttribute("mhList", mhList);
				session.setAttribute("total_actual", total_actual);
				session.setAttribute("display_date", display_date);
				resp.sendRedirect("man_hour_request_view.jsp");
			}
		}
	}
}
