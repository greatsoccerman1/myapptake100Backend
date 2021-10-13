package services;

import java.time.LocalDateTime;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.internal.connection.Time;

import models.RemoveNeed;

@Service
public class NeedsController {

	public RemoveNeed removeNeed(String mongoId) {
		RemoveNeed removeNeed = new RemoveNeed();
		// TODO Auto-generated method stub
		try {
    	com.mongodb.client.MongoClient client = MongoClients.create("mongodb://localhost:27017");
    	MongoDatabase database = client.getDatabase("PeopleApplication");
    	MongoCollection<Document> collection = database.getCollection("Jobs");
    	collection.deleteOne(new Document("_id", new ObjectId(mongoId)));
    	removeNeed.setRemoved(true);
		}catch(MongoException e) {
			removeNeed.setRemoved(false);
		}
    	return removeNeed;
	}
	
	public RemoveNeed markJobComplete(String mongoId) {
		RemoveNeed removeNeed = new RemoveNeed();
		try {
			LocalDateTime timeOfLastComplete = java.time.LocalDateTime.now();
		/*	LocalDateTime timeOfLastComplete2 = java.time.LocalDateTime.now();
		
			long days = Days.between(timeOfLastComplete, timeOfLastComplete2);*/
	    	com.mongodb.client.MongoClient client = MongoClients.create("mongodb://localhost:27017");
	    	MongoDatabase database = client.getDatabase("PeopleApplication");
	    	MongoCollection<Document> collection = database.getCollection("Jobs");
	    	collection.updateOne(new Document("_id", new ObjectId(mongoId)), new Document("$set", new Document("lastCompeletedOn", timeOfLastComplete)));
	    	removeNeed.setRemoved(true);
			}catch(MongoException e) {
				removeNeed.setRemoved(false);
			}
		return null;
	}

}
