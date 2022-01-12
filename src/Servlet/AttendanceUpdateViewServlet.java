package Servlet;

import java.io.IOException;
import java.sql.SQLException;
<<<<<<< HEAD
import java.util.ArrayList;
=======
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
>>>>>>> 80cffd47c5175d81e090e931f673087ded55d4bc

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
<<<<<<< HEAD
//	private static final String NUMBERFORMATEXCEPTION = "NumberFormatException occurred";
//	private static final String PARSEEXCEPTION = "ParseException occurred";
	
=======
	private static final String NUMBERFORMATEXCEPTION = "NumberFormatException occurred";
	private static final String PARSEEXCEPTION = "ParseException occurred";

>>>>>>> 80cffd47c5175d81e090e931f673087ded55d4bc
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
<<<<<<< HEAD
=======
	
	/**
	 * 日付から曜日を取得するメソッド
	 * @param date 曜日を取得する日付
	 * @return 各曜日を格納した配列
	 */
	public String getYobi(String date) {
		log.info("getYobi start");
		log.info("date : {}", date);
		
		try {
			//取得する曜日を配列で設定
			String yobi[] = {"日曜日","月曜日","火曜日","水曜日","木曜日","金曜日","土曜日"};
			
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
		    
		    log.info("getYobi end");
		    
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
>>>>>>> 80cffd47c5175d81e090e931f673087ded55d4bc
}
