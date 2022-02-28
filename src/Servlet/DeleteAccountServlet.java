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

import Bean.LoginBean;
import DAO.DeleteAccountDAO;

/**
 *ログイン情報を基にシステムへのログイン判定を行うサーブレットクラス
 */
@WebServlet("/DeleteAccountServlet")
public class DeleteAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(DeleteAccountServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";

	/**
	 * doPostメソッドを呼び出すメソッド
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
	 * Servlet に POST リクエストを処理可能にさせるため、(service メソッド経由で) サーバによって呼び出される
	 * データベースに接続して従業員のログインを判断する
	 * ログインに成功したらセッション情報に社員基本情報リストをセットする
	 * @param req クライアントが Servlet へ要求したリクエスト内容を含む HttpServletRequest オブジェクト
	 * @param resp Servlet がクライアントに返すレスポンス内容を含む HttpServletResponse オブジェクト
	 * @throws ServletException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 * @throws IOException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{

		resp.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		//セッション情報の送受信用変数宣言
		HttpSession session = req.getSession();
		
		Integer emp_num = null;
		String pro_result = null;
		boolean result1 = false;
		boolean result2 = false;
		boolean result3 = false;
		boolean result4 = false;
		boolean result5 = false;
		
		//セッション情報取得
		@SuppressWarnings("unchecked")
		ArrayList<LoginBean> empList = (ArrayList<LoginBean>)session.getAttribute("empList");

		//削除処理に使用する従業員番号の取得
		for(LoginBean lb : empList) {
			emp_num = lb.getNumber();
		}
		
		DeleteAccountDAO ddao = DeleteAccountDAO.getInstance();
		try {
			ddao.dbConenect();
			ddao.createSt();
			result1 = ddao.deleteContractTime(emp_num);
			result2 = ddao.deleteEmployeeKintai(emp_num);
			result3 = ddao.deleteEmpManHour(emp_num);
			result4 = ddao.deleteEmployee(emp_num);
			result5 = ddao.deletePaidHoliday(emp_num);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
			return;
		} finally {
			ddao.dbDiscnct();
		}
		
		if(result1) {
			if(result2) {
				if(result3) {
					if(result4) {
						//有給休暇情報削除に関しては、有給が与えられる前に退職することを懸念して、TRUEでもFALSEでも削除成功とする。
						if(result5) {
							pro_result = "削除成功";
							session.setAttribute("pro_result", pro_result);
							getServletContext().getRequestDispatcher("/delete_account_result.jsp").forward(req, resp);
						} else {
							pro_result = "削除成功";
							session.setAttribute("pro_result", pro_result);
							getServletContext().getRequestDispatcher("/delete_account_result.jsp").forward(req, resp);
						}
					} else {
						pro_result = "削除失敗";
						session.setAttribute("pro_result", pro_result);
						getServletContext().getRequestDispatcher("/delete_account_result.jsp").forward(req, resp);
					}
				} else {
					pro_result = "削除失敗";
					session.setAttribute("pro_result", pro_result);
					getServletContext().getRequestDispatcher("/delete_account_result.jsp").forward(req, resp);
				}
			} else {
				pro_result = "削除失敗";
				session.setAttribute("pro_result", pro_result);
				getServletContext().getRequestDispatcher("/delete_account_result.jsp").forward(req, resp);
			}
		} else {
			pro_result = "削除失敗";
			session.setAttribute("pro_result", pro_result);
			getServletContext().getRequestDispatcher("/delete_account_result.jsp").forward(req, resp);
		}
	}
}
