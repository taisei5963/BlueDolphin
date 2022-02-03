package Servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import DAO.LoginDAO;

/**
 * パスワード変更処理を行うサーブレットクラス
 */
@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ChangePasswordServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String ERRORWORDINNG1 = "Not match password";
	private static final String ERRORWORDINNG2 = "Sorry wodun't change";

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
	 * 更新処理を表示するメソッド
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException サーブレットで何らかの問題が発生した場合
	 * @throws IOException 入出力処理の失敗、または割り込みの発生が起きた場合
	 *
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		String email = req.getParameter("email");
		String new_pass1 = req.getParameter("new_password1");
		String new_pass2 = req.getParameter("new_password2");
		
		boolean result = false;
		
		if(!new_pass1.equals(new_pass2)) {
			log.error(ERRORWORDINNG1);
			resp.sendRedirect("match_password_error.jsp");
			return;
		} else {
			LoginDAO dao = LoginDAO.getInstance();
			try {
				dao.dbConnect();
				dao.createSt();
				result = dao.setPassword(email, new_pass1);
				log.info("result : {}", result);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(SQLEXCEPTION);
				return;
			} finally {
				dao.dbDiscnct();
			}
		}
		if(result) {
			resp.sendRedirect("change_password_finish.jsp");
		} else {
			log.error(ERRORWORDINNG2);
			resp.sendRedirect("change_password_error.jsp");
		}
	}
}
