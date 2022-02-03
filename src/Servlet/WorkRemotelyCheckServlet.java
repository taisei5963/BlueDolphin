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

import DAO.WorkRemotelyDAO;

/**
 * 在宅勤務申請入力内容確認画面を表示させるサーブレットクラス
 *
 */

@WebServlet("/WorkRemotelyCheckServlet")
public class WorkRemotelyCheckServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(WorkRemotelyCheckServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String PROCESS_REFERENCE = "参照処理";
	private static final String PROCESS_CONFIRMATION = "内容確認処理";
	
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
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		HttpSession session = req.getSession();
		
		String process = req.getParameter("process");
		String applicant = req.getParameter("applicant");
		String start_application_date = req.getParameter("start_application_date");
		String end_application_date = req.getParameter("end_application_date");
		String start_application_time = req.getParameter("start_application_time");
		String end_application_time = req.getParameter("end_application_time");
		String reason_for_application = req.getParameter("reason_for_application");
		String work_content = req.getParameter("work_content");
		String contact_address = req.getParameter("contact_address");
		
		log.debug("process : {}", process);
		
		WorkRemotelyDAO wrdao = WorkRemotelyDAO.getInstance();
		
		if(PROCESS_REFERENCE.equals(process)) {
			ArrayList<String> usernameList = new ArrayList<String>();
			try {
				wrdao.dbConenect();
				wrdao.createSt();
				usernameList = wrdao.getUserName(applicant);
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
				return;
			} finally {
				wrdao.dbDiscnct();
			}
			session.setAttribute("usernameList", usernameList);
			resp.sendRedirect("work_remotely.jsp");
		} else if(PROCESS_CONFIRMATION.equals(process)) {
			session.setAttribute("applicant", applicant);
			session.setAttribute("start_application_date", start_application_date);
			session.setAttribute("end_application_date", end_application_date);
			session.setAttribute("start_application_time", start_application_time);
			session.setAttribute("end_application_time", end_application_time);
			session.setAttribute("reason_for_application", reason_for_application);
			session.setAttribute("work_content", work_content);
			session.setAttribute("contact_address", contact_address);
			resp.sendRedirect("work_remotely_check.jsp");
		}
	}
}
