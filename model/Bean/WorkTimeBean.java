package Bean;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 作業時間管理Beanクラス
 *
 */
public class WorkTimeBean {
	//休憩開始時刻
	private LocalTime breakStartTime = LocalTime.of(12, 0);
	//休憩終了時刻
	private LocalTime breakEndTime = LocalTime.of(13, 0);
	//出勤日
	private LocalDate workDate;
	//出勤時刻
	private LocalTime workStartTime;
	//退勤時刻
	private LocalTime workEndTime;
	//休憩時間
	private Duration breakWorkTime;
	//稼働時間
	private Duration workActualTime;
	//残業時間
	private Duration workZangyoTime;
	
	public LocalDate getWorkDate() {
		return workDate;
	}
	public void setWorkDate(LocalDate workDate) {
		this.workDate = workDate;
	}
	public LocalTime getWorkStartTime() {
		return workStartTime;
	}
	public void setWorkStartTime(LocalTime workStartTime) {
		this.workStartTime = workStartTime;
	}
	public LocalTime getWorkEndTime() {
		return workEndTime;
	}
	public void setWorkEndTime(LocalTime workEndTime) {
		this.workEndTime = workEndTime;
	}
	public Duration getBreakWorkTime() {
		return breakWorkTime;
	}
	public void setBreakWorkTime(Duration breakWorkTime) {
		this.breakWorkTime = breakWorkTime;
	}
	public Duration getWorkActualTime() {
		return workActualTime;
	}
	public void setWorkActualTime(Duration workActualTime) {
		this.workActualTime = workActualTime;
	}
	public Duration getWorkZangyoTime() {
		return workZangyoTime;
	}
	public void setWorkZangyoTime(Duration workZangyoTime) {
		this.workZangyoTime = workZangyoTime;
	}
	
	/**
	 * 休憩開始時間と休憩終了時間から休憩時間を自動計算する。
	 */
	public void calcBreakTime() {
		Duration duration = Duration.between(breakStartTime, breakEndTime);
		setBreakWorkTime(duration);
	}
	
	/**
	 * 出勤時間と退勤時間から勤務時間を自動計算する。<br>
	 * 休憩時間があるときは勤務時間から休憩時間を引く。
	 */
	public void calcWorkingHours() {
		Duration duration = Duration.between(workStartTime, workEndTime);
		setWorkActualTime(duration);
		if(breakWorkTime != null) {
			duration = workActualTime.minus(breakWorkTime);
			setWorkActualTime(duration);
		}
	}
}
