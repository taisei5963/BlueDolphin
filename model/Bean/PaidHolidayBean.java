package Bean;

/**
 * 休暇申請管理Beanクラス
 *
 */
public class PaidHolidayBean {
	private int digestionDays, undigestionDays;
	private String periodUse;
	private double digestionTimes, undigestionTimes;
	
	public int getDigestionDays() {
		return digestionDays;
	}
	public void setDigestionDays(int digestionDays) {
		this.digestionDays = digestionDays;
	}
	public int getUndigestionDays() {
		return undigestionDays;
	}
	public void setUndigestionDays(int undigestionDays) {
		this.undigestionDays = undigestionDays;
	}
	public String getPeriodUse() {
		return periodUse;
	}
	public void setPeriodUse(String periodUse) {
		this.periodUse = periodUse;
	}
	public double getDigestionTimes() {
		return digestionTimes;
	}
	public void setDigestionTimes(double digestionTimes) {
		this.digestionTimes = digestionTimes;
	}
	public double getUndigestionTimes() {
		return undigestionTimes;
	}
	public void setUndigestionTimes(double usdigestionTimes) {
		this.undigestionTimes = usdigestionTimes;
	}
}
