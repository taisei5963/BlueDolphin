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

import DAO.MatterDAO;

/**
 * アサイン案件情報の検索・登録・キャンセル処理を行うサーブレットクラス
 */
@WebServlet("/MatterAssignmentServlet")
public class MatterAssignmentServlet extends HttpServlet {
	
	private static final Logger log = LogManager.getLogger(MatterAssignmentServlet.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
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
	 * 押下されたボタンの処理内容に応じた処理を実行するサーブレット
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException サーブレットで何らかの問題が発生した場合
	 * @throws IOException 入出力処理の失敗、または割り込みの発生が起きた場合
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		//セッション情報の送受信を行うための変数宣言
		HttpSession session = req.getSession();
		
		//DAOクラスのインスタンス取得
		MatterDAO mdao = MatterDAO.getInstance();
		
		//実行させる処理情報の取得
		String process = req.getParameter("process");
		
		switch(process) {
			case "検索処理":
				//リスト情報格納用変数宣言
				ArrayList<Integer> mattercodeList = new ArrayList<Integer>();
				ArrayList<String> matternameList = new ArrayList<String>();
				
				//案件情報の取得
				String code_value = req.getParameter("matter_code");
				Integer matter_code = null;
				String matter_name = req.getParameter("matter_name");
				try {
					if(code_value == null || code_value.length() == 0) {
						matter_code = null;
						mdao.dbConenect();
						mdao.createSt();
						mattercodeList = mdao.getMatterCodeInfo(matter_code, matter_name);
						matternameList = mdao.getMatterNameInfo(matter_code, matter_name);
					} else {
						matter_code = Integer.parseInt(code_value);
						mdao.dbConenect();
						mdao.createSt();
						mattercodeList = mdao.getMatterCodeInfo(matter_code, matter_name);
						matternameList = mdao.getMatterNameInfo(matter_code, matter_name);
					}
				} catch(SQLException e) {
					e.printStackTrace();
					log.error(SQLEXCEPTION);
				} finally {
					mdao.dbDiscnct();
				}
				session.setAttribute("mattercodeList", mattercodeList);
				session.setAttribute("matternameList", matternameList);
				session.setAttribute("process", process);
				resp.sendRedirect("matter_assignment_result.jsp");
				break;
			case "登録処理":
				
				//工数リストのセッション情報の取得
				ArrayList<Integer> mattercodelist = (ArrayList<Integer>)session.getAttribute("mattercodeList");
				ArrayList<String> matternamelist = (ArrayList<String>)session.getAttribute("matternameList");
				
				//変数宣言
				boolean result = false;
				Integer code [] = new Integer[mattercodelist.size()];
				String name[] = new String[matternamelist.size()];
				String chbox[] = new String[mattercodelist.size()];
				
				//チェックされたチェックボックス情報の取得
				for(int i=0; i<mattercodelist.size(); i++) {
					chbox[i] = req.getParameter("checkbox_name_" + (i+1));
					//チェックボックスの値がNULLの場合は、処理続行を継続
					if(chbox[i] == null) {
						continue;
					}
					//チェックボックスの値がNULLではない場合は、案件コードおよび案件名を取得
					name[i] = matternamelist.get(i);
					code[i] = mattercodelist.get(i);
				}
				
				try {
					mdao.dbConenect();
					mdao.createSt();
					result = mdao.setMatterInfo(code, name);
				} catch(SQLException e) {
					e.printStackTrace();
					log.error(SQLEXCEPTION);
				} finally {
					mdao.dbDiscnct();
				}
				if(result) {
					session.setAttribute("process", process);
					getServletContext().getRequestDispatcher("/matter_assignment_result.jsp").forward(req, resp);
				} else {
					getServletContext().getRequestDispatcher("/matter_assignment_error.jsp").forward(req, resp);
				}
				break;
			default:
				break;
		}
	}
}
