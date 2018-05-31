package com.eth.jobs;


import java.io.IOException;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.sample.Application;

import com.eth.datadeal.EthDataDeal;
import com.eth.datadeal.TBCPendingDeal;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;


@DisallowConcurrentExecution
public class QuartzJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static final int THREAD = 20;
	private static final String URL = "https://www.baidu.com";
	private static final Web3j web3j = Web3j.build(new HttpService(PropKit.get("ethnet")));

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String group = context.getJobDetail().getKey().getGroup();
		logger.info(group + " - start - ");
		try {
			int end = web3j.ethBlockNumber().send().getBlockNumber().intValue();
			int start = end -2;
			EthDataDeal eth = new EthDataDeal();
			eth.dealBlocks(start, end);
			TBCPendingDeal pend = new TBCPendingDeal();
			pend.dealPending();
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info(group + " - end - ");
	}
	public static void main(String[] args) {
//		LynxUtil.startARP();
		new QuartzPlugin().start();
		JobBean job = new JobBean();
		job.setJobClass("com.eth.jobs.QuartzJob");
		job.setCronExpression("* * * * * ?");
		job.setJobGroup("任务1");
		job.setJobDesc("任务1");
		job.setJobCat("748");
		job.setJobSpiderType("");
		QuartzPlugin.addJob(job);
	}
}

