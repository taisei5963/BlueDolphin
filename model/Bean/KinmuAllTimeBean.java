package Bean;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 全出退勤時間管理Beanクラス
 *
 */
public class KinmuAllTimeBean {
	//出勤日
	private LocalDate workDate;
	//出勤時間
	private LocalTime startTime;
	//退勤時間
	private LocalTime endTime;
	//実動時間
	private LocalTime actualTime;
	//残業時間
	private LocalTime zangyoTime;
	//曜日
	private String yobi;
	//備考
	private String remarks;
	
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	public LocalTime getActualTime() {
		return actualTime;
	}
	public void setActualTime(LocalTime actualTime) {
		this.actualTime = actualTime;
	}
	public LocalTime getZangyoTime() {
		return zangyoTime;
	}
	public void setZangyoTime(LocalTime zangyoTime) {
		this.zangyoTime = zangyoTime;
	}
	public LocalDate getWorkDate() {
		return workDate;
	}
	public void setWorkDate(LocalDate workDate) {
		this.workDate = workDate;
	}
	public String getYobi() {
		return yobi;
	}
	public void setYobi(String yobi) {
		this.yobi = yobi;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
