package com.pageProcessor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.pageProcessor.db.DBOperations;

public class JobProducer {
	
	private BlockingQueue<JobInfo> queue;
	private Vector<String> jobsInQueue;
	private static JobProducer producer;
	private static int WORKER_POOL_SIZE = 5;

	private JobProducer(BlockingQueue<JobInfo> queue, Vector<String> jobsInQue) {
		this.queue = queue;
		this.jobsInQueue = jobsInQue;
	}
	
	public String addJob(String pageUrl) {
		String jobId = UUID.randomUUID().toString();
		try {
			URL url = new URL(pageUrl);
			JobInfo job = new JobInfo(jobId, url);
			synchronized (queue) {
				queue.put(job);
				jobsInQueue.add(jobId);
				queue.notifyAll();				
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "invalidUrl";
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "error";
		}
		return jobId;
	}
	
	public String getJobStatus(String jobId) {
		if(jobsInQueue.contains(jobId))
			return "Waiting";

		String status = DBOperations.getJobStatus(jobId);
		if(status!=null && "".equals(status)) {
			return "NotFound";
		}
		return status;
	}

	public static JobProducer getProducer() {
		if(producer==null) {
			BlockingQueue<JobInfo> queue = new ArrayBlockingQueue<JobInfo>(1);
			Vector<String> jobsInQueue = new Vector<String>();
			producer = new JobProducer(queue, jobsInQueue);
			for(int i=0; i<WORKER_POOL_SIZE; i++) {
				Thread jobProcessor = new Thread(new Worker(queue, jobsInQueue), "Thread"+i);
				jobProcessor.start();
			}		 
		}
		return producer;
	}
}
