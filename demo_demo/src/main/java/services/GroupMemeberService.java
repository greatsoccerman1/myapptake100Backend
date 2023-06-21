package services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import models.AddGroupMememberRequest;
import models.AddGroupMememberResponse;
import models.DeleteGroupMemberRequest;
import models.DeleteGroupMemberResponse;
import models.GetGroupMemberInfoRequest;
import models.GetGroupMemberInfoResponse;
import models.GetGroupMembersRequest;
import models.GetGroupMembersResponse;
import models.GroupMember;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Service
public class GroupMemeberService {
	
	String port = "1433";
	String pass = "Curtis123";
	String userName = "Curtis";
	String databaseName = "master";
	String ip = "localhost";
	String hostName = "localhost";
    String connectionUrl = "mongodb://localhost:27017";
	String userDataBaseName = "Curtis";
	String userId;
	GroupMember groupMemberModel = null;
	private final String getGroupMembersQuery = "Select * from groupMembers where GroupId = ?";
	private final String deleteGroupMemberQuery = "Delete From groupMembers where GroupId = ? AND MemberId = ?";
	private final String checkForAccount = "Select * from Users where userId = ?";
	private final String addGroupMemberQuery = "Insert into groupMembers (GroupId, MemberId, First_Name, Last_Name, UserName, Role, GroupName) VALUES (?,?,?,?,?,?,?)";
	private final String getGroupMemberInfo = "Select * from CompletedJobs where personId = ? AND dateOfCompletion >= CAST(? AS date) AND dateOfCompletion <= CAST(? AS date) AND stillCompleted = 'DONE';";
	Logger logger = LogManager.getLogger(GroupMemeberService.class);
	
	public AddGroupMememberResponse addGroupMember(AddGroupMememberRequest req) {
		AddGroupMememberResponse addGroupMembersResponse = new AddGroupMememberResponse();
		logger.atInfo().log("AddGroupMememberRequest: " + req);
		
		Connection connection = null;
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

		String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
		System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
		
		try {
			connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
			if (connection != null) {
				PreparedStatement checkPs = connection.prepareStatement(checkForAccount);
				
				checkPs.setString(1, req.getUserName());
				ResultSet rs = checkPs.executeQuery();
				
			    if (rs != null) {;
					PreparedStatement ps = connection.prepareStatement(addGroupMemberQuery);
					if (req.isNewGroup()) {
						req.setGroupId(UUID.randomUUID().toString());
						req.setRole("admin");
					}
					ps.setString(1, req.getGroupId());
					ps.setString(2, uuidAsString);
					ps.setString(3, req.getFirstName());
					ps.setString(4, req.getLastName());
					ps.setString(5, req.getUserName());
					ps.setString(6, req.getRole());
					ps.setString(7, req.getGroupName());
					ps.executeQuery();
					addGroupMembersResponse.setStatus("Good");
			    }else {
			    	addGroupMembersResponse.setStatus("No UserName");
			    }
				connection.close();
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addGroupMembersResponse;
	}

	public GetGroupMembersResponse getGroupMembers(String groupId) {
		logger.atInfo().log("getGroupMembersRequest: groupId: " + groupId);
		Connection connection = null;
		GetGroupMembersResponse getGroupMembersResponse = new GetGroupMembersResponse();
		List<GroupMember> groupMemberList = null;

		String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
		System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
		try {
			connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
			if (connection != null) {
				PreparedStatement ps = connection.prepareStatement(getGroupMembersQuery);
				ps.setString(1, groupId);
				ResultSet result = ps.executeQuery();
				// connection.close();
				groupMemberList = new ArrayList();
				while (result.next()) {
					groupMemberList.add(GroupMember.builder()
						.firstName(result.getString("First_Name"))
						.lastName(result.getString("Last_Name"))
						.memberId(result.getString("MemberId")).build());
				}
				 connection.close();
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getGroupMembersResponse.setGroupMembers(groupMemberList);
		logger.atInfo().log("getGroupMembersResponse: groupId: " + getGroupMembersResponse);
		return getGroupMembersResponse;
	}

	public DeleteGroupMemberResponse deleteGroupMembers(DeleteGroupMemberRequest req) throws SQLException {
		Connection connection = null;
		DeleteGroupMemberResponse deleteGroupMemberResponse = new DeleteGroupMemberResponse();
		String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
		System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
		try {
			connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
			if (connection != null) {
				PreparedStatement ps = connection.prepareStatement(deleteGroupMemberQuery);
				ps.setString(1, req.getGroupId());
				ps.setString(2, req.getUserId());				
				ps.executeQuery();
				connection.close();
			} else {
				deleteGroupMemberResponse.setStatus("Could Not Connect To DataBase");
			}
		} catch (SQLException e) {
			connection.close();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deleteGroupMemberResponse.setStatus("Good");
		return deleteGroupMemberResponse;
	}
	
	public GetGroupMemberInfoResponse getGroupInfo(GetGroupMemberInfoRequest req) throws SQLException {
		logger.atInfo().log("GetGroupMemberInfoRequest: " + req);
		Connection connection = null;
		GetGroupMemberInfoResponse resp = new GetGroupMemberInfoResponse();
		String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
		
		System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
		
		try {
			BigDecimal totalAmount = new BigDecimal(0.00);
			connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
			if (connection != null)
			{
				PreparedStatement ps = connection.prepareStatement(getGroupMemberInfo);
				ps.setString(1, req.getInfoForPersonId());
				ps.setString(2, req.getStartEarningDate());
				ps.setString(3, req.getEndEarningDate());
				ResultSet result = ps.executeQuery();
				while (result.next()) {
					totalAmount = totalAmount.add(result.getBigDecimal("price"));
				}
				resp.setTotalIncome(totalAmount);
				connection.close();
			}
		}catch (SQLException e) {
				connection.close();
				// TODO Auto-generated catch block
				e.printStackTrace();
			
		}
		return resp;
	}
}
