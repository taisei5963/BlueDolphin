package Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Bean.KinmuAllTimeBean;
import Bean.LoginBean;
import DAO.KintaiDAO;

/**
 * 「今月」の情報を受け取り、社員勤怠情報を画面に送信するサーブレットクラス
 */
@WebServlet("/KinmuhyoServlet")
public class KinmuhyoServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(KinmuhyoServlet.class);
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
	 * 勤務表を表示するクラス
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
		
		//選択したプルダウン情報を取得する
		String thisMonth = req.getParameter("thisMonth");
		log.info("thisMonth : {}", thisMonth);
		
		//日付・時刻の取得
		LocalDateTime now = LocalDateTime.now();
		String year = null;
		String month = null;
		
		Calendar thisMonthCaleneder = Calendar.getInstance();
		
		if(thisMonth == null && now.getMonthValue() < 10) {
			year = String.valueOf(now.getYear());
			month = String.valueOf(now.getMonthValue());
			thisMonth = year + "-0" + month;
		} else if(thisMonth == null && now.getMonthValue() >= 10) {
			year = String.valueOf(now.getYear());
			month = String.valueOf(now.getMonthValue());
			thisMonth = year + "-" + month;
		}
		
		thisMonthCaleneder.set(Calendar.YEAR, Integer.parseInt(thisMonth.substring(0, 4)));
		thisMonthCaleneder.set(Calendar.MONTH, Integer.parseInt(thisMonth.substring(5, 7)));
		
		//LoginBean型のListにユーザのセッション情報を格納する
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>) session.getAttribute("userlist");
		Integer emp_num = null;
		//社員番号の取得
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
		}
		
		//KintaiDAOクラスの唯一のインスタンス取得
		KintaiDAO kinDAO = KintaiDAO.getInstance();
		try {
			kinDAO.dbConenect();
			kinDAO.createSt();
			ArrayList<KinmuAllTimeBean> kinmuTimeList = kinDAO.getAllWorkTime(emp_num, thisMonth);
			session.setAttribute("kinmuTimeList", kinmuTimeList);
			log.info("kinmuTimeList : {}", kinmuTimeList);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
			return;
		} finally {
			kinDAO.dbDiscnct();
		}
		session.setAttribute("thisMonth", thisMonth);
		session.setAttribute("thisMonthCaleneder", thisMonthCaleneder);
		resp.sendRedirect("kintai_view.jsp");
	}
}
