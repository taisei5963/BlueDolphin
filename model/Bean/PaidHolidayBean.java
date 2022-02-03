package Bean;

/**
 * 休暇申請管理Beanクラス
 *
 */
public class PaidHolidayBean {
	private int digestionDays, undigestionDays;
	private String periodUse;
	
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
}
