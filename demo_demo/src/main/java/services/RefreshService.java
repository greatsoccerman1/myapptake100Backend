package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

import models.AddTaskResponse;

@Service
public class RefreshService {
	
	String port = "1433";
	String pass = "Curtis123";
	String userName = "Curtis";
	String databaseName = "master";
	String ip = "localhost";
	String hostName = "localhost";
    String connectionUrl = "mongodb://localhost:27017";
	String userDataBaseName = "Curtis";
	private final String refreshJobs = "update Jobs set jobStatus = 'NOT DONE' where GETDATE() > nextRefreshDate OR jobStatus = 'DONE'";
	
	public AddTaskResponse refresh() {
		Connection connection = null;
		AddTaskResponse addTaskResponse = new AddTaskResponse();
		String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
		System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
		try {
			connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
			if (connection != null) {
				PreparedStatement ps = connection.prepareStatement(refreshJobs);
				ps.execute();
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addTaskResponse.setStatus("Good");
		
		
		//}
		return addTaskResponse;
		
		
		
	}
	

}
