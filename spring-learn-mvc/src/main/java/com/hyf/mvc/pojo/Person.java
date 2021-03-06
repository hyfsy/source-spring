package com.hyf.mvc.pojo;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * @author baB_hyf
 * @date 2020/04/21
 */
public class Person {

	@Min(5)
	private int id;

	@NotEmpty
	private String name;

	@Range(min = 5, max = 10)
	private Integer age;

	public Person() {
	}

	public Person(int id, String name, Integer age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}
