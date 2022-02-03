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

import Bean.LoginBean;
import Bean.PaidHolidayBean;
import Bean.VacationRequestBean;
import DAO.VacationRequestDAO;

/**
 * 休暇申請入力画面を表示させるサーブレットクラス
 *
 */

@WebServlet("/VacationRequestViewServlet")
public class VacationRequestViewServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(VacationRequestViewServlet.class);
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
		String vacation_name = (String)session.getAttribute("vacation_name");
		
		if(userlist == null){
			resp.sendRedirect("LogoutServlet");
		}
		
		if(vacation_name == null || vacation_name.equals("")) {
			vacation_name = "年次有給休暇";
		}
		
		Integer emp_num = null;
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
		}
		
		ArrayList<VacationRequestBean> nameList = new ArrayList<VacationRequestBean>();
		ArrayList<PaidHolidayBean> phList = new ArrayList<PaidHolidayBean>();
		VacationRequestDAO vrdao = VacationRequestDAO.getInstance();
		
		try {
			vrdao.dbConenect();
			vrdao.createSt();
			nameList = vrdao.getVacationRequest();
			phList = vrdao.getPaidHolidayInfo(emp_num);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
			return;
		} finally {
			vrdao.dbDiscnct();
		}
		
		session.setAttribute("nameList", nameList);
		session.setAttribute("phList", phList);
		session.setAttribute("vacation_name", vacation_name);
		resp.sendRedirect("vacation_request_view.jsp");
	}
}
