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
import DAO.TransportationExpensesDAO;

/**
 *交通費情報を受け取り、DBに登録する処理
 */
@WebServlet("/TransportationExpensesServlet")
public class TransportationExpensesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(TransportationExpensesServlet.class);
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
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{

		resp.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		//変数宣言
		HttpSession session = req.getSession();
		Integer emp_num = null;
		boolean result = false;
		
		//セッション情報取得
		ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
		
		//パラメータ情報の取得
		String date = req.getParameter("date");							//日付
		String departure = req.getParameter("departure");				//出発
		String arrival = req.getParameter("arrival");					//到着
		String via = req.getParameter("via");							//経由
		String claims = req.getParameter("claims");						//請求範囲
		String billing_address = req.getParameter("billing_address");	//請求先
		String purpose = req.getParameter("purpose");					//目的・行き先
		Integer amounts  = Integer.parseInt(req.getParameter("amounts"));					//金額
		
		if(claims.equals("one_way"))							claims = "片道";
		else if(claims.equals("round_trip"))					claims = "往復";
		
		if(billing_address.equals("private_company"))			billing_address = "自社";
		else if(billing_address.equals("customer_company"))		billing_address = "客先";

		//削除処理に使用する従業員番号の取得
		for(LoginBean lb : userlist) {
			emp_num = lb.getNumber();
		}
		
		TransportationExpensesDAO tedao = TransportationExpensesDAO.getInstance();
		try {
			tedao.dbConenect();
			tedao.createSt();
			result = tedao.setEmpTransportationExpenses(emp_num, date, departure, arrival, via, claims, billing_address, purpose, amounts);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(SQLEXCEPTION);
			return;
		} finally {
			tedao.dbDiscnct();
		}
		
		if(result) {
			getServletContext().getRequestDispatcher("/transportation_expenses_compleat.jsp").forward(req, resp);
		} else {
			getServletContext().getRequestDispatcher("/transportation_expenses_error.jsp").forward(req, resp);
		}
	}
}
