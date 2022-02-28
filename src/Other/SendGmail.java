package Other;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 所定のメールアドレスに対して、Gmailを送信するクラス
 * 
 *
 */
public class SendGmail {
	private static final Logger log = LogManager.getLogger(SendGmail.class);
	
	private static final String UNSUPPORTEDENCODINGEXCEPTION = "UnsupportedEncodingException occured";
	private static final String MESSAGINGEXCEPTION = "MessagingException occured";
	
	private static SendGmail only_instance = new SendGmail();
	
	private SendGmail() {}
	
	public static SendGmail getInstance(){
		return only_instance;
	}
	
	/**
	 * メール送信処理を実行するメソッド
	 * @param sendadress　送信先Gmailアカウントのメールアドレス
	 * @param sendaddresspass　送信先Gmailアカウントのパスワード
	 * @param to_address　送信先Gmailアドレス
	 * @param to_name　送信先名
	 * @param from_address　送信元Gmailアドレス
	 * @param from_name　送信元名
	 * @param subject　送信するメールの件名
	 * @param title　送信するメールの本文
	 */
	public void SendMail(String sendadress, String sendaddresspass, String to_address, String to_name, 
			String from_address, String from_name, String subject, String text) {
		
		log.info("SendMail start");
		
		try {
			Properties property = new Properties();
			property.put("mail.smtp.host", "smtp.gmail.com");
			property.put("mail.smtp.auth", "true");
			property.put("mail.smtp.starttls.enable", "true");
			property.put("mail.smtp.host", "smtp.gmail.com");
			property.put("mail.smtp.port", "587");
			property.put("mail.smtp.debug", "true");

			Session session = Session.getInstance(property, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(sendadress, sendaddresspass);
				}
			});
			
			MimeMessage mimeMessage = new MimeMessage(session);
			//メール受信者
			InternetAddress toAddress = new InternetAddress(to_address, to_name);
			mimeMessage.setRecipient(Message.RecipientType.TO, toAddress);
			//メール送信者
			InternetAddress fromAddress = new InternetAddress(from_address, from_name);
			mimeMessage.setFrom(fromAddress);
			//メール件名
			mimeMessage.setSubject(subject, "ISO-2022-JP");
			//メール本文
			mimeMessage.setText(text, "ISO-2022-JP");
			Transport.send(mimeMessage);
			System.out.println("メール送信が完了しました。");
		} catch(UnsupportedEncodingException de) {
			log.error("エラーが発生したため、メール送信が完了できませんでした。");
			log.error(UNSUPPORTEDENCODINGEXCEPTION);
			de.printStackTrace();
			return;
		} catch(MessagingException me) {
			log.error("エラーが発生したため、メール送信が完了できませんでした。");
			log.error(MESSAGINGEXCEPTION);
			me.printStackTrace();
			return;
		} finally {}
		
		log.info("SendMail end");
	}
}
