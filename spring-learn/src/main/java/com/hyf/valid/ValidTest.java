package com.hyf.valid;

import com.hyf.valid.other.pojo.Student;
import com.hyf.valid.other.pojo.Teacher;
import com.hyf.valid.other.service.TeacherServiceI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.lang.reflect.Method;

/**
 * @author baB_hyftt
 * @date 2020/05/12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ValidConfig.class)
public class ValidTest {

	@Autowired
	private TeacherServiceI teacherService;

	@Autowired
	private Validator validator;

	@Autowired
	private javax.validation.Validator validator4J;

	@Test
	public void testValidField() {
		teacherService.validField(1, null);
	}

	@Test
	public void testPojo() {
		teacherService.validPojo(new Student(1, ""));
	}

	@Test
	public void testGroups() {
		teacherService.validGroup(new Student(2, "王五"));
	}

	@Test
	public void testNested() {
		Student stu = new Student(3, "小欧");
		teacherService.validNested(new Teacher(1, false, "樊老师", stu));
	}

	@Test
	public void testReturnValue() {
	    teacherService.validReturnValue("");
	}

	@Test
	public void testValidatorValid() {
		Student student = new Student(1, "");
		Errors errors = new BeanPropertyBindingResult(student, "student");
		validator.validate(student, errors);
		System.out.println(errors);
	}

	@Test
	public void testAnnotationMethodPointcut() throws Exception {
		Pointcut pointcut = new AnnotationMatchingPointcut(Valid.class, true);

		Method method0 = teacherService.getClass().getMethod("validField", int.class, String.class);
		Method method1 = teacherService.getClass().getMethod("validNested", Teacher.class);
		Method method2 = teacherService.getClass().getMethod("validReturnValue", String.class);

		boolean matchNoMatch = pointcut.getMethodMatcher().matches(method0, TeacherServiceI.class);
		boolean matchParameter = pointcut.getMethodMatcher().matches(method1, TeacherServiceI.class);
		boolean matchReturnValue = pointcut.getMethodMatcher().matches(method2, TeacherServiceI.class);
		System.out.println("matchNoMatch: " + matchNoMatch);
		System.out.println("matchParameter: " + matchParameter);
		System.out.println("matchReturnValue: " + matchReturnValue);
	}
}
