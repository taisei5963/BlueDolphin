package Bean;


/**
 * 申請内容詳細情報管理Beanクラス
 *
 */
public class ApprovalApplicationDetailsBean {
	
	//申請番号
	private int applicationNumber;
	//申請名称
	private String applicationName;
	//申請種類
	private String applicationType;
	//申請理由
	private String applicationReason;
	//対象日
	private String targetDate;
	//取得時間
	private String acquisitionTime;
	//振休取得日
	private String holidayAcquisitionDate;
	//総稼働時間
	private String totalUptime;
	//総残業時間
	private String totalOvertimeHours;
	
	public int getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(int applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	public String getApplicationReason() {
		return applicationReason;
	}
	public void setApplicationReason(String applicationReason) {
		this.applicationReason = applicationReason;
	}
	public String getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	public String getAcquisitionTime() {
		return acquisitionTime;
	}
	public void setAcquisitionTime(String acquisitionTime) {
		this.acquisitionTime = acquisitionTime;
	}
	public String getHolidayAcquisitionDate() {
		return holidayAcquisitionDate;
	}
	public void setHolidayAcquisitionDate(String holidayAcquisitionDate) {
		this.holidayAcquisitionDate = holidayAcquisitionDate;
	}
	public String getTotalUptime() {
		return totalUptime;
	}
	public void setTotalUptime(String totalUptime) {
		this.totalUptime = totalUptime;
	}
	public String getTotalOvertimeHours() {
		return totalOvertimeHours;
	}
	public void setTotalOvertimeHours(String totalOvertimeHours) {
		this.totalOvertimeHours = totalOvertimeHours;
	}
}