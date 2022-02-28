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

import Bean.LoginBean;
import DAO.ApplicationApproverDAO;
import DAO.ApprovalApplicationDAO;
import Other.ApplicationNumber;

/**
 * 変更された新規申請者名を反映させた在宅勤務申請入力画面を表示させるサーブレットクラス
 *
 */

@WebServlet("/WorkRemotelyResultServlet")
public class WorkRemotelyResultServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(WorkRemotelyResultServlet.class);
	private static final String CONTENTCORRECTIONPROCESS = "内容修正処理";
	private static final String APPROVALAPPLICATIONPROCESS = "申請処理";
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String WORKINGFROMHOUSEREQUEST = "在宅勤務申請";
	private static final String APPLICATIONSTATUS = "申請中";
	
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
		String process = req.getParameter("process");
		String applicant = (String)session.getAttribute("applicant");
		String start_application_date = (String)session.getAttribute("start_application_date");
		String end_application_date = (String)session.getAttribute("end_application_date");
		String start_application_time = (String)session.getAttribute("start_application_time");
		String end_application_time = (String)session.getAttribute("end_application_time");
		String reason_for_application = (String)session.getAttribute("reason_for_application");
		String work_content = (String)session.getAttribute("work_content");
		String contact_address = (String)session.getAttribute("contact_address");
		
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
		Integer emp_num = null;
		
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
		}
		
		log.debug("process : {}", process);
		
		//申請内容修正処理
		if(CONTENTCORRECTIONPROCESS.equals(process)) {
			session.setAttribute("applicant", applicant);
			session.setAttribute("start_application_date", start_application_date);
			session.setAttribute("end_application_date", end_application_date);
			session.setAttribute("start_application_time", start_application_time);
			session.setAttribute("end_application_time", end_application_time);
			session.setAttribute("reason_for_application", reason_for_application);
			session.setAttribute("work_content", work_content);
			session.setAttribute("contact_address", contact_address);
			resp.sendRedirect("WorkRemotelyViewServlet");
		//申請処理
		} else if(APPROVALAPPLICATIONPROCESS.equals(process)) {
			
			//各インスタンス情報の取得
			ApplicationNumber an = ApplicationNumber.getInstance();
			ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
			ApplicationApproverDAO adao = ApplicationApproverDAO.getInstance();
			
			//データベース登録処理結果格納用変数宣言
			boolean result = false;
			
			//現在日付を取得
			LocalDate date = LocalDate.now();
			DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)");
			String formatDate = dtf1.format(date);
			
			//申請内容テーブルへの登録処理
			try {
				adao.dbConenect();
				aadao.dbConenect();
				adao.createSt();
				aadao.createSt();
				result = aadao.setApplicationContents(
						//申請番号
						an.getAppNumber(emp_num), 
						//申請日
						formatDate, 
						//申請者
						applicant, 
						//申請名
						WORKINGFROMHOUSEREQUEST, 
						//申請種類
						"", 
						//申請区分
						WORKINGFROMHOUSEREQUEST, 
						//申請状況
						APPLICATIONSTATUS, 
						//対象日
						start_application_date + "〜" + end_application_date, 
						//申請理由
						reason_for_application, 
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
						adao.getAuthorizer(applicant), 
						//申請者コメント
						"", 
						//承認者コメント
						"", 
						//作業内容
						work_content, 
						//連絡先
						contact_address, 
						//作業時間
						start_application_time + "〜" + end_application_time,
						//修正後出勤時刻
						"",
						//修正後退勤時刻
						""
						);
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
				return;
			} finally {
				adao.dbDiscnct();
				aadao.dbDiscnct();
			}
			if(result) {
				resp.sendRedirect("work_remotely_result.jsp");
			} else {
				resp.sendRedirect("work_remotely_error.jsp");
			}
		}
	}
}
