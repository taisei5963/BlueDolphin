package Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *ログイン情報を基にシステムへのログイン判定を行うサーブレットクラス
 */
@WebServlet("/AccountCheckServlet")
public class AccountCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AccountCheckServlet.class);
	//ログ出力文言宣言
	private static final String NOTMATCHPASS = "Password entered does not match";

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

		//入力情報の取得
		Integer num = Integer.parseInt(req.getParameter("emp_num"));
		String name = req.getParameter("emp_name");
		String email = req.getParameter("emp_mail");
		String pass1 = req.getParameter("emp_pass1");
		String pass2 = req.getParameter("emp_pass2");
		Integer authority = Integer.parseInt(req.getParameter("granting_authority"));
		
		//セッション情報の送受信用変数宣言
		HttpSession session = req.getSession();

		//入力されたパスワードが不一致の場合
		if(!pass1.equals(pass2)) {
			log.error(NOTMATCHPASS);
			getServletContext().getRequestDispatcher("/add_account_error.jsp").forward(req, resp);
			return;
		} else {
			session.setAttribute("num", num);
			session.setAttribute("name", name);
			session.setAttribute("email", email);
			session.setAttribute("pass1", pass1);
			session.setAttribute("authority", authority);
			getServletContext().getRequestDispatcher("/add_account_check.jsp").forward(req, resp);
		}
	}
}
