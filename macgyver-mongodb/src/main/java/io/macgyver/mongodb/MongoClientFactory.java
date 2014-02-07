package io.macgyver.mongodb;

import io.macgyver.core.ConfigurationException;
import io.macgyver.core.ServiceFactoryBean;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoClientFactory extends
		ServiceFactoryBean<MongoClient> {

	public MongoClientFactory() {
		super(MongoClient.class);
		
	}

	String uri;
	
	public MongoClient getObject() {
		try {
		return new ExtendedMongoClient(new MongoClientURI(uri));
		}
		catch (UnknownHostException e) {
			throw new ConfigurationException(e);
		}
	}
	public class ExtendedMongoClient extends MongoClient {

		MongoClientURI mongoClientUri;
		
		public ExtendedMongoClient(MongoClientURI uri)
				throws UnknownHostException {
			super(uri);
			this.mongoClientUri = uri;
		}
		
	}
	
	/*
	public DB getDB(String name) {
		ExtendedMongoClient c = (ExtendedMongoClient) get(name);
		String dbName = c.mongoClientUri.getDatabase();
		if (dbName==null) {
			throw new ConfigurationException("db name not specified in URL");
		}
		return c.getDB(dbName);
		
	}
	*/
}