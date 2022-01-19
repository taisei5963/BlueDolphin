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

import Bean.WorkRemotelyBean;
import DAO.WorkRemotelyDAO;

/**
 * 在宅勤務申請入力画面を表示させるサーブレットクラス
 *
 */

@WebServlet("/WorkRemotelyViewServlet")
public class WorkRemotelyViewServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(WorkRemotelyViewServlet.class);
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
		String other_name = (String)session.getAttribute("other_name");
		String applicant = (String)session.getAttribute("applicant");
		String start_application_date = (String)session.getAttribute("start_application_date");
		String end_application_date = (String)session.getAttribute("end_application_date");
		String start_application_time = (String)session.getAttribute("start_application_time");
		String end_application_time = (String)session.getAttribute("end_application_time");
		String reason_for_application = (String)session.getAttribute("reason_for_application");
		String work_content = (String)session.getAttribute("work_content");
		String contact_address = (String)session.getAttribute("contact_address");
		
		ArrayList<WorkRemotelyBean> reasonList = new ArrayList<WorkRemotelyBean>();
		WorkRemotelyDAO wrdao = WorkRemotelyDAO.getInstance();
		
		try {
			wrdao.dbConenect();
			wrdao.createSt();
			reasonList = wrdao.getWorkRemotelyReason();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(SQLEXCEPTION);
			return;
		} finally {
			wrdao.dbDiscnct();
		}
		
		session.setAttribute("other_name", other_name);
		session.setAttribute("applicant", applicant);
		session.setAttribute("reasonList", reasonList);
		session.setAttribute("start_application_date", start_application_date);
		session.setAttribute("end_application_date", end_application_date);
		session.setAttribute("start_application_time", start_application_time);
		session.setAttribute("end_application_time", end_application_time);
		session.setAttribute("reason_for_application", reason_for_application);
		session.setAttribute("work_content", work_content);
		session.setAttribute("contact_address", contact_address);
		resp.sendRedirect("work_remotely_view.jsp");
	}
}
