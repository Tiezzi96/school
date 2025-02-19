package com.example.school.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.awaitility.Awaitility.await;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.example.school.view.StudentView;

public class SchoolControllerRaceConditionTest {

	private AutoCloseable closeable;

	@Mock
	StudentRepository studentRepository;

	@Mock
	StudentView studentView;

	@InjectMocks
	SchoolController schoolController;

	@Before
	public void setUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);

	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void test() {
		List<Student> students = new ArrayList<Student>();
		Student student = new Student("1", "test1");
		// stub studentRepository
		when(studentRepository.findById(anyString()))
				.thenAnswer(invocation -> students.stream().findFirst().orElse(null));
		doAnswer(invocation -> {
			students.add(student);
			return null;
		}).when(studentRepository).save(any(Student.class));
		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> schoolController.newStudent(student))).peek(t -> t.start())
				.collect(Collectors.toList());
		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));
		assertThat(students).containsExactly(student);
	}

}
