package com.hyf.valid.other.pojo;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.beans.ConstructorProperties;

/**
 * @author baB_hyf
 * @date 2020/05/12
 */
public class Teacher {

	@DecimalMin("5.1") // 最小为 5.1
//	@Digits(integer = 6, fraction = 999) // 只支持 6.999
	private double id;

	@AssertTrue
	private boolean isMan;

	@Pattern(regexp = "hyf*")
	private String name;

	@Valid
	private Student student;

	public Teacher() {
	}

	@ConstructorProperties({"id", "isMan","name", "student"})
	public Teacher(double id, boolean isMan, String name, Student student) {
		this.id = id;
		this.isMan = isMan;
		this.name = name;
		this.student = student;
	}


//	public Teacher(@DecimalMin("5.0d") @Digits(integer = 6, fraction = 999) double id, @AssertTrue boolean isMan, @Pattern(regexp = "hyf*") String name, @Valid Student student) {
//		this.id = id;
//		this.isMan = isMan;
//		this.name = name;
//		this.student = student;
//	}

	public double getId() {
		return id;
	}

	public void setId(double id) {
		this.id = id;
	}

	public boolean isMan() {
		return isMan;
	}

	public void setMan(boolean man) {
		isMan = man;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Override
	public String toString() {
		return "Teacher{" +
				"id=" + id +
				", isMan=" + isMan +
				", name='" + name + '\'' +
				", student=" + student +
				'}';
	}
}
