package com.hyf.valid.other.pojo;

import com.hyf.valid.other.groups.Group1;
import com.hyf.valid.other.groups.Group2;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2020/05/12
 */
public class Student {

	@Min(value = 5, message = "id不能小于5", groups = {Default.class, Group1.class})
	private int id;

	@NotBlank(message = "字符串不能为空", groups = {Group2.class})
	@Length(max = 10, min = 5, message = "字符串长度不能大于10，小于5")
	private String name;

	@Size(min = 1, message = "list长度必须大于1")
	private List<String> stringList = new ArrayList<>();

	public Student() {
	}

	public Student(int id, String name) {
		this.id = id;
		this.name = name;
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

	public List<String> getStringList() {
		return stringList;
	}

	public void setStringList(List<String> stringList) {
		this.stringList = stringList;
	}

	@Override
	public String toString() {
		return "Student{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
