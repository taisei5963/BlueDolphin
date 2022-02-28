package Bean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 承認者メール情報管理Beanクラス
 *
 */
public class AuthorizerPropertyBean {

	private static final Logger log = LogManager.getLogger(AuthorizerPropertyBean.class);
	//ログ専用定数宣言
	private static final String IOEXCEPTION = "IOException occured";
	private static final String FILENOTFOUNDEXCEPTION = "FileNotFoundException occured";

	//メールアドレス
	private String mailadd;
	//メールアドレス用パスワード
	private String mailpass;

	public String getMailadd() {
		return mailadd;
	}

	public void setMailadd(String mailadd) {
		this.mailadd = mailadd;
	}

	public String getMailpass() {
		return mailpass;
	}

	public void setMailpass(String mailpass) {
		this.mailpass = mailpass;
	}

	public ArrayList<AuthorizerPropertyBean> getProperty(Integer number){
		log.info("getProperty START");

		ArrayList<AuthorizerPropertyBean> list = new ArrayList<AuthorizerPropertyBean>();
		Properties pro = new Properties();
		AuthorizerPropertyBean apb = new AuthorizerPropertyBean();

		//プロパティファイルの絶対パス
		String file = "/Applications/Eclipse_2021-03.app/Contents/workspace/BlueDolphin/WebContent/common/properties/authorizer.properties";

		try {
			FileInputStream fis = new FileInputStream(file);
			pro.load(fis);
			apb.setMailadd(pro.getProperty("AUTHORIZER_ADDRESS_" + number));
			apb.setMailpass(pro.getProperty("AUTHORIZER_ADDRESS_PASS_" + number));
			list.add(apb);
		} catch (FileNotFoundException e) {
			log.error(FILENOTFOUNDEXCEPTION);
			e.printStackTrace();
		} catch (IOException e) {
			log.error(IOEXCEPTION);
			e.printStackTrace();
		}
		log.info("getProperty END");
		return list;
	}
}
