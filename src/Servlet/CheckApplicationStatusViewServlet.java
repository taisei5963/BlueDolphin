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
import DAO.ApprovalApplicationDAO;

/**
 * ユーザの申請状況確認画面を表示させるサーブレットクラス
 *
 */

@WebServlet("/CheckApplicationStatusViewServlet")
public class CheckApplicationStatusViewServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(CheckApplicationStatusViewServlet.class);
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
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		HttpSession session = req.getSession();
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		ArrayList<ApplicationContentsBean> appList = new ArrayList<ApplicationContentsBean>();
		
		String name = "";
		
		if(userlist == null) {
			resp.sendRedirect("various_applications_error.jsp");
		}
		
		for(LoginBean lb : userlist) {
			name = lb.getName();
		}
		
		try {
			aadao.dbConenect();
			aadao.createSt();
			appList = aadao.getApplicationContents(null, name);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
			resp.sendRedirect("various_applications_error.jsp");
		} finally {
			aadao.dbDiscnct();
		}
		
		session.setAttribute("appList", appList);
		resp.sendRedirect("check_application_status_view.jsp");
	}
}
