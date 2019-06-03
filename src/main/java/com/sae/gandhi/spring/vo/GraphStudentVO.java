package com.sae.gandhi.spring.vo;

public class GraphStudentVO {

	public String course;
	public Long students;
	
	public GraphStudentVO(String course, Long students) {
		super();
		this.course = course;
		this.students = students;
	}
	
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public Long getStudents() {
		return students;
	}
	public void setStudents(Long students) {
		this.students = students;
	}
	
}
