package com.pageProcessor;

import java.net.URL;

public class JobInfo {

	private String jobId;
	private URL url;
	
	public JobInfo(String jobId, URL url) {
		this.jobId = jobId;
		this.url = url;
	}

	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
}
