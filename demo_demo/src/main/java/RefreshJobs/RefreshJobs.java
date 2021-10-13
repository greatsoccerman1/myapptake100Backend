package RefreshJobs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

public class RefreshJobs {
    String connectionUrl = "mongodb://localhost:27017";
	public void refreshJobs() {
	try {
	com.mongodb.client.MongoClient client = MongoClients.create(connectionUrl);
	MongoDatabase database = client.getDatabase("PeopleApplication");
	MongoCollection<Document> collection = database.getCollection("Jobs");
	BasicDBObject query = new BasicDBObject();
	FindIterable<Document> results = collection.find(query);
	for (Document result : results){
		Date currentDate = new Date();
		Date dateOfCompletion = result.getDate("lastCompeletedOn");
		int refreshRate = result.getInteger("refreshRate");
	
		long difference = Math.abs(currentDate.getTime() - dateOfCompletion.getTime());

	  }
	}
    catch(MongoException e) {
    	e.printStackTrace();
    	}
	}
}
	
	//com.mongodb.client.MongoClient client = MongoClients.create(connectionUrl);
	//MongoDatabase database = client.getDatabase("PeopleApplication");
	/*MongoCollection<Document> collection = database.getCollection("Jobs");
	BasicDBObject query = new BasicDBObject();
	query.append("groupId", groupId);
	FindIterable<Document> results = collection.find(query).sort(Sorts.ascending("name"));
	*/
	
