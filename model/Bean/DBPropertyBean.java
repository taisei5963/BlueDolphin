package Bean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * MySQL管理Beanクラス
 *
 */
public class DBPropertyBean {

	private static final Logger log = LogManager.getLogger(DBPropertyBean.class);
	//ログ専用定数宣言
	private static final String IOEXCEPTION = "IOException occured";
	private static final String FILENOTFOUNDEXCEPTION = "FileNotFoundException occured";

	private String uri, user, pass, driver;

	public String getUri() {
		return uri;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public String getDriver() {
		return driver;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public ArrayList<DBPropertyBean> getProperty(){
		log.info("getProperty START");

		ArrayList<DBPropertyBean> list = new ArrayList<DBPropertyBean>();
		Properties pro = new Properties();
		DBPropertyBean db = new DBPropertyBean();

		//プロパティファイルの絶対パス
		String file = "/Applications/Eclipse_2021-03.app/Contents/workspace/BlueDolphin/WebContent/common/properties/db.properties";

		try {
			FileInputStream fis = new FileInputStream(file);

			try {
				pro.load(fis);

				db.setUri(pro.getProperty("MYSQL_URI"));
				db.setUser(pro.getProperty("MYSQL_USER"));
				db.setPass(pro.getProperty("MYSQL_PASSWORD"));
				db.setDriver(pro.getProperty("MYSQL_DRIVER"));
				list.add(db);

			} catch (IOException e) {
				log.error(IOEXCEPTION);
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			log.error(FILENOTFOUNDEXCEPTION);
			e.printStackTrace();
		}
		log.info("getProperty END");
		return list;
	}
}
