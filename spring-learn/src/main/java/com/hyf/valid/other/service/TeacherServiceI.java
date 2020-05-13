package com.hyf.valid.other.service;

import com.hyf.valid.other.pojo.Student;
import com.hyf.valid.other.pojo.Teacher;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 接口参数上的注解签名需要和实现匹配，返回值可以不匹配
 *
 * @author baB_hyf
 * @date 2020/05/12
 */
public interface TeacherServiceI {

	void validField(int id, @NotEmpty String name);

	void validPojo(@Valid @NotNull Student student);

	void validGroup(@Valid Student student);

	void validNested(@Valid Teacher teacher);

	String validReturnValue(String id);

}
