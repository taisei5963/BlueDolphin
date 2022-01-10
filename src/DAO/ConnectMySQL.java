package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Bean.PropertyBean;

/**
 * データベース（MySQL）へのアクセス専用クラス
 *
 */
public class ConnectMySQL {
	
	//ログ取得
	private static final Logger log = LogManager.getLogger(ConnectMySQL.class);
	//唯一のインスタンスの生成
	private static ConnectMySQL only_instance = new ConnectMySQL();
	//ログ出力文言宣言
	private static final String CLASSNOTFOUNDEXCEPTION = "ClassNotFoundException occurred";
	
	//JDBCサーバ接続関連変数宣言
	PropertyBean pb = new PropertyBean();
	ArrayList<PropertyBean> pblist = pb.getProperty();
	String uri = null, user = null, pw = null, driver = null;
	
	//privateのため、新規のインスタンスを生成させない
	private ConnectMySQL() {
		log.info("ConnectMySQL start");
		log.info("ConnectMySQL end");
	}
	
	/**
	 * 唯一のインスタンスの取得
	 * @return ConnectMySQLの唯一のインスタンス
	 */
	public static ConnectMySQL getInstance() {
		log.info("getInstance start");
		log.info("getInstance end");
		return only_instance;
	}
	
	/**
	 * DBへの接続を行うメソッド
	 * @return 接続情報
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public Connection cnct() throws SQLException {
		log.info("cnct start");
		for(PropertyBean pb : pblist) {
			uri = pb.getUri();
			user = pb.getUser();
			pw = pb.getPass();
			driver = pb.getDriver();
		}
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			log.error(CLASSNOTFOUNDEXCEPTION);
			e.printStackTrace();
		}
		log.info("cnct end");
		return DriverManager.getConnection(uri, user, pw);
	}
}
