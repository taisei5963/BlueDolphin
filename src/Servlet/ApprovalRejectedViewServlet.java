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

import Bean.ApprovalApplicationBean;
import Bean.LoginBean;
import DAO.ApprovalApplicationDAO;

/**
 * 申請情報を取得し、申請却下項目選択画面を表示させるサーブレットクラス
 */
@WebServlet("/ApprovalRejectedViewServlet")
public class ApprovalRejectedViewServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ApprovalRejectedViewServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
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
		
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		ArrayList<ApprovalApplicationBean> appList = new ArrayList<ApprovalApplicationBean>();
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
		String name = null;
		for(LoginBean lb : userlist) {
			name = lb.getName();
		}
		
		//リスト情報取得
		try {
			aadao.dbConenect();
			aadao.createSt();
			appList = aadao.getApprovalApplication(name);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
		} finally {
			aadao.dbDiscnct();
		}
		
		session.setAttribute("appList", appList);
		resp.sendRedirect("approval_rejected_view.jsp");
	}
}
