package Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import DAO.ApplicationApproverDAO;
import DAO.ApprovalApplicationDAO;
import Other.ApplicationNumber;

/**
 * 勤怠時刻変更内容をデータベースに登録する処理
 */
@WebServlet("/AttendanceUpdateServlet")
public class AttendanceUpdateServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AttendanceUpdateServlet.class);
	
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	//申請文言用定数宣言
	private static final String CHANGECOMMUTINGTIMEREQUEST             = "出勤時刻変更";
	private static final String CHANGEOFLEAVINGTIMEREQUEST             = "退勤時刻変更";
	private static final String CHANGETIMEOFARRIVALANDDEPARTUREREQUEST = "出退勤時刻変更";
	private static final String ATTENDANCETIMECHANGEAPPLICATION        = "勤怠時刻変更申請";
	private static final String APPLICATIONSTATUS                      = "申請中";
	
	//処理文言用定数宣言
	private static final String APPLICATIONPROCESSING = "申請処理";

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
		
		//本クラスで使用する各変数宣言(初期化済み)
		String before_syukkin = null, before_taikin = null;
		String application_name = null, name = null, formatDate = null;
		LocalDate ld = null;
		DateTimeFormatter dtf = null;
		Integer emp_num = null;
		boolean result = false;
		
		//セッション情報の取得
		String thisMonth = (String)session.getAttribute("thisMonth");
		log.info("thisMonth : {}", thisMonth);
		
		//リストセッション情報の取得
		ArrayList<KintaiUpdateBean> syuttaikinList = (ArrayList<KintaiUpdateBean>)session.getAttribute("syuttaikinList");
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
		
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
			name = lb.getName();
		}
		
		//修正前出退勤リスト情報のNULLチェック
		if(syuttaikinList == null) {
			resp.sendRedirect("KinmuhyoServlet");
		}
		
		//修正前出退勤時刻の取得
		for(KintaiUpdateBean kub : syuttaikinList) {
			before_syukkin = kub.getSyukkin();
			before_taikin = kub.getTaikin();
		}
		
		log.debug("before_syukkin : {}", before_syukkin);
		log.debug("before_taikin : {}", before_taikin);
		
		//処理パラメータ取得
		String process = req.getParameter("process");
		
		//時刻変更箇所の取得
		String after_syukkin = req.getParameter("SyukkinTime");
		String after_taikin = req.getParameter("TaikinTime");
		String remarks = req.getParameter("Remarks");
		
		//変更した箇所により申請名称の内容を変更する
		if(before_syukkin == null && before_taikin == null) {
			application_name = CHANGETIMEOFARRIVALANDDEPARTUREREQUEST;
			before_syukkin = "00:00";
			before_taikin = "00:00";
		} else if(before_syukkin == null && before_taikin != null) {
			if(before_taikin.equals(after_taikin)) {
				application_name = CHANGECOMMUTINGTIMEREQUEST;
				before_syukkin = "00:00";
			} else {
				application_name = CHANGETIMEOFARRIVALANDDEPARTUREREQUEST;
				before_syukkin = "00:00";
			}
		} else if(before_taikin == null && before_syukkin != null) {
			if(before_syukkin.equals(after_syukkin)) {
				application_name = CHANGEOFLEAVINGTIMEREQUEST;
				before_taikin = "00:00";
			} else {
				application_name = CHANGETIMEOFARRIVALANDDEPARTUREREQUEST;
				before_taikin = "00:00";
			}
		} else {}
		
		//各インスタンス情報取得
		ApplicationNumber an = ApplicationNumber.getInstance();
		ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
		ApplicationApproverDAO adao = ApplicationApproverDAO.getInstance();
		
		//処理パラメータにより、処理を分岐させる
		switch(process) {
		//申請処理
		case APPLICATIONPROCESSING:
			
			//現在日付を取得
			ld = LocalDate.now();
			dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)");
			formatDate = dtf.format(ld);
			
			try {
				adao.dbConenect();
				aadao.dbConenect();
				adao.createSt();
				aadao.createSt();
				
				//申請者に該当する承認者を取得
				String corresponding_person = adao.getAuthorizer(name);
				
				//承認申請テーブルに申請情報を登録
				if(CHANGECOMMUTINGTIMEREQUEST.equals(application_name)) {
					result = aadao.setApplicationContents(
							//申請番号
							an.getAppNumber(emp_num),
							//申請日
							formatDate,
							//申請者
							name, 
							//申請名
							application_name,
							//申請種類
							"",
							//申請区分
							ATTENDANCETIMECHANGEAPPLICATION,
							//申請状況
							APPLICATIONSTATUS,
							//対象日
							thisMonth,
							//申請理由
							remarks, 
							//取得時間
							"",
							//振休取得日
							"",
							//総稼働時間
							"",
							//総残業時間
							"",
							//承認日
							"", 
							//承認者
							corresponding_person, 
							//申請者コメント
							"出勤時刻を" + before_syukkin + "から" + after_syukkin + "へ修正", 
							//承認者コメント
							"", 
							//作業内容
							"", 
							//連絡先
							"", 
							//作業時間
							"",
							//修正後出勤時刻
							after_syukkin,
							//修正後退勤時刻
							after_taikin
							);
				} else if(CHANGEOFLEAVINGTIMEREQUEST.equals(application_name)) {
					result = aadao.setApplicationContents(
							//申請番号
							an.getAppNumber(emp_num),
							//申請日
							formatDate,
							//申請者
							name, 
							//申請名
							application_name,
							//申請種類
							"",
							//申請区分
							ATTENDANCETIMECHANGEAPPLICATION,
							//申請状況
							APPLICATIONSTATUS,
							//対象日
							thisMonth,
							//申請理由
							remarks, 
							//取得時間
							"",
							//振休取得日
							"",
							//総稼働時間
							"",
							//総残業時間
							"",
							//承認日
							"", 
							//承認者
							corresponding_person, 
							//申請者コメント
							"退勤時刻を" + before_taikin + "から" + after_taikin + "へ修正", 
							//承認者コメント
							"", 
							//作業内容
							"", 
							//連絡先
							"", 
							//作業時間
							"",
							//修正後出勤時刻
							after_syukkin,
							//修正後退勤時刻
							after_taikin
							);
				} else {
					result = aadao.setApplicationContents(
							//申請番号
							an.getAppNumber(emp_num),
							//申請日
							formatDate,
							//申請者
							name, 
							//申請名
							application_name,
							//申請種類
							"",
							//申請区分
							ATTENDANCETIMECHANGEAPPLICATION,
							//申請状況
							APPLICATIONSTATUS,
							//対象日
							thisMonth,
							//申請理由
							remarks, 
							//取得時間
							"",
							//振休取得日
							"",
							//総稼働時間
							"",
							//総残業時間
							"",
							//承認日
							"", 
							//承認者
							corresponding_person, 
							//申請者コメント
							"出勤時間を" + before_syukkin + "から" + after_syukkin + "へ修正し、退勤時間を" 
									+ before_taikin + "から" + after_taikin + "へ修正", 
							//承認者コメント
							"", 
							//作業内容
							"", 
							//連絡先
							"", 
							//作業時間
							"",
							//修正後出勤時刻
							after_syukkin,
							//修正後退勤時刻
							after_taikin
							);
				}
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
				return;
			} finally {
				adao.dbDiscnct();
				aadao.dbDiscnct();
			}
			log.info("result : {}", result);
			
			if(result) {
				resp.sendRedirect("various_applications_completed.jsp");
			} else {
				resp.sendRedirect("various_applications_error.jsp");
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
