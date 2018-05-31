package com.eth.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.sample.Application;

import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.IPlugin;




public class QuartzPlugin implements IPlugin {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	private List<JobBean> jobs = new ArrayList<JobBean>();
	private SchedulerFactory sf;
	private static Scheduler scheduler;
	private String jobConfig;
	private String confConfig;
 
	public QuartzPlugin(String jobConfig, String confConfig) {
		this.jobConfig = jobConfig;
		this.confConfig = confConfig;
	} 
	public QuartzPlugin(String jobConfig) {
		this.jobConfig = jobConfig;
	} 
	public QuartzPlugin() {
	}
 
	public static void addJob(JobBean job) {
		logger.info("Start Jobssss");
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobDesc(), job.getJobGroup());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 不存在，创建一个
			if (trigger == null) {
		        Class<Job> jobClass = (Class<Job>) Class.forName(job.getJobClass());
				JobDataMap jobDataMap = new JobDataMap();
				jobDataMap.put("cat", job.getJobCat());
				jobDataMap.put("spiderType", job.getJobSpiderType());
				JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(job.getJobDesc(), job.getJobGroup()).setJobData(jobDataMap).build();
 
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression()); 
				// 按新的cronExpression表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobDesc(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
				try {
					scheduler.scheduleJob(jobDetail, trigger);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// Trigger已存在，那么更新相应的定时设置
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression()); 
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build(); 
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			}
		} catch (Exception e) {
			logger.info(e.toString());
		}
	}
 
	@Override
	public boolean start() {
		loadJobsFromProperties();
		startJobs();
		return true;
	}
 
	private void startJobs() {
		try {
			if (StrKit.notBlank(confConfig)) {
				sf = new StdSchedulerFactory(confConfig);
			} else {
				sf = new StdSchedulerFactory();
			}
			scheduler = sf.getScheduler();
		} catch (SchedulerException e) {
			logger.debug(e.toString());		
			}
		for (JobBean entry : jobs) {
			addJob(entry);
		}
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			logger.debug(e.toString());		
		}
	}
 
	private void loadJobsFromProperties() {
		String jobArray = PropKit.get("jobArray");
		if (StrKit.isBlank(jobArray)) {
			return;
		}
		String[] jobArrayList = jobArray.split(",");
		for (String jobName : jobArrayList) {
			jobs.add(createJobBean(jobName));
		}
	}
 
	private JobBean createJobBean(String key) {
		JobBean job = new JobBean();
		job.setJobClass(PropKit.get(key + ".job"));
		job.setCronExpression(PropKit.get(key + ".cron"));
		job.setJobGroup(PropKit.get(key + ".desc"));
		job.setJobDesc(PropKit.get(key + ".desc"));
		job.setJobCat(PropKit.get(key + ".cat"));
		job.setJobSpiderType(PropKit.get(key + ".spiderType"));
		return job;
	}
 
	@Override
	public boolean stop() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			logger.info(e.toString());		
		}
		return true;
	}
}