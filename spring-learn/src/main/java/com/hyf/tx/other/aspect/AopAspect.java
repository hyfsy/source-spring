package com.hyf.tx.other.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author baB_hyf
 * @date 2020/04/11
 */
@Aspect
@Component
public class AopAspect {

	@Pointcut("bean(moneyService)")
	public void pc1() {
	}

	@Before("pc1()")
	public void before() {
		System.out.println("---before");
	}

	@AfterReturning(value = "pc1()")
	public void afterReturning() {
		System.out.println("---afterReturning");
	}

	@AfterThrowing(value = "pc1()")
	public void afterThrowing() {
		System.out.println("---afterThrowing");
	}

	@After("pc1()")
	public void after() {
		System.out.println("---after");
	}

	@Around(value = "pc1()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		return pjp.proceed();
	}

}
