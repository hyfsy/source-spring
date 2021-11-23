package com.hyf.demo.aop.example;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

// @Scope("prototype")
@Aspect // ("pertarget(" + A.PC1 + ")")
@Component
public class A {


	public static final String PC1 = "execution(* com.hyf.demo.aop.common..*.*(..))";

	@Pointcut(PC1)
	public void pc1() {}


	// @Before("pc1() && args(id) " +
	// 		"&& @annotation(org.springframework.stereotype.Component) " +
	// 		"&& target(com.hyf.demo.aop.common.UserServiceImpl) " +
	// 		"and this(com.hyf.demo.aop.common.UserService) " +
	// 		"or bean(userService)")
	@Before(PC1)
	public void before() {
		System.out.println("before");
	}


	@After(PC1)
	public void after(JoinPoint jp) {
		System.out.println("after");
	}


	@AfterThrowing(value = PC1, throwing = "ex")
	public void afterThrowing(JoinPoint jp, Throwable ex) {
		ex.printStackTrace();
		System.out.println("after throwing");
	}


	@AfterReturning(value = PC1, returning = "rtn")
	public void afterReturning(JoinPoint jp, Object rtn) {
		System.out.println("after returning: " + rtn);
	}


	@Around(PC1)
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		try {
			System.out.println("around before");
			Object result = pjp.proceed(pjp.getArgs());
			System.out.println("around after returning: " + result);
			return result;
		} catch (Throwable t) {
			System.out.println("around after throwing");
			throw t;
		} finally {
			System.out.println("around after");
		}
	}
}
