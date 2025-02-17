package com.example.school.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
// import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;

import com.example.school.model.Student;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class StudentMongoRepositoryTestcontainersIT {

	@SuppressWarnings("rawtypes")
	@ClassRule
	//public static final GenericContainer mongo = new GenericContainer("mongo:4.4.3").withExposedPorts(27017);

	// it's not necessary to map container on port 27017. It's automatic with MongoDBContainer
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");
	private MongoClient client;
	private StudentMongoRepository studentRepository;
	private MongoCollection<Document> studentCollection;
	
	@Before
	public void setUp() {
		client = new MongoClient(new ServerAddress(mongo.getContainerIpAddress(), mongo.getMappedPort(27017)));
		studentRepository = new StudentMongoRepository(client); 
		MongoDatabase database = client.getDatabase(StudentMongoRepository.SCHOOL_DB_NAME);
		database.drop();
		studentCollection = database.getCollection(StudentMongoRepository.STUDENT_COLLECTION_NAME);
	}

	@After
	public void tearDown() throws Exception {
		client.close();
	}
	
	private void addTestStudentToDatabase(String id, String name) {
		studentCollection.insertOne(new Document().append("id", id).append("name", name));
	}
	
	@Test
	public void testFindAllWhenDatabaseContainsExactlyTwoElements() {
		addTestStudentToDatabase("1", "test1");
		addTestStudentToDatabase("2", "test2");
		assertThat(studentRepository.findall()).containsExactly(new Student("1", "test1"), new Student("2", "test2"));

	}


	@Test
	public void testFindByIdIsFound() {
		addTestStudentToDatabase("2", "test2");
		addTestStudentToDatabase("1", "test1");
		assertThat(studentRepository.findById("1")).isEqualTo(new Student("1", "test1"));
	}
}
