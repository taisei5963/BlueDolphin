package Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private static final String NUMBERFORMATEXCEPTION = "NumberFormatException occurred";
	private static final String PARSEEXCEPTION = "ParseException occurred";

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
			//DB接続
			kinDAO.dbConenect();
			//Statement生成
			kinDAO.createSt();
			//勤怠時間リストの取得
			ArrayList<KinmuAllTimeBean> kinmuTimeList = kinDAO.getAllWorkTime(emp_num, thisMonth);
			//セッション情報としてリストを取得する
			session.setAttribute("kinmuTimeList", kinmuTimeList);
			log.info("kinmuTimeList : {}", kinmuTimeList);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
			return;
		}
		session.setAttribute("thisMonth", thisMonth);
		session.setAttribute("thisMonthCaleneder", thisMonthCaleneder);
		resp.sendRedirect("kintai_view.jsp");
	}
	
	/**
	 * 日付から曜日を取得するメソッド
	 * @param date 曜日を取得する日付
	 * @return 各曜日を格納した配列
	 */
	public String getYobi(String date) {
		
		try {
			//取得する曜日を配列で設定
			String yobi[] = {"（日）","（月）","（火）","（水）","（木）","（金）","（土）"};
			
			//年・月を取得する
		    int year = Integer.parseInt(date.substring(0,4));
		    int month = Integer.parseInt(date.substring(5,7))-1;
		    int day = Integer.parseInt(date.substring(8,10));
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    sdf.setLenient(false);
		    sdf.parse(date);
			
			//取得した年月の最終年月日を取得する
		    Calendar cal = Calendar.getInstance();
		    cal.set(year, month, day);	    
		    //YYYY-MM-DD形式にして変換して返す
		    return yobi[cal.get(Calendar.DAY_OF_WEEK)-1];
		} catch (NumberFormatException e) {
			log.error(NUMBERFORMATEXCEPTION);
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			log.error(PARSEEXCEPTION);
			e.printStackTrace();
			return null;
		}
	}
}
