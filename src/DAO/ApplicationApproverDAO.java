package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 申請承認者テーブルに接続するためのDAOクラス 
 *
 */
public class ApplicationApproverDAO{
	
	//唯一のインスタンス生成
	private static ApplicationApproverDAO only_instance = new ApplicationApproverDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(ApplicationApproverDAO.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String NOTDATA = "Sorry! No matching data";
	
	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	private ResultSet rs;
	
	/**
	 * privateのため、新規のインスタンスを作らせない
	 */
	private ApplicationApproverDAO() {
	}
	
	/**
	 * 唯一のインスタンスを取得(=シングルトン)
	 * @return KintaiDAOの唯一のインスタンス
	 */
	public static ApplicationApproverDAO getInstance() {
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
	 * 申請者に該当する承認者名を取得するメソッド
	 * @param applicant　申請者名
	 * @return　authorizer　承認者名
	 * @throws SQLException　データベース処理に問題が発生した場合
	 */
	public String getAuthorizer(String applicant) throws SQLException{
		
		log.info("getAuthorizer start");
		
		//取得情報格納用変数宣言
		String authorizer = null;
		
		//SQL文
		String sql = "select authorizer from application_approver where applicant = '" + applicant + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		//SQL実行結果の取得
		if(rs.next()) {
			authorizer = rs.getString("authorizer");
		} else {
			log.error(NOTDATA);
		}
		
		log.info("getAuthorizer end");
		return authorizer;
	}
	
	/**
	 * 承認者のメールアドレスを取得するメソッド
	 * @param authorizer　承認者名
	 * @return　承認者のメールアドレス
	 * @throws SQLException　データベース処理に問題が発生した場合
	 */
	public String getAuthorizerMailAdd(String authorizer) throws SQLException{
		log.info("getAuthorizerMailAdd start");
		
		//メールアドレス情報格納用変数宣言
		String mailAdd = null;
		
		//SQL文
		String sql = "select emp_mail from employee where emp_name = '" + authorizer + "';";
		rs = st.executeQuery(sql);
		log.debug("st : {}", st);
		
		//SQL実行結果の取得
		if(rs.next()) {
			mailAdd = rs.getString("emp_mail");
		} else {
			log.error(NOTDATA);
		}
		
		log.info("getAuthorizerMailAdd end");
		return mailAdd;
	}
}
