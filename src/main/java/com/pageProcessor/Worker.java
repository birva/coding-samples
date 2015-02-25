package com.pageProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import com.pageProcessor.db.DBOperations;

public class Worker implements Runnable{

	private final BlockingQueue<JobInfo> queue;
	private final Vector<String> jobsInQue;
	
	public Worker(BlockingQueue<JobInfo> queue, Vector<String> idsInQue) {
		this.queue = queue;
		this.jobsInQue = idsInQue;
	}

	@Override
	public void run() {
		synchronized (queue) {
			while(queue.isEmpty()) {
				try {
					queue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		}
		
		JobInfo jobInfo = queue.poll();
		URL url = jobInfo.getUrl();
		try {
			String pageData = readDataFromUrl(url);
			DBOperations.storePage(jobInfo.getJobId(), url.toString(), pageData);
			jobsInQue.remove(jobInfo.getJobId());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String readDataFromUrl(URL url) throws MalformedURLException, IOException {
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(url.openStream()));
		StringBuilder pageContent = new StringBuilder();
        String contentLine;
        while ((contentLine = in.readLine()) != null)
            pageContent.append(contentLine);
        in.close();
		return pageContent.toString();
	}

}
