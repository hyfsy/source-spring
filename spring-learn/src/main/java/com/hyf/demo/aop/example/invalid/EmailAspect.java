package com.hyf.demo.aop.example.invalid;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EmailAspect {

	@Before("execution(* com.hyf.demo.aop.example..*.sendEmail(int)) and args(loginId)")
	public void before(int loginId) {
		System.out.printf("send email to %s, current time: %s%n", loginId, System.currentTimeMillis());
	}
}
