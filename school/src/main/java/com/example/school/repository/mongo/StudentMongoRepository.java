package com.example.school.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class StudentMongoRepository implements StudentRepository {
	public static final String SCHOOL_DB_NAME = "school";
	public static final String STUDENT_COLLECTION_NAME = "Student";
	@SuppressWarnings("unused")
	private MongoCollection<Document> studentCollection;

	public StudentMongoRepository(MongoClient client) {
		studentCollection = client.getDatabase(SCHOOL_DB_NAME).getCollection(STUDENT_COLLECTION_NAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Student> findall() {
		// TODO Auto-generated method stub
		return StreamSupport.stream(studentCollection.find().spliterator(), false).map(this::fromDocumentToStudent)
				.collect(Collectors.toList());

		/*
		 * List<Student> s = StreamSupport.
		 * stream(studentCollection.find().spliterator(), false) .map(d -> new
		 * Student(String.valueOf(String.valueOf(d.get("id"))),
		 * String.valueOf(d.get("name")))) .collect(Collectors.toList());
		 * if(!s.isEmpty()) { System.out.println("s id: " + s.getFirst().getId());
		 * System.out.println("s name: " + s.getFirst().getName());
		 * 
		 * } return s;
		 */
	}

	public Student fromDocumentToStudent(Document d) {
		return new Student("" + d.get("id"), "" + d.get("name"));
	}

	@Override
	public Student findById(String id) {
		// TODO Auto-generated method stub
		// Student findStudent =
		// StreamSupport.stream(studentCollection.find().spliterator(),
		// false).map(d->new Student(""+d.get(id),""+ d.get("name")))
		// .collect(Collectors.toList()).getFirst();
		Document d = studentCollection.find(Filters.eq("id", id)).first();
		if (d != null) {
			return fromDocumentToStudent(d);
		}
		return null;
	}

	@Override
	public void save(Student student) {
		// TODO Auto-generated method stub
		studentCollection.insertOne(new Document().append("id", student.getId()).append("name", student.getName()));

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		studentCollection.deleteOne(Filters.eq("id", id));

	}

}
