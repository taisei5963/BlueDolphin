package Bean;


/**
 * 申請内容管理Beanクラス
 *
 */
public class ApplicationContentsBean {
	//申請番号
	private int applicationNumber;
	//申請日
	private String applicationDate;
	//申請者
	private String applicant;
	//申請名称
	private String applicationName;
	//申請種類
	private String applicationType;
	//申請区分
	private String applicationCategory;
	//申請状況
	private String applicationStatus;
	//対象日
	private String targetDate;
	//申請理由
	private String applicationReason;
	//取得時間
	private String acquisitionTime;
	//振休取得日
	private String holidayAcquisitionDate;
	//総稼働時間
	private String totalUptime;
	//総残業時間
	private String totalOvertimeHours;
	//対応日
	private String correspondenceDate;
	//対応者
	private String correspondingPerson;
	//申請者コメント
	private String applicantComment;
	//対応者コメント
	private String approverComment;
	//作業内容
	private String workContents;
	//連絡先
	private String contact;
	//作業時間
	private String workingHours;
	//修正後出勤時刻
	private String correctedWorkTime;
	//修正後退勤時刻
	private String correctedRetreatTime;
	
	//セッター・ゲッター
	public int getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(int applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public String getApplicationDate() {
		return applicationDate;
	}
	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
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
	public String getApplicationCategory() {
		return applicationCategory;
	}
	public void setApplicationCategory(String applicationCategory) {
		this.applicationCategory = applicationCategory;
	}
	public String getApplicationStatus() {
		return applicationStatus;
	}
	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	public String getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	public String getApplicationReason() {
		return applicationReason;
	}
	public void setApplicationReason(String applicationReason) {
		this.applicationReason = applicationReason;
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
	public String getCorrespondenceDate() {
		return correspondenceDate;
	}
	public void setCorrespondenceDate(String correspondenceDate) {
		this.correspondenceDate = correspondenceDate;
	}
	public String getCorrespondingPerson() {
		return correspondingPerson;
	}
	public void setCorrespondingPerson(String correspondingPerson) {
		this.correspondingPerson = correspondingPerson;
	}
	public String getApplicantComment() {
		return applicantComment;
	}
	public void setApplicantComment(String applicantComment) {
		this.applicantComment = applicantComment;
	}
	public String getApproverComment() {
		return approverComment;
	}
	public void setApproverComment(String approverComment) {
		this.approverComment = approverComment;
	}
	public String getWorkContents() {
		return workContents;
	}
	public void setWorkContents(String workContents) {
		this.workContents = workContents;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getWorkingHours() {
		return workingHours;
	}
	public void setWorkingHours(String workingHours) {
		this.workingHours = workingHours;
	}
	public String getCorrectedWorkTime() {
		return correctedWorkTime;
	}
	public void setCorrectedWorkTime(String correctedWorkTime) {
		this.correctedWorkTime = correctedWorkTime;
	}
	public String getCorrectedRetreatTime() {
		return correctedRetreatTime;
	}
	public void setCorrectedRetreatTime(String correctedRetreatTime) {
		this.correctedRetreatTime = correctedRetreatTime;
	}
}
