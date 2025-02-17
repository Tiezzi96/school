package com.example.school;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.school.controller.SchoolController;
import com.example.school.model.Student;
import com.example.school.repository.mongo.StudentMongoRepository;
import com.example.school.view.StudentView;
import com.mongodb.MongoClient;
import static java.util.Arrays.asList;

public class SchoolControllerIT {

	private AutoCloseable closeable;
	private StudentMongoRepository studentRepository;
	@Mock
	private StudentView studentView;
	private SchoolController schoolController;

	@Before
	public void setUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this); 
		studentRepository = new StudentMongoRepository(new MongoClient("localhost"));
		for(Student student: studentRepository.findall()) {
			studentRepository.delete(student.getId());
			
		}
		schoolController = new SchoolController(studentView, studentRepository);
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
		
	}

	@Test
	public void testAllStudents() {
		Student student = new Student("1", "test1");
		studentRepository.save(student);
		schoolController.allStudents();
		verify(studentView).showAllStudents(asList(student));
		
	}
	
	@Test
	public void testNewStudent() {
		Student student = new Student("1", "test1");
		schoolController.newStudent(student);
		verify(studentView).studentAdded(student);
	}
	
	@Test
	public void testDeleteStudent() {
		Student student = new Student("1", "test1");
		studentRepository.save(student);
		schoolController.deleteStudent(student);
		verify(studentView).studentRemoved(student);
	}

}
