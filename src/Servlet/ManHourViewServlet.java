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

import DAO.ManHourDAO;

/**
 * 工数情報を取得し、「工数実績登録画面」を表示させるサーブレットクラス
 *
 */

@WebServlet("/ManHourViewServlet")
public class ManHourViewServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ManHourViewServlet.class);
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
		HttpSession session = req.getSession();
		//案件情報を格納するためのリストの変数宣言
		ArrayList<Integer> manhourcodeList = new ArrayList<Integer>();
		ArrayList<String> manhournameList = new ArrayList<String>();
		
		//実動時間を格納するための変数宣言
		String actual = "00:0:00";
		
		//選択した日付を取得
		String day = req.getParameter("Day");
		log.info("day : {}", day);
		int date = Integer.parseInt(day);
		
		//今月のセッション情報を取得する
		String thisMonth = (String)session.getAttribute("thisMonth");
		log.info("thisMonth : {}", thisMonth);
		int year = Integer.parseInt(thisMonth.substring(0, 4));
		int month = Integer.parseInt(thisMonth.substring(5, 7));
		String thisMonthForm = "";
		
		thisMonth = year + "年" + month + "月" + date + "日";
		if(month < 10) {
			if(date < 10) {
				thisMonthForm = year + "-0" + month + "-0" + date;
			} else {
				thisMonthForm = year + "-0" + month + "-" + date;
			}
		} else {
			if(date < 10) {
				thisMonthForm = year + "-" + month + "-0" + date;
			} else {
				thisMonthForm = year + "-" + month + "-" + date;
			}
		}
		
		//DAOクラスのインスタンス宣言
		ManHourDAO mhDao = ManHourDAO.getInstance();
		try {
			mhDao.dbConenect();
			mhDao.createSt();
			manhourcodeList = mhDao.getManHourCode();
			manhournameList = mhDao.getManHourName();
			actual = mhDao.getActual(thisMonthForm);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
			return;
		} finally {
			mhDao.dbDiscnct();
		}
		session.setAttribute("manhourcodeList", manhourcodeList);
		session.setAttribute("manhournameList", manhournameList);
		session.setAttribute("thisMonth", thisMonth);
		session.setAttribute("thisMonthForm", thisMonthForm);
		session.setAttribute("actual", actual);
		resp.sendRedirect("man_hour.jsp");
	}

	/**
	 * 勤怠関連処理の基底メソッド
	 * @param req クライアントが Servlet へ要求したリクエスト内容を含む HttpServletRequest オブジェクト
	 * @param resp Servlet がクライアントに返すレスポンス内容を含む HttpServletResponse オブジェクト
	 * @throws ServletException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 * @throws IOException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
