package com.example.school.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.school.model.Student;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class StudentMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;
	private MongoClient client;
	private StudentMongoRepository studentRepository;
	private MongoCollection<Document> studentCollection;

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		// bind on a random local port
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutDown() {
		server.shutdown();
	}

	@Before
	public void setUp() {
		client = new MongoClient(new ServerAddress(serverAddress));
		studentRepository = new StudentMongoRepository(client);
		MongoDatabase mongoDatabase = client.getDatabase(StudentMongoRepository.SCHOOL_DB_NAME);
		// make sure we always start with clean database
		mongoDatabase.drop();
		studentCollection = mongoDatabase.getCollection(StudentMongoRepository.STUDENT_COLLECTION_NAME);

	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAllWhenListIsEmpty() {
		assertThat(studentRepository.findall()).isEmpty();
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
	public void testFindByIdNotFound() {
		assertThat(studentRepository.findById("1")).isNull();

	}

	@Test
	public void testFindByIdIsFound() {
		addTestStudentToDatabase("2", "test2");
		addTestStudentToDatabase("1", "test1");
		assertThat(studentRepository.findById("1")).isEqualTo(new Student("1", "test1"));
	}

	@Test
	public void testDelete() {
		addTestStudentToDatabase("1", "test1");
		studentRepository.delete("1");
		assertThat(studentRepository.findall().size()).isEqualTo(0);
	}

	@Test
	public void testSave() {
		Student student = new Student("1", "test1");
		studentRepository.save(student);
		assertThat(readAllStudentsFromDatabase()).containsExactly(student);
	}

	private List<Student> readAllStudentsFromDatabase() {
		return StreamSupport.stream(studentCollection.find().spliterator(), false)
				.map(d -> new Student("" + d.get("id"), "" + d.get("name"))).collect(Collectors.toList());
	}

}
