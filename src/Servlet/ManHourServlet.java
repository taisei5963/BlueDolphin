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

import Bean.MatterBean;
import DAO.ManHourDAO;

/**
 * 案件情報を工数情報データベースへ登録するサーブレットクラス
 *
 */

@WebServlet("/ManHourServlet")
public class ManHourServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ManHourServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String REGISTRATION1= "Registration success";
	private static final String REGISTRATION2 = "Registration failure";
	
	/**
	 * @param req クライアントが Servlet へ要求したリクエスト内容を含む HttpServletRequest オブジェクト
	 * @param resp Servlet がクライアントに返すレスポンス内容を含む HttpServletResponse オブジェクト
	 * @throws ServletException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 * @throws IOException Servlet が GET リクエストを処理している間に入出力エラーが発生した場合
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		HttpSession session = req.getSession();
		
		//チェックボックスの情報を取得
		String part = req.getParameter("part");
		
		log.debug("part : {}", part);
		
		//案件リスト情報の取得
		ArrayList<MatterBean> matterList = (ArrayList<MatterBean>)session.getAttribute("matterList");
		ArrayList<Integer> manhourcodeList = (ArrayList<Integer>)session.getAttribute("manhourcodeList");
		ArrayList<String> manhournameList = (ArrayList<String>)session.getAttribute("manhournameList");
		
		if(matterList.size()==0 || matterList == null) {
			log.error(REGISTRATION2);
			resp.sendRedirect("LogoutServlet");
		}
		
		//ManHourDAOクラスのインスタンス宣言
		ManHourDAO mhdao = ManHourDAO.getInstance();
		
		//登録成功したか否かを判断するための変数
		boolean result = false;
		
		//案件名格納用リスト宣言
		ArrayList<String> nameList = new ArrayList<String>();
		
		for(MatterBean mb : matterList) {
			nameList.add(mb.getMname());
		}
		
		if(nameList.contains(part)) {
			try {
				mhdao.dbConenect();
				mhdao.createSt();
				result = mhdao.setMatter(part);
				log.debug("result : {}", result);
			} catch (SQLException e) {
				log.error(SQLEXCEPTION);
				e.printStackTrace();
			} finally {
				mhdao.dbDiscnct();
			}
		} else {
			log.debug("result : {}", result);
		}
		
		if(result) {
			try {
				mhdao.dbConenect();
				mhdao.createSt();
				manhourcodeList = mhdao.getManHourCode();
				manhournameList = mhdao.getManHourName();
				session.setAttribute("manhourcodeList", manhourcodeList);
				session.setAttribute("manhournameList", manhournameList);
				session.setAttribute("matterList", matterList);
				resp.sendRedirect("man_hour_after.jsp");
				log.info(REGISTRATION1);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(SQLEXCEPTION);
				return;
			} finally {
				mhdao.dbDiscnct();
			}
		} else {
			resp.sendRedirect("man_hour_after.jsp");
			log.error(REGISTRATION2);
		}
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
