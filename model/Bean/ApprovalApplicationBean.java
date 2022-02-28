package Bean;


/**
 * 承認申請情報管理Beanクラス
 *
 */
public class ApprovalApplicationBean {
	
	private String applicant, application_date, application_time, approval_date, approval_time, application_status, application_title;
	private Integer application_number;
	
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public String getApplication_date() {
		return application_date;
	}
	public void setApplication_date(String application_date) {
		this.application_date = application_date;
	}
	public String getApplication_time() {
		return application_time;
	}
	public void setApplication_time(String application_time) {
		this.application_time = application_time;
	}
	public String getApproval_date() {
		return approval_date;
	}
	public void setApproval_date(String approval_date) {
		this.approval_date = approval_date;
	}
	public String getApproval_time() {
		return approval_time;
	}
	public void setApproval_time(String approval_time) {
		this.approval_time = approval_time;
	}
	public String getApplication_status() {
		return application_status;
	}
	public void setApplication_status(String application_status) {
		this.application_status = application_status;
	}
	public Integer getApplication_number() {
		return application_number;
	}
	public void setApplication_number(Integer application_number) {
		this.application_number = application_number;
	}
	public String getApplication_title() {
		return application_title;
	}
	public void setApplication_title(String application_title) {
		this.application_title = application_title;
	}
}
