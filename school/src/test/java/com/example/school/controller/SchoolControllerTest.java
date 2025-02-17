package com.example.school.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.example.school.view.StudentView;

public class SchoolControllerTest {

	@Mock
	private StudentRepository studentRepository;

	@Mock
	private StudentView studentView;

	@InjectMocks
	private SchoolController schoolController;

	private AutoCloseable autoCloseable;

	@Before
	public void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		autoCloseable.close();
	}

	@Test
	public void testAllStudents() {
		List<Student> students = asList(new Student());
		when(studentRepository.findall()).thenReturn(students);
		schoolController.allStudents();
		verify(studentView).showAllStudents(students);
	}

	@Test
	public void testNewStudentWhenStudentDoesNotAlreadyExist() {
		Student newStudent = new Student("1", "test");
		when(studentRepository.findById("1")).thenReturn(null);
		schoolController.newStudent(newStudent);
		InOrder inOrder = inOrder(studentRepository, studentView);
		inOrder.verify(studentRepository).save(newStudent);
		inOrder.verify(studentView).studentAdded(newStudent);

	}

	@Test
	public void testNewStudentWhenStudentAlreadyExist() {
		Student studentToAdd = new Student("1", "test");
		Student existingStudent = new Student("1", "name");
		when(studentRepository.findById("1")).thenReturn(existingStudent);
		schoolController.newStudent(studentToAdd);
		verify(studentView).showError("Already existing student with id 1", existingStudent);
		verifyNoMoreInteractions(ignoreStubs(studentRepository));
	}

	@Test
	public void testDeleteStudentWhenStudentDoesExist() {
		Student studentToDelete = new Student("1", "test");
		when(studentRepository.findById("1")).thenReturn(studentToDelete);
		schoolController.deleteStudent(studentToDelete);
		InOrder inOrder = inOrder(studentRepository, studentView);
		inOrder.verify(studentRepository).delete(studentToDelete.getId());
		inOrder.verify(studentView).studentRemoved(studentToDelete);
	}

	@Test
	public void testDeleteStudentWhenStudentDoesNotExist() {
		Student student = new Student("1", "test");
		when(studentRepository.findById("1")).thenReturn(null);
		schoolController.deleteStudent(student);
		verify(studentView).showError("No existing student with id 1", student);
		verifyNoMoreInteractions(ignoreStubs(studentRepository));
	}

}
