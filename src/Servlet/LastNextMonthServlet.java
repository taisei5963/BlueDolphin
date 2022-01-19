package Servlet;

import java.io.IOException;
import java.sql.SQLException;
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
 * 「前月・今月」リンク押下時の勤怠情報を画面に送信するサーブレットクラス
 */
@WebServlet("/LastNextMonthServlet")
public class LastNextMonthServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(LastNextMonthServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String NULLPOINTEREXCEPTION = "NullPointerException occurred";
	
	//処理名称宣言
	private static final String PreviousMonthProcessing = "前月処理";
	private static final String NextMonthProcessing = "翌月処理";
	

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
	 * 勤務表を表示するメソッド
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
		
		//リンク情報の取得
		String choice = req.getParameter("ChoiceMonth");
		String thisMonth = (String)session.getAttribute("thisMonth");
		log.debug("choice : {}", choice);
		log.debug("thisMonth : {}", thisMonth);
		
		//日付情報のNULLチェック
		if(thisMonth == null) {
			log.error("thisMonth is NULL");
			throw new NullPointerException(NULLPOINTEREXCEPTION);
		}
		
		//今月から「年」・「月」をそれぞれ取得する
		int year = Integer.parseInt(thisMonth.substring(0, 4));
		int month = Integer.parseInt(thisMonth.substring(5, 7));
		
		Calendar thisMonthCaleneder = Calendar.getInstance();
		
		//前月処理
		if(PreviousMonthProcessing.equals(choice)) {
			switch(year) {
				case 2021:
					if(month == 1) {
						year = year - 1;
						month = 12;
						thisMonth = String.valueOf(year) + "-" + String.valueOf(month);
					} else {
						if(2 <= month && month < 11) {
							month = month - 1;
							thisMonth = String.valueOf(year) + "-0" + String.valueOf(month);
						} else {
							month = month - 1;
							thisMonth = String.valueOf(year) + "-" + String.valueOf(month);
						}
					}
					break;
				case 2022:
					if(month == 1) {
						year = year - 1;
						month = 12;
						thisMonth = String.valueOf(year) + "-" + String.valueOf(month);
					} else {
						if(2 <= month && month < 11) {
							month = month - 1;
							thisMonth = String.valueOf(year) + "-0" + String.valueOf(month);
						} else {
							month = month - 1;
							thisMonth = String.valueOf(year) + "-" + String.valueOf(month);
						}
					}
					break;
				case 2023:
					if(month == 1) {
						year = year - 1;
						month = 12;
						thisMonth = String.valueOf(year) + "-" + String.valueOf(month);
					} else {
						if(2 <= month && month < 11) {
							month = month - 1;
							thisMonth = String.valueOf(year) + "-0" + String.valueOf(month);
						} else {
							month = month - 1;
							thisMonth = String.valueOf(year) + "-" + String.valueOf(month);
						}
					}
					break;
			}
		} 
		//今月処理
		if(NextMonthProcessing.equals(choice)) {
			switch(year) {
				case 2021:
					if(month == 12) {
						year = year + 1;
						month = 1;
						thisMonth = String.valueOf(year) + "-0" + String.valueOf(month);
					} else {
						if(1 <= month && month < 9) {
							month = month + 1;
							thisMonth = String.valueOf(year) + "-0" + String.valueOf(month);
						} else {
							month = month + 1;
							thisMonth = String.valueOf(year) + "-" + String.valueOf(month);
						}
					}
					break;
				case 2022:
					if(month == 12) {
						year = year + 1;
						month = 1;
						thisMonth = String.valueOf(year) + "-0" + String.valueOf(month);
					} else {
						if(1 <= month && month < 9) {
							month = month + 1;
							thisMonth = String.valueOf(year) + "-0" + String.valueOf(month);
						} else {
							month = month + 1;
							thisMonth = String.valueOf(year) + "-" + String.valueOf(month);
						}
					}
					break;
				case 2023:
					if(month == 12) {
						year = year + 1;
						month = 1;
						thisMonth = String.valueOf(year) + "-0" + String.valueOf(month);
					} else {
						if(1 <= month && month < 9) {
							month = month + 1;
							thisMonth = String.valueOf(year) + "-0" + String.valueOf(month);
						} else {
							month = month + 1;
							thisMonth = String.valueOf(year) + "-" + String.valueOf(month);
						}
					}
					break;
			}
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
