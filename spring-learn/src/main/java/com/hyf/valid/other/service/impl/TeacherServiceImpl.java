package com.hyf.valid.other.service.impl;

import com.hyf.valid.other.pojo.Student;
import com.hyf.valid.other.pojo.Teacher;
import com.hyf.valid.other.service.TeacherServiceI;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author baB_hyf
 * @date 2020/05/12
 */
@Service("teacherService")
@Validated
public class TeacherServiceImpl implements TeacherServiceI {

	@Override
	public void validField(int id, @NotEmpty String name) {
		System.out.println(id + "->" + name);
	}

	@Override
	public void validPojo(@Valid @NotNull Student student) {
		System.out.println(student);
	}

	@Override
	public void validGroup(@Valid Student student) {
		System.out.println(student);
	}

	@Override
	public void validNested(@Valid Teacher teacher) {
		System.out.println(teacher);
	}

	@Override
	public @Length(min = 5, max = 10) @NotEmpty String validReturnValue(String id) {
		System.out.println("id -> " + id);
		return id;
	}
}
