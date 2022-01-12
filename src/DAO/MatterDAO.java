package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 案件情報データベースと接続するためのDAOクラス
 *
 */
public class MatterDAO{
	
	//唯一のインスタンス生成
	private static MatterDAO only_instance = new MatterDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(MatterDAO.class);
	//Exception専用ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	
	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	private ResultSet rs;
	
	/**
	 * privateのため、新規のインスタンスを作らせない
	 */
	private MatterDAO() {}
	
	/**
	 * 唯一のインスタンスを取得(=シングルトン)
	 * @return MatterDAOの唯一のインスタンス
	 */
	public static MatterDAO getInstance() {
<<<<<<< HEAD
=======
		log.info("getInstance start");
		log.info("getInstance end");
>>>>>>> 80cffd47c5175d81e090e931f673087ded55d4bc
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
	 * 入力された「案件コード」または、「案件名」から適合する案件名を取得し、リスト化するメソッド
	 * @param code　案件コード
	 * @param name　案件名
	 * @return　matterList 案件コードおよび案件名の情報を格納したリスト
	 * @throws SQLException　データベース処理に問題が発生した場合
	 */
	public ArrayList<String> getMatterNameInfo(Integer code, String name) throws SQLException{
		log.info("getMatterNameInfo start");
		
		//格納用リスト変数宣言
		ArrayList<String> mattenamerList = new ArrayList<String>();
		String sql = null;
		if(code != null && name != null) {
			sql = "select matter_name from matter where matter_code like '" + code + "%';";
			rs= st.executeQuery(sql);
			log.debug("rs : {}", rs);
			while(rs.next()) {
				mattenamerList.add(rs.getString("matter_name"));
			}
		} else if(code != null && name == null) {
			sql = "select matter_name from matter where matter_code like '" + code + "'%;";
			rs= st.executeQuery(sql);
			log.debug("rs : {}", rs);
			while(rs.next()) {
				mattenamerList.add(rs.getString("matter_name"));
			}
		} else if(code == null && name != null) {
			sql = "select matter_name from matter where matter_name like '%" + name + "%';";
			rs= st.executeQuery(sql);
			log.debug("rs : {}", rs);
			while(rs.next()) {
				mattenamerList.add(rs.getString("matter_name"));
			}
		}
		log.info("getMatterNameInfo end");
		return mattenamerList;
	}
	
	/**
	 * 入力されたキーワードから適合する案件コードを取得し、リスト化するメソッド
	 * @param code　入力された案件コード
	 * @param name　入力された案件名
	 * @return　matterList 案件コードおよび案件名の情報を格納したリスト
	 * @throws SQLException　データベース処理に問題が発生した場合
	 */
	public ArrayList<Integer> getMatterCodeInfo(Integer code, String name) throws SQLException{
		log.info("getMatterCodeInfo start");
		
		//格納用リスト変数宣言
		ArrayList<Integer> mattecoderList = new ArrayList<Integer>();
		String sql = null;
		if(code != null && name != null) {
			sql = "select matter_code from matter where matter_code like '" + code + "%';";
			rs= st.executeQuery(sql);
			log.debug("rs : {}", rs);
			while(rs.next()) {
				mattecoderList.add(rs.getInt("matter_code"));
			}
		} else if(code != null && name == null) {
			sql = "select matter_code from matter where matter_code like '" + code + "'%;";
			rs= st.executeQuery(sql);
			log.debug("rs : {}", rs);
			while(rs.next()) {
				mattecoderList.add(rs.getInt("matter_code"));
			}
		} else if(code == null && name != null) {
			sql = "select matter_code from matter where matter_name like '%" + name + "%';";
			rs= st.executeQuery(sql);
			log.debug("rs : {}", rs);
			while(rs.next()) {
				mattecoderList.add(rs.getInt("matter_code"));
			}
		}
		log.info("getMatterCodeInfo end");
		return mattecoderList;
	}
	
	/**
	 * 新規の工数情報をデータベースに登録するメソッド
	 * @param code 工数コード
	 * @param name 工数名
	 * @return 登録情報が登録できた場合は「true」、登録できなかった場合は「false」を返却
	 * @throws SQLException
	 */
	public boolean setMatterInfo(Integer [] code, String [] name) throws SQLException{
		log.info("setMatter start");
		
		//引数のNULLチェック
		if(code == null || name == null) {
			log.error("Value is NULL");
			throw new NullPointerException();
		}
		
		Integer [] codeList = new Integer[code.length];
		String [] nameList = new String[name.length];
		
		//処理結果格納用変数宣言（初期値：false）
		boolean result = false;
		
		for(int i=0; i<code.length; i++) {
			if(code[i] == null || name[i] == null) {
				continue;
			}
			codeList[i] = code[i];
			nameList[i] = name[i];
		}
		
		//引数の情報分データ存在チェックおよびデータ登録
		for(int i=0; i<codeList.length; i++) {
			//自動コミットモード解除
			cnct.setAutoCommit(false);
			
			//すでに登録されていた場合は、「false」を返却
			String sql = "select man_hour_code from man_hour where man_hour_name = '" + nameList[i] + "';";
			rs = st.executeQuery(sql);
			log.debug("rs:{}", rs);
			if(rs.next()) {
				result = false;
			} else {
				sql = "insert into man_hour (man_hour_code, man_hour_name) values ('" + codeList[i] + "', '" + nameList[i] + "');";
				st.executeUpdate(sql);
				cnct.commit();
				log.info("setMatter end");
				result = true;
//				sql = "select matter_code from matter where matter_name = '" + nameList[i] + "';";
//				rs = st.executeQuery(sql);
//				log.debug("rs:{}", rs);
//				if(rs.next()) {
//					sql = "insert into man_hour (man_hour_code, man_hour_name) values ('" + codeList[i] + "', '" + nameList[i] + "');";
//					st.executeUpdate(sql);
//					cnct.commit();
//					log.info("setMatter end");
//					result = true;
//				} else {
//					result = false;
//				}
			}
		}
		return result;
	}
	
	/**
	 * 工数名に該当する工数情報を削除するメソッド
	 * @param code 案件コード
	 * @return 削除が行えた場合は「true」、削除が行えなかった場合は「false」を返却
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public boolean deleteMatter(String name) throws SQLException{
		log.info("deleteMatter start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//データが存在しない場合は、「false」を返却
		String sql = "select man_hour_name from man_hour where man_hour_name = '" + name + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		if(!rs.next()) {
			return false;
		} else {
			sql = "delete from man_hour where man_hour_name = '" + name + "';";
			st.executeUpdate(sql);
			cnct.commit();
			log.info("deleteMatter end");
			return true;
		}
	}
}
