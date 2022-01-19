package Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Bean.KinmuTimeBean;
import Bean.LoginBean;
import DAO.KintaiDAO;
import Other.HolidayName;

/**
 * 勤怠登録完了画面を表示し、勤怠情報をMySQLに登録するサーブレットクラス
 *
 */

@WebServlet("/KintaiServlet")
public class KintaiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(KintaiServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";

	/**
	 * doPostメソッドを呼び出すメソッド
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException サーブレットで問題が発生したとき
	 * @throws IOException 入出力処理の失敗、または割り込みの発生が起きたとき
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * 勤怠関連の処理を行うメソッド
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException サーブレットで問題が発生したとき
	 * @throws IOException 入出力処理の失敗、または割り込みの発生が起きたとき
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");

		//セッション情報の送受信を行うための変数宣言
		HttpSession session = req.getSession();
		//LoginBean型のListにユーザのセッション情報を格納する
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>) session.getAttribute("userlist");
		
		//祝日を取得するクラスのインスタンス生成
		HolidayName holidayName = HolidayName.getInstance();
		Calendar cal = Calendar.getInstance();
		LocalDateTime now = LocalDateTime.now();
		cal.set(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
		
		//KintaiDAOクラスの唯一のインスタンスを取得
		KintaiDAO kintaiDAO = KintaiDAO.getInstance();
		
		Integer emp_num = null;
		String production = null;
		int act_hour = 0, act_min = 0;
		
		//ユーザのセッション情報から「メールアドレス」、「実動時間」を取得
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
		}
		try {
			kintaiDAO.dbConenect();
			kintaiDAO.createSt();
			production = kintaiDAO.getStandardTime(emp_num);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
		} finally {
			kintaiDAO.dbDiscnct();
		}
		
		//文字列型の所定労働時間を整数型に型キャスト
		int product_hour = Integer.parseInt(production.substring(0, 2));
		int product_min = Integer.parseInt(production.substring(3, 5));
		
		//勤怠登録画面にてどのボタンが押されたか判断する
		String attendance = req.getParameter("attendance");
		log.info("attendance : {}", attendance);
		
		//勤怠情報が登録されたかを判断するための変数宣言
		boolean result = false;
		String syukkin_hour = null, syukkin_min = null, taikin_hour = null, taikin_min = null;
		String actual = null, zangyo = null;
		
		//実働時間、残業時間算出用リストの変数宣言
		List<KinmuTimeBean> kinmuTimeList = new ArrayList<KinmuTimeBean>();
		
		try {
			kintaiDAO.dbConenect();
			kintaiDAO.createSt();
			if(attendance.equals("出勤処理")) {
				result = kintaiDAO.setSyukkinTime(emp_num);
			} else if(attendance.equals("退勤処理")) {
				//退勤時間をデータベースに登録し、登録結果を受け取る
				result = kintaiDAO.setTaikinTime(emp_num);
				//出勤時間と退勤時間をリスト型で受け取る
				kinmuTimeList = kintaiDAO.getWorkTime(emp_num);
				for(KinmuTimeBean ktBean : kinmuTimeList) {
					syukkin_hour = ktBean.getWorkStartTime().substring(0, 2);
					syukkin_min = ktBean.getWorkStartTime().substring(3, 5);
					taikin_hour = ktBean.getWorkEndTime().substring(0, 2);
					taikin_min = ktBean.getWorkEndTime().substring(3, 5);
				}
				//実動時間算出処理
				actual = calcTime(syukkin_hour, syukkin_min, taikin_hour, taikin_min);
				//算出した実動時間を整数型に型キャスト
				if(Integer.parseInt(actual.substring(0, 1)) < 10) {
					act_hour = Integer.parseInt(actual.substring(0, 1));
					act_min = Integer.parseInt(actual.substring(2, 4));
				} else {
					act_hour = Integer.parseInt(actual.substring(0, 2));
					act_min = Integer.parseInt(actual.substring(3, 5));
				}
				
				//算出した実動時間をデータベースに登録し、登録結果を受け取る
				result = kintaiDAO.setActualTime(emp_num, actual);
				
				//休日出勤の場合は、稼働時間をそのまま残業時間とする
				if(!holidayName.getHolidayName(cal).equals("平日")) {
					zangyo = actual;
				} else {
					//実動時間が契約時間よりも大きい場合は、残業時間を差分として算出する
					if(act_hour > product_hour) {
						zangyo = (act_hour - product_hour) + ":" + (act_min - product_min);
					//実動時間が契約時間と同じか少ない場合は、残業時間をなしとして算出する
					} else {
						zangyo = "00:00";
					}
				}
				log.info("holidayName : {}", holidayName.getHolidayName(cal));
				//残業時間をデータベースに登録し、登録結果を受け取る
				result = kintaiDAO.setZangyoTime(emp_num, zangyo);
			}
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
		} finally {
			kintaiDAO.dbDiscnct();
		}
		log.info("result : {}", result);
		
		if(result) {
			session.setAttribute("attendance", attendance);
			resp.sendRedirect("kintai_finish.jsp");
		} else {
			resp.sendRedirect("kintai_input_error.jsp");
		}
	}
	
	/**
	 * データベースから取得した出退勤時刻から稼働時間を算出するメソッド
	 * @param syukkin_hour 出勤時間の「時」
	 * @param syukkin_min 出勤時間の「分」
	 * @param taikin_hour 退勤時間の「時」
	 * @param taikin_min 退勤時間の「分」
	 * @return 稼働時間（60進数を10進数に変換）
	 */
	public String calcTime(String syukkin_hour, String syukkin_min, String taikin_hour, String taikin_min) {
		log.info("calcTime start");
		
		//休憩時間を格納するための変数宣言
		String breakTime = "01:00";
		
		//休憩時間を「時」と「分」に分ける
		int bTimeHour = 0;
		int bTimeMin = 0;
		int sTimeHour = Integer.parseInt(syukkin_hour);
		int sTimeMin = Integer.parseInt(syukkin_min);
		int eTimeHour = Integer.parseInt(taikin_hour);
		int eTimeMin = Integer.parseInt(taikin_min);
		
		if((eTimeHour - sTimeHour) <= 6) {
			breakTime = "00:00";
			bTimeHour = Integer.parseInt(breakTime.substring(0, 2));
			bTimeMin = Integer.parseInt(breakTime.substring(3, 5));
		} else if(6 < (eTimeHour - sTimeHour) && (eTimeHour - sTimeHour) <= 7 && (eTimeMin - sTimeMin) < 30) {
			breakTime = "00:45";
			bTimeHour = Integer.parseInt(breakTime.substring(0, 2));
			bTimeMin = Integer.parseInt(breakTime.substring(3, 5));
		} else {
			bTimeHour = Integer.parseInt(breakTime.substring(0, 2));
			bTimeMin = Integer.parseInt(breakTime.substring(3, 5));
		}
		
		String actual = (eTimeHour - sTimeHour - bTimeHour) + ":" + (eTimeMin - sTimeMin - bTimeMin);
		log.info("actual : {}", actual);
		log.info("calcTime end");	
		return actual;
	}
}
