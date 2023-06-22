package services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import models.EmployeeHistory;
import models.EmployeeHistoryRequest;
import models.EmployeeHistoryResponse;
import models.JobsModel;


@Service
public class EmployeeService {

	String port = "1433";
	String pass = "Curtis123";
	String userName = "Curtis";
	String databaseName = "master";
	String ip = "localhost";
	String hostName = "localhost";
	String userDataBaseName = "Curtis";
   	Logger logger = LogManager.getLogger(EmployeeService.class);
	private final String employeeHistoryQuery = "Select * from CompletedJobs where personId = ? and groupId = ? and dateOfCompletion BETWEEN ? AND ?";
	
	public EmployeeHistoryResponse getEmployeeHistory(EmployeeHistoryRequest req) {
		EmployeeHistoryResponse resp = new EmployeeHistoryResponse();
		List<EmployeeHistory> employeeHistoryModel = null;
		employeeHistoryModel = new ArrayList();
    	logger.atInfo().log("getJobsRequest: groupId :" + req.getGroupId() +  " personId: " + req.getMemberId());
    	Connection connection = null;
    	try {
    	String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
		System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
		connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
		
		if (req.getStartDate() == null) {
			req.setStartDate(Date.valueOf(LocalDate.now()));
		}
		
		if (req.getEndDate() == null) {
			req.setEndDate(Date.valueOf(LocalDate.now().minusDays(30)));
		}
		
		if (connection != null) {
			PreparedStatement ps = connection.prepareStatement(employeeHistoryQuery);
			ps.setString(1, req.getMemberId());
			ps.setString(2, req.getGroupId());
			ps.setDate(3, req.getEndDate());
			ps.setDate(4, req.getStartDate());
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				employeeHistoryModel.add(EmployeeHistory.builder()
	    			.dateOfCompletion(result.getDate("dateOfCompletion"))
	    			.stillComplete(result.getString("stillCompleted"))
	    			.price(result.getBigDecimal("price"))
	    			.jobId(result.getString("jobId")).build());
				}	 
			connection.close();
			resp.setEmployeeHistory(employeeHistoryModel);
			}
		return resp;
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
