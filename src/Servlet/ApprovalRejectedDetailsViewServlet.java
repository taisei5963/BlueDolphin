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

import Bean.ApprovalApplicationDetailsBean;
import DAO.ApprovalApplicationDAO;

/**
 * 申請内容詳細画面を表示させるサーブレットクラス
 */
@WebServlet("/ApprovalRejectedDetailsViewServlet")
public class ApprovalRejectedDetailsViewServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ApprovalRejectedDetailsViewServlet.class);
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
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");

		//セッション情報の送受信を行うための変数宣言
		HttpSession session = req.getSession();
		
		int application_number = Integer.parseInt(req.getParameter("AppNumber"));
		
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		ArrayList<ApprovalApplicationDetailsBean> dateList = new ArrayList<ApprovalApplicationDetailsBean>();
		
		try {
			aadao.dbConenect();
			aadao.createSt();
			dateList = aadao.getApprovalApplicationDetails(application_number);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
		} finally {
			aadao.dbDiscnct();
		}
		
		session.setAttribute("dateList", dateList);
		resp.sendRedirect("approval_rejected_details_view.jsp");
	}
}
