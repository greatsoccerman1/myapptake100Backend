package services;

import com.example.demoController.MarkJobCompleteResponse;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.PseudoColumnUsage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import models.Jobs;
import models.JobsModel;
import models.MarkJobCompleteRequest;
import models.RemoveJobResponse;
import models.AddJobRequest;
import models.AddJobResponse;
import models.AddTaskReq;
import models.AddTaskResponse;
import models.GetTasksRequest;
import models.JobTasks;
import models.JobTasksModel;

import org.apache.catalina.startup.WebappServiceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class JobTaskService {
	String port = "1433";
	String pass = "Curtis123";
	String userName = "Curtis";
	String databaseName = "master";
	String ip = "localhost";
	String hostName = "localhost";
    String connectionUrl = "mongodb://localhost:27017";
    private String getJobs = "Select * from Jobs where groupId = ?";
    private String addJob = "Insert into Jobs (Name, GroupId, JobId, refreshRate, price, nextRefreshDate, jobStatus) Values (?,?,?,?,?,  GETDATE() + ?,?)";
    private String markJobComplete = "update Jobs set jobStatus = ?, lastCompletedOn = GETDATE(), nextRefreshDate = (GETDATE() + ?) where jobId = ?";
    private String storeJobComplete = "Insert into CompletedJobs (jobCompletionId, jobId, dateOfCompletion, stillCompleted,price, personId, updateDate) values (?,?,GETDATE(),?,?,?,GetDate())";
    private String removeJob = "Delete from Jobs where jobId = ? AND groupId = ?";
	String userDataBaseName = "Curtis";
   	Logger logger = LogManager.getLogger(JobTaskService.class);

    //Used to get the list of jobs associated with a bussiness Code
    // TODO needs to sort by date. Also give manager access to all jobs. 
    public Jobs getJobs(String groupId, String personId) {
    	logger.atInfo().log("getJobsRequest: groupId :" + groupId +  " personId: " + personId);
    	Connection connection = null;
    	try {
    	String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
		System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
		connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
		if (connection != null) {
			PreparedStatement ps = connection.prepareStatement(getJobs);
			ps.setString(1, groupId);
			ResultSet result = ps.executeQuery();
    	
			List<JobsModel> jobListModel = null;
			Jobs jobsModel = new Jobs();
			List<JobTasks> needsList = null;
			jobListModel = new ArrayList();
			while (result.next()) {
    		jobListModel.add(JobsModel.builder()
    			.jobName(result.getString("name"))
    			.jobId(result.getString("jobId"))
    			.refreshRate(result.getInt("refreshRate"))
    			.jobStatus(result.getString("jobStatus"))
    			.jobPrice(result.getBigDecimal("price")).build());
			}	    	
			connection.close();
			Collections.sort(jobListModel, (o1, o2) -> o1.getJobStatus().compareTo(o2.getJobStatus()));
			Collections.reverse(jobListModel);
			jobsModel.setJobInfo(jobListModel);
			logger.atInfo().log("getJobsResponse: " + jobsModel);
			return jobsModel;
			}
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Jobs jobsModel = new Jobs();
    	jobsModel.setStatus("failed");
		return jobsModel;
    }
    
    public JobTasksModel getTask(GetTasksRequest req) {
 
    	try {
    	com.mongodb.client.MongoClient client = MongoClients.create("mongodb://localhost:27017");
    	MongoDatabase database = client.getDatabase("PeopleApplication");
    	MongoCollection<Document> collection = database.getCollection("Tasks");
    	BasicDBObject query = new BasicDBObject();
    	query.append("jobId", req.getJobId());
    	FindIterable<Document> results = collection.find(query).sort(Sorts.ascending("name"));
    	//FindIterable<Document> results = collection.find(new BasicDBObject("groupId", userId)).sort(Sorts.ascending("name"));
  
    	JobTasksModel jobTasksToReturn = new JobTasksModel();
    	List<JobTasks> jobTasksList = new ArrayList<>();
    	for (Document result : results) {
    	  	Date date = new Date();
    		jobTasksList.add(JobTasks.builder()
				.task(result.getString("task").toString())
				.taskDescription(result.getString("description"))
				.taskMongoId(result.get("_id").toString())
				.taskStatus(result.getString("taskStatus")).build()); 	
    	}
    	jobTasksToReturn.setJobTasks(jobTasksList);
    	return jobTasksToReturn; 
    	
    	}catch(MongoException e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public AddJobResponse addJob(AddJobRequest req) {
    	AddJobResponse addJobResponse = new AddJobResponse();
        Connection connection = null;
        try {
       
        	String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
        	System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
        	connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
        	if (connection != null) {
        	
        		PreparedStatement ps = connection.prepareStatement(addJob);
    			ps.setString(1, req.getJobName());
    			ps.setString(2, req.getGroupId());
    			ps.setString(3, UUID.randomUUID().toString());
    			ps.setInt(4, req.getRefreshRate());
    			ps.setInt(5, req.getJobCost());
    			ps.setInt(6, req.getRefreshRate());
    			ps.setString(7, req.getJobStatus());
    			ps.execute();
    			addJobResponse.setStatus("OK");
        	}
        } catch (SQLException e) {
    			// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
        return addJobResponse;
    }
        
    
    public AddTaskResponse addTask (AddTaskReq req) {
    	AddTaskResponse resp = new AddTaskResponse();
    	try {
    	com.mongodb.client.MongoClient client = MongoClients.create("mongodb://localhost:27017");
    	MongoDatabase database = client.getDatabase("PeopleApplication");
    	MongoCollection<Document> collection = database.getCollection("Tasks");
    	Document document = new Document();
    	document.put("task", req.getTaskName());
    	document.put("description", req.getTaskDescription());
    	document.put("jobId",req.getJobId());
    	document.put("refreshRate", req.getRefreshRate());
    	document.put("taskStatus", req.getTaskStatus());
    	collection.insertOne(document);
    	resp.setStatus("OK");
		return resp;
    }catch(MongoException e) {
    	resp.setStatus(e.toString());
    	return resp;
    	}
    }

	public MarkJobCompleteResponse markJobComplete(MarkJobCompleteRequest req) {
		MarkJobCompleteResponse markJobCompleteResponse = new MarkJobCompleteResponse();
        Connection connection = null;
        try {
       
        	String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
        	System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
        	connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
        	if (connection != null) {
            	
        		PreparedStatement ps = connection.prepareStatement(markJobComplete);
        		ps.setString(1, req.getJobStatus());
        		ps.setInt(2, req.getRefreshRate());
        		ps.setString(3, req.getJobId());   			
    			ps.execute();
    			
    			PreparedStatement ps2 = connection.prepareStatement(storeJobComplete);
    			ps2.setString(1, UUID.randomUUID().toString());
    			ps2.setString(2, req.getJobId());
    			ps2.setString(3, req.getJobStatus());
    			ps2.setBigDecimal(4, req.getPrice());
    			ps2.setString(5, req.getPersonId());
    			ps2.execute();
    			connection.close();
        	}
        	markJobCompleteResponse.setStatus("DONE");
        }catch(SQLException e) {
        	e.printStackTrace();
        }
		return markJobCompleteResponse;
	}
	
	public MarkJobCompleteResponse markJobInComplete(MarkJobCompleteRequest req) {
		MarkJobCompleteResponse markJobCompleteResponse = new MarkJobCompleteResponse();
        Connection connection = null;
        try {
       
        	String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
        	System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
        	connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
        	if (connection != null) {
            	
        		PreparedStatement ps = connection.prepareStatement(markJobComplete);
        		ps.setString(1, req.getJobStatus());
        		ps.setInt(2, req.getRefreshRate());
        		ps.setString(3, req.getJobId());   			
    			ps.execute();
    			
    			PreparedStatement ps2 = connection.prepareStatement(storeJobComplete);
    			ps2.setString(1, UUID.randomUUID().toString());
    			ps2.setString(2, req.getJobId());
    			ps2.setString(3, req.getJobStatus());
    			ps2.setBigDecimal(4, req.getPrice());
    			ps2.setString(5, req.getPersonId());
    			ps2.execute();
    			connection.close();
        	}
        	markJobCompleteResponse.setStatus("NOT DONE");
        }catch(SQLException e) {
        	e.printStackTrace();
        }
		return markJobCompleteResponse;
	}
	
	//TODO: remove task along with job
	public RemoveJobResponse removeJob(String groupId, String jobId) {
		RemoveJobResponse resp = new RemoveJobResponse();
		Connection connection = null;
        try {
	  	String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
    	System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
    	connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
	    	if (connection != null) {
	    		PreparedStatement ps = connection.prepareStatement(removeJob);
	    		ps.setString(1, groupId);   	
	    		ps.setString(2, jobId);
	    		ps.execute();
	    		connection.close();
	    	}
        }catch(SQLException e) {
        	e.printStackTrace();
        }
        return resp;
	}
}
