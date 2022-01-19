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

import Bean.KintaiUpdateBean;
import Bean.LoginBean;
import DAO.KintaiDAO;

/**
 * 勤怠時刻更新画面を表示させるサーブレットクラス
 */
@WebServlet("/AttendanceUpdateViewServlet")
public class AttendanceUpdateViewServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AttendanceUpdateViewServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
//	private static final String NUMBERFORMATEXCEPTION = "NumberFormatException occurred";
//	private static final String PARSEEXCEPTION = "ParseException occurred";
	
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
	 * 更新画面を表示するメソッド
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
		
		//時刻変更箇所の取得
		String startTime = req.getParameter("startTime");
		log.info("startTime : {}", startTime);
		String endTime = req.getParameter("endTime");
		log.info("endTime : {}", endTime);
		String day = req.getParameter("Day");
		int date = Integer.parseInt(day);
		log.info("day : {}", day);
		
		//今月のセッション情報を取得する
		String thisMonth = (String)session.getAttribute("thisMonth");
		log.info("thisMonth : {}", thisMonth);
		
		if(1 <= date && date < 10) {
			thisMonth = thisMonth + "-0" + day;
		} else if(date >= 10) {
			thisMonth = thisMonth + "-" + day;
		}
		
		//LoginBean型のListにユーザのセッション情報を格納する
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>) session.getAttribute("userlist");
		Integer emp_num = null;
		
		//社員番号の取得
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
		}
		log.info("emp_num : {}", emp_num);
		
		//KintaiDAOクラスの唯一のインスタンス取得
		KintaiDAO kinDAO = KintaiDAO.getInstance();
		try {
			kinDAO.dbConenect();
			kinDAO.createSt();
			ArrayList<KintaiUpdateBean> syuttaikinList = kinDAO.selectWorkingTime(emp_num, thisMonth);
			session.setAttribute("syuttaikinList", syuttaikinList);
			log.info("syuttaikinList : {}", syuttaikinList);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
			return;
		}
		session.setAttribute("thisMonth", thisMonth);
		resp.sendRedirect("kintai_update_view.jsp");
	}
}
