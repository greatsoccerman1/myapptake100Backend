package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

import java.util.UUID;
import models.RegisterNewAccountRequest;
import models.RegisterNewAccountResponse;
import models.UserLoginModel;
import models.groupInfo;

@Service
public class LoginService {

	String port = "1433";
	String pass = "Curtis123";
	String userDataBaseName = "Curtis";

	String ip = "localhost";
	String hostName = "localhost";

	String userName = "Curtis";
	String databaseName = "master";

    String connectionUrl = "mongodb://localhost:27017";
	String userId;
	UserLoginModel userLoginModel = null;
	private String loginCheck = "Select * from Users where userName = ? AND password = ?";
	private String getGroups = "Select * from Groups where userId = ?";
	private String registerNewAccount = "Insert into Users (Name, GroupName, Password, Userid, GroupId, UserName) VALUES (?,?,?,?,?,?)";

	public UserLoginModel getUserLogin(String username, String password) {
		Connection connection = null;
		try {
		    String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
			System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
			connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
			userLoginModel = new UserLoginModel();
			ArrayList<groupInfo> groupInfoList = new ArrayList<>();

			if (connection != null) {
				PreparedStatement ps = connection.prepareStatement(loginCheck);
				ps.setString(1, username);
				ps.setString(2, password);
				ResultSet result = ps.executeQuery();
				// connection.close();
				if (result.next()) {
					userLoginModel.setUserName(result.getString("Name"));
					userLoginModel.setUserId(result.getString("UserId"));
					
					PreparedStatement ps2 = connection.prepareStatement(getGroups);
					ps2.setString(1, userLoginModel.getUserId());
					ResultSet result2 = ps2.executeQuery();
					while (result2.next()){
					  ArrayList<groupInfo> tempGroupInfo = new ArrayList<>();
				      String groupName = result2.getString("GroupName");
					  String groupId = result2.getString("GroupId");
					  String groupOwnerId = result2.getString("GroupOwner");
					  tempGroupInfo.add(groupInfo.builder()
					 	.groupName(groupName)
						.groupCode(groupId)
						.groupOwnerId(groupOwnerId).build());
						groupInfoList.addAll(tempGroupInfo);
		    		  }
					  userLoginModel.setGroupInfo(groupInfoList);
				}
			connection.close();
			return userLoginModel;
			} else {
				return null;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userLoginModel;
	}

	private UserLoginModel getGroupsForPerson(String userId, UserLoginModel userLoginModel) {

		HashMap<String, String> groupAndName = new HashMap<>();
		com.mongodb.client.MongoClient client = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = client.getDatabase("PeopleApplication");
		MongoCollection<Document> collection = database.getCollection("Groups");
		FindIterable<Document> results = collection.find(new BasicDBObject("PersonId", userId));
		ArrayList<groupInfo> groupInfoList = new ArrayList<>();

		for (Document result : results) {
			String json = result.toJson();
			JSONObject jsonObj = new JSONObject(json);
			JSONObject geodata = jsonObj.getJSONObject("Groups");
				ArrayList<groupInfo> tempGroupInfo = new ArrayList<>();
				JSONObject person = geodata;
				String groupName = person.getString("GroupName");
				String groupCode = person.getString("GroupCode");
				String groupOwnerId = person.getString("GroupOwnerId");

				tempGroupInfo.add(groupInfo.builder().groupName(groupName).groupCode(groupCode)
						.groupOwnerId(groupOwnerId).build());

				groupInfoList.addAll(tempGroupInfo);
			}
			userLoginModel.setGroupInfo(groupInfoList);
		return userLoginModel;
	}

	public RegisterNewAccountResponse registerNewAccount(RegisterNewAccountRequest req) {
		
		//TODO: first check to see if group id is being used.
		Connection connection = null;
		RegisterNewAccountResponse registerNewAccountResponse = new RegisterNewAccountResponse();
		try {
			String connectionUrl = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName;
			System.out.print("DriverManager.getConnection(\"" + connectionUrl + "\")");
			connection = DriverManager.getConnection(connectionUrl, userDataBaseName, pass);
			if (connection != null) {
				String groupId = UUID.randomUUID().toString();
				String userId = UUID.randomUUID().toString();
				PreparedStatement ps = connection.prepareStatement(registerNewAccount);
				ps.setString(1, req.getFirstName());
				ps.setString(2, req.getGroupName());
				ps.setString(3, req.getPassword());
				// TODO: not nullable double check
				ps.setString(4, userId);
				ps.setString(5, groupId);
				ps.setString(6, req.getUserName());
				ps.execute();
				connection.close();
				
		    	com.mongodb.client.MongoClient client = MongoClients.create("mongodb://localhost:27017");
		    	MongoDatabase database = client.getDatabase("PeopleApplication");
		    	MongoCollection<Document> collection = database.getCollection("Groups");
		    	BasicDBObject query = new BasicDBObject();
		    	BasicDBObject list = new BasicDBObject();
		    	List<groupInfo> groupToAddInfo = new ArrayList<groupInfo>();
		    	groupToAddInfo.add(groupInfo.builder().groupName(req.getGroupName()).groupCode(groupId)
		    			.groupOwnerId(userId).build());
		        query.put("PersonId", userId);
		        
		        
		        Document document = new Document();
		        Document docuemnt2Document = new Document();
		    	document.append("PersonId", userId);
		    //	document.append("Groups", groupToAddInfo);
		    	docuemnt2Document.append("GroupName", req.getGroupName());
		    	docuemnt2Document.append("GroupCode", groupId);
		    	docuemnt2Document.append("GroupOwnerId", userId);
		    	document.append("Groups", docuemnt2Document);
		    	collection.insertOne(document);
		    	
		    	userLoginModel = new UserLoginModel();
		    	
		    	userLoginModel = getGroupsForPerson(userId, userLoginModel);

				// connection.close();
				registerNewAccountResponse.setRegisterNewAccountStatus("OK");
			    registerNewAccountResponse.setGroupId(groupId);
			    registerNewAccountResponse.setUserId(userId);
			    registerNewAccountResponse.setGroupInfo(userLoginModel.getGroupInfo());
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			registerNewAccountResponse.setRegisterNewAccountStatus("SqlException");
		}
		return registerNewAccountResponse;
	}
}
