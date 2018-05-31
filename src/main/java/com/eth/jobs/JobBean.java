package com.eth.jobs;

public class JobBean {
	 
	/** 任务id */
	private String jobId;
 
	/** 任务描述 */
	private String jobDesc;
 
	/** 任务运行时间表达式 */
	private String cronExpression;
 
	/** 任务分组 */
	private String jobGroup;
 
	/** 任务类 */
	private String jobClass;
	
	/** 爬虫类型 */
	private String jobSpiderType;
	
	/** 商品类型 */
	private String jobCat;
 
	public String getJobId() {
		return jobId;
	}
 
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
 
	public String getJobDesc() {
		return jobDesc;
	}
 
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
 
	public String getCronExpression() {
		return cronExpression;
	}
 
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
 
	public String getJobGroup() {
		return jobGroup;
	}
 
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
 
	public String getJobClass() {
		return jobClass;
	}
 
	public void setJobSpiderType(String jobSpiderType) {
		this.jobSpiderType = jobSpiderType;
	}
	
	public String getJobSpiderType() {
		return jobSpiderType;
	}
 
	public void setJobCat(String cat) {
		this.jobCat = cat;
	}

	public String getJobCat() {
		return jobCat;
	}
 
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
 
	public JobBean(String jobId, String jobDesc, String cronExpression, String jobGroup, String jobClass, String jobSpiderType, String jobCat) {
		this.jobId = jobId;
		this.jobDesc = jobDesc;
		this.cronExpression = cronExpression;
		this.jobGroup = jobGroup;
		this.jobClass = jobClass;
		this.jobSpiderType = jobSpiderType;
		this.jobCat = jobCat;
	}
 
	public JobBean() {
		super();
	}
}
