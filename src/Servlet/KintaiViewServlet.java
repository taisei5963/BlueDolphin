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
import DAO.WorkTimeDAO;

/**
 * 「月」を受け取りDBデータに対応する出勤時間情報を画面に送信するサーブレットクラス
 *
 */

@WebServlet("/KintaiViewServlet")
public class KintaiViewServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(KintaiViewServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	/**
	 * @param req クライアントが Servlet へ要求したリクエスト内容を含む HttpServletRequest オブジェクト
	 * @param resp Servlet がクライアントに返すレスポンス内容を含む HttpServletResponse オブジェクト
	 * @throws ServletException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 * @throws IOException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
		Integer emp_num = null;
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
		}
		//KintaiDAOクラスの唯一のインスタンスを取得
		WorkTimeDAO workTimeDAO = WorkTimeDAO.getInstance();
		log.info("workTimeDAO : {}", workTimeDAO);
		try {
			workTimeDAO.dbConenect();
			workTimeDAO.createSt();
			String syukkin = workTimeDAO.selectSyukkinTime(emp_num);
			log.info("syukkin : {}", syukkin);
			session.setAttribute("syukkin", syukkin);
			
			//「出勤時間」がまだ登録されていない場合
			if(syukkin != null) {
				String taikin = workTimeDAO.selectTaikinTime(emp_num);
				log.info("taikin : {}", taikin);
				session.setAttribute("taikin", taikin);
			}
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
		} finally {
			workTimeDAO.dbDiscnct();	
		}
		resp.sendRedirect("kintai_input.jsp");
	}

	/**
	 * 勤怠関連処理の基底メソッド
	 * @param req クライアントが Servlet へ要求したリクエスト内容を含む HttpServletRequest オブジェクト
	 * @param resp Servlet がクライアントに返すレスポンス内容を含む HttpServletResponse オブジェクト
	 * @throws ServletException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 * @throws IOException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
