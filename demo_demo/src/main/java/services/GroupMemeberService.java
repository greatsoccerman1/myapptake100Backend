package services;

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
import models.GetGroupMembersRequest;
import models.GetGroupMembersResponse;
import models.GroupMember;


@Service
public class GroupMemeberService {
	
	String port = "1433";
	String pass = "Curtis123";
	String userName = "Curtis";
	String databaseName = "master";
	String ip = "192.168.131.148";
	String hostName = "localhost";
    String connectionUrl = "mongodb://localhost:27017";
	String userDataBaseName = "Curtis";
	String userId;
	GroupMember groupMemberModel = null;
	private final String getGroupMembersQuery = "Select * from groupMembers where GroupId = ?";
	private final String deleteGroupMemberQuery = "Delete From groupMembers where GroupId = ? AND MemberId = ?";
	private final String addGroupMemberQuery = "Insert into groupMembers (GroupId, MemberId, First_Name, Last_Name) VALUES (?,?,?,?)";
	
	public AddGroupMememberResponse addGroupMember(AddGroupMememberRequest req) {
		AddGroupMememberResponse addGroupMembersResponse = new AddGroupMememberResponse();
		/*for (int i = 0; i < 100 ; i++) {
			int groupId = 1 + (int)(Math.random() * ((10 - 1) + 1));
			String groupIdString = Integer.toString(groupId);
			
			int firstName = 1 + (int)(Math.random() * ((10 - 1) + 1));
			String firstNameToSend = "";
			if (firstName == 1)
				firstNameToSend = "Jason";
			if (firstName == 2)
				firstNameToSend = "Aaron";
			if (firstName == 3)
				firstNameToSend = "Kerri";
			if (firstName == 4)
				firstNameToSend = "Sarah";
			if (firstName == 5)
				firstNameToSend = "Patti";
			if (firstName == 6)
				firstNameToSend = "Robert";
			if (firstName == 7)
				firstNameToSend = "Frank";
			if (firstName == 8)
				firstNameToSend = "Nazebo";
			if (firstName == 9)
				firstNameToSend = "Donna";
			if (firstName == 9)
				firstNameToSend = "Crystal";
			if (firstName == 10)
				firstNameToSend = "Curtis";
			
			int lastName = 1 + (int)(Math.random() * ((10 - 1) + 1));
			String lastNameToSend = "";
			if (lastName == 1)
				lastNameToSend = "Smith";
			if (lastName == 2)
				lastNameToSend = "Andrews";
			if (lastName == 3)
				lastNameToSend = "Tom";
			if (lastName == 4)
				lastNameToSend = "Richard";
			if (lastName == 5)
				lastNameToSend = "Franklin";
			if (lastName == 6)
				lastNameToSend = "Penis";
			if (lastName == 7)
				lastNameToSend = "Uganda";
			if (lastName == 8)
				lastNameToSend = "Ripley";
			if (lastName == 9)
				lastNameToSend = "Weller";
			if (lastName == 9)
				lastNameToSend = "Williams";
			if (lastName == 10)
				lastNameToSend = "FitzGibbon";*/
			
		
    	/*com.mongodb.client.MongoClient client = MongoClients.create("mongodb://localhost:27017");
    	MongoDatabase database = client.getDatabase("PeopleApplication");
    	MongoCollection<Document> collection = database.getCollection("Jobs");
    	Document document = new Document();
    	document.append("name", req.getName());
    	document.append("needs", req.getNeed());
    	document.append("description", req.getDescription());
    	document.append("groupId", req.getGroupId());
    	document.append("personId", req.getUserId());
    	collection.insertOne(document);
		return null;*/
		Connection connection = null;
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

		String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
		System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
		
		try {
			connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
			if (connection != null) {
				PreparedStatement ps = connection.prepareStatement(addGroupMemberQuery);
				ps.setString(1, req.getGroupId());
				//ps.setString(1, groupIdString);
				ps.setString(2, uuidAsString);
				ps.setString(3, req.getFirstName());
				//ps.setString(3, firstNameToSend);
				ps.setString(4, req.getLastName());
				//ps.setString(4, lastNameToSend);
				ps.executeQuery();
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addGroupMembersResponse.setStatus("Good");
		return addGroupMembersResponse;
	}

	public GetGroupMembersResponse getGroupMembers(String groupId) {
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
//	            		return userLoginModel;
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
		return getGroupMembersResponse;
	}

	public DeleteGroupMemberResponse deleteGroupMembers(DeleteGroupMemberRequest req) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deleteGroupMemberResponse.setStatus("Good");
		return deleteGroupMemberResponse;
	}
}
