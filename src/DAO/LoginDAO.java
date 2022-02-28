package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Bean.LoginBean;

/**
 * 社員基本情報データベースと接続するためのDAOクラス
 *
 */
public class LoginDAO {
	
	//唯一のインスタンス生成
	private static LoginDAO only_instance = new LoginDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(LoginDAO.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String DBWORDING = "Values ​​do not match";

	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	private ResultSet rs;

	/**
	 * privateのため、新規インスタンスを生成させない
	 * 
	 */
	private LoginDAO() {
	}
	
	/**
	 * 唯一のインスタンスを取得（=シングルトン）
	 * @return LoginDAO唯一のインスタンス
	 */
	public static LoginDAO getInstance() {
		return only_instance;
	}
	
	/**
	 * 特定のデータベースとの接続（セッション）を生成
	 * @throws SQLException データベース処理で問題が発生した場合
	 */
	public void dbConnect() throws SQLException {
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
	 * ログイン画面でのインプット情報をもとにログインできるかを判断するメソッド
	 * @param email 社用メールアドレス
	 * @param pass 社用メールアドレスに対応するパスワード
	 * @return loginList 取得した社員基本情報リスト
	 * @throws SQLException データベース処理で問題が発生した場合
	 */
	public ArrayList<LoginBean> login_chk(String email, String pass) throws SQLException {
		log.info("login_chk start");
		
		//取得データ格納用リストのインスタンス生成
		ArrayList<LoginBean> loginList = new ArrayList<>();
		
		//SQL文
		String sql = "select emp_num, emp_name, emp_mail, emp_pass, authority from employee where emp_mail='" + email + "' and emp_pass='" + pass + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		//データベースから該当の１レコード分を取得し、データベースの値と比較
		if(rs.next()) {
			if(email.equals(rs.getString("emp_mail"))) {
				if(pass.equals(rs.getString("emp_pass"))) {
					//LoginBeanクラスのインスタンス生成
					LoginBean lb = new LoginBean();
					lb.setNumber(rs.getInt("emp_num"));
					lb.setName(rs.getString("emp_name"));
					lb.setEmail(rs.getString("emp_mail"));
					lb.setPassword(rs.getString("emp_pass"));
					lb.setAuthority(rs.getInt("authority"));
					loginList.add(lb);
				}
			}
		} else {
			log.error(DBWORDING);
			return null;
		}
		log.info("login_chk end");
		return loginList;
	}
	
	/**
	 * ユーザパスワード変更処理を行うメソッド
	 * @param email 社用メールアドレス
	 * @param password 現在のログインパスワード
	 * @return 変更できた場合：「true」、変更できない場合：「false」を返却する
	 * @throws SQLException DB処理に問題が発生した場合
	 */
	public boolean setPassword(String email, String password) throws SQLException{
		log.info("setPassword start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		String sql = "select emp_num, emp_name from employee where emp_mail = '" + email + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		//情報が取得できた場合は、パスワードの更新を行う
		if(rs.next()) {
			sql = "update user set emp_pass = '" + password + "' where emp_mail = '" + email + "';";
			log.debug("sql:{}", sql);
			st.executeUpdate(sql);
			cnct.commit();
			log.info("setPassword end");
			return true;
		}
		return false;
	}
}