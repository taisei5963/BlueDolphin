package Servlet;

import java.io.IOException;
import java.sql.SQLException;
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
import Bean.ManHourRequestBean;
import DAO.ApprovalApplicationDAO;

/**
 * 申請詳細画面を表示させるサーブレットクラス
 */
@WebServlet("/ApprovalRejectedServlet")
public class ApprovalRejectedServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ApprovalRejectedServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION            = "SQLException occurred";
	
	//申請区分用定数宣言
	private static final String VACATIONREQUEST             = "休暇申請";
	private static final String MANHOURREQUEST              = "工数申請";
	private static final String WORKINGFROMHOUSEREQUEST     = "在宅勤務申請";
	private static final String ATTENDANCEREQUEST           = "勤怠申請";
	private static final String HOLIDAYWORKREQUEST          = "休日出勤申請";
	private static final String OVERTIMEHOURSREQUEST        = "残業申請";
	private static final String EARLYOVERTIMEREQUEST        = "早出申請";
	private static final String CHANGEWORKINGHOURSREQUEST   = "勤務時間変更申請";
	private static final String BEHINDTIMEREQUEST           = "遅刻申請";
	private static final String LEAVEEARLYREQEST            = "早退申請";
	private static final String DIRECTBOUNCEREQUEST         = "直行直帰申請";
	private static final String ATTENDANCETIMECHANGEREQUEST = "勤怠時刻変更申請";
	
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
		
		//セッションリスト情報を取得
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
		Integer AppNum = null;
		
		if(req.getParameter("AppNum") != null) {
			AppNum = Integer.parseInt(req.getParameter("AppNum"));
		} 
		String AppCategory = req.getParameter("AppCategory");
		log.debug("AppCategory : {}", AppCategory);
		
		ArrayList<ApplicationContentsBean> appList = new ArrayList<ApplicationContentsBean>();
		ArrayList<ManHourRequestBean> mhList = new ArrayList<ManHourRequestBean>();
		
		String name = null;
		Integer emp_num = null;
		for(LoginBean lb: userlist) {
			name = lb.getName();
			emp_num = lb.getNumber();
		}
		
		//DAOクラスのインスタンス取得
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		
		if(AppNum != null && AppCategory != null) {
			switch(AppCategory) {
			//休暇申請
			case VACATIONREQUEST:
				try {
					aadao.dbConenect();
					aadao.createSt();
					appList = aadao.getApplicationAppNumContents(AppNum);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					aadao.dbDiscnct();
				}
				session.setAttribute("appList", appList);
				resp.sendRedirect("approval_rejected_details_view.jsp");
				break;
			//工数申請
			case MANHOURREQUEST:
				String target_date = "";
				try {
					aadao.dbConenect();
					aadao.createSt();
					appList = aadao.getApplicationAppNumContents(AppNum);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					aadao.dbDiscnct();
				}
				
				//対象日の情報を取得
				for(ApplicationContentsBean acb : appList) {
					target_date = acb.getTargetDate();
				}
				if(Integer.parseInt(target_date.substring(5, 7)) < 10) {
					target_date = target_date.substring(0, 4) + "-0" + target_date.substring(5, 7);
				} else {
					target_date = target_date.substring(0, 4) + "-" + target_date.substring(5, 7);
				}
				
				try {
					aadao.dbConenect();
					aadao.createSt();
					mhList = aadao.getManHourInfo(emp_num, target_date);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					resp.sendRedirect("various_applications_error.jsp");
				} finally {
					aadao.dbDiscnct();
				}
				
				session.setAttribute("appList", appList);
				session.setAttribute("mhList", mhList);
				resp.sendRedirect("man_hour_request_details_view.jsp");
				
				break;
			//在宅勤務申請
			case WORKINGFROMHOUSEREQUEST:
				try {
					aadao.dbConenect();
					aadao.createSt();
					appList = aadao.getApplicationAppNumContents(AppNum);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					aadao.dbDiscnct();
				}
				session.setAttribute("appList", appList);
				resp.sendRedirect("work_remotely_details_view.jsp");
				break;
			//勤怠申請
			case ATTENDANCEREQUEST:
				try {
					aadao.dbConenect();
					aadao.createSt();
					appList = aadao.getApplicationAppNumContents(AppNum);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					aadao.dbDiscnct();
				}
				session.setAttribute("appList", appList);
				resp.sendRedirect("time_and_attendance_request_details_view.jsp");
				break;
			//休日出勤申請
			case HOLIDAYWORKREQUEST:
				try {
					aadao.dbConenect();
					aadao.createSt();
					appList = aadao.getApplicationAppNumContents(AppNum);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					aadao.dbDiscnct();
				}
				session.setAttribute("appList", appList);
				resp.sendRedirect("holiday_work_details_view.jsp");
				break;
			//残業申請
			case OVERTIMEHOURSREQUEST:
			//早出申請
			case EARLYOVERTIMEREQUEST:
				try {
					aadao.dbConenect();
					aadao.createSt();
					appList = aadao.getApplicationAppNumContents(AppNum);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					aadao.dbDiscnct();
				}
				session.setAttribute("appList", appList);
				resp.sendRedirect("overtime_request_details_view.jsp");
				break;
			//勤務時間変更申請
			case CHANGEWORKINGHOURSREQUEST:
				try {
					aadao.dbConenect();
					aadao.createSt();
					appList = aadao.getApplicationAppNumContents(AppNum);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					aadao.dbDiscnct();
				}
				session.setAttribute("appList", appList);
				resp.sendRedirect("change_working_hours_request_details_view.jsp");
				break;
			//遅刻申請
			case BEHINDTIMEREQUEST:
				try {
					aadao.dbConenect();
					aadao.createSt();
					appList = aadao.getApplicationAppNumContents(AppNum);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					aadao.dbDiscnct();
				}
				session.setAttribute("appList", appList);
				resp.sendRedirect("behind_time_request_details_view.jsp");
				break;
			//早退申請
			case LEAVEEARLYREQEST:
				try {
					aadao.dbConenect();
					aadao.createSt();
					appList = aadao.getApplicationAppNumContents(AppNum);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					aadao.dbDiscnct();
				}
				session.setAttribute("appList", appList);
				resp.sendRedirect("leave_early_request_details_view.jsp");
				break;
			//直行直帰申請
			case DIRECTBOUNCEREQUEST:
				try {
					aadao.dbConenect();
					aadao.createSt();
					appList = aadao.getApplicationAppNumContents(AppNum);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					aadao.dbDiscnct();
				}
				session.setAttribute("appList", appList);
				resp.sendRedirect("direct_bounce_request_details_view.jsp");
				break;
			//勤怠時刻変更申請
			case ATTENDANCETIMECHANGEREQUEST:
				try {
					aadao.dbConenect();
					aadao.createSt();
					appList = aadao.getApplicationAppNumContents(AppNum);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					aadao.dbDiscnct();
				}
				session.setAttribute("appList", appList);
				resp.sendRedirect("attendance_update_details_view.jsp");
				break;
			default:
				break;
			}
		} else if(AppNum == null || AppCategory == null){
			//パラメータ取得
			String status = req.getParameter("status");
			try {
				aadao.dbConenect();
				aadao.createSt();
				appList = aadao.getApplicationStatusContents(status, name, null);
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
				return;
			} finally {
				aadao.dbDiscnct();
			}
			session.setAttribute("status", status);
			session.setAttribute("appList", appList);
			resp.sendRedirect("approval_rejected_view.jsp");
		}
	}
}
