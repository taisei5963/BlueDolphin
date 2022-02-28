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
import DAO.LoginDAO;

/**
 *ログイン情報を基にシステムへのログイン判定を行うサーブレットクラス
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(LoginServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String NULLPOINTEREXCEPTION = "NullPointerException occurred";

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

		//ユーザ情報用のリストのインスタンス生成
		ArrayList<LoginBean> userlist = new ArrayList<LoginBean>();
		
		HttpSession session = req.getSession();

		//入力情報の取得
		String email = req.getParameter("mail");
		String pass = req.getParameter("pass");

		//DAOクラスのインスタンス化の生成
		LoginDAO d1 = LoginDAO.getInstance();
		
		//ログイン情報を元にユーザ情報を取得
		//Exception発生時は、ユーザに戻る
		try {
			d1.dbConnect();
			d1.createSt();
			userlist = d1.login_chk(email, pass);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
			return;
		} finally {
			d1.dbDiscnct();
		}
		log.info("userlist : {}", userlist);

		try {
			for (LoginBean lb : userlist) {
				//ログイン成功
				if ((email.equals(lb.getEmail())) && (pass.equals(lb.getPassword()))) {
					//ユーザ情報をセッションへ渡す
					session.setAttribute("userlist", userlist);
					log.info("LOGIN SUCCESSED.");
					//勤怠登録画面へ遷移
					resp.sendRedirect("KintaiViewServlet");
				}
			}
		} catch (NullPointerException e) {
			log.error(NULLPOINTEREXCEPTION);
			e.printStackTrace();
			session.setAttribute("email", email);
			resp.sendRedirect("login_mistake.jsp");
		} finally {}
	}
}
