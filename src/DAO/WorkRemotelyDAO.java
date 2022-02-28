package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * 申請理由データベースと接続するためのDAOクラス
 *
 */

import Bean.WorkRemotelyBean;

/**
 * 在宅勤務申請関連テーブルにアクセスするためのDAOクラス
 * 
 *
 */
public class WorkRemotelyDAO{
	
	//唯一のインスタンス生成
	private static WorkRemotelyDAO only_instance = new WorkRemotelyDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(WorkRemotelyDAO.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	private ResultSet rs;
	
	/**
	 * privateのため、新規のインスタンスを作らせない
	 */
	private WorkRemotelyDAO() {
	}
	
	/**
	 * 唯一のインスタンスを取得(=シングルトン)
	 * @return KintaiDAOの唯一のインスタンス
	 */
	public static WorkRemotelyDAO getInstance() {
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
	 * 在宅勤務申請で使用する申請理由の情報を取得するメソッド
	 * @return 申請理由情報を格納したリスト
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public ArrayList<WorkRemotelyBean> getWorkRemotelyReason() throws SQLException{
		log.info("getWorkRemotelyReason start");
		
		//返却用リスト変数宣言
		ArrayList<WorkRemotelyBean> reasonList = new ArrayList<WorkRemotelyBean>();
		
		//SQL文
		String sql = "select reason_name, reason from reason_for_application";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		//取得した情報を全件リストに追加する
		while(rs.next()) {
			WorkRemotelyBean wrb = new WorkRemotelyBean();
			wrb.setResonName(rs.getString("reason_name"));
			wrb.setReason(rs.getString("reason"));
			reasonList.add(wrb);
		}
		log.info("getWorkRemotelyReason end");
		return reasonList;
	}
	
	/**
	 * 指定ユーザ以外のユーザ情報を取得するメソッド
	 * @param name ユーザ名
	 * @return ユーザ情報を格納したリスト
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public ArrayList<String> getUserName(String name) throws SQLException{
		log.info("getUserName start");
		
		//返却用リスト変数宣言
		ArrayList<String> usernameList = new ArrayList<String>();
		
		//SQL文
		String sql = "select emp_name from employee where emp_name != '" + name + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		//取得した情報を全件リストに追加
		while(rs.next()) {
			usernameList.add(rs.getString("emp_name"));
		}
		log.info("getUserName end");
		return usernameList;
	}
}
