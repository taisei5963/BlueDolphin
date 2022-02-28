package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * 申請番号データベースと接続するためのDAOクラス
 *
 */
public class ApplicationNumberDAO{
	
	//唯一のインスタンス生成
	private static ApplicationNumberDAO only_instance = new ApplicationNumberDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(ApplicationNumberDAO.class);
	//ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	private ResultSet rs;
	
	/**
	 * privateのため、新規のインスタンスを作らせない
	 */
	private ApplicationNumberDAO() {
	}
	
	/**
	 * 唯一のインスタンスを取得(=シングルトン)
	 * @return KintaiDAOの唯一のインスタンス
	 */
	public static ApplicationNumberDAO getInstance() {
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
	 * 申請番号がすでに存在しているものかを判断するメソッド
	 * @param number 判断対象の申請番号
	 * @return 同じ申請番号が存在している場合は、FALSEを返却<br>同じ申請番号が存在していない場合は、TRUEを返却
	 * @throws SQLException データベース接続に問題が発生した場合
	 */
	public boolean checkApplicationNumber(Integer number) throws SQLException{
		log.info("checkApplicationNumber start");
		//SQL文
		String sql = "select number from application_number where number = '" + number + "';";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		
		//存在している申請番号の場合は、falseを返却
		if(rs.next()) {
			return false;
		} else {
			log.info("checkApplicationNumber end");
			return true;
		}
	}
	
	/**
	 * 新しく作成した申請番号をデータベースに登録するメソッド
	 * @param number 申請番号
	 * @throws SQLException データベース接続に問題が発生した場合
	 */
	public void setApplicationNumber(int number) throws SQLException{
		log.info("setApplicationNumber start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//SQL文
		String sql = "insert into application_number (number) values ('" + number + "');";
		st.executeUpdate(sql);
		cnct.commit();
		log.info("setApplicationNumber end");
	}
}
