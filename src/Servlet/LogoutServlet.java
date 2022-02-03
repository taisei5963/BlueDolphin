package Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * すべてのセッション情報を破棄して、当システムからのログアウト処理を行うサーブレットクラス
 *
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//ログ取得
	private static final Logger log = LogManager.getLogger(LogoutServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");

		HttpSession session = req.getSession();
		
		//セッション情報の破棄
		session.removeAttribute("process_result");
		session.removeAttribute("num");
		session.removeAttribute("name");
		session.removeAttribute("email");
		session.removeAttribute("pass1");
		session.removeAttribute("authority");
		session.removeAttribute("thisMonth");
		session.removeAttribute("syuttaikinList");
		session.removeAttribute("pro_result");
		session.removeAttribute("empList");
		session.removeAttribute("kinmuTimeList");
		session.removeAttribute("thisMonthCaleneder");
		session.removeAttribute("attendance");
		session.removeAttribute("syukkin");
		session.removeAttribute("taikin");
		session.removeAttribute("userlist");
		session.removeAttribute("process");
		session.removeAttribute("emp_num");
		session.removeAttribute("manhourcodeList");
		session.removeAttribute("manhournameList");
		session.removeAttribute("matterList");
		session.removeAttribute("thisMonthForm");
		session.removeAttribute("actual");
		session.removeAttribute("mattercodeList");
		session.removeAttribute("matternameList");
		session.removeAttribute("usernameList");
		session.removeAttribute("applicant");
		session.removeAttribute("start_application_date");
		session.removeAttribute("end_application_date");
		session.removeAttribute("start_application_time");
		session.removeAttribute("end_application_time");
		session.removeAttribute("reason_for_application");
		session.removeAttribute("work_content");
		session.removeAttribute("contact_address");
		session.removeAttribute("other_name");
		session.removeAttribute("reasonList");
		session.removeAttribute("authorizer");
		session.removeAttribute("mailAdd");
		session.removeAttribute("nameList");
		session.removeAttribute("vacation_name");
		session.removeAttribute("appList");
		session.removeAttribute("detailist");
		session.removeAttribute("rejected");
		session.removeAttribute("classification");
		
		resp.sendRedirect("/BlueDolphin/index.jsp");
		
		log.info("Logout SUCCESS");
	}
}
