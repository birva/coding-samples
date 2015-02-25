package com.pageProcessor.db;

import java.net.UnknownHostException;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class DBOperations {

	private static DB dbConnection;
	
	public static boolean storePage(String jobId, String url, String content) {
		try {
			DB db = getDBConnection();
			DBCollection table = db.getCollection("page_collection");
			BasicDBObject document = new BasicDBObject();
			document.put("jobId", jobId);
			document.put("url", url);
			document.put("data", content);
			document.put("creation_date", new Date());
			table.insert(document); 
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static String getJobStatus(String jobId) {
		String result = "";
		try {
			DB db = getDBConnection();
			DBCollection table = db.getCollection("page_collection");
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("jobId", jobId);
		 
			DBCursor cursor = table.find(searchQuery);
			while (cursor.hasNext()) {
				result = cursor.next().toString();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "error";
		}
		return result;
	}

	public static DB getDBConnection() throws UnknownHostException {
		if(dbConnection==null) {
			MongoClient mongoClient = new MongoClient( "localhost" , 27017);
			dbConnection = mongoClient.getDB("webpage_storage");
		}
		return dbConnection;
	}
	
}
