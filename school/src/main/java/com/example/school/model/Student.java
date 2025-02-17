package com.example.school.model;

import java.util.Objects;

public class Student {
	
	private String id;
	private String name;

	public Student() {
		// TODO Auto-generated constructor stub
	}
	
	public Student(String id, String name) {
		this.id = id;
		this.name = name;
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}
	public String getName() {
		return name;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) &&
               Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
