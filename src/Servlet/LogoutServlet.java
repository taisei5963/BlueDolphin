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

		//ユーザ情報セッションを破棄する
		HttpSession session = req.getSession();

		session.removeAttribute("mail");				//メールアドレス
		session.removeAttribute("pass");				//パスワード
		session.removeAttribute("userlist");			//user情報
		session.removeAttribute("syuttaikinList");		//出退勤情報
		session.removeAttribute("thisMonth");			//今月情報
		session.removeAttribute("kinmuTimeList");		//勤務情報
		session.removeAttribute("thisMonthCalneder");	//今月のカレンダー情報
		
		resp.sendRedirect("/BlueDolphin/index.jsp");
		
		log.info("Logout SUCCESS");
	}
}
