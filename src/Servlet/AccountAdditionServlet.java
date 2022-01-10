package Servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import DAO.NewAccountDAO;

/**
 *ログイン情報を基にシステムへのログイン判定を行うサーブレットクラス
 */
@WebServlet("/AccountAdditionServlet")
public class AccountAdditionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AccountAdditionServlet.class);
	private static final String SQLEXCEPTION = "SQLException occured";

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
		
		//セッション情報送受信用変数宣言
		HttpSession session = req.getSession();

		//セッション情報の取得
		Integer num = (Integer)session.getAttribute("num");
		String name = (String)session.getAttribute("name");
		String email = (String)session.getAttribute("email");
		String pass = (String)session.getAttribute("pass1");
		Integer authority = (Integer)session.getAttribute("authority");
		
		//登録判定結果用変数宣言
		boolean result = false;
		
		//処理内容格納用変数宣言
		String process_result = null;
		
		//DAOクラスのインスタンス取得
		NewAccountDAO ndao = NewAccountDAO.getInstance();
		try {
			ndao.dbConenect();
			ndao.createSt();
			result = ndao.setNewAccountInfo(name, num, email, pass, authority);
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
			return;
		} finally {
			ndao.dbDiscnct();
		}
		
		if(result) {
			process_result = "登録成功";
			session.setAttribute("process_result", process_result);
			getServletContext().getRequestDispatcher("/add_account_result.jsp").forward(req, resp);
		} else {
			process_result = "登録失敗";
			session.setAttribute("process_result", process_result);
			getServletContext().getRequestDispatcher("/add_account_result.jsp").forward(req, resp);
		}
	}
}