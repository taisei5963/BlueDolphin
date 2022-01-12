package Bean;

/**
 * 勤怠更新情報管理Beanクラス
 *
 */
public class KintaiUpdateBean{

	private String syukkin, taikin, date;

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSyukkin() {
		return syukkin;
	}
	public void setSyukkin(String syukkin) {
		this.syukkin = syukkin;
	}
	public String getTaikin() {
		return taikin;
	}
	public void setTaikin(String taikin) {
		this.taikin = taikin;
	}
}