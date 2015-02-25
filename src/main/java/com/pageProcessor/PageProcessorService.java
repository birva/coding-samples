package com.pageProcessor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/jobs")
public class PageProcessorService {
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getJob(@PathParam("id") String jobId) {
		JobProducer producer = JobProducer.getProducer();
		String status = producer.getJobStatus(jobId);
		if("Waiting".equals(status))
			return Response.ok("Waiting to be processed").build();
		
		if(status.equals("NotFound")) {
			return Response.status(Status.NOT_FOUND).entity("No Job Found With ID: "+jobId).build();
		}
		if(status.equals("error")) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(status).build();
	}
	
	@POST
	@Produces("application/json")
	public Response addJob(String pageUrl) {
		JobProducer producer = JobProducer.getProducer(); 
		String jobId = producer.addJob(pageUrl);
		if(jobId.equals("invalidUrl")) {
			Response.status(Status.BAD_REQUEST).entity("Invalid Url").build();
		}
		return Response.ok(jobId).build();
	}
}
