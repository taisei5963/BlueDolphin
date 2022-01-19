package Servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 変更された新規申請者名を反映させた在宅勤務申請入力画面を表示させるサーブレットクラス
 *
 */

@WebServlet("/WorkRemotelyServlet")
public class WorkRemotelyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(WorkRemotelyServlet.class);
	
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
		ArrayList<String> usernameList = (ArrayList<String>)session.getAttribute("usernameList");
		
		log.debug("usernameList : {}", usernameList);
		
		//ユーザ名リスト数分の配列設定
		String [] emp_check = new String[usernameList.size()];
		String other_name = null;
		
		//チェックボックスの情報取得
		for(int i=0; i<usernameList.size(); i++) {
			emp_check[i] = req.getParameter("emp_check_" + (i+1));
			//チェックがされていない項目が存在する場合は、何もせずに処理続行
			if(emp_check[i] == null) {
				continue;
			//チェックされた項目が存在する場合は、チェック情報に該当する従業員名の情報を取得し変数に格納
			} else if(emp_check[i].equals("on")) {
				other_name = usernameList.get(i);
			}
		}
		
		//取得した新規申請者名をセッション情報として、渡す
		session.setAttribute("other_name", other_name);
		resp.sendRedirect("WorkRemotelyViewServlet");
	}
}
