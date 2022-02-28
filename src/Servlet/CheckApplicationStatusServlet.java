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
 * 申請物の削除処理実行メソッド
 *
 */

@WebServlet("/CheckApplicationStatusServlet")
public class CheckApplicationStatusServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(CheckApplicationStatusServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String DELETEPROCESS = "承認取消処理";
	
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
		ArrayList<ApplicationContentsBean> appList = (ArrayList<ApplicationContentsBean>)session.getAttribute("appList");
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		String process = req.getParameter("process");
		
		String name = null;
		for(LoginBean lb: userlist) {
			name = lb.getName();
		}
		
		if(DELETEPROCESS.equals(process)){
			boolean result = false;
			String application_status = "", applicant = "";
			Integer application_number = null;
			
			for(ApplicationContentsBean acb : appList) {
				application_number = acb.getApplicationNumber();
				application_status = acb.getApplicationStatus();
				applicant = acb.getApplicant();
			}
			
			try {
				aadao.dbConenect();
				aadao.createSt();
				result = aadao.deleteApplicationContents(application_number, application_status, applicant);
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
				return;
			} finally {
				aadao.dbDiscnct();
			}
			
			if(result) {
				resp.sendRedirect("CheckApplicationStatusViewServlet");
			} else {
				resp.sendRedirect("varioous_applications_view.jsp");
			}
		} else if(process == null || process.equals("")) {
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
			resp.sendRedirect("check_application_status_view.jsp");
		}
	}
}
