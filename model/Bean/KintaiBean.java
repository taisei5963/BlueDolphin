package Bean;

/**
 * 勤怠情報管理Beanクラス
 *
 */
public class KintaiBean{

	private String email, syukkin, taikin, date;
	private double actual, zangyo;
	private int num;

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public double getActual() {
		return actual;
	}
	public void setActual(double actual) {
		this.actual = actual;
	}
	public double getZangyo() {
		return zangyo;
	}
	public void setZangyo(double zangyo) {
		this.zangyo = zangyo;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
}