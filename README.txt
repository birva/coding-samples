Page Processor Job Queue:
 A job queue whose workers fetch data from a URL and store the results in a database.  
 The job queue should expose a REST API for adding jobs and checking their status / results.
 
Project Structure:
package: com.pageProcessor
	JobProducer.java: adds job to the queue
	Worker.java: Consumer thread to access url from queue and store content at the page in database
	JobInfo: Object stored in queue containing jobId and url 
	PageProcessorService: REST service exposed for producing jobs & checking status of jobs

package: com.pageProcessor.db
	DBOperations.java: defines operations with mongodb database
	
REST API:
url: /jobs

GET: /jobId
Description: checks the status of jobId
Response:
If job is processed:
	Code: 200 | Message: Stored results from page of url
If job is still in queue:	
	Code: 200 | Message: Waiting to be processed
If no job with jobId was found:
	Code: 404 | Message: JobId
	
POST:
Description: Add to the job queue, url to be processed
Body: String | url to be processed
Response:
	Code: 400 : If url is invalid
	Code: 201 | Message: JobId resource associated with the new job
	Code: 500 : If some error occured on server side 