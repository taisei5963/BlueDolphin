package Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
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
import DAO.ApprovalApplicationDAO;
import DAO.KintaiDAO;

/**
 * 工数申請画面を表示させるサーブレットクラス
 *
 */

@WebServlet("/ManHourRequestViewServlet")
public class ManHourRequestViewServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ManHourRequestViewServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
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
		
		Integer emp_num = null;
		
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
		}
		
		String display_date = req.getParameter("display_date");
		log.debug("display_date : {}", display_date);
		
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		KintaiDAO kdao = KintaiDAO.getInstance();
		Calendar thisMonthCaleneder = Calendar.getInstance();
		String total_actual = "";
		
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
