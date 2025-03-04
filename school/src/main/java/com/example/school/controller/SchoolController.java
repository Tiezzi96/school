package com.example.school.controller;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.example.school.view.StudentView;

public class SchoolController {

	private StudentRepository studentRepository;
	private StudentView studentView;

	public SchoolController(StudentView studentView, StudentRepository studentRepository) {
		this.studentView = studentView;
		this.studentRepository = studentRepository;
		// TODO Auto-generated constructor stub
	}

	public void allStudents() {
		studentView.showAllStudents(studentRepository.findall());
	}

	public synchronized void newStudent(Student student) {
		Student existingStudent = studentRepository.findById(student.getId());
		if (existingStudent != null) {
			System.out.println(": "+existingStudent);
			studentView.showError("Already existing student with id " + student.getId(), existingStudent);
			return;
		}

		studentRepository.save(student);
		studentView.studentAdded(student);

	}

	public void deleteStudent(Student student) {
		if (studentRepository.findById(student.getId()) == null) {
			studentView.showError("No existing student with id " + student.getId(), student);
			return;
		}

		studentRepository.delete(student.getId());
		studentView.studentRemoved(student);
	}
}
