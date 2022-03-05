package Other;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * String型暗号化クラス
 * 
 */
public class StringEncrypt {
	
	private static final Logger log = LogManager.getLogger(StringEncrypt.class);
	private static StringEncrypt only_instance = new StringEncrypt();
	
	private StringEncrypt() {}
	
	public static StringEncrypt getInstance() {
		return only_instance;
	}
	
	/**
	 * パスワードでメッセージダイジェストを作成し文字列化して返す
	 * @param encript_str　暗号化する文字列
	 * @return　生成したメッセージダイジェスト
	 * @throws NoSuchAlgorithmException　Java実行環境にSHA-1が実装されていない場合
	 */
	public String getHashEncrypt(String encript_str) throws NoSuchAlgorithmException {
		log.info("getHashEncrypt start");
		
		StringBuilder sb = new StringBuilder();
		
		if(encript_str != null && !encript_str.isEmpty()) {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(encript_str.getBytes());
			byte[] digest = md.digest();
			
			for(byte b : digest) {
				sb.append((int)b&0xFF);
			}
		}
		log.info("getHashEncrypt end");
		return sb.toString();
	}
}
