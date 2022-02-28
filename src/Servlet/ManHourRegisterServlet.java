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

import DAO.ManHourDAO;
import DAO.MatterDAO;

@WebServlet("/ManHourRegisterServlet")
public class ManHourRegisterServlet extends HttpServlet {
	
	private static final Logger log = LogManager.getLogger(ManHourRegisterServlet.class);
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String NULLPOINTEREXCEPTION = "NullPointerException occurred";
	
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
	 * 各処理を実行するメソッド
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
		
		//MatterDAOクラスのインスタンス取得
		MatterDAO mdao = MatterDAO.getInstance();
		ManHourDAO mhdao = ManHourDAO.getInstance();
		
		boolean result = false;
		
		//実行させる処理情報の取得
		String process = req.getParameter("process");
		log.info("process : {}", process);
		switch(process) {
			case "削除処理":
				String manhourName = (String)session.getAttribute("manhourName");
				if(manhourName == null) {
					log.error(NULLPOINTEREXCEPTION);
					return;
				}
				try {
					mdao.dbConenect();
					mdao.createSt();
					result = mdao.deleteMatter(manhourName);
				} catch(SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					mdao.dbDiscnct();
				}
				if(result) {
					log.debug("Deletion successful");
					process = "削除成功";
					session.setAttribute("process", process);
					getServletContext().getRequestDispatcher("/man_hour_result.jsp").forward(req, resp);
				} else {
					log.error("Deletion failure");
					process = "削除失敗";
					session.setAttribute("process", process);
					getServletContext().getRequestDispatcher("/man_hour_error.jsp").forward(req, resp);
				}
				break;
			case "登録処理":
				
				//セッション情報の取得
				String thisMonthForm = (String)session.getAttribute("thisMonthForm");
				Integer emp_num = (Integer)session.getAttribute("emp_num");
				ArrayList<Integer> manhourcodeList = (ArrayList<Integer>)session.getAttribute("manhourcodeList");
				ArrayList<String> manhournameList = (ArrayList<String>)session.getAttribute("manhournameList");
				
				//工数名および工数時間格納用変数宣言
				String name [] = new String[manhourcodeList.size()];
				String time [] = new String[manhourcodeList.size()];
				
				//パラメータ値の取得
				for(int i=0; i<manhourcodeList.size(); i++) {
					if(!((req.getParameter("time_name_" + (i+1)).equals("00:00"))) || req.getParameter("time_name_" + (i+1)) != null){
						name[i] = manhournameList.get(i);
						time[i] = req.getParameter("time_name_" + (i+1));
					} else {
						continue;
					}
				}
				try {
					mhdao.dbConenect();
					mhdao.createSt();
					result = mhdao.setManHourEmployee(emp_num, thisMonthForm, name, time);
				} catch (SQLException e) {
					log.error(SQLEXCEPTION);
					e.printStackTrace();
					return;
				} finally {
					mhdao.dbDiscnct();
				}
				if(result) {
					log.debug("Successful registration");
					process = "登録成功";
					session.setAttribute("process", process);
					getServletContext().getRequestDispatcher("/man_hour_result.jsp").forward(req, resp);
				} else {
					log.error("Registration failure");
					process = "登録失敗";
					session.setAttribute("process", process);
					getServletContext().getRequestDispatcher("/man_hour_error.jsp").forward(req, resp);
				}
				break;
			default:
				break;
		}
	}
}
