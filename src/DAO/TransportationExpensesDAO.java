package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * 交通費情報データベースと接続するためのDAOクラス
 *
 */
public class TransportationExpensesDAO{
	
	//唯一のインスタンス生成
	private static TransportationExpensesDAO only_instance = new TransportationExpensesDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(TransportationExpensesDAO.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	
	/**
	 * privateのため、新規のインスタンスを作らせない
	 */
	private TransportationExpensesDAO() {
	}
	
	/**
	 * 唯一のインスタンスを取得(=シングルトン)
	 * @return KintaiDAOの唯一のインスタンス
	 */
	public static TransportationExpensesDAO getInstance() {
		return only_instance;
	}
	
	/**
	 * 特定のデータベースとの接続（セッション）を生成
	 * @throws SQLException データベース処理で問題が発生した場合
	 */
	public void dbConenect() throws SQLException {
		log.info("dbConenect start");
		ConnectMySQL cm = ConnectMySQL.getInstance();
		cnct = cm.cnct();
		log.debug("cnct : {}", cnct);
		log.info("dbConenect end");
	}
	
	/**
	 * SQL文を実行し、結果を返却するためのオブジェクト生成
	 * @throws SQLException データベース処理で問題が発生
	 */
	public void createSt() throws SQLException {
		log.info("createSt start");
		st = cnct.createStatement();
		log.debug("st : {}", st);
		log.info("createSt end");
	}
	
	/**
	 * 特定データベースとの接続（セッション）をクローズする
	 */
	public void dbDiscnct() {
		log.info("dbDiscnct start");
		try {
			if(st != null)		st.close();
			if(cnct != null)	cnct.close();
		} catch (SQLException e) {
			log.error(SQLEXCEPTION);
			e.printStackTrace();
		}
		log.info("dbDiscnct end");
	}
	
	/**
	 * 入力された交通費情報をデータベースに登録するメソッド
	 * @param emp_num　社員番号
	 * @param date　日付
	 * @param departure　出発
	 * @param arrival　到着
	 * @param via　経由
	 * @param claims　請求範囲
	 * @param billing_address　請求先
	 * @param purpose　目的・行き先
	 * @param amounts　金額
	 * @return　TRUE：登録が正常に行われた場合<br>FALSE：登録が正常に行われなかった場合
	 * @throws SQLException　データベース処理で問題が発生した場合
	 */
	public boolean setEmpTransportationExpenses(Integer emp_num, String date, String departure, String arrival, 
			String via, String claims, String billing_address, String purpose, Integer amounts) throws SQLException{
		
		log.info("setEmpTransportationExpenses start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL文
		String sql = "insert into transportation_expenses (emp_num, date, departure, arrival, via, claims, "
				+ "billing_address, purpose, amounts) values ('" + emp_num + "', '" + date + "', '" + departure 
				+ "', '" + arrival + "', '" + via + "', '" + claims + "', '" + billing_address + "', '" 
				+ purpose + "', '" + amounts + "');";
		st.executeUpdate(sql);
		cnct.commit();
		
		log.info("setEmpTransportationExpenses end");
		return true;
	}
}
