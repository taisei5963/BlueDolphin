package Bean;

/**
 * 出退勤時間管理Beanクラス
 *
 */
public class KinmuTimeBean {
	//出勤時刻
	private String workStartTime, workEndTime;
	
	public String getWorkStartTime() {
		return workStartTime;
	}
	public void setWorkStartTime(String workStartTime) {
		this.workStartTime = workStartTime;
	}
	public String getWorkEndTime() {
		return workEndTime;
	}
	public void setWorkEndTime(String workEndTime) {
		this.workEndTime = workEndTime;
	}
}
