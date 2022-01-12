package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Bean.MatterBean;

/**
 * 工数情報データベースと接続するためのDAOクラス
 *
 */
public class ManHourDAO{
	
	//唯一のインスタンス生成
	private static ManHourDAO only_instance = new ManHourDAO();
	//ログ取得変数宣言
	private static final Logger log = LogManager.getLogger(ManHourDAO.class);
	//Exception専用ログ出力文言宣言
	private static final String SQLEXCEPTION = "SQLException occurred";
	private static final String NULLPOINTEREXCEPTION = "NullPointerException occurred";
	
	//データベース接続時の各変数宣言
	private Connection cnct;
	private Statement st;
	private ResultSet rs;
	
	/**
	 * privateのため、新規のインスタンスを作らせない
	 */
	private ManHourDAO() {}
	
	/**
	 * 唯一のインスタンスを取得(=シングルトン)
	 * @return ManHourDAOの唯一のインスタンス
	 */
	public static ManHourDAO getInstance() {
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
	 * 「工数コード」を取得するメソッド
	 * @return manhourcodeList 「工数コード」情報を格納したリスト
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public ArrayList<Integer> getManHourCode() throws SQLException{
		log.info("getManHourCode start");
		
		//格納用リストのインスタンス生成
		ArrayList<Integer> manhourcodeList = new ArrayList<Integer>();
		String sql = "select man_hour_code from man_hour;";
		rs= st.executeQuery(sql);
		log.debug("rs : {}", rs);
		while(rs.next()) {
			manhourcodeList.add(rs.getInt("man_hour_code"));
			log.info("getManHourCode end");
		}
		return manhourcodeList;
	}
	
	/**
	 * 「工数名」を取得するメソッド
	 * @return manhournameList 「工数名」情報を格納したリスト
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public ArrayList<String> getManHourName() throws SQLException{
		log.info("getManHourName start");
		
		//格納用リストのインスタンス生成
		ArrayList<String> manhournameList = new ArrayList<String>();
		String sql = "select man_hour_name from man_hour;";
		rs= st.executeQuery(sql);
		log.debug("rs : {}", rs);
		while(rs.next()) {
			manhournameList.add(rs.getString("man_hour_name"));
			log.info("getManHourName end");
		}
		return manhournameList;
	}
	
	/**
	 * 「案件コード」および「案件名」を取得するメソッド
	 * @return matterList 「案件コード」、「案件名」を格納したリスト
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public ArrayList<MatterBean> getMatter() throws SQLException{
		log.info("getMatter start");
		
		//格納用リストのインスタンス生成
		ArrayList<MatterBean> matterList = new ArrayList<MatterBean>();
		String sql = "select matter_code, matter_name from matter;";
		rs= st.executeQuery(sql);
		log.debug("rs : {}", rs);
		while(rs.next()) {
			MatterBean mhb = new MatterBean();
			mhb.setMcode(rs.getInt("matter_code"));
			mhb.setMname(rs.getString("matter_name"));
			matterList.add(mhb);
			log.info("getMatter end");
		}
		return matterList;
	}
	
	/**
	 * 新規の工数情報をデータベースに登録するメソッド
	 * @param code 工数コード
	 * @param name 工数名
	 * @return 登録情報が登録できた場合は「true」、登録できなかった場合は「false」を返却
	 * @throws SQLException
	 */
	public boolean setMatter(String name) throws SQLException{
		log.info("setMatter start");
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		//すでに登録されていた場合は、「false」を返却
		String sql = "select man_hour_code from man_hour where man_hour_name = '" + name + "';";
		rs = st.executeQuery(sql);
		log.debug("rs:{}", rs);
		if(rs.next()) {
			return false;
		} else {
			sql = "select matter_code from matter where matter_name = '" + name + "';";
			rs = st.executeQuery(sql);
			log.debug("rs:{}", rs);
			if(rs.next()) {
				int code = rs.getInt("matter_code");
				sql = "insert into man_hour (man_hour_code, man_hour_name) values ('" + code + "', '" + name + "');";
				st.executeUpdate(sql);
				cnct.commit();
				log.info("setMatter end");
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * 指定日の稼働時間を取得するメソッド
	 * @param date 取得する日付
	 * @return actual 実動時間
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public String getActual(String date) throws SQLException{
		log.info("getActual start");
		String actual = "00:00";
		String sql = "select actual_hours from emp_kintai where emp_workday = '" + date + "';";
		rs= st.executeQuery(sql);
		log.debug("rs : {}", rs);
		if(rs.next()) {
			actual = rs.getString("actual_hours");
		} else {
			return null;
		}
		log.info("getActual end");
		return actual;
	}
	
	/**
	 * 社員工数情報をデータベースへ登録するメソッド
	 * @param emp_num	社員番号
	 * @param date		日付
	 * @param name1		工数名１
	 * @param time1		工数時間１
	 * @param name2		工数名２
	 * @param time2		工数時間２
	 * @param name3		工数名３
	 * @param time3		工数時間３
	 * @param name4		工数名４
	 * @param time4		工数時間４
	 * @param name5		工数名５
	 * @param time5		工数時間５
	 * @param name6		工数名６
	 * @param time6		工数時間６
	 * @param name7		工数名７
	 * @param time7		工数時間７
	 * @param name8		工数名８
	 * @param time8		工数時間８
	 * @param name9		工数名９
	 * @param time9		工数時間９
	 * @param name10	工数名１０
	 * @param time10	工数時間１０
	 * @return	登録処理が行える場合；ture<br>登録処理が行えない場合：false
	 * @throws SQLException データベース処理に問題が発生した場合
	 */
	public boolean setMan_Hour_Employee(Integer emp_num, String date, String [] name, String [] time) throws SQLException{
		log.info("setMan_Hour_Employee start");
		
		//引数のNULLチェック
		if(emp_num == null || date == null || name == null || time == null) {
			log.error(NULLPOINTEREXCEPTION);
			throw new NullPointerException();
		}
		//配列に格納している要素格納用変数宣言
		String nameValue1 = null, nameValue2 = null, nameValue3 = null, nameValue4 = null, nameValue5 = null,
				nameValue6 = null, nameValue7 = null, nameValue8 = null, nameValue9 = null, nameValue10 = null;
		String timeValue1 = null, timeValue2 = null, timeValue3 = null, timeValue4 = null, timeValue5 = null,
				timeValue6 = null, timeValue7 = null, timeValue8 = null, timeValue9 = null, timeValue10 = null;
		
		//処理結果格納用変数宣言
		boolean result = false;
		
		//配列をリストに変換
		ArrayList<String> nameList = new ArrayList<String>(Arrays.asList(name));
		ArrayList<String> timeList = new ArrayList<String>(Arrays.asList(time));
		
		//条件に適合するリストの要素を削除
		for(int j=0; j<time.length; j++) {
			for(int i=0; i<timeList.size(); i++) {
				if(nameList.get(i) != null && timeList.get(i).equals("00:00")) {
					nameList.remove(i);
					timeList.remove(i);
				}
			}
		}
		
		//リストから配列に変換
		String [] namelist = (String[])nameList.toArray(new String[nameList.size()]);
		String [] timelist = (String[])timeList.toArray(new String[timeList.size()]);
		
		switch(namelist.length) {
		case 1:
			nameValue1 = namelist[0];		timeValue1 = timelist[0];
			break;
		case 2:
			nameValue1 = namelist[0];		timeValue1 = timelist[0];
			nameValue2 = namelist[1];		timeValue2 = timelist[1];
			break;
		case 3:
			nameValue1 = namelist[0];		timeValue1 = timelist[0];
			nameValue2 = namelist[1];		timeValue2 = timelist[1];
			nameValue3 = namelist[2];		timeValue3 = timelist[2];
			break;
		case 4:
			nameValue1 = namelist[0];		timeValue1 = timelist[0];
			nameValue2 = namelist[1];		timeValue2 = timelist[1];
			nameValue3 = namelist[2];		timeValue3 = timelist[2];
			nameValue4 = namelist[3];		timeValue4 = timelist[3];
			break;
		case 5:
			nameValue1 = namelist[0];		timeValue1 = timelist[0];
			nameValue2 = namelist[1];		timeValue2 = timelist[1];
			nameValue3 = namelist[2];		timeValue3 = timelist[2];
			nameValue4 = namelist[3];		timeValue4 = timelist[3];
			nameValue5 = namelist[4];		timeValue5 = timelist[4];
			break;
		case 6:
			nameValue1 = namelist[0];		timeValue1 = timelist[0];
			nameValue2 = namelist[1];		timeValue2 = timelist[1];
			nameValue3 = namelist[2];		timeValue3 = timelist[2];
			nameValue4 = namelist[3];		timeValue4 = timelist[3];
			nameValue5 = namelist[4];		timeValue5 = timelist[4];
			nameValue6 = namelist[5];		timeValue6 = timelist[5];
			break;
		case 7:
			nameValue1 = namelist[0];		timeValue1 = timelist[0];
			nameValue2 = namelist[1];		timeValue2 = timelist[1];
			nameValue3 = namelist[2];		timeValue3 = timelist[2];
			nameValue4 = namelist[3];		timeValue4 = timelist[3];
			nameValue5 = namelist[4];		timeValue5 = timelist[4];
			nameValue6 = namelist[5];		timeValue6 = timelist[5];
			nameValue7 = namelist[6];		timeValue7 = timelist[6];
			break;
		case 8:
			nameValue1 = namelist[0];		timeValue1 = timelist[0];
			nameValue2 = namelist[1];		timeValue2 = timelist[1];
			nameValue3 = namelist[2];		timeValue3 = timelist[2];
			nameValue4 = namelist[3];		timeValue4 = timelist[3];
			nameValue5 = namelist[4];		timeValue5 = timelist[4];
			nameValue6 = namelist[5];		timeValue6 = timelist[5];
			nameValue7 = namelist[6];		timeValue7 = timelist[6];
			nameValue8 = namelist[7];		timeValue8 = timelist[7];
			break;
		case 9:
			nameValue1 = namelist[0];		timeValue1 = timelist[0];
			nameValue2 = namelist[1];		timeValue2 = timelist[1];
			nameValue3 = namelist[2];		timeValue3 = timelist[2];
			nameValue4 = namelist[3];		timeValue4 = timelist[3];
			nameValue5 = namelist[4];		timeValue5 = timelist[4];
			nameValue6 = namelist[5];		timeValue6 = timelist[5];
			nameValue7 = namelist[6];		timeValue7 = timelist[6];
			nameValue8 = namelist[7];		timeValue8 = timelist[7];
			nameValue9 = namelist[8];		timeValue9 = timelist[8];
			break;
		case 10:
			nameValue1 = namelist[0];		timeValue1 = timelist[0];
			nameValue2 = namelist[1];		timeValue2 = timelist[1];
			nameValue3 = namelist[2];		timeValue3 = timelist[2];
			nameValue4 = namelist[3];		timeValue4 = timelist[3];
			nameValue5 = namelist[4];		timeValue5 = timelist[4];
			nameValue6 = namelist[5];		timeValue6 = timelist[5];
			nameValue7 = namelist[6];		timeValue7 = timelist[6];
			nameValue8 = namelist[7];		timeValue8 = timelist[7];
			nameValue9 = namelist[8];		timeValue9 = timelist[8];
			nameValue10 = namelist[9];		timeValue10 = timelist[9];
			break;
		default :
			result = false;
			break;
		}
		
		//自動コミットモード解除
		cnct.setAutoCommit(false);
		
		String sql = "select man_hour_name1, man_hour_time1, man_hour_name2, man_hour_time2, "
				+ "man_hour_name3, man_hour_time3, man_hour_name4, man_hour_time4, man_hour_name5, man_hour_time5, "
				+ "man_hour_name6, man_hour_time6, man_hour_name7, man_hour_time7, man_hour_name8, man_hour_time8, "
				+ "man_hour_name9, man_hour_time9, man_hour_name10, man_hour_time10 from emp_man_hour "
				+ "where emp_num = '" + emp_num + "' and  registration_date = '" + date + "'";
		rs = st.executeQuery(sql);
		log.debug("rs : {}", rs);
		//データがすでに存在する場合は、登録処理を行わない
		if(rs.next()) {
			result = false;
		} else {
			sql = "insert into emp_man_hour (emp_num, registration_date, man_hour_name1, man_hour_time1, "
					+ "man_hour_name2, man_hour_time2, man_hour_name3, man_hour_time3, man_hour_name4, man_hour_time4, "
					+ "man_hour_name5, man_hour_time5, man_hour_name6, man_hour_time6, man_hour_name7, man_hour_time7, "
					+ "man_hour_name8, man_hour_time8, man_hour_name9, man_hour_time9, man_hour_name10, man_hour_time10 "
					+ "values ('" + emp_num + "', '" + date + "', '" + nameValue1 + "', '" + timeValue1 + "', '" 
					+ nameValue2 + "', '" + timeValue2 + "', '" + nameValue3 + "', '" + timeValue3 + "', '" + nameValue4 + "', '" + timeValue4 + "', '" 
					+ nameValue5 + "', '" + timeValue5 + "', '" + nameValue6 + "', '" + timeValue6 + "', '" + nameValue7 + "', '" + timeValue7 + "', '"
					+ nameValue8 + "', '" + timeValue8 + "', '" + nameValue9 + "', '" + timeValue9 + "', '" + nameValue10 + "', '" + timeValue10 + "';";
			st.executeUpdate(sql);
			cnct.commit();
			result = true;
		}
		
		log.info("setMan_Hour_Employee end");
		return result;
	}
}
