package Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import Bean.ApplicationContentsBean;
import Bean.KintaiUpdateBean;
import Bean.LoginBean;
import DAO.ApprovalApplicationDAO;
import DAO.KintaiDAO;
import Other.HolidayName;

/**
 * 勤怠時刻変更内容をデータベースに登録する処理
 */
@WebServlet("/AttendanceUpdateDetailsServlet")
public class AttendanceUpdateDetailsServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AttendanceUpdateDetailsServlet.class);
	
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String APPROVALPROCESS       = "承認処理";
	private static final String REJECTIONPROCESSING   = "却下処理";
	private static final String APPROVAL = "承認";
	private static final String REJECTED = "却下";

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
	 * 勤怠時間変更申請実行メソッド
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
		
		//祝日を取得するクラスのインスタンス生成
		HolidayName holidayName = HolidayName.getInstance();
		String syukkin_hour = null, syukkin_min = null, taikin_hour = null, taikin_min = null;
		String actual = null, zangyo = null, production = null, name = null, comment = null, formatDate = null;
		Integer AppNum = null, emp_num = null;
		boolean result1 = false, result2 = false;
		LocalDate ld = null;
		DateTimeFormatter dtf = null;
		
		//リストセッション情報の取得
		ArrayList<KintaiUpdateBean> syuttaikinList = (ArrayList<KintaiUpdateBean>)session.getAttribute("syuttaikinList");
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
		ArrayList<ApplicationContentsBean> appList = (ArrayList<ApplicationContentsBean>)session.getAttribute("appList");
		String after_syukkin = null;
		String after_taikin = null;
		String remarks = null;
		
		//セッションリスト情報のNULLチェック
		if(syuttaikinList == null || userlist == null || appList == null) {
			resp.sendRedirect("KinmuhyoServlet");
		}
		
		//セッションユーザ情報リストから特定情報の取得
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
			name = lb.getName();
		}
		
		//セッション申請情報リストから特定情報の取得
		String thisMonth = null;
		for(ApplicationContentsBean acb : appList) {
			thisMonth = acb.getTargetDate();
			after_syukkin = acb.getCorrectedWorkTime();
			after_taikin = acb.getCorrectedRetreatTime();
			remarks = acb.getApplicationReason();
		}
		log.info("thisMonth : {}", thisMonth);
		
		int year = Integer.parseInt(thisMonth.substring(0, 4));
		int month = Integer.parseInt(thisMonth.substring(5, 7));
		int date = Integer.parseInt(thisMonth.substring(8, 10));
		
		//処理パラメータ取得
		String process = req.getParameter("process");
		
		//各インスタンス情報取得
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		
		//処理パラメータにより、処理を分岐させる
		switch(process) {
		//承認処理
		case APPROVALPROCESS:
			
			for(ApplicationContentsBean acb: appList) {
				AppNum = acb.getApplicationNumber();
			}
			
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, date);
			//KintaiDAOクラスの唯一のインスタンス取得
			KintaiDAO kinDAO = KintaiDAO.getInstance();
			
			try {
				kinDAO.dbConenect();
				kinDAO.createSt();
				production = kinDAO.getScheduledWorkingHours(emp_num);
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
			} finally {
				kinDAO.dbDiscnct();
			}
			
			//文字列型の所定労働時間を整数型に型キャスト
			int product_hour = Integer.parseInt(production.substring(0, 2));
			int product_min = Integer.parseInt(production.substring(3, 5));
			
			syukkin_hour = after_syukkin.substring(0, 2);
			syukkin_min = after_syukkin.substring(3, 5);
			taikin_hour = after_taikin.substring(0, 2);
			taikin_min = after_taikin.substring(3, 5);
			//更新後の実動時間を算出
			actual = calcTime(syukkin_hour, syukkin_min, taikin_hour, taikin_min);
			System.out.println("actual : " + actual);
			//算出した実動時間を整数型に型キャスト
			int act_hour = Integer.parseInt(actual.substring(0, 2));
			int act_min = Integer.parseInt(actual.substring(3, 5));
			
			//休日出勤の場合は、稼働時間をそのまま残業時間とする
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
			
			try {
				kinDAO.dbConenect();
				kinDAO.createSt();
				//出勤時刻、退勤時刻、実動時間、残業時間、備考をそれぞれ更新する
				result1 = kinDAO.WorkingTimeUpdate(emp_num, after_syukkin, after_taikin, thisMonth, actual, zangyo, remarks);
				
				if(result1) {
					//パラメータ取得
					comment = req.getParameter("comment");
					
					//現在日付を取得
					ld = LocalDate.now();
					dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)");
					formatDate = dtf.format(ld);
					
					//承認情報を申請情報テーブルに登録
					try {
						aadao.dbConenect();
						aadao.createSt();
						result2 = aadao.updateApplicationContents(AppNum, APPROVAL, formatDate, name, comment);
						if(result2) {
							resp.sendRedirect("approval_rejected_completed.jsp");
						} else {
							resp.sendRedirect("approval_rejected_error.jsp");
						}
					} catch (SQLException e) {
						log.error(SQLEXCEPTION);
						e.printStackTrace();
						resp.sendRedirect("approval_rejected_error.jsp");
					} finally {
						aadao.dbDiscnct();
					}
				} else {
					resp.sendRedirect("approval_rejected_error.jsp");
				}
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
				resp.sendRedirect("approval_rejected_error.jsp");
			} finally {
				kinDAO.dbDiscnct();
			}
			break;
		//却下処理
		case REJECTIONPROCESSING:
			
			for(ApplicationContentsBean acb: appList) {
				AppNum = acb.getApplicationNumber();
			}
			//パラメータ取得
			comment = req.getParameter("comment");
			
			//現在日付を取得
			ld = LocalDate.now();
			dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)");
			formatDate = dtf.format(ld);
			
			//却下情報を申請情報テーブルに登録
			try {
				aadao.dbConenect();
				aadao.createSt();
				result1 = aadao.updateApplicationContents(AppNum, REJECTED, formatDate, name, comment);
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
				result1 = false;
			} finally {
				aadao.dbDiscnct();
			}
			
			//更新処理結果
			if(result1) {
				resp.sendRedirect("approval_rejected_completed.jsp");
			} else {
				resp.sendRedirect("approval_rejected_error.jsp");
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 出退勤時刻から稼働時間を算出するメソッド
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
		String actual = null;
		
		int bTimeHour = 0;
		int bTimeMin = 0;
		int sTimeHour = Integer.parseInt(syukkin_hour);
		int sTimeMin = Integer.parseInt(syukkin_min);
		int eTimeHour = Integer.parseInt(taikin_hour);
		int eTimeMin = Integer.parseInt(taikin_min);
		int diffHour = (Integer.parseInt(taikin_hour) - Integer.parseInt(syukkin_hour));
		int diffMin = (Integer.parseInt(taikin_min) - Integer.parseInt(syukkin_min));
		
		//実動時間に応じた休憩時間設定
		if(diffHour <= 6 && diffMin == 0) {
			breakTime = "00:00";
			bTimeHour = Integer.parseInt(breakTime.substring(0, 2));
			bTimeMin = Integer.parseInt(breakTime.substring(3, 5));
		} else if((6 <= diffHour && diffMin != 0) && diffHour <= 7 ) {
			breakTime = "00:45";
			bTimeHour = Integer.parseInt(breakTime.substring(0, 2));
			bTimeMin = Integer.parseInt(breakTime.substring(3, 5));
		} else {
			breakTime = "01:00";
			bTimeHour = Integer.parseInt(breakTime.substring(0, 2));
			bTimeMin = Integer.parseInt(breakTime.substring(3, 5));
		}
		
		if((eTimeHour - sTimeHour - bTimeHour) < 10 && (eTimeMin - sTimeMin - bTimeMin) != 0) {
			if((eTimeMin - sTimeMin) < bTimeMin) {
				actual = "0" + ((eTimeHour - 1) - sTimeHour - bTimeHour) + ":" + ((eTimeMin + 60) - sTimeMin - bTimeMin);
			} else {
				actual = "0" + (eTimeHour - sTimeHour - bTimeHour) + ":" + (eTimeMin - sTimeMin - bTimeMin);
			}
		} else if((eTimeHour - sTimeHour - bTimeHour) < 10 && (eTimeMin - sTimeMin - bTimeMin) == 0){
			actual = "0" + (eTimeHour - sTimeHour - bTimeHour) + ":" + (eTimeMin - sTimeMin - bTimeMin) + "0";
		} else if((eTimeHour - sTimeHour - bTimeHour) >= 10 && (eTimeMin - sTimeMin - bTimeMin) != 0) {
			if((eTimeMin - sTimeMin) < bTimeMin) {
				actual = ((eTimeHour - 1) - sTimeHour - bTimeHour) + ":" + ((eTimeMin + 60) - sTimeMin - bTimeMin);
			} else {
				actual = (eTimeHour - sTimeHour - bTimeHour) + ":" + (eTimeMin - sTimeMin - bTimeMin);
			}
		} else {
			actual = (eTimeHour - sTimeHour - bTimeHour) + ":" + (eTimeMin - sTimeMin - bTimeMin) + "0";
		}
		
		log.info("actual : {}", actual);
		log.info("calcTime end");	
		return actual;
	}
}
