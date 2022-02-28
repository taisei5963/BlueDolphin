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
 * 残業申請処理行うサーブレットクラス
 *
 */

@WebServlet("/DirectBounceRequestProcessServlet")
public class DirectBounceRequestProcessServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(DirectBounceRequestProcessServlet.class);
	
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	//文言比較用定数宣言
	private static final String APPROVALPROCESS            = "申請処理";
	private static final String GOSTRAIGHTREQUESTNAME      = "直行申請";
	private static final String BOUNCEREQUESTNAME          = "直帰申請";
	private static final String DIRECTBOUNCEREQUESTNAME    = "直行直帰申請";
	private static final String APPLICATIONSTATUS          = "申請中";
	
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
		
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
		Integer emp_num = null;
		String name = null;
		
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
			name = lb.getName();
		}
		
		//申請種別パラメータ値取得
		String kind_direct_bounce = req.getParameter("kind_direct_bounce");
		
		//実行処理内容
		String process = req.getParameter("process");
		
		log.debug("kind_direct_bounce : {}", kind_direct_bounce);
		log.debug("process : {}", process);
		
		//申請処理実行
		if(APPROVALPROCESS.equals(process)) {
			//申請日
			String startDate = req.getParameter("startDate");
			//申請理由
			String application_reason = req.getParameter("application_reason");
			//コメント
			String comment = req.getParameter("comment");
			
			//各インスタンス情報取得
			ApplicationNumber an = ApplicationNumber.getInstance();
			ApprovalApplicationDAO aadao = ApprovalApplicationDAO.getInstance();
			ApplicationApproverDAO adao = ApplicationApproverDAO.getInstance();

			//現在日付を取得
			LocalDate date = LocalDate.now();
			DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)");
			String formatDate = dtf1.format(date);
			
			switch(kind_direct_bounce) {
			//直行申請
			case GOSTRAIGHTREQUESTNAME:
				try {
					aadao.dbConenect();
					aadao.createSt();
					adao.dbConenect();
					adao.createSt();
					
					//申請者に該当する承認者を取得
					String corresponding_person = adao.getAuthorizer(name);
					
					//承認申請テーブルに申請情報を登録
					boolean result = aadao.setApplicationContents(
							//申請番号
							an.getAppNumber(emp_num),
							//申請日
							formatDate,
							//申請者
							name, 
							//申請名
							GOSTRAIGHTREQUESTNAME,
							//申請種類
							"",
							//申請区分
							DIRECTBOUNCEREQUESTNAME,
							//申請状況
							APPLICATIONSTATUS,
							//対象日
							startDate,
							//申請理由
							application_reason, 
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
							comment, 
							//承認者コメント
							"", 
							//作業内容
							"", 
							//連絡先
							"", 
							//作業時間
							"",
							//修正後出勤時刻
							"",
							//修正後退勤時刻
							""
							);
					log.debug("result : {}", result);
					if(result) {
						resp.sendRedirect("various_applications_completed.jsp");
					} else {
						log.error("申請情報を正常に登録できませんでした。");
						resp.sendRedirect("various_application_error.jsp");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					log.error(SQLEXCEPTION);
					resp.sendRedirect("various_application_error.jsp");
				} finally {
					aadao.dbDiscnct();
					adao.dbDiscnct();
				}
				break;
			//直帰申請
			case BOUNCEREQUESTNAME:
				try {
					aadao.dbConenect();
					aadao.createSt();
					adao.dbConenect();
					adao.createSt();
					
					//申請者に該当する承認者を取得
					String corresponding_person = adao.getAuthorizer(name);
					
					//承認申請テーブルに申請情報を登録
					boolean result = aadao.setApplicationContents(
							//申請番号
							an.getAppNumber(emp_num),
							//申請日
							formatDate,
							//申請者
							name, 
							//申請名
							BOUNCEREQUESTNAME,
							//申請種類
							"",
							//申請区分
							DIRECTBOUNCEREQUESTNAME,
							//申請状況
							APPLICATIONSTATUS,
							//対象日
							startDate,
							//申請理由
							application_reason, 
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
							comment, 
							//承認者コメント
							"", 
							//作業内容
							"", 
							//連絡先
							"", 
							//作業時間
							"",
							//修正後出勤時刻
							"",
							//修正後退勤時刻
							""
							);
					log.debug("result : {}", result);
					if(result) {
						resp.sendRedirect("various_applications_completed.jsp");
					} else {
						log.error("申請情報を正常に登録できませんでした。");
						resp.sendRedirect("various_application_error.jsp");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					log.error(SQLEXCEPTION);
					resp.sendRedirect("various_application_error.jsp");
				} finally {
					aadao.dbDiscnct();
					adao.dbDiscnct();
				}
				break;
			case DIRECTBOUNCEREQUESTNAME:
				try {
					aadao.dbConenect();
					aadao.createSt();
					adao.dbConenect();
					adao.createSt();
					
					//申請者に該当する承認者を取得
					String corresponding_person = adao.getAuthorizer(name);
					
					//承認申請テーブルに申請情報を登録
					boolean result = aadao.setApplicationContents(
							//申請番号
							an.getAppNumber(emp_num),
							//申請日
							formatDate,
							//申請者
							name, 
							//申請名
							DIRECTBOUNCEREQUESTNAME,
							//申請種類
							"",
							//申請区分
							DIRECTBOUNCEREQUESTNAME,
							//申請状況
							APPLICATIONSTATUS,
							//対象日
							startDate,
							//申請理由
							application_reason, 
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
							comment, 
							//承認者コメント
							"", 
							//作業内容
							"", 
							//連絡先
							"", 
							//作業時間
							"",
							//修正後出勤時刻
							"",
							//修正後退勤時刻
							""
							);
					log.debug("result : {}", result);
					if(result) {
						resp.sendRedirect("various_applications_completed.jsp");
					} else {
						log.error("申請情報を正常に登録できませんでした。");
						resp.sendRedirect("various_application_error.jsp");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					log.error(SQLEXCEPTION);
					resp.sendRedirect("various_application_error.jsp");
				} finally {
					aadao.dbDiscnct();
					adao.dbDiscnct();
				}
				break;
			//上記以外
			default :
				break;
			}
		} else {}
	}
}
