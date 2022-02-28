package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 従業員情報データベースと接続するためのDAOクラス
 *
 */
public class NewAccountDAO{
	
	//唯一のインスタンス生成
	private static NewAccountDAO only_instance = new NewAccountDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(NewAccountDAO.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	private ResultSet rs;
	
	/**
	 * privateのため、新規のインスタンスを作らせない
	 */
	private NewAccountDAO() {}
	
	/**
	 * 唯一のインスタンスを取得(=シングルトン)
	 * @return KintaiDAOの唯一のインスタンス
	 */
	public static NewAccountDAO getInstance() {
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
	 * 新規ユーザ情報登録処理を行うメソッド
	 * @param name 社員名
	 * @param number 社員番号
	 * @param email 社用メールアドレス
	 * @param password ログインパスワード
	 * @param authority 管理者ユーザ判別フラグ
	 * @return 登録できる場合：「true」、登録できない場合：「false」を返却
	 * @throws SQLException
	 */
	public boolean setNewAccountInfo(String name, Integer number, String email,
			String password, Integer authority) throws SQLException{
		
		log.info("setNewAccountInfo start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		String sql = "select emp_name, emp_num, emp_pass from employee where emp_mail = '" + email + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		//データが取得できた場合は、「false」を返却する
		if(rs.next()) {
			return false;
		} else {
			sql = "insert into employee (emp_num, emp_name, emp_mail, authority, emp_pass) values ('"
					+ number + "', '" + name + "', '" + email + "', '" + authority + "', '" + password + "');";
			st.executeUpdate(sql);
			cnct.commit();
			log.info("setNewAccountInfo end");
			return true;
		}
	}
}
